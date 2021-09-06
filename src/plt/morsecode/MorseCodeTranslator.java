package plt.morsecode;

import java.io.IOException;
import java.util.List;

public interface MorseCodeTranslator {

    Character[] characters = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
            ',', '.', '?' };

    String[] morse = { ".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..",
            ".---", "-.-", ".-..", "--", "-.", "---", ".---.", "--.-", ".-.",
            "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--..", ".----",
            "..---", "...--", "....-", ".....", "-....", "--...", "---..", "----.",
            "-----", "--..--", ".-.-.-", "..--.." };

    /**
     * Converts a given character to a morse String.
     * @param c the character to be converted.
     * @return the String containing the morse representation of the given character.
     */
    public String convertCharacterToMorse(Character c);

    /**
     * Converts a given morse code to a character.
     * @param morse the morse code to be converted
     * @return the character representation of the given morse code.
     */
    public char convertMorseToCharacter(String morse);

    /**
     * Tokenizes a given text message.
     * @param message the message to be tokenized.
     * @return an array consisting of TextToken instances.
     */
    public List<TextToken> tokenizeMessage(String message) throws IOException;

    /**
     * Encodes a given array of TextTokens to morse code.
     * @param textMessage the message to be encoded.
     * @return the morse code sequence.
     */
    public List<AbstractMorseToken> encodeMessage(List<TextToken> textMessage);

    /**
     * Decodes a given morse code message.
     * @param morseMessage the morse code message to be decoded.
     * @return the decoded message.
     */
    public List<TextToken> decodeMessage(List<AbstractMorseToken> morseMessage);

    /**
     * Converts a list of TextTokens to String.
     * @param textTokens the list to be converted.
     * @return the String.
     */
    public String textTokenListToString(List<TextToken> textTokens);
}
