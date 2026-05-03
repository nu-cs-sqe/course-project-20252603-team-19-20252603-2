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
- `insertAt(Card, int index)` — supports *Defuse* re-inserting an Exploding Kitten anywhere in the draw pile.
- `discard(Card)`
- `size(): int`
- `isEmpty(): boolean`

---

## UI Class

### Screens
- Start Screen
- End Screen
- Game Screen
- Player Screen
- Instructions Screen
