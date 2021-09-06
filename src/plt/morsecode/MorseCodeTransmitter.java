package plt.morsecode;

import java.util.List;

public interface MorseCodeTransmitter {

    /**
     * Converts a given morse code message to binary.
     * @param morseCode the morse code to be converted.
     * @return the binary representation of the morse code message.
     */
    String convertMorseCodeToBinary(List<AbstractMorseToken> morseCode);

    /**
     * Converts a binary signal to a morse code message.
     * @param binary the binary signal.
     * @return the morse code message transmitted by the binary signal.
     */
    List<AbstractMorseToken>  convertBinaryToMorseCodes(String binary);

}
