package de.tum.cit.fop.maze.game.objects;

//TODO: @William still needs to create a texture rendering for the key;
//TODO: @Vincent needs to link the Player interaction with the Exit to see if the number of keys matched the requirement of the exit; this should be done through Player and Exit Classes via the Interactable interface (it won't/shouldn't conflict with William's implementation of textures)

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Key extends StaticGameObject implements Interactable {
    private float x, y;
    private boolean isCollected;
    private Texture keyTexture;
    private Sprite keySprite;
    private final int keyIndex;

    private boolean disposed = false;

    /**
     * Constructor for the Key object.
     *
     * @param x      Initial x-position of the key
     * @param y      Initial y-position of the key
     * @param width  Width of the key
     * @param height Height of the key
     * @param texturePath Path to the texture file
     */
    public Key(float x, float y, float width, float height, String texturePath, int keyIndex) {
        // Call the constructor of the superclass StaticGameObject
        super(x, y, width, height, texturePath);
        this.isCollected = false;

        // Load the texture from the sprite sheet
        keyTexture = new Texture(Gdx.files.internal(texturePath)); // Use the texturePath passed in

        // Create a TextureRegion (assuming the key is in a single frame)
        TextureRegion keyRegion = new TextureRegion(keyTexture, 0, 0, 33, 32); // Adjust to match key's position and size in the sprite sheet
        keySprite = new Sprite(keyRegion);

        // Set the position of the sprite
        setPosition(x, y);

        this.keyIndex = keyIndex;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        keySprite.setPosition(x, y);
    }

    // Render method to draw the key sprite
    public void render(SpriteBatch batch, GameState gameState) {
        if (!gameState.isKeyCollected(keyIndex)) {
            if (keySprite != null && keySprite.getTexture() != null) {
                keySprite.draw(batch);
            } else {
                System.err.println("Key sprite or texture is null during rendering!");
            }
        }
    }

    // Dispose of resources when the key is no longer needed
    public void dispose() {
        if (!disposed) {
            keyTexture.dispose();
            disposed = true;
        }
    }

    // Marks the key as collected
    public void collect() {
        this.isCollected = true;
        this.dispose();
    }

    // Checks if the key is collected
    public boolean isCollected() {
        return isCollected;
    }

    /**
     * Checks if the key collides with the player.
     *
     * @param player The player object to check collision against.
     * @return True if the player collides with the key, false otherwise.
     */
    public boolean checkCollision(Player player) {
        return x < player.getX() + player.getWidth() &&
                x + getWidth() > player.getX() &&
                y < player.getY() + player.getHeight() &&
                y + getHeight() > player.getY();
    }

    @Override
    public void interact(Player player, GameState gameState) {
        if (!gameState.isKeyCollected(keyIndex)) {
            gameState.collectKey(keyIndex);
            player.pickUpKey();
            System.out.println("Player interacted with the key and collected it!");
            System.out.println("Key index: " + keyIndex);
        }
    }
}