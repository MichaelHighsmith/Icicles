package com.udacity.gamedev.icicles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.udacity.gamedev.icicles.Constants.Difficulty;

import static com.badlogic.gdx.graphics.Texture.TextureFilter;
import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import static com.udacity.gamedev.icicles.Constants.BACKGROUND_COLOR;
import static com.udacity.gamedev.icicles.Constants.HUD_FONT_REFERENCE_SCREEN_SIZE;
import static com.udacity.gamedev.icicles.Constants.HUD_MARGIN;
import static com.udacity.gamedev.icicles.Constants.ICICLE_COLOR;
import static com.udacity.gamedev.icicles.Constants.WORLD_SIZE;
/**
 * Created by Owner on 3/29/2017.
 */

public class IciclesScreen extends InputAdapter implements Screen {

    public static final String TAG = IciclesScreen.class.getName();

    IciclesGame game;
    Difficulty difficulty;

    ExtendViewport iciclesViewport;
    ShapeRenderer renderer;

    Icicles icicles;
    Player player;

    //add a ScreenViewport for the HUD and spritebatch/bitmap for the text to be displayed in it
    ScreenViewport hudViewport;
    SpriteBatch batch;
    BitmapFont font;

    int topScore;

    //Accept a Difficulty in the constructor
    public IciclesScreen(IciclesGame game, Difficulty difficulty) {
        this.difficulty = difficulty;
        this.game = game;
    }

    @Override
    public void show() {
        //Initialize the viewport using the world size constant
        iciclesViewport = new ExtendViewport(WORLD_SIZE, WORLD_SIZE);

        //Initialize the shapeRenderer
        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);

        //Initialize the HUD viewport, SpriteBatch, and BitmapFont
        hudViewport = new ScreenViewport();
        batch = new SpriteBatch();
        font = new BitmapFont();
        //Give the font a linear TextureFilter
        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);


        icicles = new Icicles(iciclesViewport, difficulty);
        player = new Player(iciclesViewport);

        Gdx.input.setInputProcessor(this);

        topScore = 0;

    }

    @Override
    public void render(float delta) {
        //Call update on player for new positions
        player.update(delta);
        icicles.update(delta);

        //Check each frame to see if the player is hit by the Icicle (this method is declared in Player and is a boolean)
        if (player.hitByIcicle(icicles)){
            icicles.init();
        }

        //Apply the viewport
        iciclesViewport.apply(true);

        //Clear the screen to the background color
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Set the ShapeRenderer's projection matrix
        renderer.setProjectionMatrix(iciclesViewport.getCamera().combined);

        //draw the icicle
        renderer.begin(ShapeType.Filled);
        renderer.setColor(ICICLE_COLOR);
        icicles.render(renderer);
        player.render(renderer);
        renderer.end();

        //Set up everything for the HUD
        topScore = Math.max(topScore, icicles.iciclesDodged);
        hudViewport.apply();
        batch.setProjectionMatrix(hudViewport.getCamera().combined);
        batch.begin();
        font.draw(batch, "Deaths: " + player.deaths + "\nDifficulty: " + difficulty.label, HUD_MARGIN, hudViewport.getWorldHeight() - HUD_MARGIN);
        font.draw(batch, "Score: " + icicles.iciclesDodged + "\nTop SCore: " + topScore, hudViewport.getWorldWidth() - HUD_MARGIN, hudViewport.getWorldHeight() - HUD_MARGIN, 0, Align.right, false);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        //Ensure that the viewport updates correctly
        iciclesViewport.update(width, height, true);

        //Update the HUD viewport and set the font scale
        hudViewport.update(width, height, true);
        font.getData().setScale(Math.min(width, height) / HUD_FONT_REFERENCE_SCREEN_SIZE);


        player.init();
        icicles.init();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        renderer.dispose();

    }

    @Override
    public void dispose() {
        renderer.dispose();
        batch.dispose();
        font.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button){
        //Tell IciclesGame to show the difficulty screen
        game.showDifficultyScreen();
        return true;
    }


}
