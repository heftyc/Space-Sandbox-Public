package com.tea.free;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.tea.free.MyGdxGame.Mode;

public class GuideScreen implements Screen {

    Stage stage;
    ScrollPane sp;

    private AssetHub assetHub;

    private String[] officalLang;
    //ÓóÍíá
    private final String[] introEsp = {
            "   Arrastre a lo largo de la pantalla y suelte para crear un nuevo objeto. " +
                    "Cuanto más lejos arrastre, más rápido viajará el objecto en esa dirección.",
            "   Toque un objeto para seguirlo. Toque de nuevo para dejar de seguirlo. " +
                    "Mientras sigues algo, puedes eliminarlo " +
                    "tocando el botón en la esquina inferior derecha.",
            "   Para guardar su universo, toque el botón del disquete que está cerca de la esquina superior derecha. Desde allí puedes crear un " +
                    "nuevo archivo presionando el botón \"+\", escribiendo el nombre del archivo y tocando " +
                    "la tecla Intro. Luego toque el archivo en el que desea guardar su universo.",
            "   Para evitar que la aplicación se ralentice cuando hay muchas estrellas o explosiones, desactive Explosiones en la configuración."
    };

    private final String[] introPort = {

            "     Arrasta pela tela e solte para criar um novo objeto. Quanto mais você arrastar, mais rápido o objeto viajará nessa direção.",
            "     Toque em um objeto para segui-lo. Toque novamente para parar de segui-lo. " +
                    "Enquanto você está seguindo algo, você pode excluí-lo " +
                    "tocando no botão no canto inferior direito.",
            "     Para salvar seu universo, toque no botão de disquete próximo ao canto superior direito. A partir daí, você pode criar um " +
                    "novo arquivo pressionando o botão \"+\", digitando o nome do arquivo e tocando em " +
                    "a tecla Enter. Em seguida, toque no arquivo no qual você deseja salvar seu universo.",
            "     Para impedir que o aplicativo diminua a velocidade quando houver muitas estrelas ou explosões, desmarque Explosões na configuração."

    };
    private final String[] introEng = {
            "     Drag and release to make a new object. The farther you drag," +
                    " the faster the object will travel in that direction.",
            "     Tap an object to follow it. Tap it again to stop following it. " +
                    "While you are following something, you can delete it " +
                    "by tapping the button that appears in the bottom right corner.",
            "To save your universe, tap the floppy disk button near the top right corner. From there you can create a " +
                    "new file by hitting the \"+\" button, typing in the name of the file, and tapping " +
                    "the enter key. Then tap the file you would like to save your universe to.", "To maintain performance " +
            "in a universe with lots of stars or explosions, turn off unneeded effects in settings."};

    public GuideScreen(MyGdxGame.Language language, AssetHub assetHub) {
        stage = new Stage();
        Table table = new Table();

        Skin skin = assetHub.skin;
        this.assetHub = assetHub;

        ImageButton homeButton = new ImageButton(skin, "homeS");
        homeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent ce, Actor actor) {
               playClick(0);
                MyGdxGame.swapMode(Mode.OPENING);
            }
        });

        Table paras = new Table();

        paras.row().width(Gdx.graphics.getWidth() * 0.6f).pad(30);

        if (language == MyGdxGame.Language.English) {
            officalLang = introEng;
        } else if (language == MyGdxGame.Language.Spanish) {
            officalLang = introEsp;
        } else {
            officalLang = introPort;
        }
        for (int i = 0; i < officalLang.length; i++) {
            Label l = new Label(officalLang[i], skin, "labelS");
            paras.add(l);
            l.setWrap(true);
            paras.row().width(Gdx.graphics.getWidth() * 0.6f).pad(26);
        }

        table.setFillParent(true);
        sp = new ScrollPane(paras, skin, "sps");
        sp.setScrollingDisabled(true, false);
        //sp.setFillParent(true);

        homeButton.setWidth(Gdx.graphics.getWidth() / 8);
        homeButton.setHeight(Gdx.graphics.getWidth() / 8);

        //table.add(homeButton).width(Gdx.graphics.getWidth() / 8).height(Gdx.graphics.getHeight() / 8).expand(false, true).align(Align.topLeft);

        table.add(homeButton).align(Align.topLeft).expand(false, true).pad(20);
        table.add(sp).expand(true, false);
        stage.addActor(table);
        stage.setDebugAll(true);

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
        stage.getViewport().update(width, height);
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
