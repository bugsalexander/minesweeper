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

    // the Model
    private MineModel model;

    // the View
    private MineView view;

    // default constructor
    MineController(MineModel model, MineView view) {
        this.model = model;
        this.view = view;
        this.view.drawBombCount(this.model.numRemainingBombs(), this.model.numRemainingTiles());
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

        // update the bombcount displayed.
        this.view.drawBombCount(this.model.numRemainingBombs(), this.model.numRemainingTiles());
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
                    // automatically takes care of tileClick.
                    this.floodFill(x, y);
                }
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
