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
	SHAPE_10(new Shape10());
	public final BlockShape shape;
	private ShapeType(BlockShape shape){ this.shape=shape; }
}