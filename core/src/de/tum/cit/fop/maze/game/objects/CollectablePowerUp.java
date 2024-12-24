package de.tum.cit.fop.maze.game.objects;

public class CollectablePowerUp extends StaticGameObject implements Interactable {
    private final int index;
    private String powerUpType; // Example: "SpeedBoost", "Shield"

    public CollectablePowerUp(float x, float y, float width, float height, String texturePath, String powerUpType, int index) {
        super(x, y, width, height, texturePath);
        this.powerUpType = powerUpType;
        this.index = index;
    }

    @Override
    public void interact(Player player, GameState gameState) {
        // Apply power-up effect to the player
        player.activatePowerUp(powerUpType);
        // Mark this item as collected in the game state
        gameState.markPowerUpCollected(this.index);
        // Optionally, trigger power-up effects or sounds here
    }

    public String getPowerUpType() {
        return powerUpType;
    }
}
