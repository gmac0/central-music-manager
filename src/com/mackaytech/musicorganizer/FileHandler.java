package com.mackaytech.musicorganizer;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Filesystem helper class
 * Created by Glen on 7/11/2015.
 */
public class FileHandler {
    private Stack<String> pendingFolderPaths = new Stack<String>();
    private ArrayList<File> currentFiles = new ArrayList<File>();
    private String destinationFolder;

    // Constructor, sets a root folder
    public FileHandler (String rootFolder) {
        System.out.println("adding " + rootFolder + " to pendingFolderPaths");
        pendingFolderPaths.push(rootFolder);
    }

    public boolean hasNextFolder() {
        return !pendingFolderPaths.empty();
    }

    public void scanNextFolderContents() {
        String folderPath = pendingFolderPaths.pop();
        System.out.println("FileHandler scanning folder" + folderPath);
        this.scanFolderContents(folderPath);
    }

    // Clears the list of current files and fills the list with the current folder's contents
    public void scanFolderContents(String folderPath) {
        File folder = new File(folderPath);
        File[] contents = folder.listFiles();
        if (contents == null) {
            System.out.println("Contents of " + folderPath + " null!");
            return;
        }
        currentFiles.clear();
        for (File file : contents) {
            if (file.isFile()) {
                currentFiles.add(file);
            } else if (file.isDirectory()) {
                System.out.println("  adding " + file.getPath() + " to pendingFolderPaths");
                pendingFolderPaths.push(file.getPath());
            }
        }
    }

    public ArrayList<File> getFolderFiles() {
        return currentFiles;
    }

    // Moves a map of files, key = file to move, value = path to move to
    public void moveFiles(HashMap filePaths) {
        Map<File, String> map = filePaths;
        for (Map.Entry<File, String> entry : map.entrySet()) {
            File file = entry.getKey();
            String destinationPath = entry.getValue();
            File destination = new File(this.destinationFolder + destinationPath);
            destination.mkdirs();
            File destinationFile = new File(destination.toPath() + File.separator + file.getName());
            if (!destinationFile.exists()) {
                try {
                    System.out.println("Copying " + file.getName());
                    Files.copy(file.toPath(), destinationFile.toPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println(file.getName() + " already exists");
            }
        }
    }

    public void setDestinationFolder(String destinationFolder) {
        this.destinationFolder = destinationFolder;
    }

}
