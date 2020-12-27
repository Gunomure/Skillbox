import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ForkJoinPool;

import static java.lang.System.exit;

public class Main {

    public static String siteToParse;

    public static void main(String[] args) {
        int maxlvl = 3; //количество переходов по ссылкам
        try {
            if (args.length == 1) {
                siteToParse = args[0];
                System.out.println("Parse site with default number of clicks between sites = 3");
                System.out.println("You can launch this app with parameters <siteLink> <maxLVL>");
            } else if (args.length == 2) {
                siteToParse = args[0];
                maxlvl = Integer.parseInt(args[1]);
            } else {
                System.out.println("You can launch this app with parameters <siteLink> <maxLVL>");
                exit(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        ForkJoinPool forkJoinPool = new ForkJoinPool(10);
        SiteMap siteMap = new SiteMap(siteToParse);
        forkJoinPool.invoke(new PageParser(siteToParse, 0, maxlvl, siteMap));
        forkJoinPool.shutdown();
        while (!forkJoinPool.isTerminated()) {
        }

        Path fileWithMap = Path.of("siteMap.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileWithMap.toAbsolutePath().toString()))) {
            writer.write(siteMap.toString()); // do something with the file we've opened
            System.out.println("See file with map of site: " + fileWithMap.toAbsolutePath().toString());
        } catch (IOException e) {
            System.out.println("Couldn't write file");
        }
    }
}
