package wraithaven.conquest.client.BuildingCreator.BlockPalette;

import wraithaven.conquest.client.ClientLauncher;
import wraithaven.conquest.client.BuildingCreator.Loop;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;

public class HorizontalGemSlider{
	double currentX;
	final UiElement gem;
	private float offsetX;
	float sliderPercent;
	final float startX,
			endX,
			yPosition;
	final float width,
			height;
	public HorizontalGemSlider(float yPosition, float startX, float endX, float currentPos, String color, float width, float height, float offX, float offY){
		this.startX = startX;
		this.endX = endX;
		this.yPosition = Loop.screenRes.height-(yPosition+offY+height);
		this.width = width;
		this.height = height;
		offsetX = offX;
		currentX = currentPos;
		gem = new UiElement(Texture.getTexture(ClientLauncher.assetFolder, color));
		gem.y = this.yPosition;
		gem.x = currentPos+offX;
		gem.w = width;
		gem.h = height;
		sliderPercent = (currentPos-startX)/(endX-startX);
	}
	public void setSliderPosition(double xPos){
		if(xPos<startX) xPos = startX;
		if(xPos>endX) xPos = endX;
		currentX = xPos;
		gem.x = (float)xPos+offsetX;
		sliderPercent = (float)((xPos-startX)/(endX-startX));
	}
}