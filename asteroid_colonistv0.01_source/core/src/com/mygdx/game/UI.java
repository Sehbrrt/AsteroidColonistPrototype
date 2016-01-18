package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sehbrrt on 02.01.2016.
 */
public class UI{

    private Table table;
    private Stage stage;
    private String inp = "bla";
    private Label globalenergy;
    private Label globalpopulation;
    private Label globalproduction;
    private Label globalore;
    private Label globalwater;
    private Table table2;
    private Table table3;
    private ImageButton imageButtonZoom;
    private int testx=0;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    private Label colonizeLabel;
    private Label exploreLabel;
    private Table attacktable;
    private Label attackLabel;
    //private Table att

    private ArrayList<Point> citybuttonstartcoords;
    private float camera1x=0;
    private float camera1y=0;

    private Skin skin;
    //private TextButton.TextButtonStyle tbStyleTop;

    private ArrayList<TextButton> citybuttons;

    private ArrayList<TextButton> buildingsbuttons;
    private ArrayList<Label> buildingsdescriptions;
    private ArrayList<Label> buildingsdescriptions2;

    private boolean iszoom = true;

    private ScrollPane spane;
    private Table table6;
    private Label colname;
    private Label prodtime;
    private Label colpop;
    private Label colprod;
    private Label colener;
    private Label coldef;
    private Label colwat;
    private Label colore;
    private Label colheal;
    private TextButton colatt;
    private TextButton colexpl;
    private TextButton colcolo;
    private enum Interaction{
        NOTHING,
        EXPLORING,
        COLONIZING,
        ATTACKING
    }
    private Colony fromColony;
    private int fromColonyKey=0;

    private Interaction interaction = Interaction.NOTHING;
    //for 3 tbs
    private TextButton[] colinteractbuttons=new TextButton[3];
    private int turns;

    private Label detaileddescription;

    private BitmapFont bmf;

    private TextButton buttonnext;

    private boolean iscitymenuechecked = false;

    //is viewing a colonyscreen? -1 if false, value of position of colony in colonies
    private int viewingcolony = -1;

    private HashMap<Integer, Colony> colonies;

    private TextButton.TextButtonStyle tbStyleTop;
    private TextButton.TextButtonStyle tbfinished;
    private TextButton.TextButtonStyle tbnotavailable;
    private TextButton.TextButtonStyle tbproducing;

    private int cameraJumpCityx=0;
    private int cameraJumpCityy=0;

    private Stack stack1;
    private Stack stack2;

    private Faction player1;

    private float lastdraggedx=0;
    private float lastdraggedy=0;

