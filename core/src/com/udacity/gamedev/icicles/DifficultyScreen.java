package com.udacity.gamedev.icicles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.badlogic.gdx.graphics.Texture.TextureFilter;
import static com.udacity.gamedev.icicles.Constants.BACKGROUND_COLOR;
import static com.udacity.gamedev.icicles.Constants.DIFFICULTY_BUBBLE_RADIUS;
import static com.udacity.gamedev.icicles.Constants.DIFFICULTY_LABLE_SCALE;
import static com.udacity.gamedev.icicles.Constants.DIFFICULTY_WORLD_SIZE;
import static com.udacity.gamedev.icicles.Constants.EASY_CENTER;
import static com.udacity.gamedev.icicles.Constants.EASY_COLOR;
import static com.udacity.gamedev.icicles.Constants.EASY_LABEL;
import static com.udacity.gamedev.icicles.Constants.HARD_CENTER;
import static com.udacity.gamedev.icicles.Constants.HARD_COLOR;
import static com.udacity.gamedev.icicles.Constants.HARD_LABEL;
import static com.udacity.gamedev.icicles.Constants.MEDIUM_CENTER;
import static com.udacity.gamedev.icicles.Constants.MEDIUM_COLOR;
import static com.udacity.gamedev.icicles.Constants.MEDIUM_LABEL;

/**
 * Created by Owner on 3/29/2017.
 */

public class DifficultyScreen extends InputAdapter implements Screen {

    public static final String TAG = DifficultyScreen.class.getName();

    IciclesGame game;

    ShapeRenderer renderer;
    SpriteBatch batch;
    FitViewport viewport;

    BitmapFont font;

    public DifficultyScreen(IciclesGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        renderer = new ShapeRenderer();
        batch = new SpriteBatch();

        viewport = new FitViewport(DIFFICULTY_WORLD_SIZE, DIFFICULTY_WORLD_SIZE);
        Gdx.input.setInputProcessor(this);

        font = new BitmapFont();
        font.getData().setScale(DIFFICULTY_LABLE_SCALE);
        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

    }

    @Override
    public void render(float delta) {
        //Apply the viewport
        viewport.apply();
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Set the ShapeRenderer's projection matrix
        renderer.setProjectionMatrix(viewport.getCamera().combined);

        //Draw the buttons
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(EASY_COLOR);
        renderer.circle(EASY_CENTER.x, EASY_CENTER.y, DIFFICULTY_BUBBLE_RADIUS);
        renderer.setColor(MEDIUM_COLOR);
        renderer.circle(MEDIUM_CENTER.x, MEDIUM_CENTER.y, DIFFICULTY_BUBBLE_RADIUS);
        renderer.setColor(HARD_COLOR);
        renderer.circle(HARD_CENTER.x, HARD_CENTER.y, DIFFICULTY_BUBBLE_RADIUS);
        renderer.end();

        //Set the SpriteBatch's projection matrix
        batch.setProjectionMatrix(viewport.getCamera().combined);

        //Write the labels on the buttons
        batch.begin(); //TODO what does this GlyphLayout do and why is it necessary?
        final GlyphLayout easyLayout = new GlyphLayout(font, EASY_LABEL);
        font.draw(batch, EASY_LABEL, EASY_CENTER.x, EASY_CENTER.y + easyLayout.height / 2, 0 , Align.center, false);
        final GlyphLayout mediumLayout = new GlyphLayout(font, MEDIUM_LABEL);
        font.draw(batch, MEDIUM_LABEL, MEDIUM_CENTER.x, MEDIUM_CENTER.y + mediumLayout.height / 2, 0 , Align.center, false);
        final GlyphLayout hardLayout = new GlyphLayout(font, HARD_LABEL);
        font.draw(batch, HARD_LABEL, HARD_CENTER.x, HARD_CENTER.y + hardLayout.height / 2, 0 , Align.center, false);
        batch.end();

    }

    @Override
    public void resize(int width, int height) {
        //update the viewport
        viewport.update(width, height, true);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button){
        //Unproject the touch from the screen to the world
        Vector2 worldTouch = viewport.unproject(new Vector2(screenX, screenY));

        //Check if the touch was inside a button, and if it was launch the appropriate difficulty game
        if (worldTouch.dst (EASY_CENTER) < DIFFICULTY_BUBBLE_RADIUS) {
            game.showIciclesScreen(Constants.Difficulty.EASY);
        }

        if (worldTouch.dst (MEDIUM_CENTER) < DIFFICULTY_BUBBLE_RADIUS) {
            game.showIciclesScreen(Constants.Difficulty.MEDIUM);
        }

        if (worldTouch.dst (HARD_CENTER) < DIFFICULTY_BUBBLE_RADIUS) {
            game.showIciclesScreen(Constants.Difficulty.HARD);
        }

        return true;
    }
}
