# BVA Analysis for `GameEngine`

This file holds the BVA analysis for every public method of the `GameEngine` class. Each public method has its own
`## Method N:` section; new methods append a new section as the class grows.

---

## Method 1: ```public GameEngine(int numPlayers)```

### Step 1-3 Results

| Step   | Input                                      | Output                                                  |
|--------|--------------------------------------------|---------------------------------------------------------|
| Step 1 | Number of players to seat at the table     | Fully set-up game state, or exception for invalid count |
| Step 2 | `int`                                      | `GameEngine` instance / `IllegalArgumentException`      |
| Step 3 | `1` (below min), `2`, `5`, `6` (above max) | exception / set-up game / set-up game / exception       |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test              | Expected output                                                                       | Implemented? |
|-------------|--------------------------------|---------------------------------------------------------------------------------------|--------------|
| TC1         | `new GameEngine(2)`            | constructs without exception                                                          | yes          |
| TC2         | `new GameEngine(5)`            | constructs without exception                                                          | yes          |
| TC3         | `new GameEngine(1)`            | throws `IllegalArgumentException` with message `"gameEngine.numPlayers.outOfRange"`   | yes          |
| TC4         | `new GameEngine(6)`            | throws `IllegalArgumentException` with message `"gameEngine.numPlayers.outOfRange"`   | yes          |
| TC5         | `new GameEngine(0)`            | throws `IllegalArgumentException` with message `"gameEngine.numPlayers.outOfRange"`   | yes          |
| TC6         | `new GameEngine(-1)`           | throws `IllegalArgumentException` with message `"gameEngine.numPlayers.outOfRange"`   | yes          |
| TC7         | `new GameEngine(2)` post-state | each player has 5 cards in hand (1 Defuse + 4 others, no Exploding Kitten)            | yes          |
| TC8         | `new GameEngine(2)` post-state | draw pile size is `61` (45 non-EK non-Defuse + (6-n) Defuses + (n-1) EK = 45 + 4 + 1) | yes          |
| TC9         | `new GameEngine(5)` post-state | draw pile size is `49` (33 + 1 + 4)                                                   | yes          |

---

## Method 2: ```public int getNumPlayers()```

### Step 1-3 Results

| Step   | Input                 | Output                                     |
|--------|-----------------------|--------------------------------------------|
| Step 1 | none (instance query) | Number of players supplied at construction |
| Step 2 | n/a                   | `int`                                      |
| Step 3 | min `2`, max `5`      | same `int`                                 |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                   | Expected output | Implemented? |
|-------------|-------------------------------------|-----------------|--------------|
| TC1         | `new GameEngine(2).getNumPlayers()` | `2`             | yes          |
| TC2         | `new GameEngine(5).getNumPlayers()` | `5`             | yes          |

---

## Method 3: ```public Player getPlayer(int playerId)```

### Step 1-3 Results

| Step   | Input                                      | Output                                  |
|--------|--------------------------------------------|-----------------------------------------|
| Step 1 | Player id to look up                       | The `Player` for that id, or exception  |
| Step 2 | `int`                                      | `Player` / `IllegalArgumentException`   |
| Step 3 | id `-1`, `0`, `numPlayers-1`, `numPlayers` | exception / player / player / exception |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                              | Expected output                                                                   | Implemented? |
|-------------|------------------------------------------------|-----------------------------------------------------------------------------------|--------------|
| TC1         | `new GameEngine(2).getPlayer(0).getPlayerId()` | `0`                                                                               | yes          |
| TC2         | `new GameEngine(2).getPlayer(1).getPlayerId()` | `1`                                                                               | yes          |
| TC3         | `new GameEngine(2).getPlayer(-1)`              | throws `IllegalArgumentException` with message `"gameEngine.getPlayer.invalidId"` | yes          |
| TC4         | `new GameEngine(2).getPlayer(2)`               | throws `IllegalArgumentException` with message `"gameEngine.getPlayer.invalidId"` | yes          |

---

## Method 4: ```public int getCurrentPlayerId()```

### Step 1-3 Results

| Step   | Input                 | Output                                |
|--------|-----------------------|---------------------------------------|
| Step 1 | none (instance query) | Id of the player whose turn it is now |
| Step 2 | n/a                   | `int`                                 |
| Step 3 | game start            | `0` (first player)                    |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                        | Expected output | Implemented? |
|-------------|------------------------------------------|-----------------|--------------|
| TC1         | `new GameEngine(2).getCurrentPlayerId()` | `0`             | yes          |
| TC2         | `new GameEngine(5).getCurrentPlayerId()` | `0`             | yes          |

---

## Method 5: ```public int getDrawPileSize()```

### Step 1-3 Results

| Step   | Input                      | Output                                 |
|--------|----------------------------|----------------------------------------|
| Step 1 | none (instance query)      | Number of cards still in the draw pile |
| Step 2 | n/a                        | `int`                                  |
| Step 3 | game start at `n=2`, `n=5` | `61`, `49`                             |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                     | Expected output | Implemented? |
|-------------|---------------------------------------|-----------------|--------------|
| TC1         | `new GameEngine(2).getDrawPileSize()` | `61`            | yes          |
| TC2         | `new GameEngine(5).getDrawPileSize()` | `49`            | yes          |

