package wraithaven.conquest.client.BuildingCreator.BlockPalette;

public class PalleteRenderer{
	private UI ui = new UI();
	public void dispose(){
		ui.dispose();
	}
	public void onMouseDown(double x, double y){
		ui.onMouseDown(x, y);
	}
	public void onMouseMove(double x, double y){
		ui.onMouseMove(x, y);
	}
	public void onMouseUp(int button){
		ui.onMouseUp(button);
	}
	public void render(){
		ui.render();
	}
	public void update(double time){
		ui.update(time);
	}
}