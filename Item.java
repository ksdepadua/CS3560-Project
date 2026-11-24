/**
 * Represents an item that can be found in a square.
 * Items can be food, water, or gold bonuses.
 * Some items are repeating (collectible once per turn),
 * while others are one-time only.
 */
public class Item {

    private String name;
    private boolean repeating;
    private boolean taken;
    private Square position;

    /**
     * Constructs an Item with the given properties.
     *
     * @param name      the name of the item
     * @param repeating whether collectible multiple times
     * @param position  the square this item is in
     */
    public Item(String name, boolean repeating, Square position) {
        this.name      = name;
        this.repeating = repeating;
        this.taken     = false;
        this.position  = position;
    }

    /**
     * Returns the name of this item.
     *
     * @return item name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns whether this item can be collected multiple times.
     *
     * @return true if repeating
     */
    public boolean isRepeating() {
        return repeating;
    }

    /**
     * Returns whether this item has already been taken.
     *
     * @return true if taken
     */
    public boolean isTaken() {
        return taken;
    }

    /**
     * Returns the square this item is located in.
     *
     * @return position square
     */
    public Square getPosition() {
        return position;
    }

    /**
     * Returns the bonus amount this item provides.
     * Subclasses override this to return their specific bonus.
     *
     * @return bonus amount
     */
    public int getBonusAmount() {
        return 0;
    }

    /**
     * Collects this item if it is available.
     * Marks non-repeating items as taken.
     *
     * @return true if the item was successfully collected
     */
    public boolean collect() {
        if (taken) {
            return false;
        }
        if (!repeating) {
            taken = true;
        }
        return true;
    }

    /**
     * Resets the item so it can be collected again.
     * Used when resetting the map for a new player.
     */
    public void reset() {
        taken = false;
    }

    @Override
    public String toString() {
        return name + " (" + (repeating ? ", repeating" : "") + ")";
    }
}
