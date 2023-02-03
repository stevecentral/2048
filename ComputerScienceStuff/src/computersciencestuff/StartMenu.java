
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenu extends JFrame implements ActionListener {

    //have the frame in order to draw things on
    JPanel main = new JPanel();
    //add the two buttons
    JButton play = new JButton("play");
    JButton speedrun = new JButton("Speedrun");
    JButton quit = new JButton("quit");
    
    static public boolean speedrunMode = false;
    
    static long startTime = 0;

    //constructor
    public StartMenu() {
        //add all of the general window stuff
        this.setVisible(true);
        this.setSize(400, 500);
        this.setTitle("Start Window");
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        main.setLayout(null);
        this.add(main);
        //set the background colour
        main.setBackground(new Color(185, 185, 185));
        
        //setup for the resume button
        play.addActionListener(this);
        play.setActionCommand("play");
        play.setBounds(50, 100, 300, 50);
        main.add(play);
        //setup for the speedrun button
        speedrun.addActionListener(this);
        speedrun.setActionCommand("speedrun");
        speedrun.setBounds(50, 200, 300, 50);
        main.add(speedrun);
        //setup for the quit button
        quit.addActionListener(this);
        quit.setActionCommand("quit");
        quit.setBounds(50, 300, 300, 50);
        main.add(quit);

    }

    // method called when a button is pressed
    public void actionPerformed(ActionEvent e) {
        // get the command from the action
        String command = e.getActionCommand();

        if(command.equals("play")){
            this.setVisible(false);
        }
        else if(command.equals("speedrun")){
            speedrunMode = true;
            this.setVisible(false);
            //determine the currrent time in terms of nano seconds
            startTime = System.nanoTime();
        }
        else if(command.equals("quit")){
            System.exit(0);
        }
    }

}
