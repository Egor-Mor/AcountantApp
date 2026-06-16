# Bank Assistant

A simple Java console application for managing bank accounts and recording transactions. Data is persisted to CSV files when the program exits.

## Features

- Create bank accounts with auto-generated IDs
- Deposit and withdraw money
- List all accounts with balances
- Load existing data on startup
- Save accounts and operations to CSV on exit

## Requirements

- Java JDK 17 or later (project configured for OpenJDK 26 in IntelliJ)

## Project Structure

```
Project1/
├── README.md
├── Project1.iml
└── src/
    └── bank/
        ├── Assistant.java   # Application entry point and state management
        ├── Account.java     # Bank account model
        ├── Operation.java   # Transaction record model
        ├── CsvStorage.java  # CSV file read/write
        └── Tools.java       # Command parsing and execution
```

All classes belong to the `bank` package.

## How to Run

### IntelliJ IDEA

1. Open the `Project1` module.
2. Open `src/bank/Assistant.java`.
3. Run the `main` method (green play button).

### Command Line

From the `Project1` directory:

```bash
javac -d out src/bank/*.java
java -cp out bank.Assistant
```

CSV files are created in the **working directory** where you run the program (usually the project root or IDE run configuration folder).

## Commands

| Command | Syntax | Description |
|---------|--------|-------------|
| Create | `create <name>` | Creates a new account. Name must be a single word (no spaces). |
| Deposit | `deposit <accountId> <amount>` | Adds money to an account and records the operation. |
| Withdraw | `withdraw <accountId> <amount>` | Removes money from an account and records the operation. |
| List | `list` | Prints all accounts with id, name, and balance. |
| Exit | `exit` | Saves data to CSV and closes the program. |

### Example Session

```
Commands: create, deposit, withdraw, list, exit
> create Alice
Created account 1
> deposit 1 100
Done. Balance: 100.0
> withdraw 1 25
Done. Balance: 75.0
> list
1: Alice - 75.0
> exit
```

## CSV Data Format

The application uses two CSV files in the working directory.

### accounts.csv

| Column | Type | Description |
|--------|------|-------------|
| `id` | int | Unique account identifier |
| `name` | string | Account holder name |
| `balance` | double | Current balance |

Example:

```csv
id,name,balance
1,Alice,75.0
2,Bob,200.0
```

### operations.csv

| Column | Type | Description |
|--------|------|-------------|
| `id` | int | Unique operation identifier |
| `accountId` | int | ID of the related account |
| `type` | string | `deposit` or `withdraw` |
| `amount` | double | Transaction amount |

Example:

```csv
id,accountId,type,amount
1,1,deposit,100.0
2,1,withdraw,25.0
```

### CSV Notes

- The first line in each file is a header row and is skipped when loading.
- Values are comma-separated. Account names must not contain commas.
- If a CSV file does not exist on startup, an empty list is used.
- Data is written only when the user types `exit` (not after every command).

## Application Flow

```
Start
  │
  ▼
Load accounts.csv and operations.csv
  │
  ▼
Restore nextAccountId and nextOperationId from loaded data
  │
  ▼
Read commands in a loop ──► Tools.manipulate()
  │
  ▼
User types "exit"
  │
  ▼
Save accounts.csv and operations.csv
  │
  ▼
End
```

## Class Reference

### `Assistant`

Main application class. Holds in-memory state and coordinates loading, saving, and the command loop.

| Field | Type | Description |
|-------|------|-------------|
| `accounts` | `List<Account>` | All bank accounts |
| `operations` | `List<Operation>` | All recorded transactions |
| `nextAccountId` | `int` | Next ID to assign to a new account |
| `nextOperationId` | `int` | Next ID to assign to a new operation |

| Method | Description |
|--------|-------------|
| `main(String[] args)` | Entry point. Creates an `Assistant` instance, loads data, runs the command loop, then saves. |
| `loadData()` | Loads accounts and operations from CSV. Updates ID counters. |
| `saveData()` | Writes accounts and operations to CSV. |
| `findAccount(int id)` | Returns the account with the given ID, or `null` if not found. |


---

### `Account`

Represents a single bank account.

| Field | Type | Description |
|-------|------|-------------|
| `id` | `int` | Unique account ID |
| `name` | `String` | Account holder name |
| `balance` | `double` | Current balance (default `0`) |

| Method | Description |
|--------|-------------|
| `Account(int id, String name)` | Constructor. Balance starts at 0. |
| `deposit(double amount)` | Increases balance by `amount`. |
| `withdraw(double amount)` | Decreases balance by `amount`. |

---

### `Operation`

Represents one financial transaction linked to an account.

| Field | Type | Description |
|-------|------|-------------|
| `id` | `int` | Unique operation ID |
| `accountId` | `int` | ID of the affected account |
| `type` | `String` | `"deposit"` or `"withdraw"` |
| `amount` | `double` | Transaction amount |

| Method | Description |
|--------|-------------|
| `Operation(int id, int accountId, String type, double amount)` | Creates a new operation record. |

---

### `CsvStorage`

Handles reading and writing CSV files. All methods are static.

| Constant | Value | Description |
|----------|-------|-------------|
| `ACCOUNTS_FILE` | `"accounts.csv"` | Path to accounts file |
| `OPERATIONS_FILE` | `"operations.csv"` | Path to operations file |

| Method | Description |
|--------|-------------|
| `saveAccounts(List<Account>)` | Overwrites `accounts.csv` with the current account list. |
| `loadAccounts()` | Reads `accounts.csv`. Returns an empty list if the file is missing. |
| `saveOperations(List<Operation>)` | Overwrites `operations.csv` with the current operation list. |
| `loadOperations()` | Reads `operations.csv`. Returns an empty list if the file is missing. |

---

### `Tools`

Parses user input and performs the requested action on the `Assistant` instance.

| Method | Description |
|--------|-------------|
| `manipulate(String command, Assistant app)` | Splits the command string and routes to create, list, deposit, or withdraw logic. |

**Command handling details:**

- Commands are split on spaces (`command.split(" ")`).
- `create` uses only the second token as the name (single-word names only).
- `deposit` and `withdraw` require a valid numeric account ID and amount.
- Invalid or incomplete commands are silently ignored (no error message).

## Design Overview

```
┌─────────────┐     uses      ┌───────────┐
│  Assistant  │──────────────►│   Tools   │
└──────┬──────┘               └─────┬─────┘
       │                            │
       │ load/save                  │ creates/updates
       ▼                            ▼
┌─────────────┐               ┌───────────┐
│ CsvStorage  │               │  Account  │
└─────────────┘               └───────────┘
       │                            ▲
       │ reads/writes               │
       ▼                            │
  accounts.csv                 ┌───────────┐
  operations.csv               │ Operation │
                               └───────────┘
```

## Known Limitations

- Account names cannot contain spaces or commas.
- No validation for negative balances on withdraw.
- No validation for negative or zero deposit/withdraw amounts.
- Data is saved only on exit; a crash or forced close may lose unsaved changes.
- CSV parsing uses simple `split(",")` — not suitable for complex CSV with quoted fields.

## Possible Improvements

- Separate `Main` class from application logic.
- Save after each change instead of only on exit.
- Add input validation and clearer error messages.
- Support multi-word account names with proper CSV escaping.
- Add a command to view operation history for an account.
