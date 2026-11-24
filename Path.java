import java.util.ArrayList;
import java.util.List;

/**
 * Represents a sequence of moves from the player's current
 * position toward a target square. Returned by Vision methods.
 * Includes the total movement, water, and food cost.
 */
public class Path {

    private Square playerPosition;
    private List<Direction> moves;
    private int totalMovementCost;
    private int totalWaterCost;
    private int totalFoodCost;

    /**
     * Constructs an empty Path starting at the given square.
     *
     * @param playerPosition the square the player is in
     */
    public Path(Square playerPosition) {
        this.playerPosition  = playerPosition;
        this.moves           = new ArrayList<>();
        this.totalMovementCost = 0;
        this.totalWaterCost    = 0;
        this.totalFoodCost     = 0;
    }

    /**
     * Adds a move to this path and updates the costs.
     *
     * @param direction    the direction of the move
     * @param movementCost movement cost of the square entered
     * @param waterCost    water cost of the square entered
     * @param foodCost     food cost of the square entered
     */
    public void addMove(Direction direction, int movementCost, int waterCost, int foodCost) {
        moves.add(direction);
        totalMovementCost += movementCost;
        totalWaterCost    += waterCost;
        totalFoodCost     += foodCost;
    }

    /**
     * Returns the starting position of this path.
     *
     * @return player's current square
     */
    public Square getPlayerPosition() {
        return playerPosition;
    }

    /**
     * Returns the list of moves in this path.
     *
     * @return list of directions
     */
    public List<Direction> getPath() {
        return moves;
    }

    /**
     * Returns the total movement cost of this path.
     *
     * @return total movement cost
     */
    public int getTotalMovementCost() {
        return totalMovementCost;
    }

    /**
     * Returns the total water cost of this path.
     *
     * @return total water cost
     */
    public int getTotalWaterCost() {
        return totalWaterCost;
    }

    /**
     * Returns the total food cost of this path.
     *
     * @return total food cost
     */
    public int getTotalFoodCost() {
        return totalFoodCost;
    }

    /**
     * Returns the number of steps in this path.
     *
     * @return path length
     */
    public int getLength() {
        return moves.size();
    }

    @Override
    public String toString() {
        return "Path " + moves +
               " (move:"  + totalMovementCost +
               " water:"  + totalWaterCost    +
               " food:"   + totalFoodCost + ")";
    }
}
