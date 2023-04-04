package enigma;

import java.util.ArrayList;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Harshaan Sall
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _rotorSlots = numRotors;
        _numPawls = pawls;
        _available = allRotors;
        _plugboard = new Permutation("", alpha);
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _rotorSlots;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _numPawls;
    }

    /** Return Rotor #K, where Rotor #0 is the reflector, and Rotor
     *  #(numRotors()-1) is the fast Rotor.  Modifying this Rotor has
     *  undefined results. */
    Rotor getRotor(int k) {
        return _chosenRotors.get(k);
    }

    Alphabet alphabet() {
        return _alphabet;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        if (rotors.length > _available.size()) {
            throw new EnigmaException("Too many Rotors!");
        }
        if (rotors.length > _rotorSlots) {
            throw new EnigmaException("Not enough slots!");
        }
        if (_chosenRotors.size() != 0) {
            _chosenRotors = new ArrayList<Rotor>();
        }
        for (String rotor : rotors) {
            for (Rotor avail : _available) {
                if (avail.name().equals(rotor)
                        || avail.name().toUpperCase().equals(rotor)) {
                    _chosenRotors.add(avail);
                }
            }
        }
        if (!_chosenRotors.get(0).reflecting()) {
            throw new EnigmaException("No reflector!");
        }
        for (int i = 0; i < _chosenRotors.size(); i++) {
            for (int j = i + 1; j < _chosenRotors.size(); j++) {
                if (_chosenRotors.get(i).name()
                        .equals(_chosenRotors.get(j).name())) {
                    throw new EnigmaException("No duplicates!");
                }
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.matches(".*\\d.*")) {
            throw new EnigmaException("No numbers!");
        }
        _machineSetting = setting;
        if (setting.length() > _rotorSlots - 1
                || setting.length() < _rotorSlots - 1) {
            throw new EnigmaException("Not correct length!");
        }
        for (int i = 0; i < _rotorSlots - 1; i++) {
            if (!_alphabet.contains(setting.charAt(i))) {
                throw new EnigmaException("Setting character not in alphabet!");
            }
        }
        for (int i = 1; i < _rotorSlots; i++) {
            _chosenRotors.get(i).set(setting.charAt(i - 1));
        }
    }

    /** Return the current plugboard's permutation. */
    Permutation plugboard() {
        return _plugboard;
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        advanceRotors();
        char charac = _alphabet.toChar(c);
        if (Main.verbose()) {
            System.err.printf("[");
            for (int r = 1; r < numRotors(); r += 1) {
                System.err.printf("%c",
                        alphabet().toChar(getRotor(r).setting()));
            }
            System.err.printf("] %c -> ", alphabet().toChar(c));
        }
        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(c));
        }
        c = applyRotors(c);
        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c%n", alphabet().toChar(c));
        }
        return c;
    }

    /** Advance all rotors to their next position. */
    private void advanceRotors() {
        boolean[] advanceRotors = new boolean[_rotorSlots];
        for (int i = 0; i < _rotorSlots; i++) {
            if (i == _rotorSlots - 1) {
                advanceRotors[i] = true;
            } else if (_chosenRotors.get(i).rotates()
                    && _chosenRotors.get(i + 1).atNotch()) {
                advanceRotors[i] = true;
            }
        }
        for (int i = 0; i < _rotorSlots; i++) {
            if (advanceRotors[i]) {
                _chosenRotors.get(i).advance();
            }
        }
    }

    /** Return the result of applying the rotors to the character C (as an
     *  index in the range 0..alphabet size - 1). */
    private int applyRotors(int c) {
        for (int i = _rotorSlots - 1; i >= 0; i -= 1) {
            c = _chosenRotors.get(i).convertForward(c);
        }
        for (int i = 1; i < _rotorSlots; i++) {
            c = _chosenRotors.get(i).convertBackward(c);
        }
        return c;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String newMsg = msg.replace(" ", "");
        StringBuilder msgg = new StringBuilder();
        for (int i = 0; i < newMsg.length(); i++) {
            int x = _alphabet.toInt(newMsg.charAt(i));
            msgg.append(_alphabet.toChar(convert(x)));
        }
        return msgg.toString();
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** The number of rotor slots this machine has. */
    private int _rotorSlots;

    /** The number of pawls this machine has. */
    private int _numPawls;

    /** Rotors chosen for this machine. */
    private ArrayList<Rotor> _chosenRotors = new ArrayList<Rotor>();

    /** plugboard of this machine. */
    private Permutation _plugboard;

    /** Rotors available for this machine. */
    private Collection<Rotor> _available;

    /** Setting for this machine. */
    private String _machineSetting;

}
