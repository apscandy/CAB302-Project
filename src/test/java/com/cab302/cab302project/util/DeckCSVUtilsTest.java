package com.cab302.cab302project.util;

import com.cab302.cab302project.error.util.*;
import com.cab302.cab302project.model.card.Card;
import com.cab302.cab302project.model.deck.Deck;
import com.cab302.cab302project.model.user.User;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DeckCSVUtilsTest {
    private Path tempDir;
    private Path exportFile;
    private final User mockUser = new User("Test", "User", "test@testing.com.au", "P@assword$$");

    @BeforeEach
    void setup() throws IOException {
        tempDir = Files.createTempDirectory("deckTest");
        exportFile = tempDir.resolve("export.csv");
    }

    @AfterEach
    void cleanup() throws IOException {
        deleteDirectory(tempDir.toFile());
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    private void deleteDirectory(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                deleteDirectory(f);
            }
        }
        dir.delete();
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Test
    void testExportDeckAndImportDeck_shouldMatchOriginal() {
        Deck originalDeck = new Deck("Sample Deck", "Just a test", mockUser);
        originalDeck.setCards(List.of(
                new Card(originalDeck, "Q1, with comma", "A1", "tag1;tag2"),
                new Card(originalDeck, "Q2 with \"quotes\"", "A2", "")
        ));

        DeckCSVUtils.exportDeck(exportFile.toString(), originalDeck);
        assertTrue(exportFile.toFile().exists());

        Deck importedDeck = DeckCSVUtils.importDeck(exportFile.toString(), mockUser);

        assertEquals(originalDeck.getName(), importedDeck.getName());
        assertEquals(originalDeck.getDescription(), importedDeck.getDescription());
        assertEquals(2, importedDeck.getCards().size());
        assertEquals("Q1, with comma", importedDeck.getCards().get(0).getQuestion());
        assertEquals("Q2 with \"quotes\"", importedDeck.getCards().get(1).getQuestion());
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Test
    void testImportDeck_invalidHeader_shouldThrow() throws IOException {
        Files.writeString(exportFile, "bad,header\\nmetadata\\n\\nbad,header\\n");
        assertThrows(CSVImportInvalidFormatException.class, () ->
                DeckCSVUtils.importDeck(exportFile.toString(), mockUser));
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Test
    void testSplitCSVLine_validQuotes_shouldSplitCorrectly() {
        String line = "\"Oiiiii, Oiiii\",\"Oi\",\"tag1;tag2\"";
        List<String> fields = DeckCSVUtils.splitCSVLine(line, 3);
        assertEquals("Oiiiii, Oiiii", fields.get(0));
        assertEquals("Oi", fields.get(1));
        assertEquals("tag1;tag2", fields.get(2));
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Test
    void testSplitCSVLine_escapedQuotes_shouldParseProperly() {
        String line = "\"John Wick said \"\"Meow\"\"\",\"Oi\",\"\"";
        List<String> fields = DeckCSVUtils.splitCSVLine(line, 3);
        assertEquals("John Wick said \"Meow\"", fields.get(0));
        assertEquals("Oi", fields.get(1));
        assertEquals("", fields.get(2));
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Test
    void testSplitCSVLine_tooFewColumns_shouldThrow() {
        String line = "OnlyOneColumn";
        assertThrows(CSVImportInvalidFormatException.class, () ->
                DeckCSVUtils.splitCSVLine(line, 2));
    }
}
