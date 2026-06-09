# BVA Analysis for `ActionController`

This file holds the BVA analysis for every public method of the `ActionController` class. Each public method has its own `## Method N:` section; new methods append a new section as the class grows.

---

## Method 1: ```public void shuffleDeck(Deck deck)```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | The deck to shuffle | Same cards, possibly reordered |
| Step 2 | `Deck` | void (deck mutated) |
| Step 3 | a full deck | size unchanged, same multiset of cards |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test         | Expected output          | Implemented? |
|-------------|---------------------------|--------------------------|--------------|
| TC1         | `shuffleDeck(new Deck())` | deck size unchanged (74) | yes          |

---

## Method 2: ```public List<Card> peekTopThree(Deck deck)```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | The draw pile | Up to the top 3 cards, in draw order |
| Step 2 | `Deck` | `List<Card>` |
| Step 3 | deck of size 0, 2, >=3 | empty list / 2-card list / 3-card list; deck size unchanged |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | `peekTopThree` on a full deck | list of size 3; deck size unchanged | yes |
| TC2 | `peekTopThree` on a 2-card deck | list of size 2 | yes |
| TC3 | `peekTopThree` on an empty deck | empty list | yes |

---

## Method 3: ```public void reverseDirection(TurnTracker turnTracker)```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | The turn tracker | Playing direction flipped |
| Step 2 | `TurnTracker` | void (tracker mutated) |
| Step 3 | direction +1, direction -1 | becomes -1 / becomes +1 |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | tracker with direction +1 | `getCurrentDirection() == -1` | yes |
| TC2 | tracker with direction -1 | `getCurrentDirection() == 1` | yes |

---

## Method 4: ```public void giveCard(Player from, Player to, int cardIndex)```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | Giver, receiver, index of the card to give | Card moves from giver to receiver |
| Step 2 | two `Player`, `int` | void (hands mutated) / `IndexOutOfBoundsException` |
| Step 3 | valid index, out-of-range index | card transferred / exception |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | `from` has one DEFUSE, `giveCard(from, to, 0)` | `from` hand size 0, `to` hand has the DEFUSE | yes |
| TC2 | `giveCard(from, to, 0)` when `from` hand empty | throws `IndexOutOfBoundsException` | yes |

---

## Method 5: ```public void stealRandomCard(Player from, Player to)```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | Victim and thief | One random card moves from victim to thief |
| Step 2 | two `Player` (with injected `Random`) | void (hands mutated) |
| Step 3 | victim with 0 cards, 1 card, many cards (seeded random) | no-op / that card moves / the seeded-index card moves |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | victim has exactly one card | that card moves to thief; victim hand size 0 | yes |
| TC2 | victim has several cards, seeded `Random` | the card at the seeded index moves to thief | yes |
| TC3 | victim has no cards | no-op; thief hand unchanged | yes |

---

## Method 6: ```public void stealDesiredCard(Player from, Player to, CardType desiredCard)```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | Victim, thief, and the specific card type wanted | the named card moves from victim to thief if present; otherwise nothing |
| Step 2 | two `Player` + `CardType` | void (hands mutated or unchanged) |
| Step 3 | victim holds the desired type, victim lacks it | that card moves / no-op |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | victim holds a DEFUSE, `stealDesiredCard(from, to, DEFUSE)` | the DEFUSE moves to the thief; victim hand shrinks by 1 | yes |
| TC2 | victim lacks the desired type | no-op; both hands unchanged | yes |

---

## Recall the 4 steps of BVA
### Step 1: Describe the input and output in terms of the domain.
### Step 2: Choose the data type for the input and the output from the BVA Catalog.
### Step 3: Select concrete values along the edges for the input and the output.
### Step 4: Determine the test cases using either all-combination or each-choice strategy.