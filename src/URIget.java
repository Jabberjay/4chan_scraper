import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.prefs.Preferences;



public class URIget {

    static Preferences prefs = Preferences.userNodeForPackage(URIget.class);

    static final ArrayList<String> boards = new ArrayList<String>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "gif", "h", "hr", "k", "m", "o", "p", "r", "s", "t", "u", "v", "vg",
            "vr", "w", "wg", "", "i", "ic", "r9k", "s4s", "cm", "hm", "lgbt", "y", "3", "adv", "an", "asp", "biz",
            "cgl", "ck", "co", "diy", "fa", "fit", "gd", "hc", "int", "jp", "lit", "mlp", "mu", "n", "out", "po",
            "pol", "sci", "soc", "sp", "tg", "toy", "trv", "tv", "vp", "wsg", "x"));

    public static String getSearchURL(String ToSearch, String board){
        return "https://4chan.org/" + board + "/" + ToSearch;
    }

    public static String getThreadURL(String id, String board){
        return "http://boards.4chan.org/" + board + "/thread/" + id;
    }

    public static String getThreadURL_JSON(String id, String board){
        return "http://api.4chan.org/" + board + "/res/" + id + ".json";
    }

    public static String getImageName(String url){
        return url.substring(url.indexOf("/", 18));
    }

    public static String[] getIDandBoard(String url){
        String[] temp = new String[2];
        try
        {
            temp[0] = url.split("/thread/")[1].substring(0, url.split("/thread/")[1].indexOf("/"));
        }
        catch(Exception e)
        {
            temp[0] = url.split("/thread/")[1];
        }

        temp[1] = url.split(".org/")[1].substring(0, url.split(".org/")[1].indexOf("/"));

        for (String s : temp)
        {
            System.out.println(s);
        }

        return temp;
    }

    public static boolean setDLPath(String uri){
        File path = new File(uri);
        System.out.println(path.getPath());

        if(path.isDirectory())
        {
            prefs.put("dlpath", uri);
            return true;
        }
        else
        {
            return false;
        }
    }

    public static String getDLPath(){
        return prefs.get("dlpath", "");
    }

    public static boolean boardExists(String board){
        if(!boards.contains(board))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static void cleardl(){
        prefs.put("dlpath", "");
    }   //For testing purposes

    public static boolean checkDL(){
        File path = new File(prefs.get("dlpath", ""));
        if(path.isDirectory())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}