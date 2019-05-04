import javalib.impworld.WorldScene;
import javalib.worldimages.*;

import java.awt.*;


/**
 * a MineView to store and modify the image of the grid.
 * View for MineSweeper game
 *
 * @author Alex Takayama
 * @since 2019-05-1
 */
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

    // draws the bombCount on the screen.
    public void drawBombCount(int bombCount, int tileCount, boolean isEnd) {
        WorldImage text = new TextImage("" + bombCount, 16, Color.YELLOW);
        WorldImage back;
        if (bombCount == 0 && tileCount == 0) {
            back = new RectangleImage(TILE_SIZE, TILE_SIZE, OutlineMode.SOLID, Color.MAGENTA);
        }
        else if (isEnd) {
            back = new RectangleImage(TILE_SIZE, TILE_SIZE, OutlineMode.SOLID, Color.RED);
        }
        else {
            back = new RectangleImage(TILE_SIZE, TILE_SIZE, OutlineMode.SOLID, Color.LIGHT_GRAY);
        }
        this.view.placeImageXY(text.overlayImages(back), TILE_SIZE, TILE_SIZE);
    }

    // resets the view.
    public void resetView(int bombCount, int tileCount) {
        for (int y = 0; y < height; y += 1) {
            for (int x = 0; x < width; x += 1) {
                this.drawBlank(x, y);
            }
        }
        // draws the new bombCount.
        this.drawBombCount(bombCount, tileCount, false);
    }
}
