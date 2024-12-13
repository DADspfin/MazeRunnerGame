package de.tum.cit.fop.maze.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.fop.maze.GameScreen;
import de.tum.cit.fop.maze.com.game.utils.AnimationUtils;

public class Player extends DynamicGameObject {
    private float speed = 100f; // Player's walking speed
    private float runMultiplier = 2.0f; // Running speed multiplier
    private int hearts = 10; // Player's health
    private Animation<TextureRegion> idleAnimation; // Idle animation
    private Animation<TextureRegion> walkLeftAnimation; // Walk left animation
    private Animation<TextureRegion> walkRightAnimation; // Walk right animation
    private Animation<TextureRegion> walkUpAnimation; // Walk up animation
    private Animation<TextureRegion> walkDownAnimation; // Walk down animation
    private Animation<TextureRegion> currentAnimation; // Current animation
    private float stateTime; // Tracks animation frame timing
    private final GameScreen screen;
    // Direction constants
    public static final int IDLE = 0;
    public static final int WALKING_LEFT = 1;
    public static final int WALKING_RIGHT = 2;
    public static final int WALKING_DOWN = 3;
    public static final int WALKING_UP = 4;
    private int currentDirection = IDLE; // Player's current direction

    private boolean isGameOver = false;

    /**
     * Player constructor initializes the player's position, size, and animations.
     *
     * @param x      Initial x-position of the player
     * @param y      Initial y-position of the player
     * @param width  Width of the player sprite
     * @param height Height of the player sprite
     */
    public Player(float x, float y, float width, float height, String texturePath, GameScreen screen) {
        super(x, y, width, height, texturePath);
        this.screen = screen;
        initializeAnimations(texturePath); // Shared logic for animations
    }

    public Player(float x, float y, float width, float height, GameScreen screen) {
        this(x, y, width, height, "character.png", screen); // Delegate to the main constructor
    }

    /**
     * Initializes the player's animations based on the provided texture.
     */
    public void initializeAnimations(String texturePath) {
        float frameDuration = 0.1f; // Duration of each animation frame

        idleAnimation = AnimationUtils.createAnimationFromCoordinates(
                texturePath,
                new int[][]{{0, 0, 18, 26}}, // Use only the first frame
                frameDuration
        );

        walkLeftAnimation = AnimationUtils.createAnimationFromRow(
                texturePath, 17, 8, 3, 3, frameDuration
        );

        walkRightAnimation = AnimationUtils.createAnimationFromRow(
                texturePath, 16, 8, 1, 3, frameDuration
        );

        walkUpAnimation = AnimationUtils.createAnimationFromRow(
                texturePath, 17, 8, 2, 3, frameDuration
        );

        walkDownAnimation = AnimationUtils.createAnimationFromRow(
                texturePath, 17, 8, 0, 3, frameDuration
        );

        // Placeholder for rows 4-7 (action animations)
        // Can extend this later by following a similar pattern:
        // - For regular rows, use `createAnimationFromRow`.
        // - For irregular rows, use `createAnimationFromCoordinates` with manual frame data.
    }

    /**
     * Reduces the player's health when taking damage.
     *
     * @param damageSource The source of the damage (e.g., "Slime").
     * @param damageAmount The amount of damage to take.
     */
    public void takeDamage(String damageSource, int damageAmount) {
        if (hearts > 0) {
            hearts -= damageAmount; // Reduce player's health
            System.out.println("Player took " + damageAmount + " damage from " + damageSource);
            if (hearts <= 0) {
                System.out.println("Player has died!");
                triggerGameOver();// Call the game over logic
                isGameOver = true; // Set the game over flag
            }
        }
    }


    public void triggerGameOver(){
        GameScreen.setGameOver(true); // Assuming GameScreen manages screen transitions
    }
    // Getters for the player's current direction and animations
    public int getCurrentDirection() {
        return currentDirection;
    }

    public Animation<TextureRegion> getWalkLeftAnimation() {
        return walkLeftAnimation;
    }

    public Animation<TextureRegion> getWalkRightAnimation() {
        return walkRightAnimation;
    }

    public Animation<TextureRegion> getWalkDownAnimation() {
        return walkDownAnimation;
    }

    public Animation<TextureRegion> getWalkUpAnimation() {
        return walkUpAnimation;
    }

    public Animation<TextureRegion> getIdleAnimation() {
        return idleAnimation;
    }

    // Getters for player's position and health
    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public int getHearts() {
        return hearts;
    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    /**
     * Updates the player's position and animation based on input.
     *
     * @param deltaTime Time elapsed since the last frame (used for smooth movement)
     */
    @Override
    public void update(float deltaTime) {
        if (isGameOver) {
            velocity.set(0, 0); // Stop all movement
            currentDirection = IDLE; // Set to idle state
            currentAnimation = idleAnimation; // Keep the idle animation
            return; // Skip further updates
        }

        velocity.set(0, 0); // Reset velocity

        int horizontal = 0;
        int vertical = 0;

        // Handle movement input
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

        // Apply running speed if the shift key is held
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SHIFT_LEFT)) {
            velocity.scl(runMultiplier);
        }

        // Update direction and animation based on input
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

        super.update(deltaTime); // Apply velocity to position
        stateTime += deltaTime; // Update animation timing
    }

    /**
     * Draws the player's current animation.
     *
     * @param batch The SpriteBatch used to draw the player
     */
    public void draw(SpriteBatch batch) {
        if (currentAnimation != null) {
            batch.draw(currentAnimation.getKeyFrame(stateTime, true), position.x, position.y);
        }
    }
}

