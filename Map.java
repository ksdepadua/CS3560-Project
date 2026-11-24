import java.util.Random;

/**
 * Represents the game map — a rectangular grid of squares.
 * Responsible for generating terrain and placing items
 * based on the selected difficulty.
 *
 * Terrain costs are balanced for a player who starts with
 * 1.5x the map width in strength, water, and food
 * (30 each on a 20-wide map; see Game.createPlayer).
 */
public class Map {

    private int width;
    private int height;
    private Square[][] gridMap;

    // Terrain types: (name, moveCost, waterCost, foodCost)
    // Costs are kept low so the game is survivable
    private static final Terrain PLAINS =
        new Terrain("plains",   1, 1, 1);
    private static final Terrain MOUNTAIN =
        new Terrain("mountain", 3, 1, 2);
    private static final Terrain DESERT =
        new Terrain("desert",   2, 2, 1);
    private static final Terrain SWAMP =
        new Terrain("swamp",    2, 1, 2);
    private static final Terrain FOREST =
        new Terrain("forest",   2, 1, 1);

    /**
     * Constructs a Map with the given dimensions.
     *
     * @param width  number of squares from west to east
     * @param height number of squares from north to south
     */
    public Map(int width, int height) {
        this.width   = width;
        this.height  = height;
        this.gridMap = new Square[height][width];
    }

    /**
     * Returns the width of the map.
     *
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the map.
     *
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the square at the given coordinates.
     *
     * @param x column (west to east)
     * @param y row (north to south)
     * @return the square at (x, y)
     */
    public Square getSquare(int x, int y) {
        return gridMap[y][x];
    }

    /**
     * Returns the full grid of squares.
     *
     * @return 2D array of squares
     */
    public Square[][] getGridMap() {
        return gridMap;
    }

    /**
     * Populates the map with terrain based on difficulty.
     * Easy: mostly plains.
     * Medium: mixed terrain.
     * Hard: heavy mountains and desert.
     *
     * @param difficulty "easy", "medium", or "hard"
     */
    public void generate(String difficulty) {
        Random rand = new Random();
        Terrain[] options;

        switch (difficulty.toLowerCase()) {
            case "easy":
                options = new Terrain[]{
                    PLAINS, PLAINS, PLAINS, FOREST, PLAINS
                };
                break;
            case "hard":
                options = new Terrain[]{
                    MOUNTAIN, DESERT, DESERT, SWAMP, MOUNTAIN
                };
                break;
            default:
                options = new Terrain[]{
                    PLAINS, FOREST, MOUNTAIN, DESERT, SWAMP
                };
                break;
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Terrain t = options[rand.nextInt(options.length)];
                gridMap[y][x] = new Square(t);
            }
        }

        placeItems(difficulty, rand);
    }

    /**
     * Places items and traders on the map based on difficulty.
     *
     * @param difficulty the difficulty string
     * @param rand       random number generator
     */
    private void placeItems(String difficulty, Random rand) {
        int itemCount;
        int traderCount;

        switch (difficulty.toLowerCase()) {
            case "easy":
                itemCount   = (width * height) / 4;
                traderCount = 4;
                break;
            case "hard":
                itemCount   = (width * height) / 8;
                traderCount = 1;
                break;
            default:
                itemCount   = (width * height) / 6;
                traderCount = 2;
                break;
        }

        for (int i = 0; i < itemCount; i++) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            Square sq = gridMap[y][x];
            int type = rand.nextInt(3);
            boolean repeating = rand.nextBoolean();
            int amount = rand.nextInt(4) + 2; // 2-5

            if (type == 0) {
                sq.addItem(
                    new FoodBonus("Food", repeating, sq, amount)
                );
            } else if (type == 1) {
                sq.addItem(
                    new WaterBonus("Water", repeating, sq, amount)
                );
            } else {
                sq.addItem(
                    new GoldBonus(
                        "Gold", false, sq,
                        rand.nextInt(5) + 1
                    )
                );
            }
        }

        for (int i = 0; i < traderCount; i++) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            Square sq = gridMap[y][x];
            if (!sq.hasTrader()) {
                Trader trader = rand.nextBoolean()
                    ? new NiceTrader(sq)
                    : new StingyTrader(sq);
                sq.setTrader(trader);
            }
        }
    }

    /**
     * Resets all squares on the map (for a new player run).
     */
    public void reset() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                gridMap[y][x].reset();
            }
        }
    }

    /**
     * Prints a simple text representation of the map.
     */
    public void printMap() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                String t =
                    gridMap[y][x].getTerrain().getTerrainName();
                System.out.print(t.charAt(0) + " ");
            }
            System.out.println();
        }
    }
}