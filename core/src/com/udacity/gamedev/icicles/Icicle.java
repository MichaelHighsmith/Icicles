package com.udacity.gamedev.icicles;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import static com.udacity.gamedev.icicles.Constants.*;

/**
 * Created by Owner on 3/29/2017.
 */

public class Icicle {

    public static final String TAG = Icicle.class.getName();

    Vector2 position;
    Vector2 velocity;

    //Constructor that sets the position
    public Icicle(Vector2 position) {
        this.position = position;
        this.velocity = new Vector2();
    }

    // add a render function that takes a ShapeRenderer
    public void render(ShapeRenderer renderer) {
        renderer.setColor(ICICLE_COLOR);
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.triangle(position.x,
                position.y,
                position.x - ICICLES_WIDTH / 2,
                position.y + ICICLES_HEIGHT,
                position.x + ICICLES_WIDTH / 2,
                position.y + ICICLES_HEIGHT);
    }

    public void update(float delta) {
        //update velocity using the icicles acceleration constant
        velocity.mulAdd(ICICLES_ACCELERATION, delta);
        //update position using velocity
        position.mulAdd(velocity, delta);
    }





}
