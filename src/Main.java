

public class Main{

    public static void main(String args[]) throws Exception{
        /** Features:
         *
         * TODO:
         *      Keep original image names
         *      (before gui)TODO:
         *                      Searches tied to subsequent thread objects
         *
         *
         *      Put everything from one search term into one folder
         *      Download whole thread page
         *      Use proxy
         *      Use 4chan json api for getting images from threads https://github.com/4chan/4chan-API  https://api.4chan.org/b/res/threadnum.json  'tim' are the images
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