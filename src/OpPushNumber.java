package bitcoin.script.ops;

import bitcoin.script.core.InterpreterContext;

/**
 * Pushes a fixed numeric string onto the stack.
 *
 * <p>Used by {@code OP_0} / {@code OP_FALSE} (pushes {@code "0"}) and
 * {@code OP_1} through {@code OP_16} (push {@code "1"} … {@code "16"}).
 */
public class OpPushNumber implements Operation {

    private final String numericValue;

    /**
     * Creates an operation that pushes the given numeric string.
     *
     * @param numericValue the value to push (e.g. {@code "0"}, {@code "1"})
     */
    public OpPushNumber(String numericValue) {
        this.numericValue = numericValue;
    }

    @Override
    public void apply(InterpreterContext ctx) {
        ctx.getStack().push(numericValue);
    }
}
