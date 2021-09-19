package plt.morsecode;

import java.util.List;

public class MorseToken extends AbstractMorseToken {
    MorseToken(List<String> codes) {
        super(codes);
    }

    @Override
    public String toBinaryString() {
        StringBuilder text = new StringBuilder();
        for (String code : codes) {
            if (!text.isEmpty()) {
                text.append("000");
            }

            char[] chars = code.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (i > 0) {
                    text.append("0");
                }

                if (chars[i] == '.') {
                    text.append('1');
                } else {
                    text.append("111");
                }
            }
        }

        return text.toString();
    }
}
