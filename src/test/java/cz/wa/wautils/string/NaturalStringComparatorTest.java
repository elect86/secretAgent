package cz.wa.wautils.string;

import static junit.framework.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * Tests the comparator. 
 * 
 * @author Ondrej Milenovsky
 */
public class NaturalStringComparatorTest {

    @Test
    public void testComparator() {
        List<String> list = new ArrayList<String>();
        list.add("aaA");
        list.add("aaa");
        list.add("zgr0");
        list.add("zgr");
        list.add("02");
        list.add("0");
        list.add("002");
        list.add("2");
        list.add("g333a");
        list.add("g4");
        list.add("");
        list.add("50a");
        list.add("8b");
        Collections.sort(list, NaturalStringComparator.INSTANCE);

        List<String> list2 = Arrays.asList("", "0", "02", "002", "2", "8b", "50a", "aaA", "aaa", "g4",
                "g333a", "zgr", "zgr0");

        assertEquals(list2, list);
    }
}
