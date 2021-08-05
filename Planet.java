package com.tea.free;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tea.free.utils.VectF;

import java.util.Queue;
import java.util.Stack;

public class Planet extends Actor {

    public enum Type {
        COMET, MOON, PLANET, GAS_GIANT, STAR, BLACK_HOLE
    }

    boolean isSubject;

    public int sub;

    boolean locked;
    static boolean renderParticles = true;
    public Type type;

    public VectF velocity;
    public float mass, rotationSpeed;
    public Circle bounds;
    public int toughness;
    public boolean destroy;
    public Sprite sprite;
    ParticleEffect effect;
    static ParticleEffect cometEffect = new ParticleEffect();
    static ParticleEffect sunEffect = new ParticleEffect();
    static ParticleEffect blueSunEffect = new ParticleEffect();
    static ParticleEffect redSunEffect = new ParticleEffect();
    static ParticleEffect holeEffect = new ParticleEffect();

    // this constructor is used when creating in game
    public Planet(Type type, float x, float y, int sub,AssetHub assetHub) {
        locked = true;
        this.type = type;
        this.sub = sub;

        sprite = assetHub.getSprite(type, sub);
        setType(type, sub);

        this.setWidth(sprite.getWidth());
        this.setHeight(sprite.getHeight());
        this.setPosition(x - getWidth() / 2, y - getHeight() / 2);
        sprite.setPosition(this.getX(), this.getY());
        sprite.setAlpha(0.5f);
        bounds = new Circle(sprite.getX() + getWidth() / 2, sprite.getY() + getHeight() / 2, sprite.getWidth() / 2);
    }

