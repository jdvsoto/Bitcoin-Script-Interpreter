package bitcoin.script.crypto;

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
