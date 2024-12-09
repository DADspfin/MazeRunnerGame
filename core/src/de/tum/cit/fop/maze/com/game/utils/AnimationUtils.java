package de.tum.cit.fop.maze.com.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.Arrays;

public class AnimationUtils {
    /**
     * Creates and handles an Animation from manually specified frame coordinates.
     *
     * @param filePath     The path to the sprite sheet file.
     * @param frameData    A 2D array specifying the frame coordinates: {x, y, width, height}.
     * @param frameDuration The duration of each frame in seconds.
     * @return The Animation object for the specified frames.
     */
    public static Animation<TextureRegion> createAnimationFromCoordinates(
            String filePath, int[][] frameData, float frameDuration) {
        Texture spriteSheet = new Texture(Gdx.files.internal(filePath));
        TextureRegion[] frames = new TextureRegion[frameData.length];
        for (int i = 0; i < frameData.length; i++) {
            int x = frameData[i][0];
            int y = frameData[i][1];
            int width = frameData[i][2];
            int height = frameData[i][3];
            frames[i] = new TextureRegion(spriteSheet, x, y, width, height);
        }
        return new Animation<>(frameDuration, frames);
    }

    /**
     * Generates frame coordinates for rows with irregular frame widths.
     *
     * @param startX   The x-coordinate of the first frame in the row.
     * @param startY   The y-coordinate of the first row.
     * @param rowData  A 2D array where each sub-array represents the widths of frames in a row.
     * @param rowHeight The height of each row.
     * @return A 2D array of frame coordinates: {x, y, width, height}.
     */
    public static int[][] generateRowSpecificFrameCoordinates(
            int startX, int startY, int[][] rowData, int rowHeight) {
        int[][] coordinates = new int[sumFrames(rowData)][4];
        int index = 0;
        for (int row = 0; row < rowData.length; row++) {
            int x = startX;
            for (int frameWidth : rowData[row]) {
                coordinates[index++] = new int[]{x, startY + row * rowHeight, frameWidth, rowHeight};
                x += frameWidth; // Move to the next frame
            }
        }
        return coordinates;
    }

    /**
     * Sums up the total number of frames across all rows.
     *
     * @param rowData A 2D array where each sub-array represents the widths of frames in a row.
     * @return The total number of frames.
     */
    private static int sumFrames(int[][] rowData) {
        return Arrays.stream(rowData).mapToInt(row -> row.length).sum();
    }

    /**
     * Creates and handles an Animation from a specific row in a sprite sheet.
     *
     * @param filePath     The path to the sprite sheet file.
     * @param frameCols    Total number of columns in the sprite sheet.
     * @param frameRows    Total number of rows in the sprite sheet.
     * @param rowIndex     The row to extract the animation from (0-based).
     * @param frameCount   The number of frames to use from the row.
     * @param frameDuration The duration of each frame in seconds.
     * @return The Animation object for the specified row.
     */
    public static Animation<TextureRegion> createAnimationFromRow(
            String filePath, int frameCols, int frameRows, int rowIndex, int frameCount, float frameDuration) {
        Texture spriteSheet = new Texture(Gdx.files.internal(filePath));
        TextureRegion[][] tmp = TextureRegion.split(
                spriteSheet,
                spriteSheet.getWidth() / frameCols,
                spriteSheet.getHeight() / frameRows
        );

        if (rowIndex < 0 || rowIndex >= frameRows) {
            throw new IllegalArgumentException("Row index out of bounds");
        }
        if (frameCount > frameCols) {
            throw new IllegalArgumentException("Frame count exceeds available columns");
        }

        TextureRegion[] rowFrames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            rowFrames[i] = tmp[rowIndex][i];
        }
        return new Animation<>(frameDuration, rowFrames);
    }
}

