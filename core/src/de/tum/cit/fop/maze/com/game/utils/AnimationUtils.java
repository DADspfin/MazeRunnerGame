package de.tum.cit.fop.maze.com.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationUtils {
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
        // Load the sprite sheet
        Texture spriteSheet = new Texture(Gdx.files.internal(filePath));

        // Split the sprite sheet into a grid of frames
        TextureRegion[][] tmp = TextureRegion.split(
                spriteSheet,
                spriteSheet.getWidth() / frameCols,
                spriteSheet.getHeight() / frameRows
        );

        // Validate the row index and frame count
        if (rowIndex < 0 || rowIndex >= frameRows) {
            throw new IllegalArgumentException("Row index out of bounds");
        }
        if (frameCount > frameCols) {
            throw new IllegalArgumentException("Frame count exceeds available columns");
        }

        // Extract the frames from the specified row
        TextureRegion[] rowFrames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            rowFrames[i] = tmp[rowIndex][i];
        }

        // Create and return the animation
        return new Animation<>(frameDuration, rowFrames);
    }
}

