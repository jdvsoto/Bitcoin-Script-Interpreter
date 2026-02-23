package bitcoin.script.ops;

import bitcoin.script.core.InterpreterContext;
import bitcoin.script.crypto.CryptoMock;

/**
 * {@code OP_HASH160} – pops the top element, passes it through the mock
 * {@link CryptoMock#hash160(String)} function, and pushes the result.
 *
 * <p>In real Bitcoin this performs SHA-256 followed by RIPEMD-160.  Here
 * {@link CryptoMock} provides a deterministic, non-cryptographic substitute.
 */
public class OpHash160 implements Operation {

    private final CryptoMock crypto;

    /**
     * Creates the operation with the given crypto mock.
     *
     * @param crypto the mock crypto provider
     */
    public OpHash160(CryptoMock crypto) {
        this.crypto = crypto;
    }

    @Override
    public void apply(InterpreterContext ctx) {
        String data = ctx.getStack().pop();
        String hash = crypto.hash160(data);
        ctx.getStack().push(hash);
    }
}
