package wraithaven.conquest.client.BuildingCreator.BlockPalette;

import java.io.File;
import wraithaven.conquest.client.BuildingCreator.Loop;
import wraithaven.conquest.client.ClientLauncher;
import wraithaven.conquest.client.GameWorld.GLGuiImage;

public class GemSlider{
	double currentX;
	float sliderPercent;
	final double startX, endX, yPosition;
	final GLGuiImage gem;
	public GemSlider(double yPosition, double startX, double endX, String color){
		this.startX=startX;
		this.endX=endX;
		this.yPosition=yPosition;
		currentX=endX;
		gem=new GLGuiImage(new File(ClientLauncher.assetFolder, color));
		gem.y=(float)(yPosition/Loop.screenRes.height);
		gem.x=(float)(endX/Loop.screenRes.width);
		gem.w=25f/Loop.screenRes.width;
		gem.h=36f/Loop.screenRes.height;
	}
	public void setSliderPosition(double xPos){
		if(xPos<startX)xPos=startX;
		if(xPos>endX)xPos=endX;
		currentX=xPos;
		gem.x=(float)(xPos/Loop.screenRes.width);
		sliderPercent=(float)((xPos-startX)/(endX-startX));
	}
}