package com.wraithavens.conquest.SinglePlayer.Entities;

import java.io.File;
import com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities.GiantEntityGroundHits;

public enum EntityType{
	Grass("Basic/Grass1.png", true, false, true, false),
	Grass1("Basic/Grass3.png", true, false, true, false),
	Grass2("Basic/Grass5.png", true, false, true, false),
	Grass3("Basic/Grass7.png", true, false, true, false),
	TayleaFlower("TayleaMeadow/TayleaFlower1.tal", false, false, true, true),
	TayleaFlower2("TayleaMeadow/TayleaFlower2.tal", false, false, true, true),
	TayleaFlower3("TayleaMeadow/TayleaFlower3.tal", false, false, true, true),
	TayleaFlower4("TayleaMeadow/TayleaFlower4.tal", false, false, true, true),
	TayleaFlower5("TayleaMeadow/TayleaFlower5.tal", false, false, true, true),
	TayleaFlower6("TayleaMeadow/TayleaFlower6.tal", false, false, true, true),
	TayleaMeadowRock1("TayleaMeadow/Taylea Meadow Rock 1.tal", false, false, false, true),
	TayleaMeadowRock2("TayleaMeadow/Taylea Meadow Rock 2.tal", false, false, false, true),
	TayleaMeadowRock3("TayleaMeadow/Taylea Meadow Rock 3.tal", false, false, false, true),
	VallaFlower("TayleaMeadow/VallaFlower1.tal", false, false, true, true),
	VallaFlower2("TayleaMeadow/VallaFlower2.tal", false, false, true, true),
	VallaFlower3("TayleaMeadow/VallaFlower3.tal", false, false, true, true),
	VallaFlower4("TayleaMeadow/VallaFlower4.tal", false, false, true, true),
	Arcstone("ArcstoneHills/ArcstoneGiant1.tal", false, true, false, true),
	Arcstone2("ArcstoneHills/ArcstoneGiant2.tal", false, true, false, true),
	Arcstone3("ArcstoneHills/ArcstoneLarge1.tal", false, true, false, true),
	Arcstone4("ArcstoneHills/ArcstoneLarge2.tal", false, true, false, true),
	Arcstone5("ArcstoneHills/ArcstoneLarge3.tal", false, true, false, true),
	Arcstone6("ArcstoneHills/ArcstoneLarge4.tal", false, true, false, true),
	Arcstone7("ArcstoneHills/ArcstoneMedium1.tal", false, true, false, true),
	Arcstone8("ArcstoneHills/ArcstoneMedium2.tal", false, true, false, true),
	Arcstone9("ArcstoneHills/ArcstoneMedium3.tal", false, true, false, true),
	Arcstone10("ArcstoneHills/ArcstoneMedium4.tal", false, true, false, true),
	Arcstone11("ArcstoneHills/ArcstoneMedium5.tal", false, true, false, true),
	Arcstone12("ArcstoneHills/ArcstoneMedium6.tal", false, true, false, true),
	Arcstone13("ArcstoneHills/ArcstoneMedium7.tal", false, true, false, true),
	Arcstone14("ArcstoneHills/ArcstoneMedium8.tal", false, true, false, true),
	Arcstone15("ArcstoneHills/ArcstoneSmall1.tal", false, true, false, true),
	Arcstone16("ArcstoneHills/ArcstoneSmall2.tal", false, true, false, true),
	Arcstone17("ArcstoneHills/ArcstoneSmall3.tal", false, true, false, true),
	Arcstone18("ArcstoneHills/ArcstoneSmall4.tal", false, true, false, true),
	Arcstone19("ArcstoneHills/ArcstoneSmall5.tal", false, true, false, true),
	Arcstone20("ArcstoneHills/ArcstoneSmall6.tal", false, true, false, true),
	Arcstone21("ArcstoneHills/ArcstoneSmall7.tal", false, true, false, true),
	Arcstone22("ArcstoneHills/ArcstoneSmall8.tal", false, true, false, true),
	Arcstone23("ArcstoneHills/ArcstoneSmall9.tal", false, true, false, true),
	Arcstone24("ArcstoneHills/ArcstoneSmall10.tal", false, true, false, true),
	Arcstone25("ArcstoneHills/ArcstoneSmall11.tal", false, true, false, true),
	Arcstone26("ArcstoneHills/ArcstoneSmall12.tal", false, true, false, true),
	Arcstone27("ArcstoneHills/ArcstoneSmall13.tal", false, true, false, true),
	Arcstone28("ArcstoneHills/ArcstoneSmall14.tal", false, true, false, true),
	Arcstone29("ArcstoneHills/ArcstoneSmall15.tal", false, true, false, true),
	Arcstone30("ArcstoneHills/ArcstoneSmall16.tal", false, true, false, true),
	AesiaStems("AesiaFields/AesiaStems1.tal", false, false, false, true),
	AesiaStems2("AesiaFields/AesiaStems2.tal", false, false, false, true),
	AesiaStems3("AesiaFields/AesiaStems3.tal", false, false, false, true),
	AesiaStems4("AesiaFields/AesiaStems4.tal", false, false, false, true),
	AesiaStems5("AesiaFields/AesiaStems5.tal", false, false, false, true),
	AesiaStems6("AesiaFields/AesiaStems6.tal", false, false, false, true),
	AesiaStems7("AesiaFields/AesiaStems7.tal", false, false, false, true),
	AesiaStems8("AesiaFields/AesiaStems8.tal", false, false, false, true),
	AesiaStems9("AesiaFields/AesiaStems9.tal", false, false, false, true),
	AesiaStems10("AesiaFields/AesiaStems10.tal", false, false, false, true),
	AesiaStems11("AesiaFields/AesiaStems11.tal", false, false, false, true),
	AesiaStems12("AesiaFields/AesiaStems12.tal", false, false, false, true),
	AesiaStems13("AesiaFields/AesiaStems13.tal", false, false, false, true),
	AesiaStems14("AesiaFields/AesiaStems14.tal", false, false, false, true),
	AesiaStems15("AesiaFields/AesiaStems15.tal", false, false, false, true),
	AesiaStems16("AesiaFields/AesiaStems16.tal", false, false, false, true),
	AesiaStems17("AesiaFields/AesiaStems17.tal", false, false, false, true),
	AesiaStems18("AesiaFields/AesiaStems18.tal", false, false, false, true),
	AesiaStems19("AesiaFields/AesiaStems19.tal", false, false, false, true),
	AesiaStems20("AesiaFields/AesiaStems20.tal", false, false, false, true),
	AesiaStems21("AesiaFields/AesiaStems21.tal", false, false, false, true),
	AesiaStems22("AesiaFields/AesiaStems22.tal", false, false, false, true),
	AesiaStems23("AesiaFields/AesiaStems23.tal", false, false, false, true),
	AesiaStems24("AesiaFields/AesiaStems24.tal", false, false, false, true),
	AesiaPedals("AesiaFields/AesiaPedals1.tal", false, false, false, false),
	AesiaPedals2("AesiaFields/AesiaPedals2.tal", false, false, false, false),
	AesiaPedals3("AesiaFields/AesiaPedals3.tal", false, false, false, false),
	AesiaPedals4("AesiaFields/AesiaPedals4.tal", false, false, false, false),
	AesiaPedals5("AesiaFields/AesiaPedals5.tal", false, false, false, false),
	AesiaPedals6("AesiaFields/AesiaPedals6.tal", false, false, false, false),
	AesiaPedals7("AesiaFields/AesiaPedals7.tal", false, false, false, false);
	public static EntityType getVariation(EntityType inital, int type){
		return values()[inital.ordinal()+type];
	}
	EntityMesh mesh;
	public final String fileName;
	public final boolean isGrass;
	public final boolean isGiant;
	public final boolean sways;
	public final boolean colorBlended;
	public final int viewDistance;
	public final GiantEntityGroundHits groundHits;
	private EntityType(String fileName, boolean isGrass, boolean isGiant, boolean sways, boolean colorBlended){
		this.fileName = fileName.replace('/', File.separatorChar);
		this.isGrass = isGrass;
		this.isGiant = isGiant;
		this.sways = sways;
		this.colorBlended = colorBlended;
		viewDistance = isGiant?0:100*100;
		groundHits = new GiantEntityGroundHits(this);
	}
	public boolean isType(EntityType base, int count){
		int o = ordinal();
		int i = base.ordinal();
		return o>=i&&o<i+count;
	}
	EntityMesh createReference(){
		if(mesh!=null){
			mesh.addReference();
			return mesh;
		}
		mesh = new EntityMesh(this);
		mesh.addReference();
		return mesh;
	}
}
