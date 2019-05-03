import tester.Tester;


/**
 * Examples class to run the game.
 * MineSweeper game.
 *
 * @author Alex Takayama
 * @since 2019-05-1
 */
class ExamplesMine {

    void testView(Tester t) {
        // currently set to medium
        int width = 16; // 16
        int height = 16; // 16
        int bombs = 40; // 40

        MineModel model = new MineModel(width, height, bombs);
        MineView view = new MineView(width, height);
        MineController tv = new MineController(model, view);

        System.out.println(model);

        tv.bigBang(width * MineView.TILE_SIZE, height * MineView.TILE_SIZE, 0.1);
    }
}