---

## Method 6: ```public boolean isDeckEmpty()```

### Step 1-3 Results

| Step   | Input                                              | Output                                   |
|--------|----------------------------------------------------|------------------------------------------|
| Step 1 | none (instance query)                              | Whether the draw pile has any cards left |
| Step 2 | n/a                                                | `boolean`                                |
| Step 3 | freshly set-up deck (non-empty), deck drained to 0 | `false` / `true`                         |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                               | Expected output | Implemented? |
|-------------|-------------------------------------------------|-----------------|--------------|
| TC1         | `new GameEngine(2).isDeckEmpty()` at game start | `false`         | yes          |
| TC2         | draw every card, then `isDeckEmpty()`           | `true`          | yes          |

---

## Method 7: ```public List<Card> getPlayerHand(int playerId)```

### Step 1-3 Results

| Step   | Input                                      | Output                                    |
|--------|--------------------------------------------|-------------------------------------------|
| Step 1 | Player id whose hand to read               | A defensive copy of that player's hand    |
| Step 2 | `int`                                      | `List<Card>` / `IllegalArgumentException` |
| Step 3 | id `-1`, `0`, `numPlayers-1`, `numPlayers` | exception / hand / hand / exception       |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                                       | Expected output                                                                   | Implemented? |
|-------------|---------------------------------------------------------|-----------------------------------------------------------------------------------|--------------|
| TC1         | `getPlayerHand(0)` at game start                        | list of size 5 (the starting hand)                                                | yes          |
| TC2         | mutate the returned list, then `getPlayerHand(0)` again | original hand unchanged (defensive copy)                                          | yes          |
| TC3         | `getPlayerHand(-1)`                                     | throws `IllegalArgumentException` with message `"gameEngine.getPlayer.invalidId"` | yes          |
| TC4         | `getPlayerHand(numPlayers)`                             | throws `IllegalArgumentException` with message `"gameEngine.getPlayer.invalidId"` | yes          |

---

## Method 8: ```public Card drawCardForCurrentPlayer()```

### Step 1-3 Results

| Step   | Input                                      | Output                                                                                        |
|--------|--------------------------------------------|-----------------------------------------------------------------------------------------------|
| Step 1 | none; acts on current player and draw pile | Top card moved from draw pile to current player's hand, and returned                          |
| Step 2 | n/a                                        | `Card` / `IllegalStateException`                                                              |
| Step 3 | non-empty draw pile, empty draw pile       | card returned + hand grows + pile shrinks / `IllegalStateException` with key `deck.emptyType` |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                                  | Expected output                                                                                | Implemented? |
|-------------|----------------------------------------------------|------------------------------------------------------------------------------------------------|--------------|
| TC1         | `drawCardForCurrentPlayer()` at game start         | returns a non-null `Card`; current player's hand size becomes 6; draw pile size decreases by 1 | yes          |
| TC2         | draw every card, then `drawCardForCurrentPlayer()` | throws `IllegalStateException` with message `"deck.emptyType"`                                 | yes          |

---

## Method 9: ```public void advanceToNextPlayer()```

### Step 1-3 Results

| Step   | Input                                           | Output                                         |
|--------|-------------------------------------------------|------------------------------------------------|
| Step 1 | none; advances the turn                         | Current player id becomes the next player's id |
| Step 2 | n/a                                             | state mutation on the turn tracker             |
| Step 3 | start at player 0 with `n=2`, call once / twice | `1` / back to `0`                              |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                            | Expected output           | Implemented? |
|-------------|----------------------------------------------|---------------------------|--------------|
| TC1         | `advanceToNextPlayer()` once with 2 players  | `getCurrentPlayerId()==1` | yes          |
| TC2         | `advanceToNextPlayer()` twice with 2 players | `getCurrentPlayerId()==0` | yes          |

---

## Method 10: ```public void playSkip()```

### Step 1-3 Results

| Step   | Input                                   | Output                                              |
|--------|-----------------------------------------|-----------------------------------------------------|
| Step 1 | current player holds a SKIP             | SKIP discarded; one owed turn ended without drawing |
| Step 2 | game state                              | void / `IllegalStateException` if no SKIP held      |
| Step 3 | holds SKIP (normal turn), holds no SKIP | turn passes to next / `gameEngine.play.notInHand`   |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                                     | Expected output                                                           | Implemented? |
|-------------|-------------------------------------------------------|---------------------------------------------------------------------------|--------------|
| TC1         | current player given a SKIP, `playSkip()` (2 players) | turn passes to player 1; draw pile unchanged                              | yes          |
| TC2         | current player has no SKIP, `playSkip()`              | throws `IllegalStateException` with message `"gameEngine.play.notInHand"` | yes          |

---

## Method 11: ```public void playShuffle()```

