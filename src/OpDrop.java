package bitcoin.script.ops;

import bitcoin.script.core.InterpreterContext;

/**
 * {@code OP_DROP} – removes the top stack element and discards it.
 *
 * <p>Throws {@link bitcoin.script.core.ScriptException} if the stack is empty.
 */
public class OpDrop implements Operation {

    @Override
    public void apply(InterpreterContext ctx) {
        ctx.getStack().pop();
    }
}
