package com.wraithavens.conquest.SinglePlayer.Entities;

import java.util.Arrays;

public class EntityList{
	private Entity[] entities = new Entity[100];
	private int size;
	public void add(Entity e){
		if(size==entities.length)
			entities = Arrays.copyOf(entities, entities.length+50);
		entities[size] = e;
		size++;
	}
	public void clear(){
		for(int i = 0; i<size; i++)
			entities[i] = null;
		size = 0;
	}
	public Entity get(int index){
		return entities[index];
	}
	public int size(){
		return size;
	}
}
