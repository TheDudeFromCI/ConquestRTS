package com.wraithavens.conquest.SinglePlayer.Entities;

import java.util.ArrayList;
import java.util.Comparator;

public class EntityDatabase{
	private final ArrayList<Entity> entities = new ArrayList();
	private final Comparator entitySorter = new Comparator<Entity>(){
		public int compare(Entity a, Entity b){
			return a.mesh==b.mesh?0:a.mesh.getId()>b.mesh.getId()?1:-1;
		}
	};
	public void addEntity(Entity e){
		entities.add(e);
		// ---
		// Let's sort them so that entities of the same type render together.
		// This allows us to render batches, without have to rebind the same
		// VBOs multiple times per frame.
		// ---
		entities.sort(entitySorter);
	}
	public void dispose(){
		for(Entity e : entities)
			e.dispose();
		entities.clear();
	}
	public void render(){
		// ---
		// Render all entities. Switching mesh types as nessicary.
		// ---
		EntityMesh mesh = null;
		for(Entity e : entities){
			if(mesh==null||e.getMesh()!=mesh){
				mesh = e.getMesh();
				mesh.bind();
			}
			e.render();
		}
	}
}
