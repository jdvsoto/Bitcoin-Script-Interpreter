package bitcoin.script.core;

import java.util.List;
import java.util.Set;

import bitcoin.script.model.Script;
import bitcoin.script.model.ScriptElement;

public class ScriptInterpreter {

    private final InterpreterContext ctx;

    /**
     * Opcodes that control the execution flow (IF/ELSE/ENDIF).
     * These are always executed regardless of the current branch state,
     * so that nested scopes are tracked correctly.
     */
    private static final Set<String> FLOW_OPS =
            Set.of("OP_IF", "OP_NOTIF", "OP_ELSE", "OP_ENDIF");

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
     * Instructions inside a false branch are skipped, except for flow-control
     * opcodes which are always processed to maintain correct nesting depth.
     *
     * @param script the script to execute
     * @throws ScriptException if any operation fails during execution
     */
    public void execute(Script script) {
        for (ScriptElement element : script.getElements()) {
            boolean skip = !ctx.shouldExecute() && !FLOW_OPS.contains(element.getToken());
            if (!skip) {
                element.execute(ctx);
            }
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
