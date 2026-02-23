package bitcoin.script.ops;

import bitcoin.script.core.InterpreterContext;

/**
 * {@code OP_DUP} – duplicates the top stack element.
 *
 * <p>Pops nothing; peeks at the top value and pushes a copy of it.
 * Throws {@link bitcoin.script.core.ScriptException} if the stack is empty.
 */
public class OpDup implements Operation {

    @Override
    public void apply(InterpreterContext ctx) {
        String top = ctx.getStack().peek();
        ctx.getStack().push(top);
    }
}
