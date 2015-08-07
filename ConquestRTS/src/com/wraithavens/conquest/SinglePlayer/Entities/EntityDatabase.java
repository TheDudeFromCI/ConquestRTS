package com.wraithavens.conquest.SinglePlayer.Entities;

import java.util.ArrayList;

public class EntityDatabase{
	private final ArrayList<Entity> entities = new ArrayList();
	public void addEntity(Entity e){
		entities.add(e);
	}
	public void dispose(){
		for(Entity e : entities)
			e.dispose();
		entities.clear();
	}
	public void render(){
		for(Entity e : entities)
			e.render();
	}
}
