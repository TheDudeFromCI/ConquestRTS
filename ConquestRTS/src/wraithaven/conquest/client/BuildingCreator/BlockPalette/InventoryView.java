package wraithaven.conquest.client.BuildingCreator.BlockPalette;

import java.util.ArrayList;
import wraithaven.conquest.client.BuildingCreator.BlockIcon;
import wraithaven.conquest.client.BuildingCreator.Loop;

public class InventoryView{
	private static int COLS_SHOWN = 5;
	private static int ROWS_SHOWN = 7;
	private static int TOTAL_SHOWN = InventoryView.COLS_SHOWN*InventoryView.ROWS_SHOWN;
	private static float screenXToWorldX(float screenX){
		return (screenX*2-1)*(Shapes.BLOCK_ZOOM*Loop.screenRes.width/Loop.screenRes.height/2f);
	}
	private static float screenYToWorldY(float screenY){
		return (screenY*2-1)*(Shapes.BLOCK_ZOOM/2f);
	}
	private final ArrayList<BlockIcon> icons;
	private int scrollPosition;
	private float width,
			height,
			x,
			y;
	public InventoryView(float width, float height, float x, float y){
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		icons = Loop.INSTANCE.getInventory().getBlocks();
	}
	private boolean checkIcon(int index, double x, double y){
		x = InventoryView.screenXToWorldX((float)(x/Loop.screenRes.width));
		y = InventoryView.screenYToWorldY((float)((Loop.screenRes.height-y)/Loop.screenRes.height));
		float blockX = getX(index-scrollPosition);
		float blockY = getY(index-scrollPosition);
		float unitX = 0.7f;
		float unitY = 0.7f;
		if(x>blockX-unitX&&x<blockX+unitX&&y>blockY-unitY&&y<blockY+unitY){
			UI.INSTANCE.load(icons.get(index));
			return true;
		}
		return false;
	}
	private float getScreenPercentX(int index){
		float minX = 667f/1024*width+x;
		float maxX = 910f/1024*width+x;
		float spacing = (maxX-minX)/(InventoryView.COLS_SHOWN-1);
		return ((index%InventoryView.COLS_SHOWN)*spacing+minX)/Loop.screenRes.width;
	}
	private float getScreenPercentY(int index){
		float minY = 303f/768*height+y;
		float maxY = 699f/768*height+y;
		float spacing = (maxY-minY)/(InventoryView.ROWS_SHOWN-1);
		return ((InventoryView.ROWS_SHOWN-1-index/InventoryView.COLS_SHOWN)*spacing+minY)/Loop.screenRes.height;
	}
	private float getX(int id){
		return InventoryView.screenXToWorldX(getScreenPercentX(id));
	}
	private float getY(int id){
		return InventoryView.screenYToWorldY(getScreenPercentY(id));
	}
	public boolean onClick(double x, double y){
		int lastShown = Math.min(scrollPosition+InventoryView.TOTAL_SHOWN, icons.size());
		for(int i = scrollPosition; i<lastShown; i++)
			if(checkIcon(i, x, y)) return true;
		return false;
	}
	public void render(){
		int lastShown = Math.min(scrollPosition+InventoryView.TOTAL_SHOWN, icons.size());
		for(int i = scrollPosition; i<lastShown; i++)
			icons.get(i).render(getX(i-scrollPosition), getY(i-scrollPosition));
	}
	public void updateScrollPosition(float percent){
		int totalRows = (int)Math.ceil(icons.size()/(float)InventoryView.COLS_SHOWN)-InventoryView.ROWS_SHOWN;
		scrollPosition = (int)(percent*totalRows)*InventoryView.COLS_SHOWN;
		if(scrollPosition<0) scrollPosition = 0;
	}
}