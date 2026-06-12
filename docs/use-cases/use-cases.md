# Use Cases

Use cases for the Exploding Kittens (Grab & Game edition) implementation. These describe player-facing flows that the `ui` and `domain` packages must support. Each use case follows the structure: Actor, Preconditions, Main Flow, Alternate Flows, Postconditions.

---

## Use Case 1: Start New Game

Actor: Player

Preconditions:

The game application is launched.

Main Flow:

1. Player clicks "Start Game".
2. System asks for the total number of players.
3. Player enters the number of players (2–5).
4. System creates one player seat per requested player, numbered 0..N-1.
5. System sets aside all 4 Exploding Kittens and all 6 Defuses from the full deck.
6. System shuffles the remaining 64 non-Exploding-Kitten, non-Defuse cards.
7. System deals each player 4 cards from that shuffled pool plus 1 Defuse, for a starting hand of 5 cards.
8. System returns the unused (6 − N) Defuses and (N − 1) Exploding Kittens to the draw pile.
9. System shuffles the draw pile.
10. System sets the initial turn to player 0, direction = forward (+1).

Alternate Flows:

3.a The number of players entered is outside the range 2–5.
  3.a.1 System displays the i18n-keyed "invalid player count" message (`gameEngine.numPlayers.outOfRange`).
  3.a.2 Resumes at step 2.

Postconditions:

- Each of the N players has exactly 5 cards in hand, including a Defuse and no Exploding Kitten.
- The draw pile holds (64 − 4·N) random cards + (6 − N) Defuses + (N − 1) Exploding Kittens, shuffled — 61 cards for 2 players, 49 cards for 5 players.
- The game is ready for the first turn, starting with player 0.

---

## Use Case 2: Take a Turn

Actor: Current Player

Preconditions:

- A game is in progress.
- The current player is alive.
- It is the current player's turn.

Main Flow:

1. System displays the current player's hand and prompts them to play a card or pass.
2. Player chooses to play a card from their hand.
3. System validates that the chosen card is legal to play from the hand (e.g., Nope and Defuse cannot be initiated this way).
4. System places the played card face-up on the discard pile and applies its effect.
5. After the effect resolves, system returns to step 1 so the player may play additional cards.
6. Player chooses to pass.
7. System draws the top card of the draw pile into the player's hand.
8. System advances to the next living player.

Alternate Flows:

3.a The chosen card is not playable in the current state.
  3.a.1 System rejects the play and resumes at step 1.

7.a The drawn card is an Exploding Kitten.
  7.a.1 Branch to Use Case 3 (Draw an Exploding Kitten).

Postconditions:

- Any cards the player chose to play are in the discard pile and their effects have been applied.
- The player has either drawn a non-Kitten card to end their turn, or has triggered the Exploding Kitten flow.
- Turn order has advanced (unless modified by Attack or Skip).

---

## Use Case 3: Draw an Exploding Kitten

Actor: Current Player

Preconditions:

- The player has just drawn the top card of the draw pile.
- The drawn card is an Exploding Kitten.

Main Flow:

1. System reveals the Exploding Kitten face-up to all players.
2. System checks the player's hand for a Defuse card.
3. Player has no Defuse — system marks the player as eliminated.
4. System places the Exploding Kitten face-up in front of the eliminated player.
5. System discards the eliminated player's remaining cards face-down.
6. System advances to the next living player's turn.

Alternate Flows:

3.a Player has at least one Defuse.
  3.a.1 Branch to Use Case 4 (Defuse an Exploding Kitten).

6.a Only one living player remains.
  6.a.1 Branch to Use Case 7 (End Game).

Postconditions:

- The player is eliminated, or the Exploding Kitten was reinserted via Defuse.
- The next living player's turn begins (unless the game has ended).

---

## Use Case 4: Defuse an Exploding Kitten

Actor: Current Player

Preconditions:

