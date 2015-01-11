import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * Created by Jhonti on 07/01/2015.
 */
public class FindImages{
    public String id;
    public String board;
    public String url;
    private HashSet ImagesDone = new HashSet();
    public PriorityQueue<String> Images = new PriorityQueue<String>();

    public FindImages(String id, String board) throws Exception{
        this.id = id;
        this.board = board;
        url = URIget.getThreadURL(this.id, this.board);
        ///scrapeImages();
    }

    public FindImages(String url) throws Exception{
        this.url = url;
        this.id = URIget.getIDandBoard(url)[0];
        this.board = URIget.getIDandBoard(url)[1];
    }

    public boolean scrapeImages(){
        try {
            final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_17);
            final HtmlPage page = webClient.getPage(url);

            final String pageAsXml = page.asXml();

            String spliton = "i.4cdn.org";
            String[] PageHtml = pageAsXml.split(spliton);

            for (int i = 1; i < PageHtml.length; i++)
            {
                if (ImagesDone.contains(PageHtml[i].substring(0, PageHtml[i].indexOf("\""))) == false)
                {
                    Images.offer("http://" + spliton + PageHtml[i].substring(0, PageHtml[i].indexOf("\"")));
                    ImagesDone.add(PageHtml[i].substring(0, PageHtml[i].indexOf("\"")));
                    System.out.println("http://" + spliton + PageHtml[i].substring(0, PageHtml[i].indexOf("\"")));
                    System.out.println("Imagesdone has " + ImagesDone.size());

                }else{
                    System.out.println("An image was rejected");
                }
            }

            System.out.println("-------------------------------------------");

        }
        catch(Exception e)
        {
            return false;
        }

        return true;

    }

}