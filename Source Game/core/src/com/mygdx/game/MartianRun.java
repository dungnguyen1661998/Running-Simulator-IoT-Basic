package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.GameScreen;

public class MartianRun extends Game{
	@Override
    public void create() {
        setScreen(new GameScreen());
    }
}
