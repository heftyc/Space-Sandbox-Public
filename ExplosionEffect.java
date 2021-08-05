package com.tea.free;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ExplosionEffect extends Actor {
    static ParticleEffect bigStandard;
    static ParticleEffect standard;
    static ParticleEffect megaStandard;
    static ParticleEffect hugeStandard;

    static ParticleEffect megaBlueStandard;
    static ParticleEffect megaRedStandard;
    ParticleEffect effect;

    final static String PLANET_PACK_NAME = "PlanetPack2.atlas";

    boolean markedForDestruction = false;

    float dX, dY, x, y;

    public ExplosionEffect(int size, float x, float y, float dX, float dY, int sub) {
        if (size < 2) {
            this.effect = new ParticleEffect(standard);
        } else if (size == 2) {
            this.effect = new ParticleEffect(bigStandard);
        } else if (size == 3) {
            this.effect = new ParticleEffect(hugeStandard);
        } else {
            switch (sub) {
                case 0:
                    this.effect = new ParticleEffect(megaStandard);
                    break;
                case 1:
                    this.effect = new ParticleEffect(megaBlueStandard);
                    break;
                case 2:
                    this.effect = new ParticleEffect(megaRedStandard);
            }
        }
        this.x = x;
        this.y = y;
        effect.setPosition(x, y);
        this.effect.start();
        this.dX = dX;
        this.dY = dY;
    }

    public void act(float delta) {
        if (!effect.isComplete()) {
            x += dX * (delta / 0.016f);
            y += dY * (delta / 0.016f);
            this.setPosition(getX() + dX, getY() + dY);
            effect.setPosition(x, y);
            effect.update(delta);
        } else {
            effect.dispose();
            markedForDestruction = true;
        }
    }

    public void draw(Batch batch, float parentAlpha) {
        if (SettingScreen.renderParticles)
            effect.draw(batch);
    }

    public static void createStandard(AssetHub assetHub) {
        standard = new ParticleEffect();
        bigStandard = new ParticleEffect();
        megaStandard = new ParticleEffect();
        hugeStandard = new ParticleEffect();
        megaRedStandard = new ParticleEffect();
        megaBlueStandard = new ParticleEffect();



        standard.load(Gdx.files.internal("newSmallEx.gxf"), (TextureAtlas) assetHub.manager.get(PLANET_PACK_NAME));
        bigStandard.load(Gdx.files.internal("bigExplosion.gxf"), (TextureAtlas) assetHub.manager.get(PLANET_PACK_NAME));
        megaStandard.load(Gdx.files.internal("megaExplosion.gxf"), (TextureAtlas) assetHub.manager.get(PLANET_PACK_NAME));
        hugeStandard.load(Gdx.files.internal("hugeExplosion.gxf"), (TextureAtlas) assetHub.manager.get(PLANET_PACK_NAME));

        megaBlueStandard.load(Gdx.files.internal("blueMegaExplosion.gxf"), (TextureAtlas) assetHub.manager.get(PLANET_PACK_NAME));
        megaRedStandard.load(Gdx.files.internal("redMegaExplosion.gxf"), (TextureAtlas) assetHub.manager.get(PLANET_PACK_NAME));


        //megaRedStandard.setEmittersCleanUpBlendFunction(false);
        //megaBlueStandard.setEmittersCleanUpBlendFunction(false);
        //bigStandard.setEmittersCleanUpBlendFunction(false);

    }

    public static void dispose() {
        standard.dispose();
        bigStandard.dispose();
    }
}