### Step 1-3 Results

| Step   | Input                          | Output                                                        |
|--------|--------------------------------|---------------------------------------------------------------|
| Step 1 | current player holds a SHUFFLE | SHUFFLE discarded; draw pile reordered; same player continues |
| Step 2 | game state                     | void                                                          |
| Step 3 | holds SHUFFLE                  | draw pile size unchanged, current player unchanged            |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                               | Expected output                                            | Implemented? |
|-------------|-------------------------------------------------|------------------------------------------------------------|--------------|
| TC1         | current player given a SHUFFLE, `playShuffle()` | `getCurrentPlayerId()` unchanged; draw pile size unchanged | yes          |

---

## Method 12: ```public List<Card> playSeeTheFuture()```

### Step 1-3 Results

| Step   | Input                                 | Output                                            |
|--------|---------------------------------------|---------------------------------------------------|
| Step 1 | current player holds a SEE_THE_FUTURE | up to top 3 cards returned; same player continues |
| Step 2 | game state                            | `List<Card>`                                      |
| Step 3 | holds SEE_THE_FUTURE, full draw pile  | list of size 3                                    |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                                           | Expected output                                          | Implemented? |
|-------------|-------------------------------------------------------------|----------------------------------------------------------|--------------|
| TC1         | current player given a SEE_THE_FUTURE, `playSeeTheFuture()` | returns list of size 3; `getCurrentPlayerId()` unchanged | yes          |

---

## Method 13: ```public void playReverse()```

### Step 1-3 Results

| Step   | Input                              | Output                                                    |
|--------|------------------------------------|-----------------------------------------------------------|
| Step 1 | current player holds a REVERSE     | REVERSE discarded; direction flipped; one owed turn ended |
| Step 2 | game state                         | void                                                      |
| Step 3 | holds REVERSE (2 players, forward) | direction becomes -1; turn passes                         |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                                           | Expected output         | Implemented? |
|-------------|-------------------------------------------------------------|-------------------------|--------------|
| TC1         | current player given a REVERSE, `playReverse()` (2 players) | turn passes to player 1 | yes          |

---

## Method 14: ```public void playAttack()```

### Step 1-3 Results

| Step   | Input                                     | Output                                                                |
|--------|-------------------------------------------|-----------------------------------------------------------------------|
| Step 1 | current player holds an ATTACK            | ATTACK discarded; turn ends without drawing; next player owes 2 turns |
| Step 2 | game state                                | void                                                                  |
| Step 3 | normal turn (owe 1), stacked turn (owe 2) | next owes 2 / next owes 4                                             |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                                                       | Expected output                                  | Implemented? |
|-------------|-------------------------------------------------------------------------|--------------------------------------------------|--------------|
| TC1         | current player given an ATTACK, `playAttack()` (2 players, normal turn) | `getCurrentPlayerId()==1`, `getForcedTurns()==2` | yes          |
| TC2         | player 1 (owing 2 after an attack) given an ATTACK, `playAttack()`      | turn passes, the next player owes 4              | yes          |

---

## Method 15: ```public void playTargetedAttack(int targetId)```

### Step 1-3 Results

| Step   | Input                                                   | Output                                           |
|--------|---------------------------------------------------------|--------------------------------------------------|
| Step 1 | current holds TARGETED_ATTACK; a chosen living opponent | turn jumps to the target who owes 2 turns        |
| Step 2 | `int` target id                                         | void / `IllegalArgumentException` for bad target |
| Step 3 | distinct living target, self/dead target                | turn to target, owes 2 / `rule.target.invalid`   |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                                                   | Expected output                                                        | Implemented? |
|-------------|---------------------------------------------------------------------|------------------------------------------------------------------------|--------------|
| TC1         | 3 players, current 0 given TARGETED_ATTACK, `playTargetedAttack(2)` | `getCurrentPlayerId()==2`, `getForcedTurns()==2`                       | yes          |
| TC2         | `playTargetedAttack(0)` by player 0 (self)                          | throws `IllegalArgumentException` with message `"rule.target.invalid"` | yes          |

---

## Method 16: ```public void playFavor(int targetId, int cardIndex)```

### Step 1-3 Results

| Step   | Input                                                         | Output                                                        |
|--------|---------------------------------------------------------------|---------------------------------------------------------------|
| Step 1 | current holds FAVOR; target and the index of the card to take | that card moves from target to current; same player continues |
| Step 2 | two `int`                                                     | void / exception for bad target or index                      |
| Step 3 | valid target + index, self target                             | card transferred / `rule.target.invalid`                      |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                                             | Expected output                                                                            | Implemented? |
|-------------|---------------------------------------------------------------|--------------------------------------------------------------------------------------------|--------------|
| TC1         | 2 players, current 0 given FAVOR, `playFavor(1, 0)`           | target hand shrinks by 1; current keeps the turn                                           | yes          |
| TC2         | `playFavor(0, 0)` by player 0 (self)                          | throws `IllegalArgumentException` with message `"rule.target.invalid"`                     | yes          |
| TC3         | `playFavor(1, size)` with a card index past the target's hand | throws `IndexOutOfBoundsException` with message `"player.removeCardFromHand.invalidIndex"` | yes          |

