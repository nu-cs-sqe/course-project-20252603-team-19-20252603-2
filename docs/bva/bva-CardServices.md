# BVA Analysis for `CardServices`

## Method 1: ```public static String getRandomCardImage(Random random, String cardName, int fileCount)```

### Step 1-3 Results

| Step   | Input                                                         | Output                                          |
|--------|---------------------------------------------------------------|-------------------------------------------------|
| Step 1 | `Random` + card name + file count                             | image path or exception if folder empty/missing |
| Step 2 | `Random`, `String` card name + `int` file count               | `String` path or `IllegalArgumentException`     |
| Step 3 | card name (valid or invalid) and Random object and file count | `String` path or `IllegalArgumentException`     |

### Step 4:

##### each-choice

| Test Case # | System under test                                                            | Expected output                                                                | Implemented? |
|-------------|------------------------------------------------------------------------------|--------------------------------------------------------------------------------|--------------|
| TC1         | card name `Attack`, file count = 4, mock `random` nextInt returns 0          | non-null path of `/assets/Attack/Attack1.png`                                  | yes          |
| TC2         | card name `CatCards`, file count = 5, mock `random` nextInt returns 0        | non-null path of `/assets/CatCards/CatCards1.png`                              | yes          |
| TC3         | card name `Defuse`, file count = 6, mock `random` nextInt returns 0          | non-null path of `/assets/Defuse/Defuse1.png`                                  | yes          |
| TC4         | card name `ExplodingKitten`, file count = 4, mock `random` nextInt returns 0 | non-null path of `/assets/ExplodingKitten/ExplodingKitten1.png`                | yes          |
| TC5         | card name `Favor`, file count = 4, mock `random` nextInt returns 0           | non-null path of `/assets/Favor/Favor1.png`                                    | yes          |
| TC6         | card name `Nope`, file count = 5, mock `random` nextInt returns 0            | non-null path of `/assets/Nope/Nope1.png`                                      | yes          |
| TC7         | card name `Reverse`, file count = 6, mock `random` nextInt returns 0         | non-null path of `/assets/Reverse/Reverse1.png`                                | yes          |
| TC8         | card name `SeeTheFuture`, file count = 5, mock `random` nextInt returns 0    | non-null path of `/assets/SeeTheFuture/SeeTheFuture1.png`                      | yes          |
| TC9         | card name `Shuffle`, file count = 4, mock `random` nextInt returns 0         | non-null path of `/assets/Shuffle/Shuffle1.png`                                | yes          |
| TC10        | card name `Skip`, file count = 4, mock `random` nextInt returns 0            | non-null path of `/assets/Skip/Skip1.png`                                      | yes          |
| TC11        | card name `TargetedAttack`, file count = 5, mock `random` nextInt returns 0  | non-null path of `/assets/TargetedAttack/TargetedAttack1.png`                  | yes          |
| TC12        | card name `Empty`, file count = 4, mock `random` nextInt returns 0           | throws `IllegalArgumentException` with message `cardServices.cardDoesNotExist` | yes          |

---

## Recall the 4 steps of BVA

### Step 1: Describe the input and output in terms of the domain.

### Step 2: Choose the data type for the input and the output from the BVA Catalog.

### Step 3: Select concrete values along the edges for the input and the output.

### Step 4: Determine the test cases using either all-combination or each-choice strategy.
