# BVA Analysis for `TurnTracker`

This file holds the BVA analysis for every public method of the `TurnTracker` class. Each public method has its own `## Method N:` section; new methods append a new section as the class grows.

---

## 1. Method under test turnGoesToNextPlayer()
- input: void
- output: none
- TC 1.1: turnGoesToNextPlayer_player0_player1
    - State of the system: numTotalPlayers is 3, currentPlayer is 0, currentDirection is 1
      - Expected output: currentPlayer is 1
    - Implemented: yes
- TC 1.2: turnGoesToNextPlayer_player2_player0
  - State of the system: numTotalPlayers is 3, currentPlayer is 2, currentDirection is 1
    - Expected output: currentPlayer is 0
  - Implemented: yes
- TC 1.3: turnGoesToNextPlayer_10TotalPlayers_shouldAdvanceToNextPlayer
  - State of the system: numTotalPlayers is 10, currentPlayer is INPUT, currentDirection is INPUT
    - Expected output: currentPlayer is the next player
  - Implemented: yes
- TC 1.4: turnGoesToNextPlayer_2TotalPlayers_shouldAdvanceToNextPlayer
  - State of the system: numTotalPlayers is 2, currentPlayer is INPUT, currentDirection is INPUT
    - Expected output: currentPlayer is the next player
  - Implemented: yes
- TC 1.5: turnGoesToNextPlayer_3TotalPlayers_shouldAdvanceToNextPlayer
  - State of the system: numTotalPlayers is 3, currentPlayer is INPUT, currentDirection is INPUT
    - Expected output: currentPlayer is the next player
  - Implemented: yes

---

## 2. Method under test turnSkipsNextPlayer()
- input: void
- output: none
- TC 2.1: turnSkipsNextPlayer_2TotalPlayers_shouldSkipNextPlayer
  - State of the system: numTotalPlayers is 2, currentPlayer is INPUT, currentDirection is INPUT
    - Expected output: currentPlayer is next next player
  - Implemented: yes
- TC 2.2: turnSkipsNextPlayer_3TotalPlayers_shouldSkipNextPlayer
  - State of the system: numTotalPlayers is 3, currentPlayer is INPUT, currentDirection is INPUT
    - Expected output: currentPlayer is next next player
  - Implemented: yes
- TC 2.3: turnSkipsNextPlayer_10TotalPlayers_shouldSkipNextPlayer
  - State of the system: numTotalPlayers is 10, currentPlayer is INPUT, currentDirection is INPUT
    - Expected output: currentPlayer is next next player
  - Implemented: yes

---

## 3. Method under test turnGoesToCurrentPlayerAgain()
- input: void
- output: none
- TC 3.1: turnGoesToCurrentPlayerAgain_2TotalPlayers_shouldRepeatTurnAgain
  - State of the system: numTotalPlayers is 2, currentPlayer is INPUT, currentDirection is INPUT
    - Expected output: currentPlayer is next next player
  - Implemented: yes

---

## 4. Method under test changeCurrentDirection()
- input: void
- output: none
- TC 4.1: changeCurrentDirection_3TotalPlayers_shouldChangeDirection
  - State of the system: numTotalPlayers is 3, currentPlayer is 0, currentDirection is 1
    - Expected output: currentPlayer is 0; currentDirection is -1
  - Implemented: yes
- TC 4.2: changeCurrentDirection_4TotalPlayers_shouldChangeDirection
  - State of the system: numTotalPlayers is 3, currentPlayer is 0, currentDirection is -1
    - Expected output: currentPlayer is 0; currentDirection is 1
  - Implemented: yes
  
---

## Recall the 4 steps of BVA
### Step 1: Describe the input and output in terms of the domain.
### Step 2: Choose the data type for the input and the output from the BVA Catalog.
### Step 3: Select concrete values along the edges for the input and the output.
### Step 4: Determine the test cases using either all-combination or each-choice strategy.