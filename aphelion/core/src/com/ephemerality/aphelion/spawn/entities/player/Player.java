package com.ephemerality.aphelion.spawn.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.ephemerality.aphelion.framework.GameManager;
import com.ephemerality.aphelion.graphics.LoadManager;
import com.ephemerality.aphelion.graphics.ScreenManager;
import com.ephemerality.aphelion.input.InputManager;
import com.ephemerality.aphelion.spawn.entities.mob.Mob;
import com.ephemerality.aphelion.spawn.entities.player.inventory.Inventory;
import com.ephemerality.aphelion.spawn.world.Warp;
import com.ephemerality.aphelion.util.Direction;
import com.ephemerality.aphelion.util.Stats;

public class Player extends Mob {

	public Inventory inventory;
	public ScreenManager screen;
	boolean isWarped;
	
//testing purposes - not permanent
	int speed = 10;
	
	public Player(ScreenManager screen, LoadManager assets, float x, float y) {
		super(x, y, 128, 64, Mob.PLAYER, assets, LoadManager.MONSTER_SCML, new Stats());
		this.screen = screen;		
		screen.setPosition(x, y);
		inventory = new Inventory();
	}
	@Override
	public void update() {
		behavior();
		updateAnim();
		inventory.update();
	}
	@Override
	public void behavior() {
		Warp warp = warp();
		if(warp != null) {
			if(!isWarped) {
				GameManager.requestWarp(warp);
				isWarped = true;
			}
		}else {
			isWarped = false;
		}
		
		
		
		if(!attacking && (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE))) {
			attackStartedThisFrame = true;
		}
		if(attacking) {
			attack();
		}
		
		
		
		boolean movedLastFrame = moving;
		for(int i = 0; i < speed; i++){	
			moving = updateMove();
		}
		if(moving != movedLastFrame) {
			movingChangedThisFrame = true;
		}else {
			movingChangedThisFrame = false;
		}
	}
	@Override
	public boolean updateMove() {
		boolean up = InputManager.up;
		boolean down = InputManager.down;
		boolean left = InputManager.left;
		boolean right = InputManager.right;
		boolean moved = false;
		if(!(up && down)){
			if(up) {
				dir = Direction.NORTH;
				if(move()) {
					screen.translate(dir);					
					moved = true;
				}
			}
			if(down) {
				dir = Direction.SOUTH;
				if(move()) {
					screen.translate(dir);
					moved = true;
				}
			}
		}
		if(!(left && right)){
			if(left) {
				dir = Direction.WEST;
				if(move()) {
					screen.translate(dir);
					moved = true;
				}
			}
			if(right) {
				dir = Direction.EAST;
				if(move()) {
					screen.translate(dir);	
					moved = true;
				}
			}
		}
		return moved;
	}
}