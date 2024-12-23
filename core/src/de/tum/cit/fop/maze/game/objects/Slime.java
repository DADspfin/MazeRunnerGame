package de.tum.cit.fop.maze.game.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.fop.maze.com.game.utils.AnimationUtils;

public class Slime extends DynamicGameObject {

    public enum SlimeState {
        STANDING,
        RUNNING_LEFT,
        RUNNING_RIGHT,
        ATTACKING,
        DAMAGED,
        DYING
    }

    private Animation<TextureRegion> standingAnimation;
    private Animation<TextureRegion> runningLeftAnimation;
    private Animation<TextureRegion> runningRightAnimation;
    private Animation<TextureRegion> attackAnimation;
    private Animation<TextureRegion> damagedAnimation;
    private Animation<TextureRegion> deathAnimation;

    private SlimeState currentState = SlimeState.STANDING;
    private float stateTime = 0f;
    private boolean isDead = false;
    private float speed = 50f; // Скорость слайма
    private Player player; // Ссылка на игрока для взаимодействия

    public Slime(float x, float y, Player player) {
        super(x, y, 32, 32, "slime.png");
        this.player = player;
        initializeAnimations();
    }

    private void initializeAnimations() {
        // Установленные значения frameDuration для каждой анимации
        standingAnimation = AnimationUtils.createAnimationFromCoordinates(
                "slime.png",
                new int[][]{
                        {77, 21, 37, 40},
                        {142, 27, 37, 30}
                },
                0.2f // Медленная анимация для стояния
        );

        runningLeftAnimation = AnimationUtils.createAnimationFromCoordinates(
                "slime.png",
                new int[][]{
                        {81, 26, 31, 28},
                        {14, 23, 39, 36}
                },
                0.1f // Быстрая анимация для бега влево
        );

        runningRightAnimation = AnimationUtils.createAnimationFromCoordinates(
                "slime.png",
                new int[][]{
                        {335, 25, 34, 31},
                        {17, 94, 28, 23}
                },
                0.1f // Быстрая анимация для бега вправо
        );

        attackAnimation = AnimationUtils.createAnimationFromCoordinates(
                "slime.png",
                new int[][]{
                        {205, 161, 36, 21},
                        {273, 155, 32, 26},
                        {333, 149, 38, 33}
                },
                0.08f // Быстрая атака
        );

        damagedAnimation = AnimationUtils.createAnimationFromCoordinates(
                "slime.png",
                new int[][]{
                        {76, 145, 36, 31},
                        {19, 145, 28, 32}
                },
                0.05f // Очень быстрая анимация получения урона
        );

        deathAnimation = AnimationUtils.createAnimationFromCoordinates(
                "slime.png",
                new int[][]{
                        {14, 287, 38, 23},
                        {72, 284, 45, 25},
                        {140, 282, 44, 29}
                },
                0.2f // Медленная анимация смерти
        );
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        stateTime += deltaTime;

        if (isDead) {
            currentState = SlimeState.DYING;
            return;
        }

        // Если игрок рядом, начинаем преследование
        if (isPlayerClose()) {
            moveToPlayer(deltaTime);
        } else {
            currentState = SlimeState.STANDING;
        }

        // Проверка столкновения с игроком для атаки
        if (checkCollisionWithPlayer()) {
            currentState = SlimeState.ATTACKING;
            player.takeDamage("Slime", 1);
        }
    }

    private boolean isPlayerClose() {
        float distance = (float) Math.sqrt(
                Math.pow(player.getX() - position.x, 2) + Math.pow(player.getY() - position.y, 2)
        );
        return distance < 100; // Например, 100 пикселей — радиус преследования
    }

    private void moveToPlayer(float deltaTime) {
        float dx = player.getX() - position.x;
        float dy = player.getY() - position.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance > 0) {
            position.x += (dx / distance) * speed * deltaTime;
            position.y += (dy / distance) * speed * deltaTime;
            currentState = dx < 0 ? SlimeState.RUNNING_LEFT : SlimeState.RUNNING_RIGHT;
        }
    }

    private boolean checkCollisionWithPlayer() {
        return Math.abs(player.getX() - position.x) < 20 && Math.abs(player.getY() - position.y) < 20;
    }

    public void takeDamage(int damage) {
        if (isDead) return;

        currentState = SlimeState.DAMAGED;
        if (damage >= 1) {
            isDead = true;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Animation<TextureRegion> currentAnimation;

        switch (currentState) {
            case RUNNING_LEFT:
                currentAnimation = runningLeftAnimation;
                break;
            case RUNNING_RIGHT:
                currentAnimation = runningRightAnimation;
                break;
            case ATTACKING:
                currentAnimation = attackAnimation;
                break;
            case DAMAGED:
                currentAnimation = damagedAnimation;
                break;
            case DYING:
                currentAnimation = deathAnimation;
                break;
            case STANDING:
            default:
                currentAnimation = standingAnimation;
                break;
        }

        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, position.x, position.y);
    }
}
