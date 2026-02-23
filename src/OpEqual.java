package bitcoin.script.ops;

import bitcoin.script.core.InterpreterContext;

/**
 * {@code OP_EQUAL} – pops two elements and pushes {@code "1"} if they are
 * equal, {@code "0"} otherwise.
 *
 * <p>Does <em>not</em> abort execution on inequality; use
 * {@link OpEqualVerify} when a mismatch should terminate the script.
 */
public class OpEqual implements Operation {

    @Override
    public void apply(InterpreterContext ctx) {
        String b = ctx.getStack().pop();
        String a = ctx.getStack().pop();
        ctx.getStack().push(a.equals(b) ? "1" : "0");
    }
}
