package wraithaven.conquest.client.GameWorld.WindowUtil;

import java.util.ArrayList;

public abstract class GuiContainer extends GuiComponent{
	protected ArrayList<GuiComponent> components = new ArrayList();
	private GuiLayout layout;
	public GuiContainer(GuiContainer parent, int bufferWidth, int bufferHeight){
		super(parent, bufferWidth, bufferHeight);
	}
	public void addComponent(GuiComponent... component){
		for(GuiComponent c : component)
			components.add(c);
		validate();
	}
	public void clearChildren(){
		components.clear();
		validate();
	}
	@Override public void dispose(){
		super.dispose();
		components.clear();
		components = null;
		layout = null;
	}
	public GuiLayout getLayout(){
		return layout;
	}
	public void removeComponent(GuiComponent... component){
		for(GuiComponent c : component)
			components.remove(c);
		validate();
	}
	public void setLayout(GuiLayout layout){
		this.layout = layout;
		validate();
	}
	@Override public void setRepainted(){
		if(needsRepaint){
			super.setRepainted();
			for(int i = 0; i<components.size(); i++)
				components.get(i).setRepainted();
		}
	}
	@Override public void setSizeAndLocation(int x, int y, int width, int height){
		super.setSizeAndLocation(x, y, width, height);
		validate();
	}
	private void validate(){
		if(layout!=null){
			layout.setParentDimensions(x, y, width, height);
			layout.validateComponents(components);
			setNeedsRepaint();
		}
	}
}