import java.util.List;

/**
 * Represents the player attempting to cross the map from
 * west to east. Tracks current and maximum resource levels
 * and delegates decision-making to the Brain and Vision.
 */
public class Player {

    private int maxStrength;
    private int maxWater;
    private int maxFood;
    private int currentStrength;
    private int currentWater;
    private int currentFood;
    private int currentGold;
    private int row;
    private int col;
    private Vision vision;
    private Brain brain;
    private Map map;

    /** Max rounds of negotiation per trader visit. */
    private static final int MAX_TRADE_ROUNDS = 3;

    /**
     * Constructs a Player with the given starting values.
     *
     * @param maxStrength starting and maximum strength
     * @param maxWater    starting and maximum water
     * @param maxFood     starting and maximum food
     * @param vision      the Vision object for this player
     * @param brain       the Brain object for this player
     * @param map         the game map
     */
    public Player(
            int maxStrength, int maxWater, int maxFood,
            Vision vision, Brain brain, Map map) {
        this.maxStrength     = maxStrength;
        this.maxWater        = maxWater;
        this.maxFood         = maxFood;
        this.currentStrength = maxStrength;
        this.currentWater    = maxWater;
        this.currentFood     = maxFood;
        this.currentGold     = 0;
        this.vision          = vision;
        this.brain           = brain;
        this.map             = map;
        this.col             = 0;
        this.row             = map.getHeight() / 2;
        vision.updatePosition(row, col);
    }

    /**
     * Executes one full turn for the player.
     * Brain decides the move, then items are collected,
     * and a trade is attempted if a trader is present.
     */
    public void takeTurn() {
        brain.makeMove(this);
        Square current = map.getSquare(col, row);
        collectItems(current);
        if (current.hasTrader()) {
            conductTrade(current.getTrader());
        }
    }

    // ---------- Trading ----------

    /**
     * Negotiates with the given trader, following the Trader
     * state diagram: the trader opens, the player either takes
     * the deal or asks for more, and the trader accepts, counters,
     * or walks away. Up to MAX_TRADE_ROUNDS rounds are allowed.
     *
     * @param trader the trader to negotiate with
     */
    public void conductTrade(Trader trader) {
        Display.printEvent("A trader is here!");

        Offer current = trader.getOffer();
        Display.printEvent("Trader offers: " + current);

        for (int round = 0;
                current != null && round < MAX_TRADE_ROUNDS;
                round++) {

            // Can't pay what's on the table, so asking for more won't help
            if (!canAffordOffer(current)) {
                break;
            }

            // 1. Take the current deal if it gives us something we need
            if (needsOfferGoods(current) && trader.acceptOffer(current)) {
                applyTrade(current);
                Display.printEvent("Trade accepted!");
                return;
            }

            // 2. Ask for a little more
            Offer playerCounter = buildCounterOffer(current);
            Display.printEvent("Player counter-offers: " + playerCounter);

            if (trader.acceptOffer(playerCounter)) {
                applyTrade(playerCounter);
                Display.printEvent("Trader accepted our counter!");
                return;
            }

            // 3. Trader counters, or returns null and walks away
            current = trader.counterOffer(playerCounter);
            if (current == null) {
                Display.printEvent("The trader got fed up and left.");
            } else {
                Display.printEvent("Trader counters: " + current);
            }
        }

        trader.endTrade();      // back to WaitingForPlayer for next visit
        Display.printEvent("No trade was made.");
    }

    /**
     * Returns true if the player can afford to give away
     * the resources specified in the offer without dying.
     *
     * @param offer the offer to check
     * @return true if affordable
     */
    private boolean canAffordOffer(Offer offer) {
        return (currentWater - offer.getOfferedWater()) > 1
            && (currentFood  - offer.getOfferedFood())  > 1
            && (currentGold  - offer.getOfferedGold())  >= 0;
    }

    /**
     * Returns true if the player actually needs what the
     * offer provides (water or food below half max, or gold).
     *
     * @param offer the offer to evaluate
     * @return true if the received goods are needed
     */
    private boolean needsOfferGoods(Offer offer) {
        boolean needWater =
            offer.getRequestedWater() > 0
            && currentWater < maxWater / 2;
        boolean needFood  =
            offer.getRequestedFood() > 0
            && currentFood  < maxFood  / 2;
        boolean needGold  =
            offer.getRequestedGold() > 0;
        return needWater || needFood || needGold;
    }

    /**
     * Builds a counter-offer that asks for one extra unit
     * of whichever resource the player needs most.
     *
     * @param offer the offer currently on the table
     * @return a new offer more favorable to the player
     */
    private Offer buildCounterOffer(Offer offer) {
        if (currentWater < currentFood) {
            return offer.withRequestedWater(offer.getRequestedWater() + 1);
        }
        return offer.withRequestedFood(offer.getRequestedFood() + 1);
    }

    /**
     * Applies a completed trade offer to the player's resources.
     * The player pays the "offered" side and receives the
     * "requested" side.
     *
     * @param offer the accepted trade offer
     */
    public void applyTrade(Offer offer) {
        spend(0,
            offer.getOfferedWater(),
            offer.getOfferedFood(),
            offer.getOfferedGold(), 0);
        gain(0,
            offer.getRequestedWater(),
            offer.getRequestedFood(),
            offer.getRequestedGold(), 0);
        Display.printEvent("Trade applied: " + offer);
    }

    // ---------- Movement ----------

