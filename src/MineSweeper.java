import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.*;
import tester.Tester;

import java.awt.*;
import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;


// a MineModel to store mine and grid data.
// Model for MineSweeper game
class MineModel {

    // the grid of bombs.
    private ArrayList<ArrayList<Boolean>> rows;
    private ArrayList<ArrayList<Boolean>> clicked;
    private ArrayList<ArrayList<Boolean>> flagged;

    // width and height
    private int width;
    private int height;

    // default constructor
    // produces a 2D arraylist of true/false, true if bomb, with # bombs as provided.
    // TODO: # bombs provided > than width * height?
    MineModel(int width, int height, int bombs) {
        this.width = width;
        this.height = height;

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
        for (int i = 0; i < bombs; i += 1) {
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
    }

    // toggles a flag
    public void toggleFlag(int x, int y) {
        // if on board and not yet clicked, toggle the flag.
        if (this.onBoard(x, y) && !this.hasBeenClicked(x, y)) {
            this.flagged.get(y).set(x, !this.flagged.get(y).get(x));
        }
        // don't fall off the end!
        else {
            return;
        }
    }

    // returns if a tile has been flagged.
    public boolean hasBeenFlagged(int x, int y) {
        return this.onBoard(x, y) && !this.hasBeenClicked(x, y) && this.flagged.get(y).get(x);
    }

    // changes a tile's status to clicked (true).
    public void tileClick(int x, int y) {
        if (this.onBoard(x, y)) {
            this.clicked.get(y).set(x, true);
        }
        else {
            return;
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

// a MineView to store and modify the image of the grid.
// View for MineSweeper game
class MineView {
    // the width/height of one tile (square)
    static final int TILE_SIZE = 20;

    // the WorldScene representing the view.
    private WorldScene view;
    // width and height of the image in grid
    private int width;
    private int height;

    // default constructor
    MineView(int width, int height) {
        this.width = width;
        this.height = height;
        this.view = new WorldScene(this.width * TILE_SIZE, this.height * TILE_SIZE);

        // fill the grid with blank tiles
        for (int y = 0; y < this.height; y += 1) {
            for (int x = 0; x < this.width; x += 1) {
                this.drawBlank(x, y);
            }
        }
    }

    /*
    Methods:

    WorldScene drawView()
    void drawBlankPressed(int, int)
    void drawOne(int, int)
    void drawTwo(int, int)
    void drawThree(int, int)
    void drawFour(int, int)
    void drawFive(int, int)
    void drawSix(int, int)
    void drawSeven(int, int)
    void drawEight(int, int)
    void drawBomb(int, int)
    void drawEnd()
     */

    // gets the complete image of the grid
    public WorldScene drawView() {
        return this.view;
    }

    // converts from grid coordinate to pixel coordinate
    private int toPixel(int grid) {
        return (TILE_SIZE / 2) + (grid * TILE_SIZE);
    }

    // converts from pixel coordinates to grid coordinates.
    public int toGrid(int pixel) {
        return pixel / TILE_SIZE;
    }

    // places a provided image at the given grid coordinates
    private void addToView(WorldImage img, int x, int y) {
        this.view.placeImageXY(img, this.toPixel(x), this.toPixel(y));
    }

    // draws a blank tile at the provided grid posn (unclicked)
    public void drawBlank(int x, int y) {
        WorldImage base = new RectangleImage(TILE_SIZE, TILE_SIZE, OutlineMode.SOLID, Color.LIGHT_GRAY);
        this.addToView(new FrameImage(base, Color.BLACK), x, y);
    }

    // draws a blank tile at the provided grid posn (clicked)
    public void drawBlankPressed(int x, int y) {
        WorldImage base = new RectangleImage(TILE_SIZE, TILE_SIZE, OutlineMode.SOLID, Color.GRAY);
        this.addToView(new FrameImage(base, Color.BLACK), x, y);
    }

    // draws a 1 at the provided grid posn.
    public void drawOne(int x, int y) {
        this.drawNumColor(1, Color.BLUE, x, y);
    }

    // draws a 2 at the provided grid posn.
    public void drawTwo(int x, int y) {
        this.drawNumColor(2, Color.GREEN, x, y);
    }

    // draws a 3 at the provided grid posn.
    public void drawThree(int x, int y) {
        this.drawNumColor(3, Color.RED, x, y);
    }

    // draws a 4 at the provided grid posn.
    public void drawFour(int x, int y) {
        this.drawNumColor(4, Color.PINK, x, y);
    }

    // draws a 5 at the provided grid posn.
    public void drawFive(int x, int y) {
        this.drawNumColor(5, Color.MAGENTA, x, y);
    }

    // draws a 6 at the provided grid posn.
    public void drawSix(int x, int y) {
        this.drawNumColor(6, Color.GREEN, x, y);
    }

    // draws a 7 at the provided grid posn.
    public void drawSeven(int x, int y) {
        this.drawNumColor(7, Color.YELLOW, x, y);
    }

    // draws an 8 at the provided grid posn.
    public void drawEight(int x, int y) {
        this.drawNumColor(8, Color.BLACK, x, y);
    }

    // draws a number of a specified color at the specified grid posn.
    private void drawNumColor(int num, Color color, int x, int y) {
        WorldImage square = new RectangleImage(TILE_SIZE, TILE_SIZE, OutlineMode.SOLID, Color.GRAY);
        WorldImage text = new TextImage("" + num, 16, color);
        this.addToView(new FrameImage(text.overlayImages(square), Color.BLACK), x, y);
    }

    // draws a bomb at the provided grid posn.
    public void drawBomb(int x, int y) {
        WorldImage base = new RectangleImage(TILE_SIZE, TILE_SIZE, OutlineMode.SOLID, Color.GRAY);
        WorldImage bomb = new CircleImage(TILE_SIZE / 2, OutlineMode.SOLID, Color.BLACK);
        this.addToView(new FrameImage(bomb.overlayImages(base), Color.BLACK), x, y);
    }

    // draws the end message
    public void drawEnd() {
        this.view = new WorldScene(this.width * TILE_SIZE, this.height * TILE_SIZE);
        this.view.placeImageXY(
                new RectangleImage(
                        this.width * TILE_SIZE,
                        this.height * TILE_SIZE,
                        OutlineMode.SOLID,
                        Color.BLACK),
                this.width * TILE_SIZE / 2, this.height * TILE_SIZE / 2);
    }

    // draws a flag at the provided grid posn.
    public void drawFlag(int x, int y) {
        WorldImage flag = new RectangleImage(TILE_SIZE, TILE_SIZE, OutlineMode.SOLID, Color.RED);
        this.addToView(new FrameImage(flag, Color.BLACK), x, y);
    }
}

// a MineController to deal with clicking and logic.
// Controller for MineSweeper game
class MineController extends World {

    // the Model
    private MineModel model;

    // the View
    private MineView view;

    // default constructor
    MineController(MineModel model, MineView view) {
        this.model = model;
        this.view = view;
    }

    // draws the game board
    public WorldScene makeScene() {
        return this.view.drawView();
    }

    // clicks on the game board.
    public void onMouseClicked(Posn pos, String button) {
        // convert the mouse pixel posn to grid posn
        int x = this.view.toGrid(pos.x);
        int y = this.view.toGrid(pos.y);

        // if left button, update view with #.
        if (button.equals("LeftButton")) {
            this.leftClick(x, y);

        }
        // else if right button, update flag.
        else if (button.equals("RightButton")) {
            // if already clicked, do nothing.
            if (this.model.hasBeenClicked(x, y)) {
                // do nothing
                return;
            }
            // if flagged, unflag. if unflagged, flag.
            else {
                if (this.model.hasBeenFlagged(x, y)) {
                    this.view.drawBlank(x, y);
                }
                else {
                    this.view.drawFlag(x, y);
                }
                // AND toggle the flag.
                this.model.toggleFlag(x, y);
            }
        }
        // a weird mouse button was pressed? do nothing.
        else {
            return;
        }
    }

    // takes action to left-click on a tile and flood-fill zero tiles.
    private void floodFill(int startX, int startY) {
        // invariant: should only ever be called on non-bomb tiles.

        // perform depth first search on nearby tiles, coloring them if not and otherwise continuing.
        Stack<Posn> toFill = new Stack<>();
        toFill.push(new Posn(startX, startY));

        // while we have nodes to check, check them.
        while (!toFill.isEmpty()) {
            Posn curr = toFill.pop();
            int x = curr.x;
            int y = curr.y;

            // if already clicked, not on board, or bomb, next.
            if (this.model.hasBeenClicked(x, y) || !this.model.onBoard(x, y) || this.model.isBombAt(x, y)) {

            }
            // if we are number tile, color and next.
            else if (this.model.numNeighboringBombs(x, y) > 0) {
                this.numberTile(x, y, this.model.numNeighboringBombs(x, y));
            }
            // if we are zero, draw self, add neighbors and continue.
            else {
                this.view.drawBlankPressed(x, y);
                toFill.push(new Posn(x - 1, y - 1));
                toFill.push(new Posn(x - 1, y));
                toFill.push(new Posn(x - 1, y + 1));
                toFill.push(new Posn(x, y - 1));
                toFill.push(new Posn(x, y + 1));
                toFill.push(new Posn(x + 1, y - 1));
                toFill.push(new Posn(x + 1, y));
                toFill.push(new Posn(x + 1, y + 1));
            }
            // we are now clicked.
            this.model.tileClick(x, y);
        }
    }

    // human click.
    private void leftClick(int x, int y) {
        if (this.model.hasBeenClicked(x, y) || this.model.hasBeenFlagged(x, y)) {
            return;
        }
        else {
            // TODO: change order of which checked based on frequency of action.
            if (this.model.isBombAt(x, y)) {
                // end the game scene with view.
                this.view.drawBomb(x, y);
                //this.view.drawEnd();

                // has now been clicked.
                this.model.tileClick(x, y);
            }
            // we know (x, y) is not a bomb.
            else {
                int numNeighboringBombs = this.model.numNeighboringBombs(x, y);

                // if we have > 0 bombs in neighboring, mark as #.
                if (numNeighboringBombs > 0) {

                    // has now been clicked.
                    this.model.tileClick(x, y);

                    this.numberTile(x, y, numNeighboringBombs);
                }
                // floodfill the neighboring tiles/zeroes. (we are a zero)
                else {

                    this.floodFill(x, y);
                    /*
                    this.view.drawBlankPressed(x, y);

                    // this tile has been clicked.
                    this.model.tileClick(x, y);

                    this.floodFill(x - 1, y - 1);
                    this.floodFill(x - 1, y);
                    this.floodFill(x - 1, y + 1);
                    this.floodFill(x, y - 1);
                    this.floodFill(x, y + 1);
                    this.floodFill(x + 1, y - 1);
                    this.floodFill(x + 1, y);
                    this.floodFill(x + 1, y + 1);
                     */
                }
            }
        }
    }

    // color a provided tile by a number.
    public void numberTile(int x, int y, int numNeighboringBombs) {
        if (numNeighboringBombs == 1) {
            this.view.drawOne(x, y);
        }
        else if (numNeighboringBombs == 2) {
            this.view.drawTwo(x, y);
        }
        else if (numNeighboringBombs == 3) {
            this.view.drawThree(x, y);
        }
        else if (numNeighboringBombs == 4) {
            this.view.drawFour(x, y);
        }
        else if (numNeighboringBombs == 5) {
            this.view.drawFive(x, y);
        }
        else if (numNeighboringBombs == 6) {
            this.view.drawSix(x, y);
        }
        else if (numNeighboringBombs == 7) {
            this.view.drawSeven(x, y);
        }
        else {
            this.view.drawEight(x, y);
        }
    }
}

// tester
class ExamplesMine {

    void testView(Tester t) {
        // currently set to medium
        int width = 16;
        int height = 16;
        int bombs = 40;

        MineModel model = new MineModel(width, height, bombs);
        MineView view = new MineView(width, height);
        MineController tv = new MineController(model, view);

        System.out.println(model);

        tv.bigBang(width * MineView.TILE_SIZE, height * MineView.TILE_SIZE, 0.1);
    }
}