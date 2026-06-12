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

## 3. Method under test changeCurrentDirection()
- input: void
- output: none
- TC 3.1: changeCurrentDirection_3TotalPlayers_shouldChangeDirection
  - State of the system: numTotalPlayers is 3, currentPlayer is 0, currentDirection is 1
    - Expected output: currentPlayer is 0; currentDirection is -1
  - Implemented: yes
- TC 3.2: changeCurrentDirection_4TotalPlayers_shouldChangeDirection
  - State of the system: numTotalPlayers is 3, currentPlayer is 0, currentDirection is -1
    - Expected output: currentPlayer is 0; currentDirection is 1
  - Implemented: yes

---

## 4. Method under test setNumTotalPlayers(int)
- input: number of players (boundary around the minimum of 2)
- output: none (state mutation) or IllegalArgumentException
- TC 4.1: setNumTotalPlayers_belowMinimum_throwsIllegalArgumentException
  - State of the system: numTotalPlayers set to 1 (just below the minimum)
    - Expected output: throws IllegalArgumentException with message "turnTracker.numPlayers.tooSmall"
  - Implemented: yes
- TC 4.2: setNumTotalPlayers_minimum_setsValue
  - State of the system: numTotalPlayers set to 2 (the minimum)
    - Expected output: getNumTotalPlayers() returns 2
  - Implemented: yes

---

## 5. Method under test setCurrentDirection(int)
- input: playing direction (valid edges 1 and -1; invalid otherwise)
- output: none (state mutation) or IllegalArgumentException
- TC 5.1: setCurrentDirection_invalid_throwsIllegalArgumentException
  - State of the system: currentDirection set to 0 (neither 1 nor -1)
    - Expected output: throws IllegalArgumentException with message "turnTracker.currentDirection.invalid"
  - Implemented: yes
- TC 5.2: setCurrentDirection_forward_setsValue
  - State of the system: currentDirection set to 1
    - Expected output: getCurrentDirection() returns 1
  - Implemented: yes
- TC 5.3: setCurrentDirection_backward_setsValue
  - State of the system: currentDirection set to -1
    - Expected output: getCurrentDirection() returns -1
  - Implemented: yes

---

## Recall the 4 steps of BVA
### Step 1: Describe the input and output in terms of the domain.
### Step 2: Choose the data type for the input and the output from the BVA Catalog.
### Step 3: Select concrete values along the edges for the input and the output.
### Step 4: Determine the test cases using either all-combination or each-choice strategy.