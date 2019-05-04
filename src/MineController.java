import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.Posn;

import java.util.Stack;


/**
 * the MineController to deal with clicking and logic.
 * Controller for MineSweeper game
 *
 * @author Alex Takayama
 * @since 2019-05-1
 */
class MineController extends World {

    // the Model and View
    private MineModel model;
    private MineView view;

    // whether or not a game is in session.
    boolean inSession;

    // width and height and bombs.
    int width;
    int height;
    int bombs;

    // default constructor
    MineController(int width, int height, int bombs) {
        this.width = width;
        this.height = height;
        this.bombs = bombs;

        this.model = new MineModel(width, height, bombs);
        this.view = new MineView(width, height);
        this.inSession = true;

        this.view.drawBombCount(this.model.numRemainingBombs(), this.model.numRemainingTiles(), false);
    }

    // draws the game board
    public WorldScene makeScene() {
        return this.view.drawView();
    }

    // clicks on the game board.
    public void onMouseClicked(Posn pos, String button) {
        // if game not in session, then start new game.
        if (!this.inSession) {
            this.model.resetBoard();

            this.view.resetView(this.model.numRemainingBombs(), this.model.numRemainingTiles());
            this.inSession = true;

            return;
        }

        // convert the mouse pixel posn to grid posn
        int x = this.view.toGrid(pos.x);
        int y = this.view.toGrid(pos.y);

        // if left button, update view with #.
        if (button.equals("LeftButton")) {
            // auto updates drawing bombcount
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

            // update the bombcount displayed.
            this.view.drawBombCount(this.model.numRemainingBombs(), this.model.numRemainingTiles(), false);
        }
        // a weird mouse button was pressed? do nothing.
        else {
            return;
        }

        // deal with winning or losing the game HERE.
        // TODO move winning/losing game to here.

        int numBombsAt = this.model.numNeighboringBombs(x, y);

        // we know (x, y) is a bomb.
        if (button.equals("LeftButton") && this.model.isBombAt(x, y)) {
            // draw all the non-flagged bombs on the board.
            for (y = 0; y < height; y += 1) {
                for (x = 0; x < width; x += 1) {
                    if (this.model.isBombAt(x, y) && !this.model.hasBeenFlagged(x, y)) {
                        this.view.drawBomb(x, y);
                    }
                }
            }

            this.view.drawBombCount(this.model.numRemainingBombs(), this.model.numRemainingTiles(), true);

            this.inSession = false;
        }
        // otherwise if the game is Winning?
        else if (this.model.numRemainingBombs() == 0 && this.model.numRemainingTiles() == 0) {
            this.view.drawBombCount(this.model.numRemainingBombs(), this.model.numRemainingTiles(), false);
            this.inSession = false;
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
            // if not clicked, on board, and no bomb
            if (!this.model.hasBeenClicked(x, y) && this.model.onBoard(x, y) && !this.model.isBombAt(x, y)) {
                // if we are number tile, color and next.
                if (this.model.numNeighboringBombs(x, y) > 0) {
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
    }

    // human leftclick.
    private void leftClick(int x, int y) {
        // if we have not been clicked and not been flagged, compute left-click.
        if (!this.model.hasBeenClicked(x, y) && !this.model.hasBeenFlagged(x, y)) {
            // if there is no bomb
            if (!this.model.isBombAt(x, y)) {
                // get the num neighboring bombs.
                int numNeighboringBombs = this.model.numNeighboringBombs(x, y);

                // if we have > 0 bombs in neighboring, mark as #.
                if (numNeighboringBombs > 0) {
                    // has now been clicked.
                    this.model.tileClick(x, y);

                    this.numberTile(x, y, numNeighboringBombs);
                }
                // floodfill the neighboring tiles/zeroes. (we are a zero)
                else {
                    // automatically takes care of tileClick.
                    this.floodFill(x, y);
                }

                // update the bombcount displayed.
                this.view.drawBombCount(this.model.numRemainingBombs(), this.model.numRemainingTiles(), false);
            }
        }
    }

    // color a provided tile by a number.
    private void numberTile(int x, int y, int numNeighboringBombs) {
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
