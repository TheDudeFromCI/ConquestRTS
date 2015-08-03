package com.wraithavens.conquest.SinglePlayer.Blocks.Octree;

import java.util.ArrayList;
import com.wraithavens.conquest.SinglePlayer.Blocks.ChunkPainter;
import com.wraithavens.conquest.SinglePlayer.Blocks.VoxelChunk;
import com.wraithavens.conquest.Utility.Algorithms;

public class Octree{
	private static boolean condenseVoxel(OctreeBranch branch){
		if(containsChunk(branch))
			return false;
		branch.children = null;
		return true;
	}
	private static boolean containsChunk(OctreeBranch branch){
		if(branch==null)
			return false;
		if(branch.owner instanceof ChunkPainter)
			return true;
		if(branch.children==null)
			return false;
		for(int i = 0; i<8; i++)
			if(containsChunk(branch.children[i]))
				return true;
		return false;
	}
	private static int getChildIndex(VoxelChunk parent, VoxelChunk child){
		int half = parent.getSize()/2;
		return (child.getX()<parent.getX()+half?1:0)|(child.getY()<parent.getY()+half?2:0)
			|(child.getZ()<parent.getZ()+half?4:0);
	}
	private static final int OctreeDepth = 10; // 16384 Blocks^2
	private static final int MainBranchSize = (int)Math.pow(2, OctreeDepth);
	private final ArrayList<OctreeBranch> branches = new ArrayList();
	private boolean stop = false;
	public void addVoxel(VoxelChunk voxel){
		// ---
		// Loop through all branches and load the parent branch of the new
		// chunk.
		// ---
		OctreeBranch parent = null;
		for(OctreeBranch b : branches)
			if(b.owner.containsBlock(voxel.getX(), voxel.getY(), voxel.getZ())){
				parent = b;
				break;
			}
		// ---
		// ... Or create a new one if needed.
		// ---
		if(parent==null){
			parent =
				new OctreeBranch(new VoxelChunk(Algorithms.groupLocation(voxel.getX(), MainBranchSize),
					Algorithms.groupLocation(voxel.getY(), MainBranchSize), Algorithms.groupLocation(
						voxel.getZ(), MainBranchSize), MainBranchSize));
			branches.add(parent);
		}
		placeVoxel(parent, voxel);
	}
	public void clear(){
		for(int i = 0; i<branches.size(); i++)
			clear(branches.get(i));
		branches.clear();
	}
	public boolean containsChunk(int x, int y, int z){
		for(int i = 0; i<branches.size(); i++)
			if(branches.get(i).owner.containsBlock(x, y, z))
				return containsChunk(branches.get(i), x, y, z);
		return false;
	}
	public void removeVoxel(VoxelChunk voxel){
		for(int i = 0; i<branches.size(); i++)
			if(branches.get(i).owner.containsBlock(voxel.getX(), voxel.getY(), voxel.getZ())){
				removeVoxel(branches.get(i), voxel);
				if(branches.get(i).children==null)
					branches.remove(i);
				return;
			}
	}
	public void runTask(OctreeTask task){
		stop = false;
		task.prepareRun();
		for(OctreeBranch b : branches)
			testBranch(b, task);
	}
	private void clear(OctreeBranch b){
		if(b==null)
			return;
		if(b.owner instanceof ChunkPainter){
			((ChunkPainter)b.owner).dispose();
			return;
		}
		if(b.children==null)
			return;
		for(int i = 0; i<8; i++)
			clear(b.children[i]);
	}
	private boolean containsChunk(OctreeBranch b, int x, int y, int z){
		if(b==null)
			return false;
		if(b.owner instanceof ChunkPainter)
			return b.owner.getX()==x&&b.owner.getY()==y&&b.owner.getZ()==z;
		if(b.children==null)
			return false;
		for(int i = 0; i<8; i++)
			if(containsChunk(b.children[i], x, y, z))
				return true;
		return false;
	}
	private void placeVoxel(OctreeBranch parent, VoxelChunk child){
		if(parent.children==null)
			parent.children = new OctreeBranch[8];
		if(parent.owner.getSize()==32){
			parent.children[getChildIndex(parent.owner, child)] = new OctreeBranch(child);
			return;
		}
		int index = getChildIndex(parent.owner, child);
		if(parent.children[index]==null){
			int voxX = Algorithms.groupLocation(child.getX(), parent.owner.getSize()/2);
			int voxY = Algorithms.groupLocation(child.getY(), parent.owner.getSize()/2);
			int voxZ = Algorithms.groupLocation(child.getZ(), parent.owner.getSize()/2);
			parent.children[index] =
				new OctreeBranch(new VoxelChunk(voxX, voxY, voxZ, parent.owner.getSize()/2));
		}
		placeVoxel(parent.children[index], child);
	}
	private void removeVoxel(OctreeBranch parent, VoxelChunk child){
		if(parent.children==null)
			return;
		int index = getChildIndex(parent.owner, child);
		if(parent.children[index]==null){
			condenseVoxel(parent);
			return;
		}
		if(parent.children[index].owner==child){
			parent.children[index] = null;
			condenseVoxel(parent);
			return;
		}
		removeVoxel(parent.children[index], child);
		condenseVoxel(parent);
	}
	private void testBranch(OctreeBranch branch, OctreeTask task){
		if(stop)
			return;
		if(!task.shouldRun(branch.owner))
			return;
		if(branch.children!=null){
			for(int i = 0; i<branch.children.length; i++)
				if(branch.children[i]!=null)
					testBranch(branch.children[i], task);
		}else
			task.run(branch.owner);
	}
	void stop(){
		stop = true;
	}
}
