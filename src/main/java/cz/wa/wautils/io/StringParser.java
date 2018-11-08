package cz.wa.wautils.io;

import java.util.HashSet;
import java.util.Set;

/**
 * Parses substrings from string.
 * 
 * @author Ondrej Milenovsky
 */
public class StringParser {

    private final String text;
    private int pos;

    public StringParser(String text) {
        this.text = text;
        pos = 0;
    }

    public char peekChar() {
        checkEnd();
        return text.charAt(pos);
    }

    public boolean startsWith(String str) {
        return text.startsWith(str, pos);
    }

    /**
     * Moves position to first occurrence of char and returns difference string
     * without the char (stops at any char from the input).
     * If there are no more occurrences of the chars, then returns rest of the string
     */
    public String parseToOr(char... cs) {
        checkEnd();
        Set<Character> set = new HashSet<Character>();
        for (char c : cs) {
            set.add(c);
        }
        for (int i = pos; i < text.length(); i++) {
            if (set.contains(text.charAt(i))) {
                if (i <= pos) {
                    pos++;
                    return "";
                }
                String ret = text.substring(pos, i);
                pos = i + 1;
                return ret;
            }
        }
        int i = pos;
        pos = text.length();
        return text.substring(i);
    }

    /**
     * Skips sequences of substrings ending with the chars
     * 
     * @return this
     */
    public StringParser skipAnd(char... cs) {
        for (char c : cs) {
            parseToOr(c);
        }
        return this;
    }

    /**
     * Skips to any of the chars
     * 
     * @return this
     */
    public StringParser skipOr(char... cs) {
        parseToOr(cs);
        return this;
    }

    /**
     * Skips chars but not other chars, so if current pos is not any of the
     * chars, it skips nothing
     * 
     * @return this
     */
    public StringParser skipIfOnPosOr(char... cs) {
        checkEnd();
        Set<Character> set = new HashSet<Character>();
        for (char c : cs) {
            set.add(c);
        }
        for (; pos < text.length(); pos++) {
            if (!set.contains(text.charAt(pos))) {
                break;
            }
        }
        return this;
    }

    public StringParser skipToEnd() {
        pos = text.length();
        return this;
    }

    public String parseToEnd() {
        String ret = text.substring(pos, text.length());
        pos = text.length();
        return ret;
    }

    /**
     * Moves forward by number
     * @param numberChars
     * @return
     */
    public StringParser skipNum(int numberChars) {
        pos += numberChars;
        return this;
    }

    public boolean isAtEnd() {
        return pos >= text.length();
    }

    private void checkEnd() {
        if (isAtEnd()) {
            throw new IllegalStateException("End of string");
        }
    }

    public char getLastChar() {
        return text.charAt(pos - 1);
    }

    /**
     * Current position
     */
    public int getPosition() {
        return pos;
    }

    public String setPosition(int pos) {
        String ret = text.substring(this.pos, pos);
        this.pos = pos;
        return ret;
    }

}
