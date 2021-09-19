package bioinformatik.aufgabe1;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BioBasicsParser {

    private List<String> content;
    private List<String> wikiContent;

    /**
     * Load the whole file into memory.
     *
     * @param file A path object to the actual file.
     * @throws IOException
     */
    public void loadEntireFile(Path file) throws IOException {
        this.content = Files.lines(file).toList();
    }

    /**
     * Read a file line by line. Recommended way for large files.
     *
     * @param file A path object to the actual file
     * @throws IOException
     */
    public void loadFileBuffered(Path file) throws IOException {
        BufferedReader br = Files.newBufferedReader(file);
        this.content = br.lines().toList();
    }

    /**
     * Retrieves an entire page and saves it in a string.
     *
     * @param url An URL object to the web page.
     * @throws IOException
     */
    public void loadWikiPage(URL url) throws IOException {
        InputStreamReader urlStream =
                new InputStreamReader(url.openStream());
        BufferedReader in = new BufferedReader(urlStream);
        this.wikiContent = new ArrayList<>();
        in.lines().forEach(line -> {
            String withoutTags = replaceTags(line);
            if (!withoutTags.isEmpty())
                wikiContent.add(replaceTags(line));
        });
        in.close();
    }

    /**
     * Accepts a keyword and returns this words subsequent content.
     *
     * @param keyword The word to look for.
     * @param length  The length of the subsequent content
     * @param useFile Use a file as source or a web page. If false, web page needs to be present.
     * @return Returns the specified keywords subsequent content.
     */
    public List<String> getInfoFor(String keyword, int length, boolean useFile) {
        List<String> source =
                useFile ? this.content : this.wikiContent;
        List<String> hits = new ArrayList<>();
        String content = source.stream().map(line -> line + " ").reduce("", String::concat);
        int index = 0;
        while ((index = content.indexOf(keyword, index)) != -1) {
            hits.add(content.substring(index, Math.min(index + length, content.length())));
            index += keyword.length();
        }

        return hits;
    }

    public static void main(String[] args) throws IOException {
        BioBasicsParser parser = new BioBasicsParser();
        parser.loadWikiPage(new URL("https://en.wikipedia.org/wiki/Meltdown_(security_vulnerability"));
        parser.loadFileBuffered(new File("Aufgaben/Bioinformatik/genom.txt").toPath());
        parser.getInfoFor("Genom", 80, true).forEach(System.out::println);
        System.out.println("------------------------------------------------------");
        parser.getInfoFor("Codierter Abschnitt", 80, true).forEach(System.out::println);
        System.out.println("------------------------------------------------------");
        parser.getInfoFor("Gen", 80, true).forEach(System.out::println);
        System.out.println("------------------------------------------------------");
        parser.getInfoFor("Basen", 80, true).forEach(System.out::println);
        System.out.println("------------------------------------------------------");
        parser.getInfoFor("privilege", 80, false).forEach(System.out::println);
    }

    /**
     * Takes a string and replaces all HTML tags.
     *
     * @param tag AN HTML tag, such as '&#60;button&#62;Klick mich!&#60;/button&#62;'.
     * @return The purified string.
     */
    public String replaceTags(String tag) {
        int begin;
        while ((begin = tag.indexOf('<')) != -1) {
            int end = tag.indexOf('>', begin);
            if (end == -1)
                return tag;

            tag = tag.substring(0, begin) + tag.substring(end + 1);
        }

        tag = tag.trim();
        return tag;
    }
}
