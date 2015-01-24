import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;


public class Downloader {
    FindImages thethread;
    File path;
    File boardpath;

    Downloader(FindImages thethread) throws Exception{
        this.thethread = thethread;

        path = new File(URIget.getDLPath() + "\\" + thethread.board + "\\" + thethread.id );
        boardpath = new File(URIget.getDLPath() + "\\" + thethread.board);

        Download();
    }

    public boolean Download(){
        if(!(boardpath.exists() && boardpath.isDirectory()))
        {
            boardpath.mkdir();
            path.mkdir();
        }
        else if(!(path.exists() && path.isDirectory()))
        {
            path.mkdir();
        }
        while (thethread.Images.size() != 0)
        {
            System.out.println("Size = " + thethread.Images.size());
            try
            {
                URL website = new URL(thethread.Images.peek());
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream(path.getPath() + "\\" + URIget.getImageName(thethread.Images.peek()));
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                fos.close();
            }
            catch(Exception e)
            {
                System.out.println("An image couldn't be downloaded for some reason");
            }
            finally
            {
                thethread.Images.poll();
                System.out.println("An image was removed from the queue");
            }
        }
        return true;
    }
}
