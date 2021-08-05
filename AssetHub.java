package com.tea.free;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.tea.free.Planet.Type;

public class AssetHub {

    public AssetManager manager;
    public Skin skin;
    Sound sound, sound2;

    final String PLANET_PACK_NAME = "PlanetPack2.atlas";

    public AssetHub() {
        manager = new AssetManager();
        manager.load("superStars.png", Texture.class);
        manager.load(PLANET_PACK_NAME, TextureAtlas.class);
        manager.load("uiPack.atlas", TextureAtlas.class);
        manager.load("bonusOCRA.fnt", BitmapFont.class);
        manager.load("OCR A Title.fnt", BitmapFont.class);
        manager.load("button-19.wav", Sound.class);
        manager.load("button-50.wav", Sound.class);
    }

    void finishLoading() {
        skin = new Skin();
        skin.addRegions((TextureAtlas) manager.get("uiPack.atlas"));

        sound = manager.get("button-19.wav");
        sound2 = manager.get("button-50.wav");

        BitmapFont titleFont = manager.get("OCR A Title.fnt");
        titleFont.getData().setScale(Gdx.graphics.getWidth() / 1280f);

        LabelStyle titleLabS = new LabelStyle();
        titleLabS.font = titleFont;
        skin.add("titleLab", titleLabS);

        BitmapFont font = manager.get("bonusOCRA.fnt");
        font.getData().setScale(0.35f * (Gdx.graphics.getWidth() / 1280f));
        ImageButtonStyle playS = new ImageButtonStyle();

        playS.pressedOffsetY = -3;
        playS.imageUp = skin.getDrawable("bigPlay");

        skin.add("playS", playS);

        ImageButtonStyle bss = new ImageButtonStyle();

        bss.pressedOffsetY = -3;
        bss.imageUp = skin.getDrawable("bigSave");

        skin.add("bigSave", bss);

        ImageButtonStyle pauseS = new ImageButtonStyle();

        pauseS.pressedOffsetY = -3;
        pauseS.imageUp = skin.getDrawable("pause");
        pauseS.imageChecked = skin.getDrawable("pauseD");
        skin.add("pauseS", pauseS);

        ImageButtonStyle homeS = new ImageButtonStyle();

        homeS.pressedOffsetY = -3;
        homeS.imageUp = skin.getDrawable("home");

        skin.add("homeS", homeS);

        ImageButtonStyle loadTrashS = new ImageButtonStyle();

        loadTrashS.checked = skin.getDrawable("trashDown");
        loadTrashS.up = skin.getDrawable("trash");

        loadTrashS.checkedOffsetY = -3;

        skin.add("loadTrashS", loadTrashS);

        ImageButtonStyle trashS = new ImageButtonStyle();

        trashS.pressedOffsetY = -3;
        trashS.imageUp = skin.getDrawable("trash");

        skin.add("trashS", trashS);

        ImageButtonStyle saveS = new ImageButtonStyle();

        saveS.pressedOffsetY = -3;
        saveS.imageUp = skin.getDrawable("save");

        skin.add("saveS", saveS);

        ImageButtonStyle backS = new ImageButtonStyle();

        backS.pressedOffsetY = -3;
        backS.imageUp = skin.getDrawable("back");

        skin.add("backS", backS);

        ButtonStyle checkStyle = new ButtonStyle();
        checkStyle.pressedOffsetY = -3;
        checkStyle.up = skin.getDrawable("check");
        checkStyle.checked = skin.getDrawable("realCheck");
        skin.add("checkS", checkStyle);

        ButtonStyle exitStyle = new ButtonStyle();
        exitStyle.pressedOffsetY = -3;
        exitStyle.up = skin.getDrawable("check");
        skin.add("exitS", exitStyle);


        LabelStyle labels = new LabelStyle();
        labels.font = font;
        skin.add("labelS", labels);

        TextButtonStyle tibs = new TextButtonStyle();

        tibs.font = font;
        tibs.pressedOffsetY = -3;
        tibs.up = skin.getDrawable("newBlank");
        skin.add("tibs", tibs);


        ScrollPaneStyle sps = new ScrollPaneStyle();
        skin.add("sps", sps);

        ImageButtonStyle addSaveS = new ImageButtonStyle();

        addSaveS.up = skin.getDrawable("addSave");
        addSaveS.pressedOffsetY = -3;

        skin.add("addSaveS", addSaveS);

        TextFieldStyle tfs = new TextFieldStyle();
        tfs.background = skin.getDrawable("textBack");
        tfs.cursor = skin.getDrawable("textCursor");
        tfs.font = font;
        tfs.fontColor = Color.BLACK;
        skin.add("tfs", tfs);

        ImageButtonStyle planDelS = new ImageButtonStyle();
        planDelS.up = skin.getDrawable("newDelPlan");
        planDelS.pressedOffsetY = -3;
        skin.add("planDelS", planDelS);

        ImageButtonStyle smallSet = new ImageButtonStyle();
        smallSet.pressedOffsetY = -3;
        smallSet.imageUp = skin.getDrawable("blankCheck");
        skin.add("smallSet", smallSet);

        ImageButtonStyle returnS = new ImageButtonStyle();
        returnS.imageUp = skin.getDrawable("return");
        returnS.pressedOffsetY = -3;
        skin.add("returnS", returnS);

        ImageButtonStyle guideS = new ImageButtonStyle();
        guideS.pressedOffsetY = -3;
        guideS.imageUp = skin.getDrawable("guide");
        skin.add("guideS", guideS);

        // COMETS

        ImageButtonStyle cometS = new ImageButtonStyle();
        cometS.pressedOffsetY = -3;
        cometS.imageUp = skin.getDrawable("cU");
        cometS.imageChecked = skin.getDrawable("cD");
        skin.add("cometS", cometS);

        // MOONS

        ImageButtonStyle moonS = new ImageButtonStyle();
        moonS.pressedOffsetY = -3;
        moonS.imageUp = skin.getDrawable("mU");
        moonS.imageChecked = skin.getDrawable("mD");
        skin.add("moonS", moonS);

        ImageButtonStyle tanMoonS = new ImageButtonStyle();
        tanMoonS.pressedOffsetY = -3;
        tanMoonS.imageUp = skin.getDrawable("tanMoonU");
        tanMoonS.imageChecked = skin.getDrawable("tanMoonD");
        skin.add("tanMoonS", tanMoonS);

        // PLANETS

        ImageButtonStyle planetS = new ImageButtonStyle();
        planetS.pressedOffsetY = -3;
        planetS.imageUp = skin.getDrawable("pU");
        planetS.imageChecked = skin.getDrawable("pD");
        skin.add("planetS", planetS);

        ImageButtonStyle lifePlanetStyle = new ImageButtonStyle();
        lifePlanetStyle.pressedOffsetY = -3;
        lifePlanetStyle.imageUp = skin.getDrawable("lifeU");
        lifePlanetStyle.imageChecked = skin.getDrawable("lifeD");
        skin.add("lifeS", lifePlanetStyle);

        ImageButtonStyle purpleStyle = new ImageButtonStyle();
        purpleStyle.pressedOffsetY = -3;
        purpleStyle.imageUp = skin.getDrawable("purpleU");
        purpleStyle.imageChecked = skin.getDrawable("purpleD");
        skin.add("purpleS", purpleStyle);

        // GAS GIANTS


        ImageButtonStyle ggS = new ImageButtonStyle();
        ggS.pressedOffsetY = -3;
        ggS.imageUp = skin.getDrawable("gU");
        ggS.imageChecked = skin.getDrawable("gD");
        skin.add("ggS", ggS);

        // STARS

        ImageButtonStyle stS = new ImageButtonStyle();
        stS.pressedOffsetY = -3;
        stS.imageUp = skin.getDrawable("sU");
        stS.imageChecked = skin.getDrawable("sD");
        skin.add("stS", stS);

        ImageButtonStyle redSunS = new ImageButtonStyle();
        redSunS.pressedOffsetY = -3;
        redSunS.imageUp = skin.getDrawable("redSunU");
        redSunS.imageChecked = skin.getDrawable("redSunD");
        skin.add("redSunS", redSunS);

        ImageButtonStyle blueSunS = new ImageButtonStyle();
        blueSunS.pressedOffsetY = -3;
        blueSunS.imageUp = skin.getDrawable("blueSunU");
        blueSunS.imageChecked = skin.getDrawable("blueSunD");
        skin.add("blueSunS", blueSunS);


        ImageButtonStyle bhS = new ImageButtonStyle();
        bhS.pressedOffsetY = -3;
        bhS.imageUp = skin.getDrawable("bhU");
        bhS.imageChecked = skin.getDrawable("bhD");
        skin.add("bhS", bhS);

        ImageButtonStyle rockStyle = new ImageButtonStyle();
        rockStyle.pressedOffsetY = -3;
        rockStyle.imageUp = skin.getDrawable("rockU");
        rockStyle.imageChecked = skin.getDrawable("rockD");
        skin.add("rockStyle", rockStyle);

        ImageButtonStyle iceStyle = new ImageButtonStyle();
        iceStyle.pressedOffsetY = -3;
        iceStyle.imageUp = skin.getDrawable("iceU");
        iceStyle.imageChecked = skin.getDrawable("iceD");
        skin.add("iceStyle", iceStyle);

        ImageButtonStyle blueGasStyle = new ImageButtonStyle();
        blueGasStyle.pressedOffsetY = -3;
        blueGasStyle.imageUp = skin.getDrawable("blueU");
        blueGasStyle.imageChecked = skin.getDrawable("blueD");
        skin.add("blueGasStyle", blueGasStyle);

        ImageButtonStyle redMoonStyle = new ImageButtonStyle();
        redMoonStyle.pressedOffsetY = -3;
        redMoonStyle.imageUp = skin.getDrawable("bloodU");
        redMoonStyle.imageChecked = skin.getDrawable("bloodD");
        skin.add("redMoonStyle", redMoonStyle);

        ImageButtonStyle gbStyle = new ImageButtonStyle();
        gbStyle.pressedOffsetY = -3;
        gbStyle.imageUp = skin.getDrawable("bgU");
        gbStyle.imageChecked = skin.getDrawable("bgD");
        skin.add("bgStyle", gbStyle);
    }