- The player has just drawn an Exploding Kitten.
- The player's hand contains at least one Defuse card.

Main Flow:

1. Player chooses to play a Defuse card.
2. System places the Defuse on the discard pile.
3. System prompts the player to choose a secret insertion index in the draw pile (0 = top, size = bottom).
4. Player enters a valid insertion index.
5. System inserts the Exploding Kitten at the chosen index without revealing the position to other players.
6. System ends the current player's turn.
7. System advances to the next living player.

Alternate Flows:

4.a Player enters an index outside the valid range.
  4.a.1 System rejects the input and resumes at step 3.

Postconditions:

- The player remains alive.
- The Defuse is in the discard pile.
- The Exploding Kitten is back in the draw pile at the player's chosen index.
- The turn has passed to the next living player.

---

## Use Case 5: Play See the Future

Actor: Current Player

Preconditions:

- It is the current player's turn.
- The player's hand contains at least one See the Future card.

Main Flow:

1. Player selects See the Future from their hand.
2. System places the card on the discard pile.
3. System privately reveals the top two cards of the draw pile to the current player, in draw order.
4. Player acknowledges and dismisses the preview.
5. System returns to the play-or-pass prompt for the same player.

Alternate Flows:

3.a The draw pile contains fewer than two cards.
  3.a.1 System reveals only the cards that exist (one card or none).

Postconditions:

- The See the Future card is in the discard pile.
- The draw pile's order is unchanged.
- It is still the same player's turn.

---

## Use Case 6: Play Attack

Actor: Current Player

Preconditions:

- It is the current player's turn.
- The player's hand contains at least one Attack card.

Main Flow:

1. Player selects Attack from their hand.
2. System places the card on the discard pile.
3. System ends the current player's turn without requiring a draw.
4. System forces the next living player to take two consecutive turns.

Alternate Flows:

4.a The current player was already serving a stacked Attack with N forced turns remaining.
  4.a.1 System transfers the remaining N turns to the next player and adds 2 more, so they owe N + 2 turns.

Postconditions:

- The Attack card is in the discard pile.
- The current player did not draw a card.
- The next player owes the correct number of forced turns.

---

## Use Case 7: End Game

Actor: System

Preconditions:

- A game is in progress.
- A player has just been eliminated by an undefused Exploding Kitten.

Main Flow:

1. System checks the count of living players.
2. Exactly one living player remains.
3. System declares that player the winner.
4. System transitions to the End Screen, showing the winner's identifier.
5. System offers the player(s) the option to start a new game or exit.

Alternate Flows:

2.a More than one living player remains.
  2.a.1 System resumes the normal turn cycle (returns control to Use Case 2).

Postconditions:

- The game is no longer accepting turn actions.
- The winning player is displayed.
- A new game can be started, or the application can be exited.

---

## Use Case 8: View Hand and Draw to End a Turn

Actor: Current Player

Preconditions:

- A game is in progress.
- It is the current player's turn.

Main Flow:

1. System shows the current player whose turn it is and displays that player's hand.
2. Player clicks "Draw Card" to end their turn.
3. System confirms the draw pile still has cards.
4. System moves the top card of the draw pile into the current player's hand and reveals it.
5. System hands the turn to the next player.
6. System displays the next player's hand, ready for their turn.

Alternate Flows:

3.a The draw pile is empty.
  3.a.1 System does not draw and instead branches to Use Case 7 (End Game) to decide the winner.

4.a The drawn card is an Exploding Kitten.
  4.a.1 Branch to Use Case 3 (Draw an Exploding Kitten).

Postconditions:

- The current player has drawn exactly one card (unless the pile was empty).
- The hand shown to each player is a snapshot that cannot be edited from outside the game.
- The turn has passed to the next player, or the game has ended.
---

## Use Case 9: Play Skip

Actor: Current Player

Preconditions:

- It is the current player's turn.
- The player's hand contains at least one Skip card.

Main Flow:

