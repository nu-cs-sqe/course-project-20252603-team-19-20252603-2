# BVA Analysis for Deck Class

## Method under test: `Deck()`

### Step 1: Input Domain
- No input parameters (constructor)

### Step 2: Identify Boundaries
- Empty initialization (0 cards before construction logic runs)
- Minimum valid deck structure after initialization
- Maximum boundary: full deck size = 56 cards

### Step 3: Boundary Value Test Cases
Focus on:
- Correct initialization of full deck
- Correct first card placement
- Correct last card placement
- Correct full ordering of cards

### Step 4: Test Cases

- **TC1: deck_createFullDeck_correctSize** (Not Implemented)
    - **State of the system**: N/A
    - **Expected output**: 56

- **TC2: deck_createFullDeck_firstCardIsExplodingKitten** (Not Implemented)
    - **State of the system**: N/A
    - **Expected output**: CardType.EXPLODING_KITTEN

- **TC3: deck_createFullDeck_lastCardIsCatCards** (Not Implemented)
    - **State of the system**: N/A
    - **Expected output**: CardType.CAT_CARDS

- **TC4: deck_createFullDeck_containsCardsInCorrectOrder** (Not Implemented)
    - **State of the system**: N/A
    - **Expected output**: [CardType.EXPLODING_KITTEN,...,CardType.CAT_CARDS]

## Method under test: `drawTop()`

### Step 1: Input Domain
- Empty deck (0 cards)
- Single card deck (1 card)
- Two card deck (2 cards)
- Full deck (56 cards)

### Step 2: Identify Boundaries
- Lower boundary: empty deck (0)
- Transition boundary: 1 → 0
- Small boundary: 2 → 1
- General case: n → n-1

### Step 3: Boundary Value Test Cases
Focus on:
- Underflow condition (empty deck)
- Minimum valid removal (1 card)
- Small deck behavior (2 cards)
- Standard operation (full deck)

### Step 4: Test Cases

- **TC1: drawTop_emptyDeck_throwsException** (Not Implemented)
    - **State of the system**: []
    - **Expected output**: IllegalStateException with a message like "The deck is empty, you cannot draw a card"

- **TC2: drawTop_sizeOneDeck_returnsEmptyDeck** (Not Implemented)
    - **State of the system**: [CardType.EXPLODING_KITTEN]
    - **Expected output**: [] the deck should now be empty with first element removed

- **TC3: drawTop_sizeTwoDeck_removesTopCard** (Not Implemented)
    - **State of the system**: [CardType.EXPLODING_KITTEN,CardType.DEFUSE]
    - **Expected output**: [CardType.EXPLODING_KITTEN] removes the last element, the deck should now be the size of 1

- **TC4: drawTop_maxSizeDeck_removesTopCard** (Not Implemented)
    - **State of the system**: [CardType.EXPLODING_KITTEN,...,CardType.CAT_CARDS]
    - **Expected output**: [CardType.EXPLODING_KITTEN,...,CardType.CAT_CARDS] removes the last element, the deck should now be size of 55

## Method under test: `shuffle()`

### Step 1: Input Domain
- Empty deck
- Single card deck
- Two card deck
- Full deck (56 cards)

### Step 2: Identify Boundaries
- Lower stability boundary (0–1 elements)
- Minimal shuffle case (2 elements)
- Large stochastic case (56 elements)

### Step 3: Boundary Value Test Cases
Focus on:
- Preservation of elements
- Preservation of size
- Non-deterministic ordering (order may change)

### Step 4: Test Cases

- **TC1: shuffle_emptyDeck_returnsEmptyDeck** (Not Implemented)
    - **State of the system**: []
    - **Expected output**: [] should return the same empty deck

- **TC2: shuffle_oneCardDeck_returnsSameDeck** (Not Implemented)
    - **State of the system**: [CardType.EXPLODING_KITTEN]
    - **Expected output**: [CardType.EXPLODING_KITTEN] should return same deck with 1 as the only element

- **TC3: shuffle_twoCardsDeck_preservesElementsAndSize** (Not Implemented)
    - **State of the system**: [CardType.EXPLODING_KITTEN,CardType.DEFUSE]
    - **Expected output**: the deck should contain the same elements as before and the size should stay unchanged

- **TC4: shuffle_maxSizeDeck_preseveresElementsAndSize** (Not Implemented)
    - **State of the system**: [CardType.EXPLODING_KITTEN,...,CardType.CAT_CARDS]
    - **Expected output**: the deck should contain the same elements as before and the size should stay unchanged
