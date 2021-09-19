package dbs;

import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.*;

public class StreamingJava {
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
        return flatStreamOf(list).parallel().min(E::compareTo).get();
    }

    // Aufgabe 2) d)
    public static <E> E lastWithOf(Stream<E> stream, Predicate<? super E> predicate) {
        return stream.filter(predicate).reduce((acc, elem) -> elem).get();
    }

    // Aufgabe 2) e)
    public static <E> Set<E> findOfCount(Stream<E> stream, int count) {
        return stream
                .collect(Collectors.groupingBy(e -> e))
                .values().stream()
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
        BufferedReader reader = Files.newBufferedReader(Path.of(path));
        return reader.lines().sequential().skip(1).onClose(() -> System.out.println("Line stream closed."));
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
                .filter(line -> {
                    String s = line.split(",")[10];
                    return s.isEmpty() || Double.parseDouble(s) == 0;
                })
                .count();
    }

    record NaturalGasBilling(long invoiceDate, long fromDate, long toDate,
                             long billingDay, double billedGJ, double basicCharge,
                             double deliveryCharge, double storage, double commodityCharges,
                             double tax, double cleanEnergyLevy, double carbonTax, double amount) {
        public String toString() {
            return String.format("%d,%d,%d,%d,%f,%f,%f,%f,%f,%f,%f,%f,%f\n",
                    invoiceDate, fromDate, toDate, billingDay, billedGJ, basicCharge, deliveryCharge,
                    storage, commodityCharges, tax, cleanEnergyLevy, carbonTax, amount);
        }

        public Stream<Byte> toBytes() {
            return this.toString().chars().mapToObj(e -> (byte) e);
        }
    }

    public static Stream<NaturalGasBilling> orderByInvoiceDateDesc(Stream<String> stream) {
        return stream
                .map(line -> {
                    String[] split = line.split(",");
                    NaturalGasBilling a = new NaturalGasBilling(
                            LocalDate.parse(split[0]).toEpochSecond(LocalTime.NOON, ZoneOffset.UTC), /* dates */
                            LocalDate.parse(split[1]).toEpochSecond(LocalTime.NOON, ZoneOffset.UTC), /* dates */
                            LocalDate.parse(split[2]).toEpochSecond(LocalTime.NOON, ZoneOffset.UTC), /* dates */
                            Long.parseLong(split[3]),     /* billingDay */
                            Double.parseDouble(split[4]),   /* billedGJ */
                            Double.parseDouble(split[5]),   /* basicCharge */
                            Double.parseDouble(split[6]),   /* deliveryCharge */
                            Double.parseDouble(split[7]),   /* storage */
                            Double.parseDouble(split[8]),   /* commodityCharges */
                            Double.parseDouble(split[9]),   /* tax */
                            split[10].isEmpty() ? 0.0 : Double.parseDouble(split[10]), /* cleanEnergyLevy (might be empty) */
                            Double.parseDouble(split[11]),   /* carbonTax */
                            Double.parseDouble(split[12])   /* amount */
                    );
                    return a;
                })
                .sorted(Comparator.comparing(NaturalGasBilling::invoiceDate).reversed());
    }

    public static Stream<Byte> serialize(Stream<NaturalGasBilling> stream) {
        return Stream.concat("Invoice Date,From Date,To Date,Billing Days,Billed GJ,Basic charge,Delivery charges,Storage and transport,Commodity charges,Tax,Clean energy levy,Carbon tax,Amount\n"
                .chars().mapToObj(c -> (byte) c), stream.flatMap(NaturalGasBilling::toBytes));
    }

    public static Stream<NaturalGasBilling> deserialize(Stream<Byte> stream) {
        /* we need to convert the stream into an array, do work on the array and then convert it back to a stream
         * 1) streams are exhausted once we finish a terminal operation
         * 2) every stream pipeline needs to have a terminal operation
         * 3) variables outside the stream pipeline can't be modified
         * 4) we can't get individual bytes without exhausting the stream
         * => there's no way to do this without an extra array
         */

        String[] lines = stream
                .map(Character::toString)
                .collect(Collectors.joining())
                .split("\n");

        if (lines.length == 0 || lines[0].isEmpty())
            return Stream.empty();

        if (lines[0].charAt(0) == 'I') {
            /* title */
            stream = Arrays.stream(lines).map(str -> str + "\n").flatMapToInt(String::chars).mapToObj(c -> (byte)c);
            stream = stream.skip(lines[0].length() + 1);
            return deserialize(stream);
        } else {
            /* data */
            String[] split = lines[0].split(",");
            NaturalGasBilling record = new NaturalGasBilling(
                    Long.parseLong(split[0]),       /* dates */
                    Long.parseLong(split[1]),       /* dates */
                    Long.parseLong(split[2]),       /* dates */
                    Long.parseLong(split[3]),       /* billingDay */
                    Double.parseDouble(split[4]),   /* billedGJ */
                    Double.parseDouble(split[5]),   /* basicCharge */
                    Double.parseDouble(split[6]),   /* deliveryCharge */
                    Double.parseDouble(split[7]),   /* storage */
                    Double.parseDouble(split[8]),   /* commodityCharges */
                    Double.parseDouble(split[9]),   /* tax */
                    split[10].isEmpty() ? 0.0 : Double.parseDouble(split[10]), /* cleanEnergyLevy (might be empty) */
                    Double.parseDouble(split[11]),   /* carbonTax */
                    Double.parseDouble(split[12])    /* amount */
            );
            stream = Arrays.stream(lines).map(str -> str + "\n").flatMapToInt(String::chars).mapToObj(c -> (byte)c);
            stream = stream.skip(lines[0].length() + 1);
            return Stream.concat(Stream.of(record), deserialize(stream));
        }
    }

    public static void main(String[] args) throws IOException {
        // Aufgabe 3) d)
        // TODO:
        //  1. Create record "NaturalGasBilling".
        //  2. Implement static method: "Stream<NaturalGasBilling> orderByInvoiceDateDesc(Stream<String> stream)".
        Stream<NaturalGasBilling> records = orderByInvoiceDateDesc(fileLines("Aufgaben/Datenbanken/NaturalGasBilling.csv"));
        System.out.println("Printing records (ordered)...");
        records.forEach(System.out::print);

        // Aufgabe 3) e)
        // TODO: Implement object method: "Stream<Byte> toBytes()" for record "NaturalGasBilling".

        // Aufgabe 3) f)
        // TODO: Implement static method: "Stream<Byte> serialize(Stream<NaturalGasBilling> stream)".
        DataOutputStream out = new DataOutputStream(new FileOutputStream(
                "NaturalGasBillingOrdered.csv"));
        serialize(orderByInvoiceDateDesc(fileLines("Aufgaben/Datenbanken/NaturalGasBilling.csv")))
                .forEach(c -> {
                    try {
                        out.writeByte(c);
                    } catch (IOException ignored) {}
                });
        out.close();

        // Aufgabe 3) g)
        // TODO: Implement static method: "Stream<NaturalGasBilling> deserialize(Stream<Byte> stream)".
        // TODO: Execute the call: "deserialize(serialize(orderByInvoiceDateDesc(fileLines(Datei aus f))))"
        // TODO: in a main Method and print the output to the console.
        System.out.println("Serializing-deserializing data...");
        deserialize(serialize(orderByInvoiceDateDesc(fileLines("Aufgaben/Datenbanken/NaturalGasBilling.csv")))).forEach(System.out::print);

        findFilesWith("/usr/lib", "", "", 10).forEachOrdered(f -> System.out.println(f.length() + " " + f));
    }

    // Aufgabe 3) h)
    public static Stream<File> findFilesWith(String dir, String startsWith, String endsWith, int maxFiles) throws IOException {
        return Files.walk(Path.of(dir))
                .parallel()
                .map(Path::toFile)
                .filter(f -> f.getName().startsWith(startsWith) && f.getName().endsWith(endsWith))
                .sorted(Comparator.comparing(File::length).reversed())
                .limit(maxFiles);
    }
}
