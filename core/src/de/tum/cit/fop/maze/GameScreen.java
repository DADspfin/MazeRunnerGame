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
import de.tum.cit.fop.maze.game.objects.Player;
import de.tum.cit.fop.maze.game.objects.Slime;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final OrthographicCamera camera;
    private final BitmapFont font;
    private final SpriteBatch batch;

    private float stateTime = 0f;

    // Maze-related fields
    private Map<String, Integer> maze;
    private Texture tilesheet;
    private TextureRegion wallTexture;

    // Player and slimes
    private final Player player;
    private final List<Slime> slimes;

    // Game flags
    private static boolean gameOver = false;

    public GameScreen(MazeRunnerGame game, Player player) {
        this.game = game;
        this.player = player;

        // Initialize the camera
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

        // Center the camera based on maze dimensions
        centerCameraOnMaze();

        // Initialize the player
        this.player.setPosition(100, 100);

        // Initialize slimes
        slimes = new ArrayList<>();
        slimes.add(new Slime(200, 200, player)); // Pass player reference
        slimes.add(new Slime(300, 400, player)); // Pass player reference
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

    @Override
    public void render(float delta) {
        if (gameOver) {
            renderGameOverScreen(); // Отдельный экран для состояния "Game Over"
            return; // Останавливаем дальнейшее выполнение
        }

        float clampedDelta = Math.min(delta, 0.1f);

        ScreenUtils.clear(0, 0, 0, 1);

        // Обновление камеры
        camera.update();

        // Обновление игрока и слаймов
        player.update(clampedDelta);
        for (Slime slime : slimes) {
            slime.update(clampedDelta);
        }

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Рендеринг элементов игры
        renderMaze();
        player.draw(batch);
        for (Slime slime : slimes) {
            slime.render(batch);
        }

        font.draw(batch, "Press ESC to go to menu", camera.position.x + 320, camera.position.y + 410);

        batch.end();
    }

    public void renderGameOverScreen() {
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

    private void restartGame() {
        gameOver = false; // Сброс состояния Game Over
        player.reset(); // Сброс состояния игрока

        // Пересоздание слаймов
        slimes.clear();
        slimes.add(new Slime(200, 200, player));
        slimes.add(new Slime(300, 400, player));
    }

    private void renderMaze() {
        int mazeWidth = getMazeWidth();
        int mazeHeight = getMazeHeight();

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
        if (batch != null) {
            batch.dispose();
        }
    }
}