---

## Method 17: ```public void playCatPair(int targetId, CardType cardType)```

### Step 1-3 Results

| Step   | Input                                                                                                  | Output                                                                                                                                |
|--------|--------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------|
| Step 1 | current holds a valid 2 card combo of `CardType`; a target                                             | one random card moves from target to current; `lastPlayedCard` = `selectedCard`; same player continues                                |
| Step 2 | `int` target id + a list of the cards selected                                                         | void / exception for bad target or too few of the type or invalid pair                                                                |
| Step 3 | valid 2 card combo + valid target, 2 of a non-cat type, fewer than 2 of the type, invalid 2 card combo | steal happens / steal happens / `rule.catPair.needTwo` / `rule.catPair.feralCannotBeBaseType` or `rule.catPair.cloneCannotBeBaseType` |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                                                                                       | Expected output                                                                    | Implemented? |
|-------------|---------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------|--------------|
| TC1         | 2 players, current 0 given 2 CAT_CARDS, `playCatPair(1, List.of(CAT_CARDS, CAT_CARDS))`                 | target hand shrinks by 1, current keeps the turn, `getLastPlayedCard()==CAT_CARDS` | yes          |
| TC2         | 2 players, current 0 given 1 CAT_CARDS AND 1 CLONE, `playCatPair(1, List.of(CAT_CARDS, CLONE))`         | target hand shrinks by 1, current keeps the turn, `getLastPlayedCard()==CAT_CARDS` | yes          |
| TC3         | 2 players, current 0 given 1 CAT_CARDS AND 1 FERAL_CAT, `playCatPair(1, List.of(CAT_CARDS, FERAL_CAT))` | target hand shrinks by 1, current keeps the turn, `getLastPlayedCard()==CAT_CARDS` | yes          |
| TC4         | current 0 given 2 ATTACK, `playCatPair(1, List.of(ATTACK, ATTACK))`                                     | target hand shrinks by 1, `getLastPlayedCard()==ATTACK`                            | yes          |
| TC5         | current holds fewer than 2 of the type, `playCatPair(1, List.of(CAT_CARDS))`                            | throws `IllegalStateException` with message `"rule.catPair.needTwo"`               | yes          |
| TC6         | 2 players, current 0 given 2 FERAL_CAT, `playCatPair(1, List.of(FERAL_CAT, FERAL_CAT))`                 | throws `IllegalStateException with message `"rule.catPair.feralCannotBeBaseType"`  | yes          |                                                                            
| TC7         | 2 players, current 0 given 2 CLONE, `playCatPair(1, List.of(CLONE, CLONE))`                             | throws `IllegalStateException with message `"rule.catPair.cloneCannotBeBaseType"`  | yes          |

---

## Method 18: ```public void defuseDrawnKitten(int reinsertIndex)```

### Step 1-3 Results

| Step   | Input                                                               | Output                                                                 |
|--------|---------------------------------------------------------------------|------------------------------------------------------------------------|
| Step 1 | current holds a drawn Exploding Kitten and a Defuse; reinsert index | kitten reinserted, Defuse discarded, turn ends; player survives        |
| Step 2 | `int` index                                                         | void / `IllegalStateException`                                         |
| Step 3 | has kitten + defuse, no kitten, no defuse                           | survives / `gameEngine.defuse.noKitten` / `gameEngine.defuse.noDefuse` |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                                            | Expected output                                                               | Implemented? |
|-------------|--------------------------------------------------------------|-------------------------------------------------------------------------------|--------------|
| TC1         | current given an Exploding Kitten, `defuseDrawnKitten(0)`    | current still alive, kitten gone from hand, draw pile grows by 1, turn passes | yes          |
| TC2         | current has no Exploding Kitten, `defuseDrawnKitten(0)`      | throws `IllegalStateException` with message `"gameEngine.defuse.noKitten"`    | yes          |
| TC3         | current given a kitten but no Defuse, `defuseDrawnKitten(0)` | throws `IllegalStateException` with message `"gameEngine.defuse.noDefuse"`    | yes          |

---

## Method 19: ```public void explodeCurrentPlayer()```

### Step 1-3 Results

| Step   | Input                                  | Output                                              |
|--------|----------------------------------------|-----------------------------------------------------|
| Step 1 | current holds a drawn Exploding Kitten | current dies; turn passes to the next living player |
| Step 2 | none                                   | void / `IllegalStateException`                      |
| Step 3 | has kitten, no kitten                  | dies + turn passes / `gameEngine.defuse.noKitten`   |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                                                      | Expected output                                                            | Implemented? |
|-------------|------------------------------------------------------------------------|----------------------------------------------------------------------------|--------------|
| TC1         | 2 players, current given an Exploding Kitten, `explodeCurrentPlayer()` | `getPlayer(0).isAlive()==false`, `getCurrentPlayerId()==1`                 | yes          |
| TC2         | current has no Exploding Kitten, `explodeCurrentPlayer()`              | throws `IllegalStateException` with message `"gameEngine.defuse.noKitten"` | yes          |
| TC3         | current with extra cards explodes                                      | eliminated player's hand is emptied (cards discarded)                      | yes          |

