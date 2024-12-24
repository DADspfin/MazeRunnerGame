package de.tum.cit.fop.maze.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.fop.maze.GameScreen;
import de.tum.cit.fop.maze.com.game.utils.AnimationUtils;

import java.util.HashMap;
import java.util.Map;

public class Player extends DynamicGameObject {
    private float speed = 100f;
    private float runMultiplier = 2.0f;
    private int hearts = 10; // Player's lives (hearts)
    private int keys = 0; // Количество собранных ключей
    private Map<String, Boolean> activePowerUps; // To track activated power-ups
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> walkLeftAnimation;
    private Animation<TextureRegion> walkRightAnimation;
    private Animation<TextureRegion> walkUpAnimation;
    private Animation<TextureRegion> walkDownAnimation;
    private Animation<TextureRegion> currentAnimation;
    private float stateTime;
    private final GameScreen screen;
    public static final int IDLE = 0;
    public static final int WALKING_LEFT = 1;
    public static final int WALKING_RIGHT = 2;
    public static final int WALKING_DOWN = 3;
    public static final int WALKING_UP = 4;
    private int currentDirection = IDLE;
    private boolean isGameOver = false;

    public Player(float x, float y, float width, float height, GameScreen screen) {
        super(x, y, width, height, "character.png");
        this.screen = screen;
        initializeAnimations("character.png");
    }

    public void initializeAnimations(String texturePath) {
        float frameDuration = 0.1f;
        idleAnimation = AnimationUtils.createAnimationFromCoordinates(texturePath, new int[][]{{0, 0, 18, 26}}, frameDuration);
        walkLeftAnimation = AnimationUtils.createAnimationFromRow(texturePath, 17, 8, 3, 3, frameDuration);
        walkRightAnimation = AnimationUtils.createAnimationFromRow(texturePath, 16, 8, 1, 3, frameDuration);
        walkUpAnimation = AnimationUtils.createAnimationFromRow(texturePath, 17, 8, 2, 3, frameDuration);
        walkDownAnimation = AnimationUtils.createAnimationFromRow(texturePath, 17, 8, 0, 3, frameDuration);
    }

    // Method to reset player data
    public void reset() {
        this.hearts = 10; // Сброс жизней
        this.keys = 0; // Сброс ключей
        this.activePowerUps.clear(); // Reset active power-ups
        this.setPosition(100, 100); // Сброс позиции
        this.velocity.set(0, 0); // Сброс скорости
        this.currentDirection = IDLE; // Установка состояния на "стояние"
        this.currentAnimation = idleAnimation; // Установка анимации на idle
        this.stateTime = 0; // Сброс времени анимации
        this.isGameOver = false; // Сброс статуса "Game Over"
    }

    // Method to pick up a key
    public void pickUpKey() {
        this.keys += 1; // Увеличиваем количество собранных ключей
        System.out.println("Key picked up! Total keys: " + this.keys);
    }

    // Method to take damage
    public void takeDamage(String damageSource, int damageAmount) {
        if (hearts > 0) {
            hearts -= damageAmount;
            if (hearts <= 0) {
                triggerGameOver();
                isGameOver = true;
            }
        }
    }

    // Game over trigger
    public void triggerGameOver() {
        GameScreen.setGameOver(true);
    }

    // Method to add lives (hearts)
    public void addLives(int amount) {
        this.hearts += amount;
        // Optionally, update HUD or display a message to the player
    }

    // Method to activate a power-up
    public void activatePowerUp(String powerUpType) {
        activePowerUps.put(powerUpType, true);
        // Apply effects for this power-up type
        if ("SpeedBoost".equals(powerUpType)) {
            // Apply speed boost
            runMultiplier = 3.0f; // Example of speed boost
        } else if ("Shield".equals(powerUpType)) {
            // Activate shield
            // Implement shield behavior (e.g., invincibility for a short time)
        }
    }

    // Getters and setters
    public int getHearts() {
        return hearts;
    }

    public Map<String, Boolean> getActivePowerUps() {
        return activePowerUps;
    }

    @Override
    public void update(float deltaTime) {
        if (isGameOver) {
            velocity.set(0, 0);
            currentDirection = IDLE;
            currentAnimation = idleAnimation;
            return;
        }

        velocity.set(0, 0);

        int horizontal = 0;
        int vertical = 0;

        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)) {
            velocity.x = -speed;
            horizontal = -1;
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
            velocity.x = speed;
            horizontal = 1;
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP)) {
            velocity.y = speed;
            vertical = 1;
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DOWN)) {
            velocity.y = -speed;
            vertical = -1;
        }

        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SHIFT_LEFT)) {
            velocity.scl(runMultiplier);
        }

        if (horizontal == 0 && vertical == 0) {
            currentDirection = IDLE;
            currentAnimation = idleAnimation;
        } else if (horizontal < 0) {
            currentDirection = WALKING_LEFT;
            currentAnimation = walkLeftAnimation;
        } else if (horizontal > 0) {
            currentDirection = WALKING_RIGHT;
            currentAnimation = walkRightAnimation;
        } else if (vertical > 0) {
            currentDirection = WALKING_UP;
            currentAnimation = walkUpAnimation;
        } else if (vertical < 0) {
            currentDirection = WALKING_DOWN;
            currentAnimation = walkDownAnimation;
        }

        super.update(deltaTime);
        stateTime += deltaTime;
    }

    public void draw(SpriteBatch batch) {
        if (currentAnimation != null) {
            batch.draw(currentAnimation.getKeyFrame(stateTime, true), position.x, position.y);
        }
    }
}
