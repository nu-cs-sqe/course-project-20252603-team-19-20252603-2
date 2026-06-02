# BVA Analysis for `GameSetupModel`
---

## Method 1: ```public void capturePlayerNamesFromInputs(List<String> rawInputs, String defaultNamePrefix)```

### Step 1-3 Results

| Step   | Input                                                                      | Output                                                       |
|--------|----------------------------------------------------------------------------|--------------------------------------------------------------|
| Step 1 | Raw text-field values and a default prefix for blank entries               | Normalized player name list stored internally                |
| Step 2 | `List<String>` (possibly empty), `String` prefix                           | void (mutated `playerNames`)                                 |
| Step 3 | list of size 1, 2, 5, 6. With empty string, normal string, and white space | defaults applied / trim applied / `IllegalArgumentException` |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                                                     | Expected output                                                                | Implemented? |
|-------------|-----------------------------------------------------------------------|--------------------------------------------------------------------------------|--------------|
| TC1         | list size of 1 with Vincent and prefix is Player                      | throws `IllegalArgumentException` with message `gameSetupModel.tooFewPlayers`  | yes          |
| TC2         | list size of 2 with Vincent, Vinny and prefix is Player               | `["Vincent", "Vinny"]`                                                         | yes          |
| TC3         | list size of 5 with Vincent, Vinny, VV, V, vv and prefix is Player    | `["Vincent", "Vinny", "VV", "V", "vv"]`                                        | yes          |
| TC4         | list size of 6 with Vincent, Vinny, VV, V, vv, v and prefix is Player | throws `IllegalArgumentException` with message `gameSetupModel.tooManyPlayers` | yes          |
| TC5         | list size of 2 with Vincent, `   ` and prefix is Player               | `["Vincent", "Player 2"]`                                                      | yes          |
| TC6         | list size of 2 with Vincent, "" and prefix is Player                  | `["Vincent", "Player 2"]`                                                      | yes          |

---

## Recall the 4 steps of BVA

### Step 1: Describe the input and output in terms of the domain.

### Step 2: Choose the data type for the input and the output from the BVA Catalog.

### Step 3: Select concrete values along the edges for the input and the output.

### Step 4: Determine the test cases using either all-combination or each-choice strategy.
