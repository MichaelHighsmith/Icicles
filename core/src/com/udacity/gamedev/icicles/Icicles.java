package com.udacity.gamedev.icicles;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.icicles.Constants.Difficulty;

import static com.udacity.gamedev.icicles.Constants.ICICLES_HEIGHT;

/**
 * Created by Owner on 3/29/2017.
 */

//This class creates an array of icicles that each have the functionality from the class Icicle.  These are what fall in the game.

public class Icicles {
    public static final String TAG = Icicles.class.getName();

    //Add a counter for how many icicles have been dodged
    int iciclesDodged;

    //Add a Difficulty (from the constants enum)
    Difficulty difficulty;

    //Use a DelayedRemovalArray instead of a regular Array so that we can get rid of icicles after they fall past the bottom of the screen
    DelayedRemovalArray<Icicle> icicleList;
    Viewport viewport;

    public Icicles(Viewport viewport, Difficulty difficulty){
        this.difficulty = difficulty;
        this.viewport = viewport;
        init();
    }

    //Initialize the array of icicles
    public void init() {
        icicleList = new DelayedRemovalArray<Icicle>(false, 100);
        iciclesDodged = 0;
    }

    public void update(float delta) {

        //Set how many icicles are spawning each second at the top.
        if (MathUtils.random() < delta * difficulty.spawnRate) {
            Vector2 newIciclePosition = new Vector2(
                    MathUtils.random() * viewport.getWorldWidth(), viewport.getWorldHeight()
            );
            Icicle newIcicle = new Icicle(newIciclePosition);
            icicleList.add(newIcicle);
        }
        //update each icicle
        for (Icicle icicle : icicleList) {
            icicle.update(delta);
        }

        //begin the removal session
        icicleList.begin();
        for (int i = 0; i < icicleList.size; i++){
            if (icicleList.get(i).position.y < -ICICLES_HEIGHT) {
                iciclesDodged += 1;
                icicleList.removeIndex(i);
            }
        }
        icicleList.end();
    }

    public void render(ShapeRenderer renderer){
        renderer.setColor(Constants.ICICLE_COLOR);

        for (Icicle icicle : icicleList) {
            icicle.render(renderer);
        }
    }
}
