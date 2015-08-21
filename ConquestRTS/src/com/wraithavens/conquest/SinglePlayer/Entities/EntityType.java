package com.wraithavens.conquest.SinglePlayer.Entities;

public enum EntityType{
	DupaiTree("DupaiTree.tal", false, null),
	Grass0("Grass.tal_0", true, null),
	Grass1("Grass.tal_1", true, null),
	Grass2("Grass.tal_2", true, null),
	Grass3("Grass.tal_3", true, null),
	Grass4("Grass.tal_4", true, null),
	Grass5("Grass.tal_5", true, null),
	Grass6("Grass.tal_6", true, null),
	Grass7("Grass.tal_7", true, null),
	TayleaFlower("TayleaFlower.tal", false, null),
	Rock1("Rock1.tal", false, null),
	Rock2("Rock2.tal", false, null),
	Rock3("Rock3.tal", false, null),
	VallaFlower("VallaFlower.tal", false, null);
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
