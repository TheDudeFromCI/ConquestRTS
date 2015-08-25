package com.wraithavens.conquest.SinglePlayer.Entities;

public enum EntityType{
	DupaiTree("DupaiTree.tal", false, false, false, null),
	TayleaMeadowGrass0("Taylea Meadow Grass.tal_0", true, false, true, null),
	TayleaMeadowGrass1("Taylea Meadow Grass.tal_1", true, false, true, null),
	TayleaMeadowGrass2("Taylea Meadow Grass.tal_2", true, false, true, null),
	TayleaMeadowGrass3("Taylea Meadow Grass.tal_3", true, false, true, null),
	TayleaMeadowGrass4("Taylea Meadow Grass.tal_4", true, false, true, null),
	TayleaMeadowGrass5("Taylea Meadow Grass.tal_5", true, false, true, null),
	TayleaMeadowGrass6("Taylea Meadow Grass.tal_6", true, false, true, null),
	TayleaMeadowGrass7("Taylea Meadow Grass.tal_7", true, false, true, null),
	ArcstoneHillsGrass0("Arcstone Hills Grass.tal_0", true, false, true, null),
	ArcstoneHillsGrass1("Arcstone Hills Grass.tal_1", true, false, true, null),
	ArcstoneHillsGrass2("Arcstone Hills Grass.tal_2", true, false, true, null),
	ArcstoneHillsGrass3("Arcstone Hills Grass.tal_3", true, false, true, null),
	ArcstoneHillsGrass4("Arcstone Hills Grass.tal_4", true, false, true, null),
	ArcstoneHillsGrass5("Arcstone Hills Grass.tal_5", true, false, true, null),
	ArcstoneHillsGrass6("Arcstone Hills Grass.tal_6", true, false, true, null),
	ArcstoneHillsGrass7("Arcstone Hills Grass.tal_7", true, false, true, null),
	TayleaFlower("TayleaFlower.tal", false, false, true, null),
	TayleaMeadowRock1("Taylea Meadow Rock 1.tal", false, false, false, null),
	TayleaMeadowRock2("Taylea Meadow Rock 2.tal", false, false, false, null),
	TayleaMeadowRock3("Taylea Meadow Rock 3.tal", false, false, false, null),
	VallaFlower("VallaFlower.tal", false, false, true, null),
	Arcstone1("Arcstone 1.tal", false, true, false, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone2("Arcstone 2.tal", false, true, false, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone3("Arcstone 3.tal", false, true, false, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone4("Arcstone 4.tal", false, true, false, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone5("Arcstone 5.tal", false, true, false, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone6("Arcstone 6.tal", false, true, false, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone7("Arcstone 7.tal", false, true, false, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone8("Arcstone 8.tal", false, true, false, new LodRadius(300, 600, 900, 1200, 1500, 1800));
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
