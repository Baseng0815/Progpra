package nlp;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map.Entry;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextNormalization {


    static List<String> readData(String filename) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        return reader.lines().toList();
    }

    static HashSet<String> loadStopwords(String filename) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        HashSet<String> result = new HashSet<>(reader.lines().toList());
        System.out.printf("# Stopwords = %d\n", result.size());
        return result;
    }

    static ArrayList<String> tokenize(String datapoint) {
        /* the regex matches whitespace, date, phrases separated by '-' and numbers */
        //Pattern pattern = Pattern.compile("\\s+|[1-3]\\d.[0,1][1-9].\\d{4}|(\\S+-)*\\S+|\\d+[,.]\\d+|[.,:;?!\\\\[\\\\]]'");
        Pattern date = Pattern.compile("^\\d{1,2}.\\d{1,2}.(\\d{2}|\\d{4})$");
        Pattern number = Pattern.compile("^\\d+[.,]\\d+$");

        ArrayList<String> ts = new ArrayList<String>(Arrays.stream(datapoint.split("\s")).toList());
        ArrayList<String> result = new ArrayList<String>();
        for (String t : ts) {
            Matcher dateMatcher = date.matcher(t);
            Matcher numberMatcher = number.matcher(t);
            if (dateMatcher.matches()) {
                /* date (keep together) */
                result.add(t);
            } else if (numberMatcher.matches()) {
                /* number (keep together) */
                result.add(t);
            } else {
                /* something different (split after delimiters except -) */
                StringTokenizer tokenizer = new StringTokenizer(t, ",.:;?![]'", true);
                while (tokenizer.hasMoreTokens()) {
                    result.add(tokenizer.nextToken());
                }
            }
        }

        System.out.println(result);
        return result;
    }

    static HashMap<String, Integer> countTokens(ArrayList<ArrayList<String>> train_data) {
        HashMap<String, Integer> result = new HashMap<>();
        for (ArrayList<String> instance : train_data) {
            for (String token : instance) {
                if (!result.containsKey(token)) {
                    result.put(token, 1);
                } else {
                    result.put(token, result.get(token) + 1);
                }
            }
        }

        return result;
    }

    static Integer getTotalTokens(Map<String, Integer> tokens) {
        return tokens.values()
                .stream()
                .reduce(0, Integer::sum);
    }

    static Integer getVocabSize(Map<String, Integer> tokens) {
        return tokens.size();
    }

    static ArrayList<String> removeStopwords(HashSet<String> stopwords, ArrayList<String> tokens) {
        return new ArrayList<>(tokens.stream().filter(e -> !stopwords.contains(e)).toList());
    }

    static SortedMap<String, Integer> sortTokens(HashMap<String, Integer> map) {
        SortedMap<String, Integer> result = new TreeMap<String, Integer>(Comparator.comparing(map::get).reversed());
        result.putAll(map);
        return result;
    }

    static void writeCorpus(List<String> data, String title, boolean removeStopwords, BufferedWriter writer) throws IOException {
        writer.write(title + "\n");
        writer.write("---------------------------------\n");

        ArrayList<ArrayList<String>> trainData = new ArrayList<>();
        HashSet<String> stopwords = loadStopwords("Aufgaben/NLP/text_normalization_data/nltk_german_stopwords");
        for (String s : data) {
            ArrayList<String> tokens = tokenize(s);
            if (removeStopwords)
                tokens = removeStopwords(stopwords, tokens);
            trainData.add(tokens);
        }

        HashMap<String, Integer> frequencies = countTokens(trainData);
        int tokenCount = getTotalTokens(frequencies);
        int vocabSize = getVocabSize(frequencies);
        writer.write("Tokens      " + tokenCount + "\n");
        writer.write("Vocabulary  " + vocabSize + "\n");
        writer.newLine();

        SortedMap<String, Integer> sorted = sortTokens(frequencies);
        writer.write("Top 10 most frequent tokens:\n");
        int i = 0;
        for (Map.Entry<String, Integer> entry : sorted.entrySet()) {
            if (i >= 10)
                break;

            writer.write(entry.getKey() + ": " + entry.getValue() + "\n");

            i++;
        }

        writer.newLine();
    }

    public static void main(String[] args) throws IOException {

        /* TODO: Read in the data from the file at filename */
        List<String> data = readData("Aufgaben/NLP/text_normalization_data/test.txt");

        /*
        String test_string1 = "Mitarbeiter eines Wachdienstes entdeckten den Sprengsatz gegen 1.15 Uhr, wie die Polizei mitteilte.";
        System.out.println(tokenize(test_string1));

        String test_string2 = "Ende 2015 war das BIP im Quartalsabstand lediglich um 0,3 Prozent und im Jahresabstand um 1,0 Prozent gewachsen.";
        System.out.println(tokenize(test_string2));

        String test_string3 = "Kinder-Faschingskostüm „Flüchtling - 1./2. Weltkrieg“";
        System.out.println(tokenize(test_string3));

        String test_string4 = "This is a test string with a date 2.2.2020 and a big number 2,310 and ends with another number 1.1.";
        System.out.println(tokenize(test_string4));
         */

        BufferedWriter writer = new BufferedWriter(new FileWriter("corpus"));
        writeCorpus(data, "Corpus information with stopwords included", false, writer);
        writeCorpus(data, "Corpus information after removing stopwords", true, writer);
        writer.close();
    }
}
