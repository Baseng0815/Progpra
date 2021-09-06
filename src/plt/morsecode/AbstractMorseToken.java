package plt.morsecode;

import java.util.List;

public abstract class AbstractMorseToken {

    List<String> codes;

    AbstractMorseToken(List<String> codes) {
        this.codes = codes;
    }

    public List<String> getCodes() {
        return codes;
    }

    public abstract String toBinaryString();

}
