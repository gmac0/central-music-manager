package com.mackaytech.musicorganizer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class for running the music organizer
 * Created by Glen on 8/9/2015.
 */
public class MusicOrganizer {
    public static void main(String[] args) {
        // Validate and set input params
        if (args.length < 2 || args[0] == "-h" || args[0] == "--help") {
            System.out.println("Usage: java -jar MusicOrganizer.jar pathToSearch pathToOutput");
            return;
        }
        String searchFolder = args[0];
        String destinationFolder = args[1];
        if (destinationFolder.substring(destinationFolder.length() - 1) != File.separator) {
            destinationFolder+= File.separator;
        }
        // Setup
        Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF);
        FileHandler fileHandler = new FileHandler(searchFolder);
        fileHandler.setDestinationFolder(destinationFolder);
        DuplicateHandler duplicateHandler = new DuplicateHandler();
        duplicateHandler.setDirRoot(destinationFolder);
        // Scan through searchFolder and move files to destinationFolder
        while (fileHandler.hasNextFolder()) {
            fileHandler.scanNextFolderContents();
            ArrayList<File> files = fileHandler.getFolderFiles();
            HashMap filesMetadata = MetadataHandler.getFilesMetadata(files);
            HashMap filePaths = MusicOrganizer.getFolderPathsFromMetadata(filesMetadata);
            HashMap finalFilePaths = duplicateHandler.resolveDuplicates(filesMetadata, filePaths);
            fileHandler.moveFiles(finalFilePaths);
        }
    }

    // Generates the destination folder path based on the music file's metadata
    private static HashMap<File, String> getFolderPathsFromMetadata(HashMap filesMetadata) {
        HashMap<File, String> filePaths = new HashMap<>();
        Map<File, HashMap> map = filesMetadata;
        for (Map.Entry<File, HashMap> entry : map.entrySet()) {
            HashMap<String, String> metadata = entry.getValue();
            String folderPath = metadata.get("Artist") + File.separator + metadata.get("Album");
            filePaths.put(entry.getKey(), folderPath);
        }
        return filePaths;
    }

}
