package com.cab302.cab302project.util;

import com.cab302.cab302project.error.util.*;

import java.io.*;

/**
 * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
 */
public class FileUtils {

    /**
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
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    private static final String CSV_CONTENT_CHECK_FAILED_MSG = "CSV file failed content examination";
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
