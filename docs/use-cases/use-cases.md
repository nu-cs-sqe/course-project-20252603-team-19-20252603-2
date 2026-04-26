# Project Use Cases Document

## Player Class

### Data Members
- `player_name`
- `is_p_start_first`: boolean
- `is_p_still_in_game`: boolean
- `turn_order`: int
- `is_p_win`: boolean

### Methods
- Constructor

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

### Data Members / Components
- List
- Size
- Top Card
- Rest of Deck
- Discard Pile

### Methods
- Shuffle Cards
- Draw Card

---

## UI Class

### Screens
- Start Screen
- End Screen
- Game Screen
- Player Screen
- Instructions Screen
