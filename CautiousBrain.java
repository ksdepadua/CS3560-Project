/**
 * A cautious brain that prioritizes survival.
 * Seeks water first if low, then food, then a trader
 * if resources are getting low. Rests to rebuild strength
 * before it runs out, otherwise takes the cheapest step east.
 */
public class CautiousBrain extends Brain {

    private static final int LOW_THRESHOLD = 3;

    /** Rest when strength drops below this fraction of max. */
    private static final double REST_BELOW = 0.4;

    /**
     * Makes a move based on a cautious survival strategy.
     *
     * @param player the player being controlled
     */
    @Override
    public void makeMove(Player player) {
        Vision vision = player.getVision();
        vision.updatePosition(player.getRow(), player.getCol());

        // 1. Seek water if low
        if (player.getCurrentWater() <= LOW_THRESHOLD) {
            if (tryMove(player, vision.closestWater())) return;
        }

        // 2. Seek food if low
        if (player.getCurrentFood() <= LOW_THRESHOLD) {
            if (tryMove(player, vision.closestFood())) return;
        }

        // 3. Seek a trader if water or food is getting low
        if (player.getCurrentWater() <= LOW_THRESHOLD + 1
                || player.getCurrentFood() <= LOW_THRESHOLD + 1) {
            if (tryMove(player, vision.closestTrader())) return;
        }

        // 4. Rest before strength runs out
        double strengthRatio =
            (double) player.getCurrentStrength() / player.getMaxStrength();
        if (strengthRatio < REST_BELOW && canRestSafely(player)) {
            player.rest();
            return;
        }

        // 5. Cheapest step east (E, NE, or SE)
        if (moveEastCheapest(player)) return;

        // 6. Blocked: rest to rebuild strength, or step around
        if (player.getCurrentStrength() < player.getMaxStrength()
                && canRestSafely(player)) {
            player.rest();
            return;
        }
        if (sidestep(player)) return;

        // 7. Last resort
        player.rest();
    }
}