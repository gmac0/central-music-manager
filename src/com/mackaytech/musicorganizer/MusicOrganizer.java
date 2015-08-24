package com.mackaytech.musicorganizer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Glen on 8/9/2015.
 */
public class MusicOrganizer {
    //private static String searchFolder = "C:\\Users\\Roddy\\Desktop\\untagged rips\\Bassnectar\\Divergent Spectrum\\";
    private static String searchFolder = "C:\\Users\\Roddy\\Desktop\\untagged rips\\Bassnectar\\";
    private static String destinationFolder = "C:\\Users\\Roddy\\Desktop\\test\\";

    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler(MusicOrganizer.searchFolder);
        fileHandler.setDestinationFolder(destinationFolder);
        while (fileHandler.hasNextFolder()) {
            fileHandler.scanNextFolderContents();
            ArrayList<File> files = fileHandler.getFolderFiles();
            HashMap filesMetadata = MetadataHandler.getFilesMetadata(files);
            HashMap filePaths = MusicOrganizer.getFolderPathsFromMetadata(filesMetadata);
            fileHandler.moveFiles(filePaths);
        }
    }

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