---

## Method 20: ```public boolean isGameOver()```

### Step 1-3 Results

| Step   | Input                                                   | Output                     |
|--------|---------------------------------------------------------|----------------------------|
| Step 1 | current alive count and draw pile                       | whether the game has ended |
| Step 2 | game state                                              | `boolean`                  |
| Step 3 | 2 alive + non-empty pile, 1 alive, 2 alive + empty pile | `false` / `true` / `true`  |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                      | Expected output | Implemented? |
|-------------|----------------------------------------|-----------------|--------------|
| TC1         | `new GameEngine(2)` at game start      | `false`         | yes          |
| TC2         | one player marked dead                 | `true`          | yes          |
| TC3         | draw pile drained to empty, both alive | `true`          | yes          |

---

## Method 21: ```public int getWinnerId()```

### Step 1-3 Results

| Step   | Input                                                                                  | Output                                              |
|--------|----------------------------------------------------------------------------------------|-----------------------------------------------------|
| Step 1 | the finished game state                                                                | id of the winner                                    |
| Step 2 | game state                                                                             | `int` / `IllegalStateException`                     |
| Step 3 | not over, last player standing, exhausted pile with a clear leader, exhausted pile tie | exception / survivor id / most-cards id / lowest id |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                                      | Expected output                                                    | Implemented? |
|-------------|--------------------------------------------------------|--------------------------------------------------------------------|--------------|
| TC1         | `getWinnerId()` before the game is over                | throws `IllegalStateException` with message `"gameEngine.notOver"` | yes          |
| TC2         | player 0 marked dead (2 players)                       | `1` (last player standing)                                         | yes          |
| TC3         | draw pile drained by player 0 (most cards), both alive | `0`                                                                | yes          |
| TC4         | exhausted pile with both hands equal                   | `0` (tie broken by lowest id)                                      | yes          |

---

## Method 22: ```public void playNope(int noperId)```

### Step 1-3 Results

| Step   | Input                                                                                                           | Output                                                                                     |
|--------|-----------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------|
| Step 1 | a played card to cancel; the noper who holds a NOPE                                                             | NOPE discarded; the last action is undone based on its card type; `lastPlayedCard` cleared |
| Step 2 | `int` noper id                                                                                                  | void / `IllegalStateException`                                                             |
| Step 3 | last card = SKIP / REVERSE / ATTACK / TARGETED_ATTACK / SEE_THE_FUTURE; nothing played yet; noper holds no NOPE | undo per card / `rule.nope.nothingToCancel` / `gameEngine.play.notInHand`                  |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                                               | Expected output                                                           | Implemented? |
|-------------|-----------------------------------------------------------------|---------------------------------------------------------------------------|--------------|
| TC1         | a card was just played, noper holds a NOPE, `playNope(noperId)` | `getLastPlayedCard()` becomes `null`                                      | yes          |
| TC2         | nothing played yet, `playNope(0)`                               | throws `IllegalStateException` with message `"rule.nope.nothingToCancel"` | yes          |
| TC3         | a card was just played, noper holds no NOPE                     | throws `IllegalStateException` with message `"gameEngine.play.notInHand"` | yes          |
| TC4         | SKIP was played (turn passed), then noped                       | turn returns to the player who played SKIP; `getForcedTurns()==1`         | yes          |
| TC5         | REVERSE was played, then noped                                  | direction restored; turn returns to the player who played REVERSE         | yes          |
| TC6         | ATTACK was played (next owes 2), then noped                     | `getForcedTurns()` reduced (2→1); turn returns to the attacker            | yes          |
| TC7         | TARGETED_ATTACK was played, then noped                          | turn returns to the attacker; `getForcedTurns()` reduced                  | yes          |
| TC8         | SEE_THE_FUTURE was played, then noped                           | draw pile is shuffled (peek invalidated); same player keeps the turn      | yes          |

---

## Method 23: ```public void endTurnByDrawing()```

### Step 1-3 Results

| Step   | Input                                                     | Output                                                                              |
|--------|-----------------------------------------------------------|-------------------------------------------------------------------------------------|
| Step 1 | the current player has just taken a safe draw             | one owed turn consumed; turn passes to the next living player only when none remain |
| Step 2 | game state (`forcedTurns`, alive flags)                   | state mutation on the turn tracker                                                  |
| Step 3 | normal turn (owe 1), stacked turn (owe 2), next seat dead | advance to next living / same player keeps the turn / dead seat skipped             |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                                      | Expected output                                    | Implemented? |
|-------------|--------------------------------------------------------|----------------------------------------------------|--------------|
| TC1         | 2 players, normal turn, `endTurnByDrawing()`           | `getCurrentPlayerId()==1`, `getForcedTurns()==1`   | yes          |
| TC2         | player owing 2 (after an Attack), `endTurnByDrawing()` | same player keeps the turn; `getForcedTurns()==1`  | yes          |
| TC3         | 3 players, next seat eliminated, `endTurnByDrawing()`  | turn skips the dead seat to the next living player | yes          |

