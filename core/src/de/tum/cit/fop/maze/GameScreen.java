package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import de.tum.cit.fop.maze.game.objects.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final OrthographicCamera camera;
    private final BitmapFont font;
    private SpriteBatch batch;

    private float sinusInput = 0f;
    private float stateTime = 0f;

    // Maze-related fields
    private Map<String, Integer> maze;
    private Texture tilesheet;
    private TextureRegion wallTexture;
    private List<Key> keys = new ArrayList<>();
    private List<CollectableLives> lives = new ArrayList<>();
    private List<CollectablePowerUp> powerUps = new ArrayList<>();
    private GameState gameState;

    // Player and slimes
    private final Player player;
    private final List<Slime> slimes;

    // Game flags
    private static boolean gameOver = false;
    private int indexForKey = 0;

    /**
     * Constructor for GameScreen. Sets up the camera, font, maze, player, and slimes.
     *
     * @param game   The main game class.
     * @param player The player object.
     */
    public GameScreen(MazeRunnerGame game, Player player) {
        this.game = game;
        this.player = player;

        // Initialize camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        // Initialize font and sprite batch
        font = game.getSkin().getFont("font");
        batch = game.getSpriteBatch();

        // Load the maze
        String relativePath = "maps/level-1.properties"; // Relative path from the project root
        String basePath = System.getProperty("user.dir"); // Current working directory
        String fullPath = basePath + File.separator + relativePath;
        maze = MazeLoader.loadMaze(fullPath);

        if (maze == null || maze.isEmpty()) {
            throw new IllegalStateException("Maze could not be loaded or is empty!");
        }

        // Load the tilesheet and extract the top-left corner tile for walls
        tilesheet = new Texture("basictiles.png");
        wallTexture = new TextureRegion(tilesheet, 0, 0, 16, 16);

        // Center the camera
        centerCameraOnMaze();

        // Initialize player
        this.player.setPosition(100, 100);

        // Initialize slimes
        slimes = new ArrayList<>();
        slimes.add(new Slime(200, 200, player));
        slimes.add(new Slime(300, 400, player));

        // Initialize game state by retrieving it from the game
        this.gameState = game.getGameState(); // Get the existing game state instance
    }

    private int getMazeWidth() {
        return maze.keySet().stream()
                .mapToInt(key -> Integer.parseInt(key.split(",")[0]))
                .max()
                .orElse(0) + 1;
    }

    private int getMazeHeight() {
        return maze.keySet().stream()
                .mapToInt(key -> Integer.parseInt(key.split(",")[1]))
                .max()
                .orElse(0) + 1;
    }

    private void centerCameraOnMaze() {
        int mazeWidth = getMazeWidth();
        int mazeHeight = getMazeHeight();

        camera.position.set(
                (mazeWidth * Gdx.graphics.getWidth() / mazeWidth) / 2f,
                (mazeHeight * Gdx.graphics.getHeight() / mazeHeight) / 2f,
                0
        );

        camera.update();
    }

    public static void setGameOver(boolean state) {
        gameOver = state;
    }

    /**
     * Renders the maze, player, slimes, and key.
     *
     * @param delta Time elapsed since the last frame.
     */
    @Override
    public void render(float delta) {
        if (gameOver) {
            renderGameOverScreen(); // Game Over screen
            return; // Stop further execution
        }

        float clampedDelta = Math.min(delta, 0.1f);

        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen

        // Handle input for returning to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        camera.update();

        // Update player and slimes
        player.update(clampedDelta);
        for (Slime slime : slimes) {
            slime.update(clampedDelta);
        }

        // Check for player interaction with the key
        if (!keys.get(indexForKey).isCollected() && keys.get(indexForKey).checkCollision(player)) {
            keys.get(indexForKey).interact(player, gameState); // Pass the gameState to key interaction
        }

        stateTime += clampedDelta;

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Render the maze
        renderMaze();

        // Render the player
        player.draw(batch);

        // Render the slimes
        for (Slime slime : slimes) {
            slime.render(batch);
        }

        // Render the key using GameState logic
        keys.get(indexForKey).render(batch, gameState);

        // Render text
        font.draw(batch, "Press ESC to go to menu", camera.position.x + 320, camera.position.y + 410);

        batch.end();
    }

    public void renderGameOverScreen() {
        SpriteBatch batch = new SpriteBatch();
        BitmapFont bigFont = new BitmapFont(); // Replace with custom font if needed
        BitmapFont smallFont = new BitmapFont(); // Replace with custom font if needed
        bigFont.getData().setScale(2.5f); // Scale up for big text
        smallFont.getData().setScale(1.2f); // Scale down for smaller text

        ScreenUtils.clear(0, 0, 0, 1); // Очистка экрана
        batch.begin();
        font.draw(batch, "Game Over! Press R to start again your journey.", camera.position.x - 200, camera.position.y + 20);
        font.draw(batch, "Press ESC to return to the menu.", camera.position.x - 200, camera.position.y - 20);
        batch.end();

        // Возврат в меню по нажатию клавиши ESC
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        // Перезапуск уровня по нажатию клавиши R
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            restartGame();
        }
    }

    //restart game is not working properly now after the player died, we need to fix it: it has something to do with Collectable lives and powerUps (both unfinished currently); also clear() method not working properly
    private void restartGame() {
        gameOver = false; // Reset Game Over state
        player.reset(); // Reset player state

        // Recreate the slimes
        slimes.clear();
        slimes.add(new Slime(200, 200, player));
        slimes.add(new Slime(300, 400, player));

        for (Key key : keys) {
            key.dispose();
        }

        // Reset keys
        //keys.clear(); //TODO: for each new game we should have cleared the keys, but right now whenever this method is called the game crashes. So I will solve it later
        keys.add(new Key(500, 300, 128, 32, "assets/keyIcons.png", indexForKey)); // Add new keys
        indexForKey++;
        keys.add(new Key(1000, 500, 128, 32, "assets/keyIcons.png", indexForKey));

        // Reset GameState
        // Reinitialize the keysCollected array based on the number of keys
        gameState = new GameState(keys.size(), lives.size(), powerUps.size());  // Create new GameState with correct number of keys
        System.out.println("Game reset with " + keys.size() + " keys.");

        // Optionally, reset the keysCollected array if needed (if you want to reset the collected status)
        boolean[] newKeysCollected = new boolean[keys.size()];
    }

    private void renderMaze() {
        int mazeWidth = getMazeWidth();
        int mazeHeight = getMazeHeight();

        // Dynamically scale each tile to fit the screen
        float tileWidth = Gdx.graphics.getWidth() / (float) mazeWidth;
        float tileHeight = Gdx.graphics.getHeight() / (float) mazeHeight;

        for (Map.Entry<String, Integer> entry : maze.entrySet()) {
            String[] coordinates = entry.getKey().split(",");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            int objectType = entry.getValue();

            if (objectType == 0) { // Wall
                batch.draw(wallTexture, x * tileWidth, y * tileHeight, tileWidth, tileHeight);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        // Make sure to provide the width, height, and texturePath
        //TODO: VINCENT please redo this part later because the key should have different spawnpoints in  different levels

        if (keys.isEmpty()) {
            keys.add(new Key(500, 300, 128, 32, "assets/keyIcons.png", indexForKey));
            indexForKey++;
            keys.add(new Key(1000, 500, 128, 32, "assets/keyIcons.png", indexForKey));
        }
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        if (player != null) {
            player.dispose();
        }
        for (Slime slime : slimes) {
            if (slime != null) {
                slime.dispose();
            }
        }
        for (Key key : keys) {
            key.dispose();
        }
        if (batch != null) {
            batch.dispose();
        }
    }
}
