package com.mackaytech.musicorganizer;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Wrapper class for jaudiotagger library (http://www.jthink.net/jaudiotagger/)
 * Used to fetch metatag data from files
 * Created by Glen on 8/23/2015.
 */
public class MetadataHandler {

    // Fetches metadata attributes from a music file
    public static HashMap getFileAttributes(File filePath) {
        HashMap<String, String> attributes = new HashMap<>();
        try {
            AudioFile f = AudioFileIO.read(filePath);
            AudioHeader header = f.getAudioHeader();
            Tag tag = f.getTag();
            if (tag == null) {
                throw new TagException("No tag data available for file " + filePath.getName());
            }
            attributes.put("Artist", tag.getFirst(FieldKey.ARTIST));
            attributes.put("Album", tag.getFirst(FieldKey.ALBUM));
            attributes.put("Title", tag.getFirst(FieldKey.TITLE));
            // Convert approximate numbers to real numbers (e.g. "~128" -> "128")
            attributes.put("Bitrate", header.getBitRate().replaceAll("[^0-9]", ""));
        } catch (TagException e) {
            e.printStackTrace();
        } catch (ReadOnlyFileException e) {
            e.printStackTrace();
        } catch (CannotReadException e) {
            e.printStackTrace();
        } catch (InvalidAudioFrameException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return attributes;
    }

    // Fetches audio metadata from a list of files
    public static HashMap getFilesMetadata(ArrayList<File> files) {
        HashMap filesMetadata = new HashMap<File, HashMap>();
        HashMap attributes = new HashMap<>();
        for (File file : files) {
            if (MetadataHandler.isMusicFile(file)) {
                attributes = MetadataHandler.getFileAttributes(file);
                if (attributes.size() == 0) {
                    System.out.println("ERROR: Could not get meta data from file "
                            + file.getPath() + file.getName() + " skipping to next file");
                    continue;
                }
                filesMetadata.put(file, attributes);
            }
            if (MetadataHandler.isImageFile(file) && attributes.size() > 0) {
                filesMetadata.put(file, attributes);
            }
        }
        if (filesMetadata.size() == 0 && files.size() > 0) {
            System.out.println("getFilesMetadata found no applicable music or image files in " + files);
        }
        return filesMetadata;
    }

    private static boolean isMusicFile(File file) {
        String[] parts = file.getName().split("\\.");
        String fileType = parts[parts.length - 1];
        String[] musicFileTypes = new String[] {"flac", "wav", "mp3", "m4a", "wma", "mp4", "m4p"};
        return Arrays.asList(musicFileTypes).contains(fileType);
    }

    private static boolean isImageFile(File file) {
        String[] parts = file.getName().split("\\.");
        String fileType = parts[parts.length - 1];
        String[] imageFileTypes = new String[] {"jpg"};
        return Arrays.asList(imageFileTypes).contains(fileType);
    }
}
