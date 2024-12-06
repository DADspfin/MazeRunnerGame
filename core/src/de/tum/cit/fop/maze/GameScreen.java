package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import de.tum.cit.fop.maze.game.objects.Player;
import de.tum.cit.fop.maze.game.objects.Slime;

import java.util.ArrayList;
import java.util.List;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game; // Reference to the main game class
    private final OrthographicCamera camera; // Camera to view the game world
    private final BitmapFont font; // Font for rendering text
    private final Player player; // Reference to the player object
    private final List<Slime> slimes; // List of slimes in the game
    private float stateTime = 0f; // Keeps track of animation timing

    /**
     * Constructor for GameScreen. Initializes the camera, player, font, and slimes.
     *
     * @param game   The main game class.
     * @param player The player object.
     */
    public GameScreen(MazeRunnerGame game, Player player) {
        this.game = game;
        this.player = player;

        camera = new OrthographicCamera();
        camera.setToOrtho(false); // Set the camera's projection to 2D
        camera.zoom = 0.75f; // Adjust the zoom level

        font = game.getSkin().getFont("font"); // Load font from skin
        this.player.setPosition(100, 100); // Set player's initial position

        slimes = new ArrayList<>();
        slimes.add(new Slime(200, 200)); // Add a slime at position (200, 200)
        slimes.add(new Slime(300, 400)); // Add another slime at position (300, 400)
    }

    /**
     * Renders the game screen and handles updates for the player, slimes, and camera.
     *
     * @param delta Time elapsed since the last frame.
     */
    @Override
    public void render(float delta) {
        float clampedDelta = Math.min(delta, 0.1f); // Prevent large delta values

        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen with a black color

        // Check for ESC key to return to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu(); // Return to the main menu
        }

        camera.update(); // Update the camera
        player.update(clampedDelta); // Update the player's position and animation

        // Update all slimes
        for (Slime slime : slimes) {
            slime.update(clampedDelta, player);
        }

        stateTime += clampedDelta; // Update animation timing

        SpriteBatch batch = game.getSpriteBatch(); // Get the SpriteBatch for rendering
        batch.setProjectionMatrix(camera.combined); // Set the batch's view to the camera

        batch.begin();

        // Render the player
        player.draw(batch);

        // Render each slime
        for (Slime slime : slimes) {
            slime.render(batch);
        }

        // Render text (optional)
        font.draw(batch, "Press ESC to go to menu", camera.position.x - 100, camera.position.y + 200);

        batch.end();
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
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        player.dispose(); // Dispose player resources
        for (Slime slime : slimes) {
            slime.dispose(); // Dispose each slime's resources
        }
    }
}
