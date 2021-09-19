package bioinformatik.aufgabe2;

import bioinformatik.aminoacid.AminoAcid;
import bioinformatik.aufgabe1.BioBasicsParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenomeDownloader {

    private final BioBasicsParser bioBasicsParser = new BioBasicsParser();
    private final String baseUrl = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/";

    /**
     * Returns the NCBI genome ID, given a specific query string.
     * @param searchStrings The query terms. Will be concatenated with '+'.
     *                      Example: ["escherichia", "coli"] -> "escherichia+coli"
     * @return The genome ID.
     * @throws IOException
     */
    public String parseGenomeId(String... searchStrings) throws IOException {
        URL obj = new URL(baseUrl +
                String.format(
                        "esearch.fcgi?db=genome&term=%s",
                        String.join("+", searchStrings)));
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        InputStreamReader urlStream = new InputStreamReader(obj.openStream());
        BufferedReader in = new BufferedReader(urlStream);
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            int start   = inputLine.indexOf("<Id>") + 4;
            int end     = inputLine.indexOf("</Id>", start);
            if (start != -1 && end != -1) {
                return inputLine.substring(start, end);
            }
        }
        in.close();
        return "";
    }

    /**
     * Returns the NCBI sequence ID for the given genome ID.
     * @param id The genome ID.
     * @return The sequence ID.
     * @throws IOException
     */
    public List<String> parseSequenceId(String id) throws IOException {
        URL obj = new URL(baseUrl +
                String.format("elink.fcgi?dbfrom=genome&db=nuccore&id=%s&term=srcdb+refseq[prop]", id));
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        InputStreamReader urlStream = new InputStreamReader(obj.openStream());
        BufferedReader in = new BufferedReader(urlStream);
        List<String> sequenceIds = new ArrayList<>();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            int start   = inputLine.indexOf("<Id>") + 4;
            int end     = inputLine.indexOf("</Id>", start);
            if (start != -1 && end != -1) {
                String foundId = inputLine.substring(start, end);
                if (foundId.compareTo(id) != 0) {
                    sequenceIds.add(inputLine.substring(start, end));
                }
            }
        }
        in.close();
        return sequenceIds;
    }

    /**
     * Gets the actual sequence for a given sequence ID.
     * @param id The sequence ID.
     * @return A map with actual sequence ("seq") and the meta information ("info")
     * @throws IOException
     */
    public Map<String, String> getSequence(String id) throws IOException {
        URL obj = new URL("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nuccore&id=" + id + "&rettype=fasta&retmode=text");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        InputStreamReader urlStream = new InputStreamReader(obj.openStream());
        BufferedReader in = new BufferedReader(urlStream);
        String inputLine;
        String metadata = "", genomeSequence = "";
        while ((inputLine = in.readLine()) != null) {
            if (inputLine.startsWith(">")) {
                /* metadata comment */
                metadata = new String(inputLine);
            } else {
                /* part of genome sequence */
                genomeSequence = genomeSequence.concat(inputLine);
            }
        }
        Map<String, String> res = new HashMap<>();
        res.put("info", metadata);
        res.put("seq", genomeSequence);
        in.close();
        return res;
    }
}
