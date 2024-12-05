package de.tum.cit.fop.maze.game.objects;

import com.badlogic.gdx.math.Vector2;

public class DynamicGameObject extends GameObject {
    protected Vector2 velocity;

    /**
     * Constructor to initialize a dynamic game object.
     *
     * @param x The x-coordinate of the object.
     * @param y The y-coordinate of the object.
     * @param width The width of the object.
     * @param height The height of the object.
     * @param texturePath The file path for the texture.
     */
    public DynamicGameObject(float x, float y, float width, float height, String texturePath) {
        super(x, y, width, height, texturePath);
        this.velocity = new Vector2(0, 0);
    }

    /**
     * move method for the basic movements of the dynamic object
     * @param deltaX
     * @param deltaY
     */
    public void move(float deltaX, float deltaY) {
        position.add(deltaX, deltaY);
    }

    @Override
    public void update(float deltaTime) {
        position.mulAdd(velocity, deltaTime);
    }

    @Override
    public void setPosition(int i, int i1) {
        this.position.set(i, i1);
    }

}
