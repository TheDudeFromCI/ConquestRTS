package wraithaven.conquest.client.BuildingCreator.BlockPalette;

import java.io.File;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;
import wraithaven.conquest.client.BuildingCreator.Loop;
import wraithaven.conquest.client.ClientLauncher;

public class HorizontalGemSlider{
	double currentX;
	private float offsetX;
	float sliderPercent;
	final float startX, endX, yPosition;
	final UiElement gem;
	final float width, height;
	public HorizontalGemSlider(float yPosition, float startX, float endX, float currentPos, String color, float width, float height, float offX, float offY){
		this.startX=startX;
		this.endX=endX;
		this.yPosition=Loop.screenRes.height-(yPosition+offY+height);
		this.width=width;
		this.height=height;
		offsetX=offX;
		currentX=currentPos;
		gem=new UiElement(new Texture(new File(ClientLauncher.assetFolder, color), 0, null));
		gem.y=this.yPosition;
		gem.x=currentPos+offX;
		gem.w=width;
		gem.h=height;
		sliderPercent=(currentPos-startX)/(endX-startX);
	}
	public void setSliderPosition(double xPos){
		if(xPos<startX)xPos=startX;
		if(xPos>endX)xPos=endX;
		currentX=xPos;
		gem.x=(float)xPos+offsetX;
		sliderPercent=(float)((xPos-startX)/(endX-startX));
	}
}