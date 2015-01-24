import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import java.util.HashSet;
import java.util.PriorityQueue;
import org.json.JSONArray;
import org.json.JSONObject;
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
    }

    public FindImages(String url) throws Exception{
        this.url = url;
        this.id = URIget.getIDandBoard(url)[0];
        this.board = URIget.getIDandBoard(url)[1];
    }

    /*public boolean scrapeImages(){
        if(!scrapeImages_JSON()) System.out.println("no kek");
        try
        {
            final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_24);
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

                }
                else
                {
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
    }*/

    public boolean scrapeImages(){
        String pageJSON;
        try
        {
            final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_24);
            final Page page = webClient.getPage(URIget.getThreadURL_JSON(id, board));
            WebResponse response = page.getWebResponse();
            pageJSON = response.getContentAsString();
            if (pageJSON.equalsIgnoreCase("")) return false;
        }
        catch(Exception e)
        {
            return true;
        }
        JSONObject json = new JSONObject(pageJSON);
        JSONArray array = (JSONArray) json.get("posts");
        /*for (int i = 0; i < array.length(); i++)
        {
            try
            {
                JSONObject j = new JSONObject(array.get(i).toString());
                String image_url = "http://i.4cdn.org/" + board + "/" + j.get("tim") + j.get("ext").toString();
                if(ImagesDone.contains(image_url))
                {
                    Images.offer(image_url);
                }
            }catch (Exception e){}
        }*/

        for (int i = array.length(); i > 0; i--)
        {
            try
            {
                JSONObject j = new JSONObject(array.get(i).toString());
                String image_url = "http://i.4cdn.org/" + board + "/" + j.get("tim") + j.get("ext").toString();
                if(ImagesDone.contains(image_url)) break;
                Images.offer(image_url);
                ImagesDone.add(image_url);
            }catch (Exception e){}
        }


        return true;
    }

}