# WSS — Wilderness Survival System

A text-based survival game built for CS3560. The player starts on the west edge of a map and attempts to cross to the east edge, managing strength, water, and food while navigating terrain, collecting items, and trading with traders.

---

## How to Run

```bash
# Clone the repository
git clone https://github.com/ksdepadua/CS3560-Project.git
cd CS3560-Project

# Run the program (update filename to match your main file)
python main.py
```

When the program starts it will ask for:
- **Map size** (width and height)
- **Difficulty** (Easy, Medium, or Hard)

The game then creates the map, places terrain, items, and traders, and sends the player across.

## Class Overview

### Game
Controls the overall flow of the program. Holds a `Difficulty` setting and sets up the map and player.

### Difficulty (`Easy`, `Medium`, `Hard`)
Subclasses of `Difficulty`. Each sets the trader behavior, brain type, and map configuration differently.

### Map
A rectangular grid of `Square` objects. Stores width, height, and the 2D grid. Responsible for generating terrain and placing items based on difficulty.

### Square
A single cell on the map. Contains a `Terrain` type and optionally one or more `Item` objects.

### Terrain
Defines the properties of a square: movement cost, water cost, and food cost. Each terrain type (plains, mountain, desert, swamp, forest) has different costs.

### Item
Represents a collectible in a square. Has a name, bonus type (food/water/gold), whether it is repeating, and whether it has been taken. Subclasses: `FoodBonus`, `WaterBonus`, `GoldBonus`.

### Trader (`StingyTrader`, `NiceTrader`)
A special item in a square. The player can propose trades (food/water/gold exchanges). The trader can accept, counter-offer, or reject. `StingyTrader` is harder to deal with; `NiceTrader` is more generous.

### Player
The main character. Tracks max and current values for strength, water, food, and gold. Has a `Vision` object and a `Brain` object. Can move, rest, collect items, and trade.

### Vision
Looks at neighboring squares and returns `Path` objects. Methods include:
- `closestFood()`, `closestWater()`, `closestGold()`, `closestTrader()`
- `easiestPath()`
- `secondClosestFood()`, `secondClosestWater()`, `secondClosestGold()`, `secondClosestTrader()`

Subclasses have different scopes of visibility.

### Brain (`CautiousBrain`, `GoalOrientedBrain`)
Decides what the player does each turn. Calls `Vision` to find nearby options, evaluates the player's current resource levels, then tells the player to move, rest, or trade.

### Path
A short sequence of moves (e.g. MoveNorth, MoveEast) returned by `Vision`. Includes a summary of movement, water, and food cost.

---

## Diagrams

All design diagrams are located in the `/diagrams` folder:

| Diagram | Description |
|---|---|
| Class Diagram | All classes, attributes, methods, and relationships |
| Player State Diagram | Player lifecycle from initialization to escaped/dead |
| Brain State Diagram | How the Brain decides each turn |
| Trader State Diagram | Trader responses to offers and counteroffers |
| Game State Diagram | Overall game flow from setup to win/game over |
| Vision State Diagram | How Vision searches for and returns the best path |
| Activity Diagram | Player's turn broken down step by step |
| Use Case: Move Squares | Sequence model for player movement |
| Use Case: Trade | Sequence model for a trader interaction |

---

## Authors

- Michelle Lee
- Keana De Padua
- Angelina Liu
