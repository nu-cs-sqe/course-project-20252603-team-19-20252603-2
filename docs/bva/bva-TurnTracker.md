# BVA Analysis for `TurnTracker`

This file holds the BVA analysis for every public method of the `TurnTracker` class. Each public method has its own `## Method N:` section; new methods append a new section as the class grows.

---

## 1. Method under test turnGoesToNextPlayer()
- input: void
- output: none
- TC 7.1: turnGoesToNextPlayer_player0_player1
    - State of the system: numTotalPlayers is 3, currentPlayer is 0, currentDirection is 1
      - Expected output: currentPlayer is 1
    - Implemented: yes
- TC 7.2: turnGoesToNextPlayer_player2_player0
  - State of the system: numTotalPlayers is 3, currentPlayer is 2, currentDirection is 1
    - Expected output: currentPlayer is 0
  - Implemented: yes
- TC 7.3: turnGoesToNextPlayer_10TotalPlayers_shouldAdvanceToNextPlayer
  - State of the system: numTotalPlayers is 10, currentPlayer is INPUT, currentDirection is INPUT
    - Expected output: currentPlayer is the next player
  - Implemented: yes
- TC 7.4: turnGoesToNextPlayer_2TotalPlayers_shouldAdvanceToNextPlayer
  - State of the system: numTotalPlayers is 2, currentPlayer is INPUT, currentDirection is INPUT
    - Expected output: currentPlayer is the next player
  - Implemented: no



---

## Recall the 4 steps of BVA
### Step 1: Describe the input and output in terms of the domain.
### Step 2: Choose the data type for the input and the output from the BVA Catalog.
### Step 3: Select concrete values along the edges for the input and the output.
### Step 4: Determine the test cases using either all-combination or each-choice strategy.