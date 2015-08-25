package com.wraithavens.conquest.SinglePlayer.Entities;

public enum EntityType{
	DupaiTree("DupaiTree.tal", false, false, false, null),
	// Grass0("Arcstone Grass.tal_0", true, false, true, null),
	// Grass1("Arcstone Grass.tal_1", true, false, true, null),
	// Grass2("Arcstone Grass.tal_2", true, false, true, null),
	// Grass3("Arcstone Grass.tal_3", true, false, true, null),
	// Grass4("Arcstone Grass.tal_4", true, false, true, null),
	// Grass5("Arcstone Grass.tal_5", true, false, true, null),
	// Grass6("Arcstone Grass.tal_6", true, false, true, null),
	// Grass7("Arcstone Grass.tal_7", true, false, true, null),
	Grass0("Grass.tal_0", true, false, true, null),
	Grass1("Grass.tal_1", true, false, true, null),
	Grass2("Grass.tal_2", true, false, true, null),
	Grass3("Grass.tal_3", true, false, true, null),
	Grass4("Grass.tal_4", true, false, true, null),
	Grass5("Grass.tal_5", true, false, true, null),
	Grass6("Grass.tal_6", true, false, true, null),
	Grass7("Grass.tal_7", true, false, true, null),
	TayleaFlower("TayleaFlower.tal", false, false, true, null),
	Rock1("Rock1.tal", false, false, false, null),
	Rock2("Rock2.tal", false, false, false, null),
	Rock3("Rock3.tal", false, false, false, null),
	VallaFlower("VallaFlower.tal", false, false, true, null),
	Arcstone1("Arcstone 1.tal", false, true, false, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone2("Arcstone 2.tal", false, true, false, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone3("Arcstone 3.tal", false, true, false, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone4("Arcstone 4.tal", false, true, false, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone5("Arcstone 5.tal", false, true, false, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone6("Arcstone 6.tal", false, true, false, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone7("Arcstone 7.tal", false, true, false, new LodRadius(300, 600, 900, 1200, 1500, 1800));
	EntityMesh mesh;
	public final String fileName;
	public final boolean isGrass;
	public final boolean isGiant;
	public final boolean sways;
	public final LodRadius lodRadius;
	private EntityType(String fileName, boolean isGrass, boolean isGiant, boolean sways, LodRadius lodRadius){
		this.fileName = fileName;
		this.isGrass = isGrass;
		this.isGiant = isGiant;
		this.sways = sways;
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
