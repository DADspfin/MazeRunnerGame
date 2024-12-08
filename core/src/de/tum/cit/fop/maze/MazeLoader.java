package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;



public class MazeLoader {

    private static final int DEFAULT_WIDTH = 15; // Adjust based on your maze size
    private static final int DEFAULT_HEIGHT = 15;

    public static Map<String, Integer> loadMaze(String filePath) {
        Map<String, Integer> maze = new HashMap<>();
        try {
            // Use a standard file handle instead of Gdx.files.internal
            File file = new File(filePath); // Replace file handling to support external folders
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                String key = parts[0].trim(); // Coordinates (e.g., "3,0")
                int value = Integer.parseInt(parts[1].trim()); // Object type (e.g., 0 for wall)
                maze.put(key, value);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maze;
    }

}

