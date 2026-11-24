/**
 * Represents a food bonus item that restores food to the player.
 */
public class FoodBonus extends Item {

    private int amount;

    /**
     * Constructs a FoodBonus item.
     *
     * @param name      the name of the item
     * @param repeating whether collectible multiple times
     * @param position  the square this item is in
     * @param amount    the amount of food this bonus provides
     */
    public FoodBonus(
            String name, boolean repeating,
            Square position, int amount) {
        super("food", repeating, position);
        this.amount = amount;
    }

    /**
     * Returns the amount of food this bonus provides.
     *
     * @return food bonus amount
     */
    @Override
    public int getBonusAmount() {
        return amount;
    }
}
