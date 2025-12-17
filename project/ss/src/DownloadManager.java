import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class DownloadManager {

    private final ExecutorService pool;

    public DownloadManager(int threads) {
        pool = Executors.newFixedThreadPool(threads);
    }

    // 🔹 Parallel Download
    public long downloadParallel(
            String source,
            String target,
            Consumer<Long> progress,
            Consumer<String> log
    ) throws Exception {

        File file = new File(source);
        long fileSize = file.length();
        int chunkSize = 1024 * 100;

        FileAssembler assembler = new FileAssembler(target);
        List<Future<Long>> futures = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        long start = 0;
        while (start < fileSize) {
            long end = Math.min(start + chunkSize, fileSize);
            Chunk chunk = new Chunk(start, end);

            futures.add(
                pool.submit(
                    new DownloadTask(source, chunk, assembler, progress, log)
                )
            );
            start = end;
        }

        for (Future<Long> f : futures) {
            f.get();
        }

        assembler.close();
        return System.currentTimeMillis() - startTime;
    }

    // 🔹 Sequential Download
    public long downloadSequential(
            String source,
            String target,
            Consumer<Long> progress,
            Consumer<String> log
    ) throws Exception {

        File file = new File(source);
        long fileSize = file.length();

        RandomAccessFile in = new RandomAccessFile(source, "r");
        RandomAccessFile out = new RandomAccessFile(target, "rw");

        long startTime = System.currentTimeMillis();

        byte[] buffer = new byte[1024 * 100];
        long total = 0;
        int read;

        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
            total += read;
            progress.accept((long) read);

            log.accept("Sequential read " + total + " bytes");
            Thread.sleep(300);
        }

        in.close();
        out.close();

        return System.currentTimeMillis() - startTime;
    }

    public void shutdown() {
        pool.shutdown();
    }
}
