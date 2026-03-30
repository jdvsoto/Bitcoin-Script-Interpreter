package bitcoin.script.ops;

import bitcoin.script.core.InterpreterContext;
import bitcoin.script.core.ScriptException;
import bitcoin.script.core.Stack;

public class OpIf implements Operation {
    
    /**
     * Executes OP_IF logic.
     * 
     * @param ctx the interpreter context containing the stack and execution state
     */
    @Override
    public void apply(InterpreterContext ctx) {
        if (ctx.shouldExecute()) {
            if (ctx.getStack().isEmpty()) {
                throw new ScriptException("OP_IF requires a value on the stack");
            }

            String value = ctx.getStack().pop();
            boolean condition = Stack.isTruthy(value);
            ctx.pushExecution(condition);
        } else {
            // Nested inside a skipped block
            ctx.pushExecution(false);
        }
    }
    
}
