import java.net.URI;

public class Main{

    public static void main(String args[]) throws Exception{
        /** Features:
         *
         * TODO:
         *      Keep original image names
         *      gui
         *      Put everything from one search term into one folder
         *      Download whole thread page
         *      Use proxy
         *
         *      /b/thread/num
         *
         *
         */

        ThreadManager t = new ThreadManager();
        Thread f = new Thread(t);
        f.start();
        Gui x = new Gui(t);
    }
}
