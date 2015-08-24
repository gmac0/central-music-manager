package com.mackaytech.musicorganizer;

import java.io.File;
import java.nio.file.Files;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Glen on 7/11/2015.
 */
public class FileHandler {
    //private static String searchFolder = "C:\\Users\\Roddy\\Desktop\\untagged rips\\Bassnectar\\Divergent Spectrum\\";
    private static Stack<String> pendingFolderPaths = new Stack<String>();
    private static ArrayList<File> currentFiles = new ArrayList<File>();
    private String destinationFolder;

    public FileHandler (String rootFolder) {
        System.out.println("adding " + rootFolder + " to pendingFolderPaths");
        pendingFolderPaths.push(rootFolder);
    }

    public boolean hasNextFolder() {
        return !pendingFolderPaths.empty();
    }

    public void scanNextFolderContents() {
        String folderPath = pendingFolderPaths.pop();
        System.out.println("scanning " + folderPath);
        File folder = new File(folderPath);
        File[] contents = folder.listFiles();
        for (File file : contents) {
            if (file.isFile()) {
                currentFiles.add(file);
            } else if (file.isDirectory()) {
                System.out.println("adding " + file.getPath() + " to pendingFolderPaths");
                pendingFolderPaths.push(file.getPath());
            }
        }
    }

    public ArrayList<File> getFolderFiles() {
        return currentFiles;
    }

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

//    public static void getFileAttributes(String[] args) {
//        File[] files = FileHandler.getFiles(FileHandler.searchFolder);
//        for (File file : files) {
//            MetadataHandler.getFileAttributes(file);
//        }
//    }
//
//    public static File[] getFolderList(String path) {
//        File initialDir = new File(path);
//        File[] files = initialDir.listFiles();
//        return files;
//    }
//
//    private static File[] getFiles(String path) {
//        File file = new File(FileHandler.searchFolder);
//        File[] list = file.listFiles();
//        return list;
//    }
//
//    private static boolean isMusicFile(File file) {
//        String fileType = file.getName().split("\\.")[1];
//        String[] musicFileTypes = new String[] {"flac", "wave"};
//        boolean isMusicFile = Arrays.asList(musicFileTypes).contains(fileType);
//        return isMusicFile;
//    }
}
