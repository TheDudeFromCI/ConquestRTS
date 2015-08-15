package com.wraithavens.conquest.SinglePlayer.Entities;

public enum EntityType{
	Grass1("Grass1.tal", true, null),
	Grass2("Grass2.tal", true, null),
	Grass3("Grass3.tal", true, null),
	Catgirl("Catgirl.tal", false, null),
	DupaiTree("DupaiTree.tal", false, null);
	EntityMesh mesh;
	public final String fileName;
	public final boolean isGrass;
	public final LodRadius lodRadius;
	private EntityType(String fileName, boolean isGrass, LodRadius lodRadius){
		this.fileName = fileName;
		this.isGrass = isGrass;
		if(lodRadius==null)
			this.lodRadius = new LodRadius(100, 200, 300, 400, 500, 600);
		else
			this.lodRadius = lodRadius;
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
