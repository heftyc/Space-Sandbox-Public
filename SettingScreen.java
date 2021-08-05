package com.tea.free;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.tea.free.MyGdxGame.Mode;

public class SettingScreen implements Screen {

    public Stage stage;
    Preferences prefs;
    Table things;

    final String[] englishPhrases = {
            "Sound", "Unneeded Effects"};

    final String[] spanishPhrases = {
            "Sonido", "Explosiones"};

    //ÓóÍíáõã

    final String[] portPhrases = {"Som", "Explosão"};

    private String[] currentPhrases;

    // ##############################
    static boolean renderParticles, sound;

    // #############################

    static Mode returnMode = Mode.OPENING;

    private AssetHub assetHub;


    public SettingScreen(MyGdxGame.Language language, AssetHub assetHub) {
        if (language == MyGdxGame.Language.English) {
            currentPhrases = englishPhrases;
        } else if (language == MyGdxGame.Language.Spanish) {
            currentPhrases = spanishPhrases;
        } else {
            currentPhrases = portPhrases;
        }

        this.assetHub = assetHub;

        prefs = Gdx.app.getPreferences("teaFreeSpaceSandbox");

        sound = prefs.getBoolean("Sound", true);
        renderParticles = prefs.getBoolean("Particles", true);

        Skin skin = assetHub.skin;

        stage = new Stage();

        Label soundL = new Label(currentPhrases[0], skin, "labelS");
        Button SoundB = new Button(skin, "checkS");

        SoundB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                swapSound();
                playClick(0);
            }
        });

        Label renParLab = new Label(currentPhrases[1], skin, "labelS");

        Button irenParBut = new Button(skin, "checkS");

        irenParBut.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                playClick(0);
                swapParticleRender();
            }
        });

        ImageButton ihomeButton = new ImageButton(skin, "backS");

        ihomeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                playClick(0);
                MyGdxGame.swapMode(returnMode);
            }
        });
        ihomeButton.setWidth(Gdx.graphics.getWidth() / 8);
        ihomeButton.setHeight(ihomeButton.getWidth());
        irenParBut.setChecked(renderParticles);
        SoundB.setChecked(sound);
        stage.addActor(ihomeButton);
        float dim = Gdx.graphics.getHeight() / 12;
        things = new Table();

        things.add(soundL);
        things.add(SoundB).pad(20).width(dim).height(dim);
        things.row();
        things.add(renParLab);
        things.add(irenParBut).pad(20).width(dim).height(dim);
        //things.setFillParent(true);

        Table uberTable = new Table();
        uberTable.setFillParent(true);

        uberTable.add(ihomeButton).align(Align.topLeft).pad(20);
        uberTable.add(things).expand(true, true).align(Align.center);
        stage.addActor(uberTable);
    }

    protected void swapSound() {
        if (things != null) {
            if (sound) {
                sound = false;
            } else {
                sound = true;
            }
        }
    }

    protected void swapParticleRender() {
        // This line prevents a unwanted swap when the program sets the button
        // to the right state
        if (things != null) {
            if (renderParticles) {
                renderParticles = false;
            } else {
                renderParticles = true;
            }
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        stage.act();
        stage.getBatch().begin();
        for (int i = 0; i < stage.getActors().size; i++) {
            stage.getActors().get(i).draw(stage.getBatch(), MyGdxGame.interpolateAlpha(stage.getBatch().getColor().a));
        }
        stage.getBatch().end();
        stage.getBatch().getColor().a = MyGdxGame.interpolateAlpha(stage.getBatch().getColor().a);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
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
        flushPrefs();
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

    void flushPrefs() {
        prefs.putBoolean("Sound", sound);
        prefs.putBoolean("Particles", renderParticles);
        prefs.flush();
    }
}
