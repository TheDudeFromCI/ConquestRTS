package com.wraithavens.conquest.SinglePlayer.Entities;

import java.io.File;
import java.util.ArrayList;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;

public class ModelGroup{
	public final ArrayList<ModelInstance> instances = new ArrayList();
	private final ShaderProgram modelShader;
	static int LOCATION;
	public ModelGroup(){
		modelShader = new ShaderProgram(new File(WraithavensConquest.assetFolder, "Entity Shader.vert"), null, new File(WraithavensConquest.assetFolder, "Entity Shader.frag"));
		ModelGroup.LOCATION = modelShader.getAttributeLocation("weight");
		GL20.glEnableVertexAttribArray(ModelGroup.LOCATION);
	}
	public void render(){
		modelShader.bind();
		for(int i = 0; i<instances.size(); i++)
			instances.get(i).render();
	}
	public void dispose(){
		for(int i = 0; i<instances.size(); i++)
			instances.get(i).dispose();
	}
}