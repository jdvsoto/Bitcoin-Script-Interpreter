package bitcoin.script.core;

import bitcoin.script.model.Script;
import bitcoin.script.ops.OperationFactory;

public class ScriptValidator {

    private final boolean traceEnabled;

    /**
     * Creates a validator.
     *
     * @param traceEnabled {@code true} to print the stack after each instruction
     */
    public ScriptValidator(boolean traceEnabled) {
        this.traceEnabled = traceEnabled;
    }

    /**
     * Validates the combination of {@code scriptSig} (unlocking) and
     * {@code scriptPubKey} (locking) scripts.
     *
     * @param scriptSig    the unlocking script (provided by the spender)
     * @param scriptPubKey the locking script (embedded in the UTXO)
     * @return {@code true} if execution succeeds and the final stack top is truthy
     */
    public boolean validate(Script scriptSig, Script scriptPubKey) {
        Stack stack              = new Stack();
        OperationFactory factory = new OperationFactory();
        InterpreterContext ctx   = new InterpreterContext(stack, factory, traceEnabled);
        ScriptInterpreter interpreter = new ScriptInterpreter(ctx);

        try {
            interpreter.execute(scriptSig);
            interpreter.execute(scriptPubKey);
        } catch (ScriptException e) {
            System.err.println("[INVALID] Script execution failed: " + e.getMessage());
            return false;
        }

        if (stack.isEmpty()) {
            System.err.println("[INVALID] Stack is empty after execution");
            return false;
        }

        String top = stack.peek();
        return Stack.isTruthy(top);
    }
}
