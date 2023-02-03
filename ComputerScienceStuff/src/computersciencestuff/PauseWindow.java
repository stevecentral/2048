
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PauseWindow extends JFrame implements ActionListener {

    //have the frame in order to draw things on
    JPanel main = new JPanel();
    //add the two buttons
    JButton resume = new JButton("resume");
    JButton quit = new JButton("quit");

    //constructor
    public PauseWindow() {
        //add all of the general window stuff
        this.setVisible(true);
        this.setSize(400, 500);
        this.setTitle("PAUSED");
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        main.setLayout(null);
        this.add(main);
        //set the background colour
        main.setBackground(new Color(185, 185, 185));
        
        //setup for the resume button
        resume.addActionListener(this);
        resume.setActionCommand("resume");
        resume.setBounds(50, 150, 300, 50);
        main.add(resume);
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

        if(command.equals("resume")){
            this.setVisible(false);
        }
        else if(command.equals("quit")){
            System.exit(0);
        }
    }

}
