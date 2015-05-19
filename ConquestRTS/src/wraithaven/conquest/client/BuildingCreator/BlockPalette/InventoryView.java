package wraithaven.conquest.client.BuildingCreator.BlockPalette;

import java.util.ArrayList;
import wraithaven.conquest.client.BuildingCreator.BlockIcon;
import wraithaven.conquest.client.BuildingCreator.Loop;

public class InventoryView{
	private int scrollPosition;
	private float width, height, x, y;
	private final ArrayList<BlockIcon> icons;
	private static int COLS_SHOWN = 5;
	private static int ROWS_SHOWN = 7;
	private static int TOTAL_SHOWN = COLS_SHOWN*ROWS_SHOWN;
	public InventoryView(float width, float height, float x, float y){
		this.width=width;
		this.height=height;
		this.x=x;
		this.y=y;
		icons=Loop.INSTANCE.getInventory().getBlocks();
	}
	public void render(){
		int lastShown = Math.min(scrollPosition+TOTAL_SHOWN, icons.size());
		for(int i = scrollPosition; i<lastShown; i++)icons.get(i).render(getX(i), getY(i));
	}
	public boolean onClick(double x, double y){
		int lastShown = Math.min(scrollPosition+TOTAL_SHOWN, icons.size());
		for(int i = scrollPosition; i<lastShown; i++)if(checkIcon(i, x, y))return true;
		return false;
	}
	private boolean checkIcon(int index, double x, double y){
		x=screenXToWorldX((float)(x/Loop.screenRes.width));
		y=screenYToWorldY((float)((Loop.screenRes.height-y)/Loop.screenRes.height));
		float blockX = getX(index);
		float blockY = getY(index);
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
		float spacing = (maxX-minX)/(COLS_SHOWN-1);
		return ((index%COLS_SHOWN)*spacing+minX)/Loop.screenRes.width;
	}
	private float getScreenPercentY(int index){
		float minY = 303f/768*height+y;
		float maxY = 699f/768*height+y;
		float spacing = (maxY-minY)/(ROWS_SHOWN-1);
		return ((ROWS_SHOWN-1-index/COLS_SHOWN)*spacing+minY)/Loop.screenRes.height;
	}
	private float getX(int id){ return screenXToWorldX(getScreenPercentX(id)); }
	private float getY(int id){ return screenYToWorldY(getScreenPercentY(id)); }
	private static float screenXToWorldX(float screenX){ return (screenX*2-1)*(Shapes.BLOCK_ZOOM*Loop.screenRes.width/Loop.screenRes.height/2f); }
	private static float screenYToWorldY(float screenY){ return (screenY*2-1)*(Shapes.BLOCK_ZOOM/2f); }
}
