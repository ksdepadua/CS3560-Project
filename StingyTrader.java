public class StingyTrader extends Trader {

    public StingyTrader(Square position) {
        // opening offer: player gives 2 gold, gets 1 water
        // quits after 2 counter-offers
        super(position, new Offer(2, 0, 0,  0, 1, 0), 2);
    }

    @Override
    public boolean acceptOffer(Offer playerOffer) {
        boolean accepted = traderNetValue(playerOffer) >= 3;  // wants a clear profit
        if (accepted) {
            endTrade();
        }
        return accepted;
    }

    @Override
    public Offer counterOffer(Offer playerOffer) {
        if (outOfPatience()) {
            endTrade();
            return null;                                   // gets mad, walks away
        }
        countRound();
        // "add one more gold and you've got a deal"
        return playerOffer.withOfferedGold(playerOffer.getOfferedGold() + 1);
    }
}