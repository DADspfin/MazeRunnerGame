package de.tum.cit.fop.maze.game.objects;

import com.badlogic.gdx.Game;

public interface Interactable {
    void interact(Player player, GameState gameState);
}
