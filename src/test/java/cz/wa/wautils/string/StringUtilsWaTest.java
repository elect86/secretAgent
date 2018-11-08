package cz.wa.wautils.string;

import static junit.framework.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class StringUtilsWaTest {
    @Test
    public void testSingleSpaces() {
        assertEquals("", StringUtilsWa.singleSpaces(""));
        assertEquals(" ", StringUtilsWa.singleSpaces("  "));
        assertEquals("a", StringUtilsWa.singleSpaces("a"));
        assertEquals(" aa bbb ccc ", StringUtilsWa.singleSpaces("       aa        bbb ccc   "));
        assertEquals(" aa bbb ccc ", StringUtilsWa.singleSpaces(" aa bbb ccc "));
        assertEquals("aa bbb ccc", StringUtilsWa.singleSpaces("aa        bbb ccc"));
        assertEquals("aabbbccc", StringUtilsWa.singleSpaces("aabbbccc"));
    }

    @Test
    public void testOnlySpaces() {
        assertEquals("", StringUtilsWa.onlySpaces(""));
        assertEquals("a", StringUtilsWa.onlySpaces("a"));
        assertEquals(" ", StringUtilsWa.onlySpaces(" "));
        assertEquals(" ", StringUtilsWa.onlySpaces("\n"));
        assertEquals("  a   b   c", StringUtilsWa.onlySpaces("  a   b   c"));
        assertEquals("   a b     c", StringUtilsWa.onlySpaces("  \ta\nb\r  \r\rc"));
    }

    @Test
    public void testNoSpaces() {
        assertEquals("", StringUtilsWa.noSpaces(""));
        assertEquals("", StringUtilsWa.noSpaces(" "));
        assertEquals("a", StringUtilsWa.noSpaces("a"));
        assertEquals("abc", StringUtilsWa.noSpaces("abc"));
        assertEquals("abc", StringUtilsWa.noSpaces("  a   b   c"));
        assertEquals("abc", StringUtilsWa.noSpaces("  \ta\nb\r  \r\rc"));
    }

    @Test
    public void testSplit() {
        assertEquals(Arrays.asList(""), StringUtilsWa.split("", "SEP"));
        assertEquals(Arrays.asList("abcd"), StringUtilsWa.split("abcd", "SEP"));
        assertEquals(Arrays.asList("ab", "cd"), StringUtilsWa.split("abSEPcd", "SEP"));
        assertEquals(Arrays.asList("", "ab", "cd"), StringUtilsWa.split("SEPabSEPcd", "SEP"));
        assertEquals(Arrays.asList("", "ab", "cd", "", ""), StringUtilsWa.split("SEPabSEPcdSEPSEP", "SEP"));
        assertEquals(Arrays.asList("", "", "", "a", "", "b", "cd", "", ""),
                StringUtilsWa.split("SEPSEPSEPaSEPSEPbSEPcdSEPSEP", "SEP"));

        assertEquals(Arrays.asList("abcd"), StringUtilsWa.split("abcd", ";"));
        assertEquals(Arrays.asList("ab", "cd"), StringUtilsWa.split("ab;cd", ";"));
        assertEquals(Arrays.asList("", "ab", "cd"), StringUtilsWa.split(";ab;cd", ";"));
        assertEquals(Arrays.asList("", "ab", "cd", "", ""), StringUtilsWa.split(";ab;cd;;", ";"));
        assertEquals(Arrays.asList("", "", "", "a", "", "b", "cd", "", ""),
                StringUtilsWa.split(";;;a;;b;cd;;", ";"));
    }

    @Test
    public void testInsertNum() {
        int num = 12;
        assertEquals("12", StringUtilsWa.insertNum("#", num));
        assertEquals("012", StringUtilsWa.insertNum("###", num));
        assertEquals("a12b", StringUtilsWa.insertNum("a#b", num));
        assertEquals("a#b12c", StringUtilsWa.insertNum("a#b#c", num));
        assertEquals("a12", StringUtilsWa.insertNum("a##", num));
        assertEquals("a012", StringUtilsWa.insertNum("a###", num));
        assertEquals("a0012", StringUtilsWa.insertNum("a####", num));
        assertEquals("#aa##bbb#cc0012d", StringUtilsWa.insertNum("#aa##bbb#cc####d", num));
    }
}
