package com.tea.free;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import java.util.Locale;

public class MyGdxGame implements ApplicationListener {

    /*
     * TODO: Graphics - make better planetSprites - make better buttons - make a
     * font - add planet pointer - add load symbol and company graphic - fix
     * click place glitch: waiting until android development starts Done??? -
     * add icons - fix setAlpha on backDrop X
     *
     * Utils - eliminate needless variables from VectF X
     *
     * Engine - eliminate mass factor to get pulledX - add settings X - add
     * follow function to planetsX - add delete function to planetsX - add
     * advanced collision detection X
     *
     * Clean Up - destroy shapeRenderer - delete unused sprites
     *
     * FIX THE SPEED UP BUG!!! XXXX
     *
     *
     * TODO
     *
     * add Sound logo / Title apply font X
     *
     *
     */
    static final float TRANSITION_TIME = 0.3f;
    private SpriteBatch batch;
    private Sprite back;
    private AssetHub assets;

    static int adBuffer = -1;
    static final int AD_DELAY = 3;
    static float timeFromAd;
    static final float AD_TIME_DELAY = 45;


    private boolean drawShapes, doneLoading;

    enum Mode {
        LOADING, OPENING, LOAD, SAVE, GAME, SETTINGS, GUIDE, TUTORIAL
    }

    enum Language {
        English, Spanish, Portuguese
    }

    Language language;

    public static Mode mode = Mode.LOADING;

    private float time;
    private Texture loadTex;
    private Sprite loadSprite;

    //ShapeRenderer sr;

    static InputMultiplexer input;

    static OpenScreen openScreen;
    static LoadScreen loadScreen;
    static GameScreen gameScreen;
    static SettingScreen settingScreen;
    static GuideScreen guideScreen;
    static TutorialScreen tutorialScreen;

    static Medium messageMedium;

    public MyGdxGame(Medium medium) {
        messageMedium = medium;
    }

    @Override
    public void create() {
        assets = new AssetHub();
        loadTex = new Texture("Tutorial Planet.png");
        loadSprite = new Sprite(loadTex);
        loadSprite.setSize(Gdx.graphics.getHeight() / 9, Gdx.graphics.getHeight() / 9);
        loadSprite.setCenter(40 + (loadSprite.getWidth() / 2), 40 + (loadSprite.getHeight() / 2));
        loadSprite.setOriginCenter();
        input = new InputMultiplexer();

        Gdx.input.setInputProcessor(input);
        Gdx.input.setCatchBackKey(true);
        batch = new SpriteBatch();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        //sr = new ShapeRenderer();
        if (Locale.getDefault().getDisplayLanguage().contains("spañol")) {
            language = Language.Spanish;
        } else if (Locale.getDefault().getDisplayLanguage().contains("ortugu")) {//Locale.getDefault().getDisplayLanguage().contains("ortugu"
            language = Language.Portuguese;
        } else {
            language = Language.English;
        }
    }

    @Override
    public void render() {
        update();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        timeFromAd += Gdx.graphics.getDeltaTime();

        if (back != null) {
            batch.begin();
            if (mode == Mode.GAME) {
                float shade = gameScreen.setAlpha();
                back.setColor(shade, shade, shade, 1.0f);
                back.draw(batch);
            } else {
                back.setColor(Color.WHITE);
                back.draw(batch);
            }
            batch.end();
        }
        switch (mode) {
            case OPENING:
                openScreen.render(Gdx.graphics.getDeltaTime());
                break;
            case LOAD:
            case SAVE:
                loadScreen.render(Gdx.graphics.getDeltaTime());
                break;
            case GAME:
                gameScreen.render(Gdx.graphics.getDeltaTime());
                break;
            case SETTINGS:
                settingScreen.render(Gdx.graphics.getDeltaTime());
                break;
            case GUIDE:
                guideScreen.render(Gdx.graphics.getDeltaTime());
                break;
            case LOADING:
                time += Gdx.graphics.getDeltaTime();
                loadSprite.rotate(4);//setAlpha((float) Math.min(time / ((float) Math.PI - 0.5f), 1.0));
                batch.begin();
                loadSprite.draw(batch);
                batch.end();
                break;
            case TUTORIAL:
                tutorialScreen.render(Gdx.graphics.getDeltaTime());
                break;
            default:
                break;

        }
		/*if (drawShapes) {
			sr.setColor(Color.PURPLE);
			sr.setAutoShapeType(true);
			if (GameScreen.spaceStage != null) {
				sr.setProjectionMatrix(GameScreen.spaceStage.getCamera().combined);
			}
			sr.begin();

			if (gameScreen != null) {
				for (int i = 0; i < GameScreen.spaceStage.getActors().size; i++) {
					if (GameScreen.spaceStage.getActors().get(i) instanceof Planet) {
						Planet a = (Planet) GameScreen.spaceStage.getActors().get(i);
						sr.circle(a.getX(), a.getY(), a.getWidth() / 2);
					}
				}
			}
			sr.circle(0, 0, 2);
			sr.circle(0, 0, 500);
			sr.end();
		}*/
    }

