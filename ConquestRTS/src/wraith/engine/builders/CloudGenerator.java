package wraith.engine.builders;

import java.io.File;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.Math.Vector4f;
import com.wraithavens.conquest.SinglePlayer.Noise.CloudNoise;
import com.wraithavens.conquest.SinglePlayer.Skybox.SkyboxBuilder;
import com.wraithavens.conquest.SinglePlayer.Skybox.SkyboxClouds;
import com.wraithavens.conquest.Utility.BinaryFile;
import com.wraithavens.conquest.Utility.PowerInterpolation;

public class CloudGenerator{
	public static void main(String[] args){
		System.out.println("Generating clouds.");
		long time;
		int i, j, k;
		for(j = 0; j<2; j++){
			for(i = 0; i<SkyboxClouds.CloudCombinationCount; i++){
				time = System.currentTimeMillis();
				File file =
					new File(System.getProperty("user.dir")+File.separatorChar+"Data"+File.separatorChar
						+"Assets"+File.separatorChar+"Sky", i+(j==0?"b":"")+".dat");
				System.out.println("Generating clouds: "+file.getName());
				BinaryFile bin =
					new BinaryFile(SkyboxClouds.TextureSize*SkyboxClouds.TextureSize*(j==0?3:4)*6*4);
				generateCloudNoise(j==0);
				for(k = 0; k<6; k++)
					makeSide(k, bin, j==0);
				bin.compile(file);
				System.out.println("Finished in "+(System.currentTimeMillis()-time)+" ms.");
			}
		}
		System.out.println("All clouds generated.");
	}
	private static void blendOver(){
		if(temp.w>=1.0f){
			temp.set(temp.x, temp.y, temp.z, 1.0f);
			return;
		}
		temp.set(temp.x*temp.w+skyColor.x*(1.0f-temp.w), temp.y*temp.w+skyColor.y*(1.0f-temp.w), temp.z*temp.w
			+skyColor.z*(1.0f-temp.w), 1.0f);
	}
	private static void generateCloudNoise(boolean backdrop){
		if(backdrop){
			SkyboxBuilder builder = new SkyboxBuilder();
			builder.setBackdrop(true);
			builder.setSeed((long)(Math.random()*Long.MAX_VALUE));
			builder.setSmoothness(50);
			builder.setDetail(3);
			builder.setFunction(SkyboxBuilder.Cerp);
			PowerInterpolation Perp2 = new PowerInterpolation(2);
			builder.setColorFunction(Perp2);
			builder.setMaxColorWeight(2);
			builder.setMaxColor(new Vector4f(1.0f, 1.0f, 1.0f, 0.8f));
			noise = builder.buildNoise();
		}else{
			SkyboxBuilder builder = new SkyboxBuilder();
			builder.setBackdrop(false);
			builder.setSeed((long)(Math.random()*Long.MAX_VALUE));
			builder.setSmoothness(70);
			builder.setDetail(4);
			builder.setFunction(SkyboxBuilder.Cerp);
			builder.setColorFunction(SkyboxBuilder.Lerp);
			builder.setMaxColorWeight(2);
			builder.setMaxColor(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
			noise = builder.buildNoise();
		}
	}
	private static void makeSide(int side, BinaryFile bin, boolean backdrop){
		int x, y, z;
		if(side==0){
			x = SkyboxClouds.TextureSize-1;
			for(y = SkyboxClouds.TextureSize-1; y>=0; y--)
				for(z = SkyboxClouds.TextureSize-1; z>=0; z--)
					placeColor(x, y, z, bin, backdrop);
		}else if(side==1){
			x = 0;
			for(y = SkyboxClouds.TextureSize-1; y>=0; y--)
				for(z = 0; z<SkyboxClouds.TextureSize; z++)
					placeColor(x, y, z, bin, backdrop);
		}else if(side==2){
			y = SkyboxClouds.TextureSize-1;
			for(z = 0; z<SkyboxClouds.TextureSize; z++)
				for(x = 0; x<SkyboxClouds.TextureSize; x++)
					placeColor(x, y, z, bin, backdrop);
		}else if(side==3){
			y = 0;
			for(z = SkyboxClouds.TextureSize-1; z>=0; z--)
				for(x = 0; x<SkyboxClouds.TextureSize; x++)
					placeColor(x, y, z, bin, backdrop);
		}else if(side==4){
			z = SkyboxClouds.TextureSize-1;
			for(y = SkyboxClouds.TextureSize-1; y>=0; y--)
				for(x = 0; x<SkyboxClouds.TextureSize; x++)
					placeColor(x, y, z, bin, backdrop);
		}else{
			z = 0;
			for(y = SkyboxClouds.TextureSize-1; y>=0; y--)
				for(x = SkyboxClouds.TextureSize-1; x>=0; x--)
					placeColor(x, y, z, bin, backdrop);
		}
	}
	private static void placeColor(float x, float y, float z, BinaryFile bin, boolean backdrop){
		temp2.set(x-SkyboxClouds.TextureSize/2, y-SkyboxClouds.TextureSize/2, z-SkyboxClouds.TextureSize/2);
		temp2.normalize();
		temp2.scale(SkyboxClouds.TextureSize);
		noise.noise(temp2.x, temp2.y, temp2.z, temp);
		if(backdrop)
			blendOver();
		bin.addFloat(temp.x);
		bin.addFloat(temp.y);
		bin.addFloat(temp.z);
		if(!backdrop)
			bin.addFloat(temp.w);
	}
	private static final Vector3f skyColor = new Vector3f(0.4f, 0.6f, 0.9f);
	private static CloudNoise noise;
	private static final Vector4f temp = new Vector4f();
	private static final Vector3f temp2 = new Vector3f();
}
