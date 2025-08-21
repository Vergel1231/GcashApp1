# GcashApp1

A command-line banking application built in Java for a school project. This app simulates basic GCash-like functionality including user login and balance inquiry, with a focus on modular design, reproducibility, and auditability.

---

## Features

- User Login: Authenticates users via JDBC connection to a PostgreSQL database.
- Balance Inquiry: Displays current user balance after successful login.
- Modular Structure: Organized for future expansion (e.g., send money, transaction history).
- Database Integration: Uses relational tables with foreign key constraints for integrity.

---

## Technologies Used

| Tool/Tech     | Purpose                          |
|---------------|----------------------------------|
| Java          | Core application logic           |
| JDBC          | Database connectivity            |
| PostgreSQL    | User and balance data storage    |
| IntelliJ IDEA | Development environment          |
| Git & GitHub  | Version control and audit trail  |
| PowerShell    | CLI testing and script execution |

---

## How to Run

1. Clone the repo  
   git clone https://github.com/Vergel1231/GcashApp1.git
   cd GcashApp1
2. Set up PostgreSQL schema
   Create users and balance tables. Ensure foreign key constraints are in place.
3. Compile and run the app
   Use IntelliJ or CLI:
   javac Main.java
   java Main
4. Login and view balance
   Follow CLI prompts to authenticate and retrieve balance.

---

## Milestone Tags

| Tag                    | Description
|------------------------|------------------------------|
| v0.1-login-flow        | Initial login implementation |
| v0.2-balance-inquiry   | Balance feature added        |
| v0.3-schema-update     | DB schema changes            |

---

## Notes

- This project is part of a school requirement and will be expanded later for portfolio use.
- All code and documentation are written with future auditability in mind.
- Contributions are currently disabled until project completion.

---

## Transaction Table

The `Transaction` table records all monetary actions within the system. It is designed for simplicity and educational clarity, without enforcing foreign key constraints.

| Column           | Type             | Description                                  |
|------------------|------------------|----------------------------------------------|
| `ID`             | `SERIAL`         | Unique transaction identifier                |
| `amount`         | `NUMERIC(12,2)`  | Transaction amount                           |
| `name`           | `VARCHAR(100)`   | Description or label for the transaction     |
| `account_ID`     | `INT`            | ID of the account initiating the transaction |
| `date`           | `TIMESTAMP`      | Timestamp of transaction (auto-generated)    |
| `transferToID`   | `INT`            | Destination account ID (if applicable)       |
| `transferFromID` | `INT`            | Source account ID (if applicable)            |

> Note: Foreign key constraints were intentionally omitted to simplify the schema for academic purposes.

---

### CashIn GUI Flow Validated
- Resolved `account_number` to `user_id` via JDBC
- Enforced unique balance row per user
- Updated balance and logged transaction
- GUI confirmed success with real-time feedback

---

## Cash Transfer Logic Validated

The `CashTransferGUI` flow has been fully tested and validated for both schema alignment and input enforcement:

- **Receiver Validation**: GUI rejects nonexistent `account_number` with clear error messaging.
- **Amount Validation**: Rejects empty, non-numeric, or negative amounts via GUI-side checks.
- **Transaction Logging**: Successful transfers are recorded in the `Transaction` table with accurate `transferFromID` and `transferToID` mappings.
- **Balance Update**: Sender’s and receiver’s balances are updated atomically, preserving one-to-one mapping in the `Balance` table.
- **Audit Trail**: All logic paths are documented and milestone-tagged for reproducibility.

> Milestone tagged as `v0.4-cash-transfer-validation`

---
## Transaction Module Completed

Implemented `viewAll`, `viewUserAll`, and `viewTransaction` methods in `Transactions.java`. Integrated with `TransactionViewerGUI` for GUI-based testing. JDBC queries validated via PostgreSQL. Milestone tagged as `v0.5-transaction-viewer`.

---

## Author

Ver
Focused on reproducible workflows, CLI-first development, and modular banking logic.
Committed to clarity, control, and continuous improvement.
