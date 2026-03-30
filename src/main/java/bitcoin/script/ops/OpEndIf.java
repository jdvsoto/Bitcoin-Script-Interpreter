package bitcoin.script.ops;

import bitcoin.script.core.InterpreterContext;

public class OpEndIf implements Operation {
    
    /**
     * Executes OP_ENDIF logic.
     * 
     * @param ctx the interpreter context containing the stack and execution state
     */
    @Override
    public void apply(InterpreterContext ctx) {
        ctx.popExecution();
    }
}
