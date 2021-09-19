package bioinformatik.aufgabe2;

import bioinformatik.aminoacid.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GenomeParserOO {

    private final String genome;
    private Map<String, AminoAcid> codonWheel;

    public GenomeParserOO(String genome) {
        this.genome = genome;
        this.codonWheel = this.initCodonWheel();
    }

    /**
     * Finds all coding regions within the given list of amino acid objects.
     * @param aminoAcids A list of amino acid objects.
     * @return A list of all found coding regions.
     */
    public List<String> getCodingRegions(List<AminoAcid> aminoAcids) {
        List<String> codingRegions = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        boolean inSequence = false;
        for (AminoAcid acid : aminoAcids) {
            if (builder.isEmpty() && !acid.getOneLetterName().equals("M"))
                continue;

            builder.append(acid.getOneLetterName());
            if (acid.getOneLetterName().equals("*")) {
                codingRegions.add(builder.toString());
                builder = new StringBuilder();
            }
        }

        return codingRegions;
    }

    /**
     * Returns the codons (such as "ATG") from the given sequence.
     * @param seq The sequence which shall return all codons.
     * @return A list with all codons.
     */
    public List<AminoAcid> getCodons(String seq) {
        List<AminoAcid> codons = new ArrayList<>();
        while (seq.length() >= 3) {
            codons.add(getAminoAcid(seq.substring(0, 3)));
            seq = seq.substring(3);
        }
        return codons;
    }

    /**
     * Returns the respective amino acid object for a given triplet (a codon such as "TTA").
     * @param triplet The query codon, such as "TTA".
     * @return The amino acid.
     */
    public AminoAcid getAminoAcid(String triplet) {
        return codonWheel.get(triplet);
    }

    /**
     * Initializes the codon wheel, which maps codons to the respective amino acid object.
     * @return The initialized codon wheel.
     */
    private Map<String, AminoAcid> initCodonWheel() {
        Map<AminoAcid, String[]> aaW = new HashMap<>();
        aaW.put(new Methionine(), new String[]{"ATG"});
        aaW.put(new Isoleucine(), new String[]{"ATA", "ATC", "ATT"});
        aaW.put(new Arginine(), new String[]{"AGG", "AGA", "CGT", "CGC", "CGA",
                "CGG"});
        aaW.put(new Serine(), new String[]{"AGC", "AGT", "TCG", "TCA", "TCC", "TCT"});
        aaW.put(new Threonine(), new String[]{"ACG", "ACA", "ACC", "ACT"});
        aaW.put(new Lysine(), new String[]{"AAG", "AAA"});
        aaW.put(new Asparagine(), new String[]{"AAC", "AAT"});
        aaW.put(new Leucine(), new String[]{"TTG", "TTA", "CTT", "CTC", "CTA", "CTG"});
        aaW.put(new Phenylalanine(), new String[]{"TTC", "TTT"});
        aaW.put(new Tryptophan(), new String[]{"TGG"});
        aaW.put(new Stop(), new String[]{"TGA", "TAG", "TAA"});
        aaW.put(new Cysteine(), new String[]{"TGC", "TGT"});
        aaW.put(new Tyrosine(), new String[]{"TAC", "TAT"});
        aaW.put(new Valine(), new String[]{"GTG", "GTA", "GTC", "GTT"});
        aaW.put(new Glycine(), new String[]{"GGG", "GGA", "GGC", "GGT"});
        aaW.put(new Alanine(), new String[]{"GCT", "GCC", "GCA", "GCG"});
        aaW.put(new GlutamicAcid(), new String[]{"GAG", "GAA"});
        aaW.put(new AsparticAcid(), new String[]{"GAT", "GAC"});
        aaW.put(new Proline(), new String[]{"CCT", "CCC", "CCA", "CCG"});
        aaW.put(new Glutamine(), new String[]{"CAA", "CAG"});
        aaW.put(new Histidine(), new String[]{"CAT", "CAC"});
        Map<String, AminoAcid> cw = new HashMap<>();
        aaW.forEach((cla, codons) -> Arrays.stream(codons).forEach(codon -> cw.put(codon, cla)));
        return cw;
    }

    public String getLengthDistribution(List<String> codingRegions, int bins, int width) {

        ArrayList<ArrayList<Integer>> collect = IntStream.rangeClosed(0, bins * width)
                .filter(x -> x % width == 0)
                .skip(1)
                .collect(ArrayList::new, (oldList, e) -> {
                    ArrayList<Integer> r = new ArrayList<>();
                    r.add(oldList.size() == 0 ? 0 : oldList.get(oldList.size() - 1).get(1));
                    r.add(e);
                    oldList.add(r);
                }, ArrayList::addAll);

        Map<String, String> tmp = new LinkedHashMap<>();
        collect.forEach(t -> {
           int s = codingRegions.stream()
                   .filter(cr -> cr.length() >= t.get(0) && cr.length() < t.get(1))
                   .collect(Collectors.toList())
                   .size();
           tmp.put(
                   String.format("[%s %s)", t.get(0), t.get(1)),
                   String.join("", Collections.nCopies(s, "*"))
           );
        });

        return tmp.entrySet().stream()
                .map(entry -> String.format("%s %s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n"));
    }
}
