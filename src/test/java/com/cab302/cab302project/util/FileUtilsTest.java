package com.cab302.cab302project.util;

import com.cab302.cab302project.error.util.*;
import com.cab302.cab302project.util.FileUtils;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class FileUtilsTest {

    // https://www.baeldung.com/java-delete-directory
    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    private boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    private Path tempDir;
    private Path tempFile;

    @BeforeEach
    void setup() throws IOException {
        tempDir = Files.createTempDirectory("testDir");
        tempFile = Files.createTempFile(tempDir, "test", ".csv");
    }

    @AfterEach
    void cleanup() throws IOException {
        deleteDirectory(tempDir.toFile());
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Test
    void testPathCheck_validPath_shouldReturnFile() {
        File file = FileUtils.pathCheck(tempFile.toString(), true, ".csv");
        assertEquals(tempFile.toFile(), file);
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Test
    void testPathCheck_invalidExtension_shouldThrow() {
        Exception e = assertThrows(InvalidFilePathException.class, () ->
                FileUtils.pathCheck("testfile.txt", true, ".csv"));
        assertTrue(e.getMessage().contains(".csv"));
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Test
    void testPathCheck_nonexistentFile_shouldThrow() {
        Path nonExistent = tempDir.resolve("doesnotexist.csv");
        assertThrows(FileDoesNotExistException.class, () ->
                FileUtils.pathCheck(nonExistent.toString(), true, ".csv"));
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Test
    void testDirTouch_createsMissingParentDirs() {
        File nestedFile = tempDir.resolve("a/b/c/file.csv").toFile();
        assertDoesNotThrow(() -> FileUtils.dirTouch(nestedFile));
        assertTrue(nestedFile.getParentFile().exists());
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Test
    void testCsvHeuristicCheck_validCsv_shouldPass() throws IOException {
        Files.writeString(tempFile, "Header1,Header2\nvalue1,value2\n");
        assertDoesNotThrow(() -> FileUtils.csvHeuristicCheck(tempFile.toFile()));
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Test
    void testCsvHeuristicCheck_binaryFile_shouldThrow() throws IOException {
        byte[] binaryData = new byte[]{0x00, 0x01, 0x02, 0x10, 0x11};
        Files.write(tempFile, binaryData);
        assertThrows(InvalidCSVContentException.class, () ->
                FileUtils.csvHeuristicCheck(tempFile.toFile()));
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Test
    void testCsvHeuristicCheck_noComma_shouldThrow() throws IOException {
        Files.writeString(tempFile, "NoCommaHere\nStillNoCommaHaha\n");
        assertThrows(InvalidCSVContentException.class, () ->
                FileUtils.csvHeuristicCheck(tempFile.toFile()));
    }
}
