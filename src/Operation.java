package bitcoin.script.ops;

import bitcoin.script.core.InterpreterContext;
import bitcoin.script.core.ScriptException;

/**
 * Functional interface representing a single Bitcoin Script operation.
 *
 * <p>Each implementation reads from and/or writes to the stack held in
 * {@link InterpreterContext}, throwing {@link ScriptException} on failure.
 */
@FunctionalInterface
public interface Operation {

    /**
     * Applies this operation to the interpreter context.
     *
     * @param ctx the current interpreter context (stack, factory, trace flag)
     * @throws ScriptException if the operation cannot be completed
     */
    void apply(InterpreterContext ctx);
}
