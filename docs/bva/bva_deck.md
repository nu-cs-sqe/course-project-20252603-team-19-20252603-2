# BVA for Deck of Cards
Edited by Allan

- format of method names: FeatureToTest_StateUnderTest_ExpectedBehavior


## 1. Method under test GetSize()
- input: void
- output: int
- TC 1: GetSize_EmptyDeck_Returns0
  - State of the system: Deck with list of 0 cards
  - Expected output: returns 0
- TC 2: GetSize_DeckSize1_Returns1
    - State of the system: Deck with list of 1 card
    - Expected output: returns 1
- TC 3: GetSize_DeckSize9_Returns9
    - State of the system: Deck with list of 9 cards
    - Expected output: returns 9

## 2. Method under test GetTopCard()
- input: void
- output: Card object
- TC 1: GetTopCard_EmptyDeck_ThrowsError
  - State of the system: Deck with list of 0 cards
  - Expected output: throws error
- TC 2: GetTopCard_DeckSize1_ReturnsCard
    - State of the system: Deck with list of 1 card
    - Expected output: returns the card
- TC 3: GetTopCard_DeckSize2_ReturnsTopCard
    - State of the system: Deck with list of 2 cards
    - Expected output: returns top card
- TC 4: GetTopCard_DeckSize9_ReturnsTopCard
    - State of the system: Deck with list of 9 cards
    - Expected output: returns top card

