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
3. Player enters the number of players.
   (Your team can also add "asking for player name" feature, etc.)
4. System shuffles the deck.
5. System removes all Exploding Kitten cards.
6. System deals 7 cards to each player.
7. System gives each player 1 Defuse card.
8. System returns the correct number of Exploding Kitten cards to the deck (1 less than the number of players).
9. System shuffles the deck again.
10. System sets the initial turn order.

Alternate Flows:

3.a The number of players entered is invalid (not in range 2–4).
  3.a.1 System displays an "invalid player count" message.
  3.a.2 Resumes at step 2.

Postconditions:

- Each player has 8 cards (7 random + 1 Defuse).
- The deck contains the correct number of Exploding Kittens (players − 1).
- The game is ready for the first turn.

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
