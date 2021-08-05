package com.tea.free;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Align;

public class TutorialScreen implements Screen {

    Stage stage;

    Sprite hand;
    Sprite planet;
    Sprite frame;

    private final int framesToWait = 30;
    private int framesWaited;

    final private float handSpeed = 0.35f, planetSpeed = 0.35f;
    float rightBound;
    float leftBound;

    SpriteBatch batch;

    Button exit;

    boolean hasSeen;

    enum State {
        dragging, waiting, goToDrag, goToWait, releasing, goToReleasing
    }

    State state;

    OrthographicCamera camera;
    Rectangle scissors;
    Rectangle bounds;

    AssetHub assetHub;

    public TutorialScreen(AssetHub assetHub) {

        stage = new Stage();
        batch = new SpriteBatch();

        planet = new Sprite(new Texture("Tutorial Planet.png"));
        hand = new Sprite(new Texture("Hand2.png"));
        frame = new Sprite(new Texture("Frame.png"));

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Skin skin = assetHub.skin;
        this.assetHub = assetHub;

        exit = new Button(skin, "exitS");

        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                hasSeen = true;
                MyGdxGame.swapMode(MyGdxGame.Mode.GAME);
                playClick(0);
            }
        });
        exit.align(Align.topRight);
        Table table = new Table();
        table.setFillParent(true);
        table.pad(20);
        table.add(exit).align(Align.topRight).expand(true, true);
        stage.addActor(table);
        exit.align(Align.topRight);
        state = State.goToDrag;

    }



    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if (hasSeen) {
            MyGdxGame.swapMode(MyGdxGame.Mode.GAME);
            return;
        }

        switch (state) {
            case dragging:
                draw(true, true);
                hand.translate(handSpeed * delta * Gdx.graphics.getWidth(), 0);
                if (hand.getX() > Gdx.graphics.getWidth() / 2) {
                    state = State.goToReleasing;
                }
                break;
            case waiting:
                draw(true, false);
                planet.translate(planetSpeed * delta * Gdx.graphics.getWidth(), 0);
                planet.rotate(3);
                if (planet.getX() > frame.getX() + frame.getWidth()) {
                    state = State.goToDrag;
                }
                break;
            case goToDrag:
                draw(true, false);
                hand.setPosition(leftBound, (Gdx.graphics.getHeight() / 2) - hand.getHeight());
                planet.setCenter(leftBound, Gdx.graphics.getHeight() / 2);

                planet.setRotation(0);

                state = State.dragging;
                break;
            case goToWait:
                state = State.waiting;
                draw(true, false);
                break;
            case releasing:
                draw(true, true);
                if (framesWaited == framesToWait) {
                    state = State.goToWait;
                }
                framesWaited++;

                break;
            case goToReleasing:
                draw(true, true);
                framesWaited = 0;
                state = State.releasing;
                break;
        }
        stage.act();
        if (MyGdxGame.interpolateAlpha(stage.getBatch().getColor().a) != 1f) {
            stage.getBatch().begin();
            for (int i = 0; i < stage.getActors().size; i++) {
                stage.getActors().get(i).draw(stage.getBatch(), MyGdxGame.interpolateAlpha(stage.getBatch().getColor().a));
            }
            stage.getBatch().end();
            stage.getBatch().getColor().a = MyGdxGame.interpolateAlpha(stage.getBatch().getColor().a);
        } else {
            stage.draw();
        }

    }


    private void draw(boolean drawPlanet, boolean drawHand) {
        camera.update();
        batch.begin();
        ScissorStack.calculateScissors(camera, batch.getTransformMatrix(), bounds, scissors);
        ScissorStack.pushScissors(bounds);

        frame.draw(batch);

        if (drawHand) {
            hand.draw(batch);
        }
        if (drawPlanet) {
            planet.draw(batch);
        }
        batch.flush();

        ScissorStack.popScissors();
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        camera = new OrthographicCamera(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        stage.getViewport().update(width, height, true);

        frame.setSize(Gdx.graphics.getWidth() * 0.7f, (Gdx.graphics.getWidth() * 0.7f) * 0.333f);
        frame.setCenter(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

        planet.setSize(frame.getHeight() * 0.5f, frame.getHeight() * 0.5f);
        hand.setSize(frame.getHeight() * 0.4f * 0.706f, frame.getHeight() * 0.4f);

        leftBound = frame.getX() + frame.getHeight() / 2;
        rightBound = frame.getX() + frame.getWidth() - frame.getHeight() / 2;

        scissors = new Rectangle((int) frame.getX() - Gdx.graphics.getWidth() / 2, (int) frame.getY() - Gdx.graphics.getHeight() / 2, (int) frame.getWidth(), (int) frame.getHeight());
        bounds = new Rectangle(frame.getX(), frame.getY(), frame.getWidth(), frame.getHeight());

        planet.setOriginCenter();
    }

    private void playClick(int ID) {
        if (SettingScreen.sound) {
            if (ID == 0) {
                assetHub.sound.play(0.5f);
            } else {
                assetHub.sound2.play(0.5f);
            }

        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}