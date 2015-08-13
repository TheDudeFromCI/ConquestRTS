package com.wraithavens.conquest.SinglePlayer.Entities;

public enum EntityType{
	Grass1("Grass1.tal", true),
	Grass2("Grass2.tal", true),
	Grass3("Grass3.tal", true),
	Catgirl("Catgirl.tal", false);
	EntityMesh mesh;
	public final String fileName;
	public final boolean isGrass;
	private EntityType(String fileName, boolean isGrass){
		this.fileName = fileName;
		this.isGrass = isGrass;
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
