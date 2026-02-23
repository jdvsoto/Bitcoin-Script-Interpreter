package bitcoin.script.ops;

import bitcoin.script.core.InterpreterContext;
import bitcoin.script.crypto.CryptoMock;

/**
 * {@code OP_CHECKSIG} – pops the public key (top) and the signature (second),
 * verifies them with the mock {@link CryptoMock#checkSig(String, String)}, and
 * pushes {@code "1"} (valid) or {@code "0"} (invalid) onto the stack.
 *
 * <p>In real Bitcoin this validates a DER-encoded ECDSA signature against the
 * serialized transaction hash.  Here {@link CryptoMock} is used instead.
 */
public class OpCheckSig implements Operation {

    private final CryptoMock crypto;

    /**
     * Creates the operation with the given crypto mock.
     *
     * @param crypto the mock crypto provider
     */
    public OpCheckSig(CryptoMock crypto) {
        this.crypto = crypto;
    }

    @Override
    public void apply(InterpreterContext ctx) {
        String pubKey    = ctx.getStack().pop();   // top element
        String signature = ctx.getStack().pop();   // second element
        boolean valid    = crypto.checkSig(signature, pubKey);
        ctx.getStack().push(valid ? "1" : "0");
    }
}
