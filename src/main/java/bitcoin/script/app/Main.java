package bitcoin.script.app;

import bitcoin.script.core.ScriptValidator;
import bitcoin.script.model.Script;

/**
 * CLI entry point for the Bitcoin Script interpreter (Phase 1 demo).
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

        // P2PKH demo script
        String scriptSigText    = "SIG_OK PUBKEY_ABC";
        String scriptPubKeyText = "OP_DUP OP_HASH160 PUBKEYHASH_ABC OP_EQUALVERIFY OP_CHECKSIG";

        Script scriptSig    = Script.parse(scriptSigText);
        Script scriptPubKey = Script.parse(scriptPubKeyText);

        ScriptValidator validator = new ScriptValidator(traceEnabled);
        boolean result = validator.validate(scriptSig, scriptPubKey);

        System.out.println();
        System.out.println("scriptSig:    " + scriptSigText);
        System.out.println("scriptPubKey: " + scriptPubKeyText);
        System.out.println("Result: " + (result ? "VALID" : "INVALID"));
    }
}
