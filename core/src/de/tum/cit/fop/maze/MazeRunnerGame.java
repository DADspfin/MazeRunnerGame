package de.tum.cit.fop.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.fop.maze.com.game.utils.AnimationUtils;
import de.tum.cit.fop.maze.game.objects.Player;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;

/**
 * The MazeRunnerGame class represents the core of the Maze Runner game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class MazeRunnerGame extends Game {
    // Screens
    private MenuScreen menuScreen;
    private GameScreen gameScreen;

    // Sprite Batch for rendering
    private SpriteBatch spriteBatch;

    // UI Skin
    private Skin skin;

    // Player object
    private Player player;

    private Animation<TextureRegion> characterDownAnimation;
    private Animation<TextureRegion> characterRightAnimation;
    private Animation<TextureRegion> characterUpAnimation;
    private Animation<TextureRegion> characterLeftAnimation;

    /**
     * Constructor for MazeRunnerGame.
     *
     * @param fileChooser The file chooser for the game, typically used in desktop environment.
     */
    public MazeRunnerGame(NativeFileChooser fileChooser) {
        super();
    }

    /**
     * Called when the game is created. Initializes the SpriteBatch and Skin.
     */
    @Override
    public void create() {
        spriteBatch = new SpriteBatch(); // Create SpriteBatch
        skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json")); // Load UI skin
        this.loadCharacterAnimation(); // Load character animation

        // Initialize the player object
        player = new Player(100, 100, 32, 32); // Position at (100, 100), size 32x32

        // Play some background music
        Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();

        goToMenu(); // Navigate to the menu screen

        characterDownAnimation = AnimationUtils.createAnimationFromRow(
                "character.png", 4, 8, 0, 4, 0.1f // Row 0, 4 frames in this row
        );
        characterRightAnimation = AnimationUtils.createAnimationFromRow(
                "character.png", 4, 8, 1, 4, 0.1f // Row 1, 4 frames in this row
        );
        characterUpAnimation = AnimationUtils.createAnimationFromRow(
                "character.png", 4, 8, 2, 4, 0.1f // Row 2, 4 frames in this row
        );
        characterLeftAnimation = AnimationUtils.createAnimationFromRow(
                "character.png", 4, 8, 3, 4, 0.1f // Row 3, 4 frames in this row
        );
    }

    public static Animation<TextureRegion> createAnimationFromRow(
            String filePath, int[][] frameData, float frameDuration) {
        Texture spritesheet = new Texture(Gdx.files.internal(filePath));
        TextureRegion[] frames = new TextureRegion[frameData.length];

        // Extract frames manually based on the provided coordinates
        for (int i = 0; i < frameData.length; i++) {
            int x = frameData[i][0];
            int y = frameData[i][1];
            int width = frameData[i][2];
            int height = frameData[i][3];
            frames[i] = new TextureRegion(spritesheet, x, y, width, height);
        }

        return new Animation<>(frameDuration, frames);
    }


    /**
     * Switches to the menu screen.
     */
    public void goToMenu() {
        this.setScreen(new MenuScreen(this)); // Set the current screen to MenuScreen
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }

    /**
     * Switches to the game screen.
     */
    public void goToGame() {
        this.setScreen(new GameScreen(this, player)); // Pass the player object to the GameScreen
        if (menuScreen != null) {
            menuScreen.dispose(); // Dispose the menu screen if it exists
            menuScreen = null;
        }
    }

    /**
     * Loads the character animation from the character.png file.
     */
    private void loadCharacterAnimation() {
        Texture characterTexture = new Texture(Gdx.files.internal("character.png"));

        int frameWidth = 68; // Adjust these values based on the sprite dimensions
        int frameHeight = 32;
        int frameCols = 4;
        int frameRows = 1; // Only 1 row for downward animation

        // Split the sprite sheet
        TextureRegion[][] frames = TextureRegion.split(characterTexture, frameWidth, frameHeight);

        // Extract frames for the downward animation
        Array<TextureRegion> walkFrames = new Array<>(frameCols);
        for (int i = 0; i < frameCols; i++) {
            walkFrames.add(frames[0][i]); // 0 is the row index
        }

        // Create animation
        characterDownAnimation = new Animation<>(0.1f, walkFrames);
    }

    /**
     * Cleans up resources when the game is disposed.
     */
    @Override
    public void dispose() {
        getScreen().hide(); // Hide the current screen
        getScreen().dispose(); // Dispose the current screen
        spriteBatch.dispose(); // Dispose the spriteBatch
        skin.dispose(); // Dispose the skin
    }

    // Getter methods
    public Skin getSkin() {
        return skin;
    }


    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public Player getPlayer() {
        return player;
    }
}