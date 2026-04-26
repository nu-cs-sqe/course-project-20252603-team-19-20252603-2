# BVA Analysis for `Card.getCardType`

## Method 1: ```public CardType getCardType()```
### Step 1-3 Results
|        | Input                       | (if more to consider for input) | Output                                                                                |
|--------|-----------------------------|---------------------------------|---------------------------------------------------------------------------------------|
| Step 1 | none (instance-state query) |                                 | The `CardType` value supplied at construction                                         |
| Step 2 | n/a                         |                                 | Enumeration (`CardType`)                                                              |
| Step 3 | n/a                         |                                 | `EXPLODING_KITTEN`, `DEFUSE`, `ATTACK`, `SHUFFLE`, `SKIP`, `SEE_THE_FUTURE`, `NOPE`   |

### Step 4:
##### All-combination or each-choice: each-choice

|              | System under test            | Expected output    | Implemented? |
|--------------|------------------------------|--------------------|--------------|
| Test Case 1  | `new Card(EXPLODING_KITTEN)` | `EXPLODING_KITTEN` | no           |
| Test Case 2  | `new Card(DEFUSE)`           | `DEFUSE`           | no           |
| Test Case 3  | `new Card(ATTACK)`           | `ATTACK`           | no           |
| Test Case 4  | `new Card(SHUFFLE)`          | `SHUFFLE`          | no           |
| Test Case 5  | `new Card(SKIP)`             | `SKIP`             | no           |
| Test Case 6  | `new Card(SEE_THE_FUTURE)`   | `SEE_THE_FUTURE`   | no           |
| Test Case 7  | `new Card(NOPE)`             | `NOPE`             | no           |

---

## Recall the 4 steps of BVA
### Step 1: Describe the input and output in terms of the domain.
### Step 2: Choose the data type for the input and the output from the BVA Catalog.
### Step 3: Select concrete values along the edges for the input and the output.
### Step 4: Determine the test cases using either all-combination or each-choice strategy.
