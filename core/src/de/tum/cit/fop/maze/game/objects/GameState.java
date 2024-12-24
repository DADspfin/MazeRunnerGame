package de.tum.cit.fop.maze.game.objects;

//use to track the number of keys (as well as other objects such as collectable lives and power-ups) that should be on the map; for example if the player returns to main menu and come back, the same key should not render the again on the map
public class GameState {
    private boolean[] keysCollected; // Collectable keys
    private boolean[] livesCollected; // Collectable lives
    private boolean[] powerUpsCollected; // Collectable power-ups

    public GameState(int numKeys, int numLives, int numPowerUps) {
        keysCollected = new boolean[numKeys];
        livesCollected = new boolean[numLives];
        powerUpsCollected = new boolean[numPowerUps];
    }

    public void collectKey(int keyIndex) {
        keysCollected[keyIndex] = true;
    }

    // Check if a key has been collected
    public boolean isKeyCollected(int keyIndex) {
        return keysCollected[keyIndex];
    }

    // Mark a life as collected
    public void markLifeCollected(int lifeIndex) {
        livesCollected[lifeIndex] = true;
    }

    // Check if a life has been collected
    public boolean isLifeCollected(int lifeIndex) {
        return livesCollected[lifeIndex];
    }

    // Mark a power-up as collected
    public void markPowerUpCollected(int powerUpIndex) {
        powerUpsCollected[powerUpIndex] = true;
    }

    // Check if a power-up has been collected
    public boolean isPowerUpCollected(int powerUpIndex) {
        return powerUpsCollected[powerUpIndex];
    }

    // Getters for collected items (if needed)
    public boolean[] getKeysCollected() {
        return keysCollected;
    }

    public boolean[] getLivesCollected() {
        return livesCollected;
    }

    public boolean[] getPowerUpsCollected() {
        return powerUpsCollected;
    }
}
