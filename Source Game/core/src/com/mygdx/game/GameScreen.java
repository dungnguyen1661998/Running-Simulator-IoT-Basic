package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import stages.GameStage;

public class GameScreen implements Screen{
	private GameStage stage;
	SpriteBatch batch;
	public static String gO="";
	public static int score = 0;
	public static int highScore = 0;
    public GameScreen() {
        stage = new GameStage();
        batch = new SpriteBatch();
    }
    public static void incHighScore() {
    	if(highScore < score) highScore = score;
    }
    
    public static void incScore() {

    	score ++;
    }
    public static void resetScore() {
    	score = 0;
    }
    public static int getScore() {
    	return score;
    }
    public static void getGO() {
    	gO = "Game Over";
    }
    public static void resetGO() {
    	gO = "";
    }
    @Override
    public void render(float delta) {
    	BitmapFont yourBitmapFontName = new BitmapFont();
        //Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Update the stage
        stage.draw();
        stage.act(delta);
        batch.begin();
        String text = "Score : "+score;
        String text2 = "High Score : "+highScore;
       
        yourBitmapFontName.setColor(10f, 10f, 10f, 11.0f);
        yourBitmapFontName.draw(batch, text, 850, 520);     
        yourBitmapFontName.draw(batch, text2, 850, 550); 
        yourBitmapFontName.getData().setScale(1.5f,1.5f);
        yourBitmapFontName.setColor(2f, 2f, 0f, 1.0f);
        yourBitmapFontName.draw(batch, gO, 475, 535);
        batch.end();
    }

    

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
