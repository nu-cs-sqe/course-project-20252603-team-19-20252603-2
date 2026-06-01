# Project Use Cases Document

## Player Class

### Data Members
- `playerId`: int — immutable; assigned at construction.
- `hand`: `List<Card>`
- `isAlive`: boolean — true until this player draws an undefused Exploding Kitten.

### Methods
- `Player(int playerId)` — constructor.
- `getPlayerId(): int`
- `addCardToHand(Card)`
- `removeCardFromHand(int index): Card`
- `getHandSize(): int`
- `getCardAt(int index): Card`
- `getHand(): List<Card>` — returns a defensive copy of the hand (`new ArrayList<>(hand)`).
- `hasCard(CardType): boolean`
- `getIndexOfCard(CardType): int`
- `isAlive(): boolean`
- `markDead()`


---

## Card Class

### Data Members
- `cardType`: `CardType` — immutable; set at construction.

### Methods
- `Card(CardType cardType)` — constructor; rejects `null`.
- `getCardType(): CardType`


---

## CardType (enum, supporting Card)

### Values
- `EXPLODING_KITTEN`
- `DEFUSE`
- `ATTACK`
- `SHUFFLE`
- `SKIP`
- `SEE_THE_FUTURE`
- `NOPE`
- `CAT_CARDS`
- `FAVOR`

---

## Deck Class

### Data Members
- `drawPile`: `List<Card>`
- `discardPile`: `List<Card>`

### Methods
- `Deck()` — constructor.
- `shuffle()`
- `drawTop(): Card`
- `peekTop(int n): List<Card>` — supports *See the Future*.
- `insertAt(Card, int index): boolean` — supports *Defuse* re-inserting an Exploding Kitten anywhere in the draw pile.
- `discard(Card)`
- `getSize(): int`
- `isEmpty(): boolean`

---

## Main Class


---

## GameEngine Class

### Data Members
- `numPlayers`: `int` — immutable; supplied at construction; must be in `[2, 5]`.
- `players`: `List<Player>` — one `Player` per id `0..numPlayers-1`.
- `deck`: `Deck` — draw and discard piles; rigged during setup with
  `(numPlayers - 1)` Exploding Kittens and `(6 - numPlayers)` remaining Defuses.
- `turnTracker`: `TurnTracker` — tracks whose turn it is.

### Methods
- `GameEngine(int numPlayers)` — constructor; throws `IllegalArgumentException` if
  `numPlayers` is outside `[2, 5]`. Performs the full setup: builds a deck with
  no Exploding Kittens and only `(6 - numPlayers)` Defuses initially, deals
  `1 Defuse + 4 random` cards to each player, inserts `(numPlayers - 1)`
  Exploding Kittens back into the draw pile, then shuffles.
- `getNumPlayers(): int`
- `getPlayer(int playerId): Player` — throws `IllegalArgumentException` if id
  is outside `[0, numPlayers)`.
- `getCurrentPlayerId(): int`
- `getDrawPileSize(): int`
- `isDeckEmpty(): boolean` — true when the draw pile has no cards left; the UI
  checks this before letting the current player draw.
- `getPlayerHand(int playerId): List<Card>` — defensive copy of the given
  player's hand (delegates to `Player.getHand()`); used by the UI to render a
  hand at game start and on each turn change.
- `drawCardForCurrentPlayer(): Card` — draws the top card of the draw pile,
  adds it to the current player's hand, and returns it. Throws
  `IllegalStateException` (`deck.emptyType`) if the draw pile is empty.
- `advanceToNextPlayer()` — hands the turn to the next player via
  `TurnTracker.turnGoesToNextPlayer()`.

---

## ActionController Class


---

## TurnTracker Class

### Data Members

- `numTotalPlayers`: `int`
- `currentPlayer`: `int`
- `currentDirection`: `int`

### Methods

- `getNumTotalPlayers()`: `int`
- `setNumTotalPlayers()`
- `getCurrentPlayer()`: `int`
- `getCurrentDirection()`: `int`
- `changeCurrentDirection()`
- `turnGoesToNextPlayer()`
- `turnSkipsNextPlayer()`
- `turnGoesToCurrentPlayerAgain()`
- `turnReversesDirection()`


---

## RuleManager Class


---

## UI Class

### Screens
- Start Screen
- End Screen
- Game Screen
- Player Screen
- Instructions Screen
