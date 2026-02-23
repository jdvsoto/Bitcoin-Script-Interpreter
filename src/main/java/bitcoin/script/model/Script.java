package bitcoin.script.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Script {

    private final List<ScriptElement> elements;

    /**
     * Creates a script from the given element list.
     *
     * @param elements the ordered list of script elements
     */
    public Script(List<ScriptElement> elements) {
        this.elements = Collections.unmodifiableList(new ArrayList<>(elements));
    }

    // Returns the ordered, immutable list of script elements.
    public List<ScriptElement> getElements() {
        return elements;
    }

    /**
     * Parses a space-delimited script string into a {@link Script}.
     * 
     * @param scriptText the raw script string (e.g. {@code "OP_DUP OP_HASH160 PUBKEYHASH_ABC OP_EQUALVERIFY OP_CHECKSIG"})
     * @return the parsed {@link Script}
     */
    public static Script parse(String scriptText) {
        List<ScriptElement> elements = new ArrayList<>();
        if (scriptText == null || scriptText.isBlank()) {
            return new Script(elements);
        }
        for (String token : scriptText.trim().split("\\s+")) {
            if (token.startsWith("OP_")) {
                elements.add(new OpElement(token));
            } else {
                elements.add(new DataElement(token));
            }
        }
        return new Script(elements);
    }
}
