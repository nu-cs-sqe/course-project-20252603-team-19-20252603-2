### Method under test: DrawCard
- **TC1: drawCard_emptyDeck_throwsException** (Not Implemented)
  - **State of the system**: []
  - **Expected output**: IllegalStateException with a message like "The deck is empty, you cannot draw a card"

- **TC2: drawCard_sizeOneDeck_returnsEmptyDeck** (Not Implemented)
  - **State of the system**: [1]
  - **Expected output**: [] the deck should now be empty with first element removed

- **TC3: drawCard_sizeTwoDeck_removesTopCard** (Not Implemented)
  - **State of the system**: [1,2]
  - **Expected output**: [1] removes the last element, the deck should now be the size of 1

- **TC4: drawCard_maxSizeDeck_removesTopCard** (Not Implemented)
  - **State of the system**: [1,...,56]
  - **Expected output**: [1,...,55] removes the last element, the deck should now be size of 55

### Method under test: ShuffleCards
- **TC1: shuffleCards_emptyDeck_returnsEmptyDeck** (Not Implemented)
  - **State of the system**: []
  - **Expected output**: [] should return the same empty deck

- **TC2: shuffleCards_oneCardDeck_returnsSameDeck** (Not Implemented)
  - **State of the system**: [1]
  - **Expected output**: [1] should return same deck with 1 as the only element

- **TC3: shuffleCards_twoCardsDeck_preservesElementsAndSize** (Not Implemented)
  - **State of the system**: [1,2]
  - **Expected output**: the deck should contain the same elements as before and the size should stay unchanged

- **TC4: shuffleCards_maxSizeDeck_preseveresElementsAndSize** (Not Implemented)
  - **State of the system**: [1,...,56]
  - **Expected output**: the deck should contain the same elements as before and the size should stay unchanged

### Method under test: Deck
- **TC1: deck_createFullDeck_correctSize** (Not Implemented)
  - **State of the system**: N/A
  - **Expected output**: 56

- **TC2: deck_createFullDeck_firstElementOne** (Not Implemented)
  - **State of the system**: N/A
  - **Expected output**: 1

- **TC3: deck_createFullDeck_lastElementFiftySix** (Not Implemented)
  - **State of the system**: N/A
  - **Expected output**: 9

- **TC4: deck_createFullDeck_elementsFromOnetoFiftySix** (Not Implemented)
  - **State of the system**: N/A
  - **Expected output**: [1,...,9]
