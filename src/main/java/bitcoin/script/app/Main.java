package bitcoin.script.app;

import bitcoin.script.core.ScriptValidator;
import bitcoin.script.model.Script;

/**
 * CLI entry point for the Bitcoin Script interpreter (Phase 2 demo).
 *
 * <p>Accepts an optional {@code --trace} flag that prints the stack state
 * after every instruction.  Runs four demonstration scripts:
 * <ol>
 *   <li>P2PKH valid</li>
 *   <li>P2PKH invalid (wrong pubKeyHash)</li>
 *   <li>OP_IF / OP_ELSE / OP_ENDIF conditional</li>
 *   <li>OP_CHECKMULTISIG 2-of-3 multisig</li>
 * </ol>
 */
public class Main {

    public static void main(String[] args) {
        boolean traceEnabled = false;
        for (String arg : args) {
            if ("--trace".equals(arg)) {
                traceEnabled = true;
                break;
            }
        }

        ScriptValidator validator = new ScriptValidator(traceEnabled);

        // Demo 1 – P2PKH valid
        runDemo(validator,
                "Demo 1 – P2PKH valid",
                "SIG_OK PUBKEY_ABC",
                "OP_DUP OP_HASH160 PUBKEYHASH_ABC OP_EQUALVERIFY OP_CHECKSIG");

        // Demo 2 – P2PKH invalid (wrong pubKeyHash)
        runDemo(validator,
                "Demo 2 – P2PKH invalid (wrong hash)",
                "SIG_OK PUBKEY_ABC",
                "OP_DUP OP_HASH160 WRONG_HASH OP_EQUALVERIFY OP_CHECKSIG");

        // Demo 3 – OP_IF / OP_ELSE / OP_ENDIF
        runDemo(validator,
                "Demo 3 – OP_IF/OP_ELSE/OP_ENDIF (false branch takes ELSE)",
                "OP_0",
                "OP_IF OP_0 OP_ELSE OP_1 OP_ENDIF");

        // Demo 4 – Multisig 2-of-3
        runDemo(validator,
                "Demo 4 – OP_CHECKMULTISIG 2-of-3 (valid)",
                "OP_0 SIG_OK SIG_OK",
                "OP_2 PUBKEY_A PUBKEY_B PUBKEY_C OP_3 OP_CHECKMULTISIG");
    }

    /**
     * Runs a single demo and prints its label, scripts, and result.
     *
     * @param validator      the shared validator instance
     * @param label          human-readable description of the demo
     * @param scriptSigText  the unlocking script as a space-delimited string
     * @param scriptPubKeyText the locking script as a space-delimited string
     */
    private static void runDemo(ScriptValidator validator,
                                String label,
                                String scriptSigText,
                                String scriptPubKeyText) {
        System.out.println("=== " + label + " ===");
        System.out.println("  scriptSig:    " + scriptSigText);
        System.out.println("  scriptPubKey: " + scriptPubKeyText);

        boolean result = validator.validate(
                Script.parse(scriptSigText),
                Script.parse(scriptPubKeyText));

        System.out.println("  Result: " + (result ? "VALID" : "INVALID"));
        System.out.println();
    }
}
