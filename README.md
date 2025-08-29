# Minesweeper Game

A Java implementation of the classic Minesweeper game with a graphical user interface using Swing.

## Features

- Three difficulty levels: Easy, Medium, and Hard
- Graphical user interface with clickable tiles
- Right-click to flag potential mines
- Recursive clearing of empty areas
- Win/lose condition detection
- Restart functionality

## Difficulty Levels

- **Easy**: 10-14 mines on an 8x8 grid
- **Medium**: 15-19 mines on an 8x8 grid  
- **Hard**: 20-24 mines on an 8x8 grid

## How to Play

1. Select a difficulty level from the initial menu
2. Left-click on tiles to reveal them
3. Right-click on tiles to place/remove flags
4. Reveal all non-mine tiles to win
5. Avoid clicking on mines

## Controls

- **Left-click**: Reveal tile
- **Right-click**: Place/remove flag
- **Restart button**: Start a new game

## Requirements

- Java JDK 8 or higher
- Swing library (included with Java)

## Running the Game

Compile and run the `Minesweeper.java` file:

```bash
javac Minesweeper.java
java Minesweeper
```