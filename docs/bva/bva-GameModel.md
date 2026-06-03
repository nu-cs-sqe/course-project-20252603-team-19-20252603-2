# BVA Analysis for `GameModel`

## Method 1: ```public void startGame(List<String> playerNames)```

### Step 1-3 Results

| Step   | Input                                     | Output                                        |
|--------|-------------------------------------------|-----------------------------------------------|
| Step 1 | List of player display names              | Game session started and `GameEngine` created |
| Step 2 | `List<String>`                            | void / `IllegalArgumentException`             |
| Step 3 | 2 players, 5 players, 1 player, 6 players | started / exception                           |

### Step 4:

| Test Case # | System under test                                       | Expected output                                                                   | Implemented? |
|-------------|---------------------------------------------------------|-----------------------------------------------------------------------------------|--------------|
| TC1         | `startGame(List.of("P0", "P1"))`                        | `isGameStarted()` is `true`                                                       | yes          |
| TC2         | `startGame(List.of("P0", "P1"))`                        | `getLocalPlayerName()` is `"P0"`                                                  | yes          |
| TC3         | `startGame(List.of("P0", "P1", "P2", "P3","P4"))`       | `isGameStarted()` is `true`                                                       | yes          |
| TC4         | `startGame(List.of("P0", "P1", "P2", "P3","P4"))`       | `getLocalPlayerName()` is `"P0"`                                                  | yes          |
| TC5         | `startGame(List.of("P0"))`                              | throws `IllegalArgumentException` with message `gameEngine.numPlayers.outOfRange` | yes          |
| TC6         | `startGame(List.of("P0", "P1", "P2", "P3","P4", "P5"))` | throws `IllegalArgumentException` with message `gameEngine.numPlayers.outOfRange` | yes          |

---

## Recall the 4 steps of BVA

### Step 1: Describe the input and output in terms of the domain.

### Step 2: Choose the data type for the input and the output from the BVA Catalog.

### Step 3: Select concrete values along the edges for the input and the output.

### Step 4: Determine the test cases using either all-combination or each-choice strategy.
