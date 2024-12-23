package de.tum.cit.fop.maze.game.objects;

//TODO: @William still needs to create a texture rendering for the key;
//TODO: @Vincent needs to link the Player interaction with the Exit to see if the number of keys matched the requirement of the exit; this should be done through Player and Exit Classes via the Interactable interface (it won't/shouldn't conflict with William's implementation of textures)

public class Key extends StaticGameObject implements Interactable{
    private boolean isCollected;

    /**
     * Constructor for the Key object.
     *
     * @param x      Initial x-position of the key
     * @param y      Initial y-position of the key
     * @param width  Width of the key
     * @param height Height of the key
     * @param texturePath Path to the texture file
     */
    public Key(float x, float y, float width, float height, String texturePath) {
        super(x, y, width, height, texturePath);
        this.isCollected = false;
    }

    /**
     * Marks the key as collected.
     */
    public void collect() {
        this.isCollected = true;
    }

    /**
     * Checks if the key is already collected.
     *
     * @return True if collected, false otherwise.
     */
    public boolean isCollected() {
        return isCollected;
    }

    @Override
    public void interact(Player player) {
        if (!isCollected()) {
            collect();
            player.pickUpKey();
            System.out.println("Player interacted with the key and collected it!");
        }
    }
}
