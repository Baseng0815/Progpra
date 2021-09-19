package plt.morsecode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MorseCodeTranslatorImpl implements MorseCodeTranslator, MorseCodeTransmitter {
    @Override
    public String convertCharacterToMorse(Character c) {
        for (int i = 0; i < characters.length; i++) {
            if (characters[i].equals(c)) {
                return morse[i];
            }
        }

        return "";
    }

    @Override
    public char convertMorseToCharacter(String morseCode) {
        for (int i = 0; i < characters.length; i++) {
            if (morse[i].equals(morseCode)) {
                return characters[i];
            }
        }

        return ' ';
    }

    @Override
    public  List<TextToken> tokenizeMessage(String message) throws IOException {
        List<TextToken> tokens = new ArrayList<>();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(message));

        tokenizer.ordinaryChar('.');
        tokenizer.ordinaryChar(',');
        tokenizer.ordinaryChar('?');

        int currentToken;
        while ((currentToken = tokenizer.nextToken()) != StreamTokenizer.TT_EOF) {
            if (tokenizer.ttype == StreamTokenizer.TT_WORD) {
                tokens.add(new TextToken(tokenizer.sval));
            } else {
                tokens.add(new TextToken(Character.toString(currentToken)));
            }
        }

        return tokens;
    }

    @Override
    public List<AbstractMorseToken> encodeMessage(List<TextToken> textMessage) {
        List<AbstractMorseToken> morseTokens = new ArrayList<>();
        for (TextToken token : textMessage) {
            List<String> morseCodes = new ArrayList<>();
            char[] tokenChars = token.getText().toLowerCase().toCharArray();
            for (char tokenChar : tokenChars) {
                String morseCode = convertCharacterToMorse(tokenChar);
                morseCodes.add(morseCode);
            }

            AbstractMorseToken morseToken = new MorseToken(morseCodes);
            morseTokens.add(morseToken);
        }

        return morseTokens;
    }

    @Override
    public List<TextToken> decodeMessage(List<AbstractMorseToken> morseMessage) {
        List<TextToken> textTokens = new ArrayList<>();
        for (AbstractMorseToken token : morseMessage) {
            StringBuilder text = new StringBuilder();
            for (String morseCode : token.getCodes()) {
                text.append(convertMorseToCharacter(morseCode));
            }

            textTokens.add(new TextToken(text.toString()));
        }

        return textTokens;
    }

    @Override
    public String textTokenListToString(List<TextToken> textTokens) {
        StringBuilder text = new StringBuilder();
        for (TextToken token : textTokens) {
            String t = token.getText();
            if (t.equals(",") || t.equals(".") || t.equals("?") || text.isEmpty()) {
                text.append(t);
            } else {
                text.append(" ").append(t);
            }
        }

        return text.toString();
    }

    @Override
    public String convertMorseCodeToBinary(List<AbstractMorseToken> morseCode) {
        StringBuilder text = new StringBuilder();
        for (AbstractMorseToken morseToken : morseCode) {
            if (!text.isEmpty()) {
                text.append("0000000");
            }

            text.append(morseToken.toBinaryString());
        }

        return text.toString();
    }

    @Override
    public List<AbstractMorseToken> convertBinaryToMorseCodes(String binary) {
        List<AbstractMorseToken> morseTokens = new ArrayList<>();
        String[] textTokens = binary.split("0000000");
        for (String textToken : textTokens) {
            String[] binaryCharacters = textToken.split("000");
            List<String> codes = new ArrayList<>();
            for (String binaryCharacter : binaryCharacters) {
                StringBuilder code = new StringBuilder();
                String[] binaryCodes = binaryCharacter.split("0");
                for (String binaryCode : binaryCodes) {
                    if (binaryCode.equals("1")) {
                        code.append(".");
                    } else {
                        code.append("-");
                    }
                }
                codes.add(code.toString());
            }

            AbstractMorseToken token = new MorseToken(codes);
            morseTokens.add(token);
        }

        return morseTokens;
    }
}