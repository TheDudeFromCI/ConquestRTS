package com.wraithavens.conquest.SinglePlayer.Entities;

import com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities.GiantEntityGroundHits;

public enum EntityType{
	DupaiTree("DupaiTree.tal", false, false, false, true, null),
	TayleaMeadowGrass0("Taylea Meadow Grass.tal_0", true, false, true, false, null),
	TayleaMeadowGrass1("Taylea Meadow Grass.tal_1", true, false, true, false, null),
	TayleaMeadowGrass2("Taylea Meadow Grass.tal_2", true, false, true, false, null),
	TayleaMeadowGrass3("Taylea Meadow Grass.tal_3", true, false, true, false, null),
	TayleaMeadowGrass4("Taylea Meadow Grass.tal_4", true, false, true, false, null),
	TayleaMeadowGrass5("Taylea Meadow Grass.tal_5", true, false, true, false, null),
	TayleaMeadowGrass6("Taylea Meadow Grass.tal_6", true, false, true, false, null),
	TayleaMeadowGrass7("Taylea Meadow Grass.tal_7", true, false, true, false, null),
	ArcstoneHillsGrass0("Arcstone Hills Grass.tal_0", true, false, true, false, null),
	ArcstoneHillsGrass1("Arcstone Hills Grass.tal_1", true, false, true, false, null),
	ArcstoneHillsGrass2("Arcstone Hills Grass.tal_2", true, false, true, false, null),
	ArcstoneHillsGrass3("Arcstone Hills Grass.tal_3", true, false, true, false, null),
	ArcstoneHillsGrass4("Arcstone Hills Grass.tal_4", true, false, true, false, null),
	ArcstoneHillsGrass5("Arcstone Hills Grass.tal_5", true, false, true, false, null),
	ArcstoneHillsGrass6("Arcstone Hills Grass.tal_6", true, false, true, false, null),
	ArcstoneHillsGrass7("Arcstone Hills Grass.tal_7", true, false, true, false, null),
	TayleaFlower("TayleaFlower1.tal", false, false, true, true, null),
	TayleaFlower2("TayleaFlower2.tal", false, false, true, true, null),
	TayleaFlower3("TayleaFlower3.tal", false, false, true, true, null),
	TayleaFlower4("TayleaFlower4.tal", false, false, true, true, null),
	TayleaFlower5("TayleaFlower5.tal", false, false, true, true, null),
	TayleaFlower6("TayleaFlower6.tal", false, false, true, true, null),
	TayleaMeadowRock1("Taylea Meadow Rock 1.tal", false, false, false, true, null),
	TayleaMeadowRock2("Taylea Meadow Rock 2.tal", false, false, false, true, null),
	TayleaMeadowRock3("Taylea Meadow Rock 3.tal", false, false, false, true, null),
	VallaFlower("VallaFlower1.tal", false, false, true, true, null),
	VallaFlower2("VallaFlower2.tal", false, false, true, true, null),
	VallaFlower3("VallaFlower3.tal", false, false, true, true, null),
	VallaFlower4("VallaFlower4.tal", false, false, true, true, null),
	Arcstone("ArcstoneGiant1.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone2("ArcstoneGiant2.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone3("ArcstoneLarge1.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone4("ArcstoneLarge2.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone5("ArcstoneLarge3.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone6("ArcstoneLarge4.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone7("ArcstoneMedium1.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone8("ArcstoneMedium2.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone9("ArcstoneMedium3.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone10("ArcstoneMedium4.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone11("ArcstoneMedium5.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone12("ArcstoneMedium6.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone13("ArcstoneMedium7.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone14("ArcstoneMedium8.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone15("ArcstoneSmall1.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone16("ArcstoneSmall2.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone17("ArcstoneSmall3.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone18("ArcstoneSmall4.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone19("ArcstoneSmall5.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone20("ArcstoneSmall6.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone21("ArcstoneSmall7.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone22("ArcstoneSmall8.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone23("ArcstoneSmall9.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone24("ArcstoneSmall10.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone25("ArcstoneSmall11.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone26("ArcstoneSmall12.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone27("ArcstoneSmall13.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone28("ArcstoneSmall14.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone29("ArcstoneSmall15.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Arcstone30("ArcstoneSmall16.tal", false, true, false, true, new LodRadius(300, 600, 900, 1200, 1500, 1800)),
	Other1("Other1.tal", false, false, false, true, null),
	Other2("Other2.tal", false, false, false, true, null),
	Other3("Other3.tal", false, false, false, true, null),
	Other4("Other4.tal", false, false, false, true, null),
	Other5("Other5.tal", false, false, false, true, null),
	Other6("Other6.tal", false, false, false, true, null);
	public static EntityType getVariation(EntityType inital, int type){
		return values()[inital.ordinal()+type];
	}
	EntityMesh mesh;
	public final String fileName;
	public final boolean isGrass;
	public final boolean isGiant;
	public final boolean sways;
	public final boolean colorBlended;
	public final LodRadius lodRadius;
	public final GiantEntityGroundHits groundHits;
	private EntityType(
		String fileName, boolean isGrass, boolean isGiant, boolean sways, boolean colorBlended,
		LodRadius lodRadius){
		this.fileName = fileName;
		this.isGrass = isGrass;
		this.isGiant = isGiant;
		this.sways = sways;
		this.colorBlended = colorBlended;
		if(lodRadius==null)
			this.lodRadius = new LodRadius(100, 200, 300, 400, 500, 600);
		else
			this.lodRadius = lodRadius;
		groundHits = new GiantEntityGroundHits(this);
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
}
