package com.cab302.cab302project.util;

import com.cab302.cab302project.error.util.CSVExportException;
import com.cab302.cab302project.error.util.CSVImportException;
import com.cab302.cab302project.error.util.CSVImportInvalidFormatException;
import com.cab302.cab302project.model.card.Card;
import com.cab302.cab302project.model.deck.Deck;
import com.cab302.cab302project.model.user.User;
import com.cab302.cab302project.util.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DeckCSVUtils {

    private static final String FILE_EXTENSION = ".csv";

    /**
     * Export a Deck to a CSV.
     *<p>
     * CSV layout:<br>
     * Deck Name,Deck Description<br>
     * &lt;name&gt;,&lt;description&gt;<br><br>
     *
     * Question,Answer,Tags<br>
     * "question 1","answer 1","tag1;tag2"<br>
     * "question 2","answer 2",""<br>
     * ...
     *</p>
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    public static void exportDeck (String exportPath, Deck deck) {
        File exportFile = FileUtils.pathCheck(exportPath, false, FILE_EXTENSION);
        FileUtils.dirTouch(exportFile);
        // https://javabeat.net/write-data-csv-file-in-java/
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(exportFile))) {
            writer.write("Deck Name,Deck Description");
            writer.newLine();
            writer.write(charEscape(deck.getName()) + ',' + charEscape(deck.getDescription()));
            writer.newLine();
            writer.newLine();
            writer.write("Question,Answer,Tags");
            writer.newLine();
            if (deck.getCards() != null) {
                for (Card card : deck.getCards()) {
                    writer.write(
                    charEscape(card.getQuestion()) + ","
                        + charEscape(card.getAnswer()) +  ","
                        + charEscape(card.getTags() == null ? "" : card.getTags())
                    );
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new CSVExportException("Failed to export deck: " + e.getMessage());
        }
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    private static String charEscape(String field) {
        if (field == null) return "";
        if (!(field.contains(",") || field.contains("\"") || field.contains("\n"))) return field;
        return '"' + field.replace("\"", "\"\"") + '"';
    }

    private static final String CSV_IMPORT_INVALID_CONTENT_FORMAT_MSG = "Invalid standard import format";

    /**
     * Export a Deck to a CSV.
     *<p>
     * CSV layout:<br>
     * Deck Name,Deck Description<br>
     * &lt;name&gt;,&lt;description&gt;<br><br>
     *
     * Question,Answer,Tags<br>
     * "question 1","answer 1","tag1;tag2"<br>
     * "question 2","answer 2",""<br>
     * ...
     *</p>
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    public static Deck importDeck(String importPath, User deckOwner) {
        File importFile = FileUtils.pathCheck(importPath, true, FILE_EXTENSION);
        FileUtils.csvHeuristicCheck(importFile);
        // https://medium.com/@snowcloudbyte/read-and-validate-csv-files-with-java-b2f2b983843d#Read%20CSV%20Files%20using%20Java%20without%20libraries
        try (BufferedReader reader = new BufferedReader(new FileReader(importFile))) {
            String metaHeader = reader.readLine();
            if (metaHeader == null || metaHeader.isEmpty()
                    || !metaHeader.toLowerCase().startsWith("deck name")
                    || !metaHeader.toLowerCase().endsWith("deck description")) throw new CSVImportInvalidFormatException(CSV_IMPORT_INVALID_CONTENT_FORMAT_MSG + ": Invalid metadata header");
            String metadata = reader.readLine();
            if (metadata == null || metadata.isEmpty()) throw new CSVImportInvalidFormatException(CSV_IMPORT_INVALID_CONTENT_FORMAT_MSG + ": Invalid metadata");
            List<String> meta = splitCSVLine(metadata, 2);
            Deck deck = new Deck(meta.get(0), meta.get(1), deckOwner);
            reader.readLine();
            String cardHeader = reader.readLine();
            if (cardHeader == null || cardHeader.isEmpty()
                    || !cardHeader.toLowerCase().startsWith("question")
                    || !cardHeader.toLowerCase().endsWith("tags")) throw new CSVImportInvalidFormatException(CSV_IMPORT_INVALID_CONTENT_FORMAT_MSG + ": Invalid card info header");
            List<Card> cards = new ArrayList<>();
            String line = reader.readLine();
            while (line != null) {
                if (line.trim().isEmpty()) continue;
                List<String> cardInfo = splitCSVLine(line, 3);
                cards.add(new Card(deck, cardInfo.get(0), cardInfo.get(1), cardInfo.get(2)));
                line = reader.readLine();
            }
            deck.setCards(cards);
            return deck;
        } catch (IOException e) {
            throw new CSVImportException("Failed to import deck: " + e.getMessage());
        }
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    static List<String> splitCSVLine(String row, int colCount) {
        List<String> cols = new ArrayList<>(colCount);
        StringBuilder stringBuilder = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < row.length(); i++) {
            char c = row.charAt(i);
            if (c == '"') {
                if (inQuotes && (i + 1) < row.length() && row.charAt(i + 1) == '"') {
                    stringBuilder.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                cols.add(stringBuilder.toString());
                stringBuilder.setLength(0); // reset string builder a field is extracted
            } else {
                stringBuilder.append(c);
            }
        }
        cols.add(stringBuilder.toString());
        if (cols.size() < colCount) throw new CSVImportInvalidFormatException(CSV_IMPORT_INVALID_CONTENT_FORMAT_MSG + ": Malformed CSV row");
        return cols;
    }
}
