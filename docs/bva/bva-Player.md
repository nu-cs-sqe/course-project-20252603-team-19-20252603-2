# BVA Analysis for Player Class

## Method 1: ```public Player(int playerId)```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | Integer player id | New player starts alive with empty hand |
| Step 2 | `int` | `Player` instance |
| Step 3 | id `0`, `1` (representative) | `Player` with that id, `isAlive()==true`, `getHandSize()==0` |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | `new Player(0)` | `getPlayerId()==0`, `isAlive()==true`, `getHandSize()==0` | yes |

---

## Method 2: ```public int getPlayerId()```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | none (instance query) | Constructor-supplied id |
| Step 2 | n/a | `int` |
| Step 3 | id `0`, `1` | same `int` returned |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | `new Player(0).getPlayerId()` | `0` | yes |
| TC2 | `new Player(1).getPlayerId()` | `1` | yes |

---

## Method 3: ```public void addCardToHand(Card card)```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | Card to add (or `null`) | Hand grows by one, or `IllegalArgumentException` |
| Step 2 | Reference type (`Card`, possibly `null`) | mutated hand / exception |
| Step 3 | valid `Card`, `null` | size +1 / `IllegalArgumentException` with i18n key `player.addCardToHand.nullCard` |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | `addCardToHand(new Card(DEFUSE))` on empty hand | `getHandSize()==1` | yes |
| TC2 | `addCardToHand(null)` | throws `IllegalArgumentException` with message `"player.addCardToHand.nullCard"` | yes |

---

## Method 4: ```public int getHandSize()```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | none (instance query) | Number of cards in hand |
| Step 2 | n/a | `int` |
| Step 3 | empty hand, hand of size 1, hand of size 2 | `0`, `1`, `2` |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | new player | `0` | yes |
| TC2 | after one `addCardToHand` | `1` | yes |
| TC3 | after two `addCardToHand` | `2` | yes |

---

## Method 5: ```public Card getCardAt(int index)```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | hand index | `Card` at that index, or exception |
| Step 2 | `int`, hand `List<Card>` | `Card` / `IndexOutOfBoundsException` |
| Step 3 | index `-1`, `0`, `size-1`, `size`, on empty hand | exception / `Card` / `Card` / exception / exception |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | `getCardAt(0)` on hand of 1 | the card | yes |
| TC2 | `getCardAt(-1)` on hand of 1 | throws `IndexOutOfBoundsException` with message `"player.getCardAt.invalidIndex"` | yes |
| TC3 | `getCardAt(1)` on hand of 1 | throws `IndexOutOfBoundsException` with message `"player.getCardAt.invalidIndex"` | yes |
| TC4 | `getCardAt(0)` on empty hand | throws `IndexOutOfBoundsException` with message `"player.getCardAt.invalidIndex"` | yes |

---

## Method 6: ```public Card removeCardFromHand(int index)```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | hand index | `Card` removed, or exception |
| Step 2 | `int`, hand `List<Card>` | `Card` / `IndexOutOfBoundsException` |
| Step 3 | index `-1`, `0`, `size-1`, `size`, on empty hand | exception / `Card` / `Card` / exception / exception |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | `removeCardFromHand(0)` on hand of 1 | returns the card, `getHandSize()==0` | yes |
| TC2 | `removeCardFromHand(-1)` on hand of 1 | throws `IndexOutOfBoundsException` with message `"player.removeCardFromHand.invalidIndex"` | yes |
| TC3 | `removeCardFromHand(0)` on empty hand | throws `IndexOutOfBoundsException` with message `"player.removeCardFromHand.invalidIndex"` | yes |

---

## Method 7: ```public boolean hasCard(CardType type)```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | CardType to look for | Boolean presence in hand |
| Step 2 | Enum reference (`CardType`) | `boolean` |
| Step 3 | type present, type absent, empty hand | `true` / `false` / `false` |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | hand has DEFUSE, query DEFUSE | `true` | yes |
| TC2 | hand has DEFUSE, query ATTACK | `false` | yes |
| TC3 | empty hand, query DEFUSE | `false` | yes |

---

## Method 8: ```public int getIndexOfCard(CardType type)```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | CardType to find | First index of card of that type, or `-1` |
| Step 2 | Enum reference (`CardType`) | `int` |
| Step 3 | type at index 0, type at index 1, type absent, duplicates of type, empty hand | `0` / `1` / `-1` / first index / `-1` |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | hand `[DEFUSE, ATTACK]`, query ATTACK | `1` | yes |
| TC2 | hand `[DEFUSE]`, query ATTACK | `-1` | yes |
| TC3 | hand `[DEFUSE, DEFUSE]`, query DEFUSE | `0` | yes |
| TC4 | empty hand, query DEFUSE | `-1` | yes |

---

## Method 9: ```public boolean isAlive()```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | none (instance query) | Alive flag |
| Step 2 | n/a | `boolean` |
| Step 3 | new player, after `markDead()` | `true` / `false` |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | new player | `true` | yes |
| TC2 | player after `markDead()` | `false` | yes |

---

## Method 10: ```public void markDead()```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | none; depends on alive state | player set dead, or exception |
| Step 2 | n/a | state mutation / `IllegalStateException` |
| Step 3 | alive player, already dead player | `isAlive()==false` / `IllegalStateException` with key `player.markDead.alreadyDead` |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | `markDead()` on alive player | `isAlive()==false` | yes |
| TC2 | `markDead()` on already-dead player | throws `IllegalStateException` with message `"player.markDead.alreadyDead"` | yes |

---

## Method 11: ```public List<Card> getHand()```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | none (instance query) | A defensive copy of the hand's cards |
| Step 2 | n/a | `List<Card>` (a new list, not the internal one) |
| Step 3 | empty hand, hand of size 1 | empty list / list of size 1; mutating the returned list does not affect the player |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | `getHand()` on new player | empty list (`size()==0`) | yes |
| TC2 | `getHand()` after one `addCardToHand` | list of size 1 with that card | yes |
| TC3 | mutate the list returned by `getHand()`, then call `getHandSize()` | player's own hand size is unchanged (copy is defensive) | yes |

---

## Recall the 4 steps of BVA
### Step 1: Describe the input and output in terms of the domain.
### Step 2: Choose the data type for the input and the output from the BVA Catalog.
### Step 3: Select concrete values along the edges for the input and the output.
### Step 4: Determine the test cases using either all-combination or each-choice strategy.