    public UI(){
        colonies = new HashMap<Integer, Colony>();


        bmf = new BitmapFont(Gdx.files.internal("testfont.fnt"));
        //scale bmf
        bmf.getData().setScale(2, 2);

        citybuttons = new ArrayList<TextButton>();
        citybuttonstartcoords = new ArrayList<Point>();
         skin = new Skin(Gdx.files.internal("basicuiskin/uiskin.json"));

        Texture texture = new Texture(Gdx.files.internal("asteroidcolonist_icon32x32.bmp"));
        TextureRegion textureRegion = new TextureRegion(texture);
        TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(textureRegion);

        Label.LabelStyle styleTop = new Label.LabelStyle();
        //styleTop.background = textureRegionDrawable;
        styleTop.font = bmf;
        skin.add("styleTop", styleTop);

        //TextButton.TextButtonStyle tbStyleTop = new TextButton.TextButtonStyle();

        tbStyleTop = new TextButton.TextButtonStyle();
        tbStyleTop.font = bmf;
        //tbStyleTop.checkedFontColor = Color.CYAN;
        //tbStyleTop.downFontColor = Color.BLACK;
        //tbStyleTop.checkedOverFontColor=Color.RED;
        tbStyleTop.downFontColor=Color.CYAN;
        tbStyleTop.overFontColor = Color.GREEN;
        tbStyleTop.fontColor = Color.WHITE;
        tbStyleTop.checkedOverFontColor=Color.BLUE;
        tbStyleTop.checkedFontColor = Color.GRAY;
        skin.add("tbStyleTop", tbStyleTop);


        globalenergy = new Label(""+inp, skin, "styleTop");
        globalpopulation = new Label(""+testx, skin, "styleTop");
        globalproduction = new Label("9999,999", skin, "styleTop");
        globalore = new Label("2354,973", skin, "styleTop");
        globalwater = new Label("1000,000", skin, "styleTop");
        TextButton buttonmanual = new TextButton("Manual", skin, "tbStyleTop");
        TextButton buttonoptions = new TextButton("Options", skin, "tbStyleTop");
        Texture text = new Texture(Gdx.files.internal("uiicons/iconenergy.png"));
        TextureRegionDrawable tenergy = new TextureRegionDrawable(new TextureRegion(text));
        Texture text2 = new Texture(Gdx.files.internal("uiicons/iconproductivity.png"));
        TextureRegionDrawable tproductivity = new TextureRegionDrawable(new TextureRegion(text2));
        Texture text3 = new Texture(Gdx.files.internal("uiicons/iconpopulation.png"));
        TextureRegionDrawable tpopulation = new TextureRegionDrawable(new TextureRegion(text3));
        Texture text4 = new Texture(Gdx.files.internal("uiicons/iconore.png"));
        TextureRegionDrawable tore = new TextureRegionDrawable(new TextureRegion(text4));
        Texture text5 = new Texture(Gdx.files.internal("uiicons/iconwater.png"));
        TextureRegionDrawable twater = new TextureRegionDrawable(new TextureRegion(text5));
        Texture text6 = new Texture(Gdx.files.internal("uiicons/iconzoom+.png"));
        TextureRegionDrawable tzoomplus = new TextureRegionDrawable(new TextureRegion(text6));
        Texture text7 = new Texture(Gdx.files.internal("uiicons/iconzoom-.png"));
        TextureRegionDrawable tzoomminus = new TextureRegionDrawable(new TextureRegion(text7));
        Texture text8 = new Texture(Gdx.files.internal("uiicons/icondefense.png"));
        TextureRegionDrawable tdefense = new TextureRegionDrawable(new TextureRegion(text8));
        Texture text9 = new Texture(Gdx.files.internal("uiicons/iconhealth.png"));
        TextureRegionDrawable thealth = new TextureRegionDrawable(new TextureRegion(text9));
        Texture text10 = new Texture(Gdx.files.internal("uiicons/iconattacks.png"));
        TextureRegionDrawable tattack = new TextureRegionDrawable(new TextureRegion(text10));
        Texture text11 = new Texture(Gdx.files.internal("uiicons/iconexplorer.png"));
        TextureRegionDrawable texplorer = new TextureRegionDrawable(new TextureRegion(text11));
        Texture text12 = new Texture(Gdx.files.internal("uiicons/iconcolonist.png"));
        TextureRegionDrawable tcolonist = new TextureRegionDrawable(new TextureRegion(text12));
        //Sprite tenergy = new Sprite(new Texture(Gdx.files.internal("uiicons/iconenergy.png")));

       // ImageButton iconenergy = new ImageButton(tenergy, skin, "styleTop");
        //ImageButton iconenergy = new ImageButton(tenergy);
        Image iconenergy = new Image(tenergy);
        Image iconenergy2 = new Image(tenergy);
        Image iconproductivity = new Image(tproductivity);
        Image iconpopulation = new Image(tpopulation);
        Image iconore = new Image(tore);
        Image iconwater = new Image(twater);
        Image icondefense = new Image(tdefense);
        Image iconproductivity2 = new Image(tproductivity);
        Image iconpopulation2 = new Image(tpopulation);
        Image iconore2 = new Image(tore);
        Image iconwater2 = new Image(twater);
        Image iconattack = new Image(tattack);
        Image iconhealth = new Image(thealth);
        Image iconexplorer = new Image(texplorer);
        Image iconcolonist = new Image(tcolonist);
        //iconenergy.setStyle(styleTop);
        Stack topStack = new Stack();


        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.setDebugAll(true);

        table = new Table(skin);
        table.setFillParent(true);
        table.setClip(true);
        table.setDebug(true, true);
        table.setWidth(stage.getWidth());
        table.setHeight(stage.getHeight());

        //----------------------------------------------------------------------------------
        //top  bar

        table2 = new Table(skin);
        //table2.setFillParent(false);
        //table2.defaults().width(100);
        table2.setDebug(true);

        //table2.setClip(true);
        //table2.background(skin.newDrawable("textfield", Color.CYAN));

        table2.add(iconenergy);
        table2.add(globalenergy).width(80);
        table2.add(iconpopulation);
        table2.add(globalpopulation).width(80);
        table2.add(iconproductivity);
        table2.add(globalproduction).width(80);
        table2.add(iconore);
        table2.add(globalore).width(80);
        table2.add(iconwater);
        table2.add(globalwater).width(80);
        table2.add(buttonmanual).width(80);
        table2.add(buttonoptions).width(80);
        //table2.add(addressLabel);
        //table2.add(addressText).width(100);

        //table2.right();
        //table2.left().top();



        //Texture uip = new Texture(Gdx.files.internal("uiicons/uipane1.png"));
        //TextureRegionDrawable uipd= new TextureRegionDrawable(new TextureRegion(uip));
        //table2.setBackground(textureRegionDrawable);
        //table2.setWidth(table.getWidth());
        Drawable d1 = skin.newDrawable(skin.getDrawable("default-pane"), new Color(180, 166, 166, 1));
        table2.setBackground(d1);
        //table2.setBackground(textureRegionDrawable);
        table2.left().top();

        //--------------------------------------------------------------------------------------------------------------
        //map and zoom
        ImageButton.ImageButtonStyle zoomibs = new ImageButton.ImageButtonStyle();
        zoomibs.imageChecked = tzoomminus;
        zoomibs.imageUp = tzoomplus;

        skin.add("zoomibs", zoomibs);

        table3 = new Table(skin);
        table3.setDebug(true, true);
       // table3.setFillParent(true);
        //table3.setClip(true);
        imageButtonZoom = new ImageButton(skin, "zoomibs");
       // Label labeltest = new Label("zoomtest", skin, "styleTop");
        Image imgmap = new Image(textureRegionDrawable);
        //imgmap.setFillParent(true);
        buttonnext = new TextButton("NEXT", skin, "tbStyleTop");
        buttonnext.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if ((((TextButton) event.getListenerActor()).isChecked())) {
                    ((TextButton) event.getListenerActor()).toggle();
                    //only able to start next round if production selected everywhere
                    // check on all colonies)
                   // System.out.println("NEXT");
                    boolean isNext = true;
                    //System.out.println(prodtime.getText());
                    if (prodtime.getText().indexOf("f") != 0 || !iscitymenuechecked) {
                        //System.out.println("NEXT2");
                        for (HashMap.Entry<Integer, Colony> entry : colonies.entrySet()) {
                           // System.out.println("NEXT3");
                            //if(colonies.get(i)==null) System.out.println("darf nicht");
                            //System.out.println(colonies.get(i)+"aad");
                            //System.out.println(colonies.get(i).getName());
                            //System.out.println(colonies.get(i).getFaction());
                           // System.out.println(entry.getValue().getName());
                            if (entry.getValue().getFaction().getName().equals(World.PLAYER_FACTION)) {
                                //System.out.println("anz playercolonies");
                                if (entry.getValue().isfinishbuilding()) {
                                    //there is something other to choose before next turn
                                    //System.out.println("notfinished");
                                    citybuttonstartcoords.get(entry.getKey()).getX();
                                    //System.out.println(citybuttonstartcoords.get(entry.getKey()).getX() + ", " + camera1x);
                                    //camera1x=colonies.get(i).getAsteroid().getXcoord();
                                    //camera1y=colonies.get(i).getAsteroid().getYcoord();
                                    cameraJumpCityx = entry.getValue().getAsteroid().getXcoord();
                                    cameraJumpCityy = entry.getValue().getAsteroid().getYcoord();
                                    //and show interface for the other colony
                                    // viewingcolony= i;
                                    if (viewingcolony != entry.getKey()) {
                                        if(viewingcolony>=0)
                                            if (citybuttons.get(viewingcolony).isChecked())
                                                citybuttons.get(viewingcolony).toggle();
                                        if (citybuttons.get(entry.getKey()).isChecked())
                                            citybuttons.get(entry.getKey()).toggle();

                                        viewingcolony = entry.getKey();
                                        AsteroidColonist.jumpbeforenext(colonies.get(viewingcolony).getAsteroid().getXcoord(), colonies.get(viewingcolony).getAsteroid().getYcoord());

                                        //citybuttons.get(i).toggle();
                                        /*InputEvent ev = new InputEvent();
                                        ev.setRelatedActor(citybuttons.get(i));
                                        ev.setType(InputEvent.Type.touchDown);
                                        // citybuttons.get(i).getClickListener().clicked(ev,0,0);
                                        citybuttons.get(i).fire(ev);
                                        viewingcolony=i;*/

                                        //from citybuttonlistener
                                        //System.out.println("check");
                                        iscitymenuechecked = true;
                                        table6.getCell(colname).getActor().setText(((citybuttons.get(entry.getKey())).getText()));
                                        updateColonieButtons(colonies.get(citybuttons.get(entry.getKey())));
                                        spane.setVisible(true);
                                        table6.setVisible(true);
                                        //viewingcolony = entry.getKey();
                                        updateColonieButtons(entry.getValue());
                                        //nextColonyStats();
                                        nextColonyStats();
                                    }
                                    //updateColonieButtons(colonies.get(i));
                                    //nextColonyStats();
                                    isNext = false;
                                }
                            }
                        }
                       // System.out.println("ende colonies");
                        if (isNext) {
                            //System.out.println(prodtime.getText() + " All colonies are building");
                            AsteroidColonist.next();
                        }
                    }else{
                        //if other colony vieved
                        for (HashMap.Entry<Integer, Colony> entry : colonies.entrySet()) {
                            if( entry.getValue().isfinishbuilding()&&entry.getValue().getFaction()==player1){
                                viewingcolony=entry.getKey();
                                citybuttonstartcoords.get(entry.getKey()).getX();
                                //System.out.println(citybuttonstartcoords.get(entry.getKey()).getX() + ", " + camera1x);
                                //camera1x=colonies.get(i).getAsteroid().getXcoord();
                                //camera1y=colonies.get(i).getAsteroid().getYcoord();
                                cameraJumpCityx = entry.getValue().getAsteroid().getXcoord();
                                cameraJumpCityy = entry.getValue().getAsteroid().getYcoord();
                                break;
                            }

                        }
                        AsteroidColonist.jumpbeforenext(colonies.get(viewingcolony).getAsteroid().getXcoord(), colonies.get(viewingcolony).getAsteroid().getYcoord());



                    }


                }
            }
        });
        int tab3width =100;
        imageButtonZoom.setStyle(zoomibs);
        table3.add(imageButtonZoom).width(tab3width).height(30);
        table3.add(buttonnext).width(tab3width).height(30);
        table3.row();
        table3.add(imgmap).width(tab3width).height(tab3width);

        //table3.add()
        table3.setBackground(d1);
      //  table3.left().bottom();


        table.add(table2).width(stage.getWidth()).height(globalenergy.getHeight() + globalenergy.getY());

        //table.add(table3).width(100).height(50);
       // table3.setX(stage.getWidth() - table.getWidth());
        //table3.setWidth(100);
         table.left().top();
        //table.right();

        //-----------------------------------------------------------------------
        //colony interface
        table6 = new Table(skin);
       // table6.setWidth(200+2*iconenergy.getWidth());
        table6.setBackground(d1);




        table6.setDebug(true, true);
        colname = new Label("BASENAME", skin, "styleTop");
        prodtime = new Label("finished", skin, "styleTop");
        table6.add();
        table6.add(colname).width(80);
        table6.add();
        table6.add(prodtime).width(80);
        table6.row();
        colener = new Label("12", skin, "styleTop");
        table6.add(iconenergy2);
        table6.add(colener).width(80);
        coldef = new Label("12", skin, "styleTop");
        table6.add(icondefense);
        table6.add(coldef).width(80);
        table6.row();
        colpop = new Label("12", skin, "styleTop");
        table6.add(iconpopulation2);
        table6.add(colpop).width(80);
        colheal = new Label("12", skin, "styleTop");
        table6.add(iconhealth);
        table6.add(colheal).width(80);
        table6.row();
        colprod = new Label("12", skin, "styleTop");
        table6.add(iconproductivity2);
        table6.add(colprod).width(80);
        colatt = new TextButton("12", skin, "tbStyleTop");
        table6.add(iconattack);
        table6.add(colatt).width(80);
        table6.row();
        colwat = new Label("12", skin, "styleTop");
        table6.add(iconwater2);
        table6.add(colwat).width(80);
        colexpl = new TextButton("12", skin, "tbStyleTop");
        table6.add(iconexplorer);
        table6.add(colexpl).width(80);
        table6.row();
        colore = new Label("12", skin, "styleTop");
        table6.add(iconore2);
        table6.add(colore).width(80);
        colcolo = new TextButton("12", skin, "tbStyleTop");
        table6.add(iconcolonist);
        table6.add(colcolo).width(80);
        table.row();
        table.add(table6).left().top();

        //colinteractbuttons:
        colinteractbuttons[0]=colatt;
        colinteractbuttons[1]=colexpl;
        colinteractbuttons[2]=colcolo;
        for(int i=0; i<3; i++){
            colinteractbuttons[i].addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                   // System.out.println("click");
                    fromColony= colonies.get(viewingcolony);
                    fromColonyKey=viewingcolony;
                    if ((((TextButton) event.getListenerActor()).isChecked())) {
                        if(((TextButton) event.getListenerActor()).getStyle()==tbStyleTop&&viewingcolony >=0&&colonies.get(viewingcolony)!=null){
                            if(event.getListenerActor()==colcolo&& colonies.get(viewingcolony).getStatstock()[StatsTypes.COLONIST.ordinal()]>0){
                               interaction=Interaction.COLONIZING;
                            } else{
                                if(event.getListenerActor()==colexpl&& colonies.get(viewingcolony).getStatstock()[StatsTypes.EXPLORER.ordinal()]>0){
                                    interaction=Interaction.EXPLORING;
                                }else if(event.getListenerActor()==colatt&& colonies.get(viewingcolony).getStatstock()[StatsTypes.ATTACK.ordinal()]>0){
                                    interaction=Interaction.ATTACKING;

                                }
                            }
                        }

                    }else interaction=Interaction.NOTHING;
                    ((TextButton) event.getListenerActor()).toggle();


                }
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    super.enter(event, x, y, pointer, fromActor);
                        stack2.setX(190);
                        stack2.setY(event.getListenerActor().getY()-(event.getListenerActor().getOriginY())+234);
                        if(event.getListenerActor()==colcolo)colonizeLabel.setVisible(true);
                        else if(event.getListenerActor()==colexpl)exploreLabel.setVisible(true);
                        else if(event.getListenerActor()==colatt)attackLabel.setVisible(true);
                   // }
                }
                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    super.exit(event, x, y, pointer, fromActor);
                    exploreLabel.setVisible(false);
                    attackLabel.setVisible(false);
                    colonizeLabel.setVisible(false);
                }
            });


        }
        //table.row();
        //table.row();

        //--------------------------------------------------------------------------------------------------------------
        //building chooser
        buildingsbuttons = new ArrayList<TextButton>();
        buildingsdescriptions = new ArrayList<Label>();
        buildingsdescriptions2 = new ArrayList<Label>();
        Table table4 = new Table(skin);
        int tab4width = 94;
       // table4.defaults().width(tab4width).right().bottom();
        table4.defaults().right().bottom().align(Align.center);
        table4.setDebug(true, true);
        final Label.LabelStyle descstyle = new Label.LabelStyle();

        descstyle.fontColor=Color.GRAY;//new Color(120, 106, 106, 1);
        descstyle.font = bmf;
        //descstyle.background = d1;

        //style tb finished
         tbfinished = new TextButton.TextButtonStyle();
        tbfinished.font=bmf;
        tbfinished.fontColor = Color.GREEN;
        skin.add("tbfinished", tbfinished);

        //style tb not available
         tbnotavailable = new TextButton.TextButtonStyle();
        tbnotavailable.font=bmf;
        tbnotavailable.fontColor = Color.GRAY;
        skin.add("tbnotavailable", tbnotavailable);

        //style tb producing
         tbproducing = new TextButton.TextButtonStyle();
        tbproducing.font=bmf;
        tbproducing.fontColor = Color.YELLOW;
        skin.add("tbproducing", tbproducing);

        skin.add("descstyle", descstyle);
       // table4.right();
        Buildings buildings;
        for(int i=0; i<50; i++){
            if(i<Buildings.values().length){
                buildingsbuttons.add(new TextButton(Buildings.values()[i].name(), skin));
                buildingsdescriptions.add(new Label("("+Buildings.values()[i].prodValue+")", skin));
            } else {
                //System.out.println(Buildings.values().length);
                buildingsbuttons.add(new TextButton("---------", skin));
                buildingsdescriptions.add(new Label("---------", skin));
            }
           // buildingsbuttons.get(i).setWidth(100);
            buildingsbuttons.get(i).setStyle(tbnotavailable);
            buildingsdescriptions.get(i).setStyle(descstyle);
            buildingsdescriptions.get(i).setAlignment(Align.left);
            //buildingsdescriptions.get(i).setWidth(30);
            buildingsbuttons.get(i).addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    if ((((TextButton) event.getListenerActor()).isChecked())) {
                         if(((TextButton) event.getListenerActor()).getStyle()==tbStyleTop&&viewingcolony >=0&&colonies.get(viewingcolony)!=null){
                             //if a colony set to producing
                             ((TextButton) event.getListenerActor()).setStyle(tbproducing);
                             colonies.get(viewingcolony).setProducingbuilding(Buildings.values()[buildingsbuttons.indexOf(event.getListenerActor())]);
                             updateColonieButtons(colonies.get(viewingcolony));
                         }

                    }
                }
            });
            buildingsbuttons.get(i).addListener(new InputListener(){
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    super.enter(event, x, y, pointer, fromActor);
                    //buildingsbuttons.indexOf(event.getListenerActor());
                    if(buildingsbuttons.indexOf(event.getListenerActor())<Buildings.values().length){
                        //System.out.println(buildingsbuttons.indexOf(event.getListenerActor()) + "ind");
                        detaileddescription.setText(Buildings.values()[buildingsbuttons.indexOf(event.getListenerActor())].effect);
                        //stack1.setX(event.getListenerActor().getX()+event.getListenerActor().getWidth());
                        //stack1.setY(event.getListenerActor().getY());
                        //System.out.println(event.getListenerActor().getY()-(event.getListenerActor().getOriginY())-1024);
                        stack1.setX(128);
                        //System.out.println(spane.getY()+"spane"+event.getListenerActor().getX());
                        stack1.setY(event.getListenerActor().getY()-(event.getListenerActor().getOriginY())-1006+spane.getScrollY());
                        detaileddescription.setVisible(true);
                    }
                }
                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    super.exit(event, x, y, pointer, fromActor);
                    detaileddescription.setVisible(false);
                }

            });
            table4.add(buildingsbuttons.get(i));
            table4.add(buildingsdescriptions.get(i));
            table4.row();

            //long description
            if(i<Buildings.values().length) {
                buildingsdescriptions2.add(new Label(Buildings.values()[i].effect, skin));

            } else {
                buildingsdescriptions2.add(new Label("---------", skin));

            }
            buildingsdescriptions2.get(i).setStyle(descstyle);
            buildingsdescriptions2.get(i).setAlignment(Align.left);
            table4.add().left().bottom();
            //table4.add(buildingsdescriptions.get(i));
            table4.row();


        }
       // table4.right().bottom();
        table4.setBackground(d1);
        //table4.left().bottom();
        //table4.align(Align.bottom);
       // table4.


        spane = new ScrollPane(table4);
        ScrollPane.ScrollPaneStyle paneStyle = new ScrollPane.ScrollPaneStyle();
        //paneStyle.background = d1;
        paneStyle.corner = tenergy;
        //spane.setStyle(paneStyle);

        //System.out.println("width"+(stage.getWidth() - buildingsbuttons.get(0).getWidth()));
        //spane.setX(stage.getWidth() - buildingsbuttons.get(0).getWidth());
        //spane.setFadeScrollBars(true);
       // spane.screenToLocalCoordinates(new Vector2(stage.getWidth() - buildingsbuttons.get(0).getWidth(), 100));
        //spane.
        //spane.
        //table5 = new Table(skin);
       // table5.setFillParent(true);
        //table5.add(spane);
        //table5.setBackground(skin.newDrawable(skin.getDrawable("default"), new Color(180, 166, 666, 1)));
        //table5.setDebug(true, true);
        //table5.right().bottom();
        //table5.right().top();
        //table2.row();
       // table2.row();
       // table2.row();
        spane.setStyle(paneStyle);
        spane.setHeight(100);
       // spane.setHeight(100);
      //  spane.setWidth(100);
        //spane.s
        //spane.setFadeScrollBars(false);
        Table table7 = new Table(skin);
        table7.add(spane).left().bottom();
        //System.out.println("widthdazwi" + (int) (table.getWidth() - spane.getWidth() - table3.getWidth()) + "," + spane.getWidth() + "," + table3.getWidth());
        table7.add().width(stage.getWidth() - tab4width * 2 - 2 - tab3width*2-2);
        table7.add(table3).right().bottom();
        //table.add(table3).bottom().right().height(50);
        table.row();
        table.add(table7).left();
        //table.row();
        //table.add(spane).bottom().left();
        //table.row();


        stack1=new Stack();
        Label.LabelStyle descstyleDet = new Label.LabelStyle();

        descstyleDet.fontColor=Color.CYAN;
        descstyleDet.font = bmf;
        skin.add("descstyleDet", descstyleDet);
        detaileddescription = new Label("descr", skin, "descstyleDet");
        stack1.setHeight(detaileddescription.getHeight());
        stack1.setWidth(120);
        stack1.add(detaileddescription);
        detaileddescription.setVisible(false);

        /*    private Label colonizeLabel;
    private Label exploreLabel;
    private Table attacktable;
    private Label attackLabel;
    private Slider attackSlider;*/
        //TODO: explo, colo, attack info, attackbox, interactions
        stack2=new Stack();
        exploreLabel = new Label("Send explorer to uncolonized asteroid[Click on name]", skin, "styleTop");
        exploreLabel.setColor(Color.CYAN);
        stack2.add(exploreLabel);
        exploreLabel.setVisible(false);
        colonizeLabel=new Label("Send colonist to uncolonized asteroid[Click on name]", skin, "styleTop");
        colonizeLabel.setColor(Color.CYAN);
        colonizeLabel.setVisible(false);
        stack2.add(colonizeLabel);
        attackLabel=new Label("Send ships to other colony or attack[Click on name]", skin, "styleTop");
        attackLabel.setColor(Color.CYAN);
        attackLabel.setVisible(false);
        stack2.add(attackLabel);



        //table2.addActor(spane);

        //table.add(table5);
       // table4.right().bottom();
        stage.addActor(table);
        stage.addActor(stack1);
        stage.addActor(stack2);

       // stage.addActor(table3);
       // stage.addActor(table5);

        spane.setVisible(false);
        table6.setVisible(false);
        Gdx.input.setInputProcessor(stage);
        stage.addListener(new DragListener(){
            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer){
               // System.out.println("dragstart");
                lastdraggedx=x;
                lastdraggedy=y;
            }
            @Override
            public void drag(InputEvent event, float x, float y, int pointer){
                System.out.println(super.getTapSquareSize());
                setTapSquareSize(super.getTapSquareSize()/8);
                AsteroidColonist.dragScreen(x-lastdraggedx, y-lastdraggedy);
                lastdraggedx=x;
                lastdraggedy=y;
               // System.out.println("drag");
            }
        });



    }

    public void resize(int width, int height, float zoom) {

        stage.getViewport().update(width, height, true);
        for(int i=0; i< citybuttons.size(); i++){
            //citybuttons.get(i).getV
           // System.out.println(citybuttonstartcoords.get(i).getX()/zoom+","+citybuttonstartcoords.get(i).getY()/zoom);
           // System.out.println(camera1x+","+camera1y);

            // gr (0, 479)
            // kl (0, 641)650  => - = 120
            //=> just y difference of 480*zoom
            citybuttons.get(i).setPosition((float)citybuttonstartcoords.get(i).getX()/zoom-(camera1x/zoom), (float)citybuttonstartcoords.get(i).getY()/zoom-((camera1y-(480*zoom))/zoom));
        }
    }

    public void zoomCityButtons(float zoom, Vector3 v){
        this.camera1x= v.x;
        this.camera1y = v.y;
        //for(int i=0; i< citybuttons.size(); i++){
            //citybuttons.get(i).getV
            //System.out.println(citybuttonstartcoords.get(i).getX()*zoom+","+citybuttonstartcoords.get(i).getX());
           // System.out.println(camera1x + "," + camera1y);
           // citybuttons.get(i).setPosition=new Point((int)(citybuttonstartcoords.get(i).getX()/zoom-(camera1x/zoom)), (int)(citybuttonstartcoords.get(i).getY()/zoom-(camera1y/zoom)));
            this.camera1x=camera1x;
            this.camera1y=camera1y;
            //if(zoom==1){
            //    this.camera1x=0;
            //    this.camera1y=0
            //}
       // }
    }

    public void draw(SpriteBatch batch){
        stage.act();
        //tests
        if(imageButtonZoom.isChecked()){
            iszoom = false;
            //System.out.println("de-zoom");
        } else iszoom = true;


        //table.getCell(table2).getActor().getCell(nameLabel).getActor().setText(inp);
        table.draw(batch, 1);
//        table5.draw(batch, 1);
       // table3.draw(batch, 1);
        stack1.draw(batch, 1);
        stack2.draw(batch, 1);

    }

    public void drawOnMap(SpriteBatch batch){
        for(int i=0; i< citybuttons.size(); i++){
            citybuttons.get(i).draw(batch, 1);
        }
    }

    public void addCityButton(String text, int x, int y){
        citybuttons.add(new TextButton(text, skin, "tbStyleTop"));
        if(colonies.get(citybuttons.indexOf(citybuttons.get(citybuttons.size()-1)))!=null){
                 citybuttons.get(citybuttons.size()-1).setColor(colonies.get(citybuttons.indexOf(citybuttons.get(citybuttons.size()-1))).getFaction().getFactioncolor());
             }

            citybuttonstartcoords.add(new Point(x, y));
        citybuttons.get(citybuttons.size()-1).setPosition(
                (float) citybuttonstartcoords.get(citybuttonstartcoords.size() - 1).getX(), (float) citybuttonstartcoords.get(citybuttonstartcoords.size() - 1).getY());
        citybuttons.get(citybuttons.size()-1).addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if ((!((TextButton) event.getListenerActor()).isChecked())
                        ) {
                    //System.out.println("uncheck");
                    iscitymenuechecked = false;
                    System.out.println(citybuttons.indexOf(event.getListenerActor()));
                    //if(colonies.get(citybuttons.indexOf(event.getListenerActor()))!=null) updateColonieButtons(colonies.get(citybuttons.indexOf(event.getListenerActor())));
                    updateColonieButtons(colonies.get(citybuttons.indexOf(event.getListenerActor())));

                    spane.setVisible(false);
                    table6.setVisible(false);
                    viewingcolony =-1;
                    ((TextButton) event.getListenerActor()).toggle();

                } else {
                    //System.out.println("check");
                    iscitymenuechecked = true;
                    table6.getCell(colname).getActor().setText((((TextButton) event.getListenerActor()).getText()));
                   // citybuttons.indexOf(event.getListenerActor());
                    //((TextButton) event.getListenerActor()).
                    //System.out.println(citybuttons.indexOf(event.getListenerActor()));
                   // System.out.println(colonies.get(citybuttons.indexOf(event.getListenerActor())).getFaction());
                    //if(colonies.get(citybuttons.indexOf(event.getListenerActor()))!=null) updateColonieButtons(colonies.get(citybuttons.indexOf(event.getListenerActor())));
                    updateColonieButtons(colonies.get(citybuttons.indexOf(event.getListenerActor())));
                    spane.setVisible(true);
                    table6.setVisible(true);
                    viewingcolony = citybuttons.indexOf(event.getListenerActor());
                    nextColonyStats();
                    ((TextButton) event.getListenerActor()).toggle();

                }
            }
        });

        stage.addActor(citybuttons.get(citybuttons.size() - 1));

    }

    public void debugshape(){
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin();
        table.drawDebug(shapeRenderer);
//        table3.drawDebug(shapeRenderer);
//        table5.drawDebug(shapeRenderer);
        shapeRenderer.end();
    }

    public boolean getIsZoom(){
        return  iszoom;
    }

    public void updateColonies(HashMap<Integer, Colony> colonies){
        this.colonies = colonies;
    }

    public void setPlayerFaction(Faction f){player1=f;}

    //has to be called every turn
    public void nextColonyStats(){
        nextGlobalStats();
        buttonnext.setText("NEXT T "+AsteroidColonist.turn);
        /*  ENERGY,
            POPULATION,
            PRODUCTION,
            WATER,
            ORE,
            DEFENSE,
            HEALTH,
            ATTACK*/
        //System.out.println(viewingcolony);
        if (viewingcolony != -1 && colonies.get(viewingcolony) != null ) {
            //System.out.println("hier");

            if (interaction == Interaction.ATTACKING && fromColony.getStatstock()[StatsTypes.ATTACK.ordinal()] > -1) {
               // System.out.println("ATTACK");
                fromColony.sendFight(colonies.get(viewingcolony));
                interaction = Interaction.NOTHING;
                viewingcolony = fromColonyKey;
                fromColonyKey = -1;
            }
            if (colonies.get(viewingcolony).getFaction() == player1) {

                //System.out.println("asdasd");
                table6.setVisible(true);
                spane.setVisible(true);

                colener.setText("" + colonies.get(viewingcolony).getStatstock()[StatsTypes.ENERGY.ordinal()] + " (+" + colonies.get(viewingcolony).getStatsperturn()[StatsTypes.ENERGY.ordinal()] + ")");
                colpop.setText("" + colonies.get(viewingcolony).getStatstock()[StatsTypes.POPULATION.ordinal()] + " (+" + colonies.get(viewingcolony).getStatsperturn()[StatsTypes.POPULATION.ordinal()] + ")");
                colprod.setText("" + colonies.get(viewingcolony).getStatstock()[StatsTypes.PRODUCTION.ordinal()] + " (+" + colonies.get(viewingcolony).getStatsperturn()[StatsTypes.PRODUCTION.ordinal()] + ")");
                colwat.setText("" + colonies.get(viewingcolony).getStatstock()[StatsTypes.WATER.ordinal()] + " (+" + colonies.get(viewingcolony).getStatsperturn()[StatsTypes.WATER.ordinal()] + ")");
                colore.setText("" + colonies.get(viewingcolony).getStatstock()[StatsTypes.ORE.ordinal()] + " (+" + colonies.get(viewingcolony).getStatsperturn()[StatsTypes.ORE.ordinal()] + ")");
                coldef.setText("" + colonies.get(viewingcolony).getStatstock()[StatsTypes.DEFENSE.ordinal()]);
                colheal.setText("" + colonies.get(viewingcolony).getStatstock()[StatsTypes.HEALTH.ordinal()]);
                colatt.setText("->" + colonies.get(viewingcolony).getStatstock()[StatsTypes.ATTACK.ordinal()]);
                colexpl.setText("->" + colonies.get(viewingcolony).getStatstock()[StatsTypes.EXPLORER.ordinal()]);
                colcolo.setText("->" + colonies.get(viewingcolony).getStatstock()[StatsTypes.COLONIST.ordinal()]);

                if (colonies.get(viewingcolony).getProducingbuilding() != null) {
                    //.println("producing");
                    prodtime.setText("" + colonies.get(viewingcolony).getTurnsToFinish() + " (" +
                            colonies.get(viewingcolony).getProductioninvested()[colonies.get(viewingcolony).getProducingbuilding().ordinal()] + " p)");
                } else prodtime.setText("finished");

                updateColonieButtons(colonies.get(viewingcolony));

                //foreign colony or uncoloniced asteroid
            } else {
                table6.setVisible(false);
                spane.setVisible(false);
                //other faction
                if (colonies.get(viewingcolony) != null ) {
                    //System.out.println("otherfaction");
                   /* System.out.println(fromColony.getStatstock()[StatsTypes.HEALTH.ordinal()]);
                    if (interaction == Interaction.ATTACKING && fromColony.getStatstock()[StatsTypes.HEALTH.ordinal()] > -1)
                        fromColony.sendFight(colonies.get(viewingcolony));
*/
                }

            }
        }else{
            //uncolonized
            //System.out.println("uncolonized");

             if(interaction == Interaction.COLONIZING && fromColony.getStatstock()[StatsTypes.COLONIST.ordinal()] > -1){
                //System.out.println("COLONIZE");
                AsteroidColonist.addColony(fromColony.getFaction(), viewingcolony, fromColony.getFaction().getName()+ "_" + (fromColony.getFaction().getColonies().size()+1));
                interaction = Interaction.NOTHING;
                fromColony.getStatstock()[StatsTypes.COLONIST.ordinal()]-=1;
                viewingcolony = fromColonyKey;
                fromColonyKey = -1;

            } else if(interaction == Interaction.EXPLORING && fromColony.getStatstock()[StatsTypes.EXPLORER.ordinal()] > -1&&colonies.get(viewingcolony)==null&&viewingcolony!=-1){
              //  System.out.println("EXPLORE");
                 fromColony.getStatstock()[StatsTypes.EXPLORER.ordinal()]-=1;
                 AsteroidColonist.showAsteroidRes(viewingcolony, true);
                 interaction = Interaction.NOTHING;
                 viewingcolony = fromColonyKey;
                 fromColonyKey = -1;
            }
        }

    }

    //has to be called every turn, before this, the playerfaction has to be set
    public void nextGlobalStats(){
        globalenergy.setText(""+player1.getStatstock()[StatsTypes.ENERGY.ordinal()]+"(+"+player1.getStatsperturn()[StatsTypes.ENERGY.ordinal()]+")");
        globalpopulation.setText("" + player1.getStatstock()[StatsTypes.POPULATION.ordinal()] + "(+" + player1.getStatsperturn()[StatsTypes.POPULATION.ordinal()] + ")");
        globalproduction.setText("" + player1.getStatstock()[StatsTypes.PRODUCTION.ordinal()] + "(+" + player1.getStatsperturn()[StatsTypes.PRODUCTION.ordinal()] + ")");
        globalore.setText(""+player1.getStatstock()[StatsTypes.ORE.ordinal()]+"(+"+player1.getStatsperturn()[StatsTypes.ORE.ordinal()]+")");
        globalwater.setText(""+player1.getStatstock()[StatsTypes.WATER.ordinal()]+"(+"+player1.getStatsperturn()[StatsTypes.WATER.ordinal()]+")");


    }
    //inside the colony interface
    public void updateColonieButtons(Colony colony){

        //System.out.println(colony.getFaction());
        //nextColonyStats();

            for(int i=0; i< Buildings.values().length;i++) {
                buildingsbuttons.get(i).setStyle(tbStyleTop);
                if(buildingsbuttons.get(i).isChecked())buildingsbuttons.get(i).toggle();

            }



            //if a colony
            if(colony==null)return;

            for(int i=0; i< colony.getBuildingses().size();i++) {
                //building is finished
                buildingsbuttons.get(colony.getBuildingses().get(i).ordinal()).setStyle(tbfinished);
            }
        //producingbuilding
        if(colony.getProducingbuilding()!=null)buildingsbuttons.get(colony.getProducingbuilding().ordinal()).setStyle(tbproducing);

        //cityname
        colname.setText(colony.getName());

        //turns left until finished
        if(colony.getProducingbuilding()!=null)
        //prodtime.setText("turns: "+(Buildings.values()[colony.getProducingbuilding().ordinal()].prodValue-colony.getProductioninvested()[colony.getProducingbuilding().ordinal()]));
            prodtime.setText("" + colony.getTurnsToFinish() + " (" +
                    colony.getProductioninvested()[colony.getProducingbuilding().ordinal()] + " p)");
        //able to build mines?

        if(colony.getMinesused().length>colony.getMinescount()){
            boolean isspaceformines = false;
            for(int i=0; i< colony.getMinesused().length;i++){
                if(colony.getMinesused()[i]== MineType.NO_MINE)isspaceformines=true;
            }
            if(!isspaceformines){
                buildingsbuttons.get(Buildings.WATER_MINE.ordinal()).setVisible(false);
                buildingsbuttons.get(Buildings.ORE_MINE.ordinal()).setVisible(false);
                buildingsbuttons.get(Buildings.FARM.ordinal()).setVisible(false);

            } else{
                    buildingsbuttons.get(Buildings.WATER_MINE.ordinal()).setVisible(true);
                    buildingsbuttons.get(Buildings.ORE_MINE.ordinal()).setVisible(true);
                    buildingsbuttons.get(Buildings.FARM.ordinal()).setVisible(true);
                if(buildingsbuttons.get(Buildings.WATER_MINE.ordinal()).getStyle()==tbfinished) buildingsbuttons.get(Buildings.WATER_MINE.ordinal()).setStyle(tbStyleTop);
                if(buildingsbuttons.get(Buildings.ORE_MINE.ordinal()).getStyle()==tbfinished)     buildingsbuttons.get(Buildings.ORE_MINE.ordinal()).setStyle(tbStyleTop);
                if(buildingsbuttons.get(Buildings.FARM.ordinal()).getStyle()==tbfinished)    buildingsbuttons.get(Buildings.FARM.ordinal()).setStyle(tbStyleTop);

            }
        } else{
            buildingsbuttons.get(Buildings.WATER_MINE.ordinal()).setVisible(false);
            buildingsbuttons.get(Buildings.ORE_MINE.ordinal()).setVisible(false);
            buildingsbuttons.get(Buildings.FARM.ordinal()).setVisible(false);
        }

        if(buildingsbuttons.get(Buildings.BATTLESHIP.ordinal()).getStyle()==tbfinished)buildingsbuttons.get(Buildings.BATTLESHIP.ordinal()).setStyle(tbStyleTop);
        if(buildingsbuttons.get(Buildings.EXPLORER.ordinal()).getStyle()==tbfinished)buildingsbuttons.get(Buildings.EXPLORER.ordinal()).setStyle(tbStyleTop);
        if(buildingsbuttons.get(Buildings.COLONIST.ordinal()).getStyle()==tbfinished)buildingsbuttons.get(Buildings.COLONIST.ordinal()).setStyle(tbStyleTop);

        //}
    }
    //on the map
    public void updateCityButtons(int location, String text){
        citybuttons.get(location).setText(text);
    }

    public int getCameraJumpCityX(){
        return cameraJumpCityx;
    }
    public int getCameraJumpCityY(){
        return cameraJumpCityy;
    }
}
