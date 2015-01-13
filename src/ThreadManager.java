import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;



public class ThreadManager implements Runnable{

    CopyOnWriteArrayList<FindImages> dlthreads = new CopyOnWriteArrayList<FindImages>(new ArrayList<FindImages>());
    CopyOnWriteArrayList<GetThreadIDS> tosearch = new CopyOnWriteArrayList<GetThreadIDS>(new ArrayList<GetThreadIDS>());

    @Override
    public void run(){
        try
        {
            Commence();
        }catch(Exception e){}
    }

    private void Commence()throws Exception{
        while(true)
        {
            for (FindImages f : dlthreads)
            {
                System.out.println("Thread size is " + dlthreads.size());

                if(f.scrapeImages())
                {
                    new Downloader(f);
                }
                else
                {
                    dlthreads.remove(f);
                    System.out.println("Added a thread to scrap list");
                }
            }

            for (GetThreadIDS t : tosearch)
            {
                System.out.println("Checking... for more...");
                t.scrapeIDS();
                while (t.Threads.size() != 0)
                {
                    addThread(new FindImages(t.Threads.peek()));
                    t.Threads.poll();
                    Thread.sleep(200);
                }
            }
        }
    }


    public boolean addThread(FindImages thread){
        for (FindImages i : dlthreads)
        {
            if (i.id.equals(thread.id))
            {
                return false;
            }
        }
        dlthreads.add(thread);
        return true;
    }

    public boolean addSearch(GetThreadIDS getthread){
        for (GetThreadIDS i : tosearch)
        {
            if (i.ToSearch.equalsIgnoreCase(getthread.ToSearch) && i.board.equalsIgnoreCase(getthread.board))
            {
                return false;
            }
        }
        tosearch.add(getthread);
        return true;
    }

    public boolean killThread(String term){
        int counter = 0;
        FindImages found = null;

        for (FindImages i : dlthreads)
        {
            if (i.url.contains(term))
            {
                if (counter > 0)
                {
                    return false;
                }
                found = i;
                counter++;
            }
        }

        if(counter == 0)
        {
            return false;
        }

        dlthreads.remove(found);
        return true;
    }
    public boolean killSearch(String board, String term){
        int counter = 0;
        GetThreadIDS found = null;
        if (!term.equalsIgnoreCase(""))
        {
            for (GetThreadIDS i : tosearch)
            {
                if (i.board.equalsIgnoreCase(board) && i.ToSearch.equalsIgnoreCase(term))
                {
                    if (counter > 0)
                    {
                        return false;
                    }
                    found = i;
                    counter++;
                }
            }
        }
        else
        {
            ArrayList<GetThreadIDS> toremove = new ArrayList<GetThreadIDS>();
            for (GetThreadIDS i : tosearch)
            {
                if (i.board.equalsIgnoreCase(board))
                {
                    toremove.add(i);
                }
            }
            for (GetThreadIDS u : toremove)
            {
                tosearch.remove(u);
            }
            if(toremove.size() != 0)
            {
                return true;
            }
        }

        if(counter == 0)
        {
            return false;
        }

        tosearch.remove(found);
        return true;
    }

    public ArrayList<String> seeThreads(){
        ArrayList<String> dlling = new ArrayList<String>();
        for(FindImages f : dlthreads)
        {
            dlling.add(f.url);
        }
        return dlling;
    }

    public void killall(){
        dlthreads.clear();
        tosearch.clear();
    }

    public ArrayList<String[]> searchSee(){
        ArrayList<String[]> searching = new ArrayList<String[]>();
        for(GetThreadIDS k : tosearch)
        {
            searching.add(new String[]{k.board, k.ToSearch});
        }
        return searching;
    }


}
