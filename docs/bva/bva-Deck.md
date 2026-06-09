# BVA Analysis for Deck Class

## Method 1: ```public Deck()```

### Step 1–3 Results

| Step   | Input                             | Output                 |
|--------|-----------------------------------|------------------------|
| Step 1 | No input parameters (constructor) | Fully initialized deck |
| Step 2 | N/A                               | `List<Card>`           |
| Step 3 | N/A                               | 74 cards in the deck   |

### Step 4:

##### each-choice

| Test Case # | System under test | Expected output                | Implemented? |
|-------------|-------------------|--------------------------------|--------------|
| TC1         | `new Deck()`      | deck size = 74                 | yes          |
| TC2         | `new Deck()`      | first card = EXPLODING_KITTEN  | yes          |
| TC3         | `new Deck()`      | last card = CAT_CARDS          | yes          |
| TC4         | `new Deck()`      | correct full ordering of cards | yes          |

---

## Method 2: ```public Card drawTop()```

### Step 1–3 Results

| Step   | Input                       | Output                                      |
|--------|-----------------------------|---------------------------------------------|
| Step 1 | Deck state (0,1,2,74 cards) | Modified deck                               |
| Step 2 | deck `List<Card>`           | Card Object                                 |
| Step 3 | deck sizes: 0, 1, 2, 74     | return top Card and deck size decrease by 1 |

### Step 4:

##### each-choice

| Test Case # | System under test     | Expected output                                            | Implemented? |
|-------------|-----------------------|------------------------------------------------------------|--------------|
| TC1         | empty drawPile (`[]`) | throws IllegalStateException with message "deck.emptyType" | yes          |
| TC2         | drawPile size = 1     | returns Card, drawPile becomes `[]`                        | yes          |
| TC3         | drawPile size = 2     | returns Card, drawPile size becomes `1`                    | yes          |
| TC4         | drawPile size = 74    | returns Card, drawPile size becomes `73`                   | yes          |

---

## Method 3: ```public void shuffle()```

### Step 1–3 Results

| Step   | Input                       | Output                                  |
|--------|-----------------------------|-----------------------------------------|
| Step 1 | The current drawPile state. | Reordered deck state with same elements |
| Step 2 | deck `List<Card>`           | N/A                                     |
| Step 3 | deck sizes: 0,1,2,74        | same elements, same size                |

### Step 4:

##### each-choice

| Test Case # | System under test | Expected output                 | Implemented? |
|-------------|-------------------|---------------------------------|--------------|
| TC1         | empty deck (`[]`) | empty deck unchanged            | yes          |
| TC2         | deck size = 1     | same single card                | yes          |
| TC3         | deck size = 2     | same elements, order may change | yes          |
| TC4         | deck size = 74    | same elements, order may change | yes          |

---

## Method 4: ```public List<Card> peekTop(int n)```

### Step 1–3 Results

| Step   | Input                                                      | Output                                                        |
|--------|------------------------------------------------------------|---------------------------------------------------------------|
| Step 1 | The current drawPile state and number of requested cards n | a list containing the top n cards from the deck               |
| Step 2 | deck `List<Card>` and Integer n                            | List<Card> / IllegalStateException / IllegalArgumentException |
| Step 3 | deck sizes: 0,1,2,74, int n: -1, 0, 1, 3, 55, 74, 75       | List<Card> / IllegalStateException / IllegalArgumentException |

### Step 4:

##### each-choice

| Test Case # | System under test          | Expected output                                                             | Implemented? |
|-------------|----------------------------|-----------------------------------------------------------------------------|--------------|
| TC1         | deck size = 0, int n = -1  | throws IllegalArgumentException with message "deck.peekTop.negativeN"       | yes          |
| TC2         | deck size = 1, int n = 0   | an empty list (`[]`) and deck size = 1                                      | yes          |
| TC3         | deck size = 2, int n = 1   | new list containing the first card with size = 1 and deck size = 2          | yes          |
| TC4         | deck size = 74, int n = 3  | new list containing the first 3 elements with size = 3 and deck size = 74   | yes          |
| TC5         | deck size = 74, int n = 55 | new list containing the first 55 elements with size = 55 and deck size = 74 | yes          |
| TC6         | deck size = 74, int n = 74 | new list containing all the elements with size = 74 and deck size = 74      | yes          |
| TC7         | deck size = 74, int n = 75 | throws IllegalStateException with message "deck.peekTop.tooManyRequested"   | yes          |
| TC8         | deck size = 3, int n = 5   | throws IllegalStateException with message "deck.peekTop.tooManyRequested"   | yes          |

---

## Method 5: ```public void discard(Card)```

### Step 1–3 Results

