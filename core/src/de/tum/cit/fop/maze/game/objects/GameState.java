package de.tum.cit.fop.maze.game.objects;

//use to track the number of keys (as well as other objects such as collectable lives and power-ups) that should be on the map; for example if the player returns to main menu and come back, the same key should not render the again on the map
public class GameState {
    private boolean[] keysCollected;

    public GameState(int numKeys) {
        keysCollected = new boolean[numKeys];
    }

    public void collectKey(int keyIndex) {
        keysCollected[keyIndex] = true;
    }

    public boolean isKeyCollected(int keyIndex) {
        return keysCollected[keyIndex];
    }

    public boolean[] getKeysCollected() {
        return keysCollected;
    }

    public void setKeysCollected(boolean[] keysCollected) {
        this.keysCollected = keysCollected;
    }
}
