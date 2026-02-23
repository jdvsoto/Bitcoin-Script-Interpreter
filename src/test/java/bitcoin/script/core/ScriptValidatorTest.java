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
}
