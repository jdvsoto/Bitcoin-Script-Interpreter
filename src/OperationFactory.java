package bitcoin.script.ops;

import bitcoin.script.crypto.CryptoMock;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry of all Bitcoin Script operations supported in Phase 1.
 *
 * <p>Operations are stored in a {@code Map<String, Operation>} keyed by opcode
 * name (e.g. {@code "OP_DUP"}).  No enums are used for opcodes.
 *
 * <p>Registered opcodes:
 * <ul>
 *   <li>{@code OP_0} / {@code OP_FALSE} – push {@code "0"}</li>
 *   <li>{@code OP_1} … {@code OP_16}   – push {@code "1"} … {@code "16"}</li>
 *   <li>{@code OP_DUP}         – duplicate top element</li>
 *   <li>{@code OP_DROP}        – discard top element</li>
 *   <li>{@code OP_EQUAL}       – equality check, push result</li>
 *   <li>{@code OP_EQUALVERIFY} – equality check, abort on failure</li>
 *   <li>{@code OP_HASH160}     – mock hash of top element</li>
 *   <li>{@code OP_CHECKSIG}    – mock signature verification</li>
 * </ul>
 */
public class OperationFactory {

    private final Map<String, Operation> registry = new HashMap<>();

    /**
     * Creates a factory pre-loaded with all Phase 1 operations.
     */
    public OperationFactory() {
        CryptoMock crypto = new CryptoMock();

        // Numeric push opcodes
        register("OP_0",     new OpPushNumber("0"));
        register("OP_FALSE", new OpPushNumber("0"));
        for (int i = 1; i <= 16; i++) {
            register("OP_" + i, new OpPushNumber(String.valueOf(i)));
        }

        // Stack operations
        register("OP_DUP",          new OpDup());
        register("OP_DROP",         new OpDrop());

        // Comparison operations
        register("OP_EQUAL",        new OpEqual());
        register("OP_EQUALVERIFY",  new OpEqualVerify());

        // Hash and signature operations (mock implementations)
        register("OP_HASH160",      new OpHash160(crypto));
        register("OP_CHECKSIG",     new OpCheckSig(crypto));
    }

    /**
     * Registers an operation under the given opcode name.
     *
     * @param opcodeName the opcode string key (e.g. {@code "OP_DUP"})
     * @param op         the operation implementation
     */
    public void register(String opcodeName, Operation op) {
        registry.put(opcodeName, op);
    }

    /**
     * Looks up the operation for the given opcode name.
     *
     * @param opcodeName the opcode name to look up
     * @return the {@link Operation}, or {@code null} if not registered
     */
    public Operation get(String opcodeName) {
        return registry.get(opcodeName);
    }
}
