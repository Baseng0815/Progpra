package nlp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class SentimentAnalysis {

    static final int NUM_STOPWORDS = 100;

    static List<String> read_data_dir(String folder) {

        List<String> data = new ArrayList<>();
        File dir_path = new File(folder);
        String contents[] = dir_path.list();

        for(int i=0; i<contents.length; i++) {
            if (!contents[i].endsWith(".txt")) {
                continue;
            }
            try {
                File myObj = new File(folder + "/" + contents[i]);
                Scanner myReader = new Scanner(myObj);  
                while (myReader.hasNextLine()) {
                    String text = myReader.nextLine();
                    data.add(text);
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("File not found.");
                e.printStackTrace();
            }
        }
        return data;
    }

    static List<String> get_stopwords(HashMap<String, Integer> pos_token_counts, 
                                      HashMap<String, Integer> neg_token_counts) {

        ArrayList<String> stopwords = new ArrayList<String>();
        HashMap<String, Integer> merged_map = new HashMap<String, Integer>();
        for (Map.Entry<String, Integer> set : pos_token_counts.entrySet()) {
            String key = set.getKey();
            if (merged_map.containsKey(key)) {
                merged_map.put(key, set.getValue() + merged_map.get(key));
            } else {
                merged_map.put(key, set.getValue());
            }
        }

        // Find the largest words
        Map<String, Integer> sorted_map = sortByValue(merged_map);
        int total_words = 0;
        for (Entry<String, Integer> entry : sorted_map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
            stopwords.add(entry.getKey());
            total_words += 1;
            if (total_words > NUM_STOPWORDS) {
                break;
            }
        }
        return stopwords;
    }

    static Map<String, Integer> sortByValue(HashMap<String, Integer> map) {
        //convert HashMap into List   
        List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(map.entrySet());  
        //sorting the list elements  
        Collections.sort(list, new Comparator<Entry<String, Integer>>() {  
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        //prints the sorted HashMap  
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();  
        for (Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());  
        }
        return sortedMap;
    }

    static HashMap<String, Integer> countTokens(List<String> train_data, HashSet<String> vocabulary) {
        HashMap<String, Integer> token_counts = new HashMap<String, Integer>();

        for (String datapoint : train_data) {

            StringTokenizer tokenizer = new StringTokenizer(datapoint.toLowerCase(), " \t\n\r\f,.:;?![]'");
            int num_tokens = tokenizer.countTokens();

            for (int i=0; i<num_tokens; i++) {
                String token = tokenizer.nextToken();
                if (token_counts.containsKey(token)) {
                    token_counts.put(token, token_counts.get(token) + 1);
                }
                else {
                    token_counts.put(token, 1);
                    vocabulary.add(token);
                }
            }

        }
        return token_counts;
    }


    static HashMap<String, Float> computeProbabilities(HashMap<String, Integer> token_counts, 
                                                       HashSet<String> vocabulary) {
        float denom = TextNormalization.getTotalTokens(token_counts) + 1;

        HashMap<String, Float> probabilities = new HashMap<>();
        for (String token : vocabulary) {
            int count = token_counts.getOrDefault(token, 0);
            probabilities.put(token, (count + 1) / denom);
        };

        return probabilities;
    }


    static void predictSentiment(List<String> instances,
                                HashSet<String> vocabulary,
                                HashMap<String, Float> probabilities_pos,
                                HashMap<String, Float> probabilities_neg,
                                List<String> predictions,
                                List<String> ground_truth,
                                String instance_type,
                                List<String> stopwords) {

        for (String datapoint : instances) {
            /* preprocess string */
            ArrayList<String> tokens = TextNormalization.tokenize(datapoint);
            tokens = TextNormalization.removeStopwords(new HashSet<>(stopwords), tokens);

            /* compute probabilities */
            float prob_pos = 1f;
            float prob_neg = 1f;
            for (String token : tokens) {
                prob_pos += Math.log(probabilities_pos.getOrDefault(token, 1.f));
                prob_neg += Math.log(probabilities_neg.getOrDefault(token, 1.f));
            }

            if (prob_pos > prob_neg) {
                predictions.add("positive");
            } else {
                predictions.add("negative");
            }

            ground_truth.add(instance_type);
        }
    }


    public static void main(String[] args) { 
        /* read in the data */
        List<String> train_pos = read_data_dir("Aufgaben/NLP/sentiment_data/train/pos");
        List<String> train_neg = read_data_dir("Aufgaben/NLP/sentiment_data/train/neg");
        HashSet<String> vocabulary = new HashSet<String>();

        /* Count Tokens */
        HashMap<String, Integer> pos_token_counts = countTokens(train_pos, vocabulary);
        HashMap<String, Integer> neg_token_counts = countTokens(train_neg, vocabulary);

        /*
            Compute Probabilities 
        */
        HashMap<String, Float> probabilities_pos = computeProbabilities(pos_token_counts, vocabulary);
        HashMap<String, Float> probabilities_neg = computeProbabilities(neg_token_counts, vocabulary);

        /* read in the test data */
        List<String> test_pos = read_data_dir("Aufgaben/NLP/sentiment_data/test/pos");
        List<String> test_neg = read_data_dir("Aufgaben/NLP/sentiment_data/test/neg");
        List<String> predictions = new ArrayList<String>();
        List<String> ground_truth = new ArrayList<String>();

        /* get stopwords */
        List<String> stopwords = get_stopwords(pos_token_counts, neg_token_counts);

        predictSentiment(test_pos, vocabulary, probabilities_pos, probabilities_neg, predictions, ground_truth, "positive", stopwords);
        predictSentiment(test_neg, vocabulary, probabilities_pos, probabilities_neg, predictions, ground_truth, "negative", stopwords);

        float random_guess = 0;
        if (test_pos.size() > test_neg.size()) {
            random_guess = test_pos.size() *1f / (test_pos.size() + test_neg.size());
        } else {
            random_guess = test_neg.size() *1f / (test_pos.size() + test_neg.size());
        }

        /* evaluate predictions */
        float accuracy = getAccuracy(predictions, ground_truth);
        System.out.println("Accuracy from random guessing: " + random_guess);
        System.out.println("Accuracy: " + accuracy);
    }

    static float getAccuracy(List<String> predictions, List<String> ground_truth) {
        int correct = 0;
        for (int i = 0; i < predictions.size(); i++) {
            if (predictions.get(i).compareTo(ground_truth.get(i)) == 0)
                correct++;
        }
        return correct / (float)predictions.size();
    }
}
