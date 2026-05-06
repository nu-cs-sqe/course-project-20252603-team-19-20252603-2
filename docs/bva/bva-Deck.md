# BVA Analysis for Deck Class

### Method under test: Deck
- **TC1: deck_createFullDeck_correctSize** (Implemented)
  - **State of the system**: N/A
  - **Expected output**: 56

- **TC2: deck_createFullDeck_firstElementOne** (Not Implemented)
  - **State of the system**: N/A
  - **Expected output**: 1

- **TC3: deck_createFullDeck_lastElementNine** (Not Implemented)
  - **State of the system**: N/A
  - **Expected output**: 9

- **TC4: deck_createFullDeck_elementsFromOnetoNine** (Not Implemented)
  - **State of the system**: N/A
  - **Expected output**: [1,...,9]

### Method under test: DrawCard
- **TC1: drawTop_emptyDeck_throwsException** (Not Implemented)
  - **State of the system**: []
  - **Expected output**: IllegalStateException with a message like "The deck is empty, you cannot draw a card"

- **TC2: drawTop_sizeOneDeck_returnsEmptyDeck** (Not Implemented)
  - **State of the system**: [1]
  - **Expected output**: [] the deck should now be empty with first element removed

- **TC3: drawTop_sizeTwoDeck_removesTopCard** (Not Implemented)
  - **State of the system**: [1,2]
  - **Expected output**: [1] removes the last element, the deck should now be the size of 1

- **TC4: drawTop_maxSizeDeck_removesTopCard** (Not Implemented)
  - **State of the system**: [1,...,9]
  - **Expected output**: [1,...,9] removes the last element, the deck should now be size of 55

### Method under test: ShuffleCards
- **TC1: shuffle_emptyDeck_returnsEmptyDeck** (Not Implemented)
  - **State of the system**: []
  - **Expected output**: [] should return the same empty deck

- **TC2: shuffle_oneCardDeck_returnsSameDeck** (Not Implemented)
  - **State of the system**: [1]
  - **Expected output**: [1] should return same deck with 1 as the only element

- **TC3: shuffle_twoCardsDeck_preservesElementsAndSize** (Not Implemented)
  - **State of the system**: [1,2]
  - **Expected output**: the deck should contain the same elements as before and the size should stay unchanged

- **TC4: shuffle_maxSizeDeck_preseveresElementsAndSize** (Not Implemented)
  - **State of the system**: [1,...,9]
  - **Expected output**: the deck should contain the same elements as before and the size should stay unchanged
