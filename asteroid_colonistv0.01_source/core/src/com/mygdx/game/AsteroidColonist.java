package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 *  by Sehbrrt
 */
public class AsteroidColonist extends ApplicationAdapter{
	SpriteBatch batch;
	Texture img;
	MapGenerator mapGenerator;

    private static OrthographicCamera camera1;
    private static OrthographicCamera camera2;
    private static OrthographicCamera uicamera;
    private static OrthographicCamera uiworldcamera;

    private static World world;

    private static BitmapFont bmf;

    private static UI ui;

    private boolean isdebug = false;

    private static float camera1startposx;
    private static float camera1startposy;

    public static int turn = 0;

    private int xbefore=0;
    private int ybefore=0;

    private static float dragvelocity=0.25f;


    private Music soundtrack;
    private String[] musicfiles = {"sound/asteroid_colonist_spacelife4.ogg", "sound/asteroid_colonist_spacelife7.ogg",
                                    "sound/asteroid_colonist_spacelife11x.ogg", "sound/asteroid_colonist_spacelife14x.ogg"};
    private int currentmusic;
    final Music.OnCompletionListener musiclistener = new Music.OnCompletionListener() {
        @Override
        public void onCompletion(Music music) {
            if(currentmusic==3)currentmusic=0;
            else currentmusic++;
            soundtrack.dispose();
            soundtrack = Gdx.audio.newMusic(Gdx.files.internal(musicfiles[currentmusic]));

            //soundtrack.play();
        }
    };
	
	@Override
	public void create () {
        bmf = new BitmapFont(Gdx.files.internal("testfont.fnt"));
        ui = new UI();
		batch = new SpriteBatch();
		img = new Texture("spacelifebg.png");
		mapGenerator = new MapGenerator();

        currentmusic=1;

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        camera1 = new OrthographicCamera();
        //camera1.setToOrtho(false, width, width * (height / width));
        camera1.setToOrtho(false);
        camera1.update();
       // System.out.println(""+camera1.position);
        //camera1.position.x = 0;
       // camera1.position.y = 0;
        camera1startposx=camera1.viewportWidth / 2f;
        camera1startposy=camera1.viewportHeight / 2f;
        camera1.position.set(camera1startposx, camera1startposy, 0);

        camera2 = new OrthographicCamera(width, width * (height / width));
       // camera2.position.set(camera1.viewportWidth / 2f, camera1.viewportHeight / 2f, 0);

        camera2.setToOrtho(false);
        camera2.update();
        camera2.translate(+300, +300);

        camera1.zoom =0.25f;
        camera2.zoom =0.25f;

        uiworldcamera =  new OrthographicCamera();
        uiworldcamera.setToOrtho(false);
        uiworldcamera.update();
        uiworldcamera.position.set(camera1.viewportWidth / 2f, camera1.viewportHeight / 2f, 0);
        uiworldcamera.zoom = 1f;


        uicamera = new OrthographicCamera(width, width * (height / width));
        uicamera.setToOrtho(false);
        uicamera.position.set(uicamera.viewportWidth / 2f, uicamera.viewportHeight / 2f, 0);
        uicamera.update();

        world = new World(500, 500,24);


        Pixmap curs = new Pixmap(Gdx.files.internal("cursor.png"));
        Gdx.input.setCursorImage(curs, 0, 0);
        curs.dispose();


        camera1.update();
        camera2.update();
        uiworldcamera.update();
       // camera1.zoom = 0.25f;
       // camera2.zoom = 0.25f;
       // uiworldcamera.zoom = 1f;
        ui.zoomCityButtons(camera1.zoom, camera1.unproject(new Vector3(0, 0, 0)));
        ui.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera1.zoom);
        camera1.update();
        camera2.update();
        uiworldcamera.update();
        ui.nextColonyStats();




        soundtrack = Gdx.audio.newMusic(Gdx.files.internal(musicfiles[currentmusic]));
        soundtrack.setOnCompletionListener(musiclistener);
        soundtrack.play();

