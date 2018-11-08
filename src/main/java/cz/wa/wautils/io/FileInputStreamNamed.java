package cz.wa.wautils.io;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Extended FileInputStream, toString returns absolute file path.
 * This is because LWGLJ texture loader uses stream.toString as key for caching textures.
 *
 * @author Ondrej Milenovsky
 */
public class FileInputStreamNamed extends FileInputStream {

    private final String fileName;

    public FileInputStreamNamed(File file) throws FileNotFoundException {
        super(file);
        fileName = file.getAbsolutePath();
    }

    public FileInputStreamNamed(FileDescriptor fdObj) {
        super(fdObj);
        fileName = fdObj.toString();
    }

    public FileInputStreamNamed(String name) throws FileNotFoundException {
        this(new File(name));
    }

    @Override
    public String toString() {
        return fileName;
    }

}