    /**
     * Moves the player one step in the given direction
     * and deducts the terrain costs of the new square.
     *
     * @param direction the direction to move
     * @return true if the move was successful
     */
    public boolean move(Direction direction) {
        int newRow = row;
        int newCol = col;

        switch (direction) {
            case NORTH:     newRow--;           break;
            case SOUTH:     newRow++;           break;
            case EAST:      newCol++;           break;
            case WEST:      newCol--;           break;
            case NORTHEAST: newRow--; newCol++; break;
            case NORTHWEST: newRow--; newCol--; break;
            case SOUTHEAST: newRow++; newCol++; break;
            case SOUTHWEST: newRow++; newCol--; break;
        }

        if (newRow < 0 || newRow >= map.getHeight()
                || newCol < 0 || newCol >= map.getWidth()) {
            return false;
        }

        Square target = map.getSquare(newCol, newRow);
        if (!canEnter(target)) {
            return false;
        }

        Terrain t = target.getTerrain();
        spend(t.getMovementCost(),
              t.getWaterCost(),
              t.getFoodCost(), 0, 0);

        row = newRow;
        col = newCol;
        vision.updatePosition(row, col);    // keep Vision in sync
        Display.printEvent(
            "Moved " + direction +
            " to (" + col + "," + row + ")"
        );
        return true;
    }

    /**
     * The player stays in the current square for one turn.
     * Regains 2 strength, uses half water and food cost.
     */
    public void rest() {
        Square current = map.getSquare(col, row);
        Terrain t = current.getTerrain();
        int halfWater = (int) Math.ceil(t.getWaterCost() / 2.0);
        int halfFood  = (int) Math.ceil(t.getFoodCost()  / 2.0);
        spend(0, halfWater, halfFood, 0, 0);
        gain(2, 0, 0, 0, 0);
        Display.printEvent(
            "Rested at (" + col + "," + row + ")"
        );
    }

    /**
     * Returns whether the player has enough resources
     * to enter the given square.
     *
     * @param square the target square
     * @return true if the player can enter
     */
    public boolean canEnter(Square square) {
        if (square == null) return false;
        Terrain t = square.getTerrain();
        return currentStrength >= t.getMovementCost()
            && currentWater    >= t.getWaterCost()
            && currentFood     >= t.getFoodCost();
    }

    // ---------- Items ----------

    /**
     * Collects all available bonus items in the given square.
     * Traders are skipped here; takeTurn() handles them.
     *
     * @param square the square to collect from
     */
    public void collectItems(Square square) {
        List<Item> items = square.getItems();
        for (Item item : items) {
            if (item instanceof Trader) {
                continue;
            }
            if (!item.collect()) {
                continue;               // already taken
            }

            int amount = item.getBonusAmount();
            if (item instanceof FoodBonus) {
                gain(0, 0, amount, 0, 0);
            } else if (item instanceof WaterBonus) {
                gain(0, amount, 0, 0, 0);
            } else if (item instanceof GoldBonus) {
                gain(0, 0, 0, amount, 0);
            }
            Display.printEvent("Collected: " + item);
        }
    }

    // ---------- Resources ----------

    /**
     * Increases the player's current resource values.
     * Values are capped at their maximums.
     *
     * @param strength amount of strength to gain
     * @param water    amount of water to gain
     * @param food     amount of food to gain
     * @param gold     amount of gold to gain
     * @param unused   reserved for future use
     */
    public void gain(
            int strength, int water,
            int food, int gold, int unused) {
        currentStrength =
            Math.min(maxStrength, currentStrength + strength);
        currentWater =
            Math.min(maxWater, currentWater + water);
        currentFood =
            Math.min(maxFood, currentFood + food);
        currentGold += gold;
    }

    /**
     * Decreases the player's current resource values.
     *
     * @param strength amount of strength to spend
     * @param water    amount of water to spend
     * @param food     amount of food to spend
     * @param gold     amount of gold to spend
     * @param unused   reserved for future use
     */
    public void spend(
            int strength, int water,
            int food, int gold, int unused) {
        currentStrength -= strength;
        currentWater    -= water;
        currentFood     -= food;
        currentGold     -= gold;
    }

    /**
     * Returns whether the player has escaped the east edge.
     *
     * @return true if at or past the east edge
     */
    public boolean hasEscaped() {
        return col >= map.getWidth() - 1;
    }

    /**
     * Returns whether the player is dead (out of resources).
     *
     * @return true if any resource is at zero or below
     */
    public boolean isDead() {
        return currentStrength <= 0
            || currentWater    <= 0
            || currentFood     <= 0;
    }

    // --- Getters ---

    public int getMaxStrength()     { return maxStrength; }
    public int getMaxWater()        { return maxWater; }
    public int getMaxFood()         { return maxFood; }
    public int getCurrentStrength() { return currentStrength; }
    public int getCurrentWater()    { return currentWater; }
    public int getCurrentFood()     { return currentFood; }
    public int getCurrentGold()     { return currentGold; }
    public int getRow()             { return row; }
    public int getCol()             { return col; }
    public Vision getVision()       { return vision; }
    public Brain getBrain()         { return brain; }
    public Map getMap()             { return map; }

    @Override
    public String toString() {
        return "Player [pos=(" + col + "," + row + ")" +
               " str="   + currentStrength + "/" + maxStrength +
               " water=" + currentWater    + "/" + maxWater    +
               " food="  + currentFood     + "/" + maxFood     +
               " gold="  + currentGold + "]";
    }
}