        //Gdx.input.setInputProcessor(new InputTouchDragProcessor(this));


	}

	@Override
	public void render () {
        if(!soundtrack.isPlaying()){
            soundtrack.setOnCompletionListener(musiclistener);
            soundtrack.play();
        }
        doInput();
        camera1.update();
        camera2.update();
        uiworldcamera.update();
        ui.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera1.zoom);

        batch.setProjectionMatrix(camera2.combined);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
		batch.draw(img, 0, 0);
        batch.setProjectionMatrix(camera1.combined);
        //batch.setProjectionMatrix(camera2.combined);
		//mapGenerator.draw(batch);
        world.draw(batch);
        batch.setProjectionMatrix(uiworldcamera.combined);
        ui.drawOnMap(batch);
        batch.setProjectionMatrix(uicamera.combined);
        ui.draw(batch);
		batch.end();
        if(isdebug)ui.debugshape();
       // System.out.println(camera1.unproject(new Vector3(0,0,0)));
       // System.out.println(camera1startposx+","+camera1startposy);
       // System.out.println(camera1.position.x+"v"+camera1.viewportWidth);

	}

    //testinputs
    private void doInput(){
        if (Gdx.input.isKeyPressed(Input.Keys.Y)||ui.getIsZoom()) {
            camera1.zoom = 0.25f;
            camera2.zoom = 0.25f;
            uiworldcamera.zoom = 1f;
            ui.zoomCityButtons(camera1.zoom, camera1.unproject(new Vector3(0, 0, 0)));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.X)||!ui.getIsZoom()) {
            camera1.zoom = 1f;
            camera2.zoom = 1f;
            uiworldcamera.zoom = 1f;
            ui.zoomCityButtons(camera1.zoom, camera1.unproject(new Vector3(0, 0, 0)));

        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera1.translate(-2, 0, 0);
            camera2.translate(-1, 0, 0);
            ui.zoomCityButtons(camera1.zoom, camera1.unproject(new Vector3(-2/camera1.zoom, 0, 0)));
            //uiworldcamera.translate(-2/camera1.zoom, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera1.translate(2, 0, 0);
            camera2.translate(1, 0, 0);
            ui.zoomCityButtons(camera1.zoom, camera1.unproject(new Vector3(+2/camera1.zoom, 0, 0)));
            //uiworldcamera.translate(2/camera1.zoom, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera1.translate(0, -2, 0);
            camera2.translate(0, -1, 0);
            ui.zoomCityButtons(camera1.zoom, camera1.unproject(new Vector3(0, +2/camera1.zoom, 0)));
            //uiworldcamera.translate(0, -2/camera1.zoom, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera1.translate(0, 2, 0);
            camera2.translate(0, 1, 0);
            ui.zoomCityButtons(camera1.zoom, camera1.unproject(new Vector3(0, -2/camera1.zoom, 0)));
            //uiworldcamera.translate(0, 2/camera1.zoom, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            world.showStructures = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            world.showStructures = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
            isdebug = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_4)) {
            isdebug = false;
        }

        /*if(Gdx.input.isTouched()){
            System.out.println(Gdx.input.getX());
            if(xbefore-Gdx.input.getX()>10){
                camera1.translate(-2, 0, 0);
                camera2.translate(-1, 0, 0);
                ui.zoomCityButtons(camera1.zoom, camera1.unproject(new Vector3(-2/camera1.zoom, 0, 0)));
            } else if(xbefore-Gdx.input.getX()<-10){
                camera1.translate(2, 0, 0);
                camera2.translate(1, 0, 0);
                ui.zoomCityButtons(camera1.zoom, camera1.unproject(new Vector3(+2/camera1.zoom, 0, 0)));
            }

            if(ybefore-Gdx.input.getY()>10){
                camera1.translate(0, -2, 0);
                camera2.translate(0, -1, 0);
                ui.zoomCityButtons(camera1.zoom, camera1.unproject(new Vector3(0, +2/camera1.zoom, 0)));
            } else if(xbefore-Gdx.input.getX()<-10){
                camera1.translate(0, 2, 0);
                camera2.translate(0, 1, 0);
                ui.zoomCityButtons(camera1.zoom, camera1.unproject(new Vector3(0, +2/camera1.zoom, 0)));
            }
            //camera1.translate(-2, 0, 0);
            //camera2.translate(-1, 0, 0);
            xbefore=Gdx.input.getX();
            ybefore=Gdx.input.getY();

        }*/
    }

    public static BitmapFont getBmf(){
        return bmf;
    }

    public static UI getUI(){
        return ui;
    }

    public static void next(){
        turn++;
        world.next();
        ui.nextColonyStats();
    }

    public static void jumpbeforenext(int befx, int befy){

        //for centering to the next colony, +24 and -60, so that it is not at the bottom right under the next button/zoom
        camera1.position.x= ui.getCameraJumpCityX()+24;
        camera1.position.y= camera1startposy+ui.getCameraJumpCityY()-60;

        camera2.position.x= camera1startposx+((ui.getCameraJumpCityX()+24-camera1startposx)/2);
        camera2.position.y= camera1startposy+((ui.getCameraJumpCityY()-60)/2);
    }
    @Override
    public void dispose(){
        super.dispose();
    }

    public static void dragScreen(float screenx, float screeny){
        //System.out.println(Gdx.input.getX());
        //if() {
            //if(screenx>10){
            camera1.translate((int) (-2 * dragvelocity * screenx), 0, 0);
            camera2.translate((int) (-1 * dragvelocity * screenx), 0, 0);
            ui.zoomCityButtons(camera1.zoom, camera1.unproject(new Vector3(-2 / camera1.zoom, 0, 0)));


            camera1.translate(0, (int) (-2 * dragvelocity * screeny), 0);
            camera2.translate(0, (int) (-1 * dragvelocity * screeny), 0);
            ui.zoomCityButtons(camera1.zoom, camera1.unproject(new Vector3(0, +2 / camera1.zoom, 0)));



    }

    public static void addColony(Faction f, int asteroid, String name){
        world.addColonyFull(f, asteroid, name);
    }

    public static void showAsteroidRes(int ast, boolean isshow){
        world.showAsteroidRes(ast, isshow);
    }




}
