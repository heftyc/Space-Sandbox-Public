package com.tea.free;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tea.free.MyGdxGame.Mode;
import com.tea.free.Planet.Type;

public class GameScreen implements Screen {

    public static float oX, oY, dragX, dragY, originalZoom;
    public Type currentType = Type.PLANET;
    public boolean paused, preparingToSave;
    private int sub;

    Planet subject;
    boolean following;

    public Stage stage;
    static Stage spaceStage;
    GestureDetector gd;

    Table rightButs;
    Table buts;

    ButtonGroup<Button> planetButtons;

    Viewport uiPort;
    Viewport spacePort;

    ImageButton delete;

    int pointers;

    ImageButton com;

    ImageButton moon;
    ImageButton redMoon;
    ImageButton tanMoon;

    ImageButton p;
    ImageButton rockPlanet;
    ImageButton IcePlanet;
    ImageButton lifePlanet;
    ImageButton purplePlanet;

    ImageButton gg;
    ImageButton blueGiant;
    ImageButton bgGiant;

    ImageButton st;
    ImageButton redSt;
    ImageButton blueSt;
    ImageButton bH;

    ScrollPane scroll;

    AssetHub assetHub;

    public GameScreen(final AssetHub assetHub) {
        stage = new Stage();
        spaceStage = new Stage();

        this.assetHub = assetHub;
        Skin skin = assetHub.skin;

        GestureListener gl = new GestureListener() {

            @Override
            public boolean touchDown(float x, float y, int pointer, int button) {
                pointers++;

                // Sets pointers back to one to fix glitch when player taps too fast
                if (pointers < 1) {
                    pointers = 1;
                }
                if (pointers == 1 && pointer == 0) {
                    Vector2 vec = spaceStage.getViewport().unproject(new Vector2(x, y));
                    spaceStage.addActor(new Planet(currentType, vec.x, vec.y, sub, assetHub));
                    oX = x;
                    oY = y;
                    return false;
                } else {
                    destroyPlanet();
                }
                return false;
            }

            @Override
            public boolean tap(float x, float y, int count, int button) {
                pointers--;
                createNewPlanet(x, y);
                return false;
            }

            @Override
            public boolean longPress(float x, float y) {
                return false;
            }

            @Override
            public boolean fling(float velocityX, float velocityY, int button) {
                return false;
            }

            @Override
            public boolean pan(float x, float y, float deltaX, float deltaY) {
                return false;
            }

            @Override
            public boolean panStop(float x, float y, int pointer, int button) {
                pointers--;
                if (pointers == 0)
                    createNewPlanet(x, y);

                return true;
            }

            @Override
            public boolean zoom(float initialDistance, float distance) {
                ((OrthographicCamera) (spaceStage.getCamera())).zoom = (float) Math.max(((initialDistance / distance) * originalZoom), 0.3);
                //setZoom((initialDistance - distance) * ZOOM_FACTOR);
                return true;
            }

            @Override
            public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
                float x = (pointer2.x + pointer1.x) / 2;
                float y = (pointer2.y + pointer1.y) / 2;

                //If we are not on the first touch of the second finger...
                if (initialPointer2.x * initialPointer2.y != pointer2.x * pointer2.y) {
                    spaceStage.getCamera().translate((dragX - x) * ((OrthographicCamera) spaceStage.getCamera()).zoom, (y - dragY) *
                            ((OrthographicCamera) spaceStage.getCamera()).zoom, 0);
                } else {
                    originalZoom = ((OrthographicCamera) (spaceStage.getCamera())).zoom;
                }
                dragX = x;
                dragY = y;

                return true;
            }

            @Override
            public void pinchStop() {
                pointers--;
            }
        };

        gd = new GestureDetector(0.01f, 0.01f, 1.1f, 0.15f, gl);
        uiPort = new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spacePort = new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.setViewport(uiPort);
        spaceStage.setViewport(spacePort);

        Image[] images = new Image[5];

        for (int i = 0; i < 5; i++) {
            images[i] = new Image();
            images[i].setDrawable(skin.getDrawable("divider"));
        }

