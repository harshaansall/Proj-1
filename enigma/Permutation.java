package enigma;
import java.util.HashMap;
import java.util.Map;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Harshaan Sall
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        String cyc1 = cycles.replace("(", "");
        String[] cycless = cyc1.split("\\)");
        _alphabet = alphabet;
        for (String cycle : cycless) {
            addCycle(cycle);
        }
        for (int x = 0; x < alphabet.size(); x++) {
            if (!perm.containsKey(alphabet.toChar(x))) {
                perm.put(alphabet.toChar(x), alphabet.toChar(x));
                reversePerm.put(alphabet.toChar(x), alphabet.toChar(x));
            }
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        String cyc = cycle.replace(" ", "");
        char[] charArray = cyc.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray.length == 1) {
                perm.put(charArray[0], charArray[0]);
                reversePerm.put(charArray[0], charArray[0]);
            }
            if (i == charArray.length - 1) {
                perm.put(charArray[i], charArray[0]);
            }
            perm.put(charArray[i], charArray[(i + 1) % charArray.length]);
            if (i == 0) {
                reversePerm.put(charArray[0],
                        charArray[charArray.length - 1]);
            }
            reversePerm.put(charArray[(i + 1) % charArray.length],
                    charArray[i]);
        }
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return this._alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char input = _alphabet.toChar(p);
        char output = perm.get(input);
        return _alphabet.toInt(output);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char input = _alphabet.toChar(c);
        char output = reversePerm.get(input);
        return _alphabet.toInt(output);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        return perm.get(p);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        return reversePerm.get(c);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (Map.Entry<Character, Character> entry: perm.entrySet()) {
            if (entry.getKey() == entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** permutation. */
    private HashMap<Character, Character> perm = new HashMap<>();

    /** permutation. */
    private HashMap<Character, Character> reversePerm = new HashMap<>();

}


