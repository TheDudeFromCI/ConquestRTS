package com.wraithavens.conquest.SinglePlayer.Entities;

public class EntityMesh{
	private final EntityType type;
	int references = 0;
	EntityMesh(EntityType type){
		this.type = type;
	}
	private void dispose(){
		// TODO
		System.out.println(type.fileName+" disposed.");
	}
	void addReference(){
		references++;
		System.out.println("Added reference to entity: '"+type.fileName+"'. References: "+references);
	}
	void drawStatic(){}
	void removeReference(){
		references--;
		System.out.println("Removed reference to entity: '"+type.fileName+"'. References: "+references);
		if(references==0){
			dispose();
			type.mesh = null;
		}
	}
}
