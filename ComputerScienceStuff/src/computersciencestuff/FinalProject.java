
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.swing.Timer;

/**
 *
 * @author ticus6313
 */
public class FinalProject extends JComponent implements ActionListener {

    // Height and Width of our game
    static final int WIDTH = 400;
    static final int HEIGHT = 500;

    //Title of the window
    String title = "2048";

    // sets the framerate and delay for our game
    // this calculates the number of milliseconds per frame
    // you just need to select an approproate framerate
    int desiredFPS = 60;
    int desiredTime = Math.round((1000 / desiredFPS));

    // timer used to run the game loop
    // this is what keeps our time running smoothly :)
    Timer gameTimer;

    double time = 0;
    // YOUR GAME VARIABLES WOULD GO HERE
    Color background = new Color(185, 185, 185);
    Color foreground = new Color(200, 200, 200);
    Color text = new Color(123, 123, 123);
    Color COLOR_2 = new Color(238, 228, 218);
    Color COLOR_4 = new Color(237, 224, 200);
    Color COLOR_8 = new Color(242, 177, 121);
    Color COLOR_16 = new Color(245, 149, 99);
    Color COLOR_32 = new Color(246, 124, 95);
    Color COLOR_64 = new Color(246, 94, 59);
    Color COLOR_128 = new Color(237, 207, 114);
    Color COLOR_256 = new Color(237, 204, 97);
    Color COLOR_512 = new Color(237, 200, 80);
    Color COLOR_1024 = new Color(237, 197, 63);
    Color COLOR_2048 = new Color(237, 194, 46);
    //colour array for each number
    Color[] colours = new Color[]{COLOR_2, COLOR_4, COLOR_8, COLOR_16, COLOR_32, COLOR_64,
        COLOR_128, COLOR_256, COLOR_512, COLOR_1024, COLOR_2048};

    //pause button
    Rectangle pause = new Rectangle(25, 25, 50, 50);
    BufferedImage menuButton;
    int score = 0;
    //bigger font
    Font bigger = new Font("Arial", Font.BOLD, 25);

    //mouse position
    int mouseX = 0;
    int mouseY = 0;
    //2D array to keep track of the position of the game board
    int[][] gridBoard = new int[4][4];

    //use an arraylist for the blocks since the number is constantly changing
    ArrayList<Block> blockList = new ArrayList<>();
    Block block = new Block(50, 150, 0, 2);
    //an array list that will move the blocks and store wich blocks have moved already and add them on by one
    ArrayList<Block> hasMoved = new ArrayList<>();
    int speed = 50;
    //movement booleans
    boolean up = false;
    boolean down = false;
    boolean right = false;
    boolean left = false;

    // GAME VARIABLES END HERE    
    public BufferedImage load(String filename) {
        //create empty image
        BufferedImage image = null;
        //loading files may have errors
        try {
            image = ImageIO.read(new File(filename));
        } catch (Exception e) {
            //print ugly error message
            e.printStackTrace();
        }
        //give the image back
        return image;
    }

