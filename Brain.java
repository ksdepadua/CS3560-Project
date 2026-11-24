/**
 * Decides how the player moves each turn.
 * Subclasses (CautiousBrain, GoalOrientedBrain) define the
 * strategy in makeMove(); this class provides the shared
 * movement helpers they both use.
 */
public abstract class Brain {

    protected String[] playerAttributes;
    protected String bonuses;

    /**
     * Decides and performs the player's action for this turn.
     *
     * @param player the player being controlled
     */
    public abstract void makeMove(Player player);

    // ---------- Shared helpers for subclasses ----------

    /**
     * Moves one step in the cheapest safe eastward direction
     * (EAST, NORTHEAST, or SOUTHEAST). EAST wins ties.
     *
     * @param player the player to move
     * @return true if the player moved
     */
    protected boolean moveEastCheapest(Player player) {
        return moveCheapest(player, new Direction[] {
            Direction.EAST, Direction.NORTHEAST, Direction.SOUTHEAST
        });
    }

    /**
     * Steps north or south (whichever is cheaper and safe)
     * to get around terrain that blocks the way east.
     *
     * @param player the player to move
     * @return true if the player moved
     */
    protected boolean sidestep(Player player) {
        return moveCheapest(player, new Direction[] {
            Direction.NORTH, Direction.SOUTH
        });
    }

    /**
     * Moves in whichever of the given directions is safe and
     * has the lowest movement cost. Earlier directions win ties.
     *
     * @param player  the player to move
     * @param options directions to choose from
     * @return true if the player moved
     */
    private boolean moveCheapest(Player player, Direction[] options) {
        Direction best = null;
        int bestCost = Integer.MAX_VALUE;

        for (Direction d : options) {
            if (!MapUtils.isSafeMove(player, d)) {
                continue;
            }
            int cost = MapUtils.getNextSquare(player, d)
                               .getTerrain().getMovementCost();
            if (cost < bestCost) {
                best = d;
                bestCost = cost;
            }
        }

        if (best == null) {
            return false;
        }
        player.move(best);
        return true;
    }

    /**
     * Returns true if resting this turn won't use up the
     * player's last water or food.
     *
     * @param player the player to check
     * @return true if resting is safe
     */
    protected boolean canRestSafely(Player player) {
        Terrain t = player.getMap()
            .getSquare(player.getCol(), player.getRow())
            .getTerrain();
        int waterCost = (int) Math.ceil(t.getWaterCost() / 2.0);
        int foodCost  = (int) Math.ceil(t.getFoodCost()  / 2.0);
        return player.getCurrentWater() > waterCost
            && player.getCurrentFood()  > foodCost;
    }

    /**
     * Moves one step along a path if that step is safe.
     *
     * @param player the player to move
     * @param path   the path to follow (may be null)
     * @return true if the player moved
     */
    protected boolean tryMove(Player player, Path path) {
        if (path == null || path.getPath().isEmpty()) {
            return false;
        }
        Direction next = path.getPath().get(0);
        if (MapUtils.isSafeMove(player, next)) {
            player.move(next);
            return true;
        }
        return false;
    }
}