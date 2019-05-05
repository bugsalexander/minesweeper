import java.util.ArrayList;
import java.util.Collections;


/**
 * a MineModel to store mine and grid data.
 * Model for MineSweeper game
 *
 * @author Alex Takayama
 * @since 2019-05-1
 */
class MineModel {

    // the grid of bombs.
    private ArrayList<ArrayList<Boolean>> rows; // true if bomb, false if no bomb.
    private ArrayList<ArrayList<Boolean>> clicked; // true if clicked
    private ArrayList<ArrayList<Boolean>> flagged; // true if flagged.

    // width and height and numBombs.
    private int width;
    private int height;
    private int numBombs;

    // # bombs flagged.
    // # tiles clicked.
    private int bombsFlagged;
    private int tilesClicked;

    // default constructor
    // produces a 2D arraylist of true/false, true if bomb, with # bombs as provided.
    // TODO: # bombs provided > than width * height?
    MineModel(int width, int height, int bombs) {
        this.width = width;
        this.height = height;
        this.numBombs = bombs;

        if (this.numBombs > this.width * this.height) {
            throw new IllegalArgumentException("the # of bombs provided was greater than the # of board tiles");
        }

        // reset the board.
        // auto sets bombsFlagged and tileClicked to zero.
        this.resetBoard();
    }

    // resets the board state
    public void resetBoard() {
        this.tilesClicked = 0;
        this.bombsFlagged = 0;

        this.rows = new ArrayList<>();

        // for every single y value,
        for (int y = 0; y < height; y += 1) {
            // create a row in length width.
            ArrayList<Boolean> curRow = new ArrayList<>();
            for (int x = 0; x < width; x += 1) {
                curRow.add(false);
            }
            this.rows.add(curRow);
        }

        // add the bombs to the grid.

        // create a list of all the possible locations and shuffle them.
        ArrayList<Integer> bombChoice = new ArrayList<>();
        for (int i = 0; i < (width * height); i += 1) {
            bombChoice.add(i);
        }
        Collections.shuffle(bombChoice);

        // choose the first bombs indices, and set them to bombs.
        for (int i = 0; i < this.numBombs; i += 1) {
            int val = bombChoice.get(i);

            int x = val % width;
            int y = val / width;

            // does mutation.
            /*
            ArrayList<Boolean> temp = this.rows.get(y);
            temp.set(x, true);
            this.rows.set(y, temp);
             */
            this.rows.get(y).set(x, true);

        }

        // instantiate clicked to be false for everything.
        this.clicked = new ArrayList<>();

        // for every single y value,
        for (int y = 0; y < height; y += 1) {
            // create a row in length width.
            ArrayList<Boolean> curRow = new ArrayList<>();
            for (int x = 0; x < width; x += 1) {
                curRow.add(false);
            }
            this.clicked.add(curRow);
        }

        // instantiate flagged to be false for everything.
        this.flagged = new ArrayList<>();

        // for every single Y value;
        for (int y = 0; y < height; y += 1) {
            // create a row in length width.
            ArrayList<Boolean> curRow = new ArrayList<>();
            for (int x = 0; x < width; x += 1) {
                curRow.add(false);
            }
            this.flagged.add(curRow);
        }

        // TODO: for testing purposes. remove later.
        System.out.println(this);
    }

    // toggles a flag
    public void toggleFlag(int x, int y) {
        // if on board and not yet clicked, toggle the flag.
        if (this.onBoard(x, y) && !this.hasBeenClicked(x, y)) {
            boolean curState = this.flagged.get(y).get(x);
            // currently flagged, unflagging.
            if (this.hasBeenFlagged(x, y)) {
                this.bombsFlagged -= 1;
            }
            else {
                this.bombsFlagged += 1;
            }

            // toggle the flag.
            this.flagged.get(y).set(x, !curState);
        }
    }

    // returns if a tile has been flagged.
    public boolean hasBeenFlagged(int x, int y) {
        return this.onBoard(x, y) && !this.hasBeenClicked(x, y) && this.flagged.get(y).get(x);
    }

    // returns the number of remaining bombs.
    public int numRemainingBombs() {
        return this.numBombs - this.bombsFlagged;
    }

    // returns the number of tiles sclicked.
    public int numRemainingTiles() {
        return (this.width * this.height) - this.tilesClicked - this.numBombs;
    }

    // changes a tile's status to clicked (true).
    public void tileClick(int x, int y) {
        if (this.onBoard(x, y)) {
            // if already flagged, then don't add 1.
            if (this.hasBeenClicked(x, y)) {
                return;
            }
            else {
                this.clicked.get(y).set(x, true);
                this.tilesClicked += 1;
            }
        }
    }

    // returns if a tile has been clicked.
    public boolean hasBeenClicked(int x, int y) {
        if (this.onBoard(x, y)) {
            return this.clicked.get(y).get(x);
        }
        else {
            return false;
        }
    }

    // returns if there is a bomb at the provided coordinates.
    // expects a grid coordinate.
    public boolean isBombAt(int x, int y) {
        if (this.onBoard(x, y)) {
            return this.rows.get(y).get(x);
        }
        else {
            return false;
        }
    }

    // returns if there are zero bombs around (and in) the provided coordinates.
    public int numNeighboringBombs(int x, int y) {
        // the number of bombs on neighboring tiles
        int bombs = 0;

        // for all -> if bomb, += 1.
        if (this.isBombAt(x - 1, y - 1)) { bombs += 1; }
        if (this.isBombAt(x - 1, y)) { bombs += 1; }
        if (this.isBombAt(x - 1, y + 1)) { bombs += 1; }
        if (this.isBombAt(x, y - 1)) { bombs += 1; }
        if (this.isBombAt(x, y + 1)) { bombs += 1; }
        if (this.isBombAt(x + 1, y - 1)) { bombs += 1; }
        if (this.isBombAt(x + 1, y)) { bombs += 1; }
        if (this.isBombAt(x + 1, y + 1)) { bombs += 1; }

        return bombs;
    }

    // returns if the provided coordinates are on the board
    // similar to a .hasNext() of a .next()
    public boolean onBoard(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    // prints the tostring for testing
    public String toString() {
        String returnThis = "";
        for (int y = 0; y < height; y += 1) {
            for (int x = 0; x < width; x += 1) {
                String square;
                if (this.isBombAt(x, y)) {
                    square = "o";
                }
                else {
                    square = "x";
                }
                returnThis += (square + " ");
            }
            returnThis += "\n";
        }
        return returnThis;
    }
}