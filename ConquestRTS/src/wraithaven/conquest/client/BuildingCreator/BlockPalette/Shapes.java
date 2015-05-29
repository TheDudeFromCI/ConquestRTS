package wraithaven.conquest.client.BuildingCreator.BlockPalette;

import java.util.ArrayList;
import wraithaven.conquest.client.ClientLauncher;
import wraithaven.conquest.client.BuildingCreator.BlockIcon;
import wraithaven.conquest.client.BuildingCreator.Loop;
import wraithaven.conquest.client.GameWorld.Voxel.BlockRotation;
import wraithaven.conquest.client.GameWorld.Voxel.BlockShape;
import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;
import wraithaven.conquest.client.GameWorld.Voxel.MipmapQuality;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;
import wraithaven.conquest.client.GameWorld.Voxel.BlockShapes.ShapeType;

public class Shapes{
	public static final float BLOCK_ZOOM = 20;
	private static final int BLOCKS_SHOWN = 7;
	private static double distanceSquared(float x1, float y1, float x2, float y2){
		return Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2);
	}
	private static float screenXPercent(){
		double width = 4;
		double height = 3;
		if(Math.abs(Loop.screenRes.width-width)>Math.abs(Loop.screenRes.height-height)){
			width *= Loop.screenRes.height/height;
			height = Loop.screenRes.height;
		}else{
			height *= Loop.screenRes.width/width;
			width = Loop.screenRes.width;
		}
		return (float)(100/1024.0*width/Loop.screenRes.width+(Loop.screenRes.width-width)/2.0/Loop.screenRes.width);
	}
	private static float screenXToWorldX(float screenX){
		return (screenX*2-1)*(Shapes.BLOCK_ZOOM*Loop.screenRes.width/Loop.screenRes.height/2f);
	}
	private static float screenYPercent(int id){
		double width = 4;
		double height = 3;
		if(Math.abs(Loop.screenRes.width-width)>Math.abs(Loop.screenRes.height-height)){
			width *= Loop.screenRes.height/height;
			height = Loop.screenRes.height;
		}else{
			height *= Loop.screenRes.width/width;
			width = Loop.screenRes.width;
		}
		return (float)(1-(100/768.0*height/Loop.screenRes.height+(Loop.screenRes.height-height)/2/Loop.screenRes.height+420.0/BLOCKS_SHOWN/768*id));
	}
	private static float screenYToWorldY(float screenY){
		return (screenY*2-1)*(Shapes.BLOCK_ZOOM/2f);
	}
	private static float x(){
		return (screenXToWorldX(screenXPercent()));
	}
	private static float y(int id){
		return screenYToWorldY(screenYPercent(id));
	}
	private final CubeTextures baseTexture = new CubeTextures();
	private final ArrayList<BlockIcon> icons = new ArrayList();
	private int scrollbarPosition;
	private int selectedBlock;
	public Shapes(){
		generateCubeTextures();
		for(int i = 0; i<ShapeType.values().length; i++)
			a(ShapeType.values()[i].shape);
	}
	private void a(BlockShape shape){
		icons.add(new BlockIcon(shape, baseTexture, BlockRotation.ROTATION_0));
	}
	public boolean checkMouseClick(double xPos, double yPos){
		yPos = Loop.screenRes.height-yPos;
		float x = screenXPercent()*Loop.screenRes.width;
		int lastShown = Math.min(scrollbarPosition+BLOCKS_SHOWN, icons.size());
		float dis = BLOCK_ZOOM*BLOCK_ZOOM*1.25f*1.25f;
		for(int i = scrollbarPosition; i<lastShown; i++){
			if(distanceSquared(x, screenYPercent(i-scrollbarPosition)*Loop.screenRes.height, (float)xPos, (float)yPos)<=dis){
				selectedBlock = i;
				return true;
			}
		}
		return false;
	}
	public void dispose(){
		for(int i = 0; i<icons.size(); i++)
			icons.get(i).dispose();
	}
	private void generateCubeTextures(){
		Texture whiteTexture = Texture.getTexture(ClientLauncher.textureFolder, "Smooth.png", 4, MipmapQuality.HIGH);
		baseTexture.xUp = whiteTexture;
		baseTexture.xDown = whiteTexture;
		baseTexture.yUp = whiteTexture;
		baseTexture.yDown = whiteTexture;
		baseTexture.zUp = whiteTexture;
		baseTexture.zDown = whiteTexture;
	}
	public BlockShape getCurrentShape(){
		return icons.get(selectedBlock).block.block.shape;
	}
	public void render(){
		int lastShown = Math.min(scrollbarPosition+BLOCKS_SHOWN, icons.size());
		float x = x();
		for(int i = scrollbarPosition; i<lastShown; i++)
			icons.get(i).render(x, y(i-scrollbarPosition), -5, 0, 0);
	}
	public void select(BlockShape shape){
		for(int i = 0; i<icons.size(); i++){
			if(icons.get(i).block.block.shape==shape){
				selectedBlock = i;
				int ideal = (int)(1-Math.ceil(BLOCKS_SHOWN/2f));
				if(ideal>icons.size()-BLOCKS_SHOWN) ideal = icons.size()-BLOCKS_SHOWN;
				if(ideal<0) ideal = 0;
				scrollbarPosition = ideal;
				return;
			}
		}
	}
	public void update(double time){
		int lastShown = Math.min(scrollbarPosition+BLOCKS_SHOWN, icons.size());
		for(int i = 0; i<lastShown; i++)
			icons.get(i).update(time);
	}
	public void updateScrollbar(float percent){
		int size = icons.size()-BLOCKS_SHOWN;
		if(size<=0) return;
		scrollbarPosition = (int)(percent*size);
	}
}