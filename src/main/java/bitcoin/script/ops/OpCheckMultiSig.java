package bitcoin.script.ops;

import bitcoin.script.core.InterpreterContext;
import bitcoin.script.core.ScriptException;
import bitcoin.script.core.Stack;
import bitcoin.script.crypto.CryptoMock;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code OP_CHECKMULTISIG} – verifies that at least M of N provided signatures
 * are valid against the provided public keys, using a simulated 2-of-3
 * multisignature scheme.
 *
 * <p>Expected stack layout (top to bottom) when this opcode executes:
 * <ol>
 *   <li>N – number of public keys (e.g. {@code "3"})</li>
 *   <li>pubkey_1 … pubkey_N – the public keys</li>
 *   <li>M – number of required valid signatures (e.g. {@code "2"})</li>
 *   <li>sig_1 … sig_M – the signatures</li>
 *   <li>dummy – one extra element (Bitcoin protocol quirk; discarded)</li>
 * </ol>
 *
 * <p>Pushes {@code "1"} if at least M signatures are valid, {@code "0"} otherwise.
 * A signature is considered valid if {@link CryptoMock#checkSig(String, String)}
 * returns {@code true} for it against any of the provided public keys.
 *
 * <p>Throws {@link ScriptException} on stack underflow or malformed numeric tokens.
 */
public class OpCheckMultiSig implements Operation {

    private final CryptoMock crypto;

    /**
     * Creates the operation with the given crypto mock.
     *
     * @param crypto the mock crypto provider
     */
    public OpCheckMultiSig(CryptoMock crypto) {
        this.crypto = crypto;
    }

    /**
     * Executes the multisig check against the current interpreter context.
     *
     * @param ctx the shared interpreter context
     * @throws ScriptException if the stack has insufficient elements or contains
     *                         non-numeric values where a count is expected
     */
    @Override
    public void apply(InterpreterContext ctx) {
        Stack stack = ctx.getStack();

        // Step 1: pop N (number of public keys)
        int n = popInt(stack, "OP_CHECKMULTISIG: expected pubkey count (N) on stack");

        // Step 2: pop N public keys
        List<String> pubKeys = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            pubKeys.add(popValue(stack, "OP_CHECKMULTISIG: expected pubkey #" + (i + 1)));
        }

        // Step 3: pop M (number of required signatures)
        int m = popInt(stack, "OP_CHECKMULTISIG: expected signature count (M) on stack");

        // Step 4: pop M signatures
        List<String> sigs = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            sigs.add(popValue(stack, "OP_CHECKMULTISIG: expected signature #" + (i + 1)));
        }

        // Step 5: pop and discard the dummy element (Bitcoin protocol quirk)
        popValue(stack, "OP_CHECKMULTISIG: expected dummy element on stack");

        // Step 6: count valid signatures
        int validCount = 0;
        for (String sig : sigs) {
            for (String pubKey : pubKeys) {
                if (crypto.checkSig(sig, pubKey)) {
                    validCount++;
                    break; // one pubkey match is enough for this signature
                }
            }
        }

        // Step 7: push result
        stack.push(validCount >= m ? "1" : "0");
    }

    /**
     * Pops a string value from the stack, throwing a descriptive
     * {@link ScriptException} on underflow.
     */
    private String popValue(Stack stack, String errorMsg) {
        if (stack.isEmpty()) {
            throw new ScriptException(errorMsg);
        }
        return stack.pop();
    }

    /**
     * Pops a string value from the stack and parses it as an integer,
     * throwing a descriptive {@link ScriptException} on underflow or parse error.
     */
    private int popInt(Stack stack, String errorMsg) {
        String raw = popValue(stack, errorMsg);
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            throw new ScriptException(errorMsg + " (got '" + raw + "')");
        }
    }
}
