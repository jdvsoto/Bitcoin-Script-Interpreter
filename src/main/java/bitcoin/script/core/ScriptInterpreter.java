package bitcoin.script.core;

import java.util.List;

import bitcoin.script.model.Script;
import bitcoin.script.model.ScriptElement;

public class ScriptInterpreter {

    private final InterpreterContext ctx;

    /**
     * Creates an interpreter that uses the given context for all executions.
     *
     * @param ctx the shared interpreter context (stack + factory + trace flag)
     */
    public ScriptInterpreter(InterpreterContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Executes all elements of {@code script} in order against the shared context.
     *
     * @param script the script to execute
     * @throws ScriptException if any operation fails during execution
     */
    public void execute(Script script) {
        for (ScriptElement element : script.getElements()) {
            element.execute(ctx);
            if (ctx.isTraceEnabled()) {
                printTrace(element.getToken());
            }
        }
    }

    // Prints the current trace line: token name + stack snapshot (bottom → top).
    private void printTrace(String token) {
        List<String> snapshot = ctx.getStack().snapshot(); // bottom-first
        System.out.println("[TRACE] token=" + token + "  stack=" + snapshot);
    }
}
