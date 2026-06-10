# BVA Analysis for `RuleManager`

This file holds the BVA analysis for every public method of the `RuleManager` class. Each public method has its own
`## Method N:` section; new methods append a new section as the class grows.

---

## Method 1: ```public void requirePlayable(CardType type)```

### Step 1-3 Results

| Step   | Input                                                                      | Output                                   |
|--------|----------------------------------------------------------------------------|------------------------------------------|
| Step 1 | The card type a player wants to play                                       | nothing (legal) or exception (illegal)   |
| Step 2 | Enum (`CardType`)                                                          | void / `IllegalArgumentException`        |
| Step 3 | `DEFUSE`, `EXPLODING_KITTEN` (illegal), any other type e.g. `SKIP` (legal) | exception / exception / returns normally |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                   | Expected output                                                                 | Implemented? |
|-------------|-------------------------------------|---------------------------------------------------------------------------------|--------------|
| TC1         | `requirePlayable(SKIP)`             | returns normally                                                                | yes          |
| TC2         | `requirePlayable(DEFUSE)`           | throws `IllegalArgumentException` with message `"rule.play.cannotPlayDirectly"` | yes          |
| TC3         | `requirePlayable(EXPLODING_KITTEN)` | throws `IllegalArgumentException` with message `"rule.play.cannotPlayDirectly"` | yes          |

---

## Method 2: ```public void requireValidTarget(Player actor, Player target)```

### Step 1-3 Results

| Step   | Input                                                                            | Output                                   |
|--------|----------------------------------------------------------------------------------|------------------------------------------|
| Step 1 | The acting player and the chosen target                                          | nothing (legal) or exception (illegal)   |
| Step 2 | two `Player` references                                                          | void / `IllegalArgumentException`        |
| Step 3 | distinct living target (legal), target == actor (illegal), dead target (illegal) | returns normally / exception / exception |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                       | Expected output                                                        | Implemented? |
|-------------|-----------------------------------------|------------------------------------------------------------------------|--------------|
| TC1         | actor id 0, distinct living target id 1 | returns normally                                                       | yes          |
| TC2         | actor and target are the same player    | throws `IllegalArgumentException` with message `"rule.target.invalid"` | yes          |
| TC3         | distinct target that is not alive       | throws `IllegalArgumentException` with message `"rule.target.invalid"` | yes          |

---

## Method 3: ```public void requireCatPair(Player actor, CardType cardType)```

### Step 1-3 Results

| Step   | Input                                                                | Output                                                      |
|--------|----------------------------------------------------------------------|-------------------------------------------------------------|
| Step 1 | The acting player's hand and the chosen `cardType` to pair           | nothing (legal) or exception (illegal)                      |
| Step 2 | `Player` reference + `CardType`                                      | void / `IllegalStateException`                              |
| Step 3 | 0, 1 of the type (illegal); 2, 3 of the type (legal); a non-cat type | exception / exception / returns normally / returns normally |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                                                             | Expected output                                                                     | Implemented? |
|-------------|-------------------------------------------------------------------------------|-------------------------------------------------------------------------------------|--------------|
| TC1         | actor holds 2 `CAT_CARDS`, `requireCatPair(actor, CAT_CARDS)`                 | returns normally                                                                    | yes          |
| TC2         | actor holds 0 `CAT_CARDS`, `requireCatPair(actor, CAT_CARDS)`                 | throws `IllegalStateException` with message `"rule.catPair.needTwo"`                | yes          |
| TC3         | actor holds 1 `CAT_CARDS`, `requireCatPair(actor, CAT_CARDS)`                 | throws `IllegalStateException` with message `"rule.catPair.needTwo"`                | yes          |
| TC4         | actor holds 3 `CAT_CARDS`, `requireCatPair(actor, CAT_CARDS)`                 | returns normally                                                                    | yes          |
| TC5         | actor holds 2 `ATTACK`, `requireCatPair(actor, ATTACK)`                       | returns normally (any matching pair)                                                | yes          |
| TC6         | actor holds 1 `ATTACK` + 1 `CAT_CARDS`, `requireCatPair(actor, ATTACK)`       | throws `IllegalStateException` with message `"rule.catPair.needTwo"`                | yes          |
| TC7         | actor holds 1 `CAT_CARDS` + 1 `FERAL CAT`, `requireCatPair(actor, CAT_CARDS)` | returns normally                                                                    | yes          |
| TC8         | actor holds 1 `ATTACK` + 1 `FERAL CAT`, `requireCatPair(actor, ATTACK)`       | throws `IllegalStateException` with message `"rule.catPair.needTwo"`                | yes          |
| TC9         | actor holds 1 `FERAL CAT` + 1 `FERAL CAT`, `requireCatPair(actor, FERAL_CAT)` | throws `IllegalStateException` with message `"rule.catPair.feralCannotBeBaseType"`  | yes          | 
| TC10        | actor holds 1 `CAT_CARDS` + 1 `CLONE`, `requireCatPair(actor, CAT_CARDS)`     | returns normally                                                                    | yes          |
| TC11        | actor holds 1 `ATTACK` + 1 `CLONE`, `requireCatPair(actor, ATTACK)`           | throws `IllegalStateException` with message `"rule.catPair.needTwo"`                | yes          |
| TC12        | actor holds 2 `CLONE`, `requireCatPair(actor, CLONE)`                         | throws `IllegalStateException` with message `"rule.catPair.cloneCannoteBeBaseType"` | yes          |

