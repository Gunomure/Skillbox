import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        String srcFolder = "/home/dmitry/skillbox/java_basics/11_Multithreading/ImageResizer/images";
        String dstFolder = "/home/dmitry/skillbox/java_basics/11_Multithreading/ImageResizer/dst";
        if (args.length > 0) {
            srcFolder = args[0];
        }
        if (args.length == 2) {
            dstFolder = args[1];
        }

        int coreCount = Runtime.getRuntime().availableProcessors();
        File srcDir = new File(srcFolder);
        List<File> files = Arrays.asList(Objects.requireNonNull(srcDir.listFiles()));
        int step = Math.round(files.size() / coreCount) + 1;
        int currentIndex = 0;
        try {
            List<Thread> threads = new ArrayList<>();
            while (currentIndex + step < files.size()) {
                ImageResizer imageResizer = new ImageResizer(files.subList(currentIndex, currentIndex + step),
                        System.currentTimeMillis(), dstFolder);
                Thread thread = new Thread(imageResizer);
                thread.start();
                threads.add(thread);
                currentIndex += step;
            }
            ImageResizer imageResizer = new ImageResizer(files.subList(currentIndex, files.size()),
                    System.currentTimeMillis(), dstFolder);
            Thread thread = new Thread(imageResizer);
            thread.start();
            threads.add(thread);

            //гарантируем завершение всех потоков
            for (Thread locThread : threads) {
                locThread.join();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}