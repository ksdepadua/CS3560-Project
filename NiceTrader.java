public class NiceTrader extends Trader {

    public NiceTrader(Square position) {
        // opening offer: player gives 1 gold, gets 1 water
        // willing to haggle 5 times
        super(position, new Offer(1, 0, 0,  0, 1, 0), 5);
    }

    @Override
    public boolean acceptOffer(Offer playerOffer) {
        boolean accepted = traderNetValue(playerOffer) >= 0;  // fair trade is fine
        if (accepted) {
            endTrade();
        }
        return accepted;
    }

    @Override
    public Offer counterOffer(Offer playerOffer) {
        if (outOfPatience()) {
            endTrade();
            return null;
        }
        countRound();
        // "throw in one more food"
        return playerOffer.withOfferedFood(playerOffer.getOfferedFood() + 1);
    }
}