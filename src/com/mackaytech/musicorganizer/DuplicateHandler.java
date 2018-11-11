package com.mackaytech.musicorganizer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Ensures destination direction only has the highest bitrate version of a file
 * Created by Glen on 8/14/2016.
 */
public class DuplicateHandler {

    private HashMap<String, Integer> existingFileBitrates = new HashMap<>();
    private HashMap<String, File> existingFilePaths = new HashMap<>();
    private HashMap<String, Integer> incomingFileBitrates = new HashMap<>();
    private HashMap<String, File> incomingFilePaths = new HashMap<>();
    private HashSet<String> scannedDirs = new HashSet<>();
    private FileHandler fileHandler;
    private String dirRoot;

    public void setDirRoot(String dirRoot) {
        this.dirRoot = dirRoot;
        this.fileHandler = new FileHandler(dirRoot);
    }

    // Resolves both incoming files and exiting files in the destination directory
    public HashMap resolveDuplicates(HashMap filesMetadata, HashMap filePaths) {
        this.incomingFileBitrates.clear();
        Map<File, HashMap> map = filesMetadata;
        HashMap finalFilePaths = filePaths;
        for (Map.Entry<File, HashMap> entry : map.entrySet()) {
            HashMap<String, String> metadata = entry.getValue();
            this.resolveIncomingFiles(entry, finalFilePaths);
            String targetDir = this.dirRoot + filePaths.get(entry.getKey());
            if (!this.scannedDirs.contains(targetDir)) {
                this.scanDir(targetDir);
            }
            this.deleteLowerBitrateDuplicate(metadata);
        }
        return finalFilePaths;
    }

    // Scans incoming files for multiple of the same song, removes lower bitrate version from filePaths
    private void resolveIncomingFiles(Map.Entry<File, HashMap> fileMetadata, HashMap filePaths) {
        HashMap<String, String> metadata = fileMetadata.getValue();
        Integer bitrate = this.getBitrate(metadata);
        String fileIndex = this.getFileIndex(metadata);

        if (this.incomingFileBitrates.get(fileIndex) != null) {

            if ((this.incomingFileBitrates.get(fileIndex) > bitrate)){
                // Previously found file is higher bitrate than current, remove current
                filePaths.remove(fileMetadata.getKey());
            } else {
                // Current file is higher bitrate than previous, remove previous
                File previousFile = this.incomingFilePaths.get(fileIndex);
                filePaths.remove(previousFile);
                this.incomingFileBitrates.put(fileIndex, bitrate);
                this.incomingFilePaths.put(fileIndex, fileMetadata.getKey());
            }
        } else {
            // No duplicates found, add current to list of incoming
            this.incomingFileBitrates.put(fileIndex, bitrate);
            this.incomingFilePaths.put(fileIndex, fileMetadata.getKey());
        }
    }

    private int getBitrate(HashMap<String, String> metadata) {
        if (metadata.get("Bitrate") == null) {
            return 0;
        }
        return Integer.parseInt(metadata.get("Bitrate"));
    }

    // Scans destination folder for music files and saves their bitrates and paths
    private void scanDir(String targetDir) {
        this.fileHandler.scanFolderContents(targetDir);
        ArrayList<File> files = this.fileHandler.getFolderFiles();
        HashMap filesMetadata = MetadataHandler.getFilesMetadata(files);
        Map<File, HashMap> map = filesMetadata;
        for (Map.Entry<File, HashMap> entry : map.entrySet()) {
            HashMap<String, String> metadata = entry.getValue();
            existingFileBitrates.put(this.getFileIndex(metadata), this.getBitrate(metadata));
            existingFilePaths.put(this.getFileIndex(metadata), entry.getKey());
        }
        this.scannedDirs.add(targetDir);
    }

    // Gets the index of a file used to resolve duplicates (artist, album, and song title)
    private String getFileIndex(HashMap<String, String> metadata) {
        String fileIndex = metadata.get("Artist") + metadata.get("Album") + metadata.get("Title");
        return fileIndex.replaceAll(" ", "").toLowerCase();
    }

    // Removes a lower bitrate files from destination directory
    private void deleteLowerBitrateDuplicate(HashMap<String, String> metadata) {
        String index = this.getFileIndex(metadata);
        if (this.existingFileBitrates.get(index) == null) {
            return;
        }
        if (this.getBitrate(metadata) > this.existingFileBitrates.get(index)) {
            System.out.println(metadata + " is higher bitrate than " + this.existingFileBitrates.get(index));
            File toDelete = this.existingFilePaths.get(index);
            System.out.println("deleting " + toDelete);
            toDelete.delete();
        }

    }

}
