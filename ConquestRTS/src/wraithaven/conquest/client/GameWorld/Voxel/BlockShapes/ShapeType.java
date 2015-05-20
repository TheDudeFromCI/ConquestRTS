package wraithaven.conquest.client.GameWorld.Voxel.BlockShapes;

import wraithaven.conquest.client.GameWorld.Voxel.BlockShape;

public enum ShapeType{
	SHAPE_0(new Shape0()),
	SHAPE_1(new Shape1()),
	SHAPE_2(new Shape2()),
	SHAPE_3(new Shape3()),
	SHAPE_4(new Shape4()),
	SHAPE_5(new Shape5()),
	SHAPE_6(new Shape6()),
	SHAPE_7(new Shape7()),
	SHAPE_8(new Shape8()),
	SHAPE_9(new Shape9()),
	SHAPE_10(new Shape10()),
	SHAPE_11(new Shape11()),
	SHAPE_12(new Shape12()),
	SHAPE_13(new Shape13()),
	SHAPE_14(new Shape14()),
	SHAPE_15(new Shape15()),
	SHAPE_16(new Shape16()),
	SHAPE_17(new Shape17()),
	SHAPE_18(new Shape18()),
	SHAPE_19(new Shape19()),
	SHAPE_20(new Shape20()),
	SHAPE_21(new Shape21()),
	SHAPE_22(new Shape22()),
	SHAPE_23(new Shape23()),
	SHAPE_24(new Shape24()),
	SHAPE_25(new Shape25());
	public final BlockShape shape;
	private ShapeType(BlockShape shape){ this.shape=shape; }
}