import java.io.RandomAccessFile;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class DownloadTask implements Callable<Long> {

    private final String sourceFile;
    private final Chunk chunk;
    private final FileAssembler assembler;
    private final Consumer<String> logCallback;
    private final Consumer<Long> progressCallback;

    public DownloadTask(
            String sourceFile,
            Chunk chunk,
            FileAssembler assembler,
            Consumer<Long> progressCallback,
            Consumer<String> logCallback
    ) {
        this.sourceFile = sourceFile;
        this.chunk = chunk;
        this.assembler = assembler;
        this.progressCallback = progressCallback;
        this.logCallback = logCallback;
    }

    @Override
    public Long call() throws Exception {

        String threadName = Thread.currentThread().getName();
        logCallback.accept(
                threadName + " started chunk [" +
                chunk.start + " - " + chunk.end + "]"
        );

        RandomAccessFile input = new RandomAccessFile(sourceFile, "r");
        input.seek(chunk.start);

        int size = (int) (chunk.end - chunk.start);
        byte[] buffer = new byte[size];
        input.readFully(buffer);

        Thread.sleep(300); // محاكاة تحميل

        assembler.write(chunk.start, buffer);
        input.close();

        progressCallback.accept((long) size);

        logCallback.accept(
                threadName + " finished chunk [" +
                chunk.start + " - " + chunk.end + "]"
        );

        return (long) size;
    }
}
