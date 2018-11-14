//package cz.wa.secretagent.utils;
//
//import java.awt.Color;
//
//import com.martiansoftware.jsap.ParseException;
//import com.martiansoftware.jsap.stringparsers.ColorStringParser;
//
///**
// * Utils for color.
// *
// * @author Ondrej Milenovsky
// */
//public class ColorUtils {
//    private ColorUtils() {
//    }
//
//    /**
//     * Parses color in format #RRGGBB or #RRGGBBAA
//     * @param str
//     * @return
//     */
//    public static Color parseColor(String str) {
//        try {
//            return (Color) ColorStringParser.getParser().parse(str);
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
