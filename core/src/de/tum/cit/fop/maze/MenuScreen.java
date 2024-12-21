package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.tum.cit.fop.maze.com.game.utils.AnimationUtils;

public class MenuScreen implements Screen {

    private final Stage stage;
    private final MazeRunnerGame game;

    private final Animation<TextureRegion> backgroundAnimation;
    private float stateTime = 0; // Время для отслеживания текущего кадра анимации

    public MenuScreen(MazeRunnerGame game) {
        this.game = game;

        // Создаем анимацию
        backgroundAnimation = AnimationUtils.createAnimationFromRow(
                "menubackground.png", // Ваш спрайт-лист
                3, 2,                 // 3 столбца, 2 строки
                0,                    // Используем первую строку (индекс 0)
                3,                    // 3 кадра в строке
                0.5f                  // Длительность одного кадра (0.5 секунды)
        );

        // Создаем сцену
        stage = new Stage(new ScreenViewport(), game.getSpriteBatch());

        // Создаем таблицу с кнопками
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.add(new Label("Main Menu", game.getSkin(), "title")).padBottom(80).row();

        TextButton continueButton = new TextButton("Continue", game.getSkin());
        table.add(continueButton).width(300).padBottom(20).row();
        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToGame();
            }
        });

        TextButton selectMazeButton = new TextButton("Select a Maze", game.getSkin());
        table.add(selectMazeButton).width(300).padBottom(20).row();
        selectMazeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Select Maze button clicked");
            }
        });

        TextButton exitButton = new TextButton("Exit", game.getSkin());
        table.add(exitButton).width(300).row();
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Обновляем время анимации
        stateTime += delta;

        SpriteBatch batch = game.getSpriteBatch();
        batch.begin();

        // Получаем текущий кадр анимации
        TextureRegion currentFrame = backgroundAnimation.getKeyFrame(stateTime, true);

        // Размеры экрана
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // Размеры картинки
        float imageWidth = currentFrame.getRegionWidth();
        float imageHeight = currentFrame.getRegionHeight();

        // Рассчитываем масштаб (чтобы заполнить весь экран)
        float scaleX = screenWidth / imageWidth;
        float scaleY = screenHeight / imageHeight;
        float scale = Math.max(scaleX, scaleY); // Растягиваем до полного покрытия

        // Вычисляем новые размеры изображения
        float drawWidth = imageWidth * scale;
        float drawHeight = imageHeight * scale;

        // Центрируем изображение
        float drawX = (screenWidth - drawWidth) / 2;
        float drawY = (screenHeight - drawHeight) / 2;

        // Отрисовка изображения с масштабированием
        batch.draw(currentFrame, drawX, drawY, drawWidth, drawHeight);

        batch.end();

        // Обновляем сцену (UI)
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
}
