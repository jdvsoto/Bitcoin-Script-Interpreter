# Bitcoin Script Interpreter – Phase 1

Didactic interpreter for a subset of Bitcoin Script.
**Course:** Estructura de Datos – Universidad del Valle de Guatemala (UVG)

---

## How to Compile and Run Tests

```bash
# Compile and run all JUnit 5 tests
mvn clean test

# Compile and package (no tests)
mvn clean package -DskipTests
```

---

## How to Run the CLI Demo

```bash
# Run the P2PKH demo (no trace)
mvn exec:java

# Run the P2PKH demo with per-instruction stack trace
mvn exec:java -Dexec.args="--trace"
```

---

## Script Input Format

Scripts are space-delimited token strings evaluated left-to-right.

| Token type | Rule | Example |
|---|---|---|
| **Opcode** | Starts with `OP_` | `OP_DUP`, `OP_HASH160` |
| **Data push** | Does **not** start with `OP_` | `SIG_OK`, `PUBKEY_ABC` |

---

## Supported Opcodes (Phase 1 only)

| Opcode | Description |
|---|---|
| `OP_0` / `OP_FALSE` | Push `"0"` (falsy) |
| `OP_1` … `OP_16` | Push numeric string `"1"` … `"16"` |
| `OP_DUP` | Duplicate top stack element |
| `OP_DROP` | Discard top stack element |
| `OP_EQUAL` | Pop two elements; push `"1"` if equal, `"0"` otherwise |
| `OP_EQUALVERIFY` | Pop two elements; abort with `ScriptException` if not equal |
| `OP_HASH160` | Pop top; push `CryptoMock.hash160(top)` |
| `OP_CHECKSIG` | Pop pubKey and signature; push `"1"` or `"0"` via mock |

---

## CryptoMock Rules

### `hash160(data)`

Strips the `"PUBKEY_"` prefix (if present) and prepends `"PUBKEYHASH_"`:

```
hash160("PUBKEY_ABC")  →  "PUBKEYHASH_ABC"
hash160("PUBKEY_XYZ")  →  "PUBKEYHASH_XYZ"
hash160("SOMEDATA")    →  "PUBKEYHASH_SOMEDATA"
```

### `checkSig(signature, pubKey)`

Returns `true` if and only if `signature` equals `"SIG_OK"` (case-sensitive).
The `pubKey` argument is ignored in the mock.

---

## P2PKH Example

```
scriptSig:    SIG_OK PUBKEY_ABC
scriptPubKey: OP_DUP OP_HASH160 PUBKEYHASH_ABC OP_EQUALVERIFY OP_CHECKSIG
```

**Step-by-step execution:**

| Step | Token | Stack (bottom → top) |
|---|---|---|
| 1 | `SIG_OK` (push) | `[SIG_OK]` |
| 2 | `PUBKEY_ABC` (push) | `[SIG_OK, PUBKEY_ABC]` |
| 3 | `OP_DUP` | `[SIG_OK, PUBKEY_ABC, PUBKEY_ABC]` |
| 4 | `OP_HASH160` | `[SIG_OK, PUBKEY_ABC, PUBKEYHASH_ABC]` |
| 5 | `PUBKEYHASH_ABC` (push) | `[SIG_OK, PUBKEY_ABC, PUBKEYHASH_ABC, PUBKEYHASH_ABC]` |
| 6 | `OP_EQUALVERIFY` | `[SIG_OK, PUBKEY_ABC]` *(equal → ok)* |
| 7 | `OP_CHECKSIG` | `[1]` *(SIG_OK → true)* |

Final top = `"1"` → **VALID**

---

## Trace Output Example

```
[TRACE] token=SIG_OK            stack=[SIG_OK]
[TRACE] token=PUBKEY_ABC        stack=[SIG_OK, PUBKEY_ABC]
[TRACE] token=OP_DUP            stack=[SIG_OK, PUBKEY_ABC, PUBKEY_ABC]
[TRACE] token=OP_HASH160        stack=[SIG_OK, PUBKEY_ABC, PUBKEYHASH_ABC]
[TRACE] token=PUBKEYHASH_ABC    stack=[SIG_OK, PUBKEY_ABC, PUBKEYHASH_ABC, PUBKEYHASH_ABC]
[TRACE] token=OP_EQUALVERIFY    stack=[SIG_OK, PUBKEY_ABC]
[TRACE] token=OP_CHECKSIG       stack=[1]

Result: VALID
```

---

## Project Structure

```
bitcoin-script-interpreter/
├── pom.xml
└── src/
    ├── main/java/gt/edu/uvg/bitcoin/script/
    │   ├── app/
    │   │   └── Main.java
    │   ├── core/
    │   │   ├── InterpreterContext.java
    │   │   ├── ScriptException.java
    │   │   ├── ScriptInterpreter.java
    │   │   ├── ScriptValidator.java
    │   │   └── Stack.java
    │   ├── crypto/
    │   │   └── CryptoMock.java
    │   ├── model/
    │   │   ├── DataElement.java
    │   │   ├── OpElement.java
    │   │   ├── Script.java
    │   │   └── ScriptElement.java
    │   └── ops/
    │       ├── OpCheckSig.java
    │       ├── OpDrop.java
    │       ├── OpDup.java
    │       ├── OpEqual.java
    │       ├── OpEqualVerify.java
    │       ├── OpHash160.java
    │       ├── OpPushNumber.java
    │       ├── Operation.java
    │       └── OperationFactory.java
    └── test/java/gt/edu/uvg/bitcoin/script/core/
        └── ScriptValidatorTest.java
```
