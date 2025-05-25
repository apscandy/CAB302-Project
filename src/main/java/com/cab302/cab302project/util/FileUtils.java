package com.cab302.cab302project.util;

import com.cab302.cab302project.error.util.*;

        import java.io.*;

/**
 * Utility class for file-related operations such as path validation, directory creation,
 * and CSV content verification.
 * <p>
 * This class throws specific custom exceptions for various failure scenarios to ensure
 * better traceability and error handling in file operations.
 * </p>
 *
 * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
 */
public class FileUtils {

    /**
     * Checks whether a file path is valid, optionally verifies its existence,
     * and ensures the file has the required extension.
     *
     * @param filePath      the path of the file to check
     * @param existed       if true, checks if the file exists and is a normal file
     * @param fileExtension the required file extension (e.g.: ".csv")
     * @return a valid {@link File} object pointing to the provided file path
     * @throws FilePathIsNullException       if file path is null or empty
     * @throws InvalidFilePathException      if the file does not have the expected extension
     * @throws FileDoesNotExistException     if the file must exist but does not
     * @throws FileIsNotNormalFile           if the file exists but is not a regular file
     *
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    public static File pathCheck(String filePath, boolean existed, String fileExtension) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new FilePathIsNullException("File path cannot be null");
        }
        File file = new File(filePath);
        if (!file.getName().toLowerCase().endsWith(fileExtension.toLowerCase())) {
            throw new InvalidFilePathException(String.format("File name must end with %s", fileExtension));
        }
        if (existed) {
            if (!file.exists()) {
                throw new FileDoesNotExistException("File does not exist");
            }
            if (!file.isFile()) {
                throw new FileIsNotNormalFile("File is not a normal file");
            }
        }
        return file;
    }

    /**
     * Ensures the parent directory of a file exists.
     * If it doesn't, attempts to create it, while verifying write permissions.
     *
     * @param file the file whose parent directory is checked or created
     * @throws FileDirectoryIsNullException     if no valid parent directory is found
     * @throws FileDirectoryIsNotWritable       if the parent directory is not writable
     * @throws FileDirectoryCreationException   if the directory could not be created
     *
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    public static void dirTouch(File file) {
        File parentDir = file.getParentFile();
        if (parentDir == null) {
            throw new FileDirectoryIsNullException("Parent directory cannot be null");
        }
        File existingDir = parentDir;
        while (existingDir != null && !existingDir.exists()) {
            existingDir = existingDir.getParentFile();
        }
        if (existingDir == null) {
            throw new FileDirectoryIsNullException("No existing parent directory found in path");
        }
        if (!existingDir.canWrite()) {
            throw new FileDirectoryIsNotWritable("Parent directory is not writable: " + existingDir.getAbsolutePath());
        }
        if (!parentDir.exists()) {
            boolean dirCreated = parentDir.mkdirs();
            if (!dirCreated) {
                throw new FileDirectoryCreationException("Failed to create directory");
            }
        }
    }

    /**
     * Error message used when CSV content fails heuristic checks.
     *
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    private static final String CSV_CONTENT_CHECK_FAILED_MSG = "CSV file failed content examination";

    /**
     * Performs a heuristic check on a CSV file to ensure its content
     * is likely valid and not binary or incorrectly formatted.
     *
     * <p>This method checks the file for the presence of commas and the absence
     * of binary-like content in the first few lines.</p>
     *
     * @param file the CSV file to validate
     * @throws InvalidCSVContentException if the file appears to be binary or lacks comma-separated content
     *
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    public static void csvHeuristicCheck(File file) {
        if (isLikelyBinary(file)) {
            throw new InvalidCSVContentException(CSV_CONTENT_CHECK_FAILED_MSG + ": " + "Suspicious file");
        }
        // https://medium.com/@snowcloudbyte/read-and-validate-csv-files-with-java-b2f2b983843d#Read%20CSV%20Files%20using%20Java%20without%20libraries
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            int linesToCheck = 3;
            int validLines = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null && linesToCheck-- > 0) {
                if (line.contains(",")) {
                    validLines++;
                }
            }
            if (validLines == 0) {
                throw new InvalidCSVContentException(CSV_CONTENT_CHECK_FAILED_MSG + ": " + "No comma found");
            }
        } catch (IOException e) {
            throw new InvalidCSVContentException(CSV_CONTENT_CHECK_FAILED_MSG + ": " + e.getMessage());
        }
    }

    /**
     * Checks whether a file is likely a binary file by inspecting its byte content.
     *
     * <p>This method reads the first few bytes of the file and flags it as binary
     * if it contains non-text ASCII characters outside of printable/control range.</p>
     *
     * @param file the file to inspect
     * @return true if the file likely contains binary data, false otherwise
     *
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    private static boolean isLikelyBinary(File file) {
        try (InputStream inputStream = new FileInputStream(file)) {
            int inspectByteSize = 512;
            byte[] buffer = new byte[inspectByteSize];
            int byteRead = inputStream.read(buffer);
            if (byteRead == -1) {
                return true;
            }
            for (int i = 0; i < byteRead; i++) {
                byte b = buffer[i];
                // https://www.ascii-code.com/
                if (b < 0x09 || (b > 0x0D && b < 0x20)) return true;
            }
        } catch (IOException ignored) {
            return true;
        }
        return false;
    }
}

