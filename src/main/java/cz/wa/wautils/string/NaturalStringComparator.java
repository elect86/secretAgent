package cz.wa.wautils.string;

import java.util.Comparator;

/**
 * Comparator for strings that takes numbers as whole numbers, not single digits. 
 */
public class NaturalStringComparator implements Comparator<String> {

    public static final NaturalStringComparator INSTANCE = new NaturalStringComparator();

    protected NaturalStringComparator() {
    }

    @Override
    public int compare(String o1, String o2) {
        int i1 = 0;
        int i2 = 0;
        int result;

        while (true) {
            int nz1 = 0;
            int nz2 = 0;

            char c1 = charAt(o1, i1);
            char c2 = charAt(o2, i2);

            while (Character.isSpaceChar(c1) || (c1 == '0')) {
                if (c1 == '0') {
                    nz1++;
                } else {
                    nz1 = 0;
                }
                i1++;
                c1 = charAt(o1, i1);
            }

            while (Character.isSpaceChar(c2) || (c2 == '0')) {
                if (c2 == '0') {
                    nz2++;
                } else {
                    nz2 = 0;
                }
                i2++;
                c2 = charAt(o2, i2);
            }

            if (Character.isDigit(c1) && Character.isDigit(c2)) {
                if ((result = compareNumber(o1.substring(i1), o2.substring(i2))) != 0) {
                    return result;
                }
            }

            if ((c1 == 0) && (c2 == 0)) {
                return nz1 - nz2;
            }

            if (c1 < c2) {
                return -1;
            } else if (c1 > c2) {
                return +1;
            }
            i1++;
            i2++;
        }
    }

    private int compareNumber(String s1, String s2) {
        int b = 0;
        int i = 0;

        while (true) {
            char c1 = charAt(s1, i);
            char c2 = charAt(s2, i);

            if (!Character.isDigit(c1) && !Character.isDigit(c2)) {
                return b;
            } else if (!Character.isDigit(c1)) {
                return -1;
            } else if (!Character.isDigit(c2)) {
                return +1;
            } else if (c1 < c2) {
                if (b == 0) {
                    b = -1;
                }
            } else if (c1 > c2) {
                if (b == 0)
                    b = +1;
            } else if (c1 == 0 && c2 == 0) {
                return b;
            }
            i++;
        }
    }

    private char charAt(String s, int i) {
        if (i >= s.length()) {
            return 0;
        } else {
            return s.charAt(i);
        }
    }
}
