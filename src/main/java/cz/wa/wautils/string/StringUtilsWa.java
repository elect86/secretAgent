package cz.wa.wautils.string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;

/**
 * Utils for string.
 *
 * @author Ondrej Milenovsky
 */
public class StringUtilsWa {
    private StringUtilsWa() {
    }

    /**
     * Splits to lines, line separator is \n or \r\n.
     * @param text input string
     * @return list of lines
     */
    public static List<String> splitToLines(String text) {
        return Arrays.asList(text.split("(\\r\\n|[\\r\\n])"));
    }

    /**
     * Replaces last sequence of # with num.
     * Examples for num=12:
     * a#b -> a123b,
     * a#b#c -> a#b12c,
     * a## -> a12,
     * a### -> a012,
     * a#### -> a0012
     * 
     * @param str
     * @param num
     * @return
     */
    public static String insertNum(String str, int num) {
        int last = str.lastIndexOf('#');
        Validate.isTrue(last >= 0, "Input string does not contain #");
        int start = last;
        while ((start >= 0) && (str.charAt(start) == '#')) {
            start--;
        }
        start++;
        last++;
        int numSize = last - start;
        return str.substring(0, start) + String.format("%0" + numSize + "d", num) + str.substring(last);
    }

    /**
     * Replaces all chars from the set in the text by replacement.
     * @param text text
     * @param chars chars to be replaced
     * @param replacement new char, if the char == 0, then removed
     * @return replaced text
     */
    public static String replaceChars(String text, Set<Character> chars, char replacement) {
        StringBuilder sb = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (chars.contains(c)) {
                if (replacement != 0) {
                    sb.append(replacement);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Splits to every substring (unlike String.split() or StringUtils.split()):
     * "a;;;bcd;;", ";" -> "a", "", "", "bcd", "", ""
     * @param text text
     * @param sep separator, not regular expression
     * @return every splited substring, even 0-length
     */
    public static List<String> split(String text, String sep) {
        Validate.isTrue(!sep.isEmpty(), "Separator must not be empty");
        List<String> list = new ArrayList<String>();
        while (true) {
            int index = text.indexOf(sep);
            if (index < 0) {
                list.add(text);
                return list;
            }
            String part = text.substring(0, index);
            list.add(part);
            text = text.substring(index + sep.length());
        }
    }

    /**
     * Multiple chars < ' ' replaces by the first char < ' '
     * @param str text
     * @return text with single white spaces
     */
    public static String singleSpaces(String str) {
        StringBuilder sb = new StringBuilder();
        char lastC = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if ((i == 0) || (c > ' ') || (lastC > ' ')) {
                sb.append(c);
            }
            lastC = c;
        }
        return sb.toString();
    }

    /**
     * All chars < ' ' replaces by ' '
     * @param str text
     * @return text containing only spaces as white spaces
     */
    public static String onlySpaces(String str) {
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c < ' ') {
                c = ' ';
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Removes all white spaces
     * @param str text
     * @return text without chars < ' '
     */
    public static String noSpaces(String str) {
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c > ' ') {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Trim, single spaces, only spaces.
     * @param str text
     * @return trimmed text with single spaces and no other white spaces 
     */
    public static String onlySingleSpaces(String str) {
        return onlySpaces(singleSpaces(str)).trim();
    }

    public static String trimBeginning(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) >= ' ') {
                return s.substring(i);
            }
        }
        return "";
    }

}