---

## Method 24: ```public List<Card> getDiscardPile()```

### Step 1-3 Results

| Step   | Input                                 | Output                                                                  |
|--------|---------------------------------------|-------------------------------------------------------------------------|
| Step 1 | none (instance query)                 | A defensive copy of the discard pile                                    |
| Step 2 | n/a                                   | `List<Card>`                                                            |
| Step 3 | nothing discarded, one card discarded | empty list / list of size 1; mutating the copy does not affect the game |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                                       | Expected output                         | Implemented? |
|-------------|---------------------------------------------------------|-----------------------------------------|--------------|
| TC1         | `getDiscardPile()` at game start                        | empty list                              | yes          |
| TC2         | after `playSkip()`, `getDiscardPile()`                  | list of size 1 containing the SKIP      | yes          |
| TC3         | mutate the returned list, then `getDiscardPile()` again | discard pile unchanged (defensive copy) | yes          |

---

## Method 25: ```public void playCatTriple(int targetId, CardType selectedCard, CardType desiredCard)```

### Step 1-3 Results

| Step   | Input                                                                                                                           | Output                                                                                                                                      |
|--------|---------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------|
| Step 1 | current holds a valid 3 card combo; a target; a `desiredCard` to demand                                                         | the desired card moves from target to current if the target has it (else nothing); `lastPlayedCard` = `selectedCard`; same player continues |
| Step 2 | `int` target id + one `CardType` + a list of the cards selected                                                                 | void / exception for bad target or fewer than three `selected cards`                                                                        |
| Step 3 | valid 3 card combo + target has desired, valid 3 card combo + target lacks desired, fewer than 3 selected, invalid 3 card combo | steal happens / no steal / `rule.catTriple.needThree`                                                                                       |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                                                                                                                                                | Expected output                                                                                    | Implemented? |
|-------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|--------------|
| TC1         | current given 3 ATTACK, `playCatTriple(1, List.of(Attack, Attack, Attack), DEFUSE)` (target has a Defuse)                                                        | target loses the Defuse, current gains it, current keeps the turn, `getLastPlayedCard()==ATTACK`   | yes          |
| TC2         | current given 3 ATTACK, `playCatTriple(1, List.of(Attack, Attack, Attack), EXPLODING_KITTEN)` (target lacks it)                                                  | no card stolen; `getLastPlayedCard()==ATTACK`                                                      | yes          |
| TC3         | current holds fewer than 3 ATTACK, `playCatTriple(1, List.of(Attack, Attack), DEFUSE)`                                                                           | throws `IllegalStateException` with message `"rule.catTriple.needThree"`                           | yes          |
| TC4         | current given `List.of("Attack", "Defuse", "Favor")`, `playCatTriple(1, List.of("Attack", "Defuse", "Favor"), DEFUSE)` (target has a Defuse)                     | throws `IllegalStateException` with message `"rule.catTriple.needThree"`                           | yes          |
| TC5         | current given `List.of("CAT_CARD", "CAT_CARD", "FERAL_CAT")`, `playCatTriple(1, List.of("CAT_CARD", "CAT_CARD", "FERAL_CAT"), DEFUSE)` (target has a DEFUSE)     | target loses the Defuse, current gains it, current keeps the turn, `getLastPlayedCard()==CAT_CARD` | yes          |
| TC6         | current given `List.of("CAT_CARD", "FERAL_CAT", "FERAL_CAT")`, `playCatTriple(1, List.of("CAT_CARD", "FERAL_CAT", "FERAL_CAT"), DEFUSE)` (target has a DEFUSE)   | target loses the Defuse, current gains it, current keeps the turn, `getLastPlayedCard()==CAT_CARD` | yes          |
| TC7         | current given `List.of("FERAL_CAT", "FERAL_CAT", "FERAL_CAT")`, `playCatTriple(1, List.of("FERAL_CAT", "FERAL_CAT", "FERAL_CAT"), DEFUSE)` (target has a DEFUSE) | throws `IllegalStateException` with message `"rule.catTriple.feralCannotBeBaseType"`               | yes          |
| TC8         | current given `List.of("CAT_CARD", "CAT_CARD", "CLONE")`, `playCatTriple(1, List.of("CAT_CARD", "CAT_CARD", "CLONE"), DEFUSE)` (target has a DEFUSE)             | target loses the Defuse, current gains it, current keeps the turn, `getLastPlayedCard()==CAT_CARD` | yes          |
| TC9         | current given `List.of("CAT_CARD", "CLONE", "CLONE")`, `playCatTriple(1, List.of("CAT_CARD", "CLONE", "CLONE"), DEFUSE)` (target has a DEFUSE)                   | throws `IllegalStateException` with message `"rule.catTriple.needThree"`                           | yes          |
| TC10        | current given `List.of("CLONE", "CLONE", "CLONE")`, `playCatTriple(1, List.of("CLONE", "CLONE", "CLONE"), DEFUSE)` (target has a DEFUSE)                         | throws `IllegalStateException` with message `"rule.catTriple.cloneCannotBeBaseType"`               | yes          |

