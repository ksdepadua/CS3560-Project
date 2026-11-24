/**
 * A trader living in a square on the map. Always repeating.
 * All offers are from the PLAYER's point of view:
 * "offered" = what the player gives, "requested" = what the player gets.
 */
public abstract class Trader extends Item {

    protected Offer offer;         // the trader's opening offer
    private final int patience;    // counter-offers before walking away
    private int rounds;            // counter-offers made in the current trade

    /**
     * @param position square this trader is in
     * @param offer    the trader's opening offer
     * @param patience number of counter-offers before the trader quits
     */
    protected Trader(Square position, Offer offer, int patience) {
        // Every trader is named "Trader" so the player can't tell types apart
        super("Trader", true, position);
        this.offer = offer;
        this.patience = patience;
    }

    // ---------- Methods from the class diagram ----------

    /**
     * Decides whether to accept the player's offer.
     *
     * @param playerOffer the player's offer
     * @return true if the trader accepts
     */
    public abstract boolean acceptOffer(Offer playerOffer);

    /**
     * Makes a counter-offer.
     *
     * @param playerOffer the player's offer
     * @return a new offer, or null if the trader walks away
     */
    public abstract Offer counterOffer(Offer playerOffer);

    /**
     * Returns the trader's opening offer.
     *
     * @return opening offer
     */
    public Offer getOffer() {
        return offer;              // Offer is immutable, so no copy needed
    }

    // ---------- Helpers for subclasses ----------

    /** Value of one unit of a resource to this trader. */
    protected int weight(String resource) {
        switch (resource) {
            case "Gold":  return 3;
            case "Water": return 2;
            case "Food":  return 2;
            default:      return 0;
        }
    }

    /** Positive = trader comes out ahead. */
    protected int traderNetValue(Offer o) {
        int receives = o.getOfferedGold()  * weight("Gold")
                     + o.getOfferedWater() * weight("Water")
                     + o.getOfferedFood()  * weight("Food");
        int gives    = o.getRequestedGold()  * weight("Gold")
                     + o.getRequestedWater() * weight("Water")
                     + o.getRequestedFood()  * weight("Food");
        return receives - gives;
    }

    protected boolean outOfPatience() {
        return rounds >= patience;
    }

    protected void countRound() {
        rounds++;
    }

    protected void endTrade() {
        rounds = 0;
    }

    // ---------- Item overrides ----------

    /** A trader is never used up. */
    @Override
    public boolean collect() {
        return true;
    }

    @Override
    public void reset() {
        super.reset();
        endTrade();
    }

    @Override
    public String toString() {
        return "Trader";
    }
}