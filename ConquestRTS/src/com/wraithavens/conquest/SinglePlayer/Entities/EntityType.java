package com.wraithavens.conquest.SinglePlayer.Entities;

public enum EntityType{
	Grass("Grass.tal");
	EntityMesh mesh;
	public final String fileName;
	private EntityType(String fileName){
		this.fileName = fileName;
	}
	public EntityMesh createReference(){
		if(mesh!=null){
			mesh.addReference();
			return mesh;
		}
		mesh = new EntityMesh(this);
		mesh.addReference();
		return mesh;
	}
	public int getMeshRenferences(){
		if(mesh==null)
			return -1;
		return mesh.references;
	}
}
