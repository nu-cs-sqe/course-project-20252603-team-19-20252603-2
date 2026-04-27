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

