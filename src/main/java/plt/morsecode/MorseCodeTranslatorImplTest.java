package plt.morsecode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MorseCodeTranslatorImplTest {

    @Test
    void test() throws IOException {
        MorseCodeTranslatorImpl translator = new MorseCodeTranslatorImpl();

        /* test tokenization and decoding/encoding */
        BufferedReader reader = new BufferedReader(new FileReader("Aufgaben/Programmiersprachen/TextToMorse.txt"));
        Object[] lines = reader.lines().toArray();
        for (int i = 0; i < lines.length; i += 2) {
            List<TextToken> tokenList = translator.tokenizeMessage((String) lines[i]);
            String text = translator.textTokenListToString(tokenList);
            Assertions.assertEquals((String) lines[i], text);

            List<AbstractMorseToken> morseList = translator.encodeMessage(tokenList);
            List<TextToken> decoded = translator.decodeMessage(morseList);
            for (int j = 0; j < tokenList.size(); j++) {
                Assertions.assertEquals(tokenList.get(j).getText().toLowerCase(),
                        decoded.get(j).getText());
            }
        }

        /* test binary string decoding/encoding */
        reader = new BufferedReader(new FileReader("Aufgaben/Programmiersprachen/TextToBinary.txt"));
        lines = reader.lines().toArray();
        for (int i = 0; i < lines.length; i += 2) {
            List<TextToken> tokenList = translator.tokenizeMessage((String) lines[i]);
            List<AbstractMorseToken> morseTokens = translator.encodeMessage(tokenList);
            String binaryString = translator.convertMorseCodeToBinary(morseTokens);
            List<AbstractMorseToken> morseTokensFromBinary = translator.convertBinaryToMorseCodes(binaryString);
            List<TextToken> tokenListFromBinary = translator.decodeMessage(morseTokensFromBinary);
            String decodedFromBinary = translator.textTokenListToString(tokenListFromBinary);
            Assertions.assertEquals((String)((String) lines[i]).toLowerCase(), decodedFromBinary);
        }
    }
}