    // Constructor to create the Frame and place the panel in
    // You will learn more about this in Grade 12 :)
    public FinalProject() {
        // creates a windows to show my game
        JFrame frame = new JFrame(title);

        // sets the size of my game
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // adds the game to the window
        frame.add(this);

        // sets some options and size of the window automatically
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // shows the window to the user
        frame.setVisible(true);

        // add listeners for keyboard and mouse
        frame.addKeyListener(new Keyboard());
        Mouse m = new Mouse();
        this.addMouseMotionListener(m);
        this.addMouseWheelListener(m);
        this.addMouseListener(m);

        // Set things up for the game at startup
        setup();

        // Start the game loop
        gameTimer = new Timer(desiredTime, this);
        gameTimer.setRepeats(true);
        gameTimer.start();
    }

    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)
    @Override
    public void paintComponent(Graphics g) {
        // always clear the screen first!
        g.clearRect(0, 0, WIDTH, HEIGHT);

        // GAME DRAWING GOES HERE
        //transform the graphics variable into Graphics2D
        Graphics2D g2d = (Graphics2D) g;
        //start of interface drawing 
        //have the background colour
        g.setColor(background);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        //have the foreground squares for the tiles
        g.setColor(foreground);
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                g.setColor(foreground);
                g.fillRect(50 + 75 * x, 150 + 75 * y, 70, 70);
                g.setColor(text);
                g.drawString("", 50 + 75 * x, 150 + 75 * y);
            }
        }
        //actually draws and colours the blocks
        for (Block block : blockList) {

            g.setColor(colours[block.colourState]);
            g.fillRect(block.block.x, block.block.y, 70, 70);
            g.setColor(text);
            g.setFont(bigger);
            //FontMetrics used to determine the width of the string so the number can be centered
            FontMetrics m = g.getFontMetrics(bigger);
            int numWidth = getWidth(m, block.number);
            int numHeight = getWidth(m, 2);

            g.drawString("" + block.number, block.block.x + (block.block.width / 2) - numWidth / 2, block.block.y + (block.block.height / 2) + numHeight / 2);
        }

        g.drawImage(menuButton, 25, 25, this);
        g.setColor(text);
        g.setFont(bigger);
        g.drawString("Score: " + score, 200, 120);

        if (StartMenu.speedrunMode) {
            g.drawString("Time " + time, 200, 50);
        }
        
        if(block.colourState == 10){
            new WinningWindow();
        }
        //end of interface drawing

        // GAME DRAWING ENDS HERE
    }

    //determine the width of any string
    public int getWidth(FontMetrics m, int number) {
        int length = 0;
        String numString = "" + number;
        for (char c : numString.toCharArray()) {
            length += m.charWidth(c);
        }
        return length;
    }

    //draws a rectangle in a random position on th  e board
    public void drawRectangle() {
        //get a random row and column that will correspond to a coordinate position
        int rows = (int) (Math.random() * 4) + 0;
        int columns = (int) (Math.random() * 4) + 0;
        System.out.println(rows + " and " + columns);
        //check if the position of the rows and columns is already filled by another block
        for (int i = 0; i < blockList.size(); i++) {
            Block b = blockList.get(i);
            if (b.block.contains(50 + 75 * columns, 150 + 75 * rows)) {
                //if it is filled find another spot until they find one that is empty
                rows = (int) (Math.random() * 4) + 0;
                columns = (int) (Math.random() * 4) + 0;
                i = -1;
            }

        }

        blockList.add(new Block(50 + 75 * columns, 150 + 75 * rows, 0, 2));
    }

    //handles the collision between two rectangles in the game 
    public boolean Collision(Block a, Block b) {
        //this is actually using the block's intersects method 
        if (a.intersects(b) && a != b && a.colourState == b.colourState) {
            return true;
        }
        return false;
    }

    //handles the positioning of the blocks if the two blocks in collision do not have the same colour state
    public void placementBlocks(Block a, Block b) {
        int column = (b.block.x - 50) / 75;
        int row = (b.block.y - 150) / 75;
        if (left) {
            a.block.x = 50 + 75 * (column + 1);

        } else if (right) {
            a.block.x = 50 + 75 * (column - 1);

        }
        if (up) {
            a.block.y = 150 + 75 * (row + 1);

        } else if (down) {
            a.block.y = 150 + 75 * (row - 1);

        }
    }

    //handles which block would move first according to their position and which direction they move
    public void moveBlock() {
        //have the arraylist empty before adding blocks
        hasMoved.clear();
        while (hasMoved.size() != blockList.size()) {
            Block closest = null;
            for (int i = 0; i < blockList.size(); i++) {
                Block r = blockList.get(i);
                //if the blocks are not alive skip over the rest of the code and loop
                if (!r.alive) {
                    continue;
                }
                //based off of what arrow was pressed, determine the closest block
                if (closest == null && !hasMoved.contains(r)) {
                    closest = r;
                } else if (left && !hasMoved.contains(r) && r.block.x < closest.block.x) {
                    closest = r;
                } else if (right && !hasMoved.contains(r) && r.block.x > closest.block.x) {
                    closest = r;
                } else if (up && !hasMoved.contains(r) && r.block.y < closest.block.y) {
                    closest = r;
                } else if (down && !hasMoved.contains(r) && r.block.y > closest.block.y) {
                    closest = r;
                }
            }
            //handle movement for the closest block and add it to the arraylist so that we do not check for the same block again
            move(closest);
            hasMoved.add(closest);
        }
        //go through each block in the arraylist
        Iterator<Block> it = blockList.iterator();
        while (it.hasNext()) {
            Block b = it.next();
            //if the block is not alive remove it
            if (!b.alive) {
                it.remove();
            }
        }
    }

    //check if the mouse is hovering over something 
    public boolean mouseOn(Rectangle object) {
        if (object.contains(mouseX, mouseY)) {
            return true;
        } else {
            return false;
        }
    }

    // This method is used to do any pre-setup you might need to do
    // This is run before the game loop begins!
    public void setup() {
        // Any of your pre setup before the loop starts should go here
        new StartMenu();
        menuButton = load("2048//menuButton.png");
        drawRectangle();
        drawRectangle();
        //blockList.add(block);
    }

    // The main game loop
    // In here is where all the logic for my game will go
    public void loop() {
        if (StartMenu.speedrunMode) {
            //determine the time by substracting the starting time and converting to miliseconds
            time = Math.round((System.nanoTime() - StartMenu.startTime) / Math.pow(10, 7)) / 100.0;
        }
        //check if the block is still moving, set the booleans to false and then a draw in a block 
        if (up || down || left || right) {
            moveBlock();
            up = false;
            down = false;
            right = false;
            left = false;
            drawRectangle();
        }
    }

    public void move(Block block) {
        //update the positions
        while (true) {
            if (up) {
                block.block.y -= speed;
            } else if (down) {
                block.block.y += speed;
            } else if (right) {
                block.block.x += speed;
            } else if (left) {
                block.block.x -= speed;
            } else {
                break;
            }
            //if there are two blocks that intersect, change their colour and delete one of them
            for (Block block2 : blockList) {
                if (block != block2 && block2.alive && block.intersects(block2) && block.colourState == block2.colourState) {
                    block.alive = false;
                    block2.colourState++;
                    block2.number *= 2;
                    score += block2.number;
                    return;
                } else if (block != block2 && block2.alive && block.intersects(block2) && block.colourState != block2.colourState) {
                    placementBlocks(block, block2);
                    return;
                }
            }
            //border for the gameboard not allowing the blocks to go past
            if (block.block.x < 50) {
                block.block.x = 50;
                break;
            } else if (block.block.x + block.block.width > 345) {
                block.block.x = 345 - block.block.width;
                break;
            }
            if (block.block.y < 150) {
                block.block.y = 150;
                break;
            } else if (block.block.y + block.block.height > 445) {
                block.block.y = 445 - block.block.height;
                break;
            }
        }
    }

    // Used to implement any of the Mouse Actions
    private class Mouse extends MouseAdapter {

        // if a mouse button has been pressed down
        @Override
        public void mousePressed(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();

            //check if the mouse presses the button
            if (mouseOn(pause)) {
                //pause window appears
                new PauseWindow();
            }
        }

        // if a mouse button has been released
        @Override
        public void mouseReleased(MouseEvent e) {

        }

        // if the scroll wheel has been moved
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {

        }

        // if the mouse has moved positions
        @Override
        public void mouseMoved(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
        }
    }

    // Used to implements any of the Keyboard Actions
    private class Keyboard extends KeyAdapter {

        // if a key has been pressed down
        @Override
        public void keyPressed(KeyEvent e) {
            //get the key code
            int key = e.getKeyCode();
            //do the actions
            //each time the blocks are moved another appears on the board
            if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
                right = true;
            } else if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
                left = true;
            } else if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
                up = true;
            } else if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
                down = true;
            }
            if (key == KeyEvent.VK_R) {
                blockList.clear();
                drawRectangle();
                drawRectangle();
                score = 0;
                time = 0;
            }
        }

        // if a key has been released
        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        loop();
        repaint();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // creates an instance of my game
        FinalProject game = new FinalProject();
    }
}
