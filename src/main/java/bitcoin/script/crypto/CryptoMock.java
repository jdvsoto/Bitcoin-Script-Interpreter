package bitcoin.script.crypto;

/**
 * Deterministic mock for cryptographic operations used in Bitcoin Script.
 *
 * <p><b>hash160 rule:</b> Strips any leading {@code "PUBKEY_"} prefix from the
 * input string and prepends {@code "PUBKEYHASH_"}. This gives a stable,
 * reproducible mapping without real cryptography. Examples:
 * <ul>
 *   <li>{@code hash160("PUBKEY_ABC")} &rarr; {@code "PUBKEYHASH_ABC"}</li>
 *   <li>{@code hash160("PUBKEY_XYZ")} &rarr; {@code "PUBKEYHASH_XYZ"}</li>
 *   <li>{@code hash160("SOMEDATA")}   &rarr; {@code "PUBKEYHASH_SOMEDATA"}</li>
 * </ul>
 *
 * <p><b>checkSig rule:</b> Returns {@code true} if and only if the signature
 * string equals {@code "SIG_OK"} (case-sensitive). The {@code pubKey} argument
 * is received but not verified in this mock.
 */
public class CryptoMock {

    private static final String PUBKEY_PREFIX     = "PUBKEY_";
    private static final String PUBKEYHASH_PREFIX = "PUBKEYHASH_";

    /**
     * Mock HASH160 operation.
     *
     * <p>Strips the {@code "PUBKEY_"} prefix (if present) from {@code data}
     * and prepends {@code "PUBKEYHASH_"}, producing a deterministic result.
     *
     * @param data the input string (represents raw bytes pushed onto the stack)
     * @return a deterministic mock hash string
     */
    public String hash160(String data) {
        String stripped = data.startsWith(PUBKEY_PREFIX)
                ? data.substring(PUBKEY_PREFIX.length())
                : data;
        return PUBKEYHASH_PREFIX + stripped;
    }

    /**
     * Mock signature verification.
     *
     * <p>Returns {@code true} iff {@code signature} equals {@code "SIG_OK"}.
     *
     * @param signature the signature string popped from the stack
     * @param pubKey    the public key string popped from the stack (not verified)
     * @return {@code true} if the mock signature is considered valid
     */
    public boolean checkSig(String signature, String pubKey) {
        return "SIG_OK".equals(signature);
    }
}