---

## Method 25: ```public void playClone()```

### Step 1-3 Results

| Step   | Input                                                                                                                                                   | Output                                                                                                            |
|--------|---------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------|
| Step 1 | a played card to clone; player id of last played player                                                                                                 | CLONE discarded; the card last played is cloned; `lastPlayedCard` cleared                                         |
| Step 2 | n/a                                                                                                                                                     | void / `IllegalStateException`                                                                                    |
| Step 3 | last card = SKIP / REVERSE / ATTACK / TARGETED_ATTACK / SEE_THE_FUTURE  / SHUFFLE / FAVOR; nothing played yet; last card = CLONE; cloner holds no clone | copy the card action / `rule.clone.nothingToCancel` / `rule.clone.cannotCloneClone `/ `gameEngine.play.notInHand` |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                                           | Expected output                                                                                     | Implemented? |
|-------------|-------------------------------------------------------------|-----------------------------------------------------------------------------------------------------|--------------|
| TC1         | a card was just played, cloner holds a clone, `playClone()` | `getLastPlayedCard()` becomes `null`                                                                | yes          |
| TC2         | nothing played yet, `playClone()`                           | throws `IllegalStateException` with message `"rule.clone.nothingToClone"`                           | yes          |
| TC3         | a card was just played, cloner holds no CLONE               | throws `IllegalStateException` with message `"gameEngine.play.notInHand"`                           | yes          |
| TC4         | SKIP was played (turn passed), then cloned                  | turn goes to the next living player                                                                 | yes          |
| TC5         | REVERSE was played, then cloned                             | direction restored; turn returns to the player who played REVERSE given 2 players                   | yes          |
| TC6         | ATTACK was played (next owes 2), then cloned                | turn goes to the next living player; `getForcedTurns()` increased (2→4);                            | yes          |
| TC7         | TARGETED_ATTACK was played, then cloned                     | turn goes to the targeted player; `getForcedTurns()` reduced (2->4)                                 | yes          |
| TC8         | SEE_THE_FUTURE was played, then cloned                      | player peeks the top 3 cards; same player keeps the turn                                            | yes          |
| TC11        | SHUFFLE was played, then cloned                             | shuffles deck; same player keeps the turn                                                           | yes          |
| TC12        | FAVOR was played, then cloned                               | favor card is played; same player keeps the turn                                                    | yes          |
| TC13        | CLONE was played, then cloned again                         | throws `IllegalStateException` with message `"rule.clone.cannotCloneClone"`                         | yes          |
| TC14        | SUPER_SKIP was played, then cloned                          | turn goes to the next living player and ignores the forced turns; `getForcedTurns()` should equal 1 | yes          |
| TC15        | PERSONAL_ATTACK_3X was played, then clone                   | forced turns increased by 3; same player keeps the turn                                             | yes          | 

---

## Method 16: ```public void playSuperSkip()```

### Step 1-3 Results

| Step   | Input                                                                                     | Output                                                    |
|--------|-------------------------------------------------------------------------------------------|-----------------------------------------------------------|
| Step 1 | current player holds a SUPER SKIP                                                         | SUPER SKIP discarded; one owed turn ended without drawing |
| Step 2 | game state                                                                                | void / `IllegalStateException` if no SUPER SKIP held      |
| Step 3 | holds SUPER SKIP (normal turn), holds no SUPER SKIP, holds SUPER SKIP and forced turn > 1 | turn passes to next / `gameEngine.play.notInHand`         |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                                                                  | Expected output                                                           | Implemented? |
|-------------|------------------------------------------------------------------------------------|---------------------------------------------------------------------------|--------------|
| TC1         | current player given a SUPER SKIP, `playSuperSkip()` (2 players)                   | turn passes to player 1; draw pile unchanged                              | yes          |
| TC2         | current player has no SUPER SKIP, `playSuperSkip()` (2 players)                    | throws `IllegalStateException` with message `"gameEngine.play.notInHand"` | yes          |
| TC3         | current player given a SUPER SKIP, forced turns = 4, `playSuperSkip()` (2 players) | forced turns reset to 1; turn passes to player 1; draw pile unchanged     | yes          |                             

---

## Method 17 ```public void playBury(int index)```

### Step 1-3 Results

