# BVA Analysis for Card constructor

## Method 1: ```public Card(CardType cardType)```
### Step 1-3 Results
|        | Input                                                         | (if more to consider for input) | Output                                                                                                  |
|--------|---------------------------------------------------------------|---------------------------------|---------------------------------------------------------------------------------------------------------|
| Step 1 | A `CardType` value to assign to the new card, or `null`       |                                 | A new `Card` whose stored type equals the argument; or an `IllegalArgumentException` on the null edge   |
| Step 2 | Enumeration (`CardType`) augmented with the `null` reference  |                                 | A `Card` instance / `IllegalArgumentException`                                                          |
| Step 3 | `EXPLODING_KITTEN` (representative valid value), `null`       |                                 | A `Card` with `cardType = EXPLODING_KITTEN` / `IllegalArgumentException`                                |

### Step 4:
##### All-combination or each-choice: each-choice

|              | System under test            | Expected output                                                  | Implemented? |
|--------------|------------------------------|------------------------------------------------------------------|--------------|
| Test Case 1  | `new Card(EXPLODING_KITTEN)` | constructor returns normally; `getCardType()` == `EXPLODING_KITTEN` | no           |
| Test Case 2  | `new Card(null)`             | `IllegalArgumentException` thrown; no `Card` produced            | no           |

---

## Recall the 4 steps of BVA
### Step 1: Describe the input and output in terms of the domain.
### Step 2: Choose the data type for the input and the output from the BVA Catalog.
### Step 3: Select concrete values along the edges for the input and the output.
### Step 4: Determine the test cases using either all-combination or each-choice strategy.