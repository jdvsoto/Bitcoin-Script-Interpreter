package bitcoin.script.core;

import bitcoin.script.ops.OperationFactory;

public class InterpreterContext {

    private final Stack stack;
    private final OperationFactory factory;
    private final boolean traceEnabled;

    
    public InterpreterContext(Stack stack, OperationFactory factory, boolean traceEnabled) {
        this.stack        = stack;
        this.factory      = factory;
        this.traceEnabled = traceEnabled;
    }

    /** Returns the shared script stack. */
    public Stack getStack() {
        return stack;
    }

    /** Returns the operation factory used to resolve opcode names. */
    public OperationFactory getFactory() {
        return factory;
    }

    /** Returns {@code true} if trace output is enabled. */
    public boolean isTraceEnabled() {
        return traceEnabled;
    }
}
