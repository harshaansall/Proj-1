package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Harshaan Sall
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    void advance() {
        set(setting() + 1);
    }

    @Override
    boolean atNotch() {
        int x = permutation().wrap(setting());
        return this._notches.contains(String.valueOf(alphabet().toChar(x)));
    }

    @Override
    void set(char cposn) {
        super.set(alphabet().toInt(cposn));
        _initial = cposn;
    }

    @Override
    String notches() {
        return "";
    }

    /** Notches. */
    private String _notches;

    /** Setting. */
    private char _initial;

}
