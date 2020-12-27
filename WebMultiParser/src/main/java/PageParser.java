import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.RecursiveAction;
import java.util.regex.Pattern;

public class PageParser extends RecursiveAction {
    static Logger logger = Logger.getLogger(PageParser.class);

    private String url;
    private int lvl;
    private int maxLvl;
    private SiteMap siteMap;

    public PageParser(String url, int lvl, int maxLvl, SiteMap siteMap) {
        this.url = url;
        this.lvl = lvl;
        this.maxLvl = maxLvl;
        this.siteMap = siteMap;
    }

    @Override
    protected void compute() {
        try {
            if (lvl >= maxLvl)
                return;

            synchronized (siteMap) {
                siteMap.addPath(url);
            }

            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (isUrlValid(url)) {
                Document doc = Jsoup.connect(url).ignoreContentType(true).userAgent("Mozilla").get();
                Elements links = doc.select("a");
                logger.debug("links count: " + links.size());

                if (links.isEmpty()) {
                    return;
                }

                // ищем ссылки, начинающиеся на корень, например 'https://lenta.ru'
                Pattern pattern = Pattern.compile("^%s[a-zA-Z0-9/]*$".formatted(getRoot(url)));
                links.stream()
                        .map((link) -> link.attr("abs:href"))
                        .filter(pattern.asPredicate())
                        .forEachOrdered((thisUrl) -> {
                    logger.debug(thisUrl);
                    if (!thisUrl.isEmpty()) {
                        PageParser pp = new PageParser(thisUrl, lvl + 1, maxLvl, siteMap);
                        pp.fork();
                    }
                });
            }
        } catch (SocketTimeoutException ex) {
            logger.debug("TIMEOUT");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static boolean isUrlValid(String url) {
        try {
            URL obj = new URL(url);
            obj.toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }

    private static String getRoot(String url) {
        try {
            URL urlForRoot = new URL(url);
            String res = urlForRoot.getProtocol() + "://" + urlForRoot.getHost();
            return res;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "";
        }
    }
}
