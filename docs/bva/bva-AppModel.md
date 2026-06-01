# BVA Analysis for `AppModel`

## Method 1: ```public void toggleLanguage()```

### Step 1-3 Results

| Step   | Input                    | Output                                               |
|--------|--------------------------|------------------------------------------------------|
| Step 1 | none (instance mutation) | Switch locale between English and Simplified Chinese |
| Step 2 | n/a                      | void                                                 |
| Step 3 | n/a                      | Chinese, English                                     |

### Step 4:

##### All-combination or each-choice: each-choice

| Test Case # | System under test                                   | Expected output                                      | Implemented? |
|-------------|-----------------------------------------------------|------------------------------------------------------|-------------|
| TC1         | Initially Locale.ENGLISH and one`toggleLanguage()`  | `getSelectedLocale()` is `Locale.SIMPLIFIED_CHINESE` | yes         |
| TC2         | Initially Locale.ENGLISH and two `toggleLanguage()` | `getSelectedLocale()` is `Locale.ENGLISH`            | yes         |

---

## Recall the 4 steps of BVA

### Step 1: Describe the input and output in terms of the domain.

### Step 2: Choose the data type for the input and the output from the BVA Catalog.

### Step 3: Select concrete values along the edges for the input and the output.

### Step 4: Determine the test cases using either all-combination or each-choice strategy.
