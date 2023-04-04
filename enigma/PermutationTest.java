package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Harshaan Sall
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void checkSize() {
        Alphabet alph = new Alphabet();
        int len = alph.size();
        Permutation perm1 = new Permutation("(AELTPHQXRU)"
                + " (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", alph);
        assertEquals(len, perm1.size());
    }

    @Test
    public void testPermute() {
        Permutation p =  new Permutation("(BACD)",
                new Alphabet("ABCD"));
        assertEquals(2, p.permute(0));
        assertEquals(0, p.permute(1));
        assertEquals(3, p.permute(2));
        assertEquals(1, p.permute(3));
    }

    @Test
    public void testInvert() {
        Permutation p = new Permutation("(BACD)",
                new Alphabet("ABCD"));
        assertEquals(1, p.invert(0));
        assertEquals(0, p.invert(2));
        assertEquals(2, p.invert(3));
        assertEquals(3, p.invert(1));
    }

    @Test
    public void testInvertChar() {
        Alphabet alph = new Alphabet();
        Permutation perm1 = new Permutation("(AELTPHQXRU)"
                + " (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", alph);
        Permutation p = new Permutation("(BACD)",
                new Alphabet("ABCD"));
        assertEquals('B', p.invert('A'));
        assertEquals('A', p.invert('C'));
        assertEquals('C', p.invert('D'));
        assertEquals('D', p.invert('B'));
        assertEquals('S', perm1.permute('S'));
    }

    @Test
    public void testPermuteChar() {
        Alphabet alph = new Alphabet();
        Permutation perm1 = new Permutation("(AELTPHQXRU)"
                + " (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", alph);
        Permutation p = new Permutation("(BACD)",
                new Alphabet("ABCD"));
        assertEquals('A', p.permute('B'));
        assertEquals('C', p.permute('A'));
        assertEquals('D', p.permute('C'));
        assertEquals('B', p.permute('D'));
        assertEquals('S', perm1.permute('S'));
    }

    @Test
    public void testDerangement() {
        Alphabet alph = new Alphabet();
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        Permutation perm1 = new Permutation("(AELTPHQXRU)"
                + " (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", alph);
        assertTrue(p.derangement());
        assertFalse(perm1.derangement());
    }


}
