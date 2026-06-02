# BVA Analysis for `RuleManager`

This file holds the BVA analysis for every public method of the `RuleManager` class. Each public method has its own `## Method N:` section; new methods append a new section as the class grows.

---

## Method 1: ```public void requirePlayable(CardType type)```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | The card type a player wants to play | nothing (legal) or exception (illegal) |
| Step 2 | Enum (`CardType`) | void / `IllegalArgumentException` |
| Step 3 | `DEFUSE`, `EXPLODING_KITTEN` (illegal), any other type e.g. `SKIP` (legal) | exception / exception / returns normally |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | `requirePlayable(SKIP)` | returns normally | yes |
| TC2 | `requirePlayable(DEFUSE)` | throws `IllegalArgumentException` with message `"rule.play.cannotPlayDirectly"` | yes |
| TC3 | `requirePlayable(EXPLODING_KITTEN)` | throws `IllegalArgumentException` with message `"rule.play.cannotPlayDirectly"` | yes |

---

## Method 2: ```public void requireValidTarget(Player actor, Player target)```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | The acting player and the chosen target | nothing (legal) or exception (illegal) |
| Step 2 | two `Player` references | void / `IllegalArgumentException` |
| Step 3 | distinct living target (legal), target == actor (illegal), dead target (illegal) | returns normally / exception / exception |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | actor id 0, distinct living target id 1 | returns normally | yes |
| TC2 | actor and target are the same player | throws `IllegalArgumentException` with message `"rule.target.invalid"` | yes |
| TC3 | distinct target that is not alive | throws `IllegalArgumentException` with message `"rule.target.invalid"` | yes |

---

## Method 3: ```public void requireCatPair(Player actor)```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | The acting player's hand | nothing (legal) or exception (illegal) |
| Step 2 | `Player` reference | void / `IllegalStateException` |
| Step 3 | 0, 1 cat cards (illegal), 2, 3 cat cards (legal) | exception / exception / returns normally / returns normally |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | actor holds 2 `CAT_CARDS` | returns normally | yes |
| TC2 | actor holds 0 `CAT_CARDS` | throws `IllegalStateException` with message `"rule.catPair.needTwo"` | yes |
| TC3 | actor holds 1 `CAT_CARDS` | throws `IllegalStateException` with message `"rule.catPair.needTwo"` | yes |
| TC4 | actor holds 3 `CAT_CARDS` | returns normally | no |

---

## Method 4: ```public void requireSomethingToNope(CardType lastPlayedCard)```

### Step 1-3 Results

| Step | Input | Output |
|------|-------|--------|
| Step 1 | The most recently played card type, or `null` if none | nothing (legal) or exception (illegal) |
| Step 2 | `CardType` or `null` | void / `IllegalStateException` |
| Step 3 | non-null last card (legal), `null` (illegal) | returns normally / exception |

### Step 4:
##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|------------------|-----------------|--------------|
| TC1 | `requireSomethingToNope(ATTACK)` | returns normally | yes |
| TC2 | `requireSomethingToNope(null)` | throws `IllegalStateException` with message `"rule.nope.nothingToCancel"` | yes |

---

## Recall the 4 steps of BVA
### Step 1: Describe the input and output in terms of the domain.
### Step 2: Choose the data type for the input and the output from the BVA Catalog.
### Step 3: Select concrete values along the edges for the input and the output.
### Step 4: Determine the test cases using either all-combination or each-choice strategy.