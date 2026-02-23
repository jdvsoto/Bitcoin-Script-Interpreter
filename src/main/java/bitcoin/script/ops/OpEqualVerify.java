package bitcoin.script.ops;

import bitcoin.script.core.InterpreterContext;
import bitcoin.script.core.ScriptException;

/**
 * {@code OP_EQUALVERIFY} – pops two elements and throws {@link ScriptException}
 * if they differ.  Pushes nothing on success.
 *
 * <p>Equivalent to {@code OP_EQUAL OP_VERIFY}: commonly used in P2PKH scripts
 * to assert that the computed public-key hash matches the expected hash.
 */
public class OpEqualVerify implements Operation {

    @Override
    public void apply(InterpreterContext ctx) {
        String b = ctx.getStack().pop();
        String a = ctx.getStack().pop();
        if (!a.equals(b)) {
            throw new ScriptException(
                    "OP_EQUALVERIFY failed: expected '" + a + "' but got '" + b + "'");
        }
    }
}
