package de.tum.cit.fop.maze.game.objects;

public class CollectableLives extends StaticGameObject implements Interactable {
    private final int index;
    private static final int LIVES_INCREMENT = 1; // Number of lives added when collected

    public CollectableLives(float x, float y, float width, float height, String texturePath, int index) {
        super(x, y, width, height, texturePath);
        this.index = index;
    }

    @Override
    public void interact(Player player, GameState gameState) {
        // Add lives to player
        player.addLives(LIVES_INCREMENT);
        // Mark this item as collected in the game state
        gameState.markLifeCollected(this.index);
        // Optionally, you could trigger some visual effects or sounds here
    }
}
