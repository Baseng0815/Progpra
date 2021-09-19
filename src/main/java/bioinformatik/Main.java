package bioinformatik;

import bioinformatik.aminoacid.*;
import bioinformatik.aufgabe2.GenomeDownloader;
import bioinformatik.aufgabe2.GenomeParser;
import bioinformatik.aufgabe2.GenomeParserOO;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        try {
            GenomeDownloader genomeDownloader = new GenomeDownloader();

            String id = genomeDownloader.parseGenomeId("escherichia", "coli");

            List<String> sequenceIds = genomeDownloader.parseSequenceId(id);

            Map<String, String> res = genomeDownloader.getSequence("38638184");
            res.forEach((key, value) -> {
                System.out.printf("%s: %s\n", key, value);
            });
            GenomeParser genomeParser = new GenomeParser(res.get("seq"));

            System.out.println("GenomeParser getAminoAcid()");
            String aminoAcid = genomeParser.getAminoAcid("AGC");
            System.out.println(aminoAcid);

            System.out.println("GenomeParser getCodon()");
            List<String> triplets = genomeParser.getCodon("G");
            System.out.println(triplets);

            GenomeParserOO genomeParserOO = new GenomeParserOO(res.get("seq"));

            System.out.println("GenomeParserOO getAminoAcid()");
            Serine s = (Serine) genomeParserOO.getAminoAcid("AGC");
            System.out.println(s.getOneLetterName());

            System.out.println("GenomeParserOO getCodons()");
            List<String> codons = genomeParser.getCodons(res.get("seq"));
            System.out.println(codons);

            System.out.println("GenomeParserOO getCodons()");
            List<AminoAcid> aminoAcids = genomeParserOO.getCodons(res.get("seq"));
            System.out.println(aminoAcids);

            System.out.println("GenomeParserOO getCodingRegions()");
            List<String> codingRegions = genomeParserOO.getCodingRegions(aminoAcids);
            System.out.println(codingRegions);

            System.out.println("GenomeParserOO getLengthDistribution()");
            System.out.println(genomeParserOO.getLengthDistribution(codingRegions, 15, 20));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




}
