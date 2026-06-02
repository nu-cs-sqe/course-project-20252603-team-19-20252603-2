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
| TC8 | `new GameEngine(2)` post-state | draw pile size is `50` (45 non-EK non-Defuse + (6-n) Defuses + (n-1) EK = 45 + 4 + 1) | yes |
| TC9 | `new GameEngine(5)` post-state | draw pile size is `38` (33 + 1 + 4) | yes |

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
| Step 3 | game start at `n=2`, `n=5` | `50`, `38` |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | `new GameEngine(2).getDrawPileSize()` | `50` | yes |
| TC2 | `new GameEngine(5).getDrawPileSize()` | `38` | yes |

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
| TC2 | draw every card, then `isDeckEmpty()` | `true` | yes |

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
| TC1 | `getPlayerHand(0)` at game start | list of size 5 (the starting hand) | yes |
| TC2 | mutate the returned list, then `getPlayerHand(0)` again | original hand unchanged (defensive copy) | yes |
| TC3 | `getPlayerHand(-1)` | throws `IllegalArgumentException` with message `"gameEngine.getPlayer.invalidId"` | yes |
| TC4 | `getPlayerHand(numPlayers)` | throws `IllegalArgumentException` with message `"gameEngine.getPlayer.invalidId"` | yes |

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
| TC1 | `drawCardForCurrentPlayer()` at game start | returns a non-null `Card`; current player's hand size becomes 6; draw pile size decreases by 1 | yes |
| TC2 | draw every card, then `drawCardForCurrentPlayer()` | throws `IllegalStateException` with message `"deck.emptyType"` | yes |

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
| TC1 | `advanceToNextPlayer()` once with 2 players | `getCurrentPlayerId()==1` | yes |
| TC2 | `advanceToNextPlayer()` twice with 2 players | `getCurrentPlayerId()==0` | yes |

---

## Method 10: ```public void playSkip()```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | current player holds a SKIP | SKIP discarded; one owed turn ended without drawing |
| Step 2 | game state | void / `IllegalStateException` if no SKIP held |
| Step 3 | holds SKIP (normal turn), holds no SKIP | turn passes to next / `gameEngine.play.notInHand` |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | current player given a SKIP, `playSkip()` (2 players) | turn passes to player 1; draw pile unchanged | yes |
| TC2 | current player has no SKIP, `playSkip()` | throws `IllegalStateException` with message `"gameEngine.play.notInHand"` | yes |

---

## Method 11: ```public void playShuffle()```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | current player holds a SHUFFLE | SHUFFLE discarded; draw pile reordered; same player continues |
| Step 2 | game state | void |
| Step 3 | holds SHUFFLE | draw pile size unchanged, current player unchanged |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | current player given a SHUFFLE, `playShuffle()` | `getCurrentPlayerId()` unchanged; draw pile size unchanged | yes |

---

## Method 12: ```public List<Card> playSeeTheFuture()```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | current player holds a SEE_THE_FUTURE | up to top 3 cards returned; same player continues |
| Step 2 | game state | `List<Card>` |
| Step 3 | holds SEE_THE_FUTURE, full draw pile | list of size 3 |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | current player given a SEE_THE_FUTURE, `playSeeTheFuture()` | returns list of size 3; `getCurrentPlayerId()` unchanged | yes |

---

## Method 13: ```public void playReverse()```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | current player holds a REVERSE | REVERSE discarded; direction flipped; one owed turn ended |
| Step 2 | game state | void |
| Step 3 | holds REVERSE (2 players, forward) | direction becomes -1; turn passes |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | current player given a REVERSE, `playReverse()` (2 players) | turn passes to player 1 | yes |

---

## Method 14: ```public void playAttack()```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | current player holds an ATTACK | ATTACK discarded; turn ends without drawing; next player owes 2 turns |
| Step 2 | game state | void |
| Step 3 | normal turn (owe 1), stacked turn (owe 2) | next owes 2 / next owes 4 |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | current player given an ATTACK, `playAttack()` (2 players, normal turn) | `getCurrentPlayerId()==1`, `getForcedTurns()==2` | yes |
| TC2 | player 1 (owing 2 after an attack) given an ATTACK, `playAttack()` | turn passes, the next player owes 4 | yes |

---

## Recall the 4 steps of BVA
### Step 1: Describe the input and output in terms of the domain.
### Step 2: Choose the data type for the input and the output from the BVA Catalog.
### Step 3: Select concrete values along the edges for the input and the output.
### Step 4: Determine the test cases using either all-combination or each-choice strategy.
