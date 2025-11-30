# Jackaroo Game (Java / JavaFX)

A full object-oriented implementation of the Jackaroo board game written in Java, based on the official rules and descriptions from the GIU Programming II Project Specification.

This version is a single-player adaptation where the human player competes against three CPU players. The game includes a complete movement engine, board logic, card hierarchy, rule enforcement, CPU decision-making, and a JavaFX-based user interface.

## Game Overview

Jackaroo is a strategic card-and-marble racing game. Each player has four marbles that start in Home, must be fielded onto the Base cell, move around the track, enter the Safe Zone, and finish the game by placing all marbles safely.

## In this adaptation:

One human player competes against three CPU players.

The deck contains 102 cards defined in Cards.csv.

The game implements all rules described in the project specification.

A complete turn system is implemented with CPU automation.

Source reference for rules: GIU Programming II Project Description (uploaded PDF).

## Game Zones
### Home Zone

Marbles begin here. Marbles in Home are inactive and cannot move until fielded using Ace or King. Destroyed marbles return here.

### Base Cell

The first track cell for each player. Fielding places a marble here. A marble in the Base cell blocks other players from passing.

### Safe Zone Entry

Located two cells before each player’s Base. If occupied, no marble can enter the Safe Zone.

### Safe Zone

Contains four cells per player. Marbles require exact movement to enter and fill all safe cells. Once inside, the marble is protected and cannot leave or be destroyed.

### Firepit

The discard pile. When the deck becomes empty, discarded cards refill the deck.

### Trap Cells

Eight cells randomly assigned at the start of each round. Landing on a trap destroys the marble and assigns a new trap location.

### Card System

The deck contains 102 cards with attributes such as code, name, frequency, rank, suit, and description. Cards are categorized into Standard Cards and Wild Cards.

### Standard Cards

Examples:

Ace: Field a marble or move 1 step.

King: Field a marble or move 13 steps while destroying marbles passed.

Queen: Discard an opponent’s card or move as normal.

Jack: Swap positions with another marble.

Seven: Split movement into two positive parts that sum to seven.

Ten: Skip an opponent’s next turn.

### Wild Cards

Burner: Destroy any opponent marble on the track, returning it to Home.

Saver: Move one of your marbles directly to your Safe Zone.

All card mechanics follow the rules in the project specification.

Gameplay Flow
Game Start

Player enters a name.

Three CPU opponents are initialized.

Colors and initial zones are assigned.

## Rounds

Each round consists of four turns per player.

Each player receives four cards at the beginning of a round.

The round ends when all cards are played or discarded.

## Player Turn

Depending on the card drawn, the player may:

Field a marble

Move a marble

Swap two marbles

Burn a marble

Save a marble to Safe Zone

Discard an opponent card

Perform multi-step or split movement

Invalid actions cause the card to be discarded.

CPU Turns

CPU players:

Select a card randomly.

Execute a legal move if possible.

Attempt actions following the rules.

Discard if no legal move exists.

Winning

The first player to move all four marbles into their Safe Zone wins.

Rule Enforcement

The system enforces all rules including:

Exact movement for entering Safe Zone.

No entering occupied Safe Zone Entry cells.

No swapping marbles inside Home, Base, or Safe Zone.

No burning marbles in protected areas.

King destroys marbles passed during movement.

Fielding allowed only with Ace or King.

Split movement rules for Seven.

Discard mechanics for Ten and Queen.

Trap activation and relocation.

These rules are implemented using a structured exception hierarchy.

## Project Architecture:
```  
src/
├── engine/
│   ├── Game.java
│   ├── GameManager.java
│   └── board/
│       ├── Board.java
│       ├── BoardManager.java
│       ├── Cell.java
│       ├── CellType.java
│       └── SafeZone.java
│
├── exception/
│   ├── ActionException.java
│   ├── GameException.java
│   ├── InvalidMovementException.java
│   ├── InvalidCardException.java
│   ├── IllegalSwapException.java
│   ├── IllegalDestroyException.java
│   ├── InvalidSelectionException.java
│   └── ... (other rule exceptions)
│
├── model/
│   ├── Colour.java
│   ├── card/
│   │   ├── Card.java
│   │   ├── Deck.java
│   │   ├── standard/
│   │   │   ├── Ace.java
│   │   │   ├── King.java
│   │   │   ├── Queen.java
│   │   │   └── ... (standard cards)
│   │   └── wild/
│   │       ├── Burner.java
│   │       ├── Saver.java
│   │       └── Wild.java
│   │
│   └── player/
│       ├── Player.java
│       ├── CPU.java
│       └── Marble.java
│
└── view/
    ├── Main.java
    └── application.css
```  


## Author

### Obay Rihan
### German International University (GIU)
### Programming II – Spring 2025