1. Player selects Skip from their hand.
2. System places the Skip on the discard pile.
3. System ends one of the player's owed turns without making them draw.
4. If the player owes no more turns, system advances to the next living player.

Alternate Flows:

4.a The player was under an Attack and still owes one or more turns.
  4.a.1 System keeps the turn with the same player for their remaining owed turn(s).

Postconditions:

- The Skip card is in the discard pile.
- The player did not draw a card for the skipped turn.
- The turn has advanced only if no owed turns remain.

---

## Use Case 10: Play Shuffle

Actor: Current Player

Preconditions:

- It is the current player's turn.
- The player's hand contains at least one Shuffle card.

Main Flow:

1. Player selects Shuffle from their hand.
2. System places the Shuffle on the discard pile.
3. System randomly reorders the draw pile.
4. System returns to the play-or-pass prompt for the same player.

Postconditions:

- The Shuffle card is in the discard pile.
- The draw pile contains the same cards in a new random order.
- It is still the same player's turn.

---

## Use Case 11: Play Reverse

Actor: Current Player

Preconditions:

- It is the current player's turn.
- The player's hand contains at least one Reverse card.

Main Flow:

1. Player selects Reverse from their hand.
2. System places the Reverse on the discard pile.
3. System flips the direction of play (forward becomes backward and vice versa).
4. System ends one owed turn without making the player draw, then advances in the new direction if no owed turns remain.

Postconditions:

- The Reverse card is in the discard pile.
- The direction of play is flipped.
- The turn has advanced in the new direction (unless the player still owes turns).

---

## Use Case 12: Play a Targeted Attack

Actor: Current Player

Preconditions:

- It is the current player's turn.
- The player's hand contains at least one Targeted Attack card.

Main Flow:

1. Player selects Targeted Attack and chooses a living opponent.
2. System places the Targeted Attack on the discard pile.
3. System ends the current player's turn without requiring a draw.
4. System makes the chosen opponent the current player and forces them to take two consecutive turns.

Alternate Flows:

1.a The chosen target is the player themselves or is already eliminated.
  1.a.1 System rejects the choice (`rule.target.invalid`) and resumes at step 1.

4.a The current player was already serving a stacked Attack with N turns remaining.
  4.a.1 System transfers the remaining N turns to the chosen target and adds 2 more.

Postconditions:

- The Targeted Attack card is in the discard pile.
- The chosen opponent is now the current player and owes the correct number of forced turns.

---

## Use Case 13: Play Favor

Actor: Current Player

Preconditions:

- It is the current player's turn.
- The player's hand contains at least one Favor card.

Main Flow:

1. Player selects Favor and chooses a living opponent.
2. System places the Favor on the discard pile.
3. System takes the chosen card from the target's hand and gives it to the current player.
4. System returns to the play-or-pass prompt for the same player.

Alternate Flows:

1.a The chosen target is the player themselves or is already eliminated.
  1.a.1 System rejects the choice (`rule.target.invalid`) and resumes at step 1.

Postconditions:

- The Favor card is in the discard pile.
- One card has moved from the target's hand to the current player's hand.
- It is still the same player's turn.

---

## Use Case 14: Play a Matching Pair

Actor: Current Player

Preconditions:

- It is the current player's turn.
- The player's hand contains at least two cards of the same type (any type — Cat cards or any other matching pair) or using clone and feral cat as substitutions.

Main Flow:

1. Player selects two cards of the same type (can use 1 clone only as substitution for cat card and any number of feral cats to substitute cat card) and chooses a living opponent.
2. System places both cards on the discard pile.
3. System takes one card chosen at random from the target's hand and gives it to the current player.
4. System returns to the play-or-pass prompt for the same player.

Alternate Flows:

1.a The player does not hold two cards of the chosen type or does not have a valid 2 card combo.
  1.a.1 System rejects the play (`rule.catPair.needTwo`) and resumes at the play-or-pass prompt.

