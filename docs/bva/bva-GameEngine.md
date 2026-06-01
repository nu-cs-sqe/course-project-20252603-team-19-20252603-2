# BVA Analysis for `GameEngine`

This file holds the BVA analysis for every public method of the `GameEngine` class. Each public method has its own `## Method N:` section; new methods append a new section as the class grows.

---

## Method 1: ```public GameEngine(int numPlayers)```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | Number of players to seat at the table | Fully set-up game state, or exception for invalid count |
| Step 2 | `int` | `GameEngine` instance / `IllegalArgumentException` |
| Step 3 | `1` (below min), `2`, `5`, `6` (above max) | exception / set-up game / set-up game / exception |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | `new GameEngine(2)` | constructs without exception | yes |
| TC2 | `new GameEngine(5)` | constructs without exception | yes |
| TC3 | `new GameEngine(1)` | throws `IllegalArgumentException` with message `"gameEngine.numPlayers.outOfRange"` | yes |
| TC4 | `new GameEngine(6)` | throws `IllegalArgumentException` with message `"gameEngine.numPlayers.outOfRange"` | yes |
| TC5 | `new GameEngine(0)` | throws `IllegalArgumentException` with message `"gameEngine.numPlayers.outOfRange"` | yes |
| TC6 | `new GameEngine(-1)` | throws `IllegalArgumentException` with message `"gameEngine.numPlayers.outOfRange"` | yes |
| TC7 | `new GameEngine(2)` post-state | each player has 5 cards in hand (1 Defuse + 4 others, no Exploding Kitten) | yes |
| TC8 | `new GameEngine(2)` post-state | draw pile size is `43` (38 non-EK non-Defuse + (6-n) Defuses + (n-1) EK = 38 + 4 + 1) | yes |
| TC9 | `new GameEngine(5)` post-state | draw pile size is `31` (26 + 1 + 4) | yes |

---

## Method 2: ```public int getNumPlayers()```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | none (instance query) | Number of players supplied at construction |
| Step 2 | n/a | `int` |
| Step 3 | min `2`, max `5` | same `int` |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | `new GameEngine(2).getNumPlayers()` | `2` | yes |
| TC2 | `new GameEngine(5).getNumPlayers()` | `5` | yes |

---

## Method 3: ```public Player getPlayer(int playerId)```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | Player id to look up | The `Player` for that id, or exception |
| Step 2 | `int` | `Player` / `IllegalArgumentException` |
| Step 3 | id `-1`, `0`, `numPlayers-1`, `numPlayers` | exception / player / player / exception |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | `new GameEngine(2).getPlayer(0).getPlayerId()` | `0` | yes |
| TC2 | `new GameEngine(2).getPlayer(1).getPlayerId()` | `1` | yes |
| TC3 | `new GameEngine(2).getPlayer(-1)` | throws `IllegalArgumentException` with message `"gameEngine.getPlayer.invalidId"` | yes |
| TC4 | `new GameEngine(2).getPlayer(2)` | throws `IllegalArgumentException` with message `"gameEngine.getPlayer.invalidId"` | yes |

---

## Method 4: ```public int getCurrentPlayerId()```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | none (instance query) | Id of the player whose turn it is now |
| Step 2 | n/a | `int` |
| Step 3 | game start | `0` (first player) |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | `new GameEngine(2).getCurrentPlayerId()` | `0` | yes |
| TC2 | `new GameEngine(5).getCurrentPlayerId()` | `0` | yes |

---

## Method 5: ```public int getDrawPileSize()```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | none (instance query) | Number of cards still in the draw pile |
| Step 2 | n/a | `int` |
| Step 3 | game start at `n=2`, `n=5` | `43`, `31` |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | `new GameEngine(2).getDrawPileSize()` | `43` | yes |
| TC2 | `new GameEngine(5).getDrawPileSize()` | `31` | yes |

---

## Method 6: ```public boolean isDeckEmpty()```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | none (instance query) | Whether the draw pile has any cards left |
| Step 2 | n/a | `boolean` |
| Step 3 | freshly set-up deck (non-empty), deck drained to 0 | `false` / `true` |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | `new GameEngine(2).isDeckEmpty()` at game start | `false` | yes |
| TC2 | draw every card, then `isDeckEmpty()` | `true` | no |

---

## Method 7: ```public List<Card> getPlayerHand(int playerId)```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | Player id whose hand to read | A defensive copy of that player's hand |
| Step 2 | `int` | `List<Card>` / `IllegalArgumentException` |
| Step 3 | id `-1`, `0`, `numPlayers-1`, `numPlayers` | exception / hand / hand / exception |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | `getPlayerHand(0)` at game start | list of size 5 (the starting hand) | no |
| TC2 | mutate the returned list, then `getPlayerHand(0)` again | original hand unchanged (defensive copy) | no |
| TC3 | `getPlayerHand(-1)` | throws `IllegalArgumentException` with message `"gameEngine.getPlayer.invalidId"` | no |
| TC4 | `getPlayerHand(numPlayers)` | throws `IllegalArgumentException` with message `"gameEngine.getPlayer.invalidId"` | no |

---

## Method 8: ```public Card drawCardForCurrentPlayer()```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | none; acts on current player and draw pile | Top card moved from draw pile to current player's hand, and returned |
| Step 2 | n/a | `Card` / `IllegalStateException` |
| Step 3 | non-empty draw pile, empty draw pile | card returned + hand grows + pile shrinks / `IllegalStateException` with key `deck.emptyType` |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | `drawCardForCurrentPlayer()` at game start | returns a non-null `Card`; current player's hand size becomes 6; draw pile size decreases by 1 | no |
| TC2 | draw every card, then `drawCardForCurrentPlayer()` | throws `IllegalStateException` with message `"deck.emptyType"` | no |

---

## Method 9: ```public void advanceToNextPlayer()```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | none; advances the turn | Current player id becomes the next player's id |
| Step 2 | n/a | state mutation on the turn tracker |
| Step 3 | start at player 0 with `n=2`, call once / twice | `1` / back to `0` |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | `advanceToNextPlayer()` once with 2 players | `getCurrentPlayerId()==1` | no |
| TC2 | `advanceToNextPlayer()` twice with 2 players | `getCurrentPlayerId()==0` | no |

---

## Recall the 4 steps of BVA
### Step 1: Describe the input and output in terms of the domain.
### Step 2: Choose the data type for the input and the output from the BVA Catalog.
### Step 3: Select concrete values along the edges for the input and the output.
### Step 4: Determine the test cases using either all-combination or each-choice strategy.
