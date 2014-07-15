package nz.co.paulo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * // http://stackoverflow.com/questions/13155700/fastest-way-to-read-and-write-large-files-line-by-line-in-java
 * The results of such a benchmark are more or less useless. First of all, when writing a file, closing the
 * output stream does not ensure that all data has been written physically to the disk. It may still lurk around in a
 * memory buffer on the OS level or on the harddisk. If you read the exact same file directly after you have written
 * it, the data will most likely be read from a memory buffer and not physically from disk.
 * According to this benchmark, my laptop HDD comes close to 500MB/s both for reading and writing and
 * that is probably somewhere around 10x the true performance.
 * Created by paulo on 14/07/2014.
 */
public class DiskTester {

    public static class Row {
        String line;
        public Row (String line) {
            this.line = line;
        }
    }


    private static final String[] directories = {"~", "/transient", "/block"};

    public static final String WRITE_RESULT = "Took %.3f seconds to write to a %d MB, file rate: %.1f MB/s%n";
    public static final String READ_RESULT = "Took %.3f seconds to read to a %d MB file, rate: %.1f MB/s%n";

    public static List<Row> testStorage() throws IOException {
        ArrayList<Row> result = new ArrayList<>();
        for (String directory : directories) {
            Path target_dir = Paths.get(directory);
            if (Files.exists(target_dir)) {
                for (int mb : new int[]{50, 100, 250, 500, 1000, 2000}) {
                    Path target = Paths.get(directory, "test.txt");
                    result.add(new Row("Now testing"  + target));
                    //result.addAll(testFileSize(mb, target));
                }
            } else {
                result.add(new Row("Skipped " + target_dir + " as it does not exist."));
            }
        }
        return result;
    }

    private static List<Row> testFileSize(int mb, Path path) throws IOException {
        ArrayList<Row> result = new ArrayList<>();

        File file = Files.createFile(path).toFile();
        file.deleteOnExit();
        char[] chars = new char[1024];
        Arrays.fill(chars, 'A');
        String longLine = new String(chars);
        long start1 = System.nanoTime();
        PrintWriter pw = new PrintWriter(new FileWriter(file));
        for (int i = 0; i < mb * 1024; i++) {
            pw.println(longLine);
        }
        pw.close();
        long time1 = System.nanoTime() - start1;
        result.add(new Row(String.format(WRITE_RESULT, time1 / 1e9, file.length() >> 20, file.length() * 1000.0 / time1)));
        long start2 = System.nanoTime();
        BufferedReader br = new BufferedReader(new FileReader(file));
        for (String line; (line = br.readLine()) != null; ) {
            // we don't do anything with it.
        }
        br.close();
        long time2 = System.nanoTime() - start2;
        result.add(new Row(String.format(READ_RESULT, time2 / 1e9, file.length() >> 20, file.length() * 1000.0 / time2)));
        file.delete();
        return result;
    }

}
