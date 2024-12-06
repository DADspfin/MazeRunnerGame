package de.tum.cit.fop.maze.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Slime {
    private float x, y; // Slime's position
    private float speed = 50f; // Movement speed
    private boolean chasing = false; // Whether the slime is chasing the player
    private Texture slimeTexture; // Texture for rendering the slime

    /**
     * Constructor for Slime.
     *
     * @param startX The initial x-position of the slime.
     * @param startY The initial y-position of the slime.
     */
    public Slime(float startX, float startY) {
        this.x = startX;
        this.y = startY;

        // Load the slime texture
        slimeTexture = new Texture("slime.png"); // Ensure slime.png is in the assets folder
    }

    /**
     * Updates the slime's behavior (e.g., moving and checking collision with the player).
     *
     * @param deltaTime Time elapsed since the last frame.
     * @param player    The player to interact with.
     */
    public void update(float deltaTime, Player player) {
        if (chasing) {
            // Move towards the player if chasing
            moveTowardsPlayer(player, deltaTime);
        } else {
            // Random movement when not chasing
            moveRandomly(deltaTime);
        }

        // Check collision with the player
        if (checkCollision(player)) {
            player.takeDamage("Slime", 1); // Deal 1 damage to the player
            chasing = true; // Slime starts chasing the player
        }
    }

    private void moveTowardsPlayer(Player player, float deltaTime) {
        float dx = player.getX() - x;
        float dy = player.getY() - y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance > 0) {
            x += (dx / distance) * speed * deltaTime;
            y += (dy / distance) * speed * deltaTime;
        }
    }

    private void moveRandomly(float deltaTime) {
        // Simple random movement
        x += Math.random() * speed * deltaTime - speed * deltaTime / 2;
        y += Math.random() * speed * deltaTime - speed * deltaTime / 2;
    }

    /**
     * Checks whether the slime collides with the player.
     *
     * @param player The player object.
     * @return True if the slime and player collide; otherwise, false.
     */
    public boolean checkCollision(Player player) {
        return Math.abs(x - player.getX()) < 32 && Math.abs(y - player.getY()) < 32;
    }

    /**
     * Renders the slime on the screen.
     *
     * @param batch The SpriteBatch used for rendering.
     */
    public void render(SpriteBatch batch) {
        batch.draw(slimeTexture, x, y); // Draw the slime at its position
    }

    /**
     * Disposes of the slime's resources.
     */
    public void dispose() {
        if (slimeTexture != null) {
            slimeTexture.dispose();
        }
    }

    // Getters for slime position
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
