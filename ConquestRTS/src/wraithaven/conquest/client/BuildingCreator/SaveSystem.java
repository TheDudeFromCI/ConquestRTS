package wraithaven.conquest.client.BuildingCreator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import org.lwjgl.opengl.GL11;
import wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing.IndexManager;
import wraithaven.conquest.client.GameWorld.LoopControls.MainLoop;
import wraithaven.conquest.client.LoadingScreenTask;
import wraithaven.conquest.client.LoadingScreen;
import wraithaven.conquest.client.GameWorld.Voxel.BlockRotation;
import wraithaven.conquest.client.GameWorld.Voxel.Block;
import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;
import wraithaven.conquest.client.GameWorld.Voxel.BlockShapes.ShapeType;

public class SaveSystem{
	public static void save(File file){
		int x, y, z, i;
		try{
			StringBuilder s = new StringBuilder();
			Block block;
			for(i = 0; i<Loop.INSTANCE.getVoxelWorld().getIndexManager().blocks.size(); i++){
				block = Loop.INSTANCE.getVoxelWorld().getIndexManager().blocks.get(i);
				s.append(ShapeType.indexOf(block.shape));
				s.append(',');
				s.append(block.rotation.index);
				s.append(',');
				s.append(CubeTextures.encrypt(block.originalCubeTextures));
				s.append('@');
			}
			if(s.charAt(s.length()-1)=='@')s.deleteCharAt(s.length()-1);
			s.append('¥');
			for(x = 0; x<BuildingCreator.WORLD_BOUNDS_SIZE; x++)
				for(y = 0; y<BuildingCreator.WORLD_BOUNDS_SIZE; y++)
					for(z = 0; z<BuildingCreator.WORLD_BOUNDS_SIZE; z++){
						s.append(Loop.INSTANCE.getVoxelWorld().getBlock(x, y, z));
						s.append(',');
					}
			if(s.charAt(s.length()-1)==',')s.deleteCharAt(s.length()-1);
			s.append('¥');
			s.append(Loop.INSTANCE.getCamera().goalX);
			s.append(',');
			s.append(Loop.INSTANCE.getCamera().goalY);
			s.append(',');
			s.append(Loop.INSTANCE.getCamera().goalZ);
			s.append(',');
			s.append(Loop.INSTANCE.getCamera().rx);
			s.append(',');
			s.append(Loop.INSTANCE.getCamera().ry);
			s.append(',');
			s.append(Loop.INSTANCE.getCamera().rz);
			s.append('¥');
			for(BlockIcon b : Loop.INSTANCE.getInventory().getBlocks()){
				s.append(ShapeType.indexOf(b.block.block.shape));
				s.append(',');
				s.append(CubeTextures.encrypt(b.block.block.originalCubeTextures));
				s.append('@');
			}
			if(s.charAt(s.length()-1)=='@')s.deleteCharAt(s.length()-1);
			s.append('¥');
			for(i = 0; i<10; i++){
				BlockIcon icon = Loop.INSTANCE.getGuiHandler().getIconManager().getIcon(i);
				if(icon==null)s.append(-1);
				else s.append(Loop.INSTANCE.getInventory().getBlocks().indexOf(icon));
				if(i<9)s.append(',');
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(s.toString());
			out.close();
		}catch(Exception exception){
			exception.printStackTrace();
			System.exit(1);
		}
	}
	public static void load(final File file){
		Loop.INSTANCE.setLoadingScreen(new LoadingScreen(new LoadingScreenTask(){
			int chunkLimit = BuildingCreator.WORLD_BOUNDS_SIZE-1;
			float percent, loadingPercent, rebuildingPercent;
			int x, y, z, w, blockIndex;
			String[] parts4;
			String[] parts1;
			byte initalized;
			String wholeFile;
			private void init0(){
				initalized = 2;
				try{
					BufferedReader in = new BufferedReader(new FileReader(file));
					wholeFile = in.readLine();
					in.close();
					Loop.INSTANCE.getVoxelWorld().clearAll();
					Loop.INSTANCE.getInventory().getBlocks().clear();
				}catch(Exception exception){
					exception.printStackTrace();
					System.exit(1);
				}
			}
			private void init1(){
				initalized = 3;
				parts1 = wholeFile.split("¥");
				String[] parts2 = parts1[0].split("@");
				for(String s : parts2){
					String[] parts3 = s.split(",");
					Loop.INSTANCE.getVoxelWorld().getIndexManager().blocks.add(new Block(ShapeType.values()[Integer.valueOf(parts3[0])].shape, CubeTextures.decrypt(parts3[2]), BlockRotation.getRotation(Integer.valueOf(parts3[1]))));
				}
				parts4 = parts1[1].split(",");
				String[] parts5 = parts1[2].split(",");
				Loop.INSTANCE.getCamera().goalX = Loop.INSTANCE.getCamera().x = Float.valueOf(parts5[0]);
				Loop.INSTANCE.getCamera().goalY = Loop.INSTANCE.getCamera().y = Float.valueOf(parts5[1]);
				Loop.INSTANCE.getCamera().goalZ = Loop.INSTANCE.getCamera().z = Float.valueOf(parts5[2]);
				Loop.INSTANCE.getCamera().rx = Float.valueOf(parts5[3]);
				Loop.INSTANCE.getCamera().ry = Float.valueOf(parts5[4]);
				Loop.INSTANCE.getCamera().rz = Float.valueOf(parts5[5]);
			}
			private void init2(){
				initalized = 4;
				if(!parts1[3].isEmpty()){
					String[] parts6 = parts1[3].split("@");
					for(String s : parts6){
						String[] parts7 = s.split(",");
						Loop.INSTANCE.getInventory().addBlock(ShapeType.values()[Integer.valueOf(parts7[0])].shape, CubeTextures.decrypt(parts7[1]));
					}
				}
				if(!parts1[4].isEmpty()){
					String[] parts8 = parts1[4].split(",");
					for(int i = 0; i<10; i++){
						int index = Integer.valueOf(parts8[i]);
						Loop.INSTANCE.getGuiHandler().getIconManager().addIcon(index==-1?null:Loop.INSTANCE.getInventory().getBlocks().get(index), i);
					}
				}
			}
			private void load(){
				for(int i = 0; i<64; i++){
					short index = Short.valueOf(parts4[blockIndex]);
					if(index!=IndexManager.AIR_BLOCK)Loop.INSTANCE.getVoxelWorld().setBlock(x, y, z, index, false);
					blockIndex++;
					z++;
					if(z>chunkLimit){
						z = 0;
						y++;
						if(y>chunkLimit){
							y = 0;
							x++;
							if(x>chunkLimit){
								loadingPercent = 1;
								return;
							}
						}
					}
				}
				loadingPercent = blockIndex/(float)parts4.length;
			}
			private void rebuild(){
				for(int i = 0; i<64; i++){
					Loop.INSTANCE.getVoxelWorld().getChunk(w).rebuild();
					w++;
					rebuildingPercent = w/(float)Loop.INSTANCE.getVoxelWorld().getChunkCount();
					if(w==Loop.INSTANCE.getVoxelWorld().getChunkCount()){
						rebuildingPercent = 1;
						return;
					}
				}
			}
			public int update(){
				if(initalized==0){
					initalized = 1;
					return 0;
				}
				if(initalized==1){
					init0();
					return 1;
				}
				if(initalized==2){
					init1();
					return 2;
				}
				if(initalized==3){
					init2();
					return 3;
				}
				if(loadingPercent<1)load();
				else rebuild();
				percent = loadingPercent*0.2f+rebuildingPercent*0.77f+0.031f;
				return (int)(percent*100);
			}
		}, new Runnable(){
			public void run(){
				Loop.INSTANCE.setLoadingScreen(null);
				GL11.glClearColor(219/255f, 246/255f, 251/255f, 0);
				MainLoop.FPS_SYNC = true;
			}
		}));
		MainLoop.FPS_SYNC = false;
	}
}