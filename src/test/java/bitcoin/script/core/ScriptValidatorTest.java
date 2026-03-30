package bitcoin.script.core;

import bitcoin.script.model.Script;
import bitcoin.script.ops.OperationFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/** Unit tests for {@link ScriptValidator}. */
class ScriptValidatorTest {

    // Test 1 – P2PKH valid

    @Test
    @DisplayName("P2PKH valid: correct signature and pubKeyHash → VALID")
    void testP2PKHValid() {
        // CryptoMock: hash160("PUBKEY_ABC") = "PUBKEYHASH_ABC"
        //             checkSig("SIG_OK", "PUBKEY_ABC") = true
        Script scriptSig    = Script.parse("SIG_OK PUBKEY_ABC");
        Script scriptPubKey = Script.parse(
                "OP_DUP OP_HASH160 PUBKEYHASH_ABC OP_EQUALVERIFY OP_CHECKSIG");

        assertTrue(new ScriptValidator(false).validate(scriptSig, scriptPubKey),
                "P2PKH with correct credentials should return VALID");
    }

    // Test 2 – P2PKH invalid: pubKeyHash mismatch

    @Test
    @DisplayName("P2PKH invalid: wrong pubKeyHash causes OP_EQUALVERIFY to fail → INVALID")
    void testP2PKHInvalidHashMismatch() {
        // "WRONG_HASH" ≠ hash160("PUBKEY_ABC") = "PUBKEYHASH_ABC"
        Script scriptSig    = Script.parse("SIG_OK PUBKEY_ABC");
        Script scriptPubKey = Script.parse(
                "OP_DUP OP_HASH160 WRONG_HASH OP_EQUALVERIFY OP_CHECKSIG");

        assertFalse(new ScriptValidator(false).validate(scriptSig, scriptPubKey),
                "Mismatched pubKeyHash should return INVALID");
    }

    // Test 3 – Stack underflow: OP_DUP on empty stack

    @Test
    @DisplayName("Stack underflow: OP_DUP on empty stack throws ScriptException")
    void testStackUnderflowOnOpDup() {
        Script script = Script.parse("OP_DUP");

        Stack stack              = new Stack();
        OperationFactory factory = new OperationFactory();
        InterpreterContext ctx   = new InterpreterContext(stack, factory, false);
        ScriptInterpreter interpreter = new ScriptInterpreter(ctx);

        assertThrows(ScriptException.class, () -> interpreter.execute(script),
                "OP_DUP on an empty stack must throw ScriptException");
    }

    // Test 4 – OP_CHECKSIG with invalid signature

    @Test
    @DisplayName("OP_CHECKSIG invalid: wrong signature token → INVALID")
    void testCheckSigInvalidSignature() {
        // "BAD_SIG" ≠ "SIG_OK" → checkSig returns false → pushes "0" → INVALID
        Script scriptSig    = Script.parse("BAD_SIG PUBKEY_ABC");
        Script scriptPubKey = Script.parse(
                "OP_DUP OP_HASH160 PUBKEYHASH_ABC OP_EQUALVERIFY OP_CHECKSIG");

        assertFalse(new ScriptValidator(false).validate(scriptSig, scriptPubKey),
                "Invalid signature should return INVALID");
    }

    // ── Phase 2: conditional flow ────────────────────────────────────────────

    // Test 5 – OP_IF true branch

    @Test
    @DisplayName("OP_IF true branch: truthy condition executes IF body → VALID")
    void testOpIfTrueBranch() {
        // OP_1 is truthy → enters IF block → pushes 1 → VALID
        Script scriptSig    = Script.parse("OP_1");
        Script scriptPubKey = Script.parse("OP_IF OP_1 OP_ENDIF");

        assertTrue(new ScriptValidator(false).validate(scriptSig, scriptPubKey),
                "Truthy condition should execute IF body and leave 1 on stack");
    }

    // Test 6 – OP_IF false branch (body skipped)

