
import java.awt.*;

//block having its own class, so more of the same block can be added to the arraylist
public class Block {

    //set the standard stuff for the blocks
    Rectangle block;
    int colourState;
    int number;

    boolean alive;

    //have the block pass in the position, colout state and what number is associated with
    public Block(int x, int y, int colour, int num) {
        block = new Rectangle(x, y, 70, 70);
        colourState = colour;
        number = num;
        alive = true;
    }

    //create the block's own intersects method so that it can be treated like a rectangle
    public boolean intersects(Block b) {
        return block.intersects(b.block);
    }

}
