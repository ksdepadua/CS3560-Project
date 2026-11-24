/**
 * A goal-oriented brain that pushes east as aggressively
 * as possible. Only detours for resources when they get
 * dangerously low, and only rests when it has to.
 */
public class GoalOrientedBrain extends Brain {

    /** Detour for resources at or below this amount. */
    private static final int CRITICAL = 3;

    /**
     * Makes a move based on a goal-oriented strategy.
     *
     * @param player the player being controlled
     */
    @Override
    public void makeMove(Player player) {
        Vision vision = player.getVision();
        vision.updatePosition(player.getRow(), player.getCol());

        boolean waterCritical = player.getCurrentWater() <= CRITICAL;
        boolean foodCritical  = player.getCurrentFood()  <= CRITICAL;

        // 1. Only detour if critically low
        if (waterCritical || foodCritical) {
            if (waterCritical && tryMove(player, vision.closestWater())) return;
            if (foodCritical  && tryMove(player, vision.closestFood()))  return;
            if (tryMove(player, vision.closestTrader())) return;
        }

        // 2. Cheapest step east (E, NE, or SE)
        if (moveEastCheapest(player)) return;

        // 3. Blocked: rest to rebuild strength, or step around
        if (player.getCurrentStrength() < player.getMaxStrength()
                && canRestSafely(player)) {
            player.rest();
            return;
        }
        if (sidestep(player)) return;

        // 4. Last resort
        player.rest();
    }
}