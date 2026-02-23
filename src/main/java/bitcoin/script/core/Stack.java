package bitcoin.script.core;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class Stack {

    private final Deque<String> deque = new ArrayDeque<>();

    /**
     * Pushes {@code value} onto the top of the stack.
     *
     * @param value the string value to push (must not be null)
     */
    public void push(String value) {
        deque.push(value);   // pushes to head (= top)
    }

    /**
     * Removes and returns the top value.
     *
     * @return the top stack value
     * @throws ScriptException if the stack is empty (stack underflow)
     */
    public String pop() {
        if (deque.isEmpty()) {
            throw new ScriptException("Stack underflow: attempted to pop from an empty stack");
        }
        return deque.pop();
    }

    /**
     * Returns (without removing) the top value.
     *
     * @return the top stack value
     * @throws ScriptException if the stack is empty
     */
    public String peek() {
        if (deque.isEmpty()) {
            throw new ScriptException("Stack underflow: attempted to peek at an empty stack");
        }
        return deque.peek();
    }

    /** Returns the number of elements currently on the stack. */
    public int size() {
        return deque.size();
    }

    /** Returns {@code true} if the stack contains no elements. */
    public boolean isEmpty() {
        return deque.isEmpty();
    }

    /**
     * Returns an ordered snapshot of the stack for trace output.
     *
     * @return a new list containing all stack elements, bottom-first
     */
    public List<String> snapshot() {
        List<String> copy = new ArrayList<>(deque);  // head (top) first
        Collections.reverse(copy);                    // reverse → bottom first
        return copy;
    }

    /**
     * Returns {@code true} if {@code value} is truthy in Bitcoin Script terms.
     *
     * @param value the string value to test
     * @return {@code true} if truthy
     */
    public static boolean isTruthy(String value) {
        return value != null && !value.equals("0") && !value.isEmpty();
    }
}
