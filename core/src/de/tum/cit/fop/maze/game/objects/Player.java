package de.tum.cit.fop.maze.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;

public class Player extends DynamicGameObject {
    private float speed = 100f; // Normal walking speed
    private float runMultiplier = 2.0f; // Running speed multiplier
    private int hearts = 5; // Player's health
    private long lastDamageTime; // Time of the last damage
    private final long damageCooldown = 1000; // Cooldown in milliseconds for damage
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> walkLeftAnimation;
    private Animation<TextureRegion> walkRightAnimation;
    private Animation<TextureRegion> walkUpAnimation;
    private Animation<TextureRegion> walkDownAnimation;
    private Animation<TextureRegion> currentAnimation;

    private float stateTime; // Tracks the time for animation frames

    // Direction constants
    public static final int IDLE = 0;
    public static final int WALKING_LEFT = 1;
    public static final int WALKING_RIGHT = 2;
    public static final int WALKING_DOWN = 3;
    public static final int WALKING_UP = 4;

    private int currentDirection = IDLE; // Default: IDLE

    public Animation<TextureRegion> getIdleAnimation() {
        return idleAnimation;
    }

    public void setIdleAnimation(Animation<TextureRegion> idleAnimation) {
        this.idleAnimation = idleAnimation;
    }

    public Animation<TextureRegion> getWalkLeftAnimation() {
        return walkLeftAnimation;
    }

    public void setWalkLeftAnimation(Animation<TextureRegion> walkLeftAnimation) {
        this.walkLeftAnimation = walkLeftAnimation;
    }

    public Animation<TextureRegion> getWalkRightAnimation() {
        return walkRightAnimation;
    }

    public void setWalkRightAnimation(Animation<TextureRegion> walkRightAnimation) {
        this.walkRightAnimation = walkRightAnimation;
    }

    public Animation<TextureRegion> getWalkUpAnimation() {
        return walkUpAnimation;
    }

    public void setWalkUpAnimation(Animation<TextureRegion> walkUpLeftAnimation) {
        this.walkUpAnimation = walkUpLeftAnimation;
    }

    public Animation<TextureRegion> getWalkDownAnimation() {
        return walkDownAnimation;
    }

    public void setWalkUpRightAnimation(Animation<TextureRegion> walkDownAnimation) {
        this.walkDownAnimation = walkDownAnimation;
    }

    public int getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(int currentDirection) {
        this.currentDirection = currentDirection;
    }

    public long getDamageCooldown() {
        return damageCooldown;
    }

    public long getLastDamageTime() {
        return lastDamageTime;
    }

    public void setLastDamageTime(long lastDamageTime) {
        this.lastDamageTime = lastDamageTime;
    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
    }

    public float getRunMultiplier() {
        return runMultiplier;
    }

    public void setRunMultiplier(float runMultiplier) {
        this.runMultiplier = runMultiplier;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * Constructor to initialize the player object.
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public Player(float x, float y, float width, float height) {
        super(x, y, width, height, "character.png");
        this.texture = new Texture(Gdx.files.internal("character.png"));
        lastDamageTime = 0;
        int frameWidth = 32;
        int frameHeight = 32;
        int frameCount = 6; // Number of frames for each animation


        // Create animations from sprite sheet frames
        idleAnimation = createAnimation(0, 0, frameWidth, frameHeight, frameCount); // Idle
        walkLeftAnimation = createAnimation(0, frameHeight, frameWidth, frameHeight, frameCount); // Walk left
        walkRightAnimation = createAnimation(0, 2 * frameHeight, frameWidth, frameHeight, frameCount); // Walk right
        walkUpAnimation = createAnimation(0, 3 * frameHeight, frameWidth, frameHeight, frameCount); // Walk up
        walkDownAnimation = createAnimation(0, 4 * frameHeight, frameWidth, frameHeight, frameCount); // Walk down
    }

    /**
     * creating animation for the player
     * @param x
     * @param y
     * @param frameWidth
     * @param frameHeight
     * @param frameCount
     * @return
     */
    private Animation<TextureRegion> createAnimation(int x, int y, int frameWidth, int frameHeight, int frameCount) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = new TextureRegion(texture, x + i * frameWidth, y, frameWidth, frameHeight);
        }
        return new Animation<>(0.1f, frames); // Adjust speed as necessary
    }


    /**
     * update the direction the player is taking
     * @param horizontal
     * @param vertical
     */
    public void updateDirection(int horizontal, int vertical) {
        if (horizontal == 0 && vertical == 0) {
            currentDirection = IDLE;  // No keys pressed
        } else if (horizontal < 0 && vertical == 0) {
            currentDirection = WALKING_LEFT;
        } else if (horizontal > 0 && vertical == 0) {
            currentDirection = WALKING_RIGHT;
        } else if (vertical > 0) {
            currentDirection = WALKING_UP;
        } else if (vertical < 0) {
            currentDirection = WALKING_DOWN;
        }
    }

    private void setCurrentAnimation(Animation<TextureRegion> animation) {
        // Set the animation to be drawn in the render method
        currentAnimation = animation;
    }

    public void draw(SpriteBatch batch) {
        // Draw the current frame of the animation
        batch.draw(currentAnimation.getKeyFrame(stateTime, true), position.x, position.y);
    }

    /**
     * Updates the player's position based on input and applies running logic.
     *
     * @param deltaTime Time elapsed since the last update (used for smooth movement).
     */
    @Override
    public void update(float deltaTime) {
        // Reset velocity to zero
        velocity.set(0, 0);

        // Check for movement inputs
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)) {
            velocity.x = -speed;
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
            velocity.x = speed;
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP)) {
            velocity.y = speed;
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DOWN)) {
            velocity.y = -speed;
        }

        // Apply running multiplier if Shift is held
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SHIFT_LEFT)) {
            velocity.scl(runMultiplier);
        }

        // Move the player
        super.update(deltaTime);
    }

    /**
     * Renders the player using the given SpriteBatch.
     *
     * @param batch The SpriteBatch to draw the player.
     */
    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }

    /**
     * Reduces the player's health when damaged by an enemy.
     */
    public void takeDamage() {
        // Apply damage cooldown to avoid rapid health depletion
        if (TimeUtils.timeSinceMillis(lastDamageTime) > damageCooldown) {
            hearts -= 1; // Reduce health by half a heart (2 damage for 1 full heart)
            lastDamageTime = TimeUtils.millis(); // Update last damage time
        }
    }

    /**
     * Checks if the player is alive.
     *
     * @return True if the player still has hearts, otherwise false.
     */
    public boolean isAlive() {
        return hearts > 0;
    }

    /**
     * Gets the player's remaining hearts.
     *
     * @return The number of hearts left.
     */
    public int getHearts() {
        return hearts;
    }
}
