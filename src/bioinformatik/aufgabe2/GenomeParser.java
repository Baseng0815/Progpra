package bioinformatik.aufgabe2;

import java.util.*;

public class GenomeParser {

    private final String genome;
    private Map<String, String> codonWheel;
    private Map<String, List<String>> aminoAcidWheel;

    public GenomeParser(String genome) {
        this.genome = genome;
        this.codonWheel = this.initCodonWheel();
        this.aminoAcidWheel = this.initAminoAcidWheel();
    }

    /**
     * Returns the codons (such as "ATG") from the given sequence.
     *
     * @param seq The sequence which shall return all codons.
     * @return A list with all codons.
     */
    public List<String> getCodons(String seq) {
        List<String> codons = new ArrayList<>();
        while (seq.length() >= 3) {
            codons.add(seq.substring(0, 3));
            seq = seq.substring(3);
        }
        return codons;
    }

    /**
     * Returns the respective amino acid for a given triplet (a codon such as "TTA").
     *
     * @param triplet The query codon, such as "TTA".
     * @return The amino acid.
     */
    public String getAminoAcid(String triplet) {
        return codonWheel.get(triplet);
    }

    /**
     * Returns all codons for the given amino acid.
     *
     * @param aminoAcid The amino acid.
     * @return A list with all possible codons of the query amino acid.
     */
    public List<String> getCodon(String aminoAcid) {
        return aminoAcidWheel.get(aminoAcid);
    }

    /**
     * Initializes the amino acid wheel, which maps amino acids to codons.
     *
     * @return The initialized amino acid wheel.
     */
    private Map<String, List<String>> initAminoAcidWheel() {
        Map<String, List<String>> aaW = new HashMap<>();
        Map<String, String> cw = initCodonWheel();
        cw.forEach((key, value) -> {
            if (!aaW.containsKey(value))
                aaW.put(value, new ArrayList<String>());

            aaW.get(value).add(key);
        });

        return aaW;
    }

    /**
     * Initializes the codon wheel, which maps codons to the respective amino acid.
     *
     * @return The initialized codon wheel.
     */
    private Map<String, String> initCodonWheel() {
        Map<String, String> cw = new HashMap<>();
        cw.put("CAT", "H");
        cw.put("CAC", "H");
        cw.put("CAA", "Q");
        cw.put("CAG", "Q");
        cw.put("CCT", "P");
        cw.put("CCC", "P");
        cw.put("CCA", "P");
        cw.put("CCG", "P");
        cw.put("CGT", "R");
        cw.put("CGC", "R");
        cw.put("CGA", "R");
        cw.put("CGG", "R");
        cw.put("CTT", "L");
        cw.put("CTC", "L");
        cw.put("CTA", "L");
        cw.put("CTG", "L");
        cw.put("GAT", "D");
        cw.put("GAC", "D");
        cw.put("GAA", "E");
        cw.put("GAG", "E");
        cw.put("GCT", "A");
        cw.put("GCC", "A");
        cw.put("GCA", "A");
        cw.put("GCG", "A");
        cw.put("GGT", "G");
        cw.put("GGC", "G");
        cw.put("GGA", "G");
        cw.put("GGG", "G");
        cw.put("GTT", "V");
        cw.put("GTC", "V");
        cw.put("GTA", "V");
        cw.put("GTG", "V");
        cw.put("TAT", "Y");
        cw.put("TAC", "Y");
        cw.put("TAA", "*");
        cw.put("TAG", "*");
        cw.put("TCT", "S");
        cw.put("TCC", "S");
        cw.put("TCA", "S");
        cw.put("TCG", "S");
        cw.put("TGT", "C");
        cw.put("TGC", "C");
        cw.put("TGA", "*");
        cw.put("TGG", "W");
        cw.put("TTT", "F");
        cw.put("TTC", "F");
        cw.put("TTA", "L");
        cw.put("TTG", "L");
        cw.put("AAT", "N");
        cw.put("AAC", "N");
        cw.put("AAA", "K");
        cw.put("AAG", "K");
        cw.put("ACT", "T");
        cw.put("ACC", "T");
        cw.put("ACA", "T");
        cw.put("ACG", "T");
        cw.put("AGT", "S");
        cw.put("AGC", "S");
        cw.put("AGA", "R");
        cw.put("AGG", "R");
        cw.put("ATT", "I");
        cw.put("ATC", "I");
        cw.put("ATA", "I");
        cw.put("ATG", "M");
        return cw;
    }

}
