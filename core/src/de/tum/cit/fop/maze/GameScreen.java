package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.fop.maze.game.objects.Player;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final OrthographicCamera camera;
    private final BitmapFont font;
    private final Player player; // Reference to the player object
    private float stateTime = 0f; // Keep track of animation timing


    /**
     * Constructor for GameScreen. Sets up the camera, font, and player.
     *
     * @param game   The main game class, used to access global resources and methods.
     * @param player
     */
    public GameScreen(MazeRunnerGame game, Player player) {
        this.game = game;
        this.player = player;

        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = 0.75f;

        // Get the font from the game's skin
        font = game.getSkin().getFont("font");

        this.player.setPosition(100, 100); // Only if default positioning is required
    }

    @Override
    public void render(float delta) {
//        float clampedDelta = Math.min(delta, 0.1f); // Cap the delta time to avoid instability
//        player.update(clampedDelta);

        //Gdx.app.log("Render", "Frame rendered with delta: " + delta);

        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        // Clear the screen
        ScreenUtils.clear(0, 0, 0, 1);

        // Update the camera
        camera.update();

        // Update the player's state (movement and direction handled here)
        player.update(delta);

        // Update state time for animation
        stateTime += delta;

        // Set the SpriteBatch's projection matrix to the camera's view
        game.getSpriteBatch().setProjectionMatrix(camera.combined);

        // Begin drawing with the sprite batch
        game.getSpriteBatch().begin();

        // Render the correct animation based on the player's direction
        switch (player.getCurrentDirection()) {
            case Player.IDLE:
                game.getSpriteBatch().draw(player.getIdleAnimation().getKeyFrame(stateTime, true),
                        player.getX(), player.getY(), player.getWidth(), player.getHeight());
                break;
            case Player.WALKING_LEFT:
                game.getSpriteBatch().draw(player.getWalkLeftAnimation().getKeyFrame(stateTime, true),
                        player.getX(), player.getY(), player.getWidth(), player.getHeight());
                break;
            case Player.WALKING_RIGHT:
                game.getSpriteBatch().draw(player.getWalkRightAnimation().getKeyFrame(stateTime, true),
                        player.getX(), player.getY(), player.getWidth(), player.getHeight());
                break;
            case Player.WALKING_DOWN:
                game.getSpriteBatch().draw(player.getWalkDownAnimation().getKeyFrame(stateTime, true),
                        player.getX(), player.getY(), player.getWidth(), player.getHeight());
                break;
            case Player.WALKING_UP:
                game.getSpriteBatch().draw(player.getWalkUpAnimation().getKeyFrame(stateTime, true),
                        player.getX(), player.getY(), player.getWidth(), player.getHeight());
                break;
            default:
                // Handle unexpected states with idle animation
                game.getSpriteBatch().draw(player.getIdleAnimation().getKeyFrame(stateTime, true),
                        player.getX(), player.getY(), player.getWidth(), player.getHeight());
                System.out.println("Default case triggered: Using Idle Animation");
                break;
        }

        // Render some text
        font.draw(game.getSpriteBatch(), "Press ESC to go to menu", camera.position.x - 100, camera.position.y + 200);

        // End drawing
        game.getSpriteBatch().end();
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
        player.dispose(); // Dispose of player resources
    }
}
