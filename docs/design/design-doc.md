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
- `REVERSE`
- `TARGETED_ATTACK`

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

Applies the deck/player-level effect of a played card. Stateless apart from an
injected `Random` (so the Cat-pair random steal is deterministic under test).
Turn-flow effects that change *whose* turn it is or *how many* turns are owed
(Skip, Attack, Targeted Attack) live in `GameEngine`, since they manipulate its
turn-state; `ActionController` only touches the deck and players' hands.

### Data Members
- `random`: `Random` — source of randomness for `stealRandomCard`.

### Methods
- `ActionController()` — production constructor; uses `new Random()`.
- `ActionController(Random random)` — package-private; injects a seeded
  `Random` for deterministic tests.
- `shuffleDeck(Deck deck)` — `deck.shuffle()` (Shuffle card).
- `peekTopThree(Deck deck): List<Card>` — top up to 3 cards (See the Future);
  returns fewer when the draw pile is smaller; no state change.
- `reverseDirection(TurnTracker turnTracker)` — `turnTracker.changeCurrentDirection()`
  (Reverse card).
- `giveCard(Player from, Player to, int cardIndex)` — Favor: removes the card at
  `cardIndex` from `from`'s hand and adds it to `to`'s hand.
- `stealRandomCard(Player from, Player to)` — Cat pair: moves one randomly
  chosen card from `from`'s hand to `to`'s hand; no-op if `from` has no cards.

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

Pure, stateless validation of whether a play is legal in the current state.
Each method throws (with an i18n key) when the play is illegal and returns
normally otherwise. No mutation; takes domain objects as parameters so it is
trivially unit-testable.

### Methods
- `requirePlayable(CardType type)` — throws `IllegalArgumentException`
  (`rule.play.cannotPlayDirectly`) if `type` is `DEFUSE` or `EXPLODING_KITTEN`
  (those are never played directly from hand).
- `requireValidTarget(Player actor, Player target)` — throws
  `IllegalArgumentException` (`rule.target.invalid`) if `target` is the actor
  themselves or is not alive.
- `requireCatPair(Player actor)` — throws `IllegalStateException`
  (`rule.catPair.needTwo`) if the actor holds fewer than two `CAT_CARDS`.
- `requireSomethingToNope(CardType lastPlayedCard)` — throws
  `IllegalStateException` (`rule.nope.nothingToCancel`) if `lastPlayedCard` is
  `null`.

---

## UI Class

### Screens
- Start Screen
- End Screen
- Game Screen
- Player Screen
- Instructions Screen
