package qengine.model;

import fr.boreal.model.logicalElements.api.Term;
import fr.boreal.model.logicalElements.factory.impl.SameObjectTermFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DictionaryTest {
    private Dictionary dictionary;

    @BeforeEach
    void setUp() {
        dictionary = new Dictionary();
    }

    @Test
    void testEncode() {
        // Test d'encodage simple
        int id1 = dictionary.encode("test1");
        int id2 = dictionary.encode("test2");

        // Vérifie que les IDs sont différents
        assertNotEquals(id1, id2);

        // Vérifie que le même string donne le même ID
        assertEquals(id1, dictionary.encode("test1"));
    }

    @Test
    void testDecode() {
        String original = "testString";
        int id = dictionary.encode(original);

        // Vérifie que le décodage donne la chaîne originale
        assertEquals(original, dictionary.decode(id));
    }

    @Test
    void testDecodeInvalidId() {
        // Vérifie que le décodage d'un ID inexistant lance une exception
        assertThrows(IllegalArgumentException.class, () -> dictionary.decode(999));
    }

    @Test
    void testEncodeNull() {
        // Vérifie que l'encodage de null lance une exception
        assertThrows(IllegalArgumentException.class, () -> dictionary.encode(null));
    }

    @Test
    void testContains() {
        String str = "testString";
        int id = dictionary.encode(str);

        // Vérifie la présence de la chaîne et de l'ID
        assertTrue(dictionary.containsString(str));
        assertTrue(dictionary.containsId(id));

        // Vérifie l'absence de valeurs non ajoutées
        assertFalse(dictionary.containsString("nonexistent"));
        assertFalse(dictionary.containsId(999));
    }

    @Test
    void testSize() {
        assertEquals(0, dictionary.size());

        dictionary.encode("test1");
        assertEquals(1, dictionary.size());

        dictionary.encode("test2");
        assertEquals(2, dictionary.size());

        // Vérifie que l'encodage d'une même chaîne ne change pas la taille
        dictionary.encode("test1");
        assertEquals(2, dictionary.size());
    }

    @Test
    void testClear() {
        dictionary.encode("test1");
        dictionary.encode("test2");

        dictionary.clear();

        assertEquals(0, dictionary.size());
        assertFalse(dictionary.containsString("test1"));
        assertFalse(dictionary.containsId(1));
    }

    @Test
    void testEncodeRDFAtom() {
        // Création d'un RDFAtom de test
        Term subject = SameObjectTermFactory.instance().createOrGetLiteral("subject");
        Term predicate = SameObjectTermFactory.instance().createOrGetLiteral("predicate");
        Term object = SameObjectTermFactory.instance().createOrGetLiteral("object");
        RDFAtom atom = new RDFAtom(subject, predicate, object);

        // Encode l'atom
        int[] encoded = dictionary.encodeRDFAtom(atom);

        // Vérifie la taille du résultat
        assertEquals(3, encoded.length);

        // Vérifie que le décodage donne les valeurs originales
        assertEquals("subject", dictionary.decode(encoded[0]));
        assertEquals("predicate", dictionary.decode(encoded[1]));
        assertEquals("object", dictionary.decode(encoded[2]));
    }

    @Test
    void testEncodeNullRDFAtom() {
        assertThrows(IllegalArgumentException.class, () -> dictionary.encodeRDFAtom(null));
    }
}