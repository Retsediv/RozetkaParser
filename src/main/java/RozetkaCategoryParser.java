import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class RozetkaCategoryParser {

    /**
     * Link to specific category on rozetka.com
     */
    private String categoryUrl;

    /**
     * Init class and set categoryUrl
     *
     * @param categoryUrl
     */
    public RozetkaCategoryParser(String categoryUrl) {
        this.categoryUrl = categoryUrl;
    }

    /**
     * categoryUrl getter
     *
     * @return categoryUrl
     */
    public String getCategoryUrl() {
        return categoryUrl;
    }

    /**
     * categoryUrl setter
     *
     * @param categoryUrl Link to category on rozetka website
     */
    public void setCategoryUrl(String categoryUrl) {
        this.categoryUrl = categoryUrl;
    }


    /**
     * Get number of pages in the catalog by parsing html
     * (fetch relevant block and get the last pagination item)
     *
     * @param url String
     * @return pagesCount
     * Integer
     * @throws IOException
     */
    private static int getNumberOfPages(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        // get all pagination elements
        Elements paginationElement = doc.select(".paginator-catalog .paginator-catalog-l-i span");
        // fetch a value of the last element
        int pagesCount = Integer.parseInt(paginationElement.last().text());

        return pagesCount;
    }

    public void process() {
        // Do something

    }
}
