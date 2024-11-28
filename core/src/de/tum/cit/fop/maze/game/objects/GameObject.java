package de.tum.cit.fop.maze.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;



public abstract class GameObject {
    protected Vector2 position;
    protected float width, height;
    protected Texture texture;

    /**
     * Constructor to initialize a game object.
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param texturePath
     */
    public GameObject(float x, float y, float width, float height, String texturePath) {
        this.position = new Vector2(x, y);
        this.width = width;
        this.height = height;
        this.texture = new Texture(texturePath);
    }

    /**
     * define how objects change over time
     * @param deltaTime
     */
    public abstract void update(float deltaTime);

    /**
     * draw object's texture at specified position and size
     * @param batch
     */
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y, width, height);
    }

    /**
     * dispose object when no longer needed
     */
    public void dispose() {
        texture.dispose();
    }
}
