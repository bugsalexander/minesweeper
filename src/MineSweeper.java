import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

// a game of MineSweeper
class MineWorld extends World {

    private MineModel grid;
    private MineView view;

    MineWorld(int width, int height, int bombs) {
        this.grid = new MineModel(width, height, bombs);
        this.view = new MineView(width, height);
    }

    @Override
    public WorldScene makeScene() {
        return null;
    }

    // deals with updating the correct tiles
    public void onMouseClicked(Posn pos, String buttonName) {

    }
}

// a MineModel to store mine and grid data.
// Model for MineSweeper game
class MineModel {

    // the grid of bombs.
    private ArrayList<ArrayList<Boolean>> rows;

    // width and height
    private int width;
    private int height;

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
            rows.add(curRow);
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

            rows.get(y).set(x, true);
        }
    }

    // returns if there is a bomb at the provided coordinates.
    public boolean isBombAt(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return this.rows.get(y).get(x);
        }
        else {
            throw new IllegalStateException("Tried to access an index not on the board");
        }
    }
}

// a MineView to store and modify the image of the grid.
class MineView {
    // the width/height of one tile (square)
    private static final int TILE_SIZE = 10;

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
     */

    // gets the complete image of the grid
    public WorldScene drawView() {
        return this.view;
    }

    // converts from grid coordinate to pixel coordinate
    private int toPixel(int grid) {
        return (TILE_SIZE / 2) + (grid * TILE_SIZE);
    }

    // places a provided image at the given grid coordinates
    private void addToView(WorldImage img, int x, int y) {
        this.view.placeImageXY(img, this.toPixel(x), this.toPixel(y));
    }

    // draws a blank tile at the provided grid posn (unclicked)
    private void drawBlank(int x, int y) {
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
        this.addToView(new FrameImage(square.overlayImages(text), Color.BLACK), x, y);
    }

    // draws a bomb at the provided grid posn.
    public void drawBomb(int x, int y) {
        WorldImage base = new RectangleImage(TILE_SIZE, TILE_SIZE, OutlineMode.SOLID, Color.GRAY);
        WorldImage bomb = new CircleImage(TILE_SIZE / 2, OutlineMode.SOLID, Color.BLACK);
        this.addToView(new FrameImage(base.overlayImages(bomb), Color.BLACK), x, y);
    }
}



