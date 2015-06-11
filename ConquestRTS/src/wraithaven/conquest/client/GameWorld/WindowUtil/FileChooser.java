package wraithaven.conquest.client.GameWorld.WindowUtil;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import wraithaven.conquest.client.BuildingCreator.TextBarElement;
import wraithaven.conquest.client.GameWorld.WindowUtil.OnScreenText.TextBox;
import wraithaven.conquest.client.BuildingCreator.BlockPalette.UI;
import wraithaven.conquest.client.ClientLauncher;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;
import wraithaven.conquest.client.BuildingCreator.BlockPalette.UiElement;
import wraithaven.conquest.client.BuildingCreator.Loop;

public class FileChooser{
	public static FileChooser INSTANCE;
	private static File DIRECTORY;
	private double mouseX, mouseY;
	private UiElement[] tempShownFiles;
	private File[] tempShownFilesRaw;
	private int scrollPos;
	private int firstShown;
	private int lastShown;
	private int selectedIndex = -1;
	private final UiElement pathDirectory;
	private final String appenedText;
	private final UiElement selectedDirectory;
	private final FileChooserResponse onAccept;
	private final UiElement backgroundFrame, foregroundFrame, upArrowButton, saveButton;
	private final TextBarElement textBar;
	private final boolean save;
 	public FileChooser(FileChooserResponse onAccept, boolean save, String appended){
		INSTANCE = this;
		if(DIRECTORY==null)DIRECTORY = new File(System.getProperty("user.home"));
		appenedText = appended;
		this.onAccept = onAccept;
		this.save = save;
		backgroundFrame = new UiElement(Texture.getTexture(ClientLauncher.assetFolder, "File Chooser Background.png"));
		foregroundFrame = new UiElement(Texture.getTexture(ClientLauncher.assetFolder, "File Chooser Foreground.png"));
		selectedDirectory = new UiElement(Texture.getTexture(ClientLauncher.assetFolder, "File Chooser Selected Directory.png"));
		upArrowButton = new UiElement(Texture.getTexture(ClientLauncher.assetFolder, "File Chooser Up Arrow.png"));
		if(save)saveButton = new UiElement(Texture.getTexture(ClientLauncher.assetFolder, "File Chooser Save Button.png"));
		else saveButton = new UiElement(Texture.getTexture(ClientLauncher.assetFolder, "File Chooser Load Button.png"));
		pathDirectory = new UiElement();
		{
			final int width = 500;
			final int height = 500;
			backgroundFrame.x = Loop.screenRes.width/2f-width/2f;
			backgroundFrame.y = Loop.screenRes.height/2f-height/2f;
			backgroundFrame.w = width;
			backgroundFrame.h = height;
			foregroundFrame.x = backgroundFrame.x+width*0.05f;
			foregroundFrame.y = backgroundFrame.y+height*0.1f;
			foregroundFrame.w = width*0.9f;
			foregroundFrame.h = height*0.8f;
			textBar = new TextBarElement((int)foregroundFrame.w, Color.WHITE);
			textBar.x = foregroundFrame.x;
			textBar.y = backgroundFrame.y+height*0.1f-17;
			textBar.w = foregroundFrame.w;
			textBar.h = 17;
			textBar.appenedText = appenedText;
			textBar.updateText();
			upArrowButton.x = (backgroundFrame.x+backgroundFrame.w)-45;
			upArrowButton.y = (backgroundFrame.y+backgroundFrame.h)-45;
			upArrowButton.w = 35;
			upArrowButton.h = 35;
			saveButton.x = (backgroundFrame.x+backgroundFrame.w)-120;
			saveButton.y = (backgroundFrame.y+backgroundFrame.h)-45;
			saveButton.w = 70;
			saveButton.h = 35;
			pathDirectory.x = foregroundFrame.x;
			pathDirectory.y = foregroundFrame.y+foregroundFrame.h+10;
			pathDirectory.w = foregroundFrame.w-135;
			pathDirectory.h = 17;
		}
		updateShownFiles();
	}
	public void render(){
		UI.renderElement(backgroundFrame);
		UI.renderElement(foregroundFrame);
		UI.renderElement(textBar);
		UI.renderElement(upArrowButton);
		UI.renderElement(saveButton);
		UI.renderElement(pathDirectory);
		GL11.glPushMatrix();
		GL11.glTranslatef(0, scrollPos, 0);
		if(selectedIndex>=firstShown
			&&selectedIndex<lastShown)UI.renderElement(selectedDirectory);
		for(int i = firstShown; i<lastShown; i++)
			UI.renderElement(tempShownFiles[i]);
		GL11.glPopMatrix();
	}
	public void onMouseDown(double x, double y){
		mouseX = x;
		mouseY = Loop.screenRes.height-y;
		for(int i = firstShown; i<lastShown; i++)
			if(mouseX>=tempShownFiles[i].x
				&&mouseY>=tempShownFiles[i].y+scrollPos
				&&mouseX<tempShownFiles[i].x+tempShownFiles[i].w
				&&mouseY<tempShownFiles[i].y+tempShownFiles[i].h+scrollPos){
				if(selectedIndex==i){
					if(tempShownFilesRaw[i].isDirectory()){
						selectDirectory(null);
						DIRECTORY = tempShownFilesRaw[i];
						scrollPos = 0;
						updateShownFiles();
					}
				}else{
					selectDirectory(tempShownFiles[i]);
					selectedIndex = i;
					if(!tempShownFilesRaw[i].isDirectory()){
						textBar.text = tempShownFilesRaw[i].getName().substring(0, tempShownFilesRaw[i].getName().length()-appenedText.length());
						textBar.updateText();
					}
				}
				return;
			}
		if(mouseX>=saveButton.x
				&&mouseY>=saveButton.y
				&&mouseX<saveButton.x+saveButton.w
				&&mouseY<saveButton.y+saveButton.h){
			if(!textBar.text.isEmpty())close(true);
			return;
		}
		if(mouseX>=upArrowButton.x
				&&mouseY>=upArrowButton.y
				&&mouseX<upArrowButton.x+upArrowButton.w
				&&mouseY<upArrowButton.y+upArrowButton.h){
			File f = DIRECTORY.getParentFile();
			if(f!=null){
				DIRECTORY = f;
				selectDirectory(null);
				scrollPos = 0;
				updateShownFiles();
			}
			return;
		}
	}
	private void selectDirectory(UiElement ele){
		if(ele==null){
			selectedDirectory.x = 0;
			selectedDirectory.y = 0;
			selectedDirectory.w = 0;
			selectedDirectory.h = 0;
			selectedIndex = -1;
		}else{
			selectedDirectory.x = ele.x;
			selectedDirectory.y = ele.y;
			selectedDirectory.w = ele.w;
			selectedDirectory.h = ele.h;
		}
	}
	public void onMouseUp(){
		//TODO
	}
	public void onMouseMove(double x, double y){
		mouseX = x;
		mouseY = Loop.screenRes.height-y;
	}
	public void onMouseWheel(double scrollAmount){
		scrollPos -= scrollAmount*10;
		int itemsShown = (int)foregroundFrame.h/17;
		int maxScroll = (tempShownFiles.length-itemsShown)*17;
		if(scrollPos>maxScroll)scrollPos = maxScroll;
		if(scrollPos<0)scrollPos = 0;
		firstShown = (int)Math.ceil(scrollPos/17f);
		lastShown = Math.min(firstShown+itemsShown, tempShownFiles.length);
	}
	private void close(boolean accept){
		INSTANCE = null;
		dispose();
		if(accept)onAccept.run(new File(DIRECTORY, textBar.text+textBar.appenedText));
	}
	public void onKey(int key, int action){
		if(key==GLFW.GLFW_KEY_ESCAPE
				&&action==GLFW.GLFW_RELEASE){
			close(false);
			return;
		}
		if(save)textBar.onKey(key, action);
	}
	private void updateShownFiles(){
		if(tempShownFiles!=null)
			for(UiElement ele : tempShownFiles)
				ele.texture.dispose();
		ArrayList<UiElement> shownFiles = new ArrayList();
		ArrayList<File> shownFilesRaw = new ArrayList();
		File[] files = DIRECTORY.listFiles();
		if(files!=null)
			for(File f : files)
				if(f.isDirectory()
						||f.getName().endsWith(appenedText)){
					shownFiles.add(generateElement(f, shownFiles.size()));
					shownFilesRaw.add(f);
				}
		shownFiles.toArray(tempShownFiles = new UiElement[shownFiles.size()]);
		shownFilesRaw.toArray(tempShownFilesRaw = new File[shownFilesRaw.size()]);
		onMouseWheel(0);
		generatePathDirectory();
		if(!save){
			textBar.text = "";
			textBar.updateText();
		}
	}
	private UiElement generateElement(File f, int pos){
		TextBox textBox = new TextBox(f.getName(), (int)foregroundFrame.w, 17, Color.black);
		UiElement ele = new UiElement(textBox.getTexture());
		textBox.disposeGraphics();
		ele.x = foregroundFrame.x;
		ele.y = (foregroundFrame.y+foregroundFrame.h)-(pos+1)*17;
		ele.w = (int)foregroundFrame.w;
		ele.h = 17;
		return ele;
	}
	public void update(double time){
		textBar.update(time);
	}
	private void generatePathDirectory(){
		if(pathDirectory.texture!=null)pathDirectory.texture.dispose();
		TextBox textBox = new TextBox(DIRECTORY.getAbsolutePath(), (int)(foregroundFrame.w-135), 17, Color.black);
		pathDirectory.texture = textBox.getTexture();
		textBox.disposeGraphics();
	}
	private void dispose(){
		if(tempShownFiles!=null)
			for(UiElement ele : tempShownFiles)
				ele.texture.dispose();
		pathDirectory.texture.dispose();
		selectedDirectory.texture.dispose();
		backgroundFrame.texture.dispose();
		foregroundFrame.texture.dispose();
		upArrowButton.texture.dispose();
		saveButton.texture.dispose();
		textBar.dispose();
	}
}