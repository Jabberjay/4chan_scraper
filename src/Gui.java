import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;
import java.util.prefs.Preferences;

/**
 * Created by Jhonti on 10/01/2015.
 */
public class Gui {
    JFrame frame = new JFrame();
    JTextField textField = new JTextField();
    JTextArea textArea = new JTextArea();

    Font font;
    Font biggerFont;

    ThreadManager manager;


    public Gui(ThreadManager manager) throws Exception{

        this.manager = manager;

        font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/res/VeraMono.ttf"));
        biggerFont = font.deriveFont(Font.BOLD, 12f);

        JPanel contentPane = new JPanel();

        textField.setForeground(Color.BLACK);
        textArea.setBackground(Color.DARK_GRAY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 450, 381);
        frame.setSize(700,400);
        frame.setMinimumSize(new Dimension(700,400));
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        frame.setContentPane(contentPane);

        JScrollPane scrollPane = new JScrollPane();

        //textField = new JTextField();
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e){
                if (e.getKeyCode() == 10) {
                    /*textArea.append(textField.getText() + "\n");
                    textField.setText("");
                    textField.requestFocus();*/             ///HANDLEkey events here
                    try
                    {
                        interaction(textField.getText().split(" "));
                    }catch(Exception ee){}

                    textField.setText("");
                    textField.requestFocus();
                }
            }
        });
        textField.setColumns(10);
        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
                gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
                        .addComponent(textField, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
        );
        gl_contentPane.setVerticalGroup(
                gl_contentPane.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textField, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
        );

        //textArea = new JTextArea();
        textArea.setBackground(Color.DARK_GRAY);
        textArea.setForeground(Color.WHITE);
        textArea.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setMargin(new Insets(0, 10, 10, 10));
        textArea.setFont(biggerFont);
        scrollPane.setViewportView(textArea);
        contentPane.setLayout(gl_contentPane);

        frame.setVisible(true);

        textArea.append("       _                 _____                      " + "\n");
        textArea.append("      | |               |  __ \\                     " + "\n");
        textArea.append("   ___| |__   __ _ _ __ | |  | | _____      ___ __  " + "\n");
        textArea.append("  / __| '_ \\ / _` | '_ \\| |  | |/ _ \\ \\ /\\ / | '_ \\ " + "\n");
        textArea.append(" | (__| | | | (_| | | | | |__| | (_) \\ V  V /| | | |" + "\n");
        textArea.append("  \\___|_| |_|\\__,_|_| |_|_____/ \\___/ \\_/\\_/ |_| |_|" + "\n");
        textArea.append("\n");
        textArea.append("> Commands:" + "\n");

        String[][] commands = {{"thread add (url)", "adds thread to download list"}, {"thread kill (part of url)", "removes thread from download list"},
                {"thread see", "see threads in download list"}, {"search add (board) (search term)", "will download threads matching search term"}, {"search kill (board) [search term]", "stops a search"},
                {"search see", "see current searches"}, {"dlpath see", "see the download path"}, {"dlpath set (path)", "set the download path"}, {"killall", "stop everything"}};

        for(String[] s : commands)
        {
            textArea.append("> " + s[0]);
            int spaces = 35 - s[0].length();
            for (int i = 0; i < spaces; i++)
            {
                textArea.append(" ");
            }
            textArea.append(s[1] + "\n");
        }
        textArea.append("\n");

        if(URIget.getDLPath() == "")
        {
            textArea.append("You must set a download path before downloading" + "\n");
        }
    }


    private void interaction(String[] args){
        String in = "";
        for (String k : args)
        {
            in += k + " ";
        }
        textArea.append("> " + in + "\n");
        if(args[0].equalsIgnoreCase("thread") || args[0].equalsIgnoreCase("t"))
        {
            if(args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("a"))
            {

                try
                {
                    if(manager.addThread(new FindImages(args[2])))
                    {
                        textArea.append("Thread added!" + "\n");
                    }
                    else
                    {
                        textArea.append("Thread is already added" + "\n");
                    }
                }
                catch (Exception e)
                {
                    textArea.append("We couldn't find that thread" + "\n");
                }
            }
            else if(args[1].equalsIgnoreCase("kill") || args[1].equalsIgnoreCase("k"))
            {
                if(!manager.killThread(args[2]))
                {
                    textArea.append("Either we aren't downloading that or be more specific" + "\n");
                }
                else
                {
                    textArea.append("Thread killed" + "\n");
                }
            }
            else if(args[1].equalsIgnoreCase("see") || args[1].equalsIgnoreCase("s"))
            {
                ArrayList<String> see = manager.seeThreads();
                if (see.size() == 0)
                {
                    textArea.append("Not downloading any threads" + "\n");
                }
                else
                {
                    for(String s : see)
                    {
                        textArea.append(s + "\n");
                    }
                }
            }
            else
            {
                textArea.append("invalid command" + "\n");
            }
        }
        else if(args[0].equalsIgnoreCase("search") || args[0].equalsIgnoreCase("s"))
        {
            if(args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("a"))
            {
                try
                {
                    if(!URIget.boardExists(args[2]))
                    {
                        textArea.append("No such board exists" + "\n");
                    }
                    else if (manager.addSearch(new GetThreadIDS(args[3], args[2])))
                    {
                        textArea.append("Search started" + "\n");
                    }
                    else
                    {
                        textArea.append("A search is already underway for that" + "\n");
                    }
                }catch(Exception e){System.out.println("ehoh");}
            }
            else if(args[1].equalsIgnoreCase("kill") || args[1].equalsIgnoreCase("k"))
            {
                try
                {
                    if(manager.killSearch(args[2], args[3]))
                    {
                        textArea.append("Search killed" + "\n");
                    }
                    else
                    {
                        textArea.append("Search not found" + "\n");
                    }
                }
                catch(Exception e)
                {
                    if(manager.killSearch(args[2], ""))
                    {
                        textArea.append("Searches killed" + "\n");
                    }
                    else
                    {
                        textArea.append("No searches on that board currently" + "\n");
                    }
                }
            }
            else if(args[1].equalsIgnoreCase("see") || args[1].equalsIgnoreCase("s"))
            {
                ArrayList<String[]> searching = manager.searchSee();
                if(searching.size() == 0)
                {
                    textArea.append("No active searches" + "\n");
                }
                else
                {
                    textArea.append("Searches:" + "\n");
                    for(String[] z : searching)
                    {
                        textArea.append(z[0] + " " +  z[1] + "\n");
                    }
                }

            }
            else
            {
                textArea.append("invalid command" + "\n");
            }
        }
        else if(args[0].equalsIgnoreCase("dlpath") || args[0].equalsIgnoreCase("d"))
        {

            if(args[1].equalsIgnoreCase("set"))
            {
                String fullpath = "";
                for (int s = 2; s < args.length; s++)
                {
                    fullpath += args[s] + " ";
                }
                fullpath = fullpath.substring(0, fullpath.length() - 1);

                try
                {
                    System.out.println(fullpath);
                }
                catch(Exception e){}
                if(URIget.setDLPath(fullpath))
                {
                    textArea.append("Download path set" + "\n");
                }
                else
                {
                    textArea.append("Invalid path" + "\n");
                }
            }
            else if(args[1].equalsIgnoreCase("see") || args[1].equalsIgnoreCase("s"))
            {
                if(URIget.getDLPath().equalsIgnoreCase(""))
                {
                    textArea.append("Download path not yet set" + "\n");
                }
                else
                {
                    textArea.append(URIget.getDLPath() + "\n");
                }

            }
            else
            {
                textArea.append("invalid command" + "\n");
            }
        }
        else if (args[0].equalsIgnoreCase("killall") || args[0].equalsIgnoreCase("ka"))
        {
            manager.killall();
            textArea.append("Killed everything" + "\n");
        }
        else
        {
            textArea.append("invalid command" + "\n");
        }
        textArea.append("\n");
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
