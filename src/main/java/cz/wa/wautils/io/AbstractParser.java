package cz.wa.wautils.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.wa.wautils.string.StringUtilsWa;

/**
 * Abstract parser parsing by lines.
 *
 * @author Ondrej Milenovsky
 */
public abstract class AbstractParser {
    private static final Logger logger = LoggerFactory.getLogger(AbstractParser.class);

    private String encoding;

    private File lastFile;
    private int lineNum = 0;
    private boolean errorPrinted = false;

    /**
     * Constructor with default encoding.
     */
    public AbstractParser() {
        this(null);
    }

    /**
     * Constructor with encoding.
     * @param encoding encoding, null means default
     */
    public AbstractParser(String encoding) {
        this.encoding = encoding;
    }

    /**
     * @param encoding encoding, null means default
     */
    protected void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * @return encoding, null means default
     */
    protected String getEncoding() {
        return encoding;
    }

    /**
     * Starts the parsing, opens the file, reads to string,
     * splits by lines and processes each line separately.
     * @param file
     * @throws IOException
     */
    protected void startParsing(File file) throws IOException {
        errorPrinted = false;
        lineNum = 0;
        lastFile = file;
        for (String line : readFileToStringLines(file, encoding)) {
            lineNum++;
            parseLine(line);
        }
    }

    /**
     * Process next line to parse.
     * @param line the line from file
     * @return false to stop parsing
     */
    protected abstract boolean parseLine(String line);

    /**
     * Reads the file and returns list of lines.
     * @param file input file
     * @param encoding encoding
     * @return list of lines
     * @throws IOException
     */
    protected List<String> readFileToStringLines(File file, String encoding) throws IOException {
        return StringUtilsWa.splitToLines(FileUtils.readFileToString(file, encoding));
    }

    protected void warn(String msg) {
        printFileName();
        logger.warn(getReportHead() + msg);
    }

    protected void error(String msg, Throwable e) {
        printFileName();
        logger.error(getReportHead() + msg, e);
    }

    protected void printFileName() {
        if (!errorPrinted && (lastFile != null)) {
            logger.warn("Error in file: " + lastFile.getAbsolutePath());
            errorPrinted = true;
        }
    }

    /**
     * Absolute path of the file will be printed before first error or warning.
     * It is set automatically when using startParsing.
     * @param file the file
     */
    protected void setFileToPrint(File file) {
        lastFile = file;
    }

    protected String getReportHead() {
        return "Line " + lineNum + ": ";
    }

}
