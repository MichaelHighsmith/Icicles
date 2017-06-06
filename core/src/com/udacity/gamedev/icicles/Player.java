package com.udacity.gamedev.icicles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.badlogic.gdx.Input.Keys;
import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import static com.udacity.gamedev.icicles.Constants.ACCELEROMETER_SENSITIVITY;
import static com.udacity.gamedev.icicles.Constants.GRAVITATIONAL_ACCELERATION;
import static com.udacity.gamedev.icicles.Constants.PLAYER_COLOR;
import static com.udacity.gamedev.icicles.Constants.PLAYER_HEAD_HEIGHT;
import static com.udacity.gamedev.icicles.Constants.PLAYER_HEAD_RADIUS;
import static com.udacity.gamedev.icicles.Constants.PLAYER_HEAD_SEGMENTS;
import static com.udacity.gamedev.icicles.Constants.PLAYER_LIMB_WIDTH;
import static com.udacity.gamedev.icicles.Constants.PLAYER_MOVEMENT_SPEED;

/**
 * Created by Owner on 3/29/2017.
 */

public class Player {

    public static final String TAG = Player.class.getName();

    Vector2 position;

    Viewport viewport;

    int deaths;

    public Player(Viewport viewport) {
        this.viewport = viewport;
        deaths = 0;
        init();
    }

    //moves the character to the bottom center of the screen
    public void init() {
        position = new Vector2(viewport.getWorldWidth() / 2, PLAYER_HEAD_HEIGHT);
    }


    //Add a render function that draws the stick figure
    public void render(ShapeRenderer renderer){
        renderer.setColor(PLAYER_COLOR);
        renderer.set(ShapeType.Filled);
        renderer.circle(position.x, position.y, PLAYER_HEAD_RADIUS, PLAYER_HEAD_SEGMENTS);

        //Make points for the top and bottom of the torso
        Vector2 torsoTop = new Vector2(position.x, position.y - PLAYER_HEAD_RADIUS);
        Vector2 torsoBottom = new Vector2(position.x, torsoTop.y - 2 * PLAYER_HEAD_RADIUS);

        renderer.rectLine(torsoTop, torsoBottom, PLAYER_LIMB_WIDTH);
        renderer.rectLine(torsoTop.x, torsoTop.y, torsoTop.x + PLAYER_HEAD_RADIUS, torsoTop.y - PLAYER_HEAD_RADIUS, PLAYER_LIMB_WIDTH);
        renderer.rectLine(torsoTop.x, torsoTop.y, torsoTop.x - PLAYER_HEAD_RADIUS, torsoTop.y - PLAYER_HEAD_RADIUS, PLAYER_LIMB_WIDTH);
        renderer.rectLine(torsoBottom.x, torsoBottom.y, torsoBottom.x + PLAYER_HEAD_RADIUS, torsoBottom.y - PLAYER_HEAD_RADIUS, PLAYER_LIMB_WIDTH);
        renderer.rectLine(torsoBottom.x, torsoBottom.y, torsoBottom.x - PLAYER_HEAD_RADIUS, torsoBottom.y - PLAYER_HEAD_RADIUS, PLAYER_LIMB_WIDTH);

    }

    public void update(float delta) { //TODO What is this delta going in though?
        //move the player when the arrow keys are pressed
        if (Gdx.input.isKeyPressed(Keys.LEFT)){
            position.x -= delta * PLAYER_MOVEMENT_SPEED;
        } else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            position.x += delta * PLAYER_MOVEMENT_SPEED;
        }

        //compute the accelerometer when tilting the phone
        float accelerometerInput = Gdx.input.getAccelerometerY() / (GRAVITATIONAL_ACCELERATION * ACCELEROMETER_SENSITIVITY);

        position.x += delta * accelerometerInput * PLAYER_MOVEMENT_SPEED;

        ensureInBounds();
    }

    private void ensureInBounds() {
        if (position.x - PLAYER_HEAD_RADIUS < 0){
            position.x = PLAYER_HEAD_RADIUS;
        }
        if (position.x + PLAYER_HEAD_RADIUS > viewport.getWorldWidth()){
            position.x = viewport.getWorldWidth() - PLAYER_HEAD_RADIUS;
        }
    }

    public boolean hitByIcicle(Icicles icicles) {
        boolean isHit = false;

        for(Icicle icicle : icicles.icicleList) {
            if (icicle.position.dst(position) < PLAYER_HEAD_RADIUS) {
                isHit = true;
            }
        }

        if (isHit) {
            deaths += 1;
        }

        return isHit;
    }

}