1.b The player tries to play two feral cats as the two card of the same type
  1.b.1 System rejects the play (`rule.catPair.feralCannotBeBaseType`) and resumes at the play-or-pass prompt.

1.c The player tries to play two clone cards as the three card of the same type
  1.c.1 System rejects the play (`rule.catPair.cloneCannotBeBaseType`) and resumes at the play-or-pass prompt.

1.d The chosen target is the player themselves or is already eliminated.
  1.d.1 System rejects the choice (`rule.target.invalid`) and resumes at step 1.

3.a The target has no cards.
  3.a.1 System steals nothing; the play still consumed the pair.

Postconditions:

- Both played cards are in the discard pile.
- At most one random card has moved from the target to the current player.
- It is still the same player's turn.

---

## Use Case 15: Nope a Played Card

Actor: Any Player (other than the one who just played)

Preconditions:

- A card was just played and its effect is the most recent action.
- The noping player's hand contains at least one Nope card.

Main Flow:

1. Player chooses to play a Nope in response to the last played card.
2. System places the Nope on the discard pile.
3. System undoes the last played card's effect according to its type:
   - Skip / Reverse — the turn returns to the player who played it (Reverse also restores the playing direction).
   - Attack / Targeted Attack — the forced-turn count is reduced by the amount the attack added and the turn returns to the attacker.
   - See the Future — the draw pile is shuffled so the peeked cards no longer apply.
   - Other cards — no automatic revert (see README design choices).

Alternate Flows:

1.a There is no recently played card to cancel.
  1.a.1 System rejects the play (`rule.nope.nothingToCancel`).

1.b The player does not hold a Nope card.
  1.b.1 System rejects the play (`gameEngine.play.notInHand`).

Postconditions:

- The Nope card is in the discard pile.
- The last action has been undone where the card type supports it, and there is no longer a pending played card to nope.

---

## Use Case 16: End Game by Exhausted Draw Pile

Actor: System

Preconditions:

- A game is in progress.
- The draw pile has just become empty while two or more players are still alive.

Main Flow:

1. System detects that the draw pile is empty.
2. System compares the hand sizes of all living players.
3. System declares the living player holding the most cards the winner.
4. System transitions to the End Screen, showing the winner.

Alternate Flows:

2.a Two or more living players are tied for the most cards.
  2.a.1 System breaks the tie in favor of the lowest player id.

Postconditions:

- The game is no longer accepting turn actions.
- The winner (most cards, lowest id on a tie) is displayed.

---

## Use Case 17: Play Three of a Kind

Actor: Current Player

Preconditions:

- It is the current player's turn.
- The player's hand contains at least three cards of the same type or another valid combo using clone or feral cat.

Main Flow:

1. Player selects three cards of the same type (excluding clone and feral cat), can use clone as one substitution and feral cat as substitution for cat card, chooses a living opponent, and names a specific card type they want.
2. System places all three cards on the discard pile.
3. System checks whether the target holds the named card.
4. The target has the named card — system takes it from the target and gives it to the current player.
5. System returns to the play-or-pass prompt for the same player.

Alternate Flows:

1.a The player does not hold three cards of the chosen type or does not have a valid 3 card combo.
  1.a.1 System rejects the play (`rule.catTriple.needThree`) and resumes at the play-or-pass prompt.

1.b The player tries to play three feral cats as the three card of the same type
  1.b.1 System rejects the play (`rule.catTriple.feralCannotBeBaseType`) and resumes at the play-or-pass prompt.

1.c The player tries to play three clone cards as the three card of the same type
  1.c.1 System rejects the play (`rule.catTriple.cloneCannotBeBaseType`) and resumes at the play-or-pass prompt.

1.d The chosen target is the player themselves or is already eliminated.
  1.b.1 System rejects the choice (`rule.target.invalid`) and resumes at step 1.

4.a The target does not have the named card.
  4.a.1 System takes nothing; the play still consumed the three cards.

