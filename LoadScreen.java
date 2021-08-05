package com.tea.free;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.tea.free.MyGdxGame.Mode;
import com.tea.free.Planet.Type;

import java.util.ArrayList;

public class LoadScreen implements Screen {

    Table mainTable;
    Stage stage;
    VerticalGroup mainButtons;
    TextField textInput;
    ImageButton returnButton;
    ScrollPane sp;
    ImageButton deleteButton;
    TextButtonStyle ibs;
    boolean deleting;

    Skin skin;

    private AssetHub assetHub;

    ArrayList<String> fileNames = new ArrayList<String>();

    public LoadScreen(AssetHub assets) {
        float butDimension = Gdx.graphics.getWidth() / 8;
        stage = new Stage();
        skin = assets.skin;
        assetHub = assets;

        ibs = skin.get("tibs", TextButtonStyle.class);
        mainButtons = new VerticalGroup();

        sp = new ScrollPane(mainButtons, skin, "sps");
        FileHandle[] files = Gdx.files.local("").list();

        VerticalGroup others = new VerticalGroup();
        deleteButton = new ImageButton(skin, "loadTrashS");

        deleteButton.setWidth(butDimension);
        deleteButton.setHeight(butDimension);

        others.addActor(deleteButton);


        deleteButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                swapDeleteButton();
            }
        });

        returnButton = new ImageButton(skin, "returnS");
        returnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                playClick(0);
                MyGdxGame.swapMode(Mode.GAME);
            }
        });

        returnButton.setWidth(Gdx.graphics.getWidth() / 8);
        returnButton.setHeight(returnButton.getWidth());
        ImageButton homeButton = new ImageButton(skin, "homeS");
        homeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                playClick(0);
                MyGdxGame.swapMode(Mode.OPENING);
            }
        });

        homeButton.setWidth(Gdx.graphics.getWidth() / 8);
        homeButton.setHeight(homeButton.getWidth());
        for (int i = 0; i < files.length; i++) {
            fileNames.add(files[i].name());

            Table table = new Table();

            TextButton p = new TextButton(files[i].name(), ibs);
            table.pad(20);
            p.setBounds(60, 60, Gdx.graphics.getWidth() / 200, 130);

            final String fileName = fileNames.get(i);

            p.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent ce, Actor actor) {
                    if (!deleting) {
                        if (MyGdxGame.mode == Mode.LOAD) {
                            MyGdxGame.gameScreen.breakFollow();
                            loadStuff(fileName);
                            MyGdxGame.swapMode(Mode.TUTORIAL);
                        }

                        if (MyGdxGame.mode == Mode.SAVE) {
                            saveStuff(fileName);
                            MyGdxGame.swapMode(Mode.TUTORIAL);
                        }
                    } else {
                        deleteFile(fileName);
                        actor.getParent().remove();
                    }
                }
            });

            table.add(new Image(skin, "leftCap"));
            table.add(p);
            table.add(new Image(skin, "rightCap"));


            mainButtons.addActor(table);
            mainButtons.space(20);
        }

        Gdx.input.setCatchBackKey(true);
        mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);
        mainTable.add(homeButton).align(Align.topLeft).pad(20).expand(true, true);
        mainTable.add(sp).align(Align.center).pad(10);
        mainTable.add(deleteButton).align(Align.left);
        mainTable.add(returnButton).align(Align.topRight).pad(20).expand(true, true);
        returnButton.top();
        mainTable.pack();

        ImageButton newButton = new ImageButton(skin, "addSaveS");
        newButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                mainButtons.space(20);
                stage.addActor(textInput);
                deleteButton.setChecked(false);
                textInput.setCursorPosition(0);
                Gdx.input.setOnscreenKeyboardVisible(true);
                stage.setKeyboardFocus(textInput);
            }
        });
        mainButtons.space(20);
        mainButtons.addActor(newButton);

        textInput = new TextField("", skin, "tfs");
        textInput.setWidth(Gdx.graphics.getWidth());
        textInput.setMaxLength(20);
        textInput.setAlignment(Align.center);
        textInput.setCursorPosition(0);
        textInput.setPosition(0f, Gdx.graphics.getHeight() - textInput.getHeight());

        textInput.addListener(new InputListener() {
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Keys.ENTER) {
                    if (textInput.getText() == "") return true;
                    if (MyGdxGame.mode == Mode.SAVE) {
                        createNewFile(textInput.getText());
                        saveStuff(textInput.getText());
                        createNewButton();
                        MyGdxGame.swapMode(Mode.TUTORIAL);
                    } else {
                        createNewFile(textInput.getText());
                        createNewButton();
                        MyGdxGame.swapMode(Mode.TUTORIAL);
                    }
                    Gdx.input.setOnscreenKeyboardVisible(false);
                }
                return true;
            }
        });

        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (textInput.hasParent() && y < Gdx.graphics.getHeight() - textInput.getHeight() * 2) {
                    textInput.remove();
                    Gdx.input.setOnscreenKeyboardVisible(false);
                    return true;
                }
                return false;
            }
        });

    }

    protected void createNewButton() {
        final String text = textInput.getText();

        if (!Gdx.files.local(text).exists()) {
            return;
        }

        mainButtons.removeActor(textInput);

        TextButton imageB = new TextButton(text, ibs);
        Table table = new Table();

        fileNames.add(text);

        table.pad(20);


        imageB.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                if (!deleting) {
                    if (MyGdxGame.mode == Mode.LOAD) {
                        MyGdxGame.gameScreen.breakFollow();
                        loadStuff(text);
                        MyGdxGame.swapMode(Mode.TUTORIAL);
                    }

                    if (MyGdxGame.mode == Mode.SAVE) {
                        saveStuff(text);
                        MyGdxGame.swapMode(Mode.TUTORIAL);
                    }
                } else {
                    deleteFile(text);
                    actor.getParent().remove();
                }
            }
        });

        imageB.setHeight(130);


        table.add(new Image(skin, "leftCap"));
        table.add(imageB);
        table.add(new Image(skin, "rightCap"));

        mainButtons.addActorAt(mainButtons.getChildren().size - 1, table);
        createNewFile(textInput.getText());
        if (MyGdxGame.mode == Mode.SAVE) {
            saveStuff(textInput.getText());
        }
        textInput.setText("");
    }

    public void saveStuff(String fileName) {

        if (!Gdx.files.local(fileName).exists()) {
            return;
        }

        FileHandle file = Gdx.files.local(fileName);
        Array<String> planets = new Array<String>();

        planets.add(GameScreen.spaceStage.getCamera().position.x + " ");
        planets.add(GameScreen.spaceStage.getCamera().position.y + " ");
        planets.add(((OrthographicCamera) GameScreen.spaceStage.getCamera()).zoom + " ");

        int subIndex = -1;
        if (MyGdxGame.gameScreen.stage.getActors().size != 0) {
            for (int i = 0; i < GameScreen.spaceStage.getActors().size; i++) {
                if (GameScreen.spaceStage.getActors().get(i) instanceof Planet) {
                    if (((Planet) GameScreen.spaceStage.getActors().get(i)).isSubject) {
                        subIndex = i;
                        break;
                    }
                }
            }

            planets.add(subIndex + " ");

            for (int i = 0; i < GameScreen.spaceStage.getActors().size; i++) {
                if (GameScreen.spaceStage.getActors().get(i) instanceof Planet) {
                    Planet planet = ((Planet) (GameScreen.spaceStage.getActors().get(i)));
                    switch (planet.type) {
                        case BLACK_HOLE:
                            planets.add("B");
                            break;
                        case COMET:
                            planets.add("C");
                            break;
                        case GAS_GIANT:
                            planets.add("G");
                            break;
                        case MOON:
                            planets.add("M");
                            break;
                        case PLANET:
                            planets.add("P");
                            break;
                        case STAR:
                            planets.add("S");
                            break;
                        default:
                            break;
                    }
                    if (planet.sub != 0) {
                        planets.add(planet.sub + "");
                    }
                    planets.add(" " + planet.velocity.getX(), " " + planet.velocity.getY(), " " + planet.getX(),
                            " " + planet.getY() + " ");
                }
            }
        }
        file.writeString("", false);
        for (int i = 0; i < planets.size; i++) {
            file.writeString(planets.get(i), true);
        }
    }

    void swapDeleteButton() {
        if (deleteButton.isChecked()) {
            deleting = true;
        } else {
            deleting = false;
        }
    }

    public void loadStuff(String fileName) {
        GameScreen.spaceStage.getActors().clear();
        int Ii;
        String[] data = Gdx.files.local(fileName).readString().split(" ");
        // if file is not empty
        if (data.length > 1) {
            GameScreen.spaceStage.getCamera().position.x = Float.parseFloat(data[0]);
            GameScreen.spaceStage.getCamera().position.y = Float.parseFloat(data[1]);
            ((OrthographicCamera) GameScreen.spaceStage.getCamera()).zoom = Float.parseFloat(data[2]);

            for (int i = 4; i < data.length; i += 5) {
                if (data[i].matches("S")) {
                    GameScreen.spaceStage.addActor(new Planet(Type.STAR, Float.parseFloat(data[i + 3]), Float.parseFloat(data[i + 4]), Float.parseFloat(data[i + 1]), Float.parseFloat(data[i + 2]), 0, assetHub));
                } else if (data[i].matches("S1")) {
                    GameScreen.spaceStage.addActor(new Planet(Type.STAR, Float.parseFloat(data[i + 3]), Float.parseFloat(data[i + 4]), Float.parseFloat(data[i + 1]), Float.parseFloat(data[i + 2]), 1, assetHub));
                } else if (data[i].matches("S2")) {
                    GameScreen.spaceStage.addActor(new Planet(Type.STAR, Float.parseFloat(data[i + 3]), Float.parseFloat(data[i + 4]), Float.parseFloat(data[i + 1]), Float.parseFloat(data[i + 2]), 2, assetHub));
                } else if (data[i].matches("P")) {
                    GameScreen.spaceStage.addActor(new Planet(Type.PLANET, Float.parseFloat(data[i + 3]), Float.parseFloat(data[i + 4]), Float.parseFloat(data[i + 1]), Float.parseFloat(data[i + 2]), 0, assetHub));
                } else if (data[i].matches("G")) {
                    GameScreen.spaceStage.addActor(new Planet(Type.GAS_GIANT, Float.parseFloat(data[i + 3]), Float.parseFloat(data[i + 4]), Float.parseFloat(data[i + 1]), Float.parseFloat(data[i + 2]), 0, assetHub));
                } else if (data[i].matches("C")) {
                    GameScreen.spaceStage.addActor(new Planet(Type.COMET, Float.parseFloat(data[i + 3]), Float.parseFloat(data[i + 4]), Float.parseFloat(data[i + 1]), Float.parseFloat(data[i + 2]), 0, assetHub));
                } else if (data[i].matches("M")) {
                    GameScreen.spaceStage.addActor(new Planet(Type.MOON, Float.parseFloat(data[i + 3]), Float.parseFloat(data[i + 4]), Float.parseFloat(data[i + 1]), Float.parseFloat(data[i + 2]), 0, assetHub));
                } else if (data[i].matches("B")) {
                    GameScreen.spaceStage.addActor(new Planet(Type.BLACK_HOLE, Float.parseFloat(data[i + 3]), Float.parseFloat(data[i + 4]), Float.parseFloat(data[i + 1]), Float.parseFloat(data[i + 2]), 0, assetHub));
                } else if (data[i].matches("P1")) {
                    GameScreen.spaceStage.addActor(new Planet(Type.PLANET, Float.parseFloat(data[i + 3]), Float.parseFloat(data[i + 4]), Float.parseFloat(data[i + 1]), Float.parseFloat(data[i + 2]), 1, assetHub));
                } else if (data[i].matches("P2")) {
                    GameScreen.spaceStage.addActor(new Planet(Type.PLANET, Float.parseFloat(data[i + 3]), Float.parseFloat(data[i + 4]), Float.parseFloat(data[i + 1]), Float.parseFloat(data[i + 2]), 2, assetHub));
                } else if (data[i].matches("P3")) {
                    GameScreen.spaceStage.addActor(new Planet(Type.PLANET, Float.parseFloat(data[i + 3]), Float.parseFloat(data[i + 4]), Float.parseFloat(data[i + 1]), Float.parseFloat(data[i + 2]), 3, assetHub));
                } else if (data[i].matches("P4")) {
                    GameScreen.spaceStage.addActor(new Planet(Type.PLANET, Float.parseFloat(data[i + 3]), Float.parseFloat(data[i + 4]), Float.parseFloat(data[i + 1]), Float.parseFloat(data[i + 2]), 4, assetHub));
                } else if (data[i].matches("M1")) {
                    GameScreen.spaceStage.addActor(new Planet(Type.MOON, Float.parseFloat(data[i + 3]), Float.parseFloat(data[i + 4]), Float.parseFloat(data[i + 1]), Float.parseFloat(data[i + 2]), 1, assetHub));
                } else if (data[i].matches("M2")) {
                    GameScreen.spaceStage.addActor(new Planet(Type.MOON, Float.parseFloat(data[i + 3]), Float.parseFloat(data[i + 4]), Float.parseFloat(data[i + 1]), Float.parseFloat(data[i + 2]), 2, assetHub));
                } else if (data[i].matches("G1")) {
                    GameScreen.spaceStage.addActor(new Planet(Type.GAS_GIANT, Float.parseFloat(data[i + 3]), Float.parseFloat(data[i + 4]), Float.parseFloat(data[i + 1]), Float.parseFloat(data[i + 2]), 1, assetHub));
                } else if (data[i].matches("G2")) {
                    GameScreen.spaceStage.addActor(new Planet(Type.GAS_GIANT, Float.parseFloat(data[i + 3]), Float.parseFloat(data[i + 4]), Float.parseFloat(data[i + 1]), Float.parseFloat(data[i + 2]), 2, assetHub));
                }

                /*
                FIXME Move the subject setter out of the for loop

                 */

                Ii = Integer.parseInt(data[3]);
                if (Ii > -1) {
                    ((Planet) (GameScreen.spaceStage.getActors().get(Ii))).makeMeSubject();
                }
            }
        }
    }

    public void createNewFile(String name) {
        if (name.equals("")) return;
        FileHandle file = Gdx.files.local(name);
        file.writeString("", false);
    }

    public void deleteFile(String name) {
        if (Gdx.files.local(name).exists())
            Gdx.files.local(name).delete();

    }

    public void setbackVisible(boolean visible) {
        returnButton.setVisible(visible);
    }

    void reset() {
        textInput.remove();
        textInput.setText("");
    }

    public void deleteButton() {

    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

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
        if (Gdx.input.isKeyPressed(Keys.BACK)) {
            textInput.remove();
        }
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