    // this constructor is used when loading from file
    public Planet(Type type, float x, float y, float dx, float dy, int sub, AssetHub assetHub) {
        this.type = type;
        this.sub = sub;
        velocity = new VectF(dx, dy);

        sprite = assetHub.getSprite(type, sub);

        setType(type, sub);

        if (effect != null) {
            effect.start();
            effect.setPosition(x, y);
        }
        this.setWidth(sprite.getWidth());
        this.setHeight(sprite.getHeight());
        this.setPosition(x, y);
        sprite.setPosition(this.getX(), this.getY());
        bounds = new Circle(sprite.getX() + getWidth() / 2, sprite.getY() + getHeight() / 2, sprite.getWidth() / 2);
        rotationSpeed = MathUtils.random(10f) - 5f;

        this.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!isSubject) {
                    makeMeSubject();
                    event.cancel();
                } else {
                    isSubject = false;
                    MyGdxGame.gameScreen.breakFollow();
                }
                return true;
            }

        });
    }

    private void setType(Type type, int sub) {
        float dim = 0;
        switch (type) {
            case BLACK_HOLE:
                mass = 2000000;
                toughness = 5;
                dim = 450;
                break;
            case COMET:
                mass = 1;
                toughness = 0;
                effect = new ParticleEffect(cometEffect);
                dim = 10;
                break;
            case GAS_GIANT:
                mass = 1200;
                toughness = 3;
                dim = 100;
                break;
            case MOON:
                mass = 13;
                toughness = 1;
                dim = 20;
                break;
            case PLANET:
                mass = 600;
                toughness = 2;
                dim = 60;
                break;
            case STAR:
                mass = 35000;
                toughness = 4;
                dim = 300;
                break;
            default:
                break;

        }
        sprite.setBounds(0, 0, dim, dim);
        sprite.setOriginCenter();
    }

    public void unlock(float x, float y) {
        locked = false;
        sprite.setAlpha(1f);
        if (effect != null) {
            effect.start();
            effect.setPosition(bounds.x, bounds.y);
        }
        rotationSpeed = MathUtils.random(10f) - 5f;
        velocity = new VectF(x, y);

        this.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!isSubject) {
                    makeMeSubject();
                    event.cancel();
                } else {
                    isSubject = false;
                    MyGdxGame.gameScreen.breakFollow();
                }
                return true;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (type == Type.BLACK_HOLE) {
            if (!locked) {
                holeEffect.setPosition(bounds.x, bounds.y);
                holeEffect.draw(batch);
            }
            sprite.draw(batch);
            return;
        }

        if (type == Type.COMET) {
            sprite.draw(batch);
            effect.draw(batch);
            return;
        }

        if (type == Type.STAR) {
            sprite.draw(batch);
            if (!locked && renderParticles) {
                switch (sub) {
                    case 0:
                        sunEffect.setPosition(bounds.x, bounds.y);
                        sunEffect.draw(batch);
                        break;
                    case 1:
                        blueSunEffect.setPosition(bounds.x, bounds.y);
                        blueSunEffect.draw(batch);
                        break;
                    case 2:
                        redSunEffect.setPosition(bounds.x, bounds.y);
                        redSunEffect.draw(batch);
                }
            }
            return;
        }

        if (renderParticles && effect != null) {
            sprite.draw(batch);
            effect.draw(batch);
        } else {
            sprite.draw(batch);
        }
    }

    public static void setUpParticles(AssetHub assetHub) {
        cometEffect.load(Gdx.files.internal("newCometEffect.gxf"), (TextureAtlas) assetHub.manager.get("PlanetPack2.atlas"));
        sunEffect.load(Gdx.files.internal("newsharpSun.gxf"), (TextureAtlas) assetHub.manager.get("PlanetPack2.atlas"));
        holeEffect.load(Gdx.files.internal("sharpHole.gxf"), (TextureAtlas) assetHub.manager.get("PlanetPack2.atlas"));
        blueSunEffect.load(Gdx.files.internal("blueSun.gxf"), (TextureAtlas) assetHub.manager.get("PlanetPack2.atlas"));
        redSunEffect.load(Gdx.files.internal("redSun.gxf"), (TextureAtlas) assetHub.manager.get("PlanetPack2.atlas"));
    }

    public void act(float delta) {

        if (locked) {
            return;
        }
        if (destroy) {
            return;// ???
        }

        float factor = delta / 0.016f;

        for (int i = 0; i < GameScreen.spaceStage.getActors().size; i++) {
            if (GameScreen.spaceStage.getActors().get(i) instanceof Planet) {
                Planet p = (Planet) GameScreen.spaceStage.getActors().get(i);
                if (!p.locked && p != this) {
                    if (this.toughness > p.toughness) {
                        if (this.bounds.overlaps(p.bounds)) {
                            destroyPlanet(p);
                            if (this.type != Type.BLACK_HOLE) {
                                GameScreen.spaceStage.addActor(new ExplosionEffect(p.toughness,
                                        p.bounds.x, p.bounds.y, this.velocity.getX(), this.velocity.getY(), p.sub));
                            }
                        } else {
                            getPulled(bounds.x, bounds.y, p.bounds.x, p.bounds.y, p.mass, factor);
                        }
                    } else if (this.toughness == p.toughness) {
                        if (this.bounds.overlaps(p.bounds) && bounds.x != p.bounds.x && bounds.y != p.bounds.y) {
                            destroyPlanet(p);
                            destroyPlanet(this);
                            if (this.type != Type.BLACK_HOLE) {
                                GameScreen.spaceStage.addActor(new ExplosionEffect(p.toughness,
                                        p.bounds.x, p.bounds.y, (this.velocity.getX() + p.velocity.getX()) / 2, (this.velocity.getY() + p.velocity.getY()) / 2, p.sub));
                                GameScreen.spaceStage.addActor(new ExplosionEffect(toughness,
                                        this.bounds.x, this.bounds.y, (this.velocity.getX() + p.velocity.getX()) / 2, (this.velocity.getY() + p.velocity.getY()) / 2, sub));

                            }
                        } else {
                            getPulled(bounds.x, bounds.y, p.bounds.x, p.bounds.y, p.mass, factor);
                        }

                    } else {
                        getPulled(bounds.x, bounds.y, p.bounds.x, p.bounds.y, p.mass, factor);
                    }
                }
            }
        }
    }

    private void destroyPlanet(Planet planet) {
        if (planet.isSubject) {
            planet.isSubject = false;
            MyGdxGame.gameScreen.breakFollow();
        }
        planet.destroy = true;
    }

    void finishActing(float delta) {
        if (!locked) {
            float factor = delta / 0.016f;
            move(velocity.getX() * factor, velocity.getY() * factor);

            if ((renderParticles || type == Type.COMET) && effect != null) {
                effect.update(delta);
                effect.setPosition(bounds.x, bounds.y);
            }
            sprite.rotate(rotationSpeed * factor);
        }
    }

    private void getPulled(float x, float y, float pX, float pY, float pMass, float factor) {
        float distance = (float) Math.hypot(x - pX, y - pY);
        // (mass1*mass2) / distance^2
        float force = (float) ((pMass) / Math.pow(distance, 2) * factor);
        float angle = (float) -Math.atan((y - pY) / (x - pX));

        float aX = (float) ((force * Math.cos(-angle)));
        float aY = (float) ((force * Math.sin(-angle)));

        if (aX > 25 || aY > 25) {
            destroy = true;
        }

        if (x > pX) {
            velocity.add(-aX, -aY);
        } else {
            velocity.add(aX, aY);
        }
    }

    void move(float x, float y) {
        this.moveBy(x, y);
        sprite.translate(x, y);
        bounds.setPosition(sprite.getX() + sprite.getWidth() / 2, sprite.getY() + sprite.getHeight() / 2);
    }

    public void makeMeSubject() {
        MyGdxGame.gameScreen.setSubject(this);
        isSubject = true;
    }

    static void disposeParticles() {
        cometEffect.dispose();
        holeEffect.dispose();
        sunEffect.dispose();
    }
}