package de.tum.cit.fop.maze.game.objects;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class StaticGameObject extends GameObject {
    private Texture texture;
    private float X;
    private float Y;
    private float width;
    private float height;

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public float getX() {
        return X;
    }

    public void setX(float x) {
        X = x;
    }

    public float getY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * Constructor to initialize a static game object.
     *
     * @param x The x-coordinate of the object.
     * @param y The y-coordinate of the object.
     * @param width The width of the object.
     * @param height The height of the object.
     * @param texturePath The file path for the texture.
     */
    public StaticGameObject(float x, float y, float width, float height, String texturePath) {
        super(x, y, width, height, texturePath);
    }

    @Override
    public void update(float deltaTime) {

    }

    /**
     * Renders the static object on the screen.
     *
     * @param batch The SpriteBatch used for rendering.
     */
    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }


    /**
     * Dispose of the texture to free memory.
     */
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}