---

## Method 4: ```public void requireSomethingToNope(CardType lastPlayedCard)```

### Step 1-3 Results

| Step   | Input                                                 | Output                                 |
|--------|-------------------------------------------------------|----------------------------------------|
| Step 1 | The most recently played card type, or `null` if none | nothing (legal) or exception (illegal) |
| Step 2 | `CardType` or `null`                                  | void / `IllegalStateException`         |
| Step 3 | non-null last card (legal), `null` (illegal)          | returns normally / exception           |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                | Expected output                                                           | Implemented? |
|-------------|----------------------------------|---------------------------------------------------------------------------|--------------|
| TC1         | `requireSomethingToNope(ATTACK)` | returns normally                                                          | yes          |
| TC2         | `requireSomethingToNope(null)`   | throws `IllegalStateException` with message `"rule.nope.nothingToCancel"` | yes          |

---

## Method 5: ```public void requireCatTriple(Player actor, CardType selectedCard)```

### Step 1-3 Results

| Step   | Input                                                                                                                                                                                                                                   | Output                                 |
|--------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------|
| Step 1 | The acting player's hand and the chosen `selectedCard` to play three of                                                                                                                                                                 | nothing (legal) or exception (illegal) |
| Step 2 | `Player` reference + `CardType`                                                                                                                                                                                                         | void / `IllegalStateException`         |
| Step 3 | 3 of the type (legal); 2 of the type + 1 feral cat (legal); 1 of the type + 2 feral cat (legal); 2 of the type + 1 clone (legal); 1 of the type + 2 clone (illegal); 3 feral cats (illegal); 3 clone (illegal); 2 of the type (illegal) | returns normally / exception           |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                                                               | Expected output                                                                     | Implemented? |
|-------------|---------------------------------------------------------------------------------|-------------------------------------------------------------------------------------|--------------|
| TC1         | actor holds 3 `CAT_CARDS`, `requireCatTriple(actor, CAT_CARDS)`                 | returns normally                                                                    | yes          |
| TC2         | actor holds 2 `CAT_CARDS`, `requireCatTriple(actor, CAT_CARDS)`                 | throws `IllegalStateException` with message `"rule.catTriple.needThree"`            | yes          |
| TC3         | actor holds 2 `CAT_CARDS` + 1 `FERAL CAT`, `requireCatTriple(actor, CAT_CARDS)` | returns normally                                                                    | yes          |    
| TC4         | actor holds 1 `CAT_CARDS` + 2 `FERAL CAT`, `requireCatTriple(actor, CAT_CARDS)` | returns normally                                                                    | yes          |
| TC5         | actor holds 3 `FERAL_CAT`, `requireCatTriple(actor, FERAL_CAT)`                 | throw `IllegalStateException` with message `"rule.catTriple.feralCannotBeBaseType"` | yes          |
| TC6         | actor holds 2 `CAT_CARDS` + 1 `CLONE`, `requireCatTriple(actor, CAT_CARDS)`     | returns normally                                                                    | yes          |
| TC7         | actor holds 1 `CAT_CARDS` + 2 `CLONE`, `requireCatTriple(actor, CAT_CARDS)`     | throws `IllegalStateException` with message `"rule.catTriple.needThree"`            | yes          |
| TC8         | actor holds 3 `CLONE`, `requireCatTriple(actor, CLONE)`                         | throw `IllegalStateException` with message `"rule.catTriple.cloneCannotBeBaseType"` | yes          |

