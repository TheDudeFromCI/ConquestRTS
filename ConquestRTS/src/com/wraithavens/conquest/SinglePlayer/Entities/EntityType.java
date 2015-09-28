package com.wraithavens.conquest.SinglePlayer.Entities;

import com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities.GiantEntityGroundHits;

public enum EntityType{
	TayleaMeadowGrass0("Taylea Meadow Grass.tal_0", true, false, true, false),
	TayleaMeadowGrass1("Taylea Meadow Grass.tal_1", true, false, true, false),
	TayleaMeadowGrass2("Taylea Meadow Grass.tal_2", true, false, true, false),
	TayleaMeadowGrass3("Taylea Meadow Grass.tal_3", true, false, true, false),
	TayleaMeadowGrass4("Taylea Meadow Grass.tal_4", true, false, true, false),
	TayleaMeadowGrass5("Taylea Meadow Grass.tal_5", true, false, true, false),
	TayleaMeadowGrass6("Taylea Meadow Grass.tal_6", true, false, true, false),
	TayleaMeadowGrass7("Taylea Meadow Grass.tal_7", true, false, true, false),
	ArcstoneHillsGrass0("Arcstone Hills Grass.tal_0", true, false, true, false),
	ArcstoneHillsGrass1("Arcstone Hills Grass.tal_1", true, false, true, false),
	ArcstoneHillsGrass2("Arcstone Hills Grass.tal_2", true, false, true, false),
	ArcstoneHillsGrass3("Arcstone Hills Grass.tal_3", true, false, true, false),
	ArcstoneHillsGrass4("Arcstone Hills Grass.tal_4", true, false, true, false),
	ArcstoneHillsGrass5("Arcstone Hills Grass.tal_5", true, false, true, false),
	ArcstoneHillsGrass6("Arcstone Hills Grass.tal_6", true, false, true, false),
	ArcstoneHillsGrass7("Arcstone Hills Grass.tal_7", true, false, true, false),
	TayleaFlower("TayleaFlower1.tal", false, false, true, true),
	TayleaFlower2("TayleaFlower2.tal", false, false, true, true),
	TayleaFlower3("TayleaFlower3.tal", false, false, true, true),
	TayleaFlower4("TayleaFlower4.tal", false, false, true, true),
	TayleaFlower5("TayleaFlower5.tal", false, false, true, true),
	TayleaFlower6("TayleaFlower6.tal", false, false, true, true),
	TayleaMeadowRock1("Taylea Meadow Rock 1.tal", false, false, false, true),
	TayleaMeadowRock2("Taylea Meadow Rock 2.tal", false, false, false, true),
	TayleaMeadowRock3("Taylea Meadow Rock 3.tal", false, false, false, true),
	VallaFlower("VallaFlower1.tal", false, false, true, true),
	VallaFlower2("VallaFlower2.tal", false, false, true, true),
	VallaFlower3("VallaFlower3.tal", false, false, true, true),
	VallaFlower4("VallaFlower4.tal", false, false, true, true),
	Arcstone("ArcstoneGiant1.tal", false, true, false, true),
	Arcstone2("ArcstoneGiant2.tal", false, true, false, true),
	Arcstone3("ArcstoneLarge1.tal", false, true, false, true),
	Arcstone4("ArcstoneLarge2.tal", false, true, false, true),
	Arcstone5("ArcstoneLarge3.tal", false, true, false, true),
	Arcstone6("ArcstoneLarge4.tal", false, true, false, true),
	Arcstone7("ArcstoneMedium1.tal", false, true, false, true),
	Arcstone8("ArcstoneMedium2.tal", false, true, false, true),
	Arcstone9("ArcstoneMedium3.tal", false, true, false, true),
	Arcstone10("ArcstoneMedium4.tal", false, true, false, true),
	Arcstone11("ArcstoneMedium5.tal", false, true, false, true),
	Arcstone12("ArcstoneMedium6.tal", false, true, false, true),
	Arcstone13("ArcstoneMedium7.tal", false, true, false, true),
	Arcstone14("ArcstoneMedium8.tal", false, true, false, true),
	Arcstone15("ArcstoneSmall1.tal", false, true, false, true),
	Arcstone16("ArcstoneSmall2.tal", false, true, false, true),
	Arcstone17("ArcstoneSmall3.tal", false, true, false, true),
	Arcstone18("ArcstoneSmall4.tal", false, true, false, true),
	Arcstone19("ArcstoneSmall5.tal", false, true, false, true),
	Arcstone20("ArcstoneSmall6.tal", false, true, false, true),
	Arcstone21("ArcstoneSmall7.tal", false, true, false, true),
	Arcstone22("ArcstoneSmall8.tal", false, true, false, true),
	Arcstone23("ArcstoneSmall9.tal", false, true, false, true),
	Arcstone24("ArcstoneSmall10.tal", false, true, false, true),
	Arcstone25("ArcstoneSmall11.tal", false, true, false, true),
	Arcstone26("ArcstoneSmall12.tal", false, true, false, true),
	Arcstone27("ArcstoneSmall13.tal", false, true, false, true),
	Arcstone28("ArcstoneSmall14.tal", false, true, false, true),
	Arcstone29("ArcstoneSmall15.tal", false, true, false, true),
	Arcstone30("ArcstoneSmall16.tal", false, true, false, true),
	AesiaFieldsGrass0("AesiaGrass.tal_0", true, false, true, false),
	AesiaFieldsGrass1("AesiaGrass.tal_1", true, false, true, false),
	AesiaFieldsGrass2("AesiaGrass.tal_2", true, false, true, false),
	AesiaFieldsGrass3("AesiaGrass.tal_3", true, false, true, false),
	AesiaFieldsGrass4("AesiaGrass.tal_4", true, false, true, false),
	AesiaFieldsGrass5("AesiaGrass.tal_5", true, false, true, false),
	AesiaFieldsGrass6("AesiaGrass.tal_6", true, false, true, false),
	AesiaFieldsGrass7("AesiaGrass.tal_7", true, false, true, false),
	AesiaStems("AesiaStems1.tal", false, false, false, true),
	AesiaStems2("AesiaStems2.tal", false, false, false, true),
	AesiaStems3("AesiaStems3.tal", false, false, false, true),
	AesiaStems4("AesiaStems4.tal", false, false, false, true),
	AesiaStems5("AesiaStems5.tal", false, false, false, true),
	AesiaStems6("AesiaStems6.tal", false, false, false, true),
	AesiaStems7("AesiaStems7.tal", false, false, false, true),
	AesiaStems8("AesiaStems8.tal", false, false, false, true),
	AesiaStems9("AesiaStems9.tal", false, false, false, true),
	AesiaStems10("AesiaStems10.tal", false, false, false, true),
	AesiaStems11("AesiaStems11.tal", false, false, false, true),
	AesiaStems12("AesiaStems12.tal", false, false, false, true),
	AesiaStems13("AesiaStems13.tal", false, false, false, true),
	AesiaStems14("AesiaStems14.tal", false, false, false, true),
	AesiaStems15("AesiaStems15.tal", false, false, false, true),
	AesiaStems16("AesiaStems16.tal", false, false, false, true),
	AesiaStems17("AesiaStems17.tal", false, false, false, true),
	AesiaStems18("AesiaStems18.tal", false, false, false, true),
	AesiaStems19("AesiaStems19.tal", false, false, false, true),
	AesiaStems20("AesiaStems20.tal", false, false, false, true),
	AesiaStems21("AesiaStems21.tal", false, false, false, true),
	AesiaStems22("AesiaStems22.tal", false, false, false, true),
	AesiaStems23("AesiaStems23.tal", false, false, false, true),
	AesiaStems24("AesiaStems24.tal", false, false, false, true),
	AesiaPedals("AesiaPedals1.tal", false, false, false, false),
	AesiaPedals2("AesiaPedals2.tal", false, false, false, false),
	AesiaPedals3("AesiaPedals3.tal", false, false, false, false),
	AesiaPedals4("AesiaPedals4.tal", false, false, false, false),
	AesiaPedals5("AesiaPedals5.tal", false, false, false, false),
	AesiaPedals6("AesiaPedals6.tal", false, false, false, false),
	AesiaPedals7("AesiaPedals7.tal", false, false, false, false);
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
	private EntityType(String fileName, boolean isGrass, boolean isGiant, boolean sways, boolean colorBlended){
		this.fileName = fileName;
		this.isGrass = isGrass;
		this.isGiant = isGiant;
		this.sways = sways;
		this.colorBlended = colorBlended;
		lodRadius = new LodRadius(isGiant?0:100);
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
	public boolean isAesiaStemType(){
		int o = ordinal();
		int i = EntityType.AesiaStems.ordinal();
		return o>=i&&o<i+24;
	}
}
