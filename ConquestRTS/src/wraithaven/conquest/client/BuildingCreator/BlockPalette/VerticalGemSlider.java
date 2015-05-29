package wraithaven.conquest.client.BuildingCreator.BlockPalette;

import wraithaven.conquest.client.ClientLauncher;
import wraithaven.conquest.client.BuildingCreator.Loop;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;

public class VerticalGemSlider{
	double currentY;
	final UiElement gem;
	private float offsetY;
	float sliderPercent;
	final float startY,
			endY,
			xPosition;
	final float width,
			height;
	public VerticalGemSlider(float xPosition, float startY, float endY, String color, float width, float height, float offX, float offY){
		this.startY = startY;
		this.endY = endY;
		this.xPosition = xPosition;
		this.width = width;
		this.height = height;
		offsetY = offY;
		currentY = startY;
		gem = new UiElement(Texture.getTexture(ClientLauncher.assetFolder, color));
		gem.y = Loop.screenRes.height-(startY+offY+height);
		gem.x = this.xPosition+offX;
		gem.w = width;
		gem.h = height;
	}
	public void setSliderPosition(double yPos){
		if(yPos<startY) yPos = startY;
		if(yPos>endY) yPos = endY;
		currentY = yPos;
		gem.y = (float)(Loop.screenRes.height-(yPos+offsetY+height));
		sliderPercent = (float)((yPos-startY)/(endY-startY));
	}
}