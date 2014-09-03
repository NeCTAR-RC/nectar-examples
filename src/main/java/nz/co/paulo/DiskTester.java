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
 * Created by paulo on 14/07/2014.
 * https://www.flamingspork.com/talks/
 */
public class DiskTester {

    public static class Row {
        String line;

        public Row(String line) {
            this.line = line;
        }
    }

    public static class Context {

        ArrayList<DiskTester.Row> results = new ArrayList<>();

        @SuppressWarnings("CloneDoesntCallSuperClone")
        @Override
        public Context clone() {
            Context copy = new Context();
            synchronized (results) {
                copy.results.addAll(results);
            }
            return copy;
        }

    }

    private static final String[] directories = {"/root", "/transient", "/block"};

    public static final String WRITE_RESULT = "It took %.3f seconds to write a %d MB, file rate: %.1f MB/s%n";
    public static final String READ_RESULT = "It took %.3f seconds to read  a %d MB file, rate: %.1f MB/s%n";

    private static Context context = new Context();

    public static void testStorage() {
        new Thread(() -> {
            try {
                DiskTester.testStorage(context.results);
            } catch (IOException e) {
                context.results.add(new DiskTester.Row("Exception: " + e.getMessage()));
                for (StackTraceElement element : e.getStackTrace()) {
                    context.results.add(new DiskTester.Row(element.toString()));
                }
            }
        }).start();
    }

    public static Context getResults() {
        return context.clone();
    }

    private static void testStorage(ArrayList<Row> results) throws IOException {
        for (String directory : directories) {
            if (Files.exists(Paths.get(directory))) {
                results.add(new Row("Now testing: " + directory));
                for (int mb : new int[]{50, 100, 250, 500, 1000, 2000}) {
                    results.addAll(testFileSize(mb, directory));
                }
            } else {
                results.add(new Row("Skipped " + directory + " as it does not exist."));
            }
        }
    }

    private static List<Row> testFileSize(int mb, String directory) throws IOException {
        ArrayList<Row> result = new ArrayList<>();
        Path target = Paths.get(directory, "write.txt");
        //System.out.println("Now testing: " + target + " at " + Integer.toString(mb) + " MB");
        File file = Files.createFile(target).toFile();
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
        try {
            target = Paths.get(directory, "read.txt");
            file.renameTo(target.toFile());
            file = target.toFile();
            long start2 = System.nanoTime();
            BufferedReader br = new BufferedReader(new FileReader(file));
            for (String line; (line = br.readLine()) != null; ) {
                // we don't do anything with it.
            }
            br.close();
            long time2 = System.nanoTime() - start2;
            result.add(new Row(String.format(READ_RESULT, time2 / 1e9, file.length() >> 20, file.length() * 1000.0 / time2)));
        } finally {
            file.delete();
        }
        return result;
    }

}
