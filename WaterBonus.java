/**
 * Represents a water bonus item that restores water to the player.
 */
public class WaterBonus extends Item {

    private int amount;

    /**
     * Constructs a WaterBonus item.
     *
     * @param name      the name of the item
     * @param repeating whether collectible multiple times
     * @param position  the square this item is in
     * @param amount    the amount of water this bonus provides
     */
    public WaterBonus(String name, boolean repeating, Square position, int amount) {
        super("water", repeating, position);
        this.amount = amount;
    }

    /**
     * Returns the amount of water this bonus provides.
     *
     * @return water bonus amount
     */
    @Override
    public int getBonusAmount() {
        return amount;
    }
}
