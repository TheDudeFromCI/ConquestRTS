package wraithaven.conquest.client.BuildingCreator.BlockPalette;

import org.lwjgl.opengl.GL11;

public class PalleteRenderer{
	private boolean mouseDown;
	private double mouseDownX, mouseDownY;
	private FloatingBlock floatingBlock;
	private static final float MOUSE_SENSITIVITY = 0.5f;
	public PalleteRenderer(){
		GL11.glClearColor(0.1490196078431373f, 0.6235294117647059f, 0.9215686274509804f, 0);
		floatingBlock=new FloatingBlock();
		floatingBlock.z=-3;
		floatingBlock.x=1f;
	}
	public void onMouseMove(double x, double y){
		if(mouseDown){
			floatingBlock.shiftRY+=(x-mouseDownX)*MOUSE_SENSITIVITY;
			floatingBlock.shiftRX+=(y-mouseDownY)*MOUSE_SENSITIVITY;
			mouseDownX=x;
			mouseDownY=y;
		}
	}
	public void onMouseDown(double x, double y){
		this.mouseDownX=x;
		this.mouseDownY=y;
		mouseDown=true;
	}
	public void dispose(){
		GL11.glClearColor(0, 0, 0, 0);
		floatingBlock=null;
	}
	public void onMouseUp(){ mouseDown=false; }
	public void render(){ floatingBlock.render(); }
	@SuppressWarnings("unused")public void update(double delta, double time){ floatingBlock.update(time); }
}