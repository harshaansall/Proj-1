package enigma;
import static enigma.EnigmaException.*;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Harshaan Sall
 */
class Alphabet {

    /** A new alphabet containing CHARS. The K-th character has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        if (chars.contains(" ")) {
            throw new EnigmaException("No white space!");
        }
        char [] charsArr = chars.toCharArray();
        for (int i = 0; i < chars.length(); i++) {
            for (int j = i + 1; j < chars.length(); j++) {
                if (charsArr[i] == charsArr[j]) {
                    throw new EnigmaException("No duplicates allowed!");
                }
            }
        }
        this._chars = chars;
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return this._chars.length();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        return this._chars.contains(String.valueOf(ch));
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return this._chars.charAt(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        return this._chars.indexOf(ch);
    }

    /** characters in alphabet.*/
    private String _chars;

}
