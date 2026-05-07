# BVA Analysis for Deck Class

## Method 1: ```public Deck()```

### Step 1–3 Results

| Step | Input                             | Output                 |
|------|-----------------------------------|------------------------|
| Step 1 | No input parameters (constructor) | Fully initialized deck |
| Step 2 | List<Card> construction           | List<Card>             |
| Step 3 | Boundary sizes                    | 0 → 56 cards           |

### Step 4:
##### each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|-------------|
| TC1 | `new Deck()` | deck size = 56 | yes         |
| TC2 | `new Deck()` | first card = EXPLODING_KITTEN | yes         |
| TC3 | `new Deck()` | last card = CAT_CARDS | yes         |
| TC4 | `new Deck()` | correct full ordering of cards | yes         |

---

## Method 2: ```public Card drawTop()```

### Step 1–3 Results

| Step | Input                       | Output        |
|------|-----------------------------|---------------|
| Step 1 | Deck state (0,1,2,56 cards) | Modified deck |
| Step 2 | List<Card>                  | List<Card>    |
| Step 3 | Boundary values             | 0, 1, 2, 56   |

### Step 4:
##### each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|-------------|
| TC1 | empty drawPile (`[]`) | throws IllegalStateException with message "The draw pile is empty" | yes         |
| TC2 | drawPile size = 1 | returns Card, drawPile becomes `[]` | yes         |
| TC3 | drawPile size = 2 | returns Card, drawPile size becomes `1` | no          |
| TC4 | drawPile size = 56 | returns Card, drawPile size becomes `55` | no          |

---

## Method 3: ```public void shuffle()```

### Step 1–3 Results

| Step | Input                  | Output                   |
|------|------------------------|--------------------------|
| Step 1 | Deck state             | reordered deck           |
| Step 2 | List<Card> permutation | List<Card>               |
| Step 3 | Boundary invariants    | same elements, same size |

### Step 4:
##### each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | empty deck (`[]`) | empty deck unchanged | no |
| TC2 | deck size = 1 | same single card | no |
| TC3 | deck size = 2 | same elements, order may change | no |
| TC4 | deck size = 56 | same elements, order may change | no |

---

## Recall the 4 steps of BVA
### Step 1: Describe the input and output in terms of the domain.
### Step 2: Choose the data type for the input and the output from the BVA Catalog.
### Step 3: Select concrete values along the edges for the input and the output.
### Step 4: Determine the test cases using either all-combination or each-choice strategy.