package bitcoin.script.core;

import bitcoin.script.ops.OperationFactory;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Holds all shared state for a single script execution:
 * the main data stack, the operation factory, the trace flag,
 * and the execution stack used to handle OP_IF / OP_ELSE / OP_ENDIF branching.
 */
public class InterpreterContext {

    private final Stack stack;
    private final OperationFactory factory;
    private final boolean traceEnabled;

    /**
     * Tracks conditional-branch state for nested OP_IF blocks.
     * Each entry represents one open IF block: {@code true} = currently executing,
     * {@code false} = currently skipping.
     */
    private final Deque<Boolean> executionStack = new ArrayDeque<>();

    /**
     * Creates a new context with the given stack, factory, and trace setting.
     *
     * @param stack        the shared data stack
     * @param factory      the operation factory for opcode lookup
     * @param traceEnabled whether to print trace output after each instruction
     */
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

    /**
     * Returns {@code true} if the interpreter should currently execute instructions.
     * This is the case when every open IF block is in its active (true) branch,
     * i.e. the execution stack is empty or all entries are {@code true}.
     *
     * @return {@code true} if execution is active
     */
    public boolean shouldExecute() {
        for (boolean active : executionStack) {
            if (!active) return false;
        }
        return true;
    }

    /**
     * Opens a new conditional scope (called by OP_IF / OP_NOTIF).
     *
     * @param active {@code true} if the new scope should execute, {@code false} to skip it
     */
    public void pushExecution(boolean active) {
        executionStack.push(active);
    }

    /**
     * Closes the innermost conditional scope (called by OP_ENDIF).
     *
     * @return the value that was popped
     * @throws ScriptException if there is no open conditional scope to close
     */
    public boolean popExecution() {
        if (executionStack.isEmpty()) {
            throw new ScriptException("OP_ENDIF without matching OP_IF");
        }
        return executionStack.pop();
    }

    /**
     * Toggles the innermost conditional scope between executing and skipping (called by OP_ELSE).
     *
     * @throws ScriptException if there is no open conditional scope to toggle
     */
    public void toggleExecution() {
        if (executionStack.isEmpty()) {
            throw new ScriptException("OP_ELSE without matching OP_IF");
        }
        boolean current = executionStack.pop();
        executionStack.push(!current);
    }
}
