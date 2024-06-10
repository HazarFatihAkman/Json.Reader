package main.java.reader.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class FileReader {
    private Path path;
    private List<File> files = new ArrayList<File>();
    private final List<String> included_file_extensions = List.of(".png", ".gif", ".jpeg", ".yaml", ".json", ".txt");

    /**
     * @param included_file_extensions = ".png", ".gif", ".jpeg", ".yaml", ".json", ".txt"
     */
    public FileReader() {
        this.path = Paths.get(System.getProperty("user.dir"));
    }

    /**
     * @param included_file_extensions = ".png", ".gif", ".jpeg", ".yaml", ".json", ".txt", included_file_extensions
     */
    public FileReader(String[] included_file_extensions) {
        this.path = Paths.get(System.getProperty("user.dir"));
        this.included_file_extensions.addAll(Arrays.asList(included_file_extensions));
    }

    public List<File> getAllFiles() throws Exception {
        if (files.isEmpty()) {
            appendFiles(path);
        }

        return Collections.unmodifiableList(files);
    }

    public File getFileByName(String file_name) throws Exception {
        return getFileByName(file_name, "");
    }

    public File getFileByName(String file_name, String folder_name) throws Exception {
        if (files.isEmpty()) {
            getAllFiles();
        }

        return files
            .stream()
            .filter(x -> x.getAbsolutePath().contains(folder_name + file_name))
            .findFirst()
            .get();
    }

    public String readFileAsString(File file) throws FileNotFoundException, IOException {
        var buffered_reader = new BufferedReader(new java.io.FileReader(file));
        StringBuilder response = new StringBuilder();

        while (buffered_reader.ready()) {
            response.append(buffered_reader.readLine());
        }

        buffered_reader.close();

        return response.toString();
    }

    private void appendFiles(Path current_path) throws IOException, NullPointerException {
        DirectoryStream<Path> stream = Files.newDirectoryStream(current_path);

        for (var path : stream) {
            if (Files.isDirectory(path)) {
                appendFiles(path);
            }
            else {
                File file;
                if (isValidFile(file = path.toFile())) {
                    files.add(file);
                }
                else {
                    continue;
                }
            }
        }

        stream.close();
    }

    private boolean isValidFile(File file) {
        var file_name = file.getName();
        String[] file_name_array = file_name.split("\\.");

        if (file_name_array.length <= 1) {
            return false;
        }

        return included_file_extensions
            .stream()
            .anyMatch(x -> x.contains(file_name_array[file_name_array.length - 1]));
    }
}