    @Test
    @DisplayName("OP_IF false branch: falsy condition skips IF body → INVALID")
    void testOpIfFalseBranch() {
        // OP_0 is falsy → IF body skipped → stack empty → INVALID
        Script scriptSig    = Script.parse("OP_0");
        Script scriptPubKey = Script.parse("OP_IF OP_1 OP_ENDIF");

        assertFalse(new ScriptValidator(false).validate(scriptSig, scriptPubKey),
                "Falsy condition should skip IF body, leaving nothing truthy on stack");
    }

    // Test 7 – OP_IF / OP_ELSE / OP_ENDIF

    @Test
    @DisplayName("OP_IF/OP_ELSE/OP_ENDIF: falsy condition executes ELSE body → VALID")
    void testOpIfElse() {
        // OP_0 → skips IF body → executes ELSE body (pushes 1) → VALID
        Script scriptSig    = Script.parse("OP_0");
        Script scriptPubKey = Script.parse("OP_IF OP_0 OP_ELSE OP_1 OP_ENDIF");

        assertTrue(new ScriptValidator(false).validate(scriptSig, scriptPubKey),
                "Falsy condition should take ELSE branch and leave 1 on stack");
    }

    // Test 8 – nested OP_IF

    @Test
    @DisplayName("Nested OP_IF: both conditions truthy → innermost body executes → VALID")
    void testNestedOpIf() {
        // Two OP_1s on stack; outer IF pops first, inner IF pops second → pushes 1 → VALID
        Script scriptSig    = Script.parse("OP_1 OP_1");
        Script scriptPubKey = Script.parse("OP_IF OP_IF OP_1 OP_ENDIF OP_ENDIF");

        assertTrue(new ScriptValidator(false).validate(scriptSig, scriptPubKey),
                "Both truthy conditions should reach innermost body and leave 1 on stack");
    }

    // Test 9 – OP_IF on empty stack

    @Test
    @DisplayName("OP_IF on empty stack: throws ScriptException (stack underflow)")
    void testOpIfEmptyStack() {
        // Empty scriptSig → OP_IF has nothing to pop → ScriptException propagates
        Script scriptSig    = Script.parse("");
        Script scriptPubKey = Script.parse("OP_IF OP_1 OP_ENDIF");

        Stack stack              = new Stack();
        OperationFactory factory = new OperationFactory();
        InterpreterContext ctx   = new InterpreterContext(stack, factory, false);
        ScriptInterpreter interpreter = new ScriptInterpreter(ctx);

        assertThrows(ScriptException.class,
                () -> interpreter.execute(scriptPubKey),
                "OP_IF with empty stack must throw ScriptException");
    }

    // ── Phase 2: multisig ───────────────────────────────────────────────────

    // Test 10 – OP_CHECKMULTISIG 2-of-3 valid

    @Test
    @DisplayName("OP_CHECKMULTISIG 2-of-3: two valid signatures → VALID")
    void testCheckMultiSig2of3Valid() {
        // dummy + SIG_OK + SIG_OK → 2 valid sigs out of 3 pubkeys → VALID
        Script scriptSig    = Script.parse("OP_0 SIG_OK SIG_OK");
        Script scriptPubKey = Script.parse(
                "OP_2 PUBKEY_A PUBKEY_B PUBKEY_C OP_3 OP_CHECKMULTISIG");

        assertTrue(new ScriptValidator(false).validate(scriptSig, scriptPubKey),
                "2-of-3 multisig with two valid signatures should return VALID");
    }

    // Test 11 – OP_CHECKMULTISIG 2-of-3 invalid

    @Test
    @DisplayName("OP_CHECKMULTISIG 2-of-3: only one valid signature → INVALID")
    void testCheckMultiSig2of3Invalid() {
        // dummy + BAD_SIG + SIG_OK → only 1 valid sig, need 2 → INVALID
        Script scriptSig    = Script.parse("OP_0 BAD_SIG SIG_OK");
        Script scriptPubKey = Script.parse(
                "OP_2 PUBKEY_A PUBKEY_B PUBKEY_C OP_3 OP_CHECKMULTISIG");

        assertFalse(new ScriptValidator(false).validate(scriptSig, scriptPubKey),
                "2-of-3 multisig with only one valid signature should return INVALID");
    }
}
