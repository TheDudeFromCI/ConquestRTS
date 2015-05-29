package wraithaven.conquest.client;

import java.awt.Color;
import org.lwjgl.opengl.GL11;
import wraithaven.conquest.client.GameWorld.LoopControls.MatrixUtils;
import wraithaven.conquest.client.BuildingCreator.BlockPalette.UI;
import wraithaven.conquest.client.BuildingCreator.BlockPalette.UiElement;
import wraithaven.conquest.client.GameWorld.WindowUtil.OnScreenText.TextBox;

public class LoadingScreen{
	private int percent;
	private final Runnable onFinish;
	private final TextBox textBox;
	private final UiElement textElement;
	private final LoadingScreenTask task;
	public LoadingScreen(LoadingScreenTask task, Runnable onFinish){
		this.task=task;
		textBox=new TextBox("Loading... 0%", 90, 16, Color.black);
		this.onFinish=onFinish;
		GL11.glClearColor(0, 0, 0, 0);
		textElement=new UiElement(textBox.getTexture());
		textElement.w=180;
		textElement.h=32;
		textElement.x=(640-textElement.w)/2f;
		textElement.y=(480-textElement.h)/2f;
	}
	public void update(){
		int newPercent = task.update();
		if(newPercent!=percent){
			percent=newPercent;
			updateText();
		}
		if(newPercent>=100)onFinish.run();
	}
	public void render(){
		MatrixUtils.setupImageOrtho(640, 480, -1, 1);
		UI.renderElement(textElement);
	}
	private void updateText(){ textBox.setText("Loading... "+percent+"%"); }
}