| Step   | Input                                                                                                                                                      | Output                                                                                                                    |
|--------|------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------|
| Step 1 | current player holds a BURY                                                                                                                                | The BURY card is discarded; the top card of the deck is pulled into a temporary mid-air holding cache.                    |
| Step 2 | game state and deck size                                                                                                                                   | void / `IllegalStateException` if no BURY card held or reinsert index bigger or smaller than deck size                    |
| Step 3 | holds BURY and reinserts at 0, middle (deck size / 2), deck size, holds no BURY, holds BURY and reinserts at -1, holds BURY and reinserts at deck size + 1 | inserts card back into deck / `gameEngine.play.notInHand` / `deck.insertAt.negativeIndex` / `deck.insertAt.indexTooLarge` |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                                                                                                | Expected output                                                           | Implemented? |
|-------------|------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------|--------------|
| TC1         | current player given a BURY and reinserts at 1, `playBury(1)` (2 players)                                        | turn passes to player 1; top card moved to index 1                        | yes          |
| TC2         | current player given a BURY and reinserts at deck size = 30, `playBury(30)` (2 players)                          | turn passes to player 1; top card moved to index 30                       | yes          |
| TC3         | current player given a BURY and reinserts at deck size = 60 (account the drawn card), `playBury(60)` (2 players) | turn passes to player 1; top card moved to index 60                       | yes          |
| TC4         | current player has no BURY, `playSuperSkip()` (2 players)                                                        | throws `IllegalStateException` with message `"gameEngine.play.notInHand"` | yes          |
| TC5         | current player given a BURY and reinserts at deck size = 62, `playBury(62)` (2 players)                          | throws `IllegalStateException` with message `"rule.bury.invalidIndex"`    | yes          |
| TC6         | current player given a BURY and reinserts at deck size = -1, `playBury(-1)` (2 players)                          | throws `IllegalStateException` with message `"rule.bury.invalidIndex"`    | yes          |

---

## Method 18: ```public void playPersonalAttack3X()```

### Step 1-3 Results

| Step   | Input                                                                                                                       | Output                                                                   |
|--------|-----------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------|
| Step 1 | The number of active turns stacked on the current player (currentForcedTurns) and current player holds a personal attack 3x | PERSONAL_ATTACK_3X discarded; forced turns plus 3; same player continues |
| Step 2 | game state                                                                                                                  | void / `IllegalStateException` if no PERSONAL_ATTACK_3X held             |
| Step 3 | holds PERSONAL_ATTACK_3X (normal turn), holds no PERSONAL_ATTACK_3X                                                         | forced turns plus 3 / `gameEngine.play.notInHand`                        |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                                                                                | Expected output                                                           | Implemented? |
|-------------|--------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------|--------------|
| TC1         | current player given a PERSONAL ATTACK 3X, forced turn = 1, `playPersonalAttack3X()` (2 players) | force turn becomes 4; same player playing                                 | yes          |
| TC2         | current player has no PERSONAL ATTACK 3X, `playPersonalAttack3X()` (2 players)                   | throws `IllegalStateException` with message `"gameEngine.play.notInHand"` | yes          |

---

## Method 19: ```public int countAlive()```

Promoted from private to public for direct unit testing; same logic used by
`isGameOver()`.

### Step 1-3 Results

| Step   | Input                           | Output                        |
|--------|---------------------------------|-------------------------------|
| Step 1 | none (instance query)           | Number of players still alive |
| Step 2 | n/a                             | `int`                         |
| Step 3 | all alive (5 players), one dead | `5` / `4`                     |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                                     | Expected output | Implemented? |
|-------------|-------------------------------------------------------|-----------------|--------------|
| TC1         | 5 players, current player marked dead, `countAlive()` | `4`             | yes          |
| TC2         | 2 players at game start, `countAlive()`               | `2`             | yes          |

---

## Method 20: ```public void playFromHand(CardType type)```

Promoted from private to public for direct unit testing and for killing mutant as you cannot directly play defuse or
exploding kitten; called by every
`play*` method after playability checks.

### Step 1-3 Results

| Step   | Input                                                         | Output                                                       |
|--------|---------------------------------------------------------------|--------------------------------------------------------------|
| Step 1 | Card type the current player attempts to play from their hand | Card discarded and play recorded, or exception               |
| Step 2 | `CardType`                                                    | void / `IllegalArgumentException` / `IllegalStateException`  |
| Step 3 | unplayable type in hand (DEFUSE), playable type absent (SKIP) | `rule.play.cannotPlayDirectly` / `gameEngine.play.notInHand` |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                            | Expected output                                                                 | Implemented? |
|-------------|----------------------------------------------|---------------------------------------------------------------------------------|--------------|
| TC1         | current holds DEFUSE, `playFromHand(DEFUSE)` | throws `IllegalArgumentException` with message `"rule.play.cannotPlayDirectly"` | yes          |
| TC2         | current has no SKIP, `playFromHand(SKIP)`    | throws `IllegalStateException` with message `"gameEngine.play.notInHand"`       | yes          |

---

## Recall the 4 steps of BVA

### Step 1: Describe the input and output in terms of the domain.

### Step 2: Choose the data type for the input and the output from the BVA Catalog.

### Step 3: Select concrete values along the edges for the input and the output.

### Step 4: Determine the test cases using either all-combination or each-choice strategy.
