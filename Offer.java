/**
 * A trade offer, always from the PLAYER's point of view:
 * "offered" = what the player gives the trader,
 * "requested" = what the player gets from the trader.
 * Offers are immutable; a counter-offer is a new Offer.
 */
public class Offer {

    private final int offeredGold;
    private final int offeredWater;
    private final int offeredFood;
    private final int requestedGold;
    private final int requestedWater;
    private final int requestedFood;

    public Offer(
            int offeredGold,   int offeredWater,   int offeredFood,
            int requestedGold, int requestedWater, int requestedFood) {

        if (offeredGold < 0 || offeredWater < 0 || offeredFood < 0
                || requestedGold < 0 || requestedWater < 0 || requestedFood < 0) {
            throw new IllegalArgumentException("Offer amounts cannot be negative");
        }
        if (offeredGold + offeredWater + offeredFood == 0
                || requestedGold + requestedWater + requestedFood == 0) {
            throw new IllegalArgumentException("Both sides of an offer must contain something");
        }

        this.offeredGold    = offeredGold;
        this.offeredWater   = offeredWater;
        this.offeredFood    = offeredFood;
        this.requestedGold  = requestedGold;
        this.requestedWater = requestedWater;
        this.requestedFood  = requestedFood;
    }

    public int getOfferedGold()    { return offeredGold; }
    public int getOfferedWater()   { return offeredWater; }
    public int getOfferedFood()    { return offeredFood; }
    public int getRequestedGold()  { return requestedGold; }
    public int getRequestedWater() { return requestedWater; }
    public int getRequestedFood()  { return requestedFood; }

    // ---------- Helpers for building counter-offers ----------

    public Offer withOfferedGold(int gold) {
        return new Offer(gold, offeredWater, offeredFood,
                         requestedGold, requestedWater, requestedFood);
    }

    public Offer withOfferedWater(int water) {
        return new Offer(offeredGold, water, offeredFood,
                         requestedGold, requestedWater, requestedFood);
    }

    public Offer withOfferedFood(int food) {
        return new Offer(offeredGold, offeredWater, food,
                         requestedGold, requestedWater, requestedFood);
    }

    public Offer withRequestedGold(int gold) {
        return new Offer(offeredGold, offeredWater, offeredFood,
                         gold, requestedWater, requestedFood);
    }

    public Offer withRequestedWater(int water) {
        return new Offer(offeredGold, offeredWater, offeredFood,
                         requestedGold, water, requestedFood);
    }

    public Offer withRequestedFood(int food) {
        return new Offer(offeredGold, offeredWater, offeredFood,
                         requestedGold, requestedWater, food);
    }

    // ---------- Object overrides ----------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Offer)) return false;
        Offer other = (Offer) o;
        return offeredGold == other.offeredGold
            && offeredWater == other.offeredWater
            && offeredFood == other.offeredFood
            && requestedGold == other.requestedGold
            && requestedWater == other.requestedWater
            && requestedFood == other.requestedFood;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(offeredGold, offeredWater, offeredFood,
                                      requestedGold, requestedWater, requestedFood);
    }

    @Override
    public String toString() {
        return "Offer [giving: gold=" + offeredGold
             + " water=" + offeredWater
             + " food="  + offeredFood
             + " | receiving: gold=" + requestedGold
             + " water=" + requestedWater
             + " food="  + requestedFood + "]";
    }
}