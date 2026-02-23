package bitcoin.script.core;

public class ScriptException extends RuntimeException {

    /**
     * Constructs a new {@code ScriptException} with the given detail message.
     *
     * @param message human-readable description of the failure
     */
    public ScriptException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code ScriptException} with a message and a cause.
     *
     * @param message human-readable description of the failure
     * @param cause   the underlying exception that triggered this failure
     */
    public ScriptException(String message, Throwable cause) {
        super(message, cause);
    }
}
