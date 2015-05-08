package wraithaven.conquest.client.BuildingCreator;

import java.util.ArrayList;
import wraithaven.conquest.client.BuildingCreator.BlockPalette.BlockIcon;

public class IconManager{
	private final ArrayList<BlockIcon> icons = new ArrayList();
	public void addIcon(BlockIcon icon){ icons.add(icon); }
	public void render(){ for(int i = 0; i<icons.size(); i++)icons.get(i).render(); }
	public void update(double time){ for(int i = 0; i<icons.size(); i++)icons.get(i).update(time); }
}