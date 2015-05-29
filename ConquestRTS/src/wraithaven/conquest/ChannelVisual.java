package wraithaven.conquest;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class ChannelVisual{
	private final ArrayList<ChannelVisualPart> parts = new ArrayList();
	public void addPart(ChannelVisualPart part){
		parts.add(part);
	}
	public void addPart(ChannelVisualPart part, int index){
		parts.add(index, part);
	}
	public int getIndexOf(ChannelVisualPart part){
		return parts.indexOf(part);
	}
	public ChannelVisualPart getPart(int index){
		return parts.get(index);
	}
	public int getPartsSize(){
		return parts.size();
	}
	public void removePart(ChannelVisualPart part){
		parts.remove(part);
	}
	public void render(Graphics2D g, int x, int y, int width, int height){
		ChannelVisualPart p;
		for(int i = 0; i<parts.size(); i++){
			p = parts.get(i);
			p.renderPart(g, x+(int)(p.getXPercent()*width), y+(int)(p.getYPercent()*height), (int)(p.getWidthPercent()*width), (int)(p.getHeightPercent()*height));
		}
	}
}