| Step   | Input                         | Output                                                |
|--------|-------------------------------|-------------------------------------------------------|
| Step 1 | A Card object (valid or null) | discardPile is modified                               |
| Step 2 | Reference type (Card)         | N/A                                                   |
| Step 3 | Valid card and null reference | discardPile increases by 1 / IllegalArgumentException |

### Step 4:

##### each-choice

| Test Case # | System under test                                    | Expected output                                                                       | Implemented? |
|-------------|------------------------------------------------------|---------------------------------------------------------------------------------------|--------------|
| TC1         | Card = Card(EXPLODING_KITTEN), discard pile size = 0 | discardPile size = 1, first element = EXPLODING_KITTEN                                | yes          |
| TC2         | Card = Card(DEFUSE), discard pile size = 0           | discardPile size = 1, first element = DEFUSE                                          | yes          |
| TC3         | Card = Card(ATTACK), discard pile size = 0           | discardPile size = 1, first element = ATTACK                                          | yes          |
| TC4         | Card = Card(SHUFFLE), discard pile size = 0          | discardPile size = 1, first element = SHUFFLE                                         | yes          |
| TC5         | Card = Card(SKIP), discard pile size = 0             | discardPile size = 1, first element = SKIP                                            | yes          |
| TC6         | Card = Card(SEE_THE_FUTURE), discard pile size = 0   | discardPile size = 1, first element = SEE_THE_FUTURE                                  | yes          |
| TC7         | Card = Card(NOPE), discard pile size = 0             | discardPile size = 1, first element = NOPE                                            | yes          |
| TC8         | Card = Card(CAT_CARDS), discard pile size = 0        | discardPile size = 1, first element = CAT_CARDS                                       | yes          |
| TC9         | Card = Card(FAVOR), discard pile size = 0            | discardPile size = 1, first element = FAVOR                                           | yes          |
| TC10        | Card = NULL, discard pile size = 0                   | throws IllegalArgumentException with message "card.nullType" and discardPile size = 0 | yes          |

## 6. Method under test getSize()

- input: void
- output: int
- TC 6.1: getSize_InitialDeck_ReturnsDeckSize
    - State of the system: Deck with list of 74 cards
    - Expected output: returns 74
    - Implemented: yes
- TC 6.2: getSize_EmptyDeck_ReturnsZero
    - State of the system: Deck with list of 0 cards
    - Expected output: returns 0
    - Implemented: yes

## 7. Method under test isEmpty()

- input: void
- output: boolean
- TC 7.1: isEmpty_InitialDeck_ReturnsFalse
    - State of the system: Deck with list of 74 cards
    - Expected output: returns false
    - Implemented: yes
- TC 7.2: isEmpty_EmptyDeck_ReturnsTrue
    - State of the system: Deck with list of 0 cards
    - Expected output: returns true
    - Implemented: yes

## 8. Method under test insertAt()

- input: Card of playing card, int of list index
- output: boolean
- TC 8.1: insertAt_InitialDeck_ReturnsTrue
    - input: Card EXPLODING_KITTEN, int 0
    - State of the system: Deck with list of 74 cards
    - Expected output: returns true
    - Implemented: yes
- TC 8.2: insertAt_InitialDeckIndex1_ReturnsTrue
    - input: Card EXPLODING_KITTEN, int 1
    - State of the system: Deck with list of 74 cards
    - Expected output: returns true
    - Implemented: yes
- TC 8.3: insertAt_InitialDeckIndex74_ReturnsTrue
    - input: Card EXPLODING_KITTEN, int 74
    - State of the system: Deck with list of 74 cards
    - Expected output: returns true
    - Implemented: yes
- TC 8.4: insertAt_InitialDeckIndex75_ThrowsException
    - input: Card EXPLODING_KITTEN, int 75
    - State of the system: Deck with list of 74 cards
    - Expected output: throws IllegalArgumentException error
    - Implemented: yes
- TC 8.5: insertAt_InitialDeckIndexNeg1_ThrowsException
    - input: Card EXPLODING_KITTEN, int -1
    - State of the system: Deck with list of 74 cards
    - Expected output: throws IllegalArgumentException error
    - Implemented: yes
- TC 8.6: insertAt_InitialDeckNullCard_ThrowsException
    - input: Card null, int 1
    - State of the system: Deck with list of 74 cards
    - Expected output: throws IllegalArgumentException error
    - Implemented: yes
- TC 8.7: insertAt_InitialDeckNullCardNeg1_ThrowsException
    - input: Card null, int -1
    - State of the system: Deck with list of 74 cards
    - Expected output: throws IllegalArgumentException error
    - Implemented: yes

---

## Recall the 4 steps of BVA
### Step 1: Describe the input and output in terms of the domain.
### Step 2: Choose the data type for the input and the output from the BVA Catalog.
### Step 3: Select concrete values along the edges for the input and the output.
### Step 4: Determine the test cases using either all-combination or each-choice strategy.
