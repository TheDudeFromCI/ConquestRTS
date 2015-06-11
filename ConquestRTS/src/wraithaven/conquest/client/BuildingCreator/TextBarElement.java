package wraithaven.conquest.client.BuildingCreator;

import java.awt.Color;
import org.lwjgl.glfw.GLFW;
import wraithaven.conquest.client.GameWorld.WindowUtil.OnScreenText.TextBox;
import wraithaven.conquest.client.BuildingCreator.BlockPalette.UiElement;

public class TextBarElement extends UiElement{
	private static final float CARRET_SPEED = 0.5f;
	public boolean selected;
	private boolean leftShift, rightShift;
	public String appenedText = "";
	public String text = "";
	private boolean cursor;
	private final TextBox textBox;
	public TextBarElement(int width, Color background){
		textBox = new TextBox("", width, 17, Color.black, background);
		texture = textBox.getTexture();
	}
	public TextBarElement(int width){
		textBox = new TextBox("", width, 13, Color.black);
		texture = textBox.getTexture();
	}
	public void onKey(int key, int action){
		if(key==GLFW.GLFW_KEY_LEFT_SHIFT){
			if(action==GLFW.GLFW_PRESS)leftShift = true;
			else if(action==GLFW.GLFW_RELEASE)leftShift = false;
			return;
		}
		if(key==GLFW.GLFW_KEY_RIGHT_SHIFT){
			if(action==GLFW.GLFW_PRESS)rightShift = true;
			else if(action==GLFW.GLFW_RELEASE)rightShift = false;
			return;
		}
		if(action==GLFW.GLFW_PRESS
				||action==GLFW.GLFW_REPEAT){
			switch(key){
				case GLFW.GLFW_KEY_A:
					appendCharacter('a');
					break;
				case GLFW.GLFW_KEY_B:
					appendCharacter('b');
					break;
				case GLFW.GLFW_KEY_C:
					appendCharacter('c');
					break;
				case GLFW.GLFW_KEY_D:
					appendCharacter('d');
					break;
				case GLFW.GLFW_KEY_E:
					appendCharacter('e');
					break;
				case GLFW.GLFW_KEY_F:
					appendCharacter('f');
					break;
				case GLFW.GLFW_KEY_G:
					appendCharacter('g');
					break;
				case GLFW.GLFW_KEY_H:
					appendCharacter('h');
					break;
				case GLFW.GLFW_KEY_I:
					appendCharacter('i');
					break;
				case GLFW.GLFW_KEY_J:
					appendCharacter('j');
					break;
				case GLFW.GLFW_KEY_K:
					appendCharacter('k');
					break;
				case GLFW.GLFW_KEY_L:
					appendCharacter('l');
					break;
				case GLFW.GLFW_KEY_M:
					appendCharacter('m');
					break;
				case GLFW.GLFW_KEY_N:
					appendCharacter('n');
					break;
				case GLFW.GLFW_KEY_O:
					appendCharacter('o');
					break;
				case GLFW.GLFW_KEY_P:
					appendCharacter('p');
					break;
				case GLFW.GLFW_KEY_Q:
					appendCharacter('q');
					break;
				case GLFW.GLFW_KEY_R:
					appendCharacter('r');
					break;
				case GLFW.GLFW_KEY_S:
					appendCharacter('s');
					break;
				case GLFW.GLFW_KEY_T:
					appendCharacter('t');
					break;
				case GLFW.GLFW_KEY_U:
					appendCharacter('u');
					break;
				case GLFW.GLFW_KEY_V:
					appendCharacter('v');
					break;
				case GLFW.GLFW_KEY_W:
					appendCharacter('w');
					break;
				case GLFW.GLFW_KEY_X:
					appendCharacter('x');
					break;
				case GLFW.GLFW_KEY_Y:
					appendCharacter('y');
					break;
				case GLFW.GLFW_KEY_Z:
					appendCharacter('z');
					break;
				case GLFW.GLFW_KEY_BACKSPACE:
					deleteLast();
					break;
				case GLFW.GLFW_KEY_SPACE:
					appendCharacter(' ');
					break;
				default:
					break;
			}
		}
	}
	private void appendCharacter(char c){
		if(isShift())c = Character.toUpperCase(c);
		text += c;
		updateText();
	}
	private void deleteLast(){
		if(text.isEmpty())return;
		text = text.substring(0, text.length()-1);
		updateText();
	}
	private boolean isShift(){
		return leftShift||rightShift;
	}
	public void updateText(){
		if(cursor)textBox.setTextWithCursor(text+appenedText, 0, text.length());
		else textBox.setText(text+appenedText);
	}
	private void toggleCursor(){
		cursor = !cursor;
		updateText();
	}
	public void update(double time){
		boolean showCarret = time%(CARRET_SPEED*2)>CARRET_SPEED;
		if(showCarret!=cursor)toggleCursor();
	}
	public void dispose(){
		textBox.dispose();
	}
}