import java.io.RandomAccessFile;
import java.util.concurrent.locks.ReentrantLock;

public class FileAssembler {

    private final RandomAccessFile outputFile;
    private final ReentrantLock lock = new ReentrantLock();

    public FileAssembler(String outputPath) throws Exception {
        outputFile = new RandomAccessFile(outputPath, "rw");
    }

    public void write(long position, byte[] data) throws Exception {
        lock.lock();
        try {
            outputFile.seek(position);
            outputFile.write(data);
        } finally {
            lock.unlock();
        }
    }

    public void close() throws Exception {
        outputFile.close();
    }
}
