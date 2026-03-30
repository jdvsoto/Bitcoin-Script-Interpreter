package bitcoin.script.ops;

import bitcoin.script.core.InterpreterContext;

public class OpElse implements Operation {
    
    /**
     * Executes OP_ELSE logic.
     * 
     * @param ctx the interpreter context containing the stack and execution state
     */
    @Override
    public void apply(InterpreterContext ctx) {
        ctx.toggleExecution();
    }
}
