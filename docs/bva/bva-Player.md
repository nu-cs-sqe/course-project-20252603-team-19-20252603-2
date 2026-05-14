# BVA Analysis for Player Class

## Method 1: ```public Player(String name)```
### Step 1-3 Results
|        | Input                            | (if more to consider for input) | Output                                |
|--------|----------------------------------|---------------------------------|---------------------------------------|
| Step 1 | Player name as a `String` value  |                                 | New player starts alive with empty hand |
| Step 2 | Reference type (`String`)        |                                 | `Player` instance                     |
| Step 3 | `"Alice"` (representative value) |                                 | `Player` with `name="Alice"`          |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test      | Expected output                              | Implemented? |
|-------------|------------------------|----------------------------------------------|--------------|
| TC1         | `new Player("Alice")`  | `getName()` returns `"Alice"`                 | yes          |
| TC2         | `new Player("Alice")`  | `isAlive()` is `true`, `getHand().size()==0` | yes          |

---

## Method 2: ```public String getName()```
### Step 1-3 Results
|        | Input                   | (if more to consider for input) | Output        |
|--------|-------------------------|---------------------------------|---------------|
| Step 1 | none (instance query)   |                                 | Player name   |
| Step 2 | n/a                     |                                 | `String`      |
| Step 3 | instance named `"Alice"`|                                 | `"Alice"`     |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test      | Expected output | Implemented? |
|-------------|------------------------|-----------------|--------------|
| TC1         | `new Player("Alice")`  | `"Alice"`       | yes          |

---

## Method 3: ```public boolean isAlive()```
### Step 1-3 Results
|        | Input                                   | (if more to consider for input) | Output    |
|--------|-----------------------------------------|---------------------------------|-----------|
| Step 1 | none (instance query over alive status) |                                 | alive flag |
| Step 2 | n/a                                     |                                 | `boolean` |
| Step 3 | before and after `explode()`            |                                 | `true` / `false` |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test                         | Expected output | Implemented? |
|-------------|-------------------------------------------|-----------------|--------------|
| TC1         | new player before `explode()`             | `true`          | yes          |
| TC2         | player after one `explode()`              | `false`         | yes          |
| TC3         | second `explode()` on already-dead player | throws `IllegalStateException` with message `"Player is already dead"` | yes |

---

## Method 4: ```public List<CardType> getHand()```
### Step 1-3 Results
|        | Input                         | (if more to consider for input) | Output                                      |
|--------|-------------------------------|---------------------------------|---------------------------------------------|
| Step 1 | none (instance query)         |                                 | Current hand contents as read-only list      |
| Step 2 | n/a                           |                                 | `List<CardType>`                             |
| Step 3 | empty hand, one card in hand  |                                 | size `0` / size `1`; list is unmodifiable    |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test                      | Expected output                         | Implemented? |
|-------------|----------------------------------------|-----------------------------------------|--------------|
| TC1         | new player                             | `getHand().size()==0`                   | yes          |
| TC2         | after `addCard(DEFUSE)`                | `getHand().size()==1`                   | yes          |
| TC3         | attempt `getHand().add(ATTACK)`        | throws `UnsupportedOperationException`  | yes          |

---

## Method 5: ```public void addCard(CardType card)```
### Step 1-3 Results
|        | Input                               | (if more to consider for input) | Output                                                 |
|--------|-------------------------------------|---------------------------------|--------------------------------------------------------|
| Step 1 | Card to add, including invalid null |                                 | Hand size increments or exception                      |
| Step 2 | Enum reference (`CardType` / `null`) |                                | Mutated hand / `IllegalArgumentException`              |
| Step 3 | `DEFUSE`, `ATTACK`, `NOPE`, `null`  |                                 | size +1 per valid input / exception on null            |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test                             | Expected output                                            | Implemented? |
|-------------|-----------------------------------------------|------------------------------------------------------------|--------------|
| TC1         | add one valid card (`DEFUSE`)                 | hand size becomes `1`                                      | yes          |
| TC2         | add multiple valid cards (`DEFUSE`, `ATTACK`, `NOPE`) | hand size becomes `3`                         | yes          |
| TC3         | `addCard(null)`                               | throws `IllegalArgumentException` with message `"Card cannot be null"` | yes |

---

## Method 6: ```public void removeCard(CardType card)```
### Step 1-3 Results
|        | Input                                       | (if more to consider for input) | Output                                                |
|--------|---------------------------------------------|---------------------------------|-------------------------------------------------------|
| Step 1 | Card to remove from current hand             |                                 | Hand size decrements or exception                     |
| Step 2 | Enum reference (`CardType` / `null`)         |                                 | Mutated hand / `IllegalArgumentException`             |
| Step 3 | present card, absent card, empty hand, null  |                                 | size -1 / exception                                   |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test                   | Expected output                                                      | Implemented? |
|-------------|-------------------------------------|----------------------------------------------------------------------|--------------|
| TC1         | remove card that exists             | card removed; hand size decreases                                   | yes          |
| TC2         | remove card not in hand             | `IllegalArgumentException` with message `"Card not in hand: ATTACK"` | yes          |
| TC3         | remove from empty hand              | `IllegalArgumentException` with message `"Card not in hand: DEFUSE"` | yes          |
| TC4         | `removeCard(null)`                  | `IllegalArgumentException` with message `"Card not in hand: null"`   | yes          |

---

## Method 7: ```public boolean hasCard(CardType card)```
### Step 1-3 Results
|        | Input                        | (if more to consider for input) | Output     |
|--------|------------------------------|---------------------------------|------------|
| Step 1 | Card to check for containment |                                 | Presence flag |
| Step 2 | Enum reference (`CardType`)  |                                 | `boolean`  |
| Step 3 | present card, absent card    |                                 | `true` / `false` |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test          | Expected output | Implemented? |
|-------------|----------------------------|-----------------|--------------|
| TC1         | card exists in hand         | `true`          | yes          |
| TC2         | card absent from hand       | `false`         | yes          |

---

## Method 8: ```public void explode()```
### Step 1-3 Results
|        | Input                                     | (if more to consider for input) | Output                                                   |
|--------|-------------------------------------------|---------------------------------|----------------------------------------------------------|
| Step 1 | none; depends on current `alive` state    |                                 | player marked dead or exception                          |
| Step 2 | n/a                                       |                                 | state mutation / `IllegalStateException`                 |
| Step 3 | player alive, player already dead         |                                 | `alive=false` / exception with dead-state message        |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test                  | Expected output                                                  | Implemented? |
|-------------|------------------------------------|------------------------------------------------------------------|--------------|
| TC1         | first `explode()` on alive player  | `isAlive()` becomes `false`                                      | yes          |
| TC2         | second `explode()` call            | throws `IllegalStateException` with message `"Player is already dead"` | yes |

---

## Recall the 4 steps of BVA
### Step 1: Describe the input and output in terms of the domain.
### Step 2: Choose the data type for the input and the output from the BVA Catalog.
### Step 3: Select concrete values along the edges for the input and the output.
### Step 4: Determine the test cases using either all-combination or each-choice strategy.
