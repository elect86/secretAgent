package cz.wa.wautils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * Utils used to serialize and deserialize object.
 *
 * @author Ondrej Milenovsky
 */
public class FileObjectUtils {
    private FileObjectUtils() {
    }

    /**
     * Writes object to file.
     * @param file target file
     * @param object object to write (must be serializable)
     * @throws IOException when failed to create stream or write the object (not when closing the stream)
     */
    public static void writeObjectToFile(File file, Object object) throws IOException {
        FileUtils.forceMkdir(file.getParentFile());
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        try {
            out.writeObject(object);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * Reads object from file.
     * @param file source file
     * @return deserialized object
     * @throws IOException when failed to create stream or read the object (not when closing the stream)
     * @throws ClassNotFoundException when failed to read the object
     */
    public static Object readObjectFromFile(File file) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        try {
            return in.readObject();
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
}
