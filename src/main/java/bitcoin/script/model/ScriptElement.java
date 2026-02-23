package bitcoin.script.model;

import bitcoin.script.core.InterpreterContext;
import bitcoin.script.core.ScriptException;

public interface ScriptElement {

    /**
     * Executes this element against the given interpreter context.
     *
     * @param ctx the current interpreter context (stack, factory, trace flag)
     * @throws ScriptException if execution of this element fails
     */
    void execute(InterpreterContext ctx);

    /**
     * Returns the string token that identifies this element (used in trace output).
     *
     * @return the opcode name (e.g. {@code "OP_DUP"}) or data value
     */
    String getToken();
}
