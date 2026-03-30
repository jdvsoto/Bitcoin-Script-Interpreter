package bitcoin.script.model;

import bitcoin.script.core.InterpreterContext;
import bitcoin.script.core.ScriptException;
import bitcoin.script.ops.Operation;

public class OpElement implements ScriptElement {

    private final String opcodeName;

    /**
     * Creates an opcode element for the given opcode name.
     *
     * @param opcodeName the opcode string (e.g. {@code "OP_DUP"})
     */
    public OpElement(String opcodeName) {
        this.opcodeName = opcodeName;
    }

    /**
     * Looks up the operation in the factory and applies it to the context.
     *
     * @param ctx the current interpreter context
     * @throws ScriptException if the opcode is unknown or the operation fails
     */
    @Override
    public void execute(InterpreterContext ctx) {
        Operation op = ctx.getFactory().get(opcodeName);
        if (op == null) {
            throw new ScriptException("Unknown opcode: " + opcodeName);
        }
        op.apply(ctx);
    }

    /** Returns the opcode name (used in trace output). */
    @Override
    public String getToken() {
        return opcodeName;
    }
}