Postconditions:

- All three played cards are in the discard pile.
- The named card has moved from the target to the current player if the target had it; otherwise no card moved.
- It is still the same player's turn.

--- 

## Use Case 18: Play Clone

Actor: Current Player

Preconditions:

- It is the current player's turn.
- The player's hand contains at least one Clone card.
- The lastPlayedCard cache in the system is not empty and contains a valid, cloneable card identity.

Main Flow:

1.	Player selects the Clone card from their hand (and provides any necessary parameters like a targetId or cardIndex if copying a targeted action).
2.	System validates that the lastPlayedCard is cloneable.
3.	System moves the Clone card from the player's hand to the discard pile.
4.	System temporarily grants the player a copy of the lastPlayedCard to evaluate its identity.
5.	System routes execution to the corresponding method matching the cloned card type (e.g., playSkip(), playFavor(), etc.) using the passed parameters.
6.	System resets the lastPlayedCard tracking history cache to null.
7.	System resumes gameplay based on the cloned card's natural post-resolution turn progression flow.

Alternate Flows:

1.a The user does not actually possess a Clone card in their hand array.
  1.a.1 System throws an IllegalStateException (gameEngine.play.notInHand) and halts execution.

2.a There is no card history to clone or the last card played was an illegal base type (e.g., another Clone or an opponent's Cat card).
  2.a.1 System rejects the play execution (rule.clone.cannotCloneClone or rule.clone.cannotCloneAnotherCatCard) and retains the Clone card in the player's hand.

Postconditions:

- The Clone card is placed in the discard pile.
- The lastPlayedCard cache is set to null to clear history tracking.
- The dynamic card action has executed successfully, applying its own unique side effects (stealing a card, reversing turns, or peeking at the deck).

--- 

## Use Case 19: Play Super Skip

Actor: Current Player

Preconditions:

- It is the current player's turn.
- player's hand contains at least one Super Skip card.

Main Flow:

1.	Player selects Super Skip from their hand.
2.	System places the Super Skip card on the discard pile.
3.	System updates the lastPlayedCard cache to SUPER_SKIP and tracks the actor's ID.
4.	System clears all required turns remaining for the current player down to zero, instantly terminating their turn without forcing them to draw.
5.	System advances control seamlessly to the next sequential living player.

Alternate Flows:

1.a The player does not actually possess a Super Skip card in their hand array.
  1.a.1 System rejects the choice (gameEngine.play.notInHand) and halts execution.

Postconditions:

- The Super Skip card is in the discard pile.
- The current player's turn penalty counter is reset to zero.
- Control has advanced to the next living player in the turn rotation.

---

## Use Case 20: Play Bury

Actor: Current Player

Preconditions:

- It is the current player's turn.
- The player's hand contains at least one Bury card.

Main Flow:

1.	Player selects Bury from their hand.
2.	System places the Bury card on the discard pile and logs it as lastPlayedCard.
3.	System draws the top card from the deck and holds it in a temporary "buried card" cache phase.
4.	System prompts the player to pick a depth index.
5.	System inserts the drawn card into the deck at the chosen index.
6.	System terminates the current player's turn without requiring another natural draw action.
7.	System advances control seamlessly to the next sequential living player.

Alternate Flows:

1.a The player does not hold a Bury card.
	1.a.1 System rejects execution (gameEngine.play.notInHand).

Postconditions:
- The Bury card is discarded.
- The top card of the deck has been relocated to a custom position.
- The current turn is ended, and control has advanced to the next player.

---

## Use Case 21: Play Personal Attack 3x

Actor: Current Player

Preconditions:

- It is the current player's turn.
- The player's hand contains at least one Personal Attack 3x card.

Main Flow:

1.	Player selects Personal Attack 3x from their hand.
2.	System places the card on the discard pile and updates lastPlayedCard.
3.	System updates the TurnTracker state, adding +3 total required turns to the current player's active turn pool.
4.	System terminates the current turn action phase and resets to the play-or-pass prompt for the same player to begin their first of three penalty turns.

Alternate Flows:

1.a The player does not hold a Personal Attack 3X card.
  1.a.1 System rejects execution (gameEngine.play.notInHand).

Postconditions:
- The card is in the discard pile.
- The active player remains the same, but their remaining turn execution counter is set to 3.

---

# Domain Class Contracts

Binding public API for domain classes (per `docs/STANDARDS.md`). Player-facing
use cases above describe behaviour; this section lists every public method.

## GameEngine Class

### Data Members

- `numPlayers`: `int` — immutable; supplied at construction; must be in `[2, 5]`.
- `players`: `List<Player>` — one `Player` per id `0..numPlayers-1`.
- `deck`: `Deck` — draw and discard piles.
- `turnTracker`: `TurnTracker` — tracks whose turn it is.

### Methods

- `GameEngine(int numPlayers)` — constructor; throws `IllegalArgumentException`
  if `numPlayers` is outside `[2, 5]`.
- `getNumPlayers(): int`
- `getPlayer(int playerId): Player` — throws `IllegalArgumentException` if id is
  outside `[0, numPlayers)`.
- `getCurrentPlayerId(): int`
- `getDrawPileSize(): int`
- `isDeckEmpty(): boolean`
- `getPlayerHand(int playerId): List<Card>` — defensive copy.
- `drawCardForCurrentPlayer(): Card` — throws `IllegalStateException`
  (`deck.emptyType`) if the draw pile is empty.
- `advanceToNextPlayer()` — hands the turn to the next living player.
- `endTurnByDrawing()` — consumes one owed turn; advances when none remain.
- `getDiscardPile(): List<Card>` — defensive copy.
- `getDrawPile(): List<Card>`
- `getForcedTurns(): int`
- `getLastPlayedCard(): CardType` — `null` before any card is played.
- `getCurrentDirection(): int`
- `setLastPlayedCard(CardType cardType)`
- `setForcedTurns(int forcedTurns)`
- `playFromHand(CardType type)` — validates playability, discards from the
  current hand, records the play; throws `IllegalStateException`
  (`gameEngine.play.notInHand`) when absent.
- `playSkip()`
- `playShuffle()`
- `playSeeTheFuture(): List<Card>`
- `playReverse()`
- `playAttack()`
- `playTargetedAttack(int targetId)`
- `playFavor(int targetId, int cardIndex)`
- `playCatPair(int targetId, List<CardType> selectedCards)`
- `playCatTriple(int targetId, List<CardType> selectedCards, CardType desiredCard)`
- `playClone(int targetId, int cardIndex): List<Card>`
- `playSuperSkip()`
- `playBury(int index)`
- `playPersonalAttack3X()`
- `playNope(int noperId)`
- `defuseDrawnKitten(int reinsertIndex)`
- `explodeCurrentPlayer()`
- `forcedTurnsAfterUndoingAttack(): int`
- `isGameOver(): boolean`
- `countAlive(): int` — number of living players; used by `isGameOver()`.
- `getWinnerId(): int` — throws `IllegalStateException` (`gameEngine.notOver`)
  if the game is not over.

---

## TurnTracker Class

### Data Members

- `numTotalPlayers`: `int`
- `currentPlayer`: `int`
- `currentDirection`: `int`

### Methods

- `getNumTotalPlayers(): int`
- `setNumTotalPlayers(int numTotalPlayers)` — throws `IllegalArgumentException`
  (`turnTracker.numPlayers.tooSmall`) when fewer than 2.
- `getCurrentPlayer(): int`
- `getCurrentDirection(): int`
- `changeCurrentDirection()` — multiplies direction by −1.
- `turnGoesToNextPlayer()` — advances one seat in the current direction.
- `turnSkipsNextPlayer()` — advances two seats in the current direction.