    public Sprite getSprite(Type type, int sub) {
        switch (type) {
            case BLACK_HOLE:
                return ((TextureAtlas) (manager.get(PLANET_PACK_NAME))).createSprite("newBlack");
            case COMET:
                return ((TextureAtlas) (manager.get(PLANET_PACK_NAME))).createSprite("newComet");
            case GAS_GIANT:
                if (sub == 0) {
                    return ((TextureAtlas) (manager.get(PLANET_PACK_NAME))).createSprite("jitGG");
                } else if (sub == 1) {
                    return ((TextureAtlas) (manager.get(PLANET_PACK_NAME))).createSprite("bluegas");
                } else if (sub == 2) {
                    return ((TextureAtlas) (manager.get(PLANET_PACK_NAME))).createSprite("blueGreenGG");
                }
            case MOON:
                if (sub == 0) {
                    return ((TextureAtlas) (manager.get(PLANET_PACK_NAME))).createSprite("newMoon");
                } else if (sub == 1) {
                    return ((TextureAtlas) (manager.get(PLANET_PACK_NAME))).createSprite("bloodMoon");
                } else if(sub == 2){
                    return ((TextureAtlas) (manager.get(PLANET_PACK_NAME))).createSprite("tanMoon");
                }
            case PLANET:
                if (sub == 0) {
                    return ((TextureAtlas) (manager.get(PLANET_PACK_NAME))).createSprite("newplanet");
                } else if (sub == 2) {
                    return ((TextureAtlas) (manager.get(PLANET_PACK_NAME))).createSprite("rockPlanet");
                } else if (sub == 1) {
                    return ((TextureAtlas) (manager.get(PLANET_PACK_NAME))).createSprite("IcePlanet");
                } else if (sub == 3) {
                    return ((TextureAtlas) (manager.get(PLANET_PACK_NAME))).createSprite("pinkPlanet");
                } else if (sub == 4) {
                    return ((TextureAtlas) (manager.get(PLANET_PACK_NAME))).createSprite("LifePlanet");
                }

            case STAR:
                if (sub == 0) {
                    return ((TextureAtlas) (manager.get(PLANET_PACK_NAME))).createSprite("shadedSun");
                } else if (sub == 1) {
                    return ((TextureAtlas) (manager.get(PLANET_PACK_NAME))).createSprite("Blue Sun");
                } else if (sub == 2) {
                    return ((TextureAtlas) (manager.get(PLANET_PACK_NAME))).createSprite("Red Sun");
                }
            default:
                break;
        }
        return null;
    }

    void dispose() {
        manager.dispose();
        skin.dispose();
        sound.dispose();
    }

}