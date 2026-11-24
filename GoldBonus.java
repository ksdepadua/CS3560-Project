/**
 * Represents a gold bonus item that gives gold to the player.
 */
public class GoldBonus extends Item {

    private int amount;

    /**
     * Constructs a GoldBonus item.
     *
     * @param name      the name of the item
     * @param repeating whether collectible multiple times
     * @param position  the square this item is in
     * @param amount    the amount of gold this bonus provides
     */
    public GoldBonus(
            String name, boolean repeating,
            Square position, int amount) {
        super("gold", repeating, position);
        this.amount = amount;
    }

    /**
     * Returns the amount of gold this bonus provides.
     *
     * @return gold bonus amount
     */
    @Override
    public int getBonusAmount() {
        return amount;
    }
}
