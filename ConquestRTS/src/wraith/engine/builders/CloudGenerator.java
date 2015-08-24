package wraith.engine.builders;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.Math.Vector4f;
import com.wraithavens.conquest.SinglePlayer.Noise.CloudNoise;
import com.wraithavens.conquest.SinglePlayer.Skybox.SkyboxBuilder;
import com.wraithavens.conquest.SinglePlayer.Skybox.SkyboxClouds;
import com.wraithavens.conquest.Utility.BinaryFile;
import com.wraithavens.conquest.Utility.PowerInterpolation;

public class CloudGenerator{
	public static void main(String[] args){
		// buildPreview();
		final int type = 1;
		System.out.println("Generating clouds. Type = "+getTypeName(type));
		long time;
		int i, j, k;
		for(j = 0; j<2; j++){
			for(i = 0; i<SkyboxClouds.CloudCombinationCount; i++){
				time = System.currentTimeMillis();
				File file =
					new File(System.getProperty("user.dir")+File.separatorChar+"Data"+File.separatorChar
						+"Assets"+File.separatorChar+"Sky", i+(j==0?"b":"a")+type+".dat");
				System.out.println("Generating clouds: "+file.getName());
				BinaryFile bin =
					new BinaryFile(SkyboxClouds.TextureSize*SkyboxClouds.TextureSize*(j==0?3:4)*6*4);
				generateCloudNoise(j==0, type);
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
	@SuppressWarnings("unused")
	private static void buildPreview(){
		final BufferedImage image =
			new BufferedImage(SkyboxClouds.TextureSize, SkyboxClouds.TextureSize, BufferedImage.TYPE_INT_ARGB);
		graphics = image.createGraphics();
		JFrame frame = new JFrame();
		panel = new JPanel(){
			@Override
			public void paintComponent(Graphics g){
				g.setColor(new Color(0.4f, 0.6f, 0.9f));
				g.fillRect(0, 0, getWidth(), getHeight());
				g.drawImage(image, 0, 0, null);
				g.dispose();
			}
		};
		frame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				frame.dispose();
				graphics.dispose();
				graphics = null;
				panel = null;
			}
		});
		panel.setPreferredSize(new Dimension(SkyboxClouds.TextureSize, SkyboxClouds.TextureSize));
		frame.add(panel);
		frame.pack();
		frame.setResizable(false);
		frame.setTitle("Cloud Preview");
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	private static void generateCloudNoise(boolean backdrop, int type){
		if(type==0){
			if(backdrop){
				SkyboxBuilder builder = new SkyboxBuilder();
				builder.setSeed((long)(Math.random()*Long.MAX_VALUE));
				builder.setSmoothness(20);
				builder.setDetail(3);
				builder.setFunction(SkyboxBuilder.Cerp);
				PowerInterpolation Perp2 = new PowerInterpolation(2);
				builder.setColorFunction(Perp2);
				builder.setMaxColorWeight(200);
				builder.setMaxColor(new Vector4f(1.0f, 1.0f, 1.0f, 0.8f));
				builder.newSubNoise();
				builder.setMaxColorWeight(1);
				builder.setMaxColor(new Vector4f(1.0f, 1.0f, 1.0f, 0.0f));
				builder.setAmplitude(0.8f);
				builder.setSmoothness(10);
				noise = builder.buildNoise();
			}else{
				SkyboxBuilder builder = new SkyboxBuilder();
				builder.setSeed((long)(Math.random()*Long.MAX_VALUE));
				builder.setSmoothness(25);
				builder.setDetail(4);
				builder.setFunction(SkyboxBuilder.Cerp);
				builder.setColorFunction(SkyboxBuilder.Cerp);
				builder.setMaxColorWeight(25);
				builder.setMaxColor(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
				builder.newSubNoise();
				builder.setMaxColorWeight(1);
				builder.setMaxColor(new Vector4f(1.0f, 1.0f, 1.0f, 0.0f));
				builder.setAmplitude(0.8f);
				builder.setSmoothness(10);
				noise = builder.buildNoise();
			}
		}else if(type==1){
			if(backdrop){
				SkyboxBuilder builder = new SkyboxBuilder();
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
	}
	private static String getTypeName(int type){
		switch(type){
			case 0:
				return "Sunny";
			case 1:
				return "Stormy";
			default:
				return null;
		}
	}
	private static void makeSide(int side, BinaryFile bin, boolean backdrop){
		int x, y, z;
		if(side==0){
			if(graphics!=null)
				graphics.clearRect(0, 0, SkyboxClouds.TextureSize, SkyboxClouds.TextureSize);
			x = SkyboxClouds.TextureSize-1;
			for(y = SkyboxClouds.TextureSize-1; y>=0; y--)
				for(z = SkyboxClouds.TextureSize-1; z>=0; z--)
					placeColor(x, y, z, bin, backdrop);
			if(panel!=null)
				panel.repaint();
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
		bin.addByte((byte)(temp.x*255));
		bin.addByte((byte)(temp.y*255));
		bin.addByte((byte)(temp.z*255));
		if(!backdrop)
			bin.addByte((byte)(temp.w*255));
		if(x==SkyboxClouds.TextureSize-1&&graphics!=null){
			try{
				graphics.setColor(new Color(temp.x, temp.y, temp.z, temp.w));
				graphics.fillRect((int)y, (int)z, 1, 1);
			}catch(Exception exception){}
		}
	}
	private static Graphics2D graphics;
	private static JPanel panel;
	private static final Vector3f skyColor = new Vector3f(0.4f, 0.6f, 0.9f);
	private static CloudNoise noise;
	private static final Vector4f temp = new Vector4f();
	private static final Vector3f temp2 = new Vector3f();
}
