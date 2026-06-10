# BVA Analysis for Card constructor

## Method 1: ```public Card(CardType cardType)```

### Step 1-3 Results

|        | Input                                                        | (if more to consider for input) | Output                                                                                                |
|--------|--------------------------------------------------------------|---------------------------------|-------------------------------------------------------------------------------------------------------|
| Step 1 | A `CardType` value to assign to the new card, or `null`      |                                 | A new `Card` whose stored type equals the argument; or an `IllegalArgumentException` on the null edge |
| Step 2 | Enumeration (`CardType`) augmented with the `null` reference |                                 | A `Card` instance / `IllegalArgumentException`                                                        |
| Step 3 | `EXPLODING_KITTEN` (representative valid value), `null`      |                                 | A `Card` with `cardType = EXPLODING_KITTEN` / `IllegalArgumentException`                              |

### Step 4:

##### All-combination or each-choice: each-choice

|             | System under test            | Expected output                                                     | Implemented? |
|-------------|------------------------------|---------------------------------------------------------------------|--------------|
| Test Case 1 | `new Card(EXPLODING_KITTEN)` | constructor returns normally; `getCardType()` == `EXPLODING_KITTEN` | yes          |
| Test Case 2 | `new Card(null)`             | `IllegalArgumentException` thrown; no `Card` produced               | yes          |

---

## Method 2: ```public CardType getCardType()```

### Step 1-3 Results

|        | Input                       | (if more to consider for input) | Output                                                                                                                                  |
|--------|-----------------------------|---------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------|
| Step 1 | none (instance-state query) |                                 | The `CardType` value supplied at construction                                                                                           |
| Step 2 | n/a                         |                                 | Enumeration (`CardType`)                                                                                                                |
| Step 3 | n/a                         |                                 | `EXPLODING_KITTEN`, `DEFUSE`, `ATTACK`, `SHUFFLE`, `SKIP`, `SEE_THE_FUTURE`, `NOPE`, `CAT_CARDS`, `FAVOR`, `REVERSE`, `TARGETED_ATTACK` |

### Step 4:

##### All-combination or each-choice: each-choice

|              | System under test              | Expected output      | Implemented? |
|--------------|--------------------------------|----------------------|--------------|
| Test Case 1  | `new Card(EXPLODING_KITTEN)`   | `EXPLODING_KITTEN`   | yes          |
| Test Case 2  | `new Card(DEFUSE)`             | `DEFUSE`             | yes          |
| Test Case 3  | `new Card(ATTACK)`             | `ATTACK`             | yes          |
| Test Case 4  | `new Card(SHUFFLE)`            | `SHUFFLE`            | yes          |
| Test Case 5  | `new Card(SKIP)`               | `SKIP`               | yes          |
| Test Case 6  | `new Card(SEE_THE_FUTURE)`     | `SEE_THE_FUTURE`     | yes          |
| Test Case 7  | `new Card(NOPE)`               | `NOPE`               | yes          |
| Test Case 8  | `new Card(CAT_CARDS)`          | `CAT_CARDS`          | yes          |
| Test Case 9  | `new Card(FAVOR)`              | `FAVOR`              | yes          |
| Test Case 10 | `new Card(REVERSE)`            | `REVERSE`            | yes          |
| Test Case 11 | `new Card(TARGETED_ATTACK)`    | `TARGETED_ATTACK`    | yes          |
| Test Case 12 | `new Card(FERAL_CAT)`          | `FERAL_CAT`          | yes          |
| Test Case 13 | `new Card(CLONE)`              | `CLONE`              | yes          |
| Test Case 14 | `new Card(SUPER_SKIP)`         | `SUPER_SKIP`         | yes          |
| Test Case 15 | `new Card(BURY)`               | `BURY`               | yes          |
| Test Case 16 | `new Card(PERSONAL_ATTACK_3X)` | `PERSONAL_ATTACK_3X` | yes          |

---

## Recall the 4 steps of BVA
### Step 1: Describe the input and output in terms of the domain.
### Step 2: Choose the data type for the input and the output from the BVA Catalog.
### Step 3: Select concrete values along the edges for the input and the output.
### Step 4: Determine the test cases using either all-combination or each-choice strategy.
