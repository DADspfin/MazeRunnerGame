package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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

public class MenuScreen implements Screen {

    private final Stage stage;
    private final MazeRunnerGame game;

    private Animation<TextureRegion> backgroundAnimation; // Анимация фона
    private float stateTime = 0;                          // Время для отслеживания текущего кадра

    public MenuScreen(MazeRunnerGame game) {
        this.game = game;

        // Загружаем изображения
        TextureRegion frame1 = new TextureRegion(new Texture("menuframe1.png"));
        TextureRegion frame2 = new TextureRegion(new Texture("menuframe2.png"));
        TextureRegion frame3 = new TextureRegion(new Texture("menuframe3.png"));

        // Создаем анимацию из кадров
        backgroundAnimation = new Animation<>(0.25f, frame1, frame2, frame3); // 0.5 секунд на кадр

        // Создаем сцену
        stage = new Stage(new ScreenViewport(), game.getSpriteBatch());

        // Добавляем UI
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.add(new Label("Maze Runner", game.getSkin(), "title")).padBottom(80).row();

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

        // Отрисовываем текущий кадр на весь экран
        batch.draw(currentFrame, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch.end();

        // Отрисовываем сцену
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
