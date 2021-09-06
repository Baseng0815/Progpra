package dbs;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamingJavaa {
    // Aufgabe 2) a)
    public static <E> Stream<E> flatStreamOf(List<List<E>> list) {
        return list.stream().flatMap(Collection::stream);
    }

    // Aufgabe 2) b)
    public static <E> Stream<E> mergeStreamsOf(Stream<Stream<E>> stream) {
        return stream.reduce(Stream.empty(), Stream::concat);
    }

    // Aufgabe 2) c)
    public static <E extends Comparable<? super E>> E minOf(List<List<E>> list) {
        return flatStreamOf(list).parallel().min(E::compareTo).get(); // throws
        // NoSuchElementException
    }

    // Aufgabe 2) d)
    public static <E> E lastWithOf(Stream<E> stream,
                                   Predicate<? super E> predicate) {
        return stream.filter(predicate).reduce((acc, cur) -> cur).get();
    }

    // Aufgabe 2) e)
    public static <E> Set<E> findOfCount(Stream<E> stream, int count) {
        return stream.collect(Collectors.groupingBy(t -> t)).values().stream()
                .filter(l -> l.size() == count)
                .map(l -> l.get(0))
                .collect(Collectors.toSet());
    }

    // Aufgabe 2) f)
    public static IntStream makeStreamOf(String[] strings) {
        return Arrays.stream(strings).flatMapToInt(String::chars);
    }

//-------------------------------------------------------------------------------------------------

    // Aufgabe 3) a)
    public static Stream<String> fileLines(String path) throws IOException {
        return Files.newBufferedReader(Path.of(path)).lines()
                .skip(1)
                .onClose(() -> System.out.println("closed"));
    }

    // Aufgabe 3) b)
    public static double averageCost(Stream<String> lines) {
        return lines
                .mapToDouble(line -> Double.parseDouble(line.split(",")[12]))
                .average().getAsDouble();
    }

    // Aufgabe 3) c)
    public static long countCleanEnergyLevy(Stream<String> stream) {
        return stream
                .map(line -> line.split(",")[10])
                .filter(entry -> entry.isEmpty() || Double.parseDouble(entry) == 0)
                .count();
    }

    record NaturalGasBilling(String invoiceDate, String fromDate, String toDate,
                             int billingDay, double billedGJ, double basicCharge,
                             double deliveryCharge, double storage,
                             double commodityCharges,
                             double tax, double cleanEnergyLevy, double carbonTax,
                             double amount) {

        Stream<Byte> toBytes() {
            return String.format("%s,%s,%s,%d,%f,%f,%f,%f,%f,%f,%f,%f,%f\n",
                    invoiceDate, fromDate, toDate, billingDay, billedGJ, basicCharge, deliveryCharge,
                    storage, commodityCharges, tax, cleanEnergyLevy, carbonTax, amount)
                    .chars().mapToObj(i -> (byte) i);
        }
    }

    public static Stream<Byte> serialize(Stream<NaturalGasBilling> stream) {
        return stream.flatMap(NaturalGasBilling::toBytes);
    }
    public static Stream<NaturalGasBilling> orderByInvoiceDateDesc(Stream<String> stream) {
        return stream
                .map(line -> line.replaceAll(",,", ",0,"))
                .map(line -> line.split(","))
                .map(list -> new NaturalGasBilling(
                        list[0],
                        list[1],
                        list[2],
                        Integer.parseInt(list[3]),
                        Double.parseDouble(list[4]),
                        Double.parseDouble(list[5]),
                        Double.parseDouble(list[6]),
                        Double.parseDouble(list[7]),
                        Double.parseDouble(list[8]),
                        Double.parseDouble(list[9]),
                        Double.parseDouble(list[10]),
                        Double.parseDouble(list[11]),
                        Double.parseDouble(list[12])))
                .sorted(Comparator.comparing(NaturalGasBilling::invoiceDate).reversed());
    }


    public static void main(String[] args) throws IOException {
        BufferedWriter br = Files.newBufferedWriter(Path.of(
                "NaturalGasBillingOrdered.csv"));
        br.write("Invoice Date,From Date,To Date,Billing Days,Billed GJ,Basic " +
                "charge,Delivery charges,Storage and transport,Commodity charges," +
                "Tax,Clean energy levy,Carbon tax,Amount");
        serialize(orderByInvoiceDateDesc(fileLines("Aufgaben/Datenbanken/NaturalGasBilling.csv")))
                .forEach(c -> {
                    try {
                        br.write(c);
                    } catch (IOException ignored) {}
                });
        br.close();
    }

    // Aufgabe 3) d)
    // TODO:
    //  1. Create record "NaturalGasBilling".
    //  2. Implement static method: "Stream<NaturalGasBilling>
    //  orderByInvoiceDateDesc(Stream<String> stream)".

    // Aufgabe 3) e)
    // TODO: Implement object method: "Stream<Byte> toBytes()" for record
    //  "NaturalGasBilling".

    // Aufgabe 3) f)
    // TODO: Implement static method: "Stream<Byte> serialize
    //  (Stream<NaturalGasBilling> stream)".

    // Aufgabe 3) g)
    // TODO: Implement static method: "Stream<NaturalGasBilling> deserialize
    //  (Stream<Byte> stream)".
    // TODO: Execute the call: "deserialize(serialize(orderByInvoiceDateDesc
    //  (fileLines(Datei aus f))))"
    // TODO: in a main Method and print the output to the console.

    // Aufgabe 3) h)
    public static Stream<File> findFilesWith(String dir, String startsWith,
                                             String endsWith, int maxFiles) {
        // TODO

        return null;
    }
}
