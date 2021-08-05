package com.tea.free;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.tea.free.MyGdxGame.Mode;

public class OpenScreen implements Screen {

    Stage stage;
    Table mainButtons;

    private AssetHub assetHub;


    public OpenScreen(AssetHub assetHub) {
        stage = new Stage();

        Skin skin = assetHub.skin;
        this.assetHub = assetHub;

        mainButtons = new Table();

        mainButtons.pack();
        Label space = new Label("Space Sandbox", skin, "titleLab");
        mainButtons.add(space);

        ImageButton ip = new ImageButton(skin, "playS");

        ip.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                playClick(0);
                MyGdxGame.gameScreen.reset();
                MyGdxGame.swapMode(Mode.TUTORIAL);
            }
        });

        mainButtons.row().height(Gdx.graphics.getHeight() / 3).space(20);
        mainButtons.add(ip).width(Gdx.graphics.getWidth() / 3).height(Gdx.graphics.getWidth() / 6);

        ImageButton il = new ImageButton(skin, "bigSave");

        il.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                playClick(0);
                MyGdxGame.swapMode(Mode.LOAD);
                MyGdxGame.loadScreen.setbackVisible(false);
                MyGdxGame.loadScreen.reset();
            }
        });

        mainButtons.row().height(Gdx.graphics.getHeight() / 3).space(20);
        mainButtons.add(il).width(Gdx.graphics.getWidth() / 3).height(Gdx.graphics.getWidth() / 6);

        ImageButton isettingsBut = new ImageButton(skin, "smallSet");

        isettingsBut.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                playClick(0);
                MyGdxGame.swapMode(Mode.SETTINGS);
                SettingScreen.returnMode = Mode.OPENING;
            }
        });

        ImageButton guideBut = new ImageButton(skin, "guideS");

        guideBut.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                playClick(0);
                MyGdxGame.swapMode(Mode.GUIDE);
            }
        });
        isettingsBut.setWidth(Gdx.graphics.getWidth() / 8);
        isettingsBut.setHeight(guideBut.getWidth());

        guideBut.align(Align.topLeft).pad(20);

        isettingsBut.align(Align.topRight).pad(20);

        Table table = new Table();

        table.add(guideBut).expand(true, true).align(Align.topLeft);
        table.add(mainButtons);
        table.add(isettingsBut).expand(true, true).align(Align.topRight);
        table.setFillParent(true);

        stage.addActor(table);

        //stage.addActor(guideBut);

        //mainButtons.setFillParent(true);
        //stage.addActor(mainButtons);

        //stage.addActor(isettingsBut);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.getBatch().begin();
        for (int i = 0; i < stage.getActors().size; i++) {
            stage.getActors().get(i).draw(stage.getBatch(), MyGdxGame.interpolateAlpha(stage.getBatch().getColor().a));
        }
        stage.getBatch().end();
        stage.getBatch().getColor().a = MyGdxGame.interpolateAlpha(stage.getBatch().getColor().a);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

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
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
