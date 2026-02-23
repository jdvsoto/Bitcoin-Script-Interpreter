package bitcoin.script.model;

import bitcoin.script.core.InterpreterContext;

/**
 * A data-push element: pushes its string data directly onto the stack.
 *
 * <p>In a parsed script, any token that does <em>not</em> start with
 * {@code "OP_"} is treated as a {@code DataElement}.  Examples include
 * signatures ({@code "SIG_OK"}), public keys ({@code "PUBKEY_ABC"}), and
 * public-key hashes ({@code "PUBKEYHASH_ABC"}).
 */
public class DataElement implements ScriptElement {

    private final String data;

    /**
     * Creates a data-push element for the given string value.
     *
     * @param data the value to push onto the stack when this element is executed
     */
    public DataElement(String data) {
        this.data = data;
    }

    /**
     * Pushes {@link #data} onto the stack.
     *
     * @param ctx the current interpreter context
     */
    @Override
    public void execute(InterpreterContext ctx) {
        ctx.getStack().push(data);
    }

    /** Returns the data value (used in trace output). */
    @Override
    public String getToken() {
        return data;
    }
}