---

## Method 6: ```public void requireSomethingToClone(CardType lastPlayedCard)```

### Step 1-3 Results

| Step   | Input                                                    | Output                                 |
|--------|----------------------------------------------------------|----------------------------------------|
| Step 1 | The most recently played card type, or `null` if none    | nothing (legal) or exception (illegal) |
| Step 2 | `CardType` or `null`                                     | void / `IllegalStateException`         |
| Step 3 | non-null last card (legal), `null` (illegal), clone card | returns normally / exception           |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                 | Expected output                                                             | Implemented? |
|-------------|-----------------------------------|-----------------------------------------------------------------------------|--------------|
| TC1         | `requireSomethingToClone(ATTACK)` | returns normally                                                            | yes          |
| TC2         | `requireSomethingToClone(null)`   | throws `IllegalStateException` with message `"rule.clone.nothingToClone"`   | yes          |
| TC3         | `requireSomethingToClone(CLONE)`  | throws `IllegalStateException` with message `"rule.clone.cannotCloneClone"` | yes          |

---

## Method 7: ```public void requireValidInsertIndex(int index, int size)```

### Step 1-3 Results

### Step 1-3 Results

| Step   | Input                                                   | Output                                 |
|--------|---------------------------------------------------------|----------------------------------------|
| Step 1 | The insertion index chosen by the player                | nothing (legal) or exception (illegal) |
| Step 2 | int index, int size                                     | void / `IllegalStateException`         |
| Step 3 | int index = -1, 0, 30, 61, 62, 100, int size = 0, 1, 61 | returns normally / exception           |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                                                                | Expected output                                                        | Implemented? |
|-------------|----------------------------------------------------------------------------------|------------------------------------------------------------------------|-------------|
| TC1         | chosenDepth = -1, currentDeckSize = 61, `requireValidInsertIndex(-1, 61)         | throws `IllegalStateException` with message `"rule.bury.invalidIndex"` | yes         |
| TC2         | chosenDepth = 0, currentDeckSize = 61, `requireValidInsertIndex(0, 61)           | returns normally                                                       | yes         |
| TC3         | chosenDepth = 30, currentDeckSize = 61, `requireValidInsertIndex(30, 61)         | returns normally                                                       | yes         |
| TC4         | chosenDepth = 61, currentDeckSize = 61, `requireValidInsertIndex(61, 61)         | returns normally                                                       | yes         |
| TC5         | chosenDepth = 62, currentDeckSize = 61, `requireValidInsertIndex(62, 61)         | throws `IllegalStateException` with message `"rule.bury.invalidIndex"` | yes         |
| TC6         | chosenDepth = 100, currentDeckSize = 61, `requireValidInsertIndex(100, 61)       | throws `IllegalStateException` with message `"rule.bury.invalidIndex"` | yes         |
| TC7         | chosenDepth = 0, currentDeckSize = 0, requireValidInsertIndex(0, 0) (Empty Deck) | returns normally                                                       | yes         | 
| TC8         | chosenDepth = 1, currentDeckSize = 0, requireValidInsertIndex(1, 0) (Empty Deck) | throws `IllegalStateException` with message `"rule.bury.invalidIndex"` | yes         | 
| TC9         | chosenDepth = 1, currentDeckSize = 1, requireValidInsertIndex(1, 1) (Empty Deck) | returns normally                                                       | yes         | 

---

## Recall the 4 steps of BVA

### Step 1: Describe the input and output in terms of the domain.

### Step 2: Choose the data type for the input and the output from the BVA Catalog.

### Step 3: Select concrete values along the edges for the input and the output.

### Step 4: Determine the test cases using either all-combination or each-choice strategy.