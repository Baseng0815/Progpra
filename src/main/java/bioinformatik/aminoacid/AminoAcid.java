package bioinformatik.aminoacid;

public abstract class AminoAcid {

    public String getName() {
        return this.getClass().getSimpleName();
    }

    abstract public String getOneLetterName();

    @Override
    public String toString() {
        return this.getOneLetterName();
    }
}