    static float interpolateAlpha(float alpha) {
        if ((alpha + (Gdx.graphics.getDeltaTime() / 3) / TRANSITION_TIME) < 1.0f) {
            return alpha + (Gdx.graphics.getDeltaTime() / 3) / TRANSITION_TIME;
        } else {
            return 1.0f;
        }
    }

    private void update() {
        if (!doneLoading) {
            if (assets.manager.update() && time > MathUtils.PI) {
                assets.finishLoading();

                gameScreen = new GameScreen(assets);
                gameScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

                openScreen = new OpenScreen(assets);
                openScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                input.addProcessor(openScreen.stage);

                loadScreen = new LoadScreen(assets);
                loadScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

                settingScreen = new SettingScreen(language, assets);
                settingScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

                guideScreen = new GuideScreen(language, assets);
                guideScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

                tutorialScreen = new TutorialScreen(assets);
                tutorialScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

                doneLoading = true;
                back = new Sprite((Texture) assets.manager.get("superStars.png"));
                back.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                swapMode(Mode.OPENING);

                batch.disableBlending();

                Planet.setUpParticles(assets);
                ExplosionEffect.createStandard(assets);
            }
        }
    }

    @Override
    public void dispose() {
        try {
            openScreen.dispose();
            loadScreen.dispose();
            gameScreen.dispose();
            settingScreen.dispose();

            assets.dispose();
            batch.dispose();

            loadSprite.getTexture().dispose();
            back.getTexture().dispose();

            Planet.disposeParticles();
            ExplosionEffect.dispose();
        } catch (Exception e) {

        }
    }

    static void swapMode(Mode to) {
        input.clear();
        Gdx.input.setOnscreenKeyboardVisible(false);
        switch (to) {
            case GAME:
                adBuffer++;
                tryToPlayAd();
                gameScreen.stage.getBatch().setColor(new Color(1.0f, 1.0f, 1.0f, 0.0f));
                input.addProcessor(gameScreen.stage);
                input.addProcessor(GameScreen.spaceStage);
                input.addProcessor(gameScreen.gd);
                break;
            case LOAD:
                loadScreen.stage.getBatch().setColor(new Color(1.0f, 1.0f, 1.0f, 0.0f));
                gameScreen.reset();
                input.addProcessor(loadScreen.stage);
                break;
            case OPENING:
                adBuffer++;
                tryToPlayAd();
                openScreen.stage.getBatch().setColor(new Color(1.0f, 1.0f, 1.0f, 0.0f));
                input.addProcessor(openScreen.stage);
                break;
            case SAVE:
                adBuffer++;
                tryToPlayAd();
                loadScreen.stage.getBatch().setColor(new Color(1.0f, 1.0f, 1.0f, 0.0f));
                input.addProcessor(loadScreen.stage);
                break;
            case SETTINGS:
                settingScreen.stage.getBatch().setColor(new Color(1.0f, 1.0f, 1.0f, 0.0f));
                input.addProcessor(settingScreen.stage);
                break;
            case GUIDE:
                guideScreen.stage.getBatch().setColor(new Color(1.0f, 1.0f, 1.0f, 0.0f));
                input.addProcessor(guideScreen.stage);
                break;
            case TUTORIAL:
                tutorialScreen.stage.getBatch().setColor(new Color(1.0f, 1.0f, 1.0f, 0.0f));
                input.addProcessor(tutorialScreen.stage);
            default:
                break;
        }
        mode = to;
    }

    private static void tryToPlayAd() {
        if (adBuffer >= AD_DELAY && timeFromAd > AD_TIME_DELAY) {
            messageMedium.showAds(true);
            adBuffer = 0;
            timeFromAd = 0;
        }
    }

    @Override
    public void resize(int width, int height) {

        if (doneLoading) {
            gameScreen.resize(width, height);
            openScreen.resize(width, height);
            loadScreen.resize(width, height);
            settingScreen.resize(width, height);
            guideScreen.resize(width, height);
            tutorialScreen.resize(width, height);
        }
    }

    @Override
    public void pause() {
        if (settingScreen != null) settingScreen.flushPrefs();
    }

    @Override
    public void resume() {

    }
}