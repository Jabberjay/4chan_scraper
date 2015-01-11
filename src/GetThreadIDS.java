import java.util.*;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class GetThreadIDS{

    public String ToSearch;
    public String board;
    String decoy;
    public PriorityQueue<String> Threads = new PriorityQueue<String>();
    public HashSet threadsDone = new HashSet();

    public GetThreadIDS(String ToSearch, String board) throws Exception{
        this.ToSearch = ToSearch;
        this.board = board;
    }

    public void scrapeIDS() throws Exception{
        System.out.println("1");
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);   //Initialises web client and sets it up
        final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_17);
        final HtmlPage page = webClient.getPage(URIget.getSearchURL(ToSearch, board));
        System.out.println("2");

        final List<?> divs = page.getByXPath("//div"); //Contains all divs and their elements

        String threads = "";
        for (Object o : divs)
        {
            threads += o;
        }

        threads = threads.substring(threads.indexOf("id=\"threads\""));     //Parses divs to find thread ids
        threads = threads.substring(threads.indexOf("HtmlDivision[<div id="));

        int index = threads.indexOf("<div id=\"thread-");
        while (index >= 0)
        {
            String temp = threads.substring(index + 16);
            String toAdd = URIget.getThreadURL(temp.substring(0, temp.indexOf("\"")), board);
            if(!threadsDone.contains(toAdd))
            {
                Threads.offer(toAdd);
                threadsDone.add(toAdd);
            }
            index = threads.indexOf("<div id=\"thread-", index + 1);
            System.out.println("3");
        }
        System.out.println("3");

        webClient.closeAllWindows();

    }
}