        ImageButton ipause = new ImageButton(skin, "pauseS");
        ipause.setName("pause");
        ipause.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                playClick(0);
                if (((ImageButton) actor).isChecked()) {
                    paused = true;
                } else {
                    paused = false;
                }
            }
        });
        ImageButton ib = new ImageButton(skin, "homeS");
        ib.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                MyGdxGame.swapMode(Mode.OPENING);
                MyGdxGame.loadScreen.setbackVisible(false);
                playClick(0);
            }
        });

        buts = new Table();
        scroll = new ScrollPane(buts, skin, "sps");
        scroll.setColor(0.5f, 0.5f, 0.5f, 1.0f);

        moon = new ImageButton(skin, "moonS");

        moon.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                playClick(0);
                currentType = Type.MOON;
                sub = 0;

            }
        });

        redMoon = new ImageButton(skin, "redMoonStyle");

        redMoon.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                playClick(0);
                currentType = Type.MOON;
                sub = 1;

            }
        });

        tanMoon = new ImageButton(skin, "tanMoonS");

        tanMoon.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                playClick(0);
                currentType = Type.MOON;
                sub = 2;
            }
        });

        com = new ImageButton(skin, "cometS");

        com.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                currentType = Type.COMET;
                sub = 0;
                playClick(0);
            }
        });
        p = new ImageButton(skin, "planetS");

        p.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                currentType = Type.PLANET;
                playClick(0);
                sub = 0;
            }
        });

        IcePlanet = new ImageButton(skin, "iceStyle");

        IcePlanet.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                currentType = Type.PLANET;
                playClick(0);
                sub = 1;

            }
        });

        rockPlanet = new ImageButton(skin, "rockStyle");

        rockPlanet.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                currentType = Type.PLANET;
                playClick(0);
                sub = 2;

            }
        });

        purplePlanet = new ImageButton(skin, "purpleS");

        purplePlanet.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                currentType = Type.PLANET;
                playClick(0);
                sub = 3;
            }
        });

        lifePlanet = new ImageButton(skin, "lifeS");

        lifePlanet.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                currentType = Type.PLANET;
                playClick(0);
                sub = 4;

            }
        });

        gg = new ImageButton(skin, "ggS");

        gg.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                playClick(0);
                sub = 0;
                currentType = Type.GAS_GIANT;

            }
        });

        blueGiant = new ImageButton(skin, "blueGasStyle");

        blueGiant.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                sub = 1;
                currentType = Type.GAS_GIANT;
            }
        });

        bgGiant = new ImageButton(skin, "bgStyle");
        bgGiant.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                sub = 2;
                currentType = Type.GAS_GIANT;
            }
        });

        st = new ImageButton(skin, "stS");

        st.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                currentType = Type.STAR;
                playClick(0);
                sub = 0;
            }
        });

        blueSt = new ImageButton(skin, "blueSunS");

        blueSt.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                currentType = Type.STAR;
                playClick(0);
                sub = 1;
            }
        });


        redSt = new ImageButton(skin, "redSunS");

        redSt.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                currentType = Type.STAR;
                playClick(0);
                sub = 2;
            }
        });
        bH = new ImageButton(skin, "bhS");

        bH.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                currentType = Type.BLACK_HOLE;
                playClick(0);
                sub = 0;
            }
        });
        ImageButton iclear = new ImageButton(skin, "trashS");
        iclear.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                spaceStage.getActors().clear();
                playClick(0);
                breakFollow();
            }
        });
        iclear.setSize(20, 20);
        ImageButton isave = new ImageButton(skin, "saveS");
        isave.setSize(40, 40);
        isave.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                preparingToSave = true;
                playClick(0);
            }
        });

        delete = new ImageButton(skin, "planDelS");
        delete.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                playClick(0);
                deleteSubject();
            }
        });

        ImageButton settB = new ImageButton(skin, "smallSet");
        settB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
                playClick(0);
                MyGdxGame.swapMode(Mode.SETTINGS);
                SettingScreen.returnMode = Mode.GAME;
            }
        });

        float dim = Gdx.graphics.getHeight() / 12;
        float margain = Gdx.graphics.getHeight() / 52;

        buts.row().height(dim).space(margain);
        buts.add(com).width(dim);
        buts.row().height(dim).space(margain);

        buts.add(images[0]).width(dim).height(dim / 10);

        buts.row().height(dim).space(margain);
        buts.add(moon).width(dim);
        buts.row().height(dim).space(margain);
        buts.add(redMoon).width(dim);
        buts.row().height(dim).space(margain);
        buts.add(tanMoon).width(dim);
        buts.row().height(dim).space(margain);

        buts.add(images[1]).width(dim).height(dim / 10);

        buts.row().height(dim).space(margain);
        buts.add(p).width(dim);
        buts.row().height(dim).space(margain);
        buts.add(IcePlanet).width(dim);
        buts.row().height(dim).space(margain);
        buts.add(rockPlanet).width(dim);
        buts.row().height(dim).space(margain);
        buts.add(purplePlanet).width(dim);
        buts.row().height(dim).space(margain);
        buts.add(lifePlanet).width(dim);
        buts.row().height(dim).space(margain);

        buts.add(images[2]).width(dim).height(dim / 10);

        buts.row().height(dim).space(margain);
        buts.add(gg).width(dim);
        buts.row().height(dim).space(margain);
        buts.add(blueGiant).width(dim);
        buts.row().height(dim).space(margain);
        buts.add(bgGiant).width(dim);
        buts.row().height(dim).space(margain);

        buts.add(images[3]).width(dim).height(dim / 10);

        buts.row().height(dim).space(margain);
        buts.add(st).width(dim);
        buts.row().height(dim).space(margain);
        buts.add(blueSt).width(dim);
        buts.row().height(dim).space(margain);
        buts.add(redSt).width(dim);
        buts.row().height(dim).space(margain);


        buts.add(images[4]).width(dim).height(dim / 10);

        buts.row().height(dim).space(margain);
        buts.add(bH).width(dim);

        rightButs = new Table();
        rightButs.add(ib).size(dim).space(margain);
        rightButs.add(ipause).size(dim).space(margain);
        rightButs.add(iclear).size(dim).space(margain);
        rightButs.add(isave).size(dim).space(margain);
        rightButs.add(settB).size(dim).space(margain);

        rightButs.pad(margain);

        Table mainTable = new Table();
        buts.pad(margain);
        buts.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

        scroll.setPosition(0, 0);
        scroll.setWidth(Gdx.graphics.getWidth() / 8);
        scroll.setHeight(Gdx.graphics.getHeight());
        scroll.setScrollingDisabled(true, false);

        mainTable.setFillParent(true);

        mainTable.add(scroll).expand(false, false).align(Align.left);

        mainTable.add(rightButs).expand(true, false).align(Align.topRight);
        stage.addActor(mainTable);

        Table bTable = new Table();
        delete.setPosition(Gdx.graphics.getWidth() - margain - dim, margain);
        delete.setSize(dim, dim);

        bTable.setFillParent(true);
        bTable.add(delete).align(Align.bottomRight).expand(true, true).size(dim).pad(margain);

        stage.addActor(bTable);

        delete.setVisible(false);


        planetButtons = new ButtonGroup<Button>();
        planetButtons.add(moon, com, p, gg, st, bH, blueGiant, redMoon, tanMoon, rockPlanet, IcePlanet, purplePlanet, lifePlanet, redSt, blueSt, bgGiant);
        planetButtons.setMaxCheckCount(1);
        p.setChecked(true);
    }

    @Override
    public void show() {

    }
    private  final float SETTING_SPEED = 100f;
    @Override
    public void render(float delta) {


        if (SettingScreen.renderParticles) {
            Planet.renderParticles = ((OrthographicCamera) spaceStage.getCamera()).zoom < 45;
            if (!paused) {
                Planet.sunEffect.update(delta);
                Planet.blueSunEffect.update(delta);
                Planet.redSunEffect.update(delta);
            }
        } else {
            Planet.renderParticles = false;
        }
        stage.act(delta);

        if (!paused) {
            Planet.holeEffect.update(delta);
            for (int i = 0; i < spaceStage.getActors().size; i++) {
                Actor actor = spaceStage.getActors().get(i);
                actor.act(delta);
                if (actor instanceof Planet) {
                    ((Planet) actor).finishActing(delta);
                }
                if (following) {
                    if (actor instanceof Planet) {
                        if (((Planet) (actor)).locked) {
                            float factor = delta / 0.016f;
                            ((Planet) actor).move(subject.velocity.getX() * factor, subject.velocity.getY() * factor);
                        }
                    }
                }
            }
        }

        if (following) {
            spaceStage.getCamera().position.set(subject.getX() + subject.sprite.getWidth() / 2,
                    subject.getY() + subject.sprite.getHeight() / 2,
                    ((OrthographicCamera) spaceStage.getCamera()).zoom);

        }
        for (Actor a : spaceStage.getActors()) {
            if (a instanceof Planet) {
                if (((Planet) a).destroy) {
                    a.remove();
                }
            } else if (a instanceof ExplosionEffect) {
                if (((ExplosionEffect) a).markedForDestruction) {
                    a.remove();
                }
            }
        }

        spaceStage.draw();

        stage.getBatch().begin();
        for (int i = 0; i < stage.getActors().size; i++) {
            if (stage.getActors().get(i).isVisible()) {
                stage.getActors().get(i).draw(stage.getBatch(), MyGdxGame.interpolateAlpha(stage.getBatch().getColor().a));
            }
        }
        stage.getBatch().end();
        stage.getBatch().getColor().a = MyGdxGame.interpolateAlpha(stage.getBatch().getColor().a);

        if (preparingToSave) {
            MyGdxGame.swapMode(Mode.SAVE);
            MyGdxGame.loadScreen.setbackVisible(true);
            preparingToSave = false;
            MyGdxGame.loadScreen.reset();
        }
    }

    private void destroyPlanet() {
        for (int i = spaceStage.getActors().size - 1; i > -1; i--) {
            if (spaceStage.getActors().get(i) instanceof Planet) {
                if (((Planet) (spaceStage.getActors().get(i))).locked) {
                    spaceStage.getActors().get(i).remove();
                }
            }
        }
    }

    private void createNewPlanet(float x, float y) {
        playClick(1);
        for (int i = spaceStage.getActors().size - 1; i > -1; i--) {
            if (spaceStage.getActors().get(i) instanceof Planet) {
                if (((Planet) (spaceStage.getActors().get(i))).locked) {
                    if (following) {
                        ((Planet) (spaceStage.getActors().get(i))).unlock(subject.velocity.getX() + ((x - oX) *
                                        ((OrthographicCamera) (spaceStage.getCamera())).zoom) / 100,
                                (subject.velocity.getY() + ((oY - y) * ((OrthographicCamera) (spaceStage.getCamera())).zoom) / 100));


                    } else {
                        ((Planet) (spaceStage.getActors().get(i))).unlock(((x - oX) * ((OrthographicCamera) (spaceStage.getCamera())).zoom) / 100,
                                ((oY - y) * ((OrthographicCamera) (spaceStage.getCamera())).zoom) / 100);
                    }
                    break;

                }
            }
        }
    }

    public void setSubject(Planet subject) {
        breakFollow();
        this.subject = subject;
        following = true;
        delete.setVisible(true);
    }

    public void breakFollow() {
        if (following) {
            subject.isSubject = false;
            delete.setVisible(false);
            following = false;
        }
    }

    public void deleteSubject() {
        if (subject.type != Type.BLACK_HOLE && !paused) {
            spaceStage.addActor(new ExplosionEffect(subject.toughness, subject.bounds.x, subject.bounds.y, subject.velocity.getX(), subject.velocity.getY(), subject.sub));
        }
        subject.remove();
        breakFollow();
    }

    void reset() {
        pointers = 0;
        originalZoom = 1.5f;
        spaceStage.getCamera().translate(-spaceStage.getCamera().position.x, -spaceStage.getCamera().position.y, 0);
        ((OrthographicCamera) spaceStage.getCamera()).zoom = 1.5f;
        spaceStage.getActors().clear();
        ((ImageButton) (rightButs.findActor("pause"))).setChecked(false);
        planetButtons.uncheckAll();
        p.setChecked(true);
        breakFollow();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        spacePort.update(width, height, false);
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

    float setAlpha() {
        if (((OrthographicCamera) spaceStage.getCamera()).zoom > 40) {
            if (((OrthographicCamera) spaceStage.getCamera()).zoom < 80) {
                return (((OrthographicCamera) spaceStage.getCamera()).zoom - 80) * -0.025f;
            } else {
                return 0.0f;
            }
        } else {
            return 1f;
        }
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
    public void dispose() {
        stage.dispose();
        spaceStage.dispose();
    }

}