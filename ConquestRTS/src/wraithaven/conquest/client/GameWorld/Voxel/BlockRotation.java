package wraithaven.conquest.client.GameWorld.Voxel;

import wraithaven.conquest.client.GameWorld.Vec3i;
import wraithaven.conquest.client.GameWorld.LoopControls.Vector4f;
import wraithaven.conquest.client.GameWorld.LoopControls.Vector3f;
import wraithaven.conquest.client.GameWorld.LoopControls.Matrix4f;

public class BlockRotation{
	public final int rx, ry, rz;
	private final Matrix4f rotMat = new Matrix4f();
	public final int index;
	private static final Vector4f tempVec = new Vector4f();
	private static final Vector3f X_AXIS = new Vector3f(1, 0, 0);
	private static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);
	private static final Vector3f Z_AXIS = new Vector3f(0, 0, 1);
	private static final int[] TEMP_CORDS = new int[3];
	public static final BlockRotation ROTATION_0 = new BlockRotation(new Vec3i(0, 0, 0), 0);
	public static final BlockRotation ROTATION_1 = new BlockRotation(new Vec3i(0, 0, 1), 1);
	public static final BlockRotation ROTATION_2 = new BlockRotation(new Vec3i(0, 0, 2), 2);
	public static final BlockRotation ROTATION_3 = new BlockRotation(new Vec3i(0, 0, 3), 3);
	public static final BlockRotation ROTATION_4 = new BlockRotation(new Vec3i(0, 1, 0), 4);
	public static final BlockRotation ROTATION_5 = new BlockRotation(new Vec3i(0, 1, 1), 5);
	public static final BlockRotation ROTATION_6 = new BlockRotation(new Vec3i(0, 1, 2), 6);
	public static final BlockRotation ROTATION_7 = new BlockRotation(new Vec3i(0, 1, 3), 7);
	public static final BlockRotation ROTATION_8 = new BlockRotation(new Vec3i(0, 2, 0), 8);
	public static final BlockRotation ROTATION_9 = new BlockRotation(new Vec3i(0, 2, 1), 9);
	public static final BlockRotation ROTATION_10 = new BlockRotation(new Vec3i(0, 2, 2), 10);
	public static final BlockRotation ROTATION_11 = new BlockRotation(new Vec3i(0, 2, 3), 11);
	public static final BlockRotation ROTATION_12 = new BlockRotation(new Vec3i(0, 3, 0), 12);
	public static final BlockRotation ROTATION_13 = new BlockRotation(new Vec3i(0, 3, 1), 13);
	public static final BlockRotation ROTATION_14 = new BlockRotation(new Vec3i(0, 3, 2), 14);
	public static final BlockRotation ROTATION_15 = new BlockRotation(new Vec3i(0, 3, 3), 15);
	public static final BlockRotation ROTATION_16 = new BlockRotation(new Vec3i(1, 0, 0), 16);
	public static final BlockRotation ROTATION_17 = new BlockRotation(new Vec3i(1, 0, 1), 17);
	public static final BlockRotation ROTATION_18 = new BlockRotation(new Vec3i(1, 0, 2), 18);
	public static final BlockRotation ROTATION_19 = new BlockRotation(new Vec3i(1, 0, 3), 19);
	public static final BlockRotation ROTATION_20 = new BlockRotation(new Vec3i(1, 2, 0), 20);
	public static final BlockRotation ROTATION_21 = new BlockRotation(new Vec3i(1, 2, 1), 21);
	public static final BlockRotation ROTATION_22 = new BlockRotation(new Vec3i(1, 2, 2), 22);
	public static final BlockRotation ROTATION_23 = new BlockRotation(new Vec3i(1, 2, 3), 23);
	private BlockRotation(Vec3i rotation, int index){
		this.index=index;
		if(rotation.x!=0)rotMat.rotate((float)(rotation.x*Math.PI/2), X_AXIS);
		if(rotation.y!=0)rotMat.rotate((float)(rotation.y*Math.PI/2), Y_AXIS);
		if(rotation.z!=0)rotMat.rotate((float)(rotation.z*Math.PI/2), Z_AXIS);
		rx=rotation.x;
		ry=rotation.y;
		rz=rotation.z;
	}
	public void rotate(int[] pos){
		tempVec.x=pos[0]-3.5f;
		tempVec.y=pos[1]-3.5f;
		tempVec.z=pos[2]-3.5f;
		tempVec.w=1;
		Matrix4f.transform(rotMat, tempVec, tempVec);
		pos[0]=Math.round(tempVec.x+3.5f);
		pos[1]=Math.round(tempVec.y+3.5f);
		pos[2]=Math.round(tempVec.z+3.5f);
	}
	public int rotateSide(int side){
		if(side==0){
			TEMP_CORDS[0]=-1;
			TEMP_CORDS[1]=0;
			TEMP_CORDS[2]=0;
		}
		if(side==1){
			TEMP_CORDS[0]=8;
			TEMP_CORDS[1]=0;
			TEMP_CORDS[2]=0;
		}
		if(side==2){
			TEMP_CORDS[0]=0;
			TEMP_CORDS[1]=-1;
			TEMP_CORDS[2]=0;
		}
		if(side==3){
			TEMP_CORDS[0]=0;
			TEMP_CORDS[1]=8;
			TEMP_CORDS[2]=0;
		}
		if(side==4){
			TEMP_CORDS[0]=0;
			TEMP_CORDS[1]=0;
			TEMP_CORDS[2]=-1;
		}
		if(side==5){
			TEMP_CORDS[0]=0;
			TEMP_CORDS[1]=0;
			TEMP_CORDS[2]=8;
		}
		rotate(TEMP_CORDS);
		if(TEMP_CORDS[0]==-1)return 0;
		if(TEMP_CORDS[0]==8)return 1;
		if(TEMP_CORDS[1]==-1)return 2;
		if(TEMP_CORDS[1]==8)return 3;
		if(TEMP_CORDS[2]==-1)return 4;
		if(TEMP_CORDS[2]==8)return 5;
		return -1;
	}
	private static int[] tempRotations = new int[6];
	public void rotateTextures(CubeTextures textures, int[] rotations){
		rotations[0]=textures.xUpRotation;
		rotations[1]=textures.xDownRotation;
		rotations[2]=textures.yUpRotation;
		rotations[3]=textures.yDownRotation;
		rotations[4]=textures.zUpRotation;
		rotations[5]=textures.zDownRotation;
		int i;
		for(i=0; i<rx; i++){
			storeTempRotations(rotations);
			rotations[0]++;
			rotations[1]++;
			rotations[2]=tempRotations[4];
			rotations[3]=tempRotations[5];
			rotations[4]=tempRotations[2];
			rotations[5]=tempRotations[3];
		}
		for(i=0; i<ry; i++){
			storeTempRotations(rotations);
			rotations[0]=tempRotations[4];
			rotations[1]=tempRotations[5];
			rotations[2]++;
			rotations[3]++;
			rotations[4]=tempRotations[0];
			rotations[5]=tempRotations[1];
		}
		for(i=0; i<rz; i++){
			storeTempRotations(rotations);
			rotations[0]=tempRotations[3];
			rotations[1]=tempRotations[2];
			rotations[2]=tempRotations[0];
			rotations[3]=tempRotations[1];
			rotations[4]++;
			rotations[5]++;
		}
		simplifyRotations(rotations);
	}
	private static void simplifyRotations(int[] r){ for(int i = 0; i<6; i++)r[i]%=4; }
	private static void storeTempRotations(int[] r){ for(int i = 0; i<6; i++)tempRotations[i]=r[i]; }
	public static BlockRotation getRotation(int index){
		if(index==0)return ROTATION_0;
		if(index==1)return ROTATION_1;
		if(index==2)return ROTATION_2;
		if(index==3)return ROTATION_3;
		if(index==4)return ROTATION_4;
		if(index==5)return ROTATION_5;
		if(index==6)return ROTATION_6;
		if(index==7)return ROTATION_7;
		if(index==8)return ROTATION_8;
		if(index==9)return ROTATION_9;
		if(index==10)return ROTATION_10;
		if(index==11)return ROTATION_11;
		if(index==12)return ROTATION_12;
		if(index==13)return ROTATION_13;
		if(index==14)return ROTATION_14;
		if(index==15)return ROTATION_15;
		if(index==16)return ROTATION_16;
		if(index==17)return ROTATION_17;
		if(index==18)return ROTATION_18;
		if(index==19)return ROTATION_19;
		if(index==20)return ROTATION_20;
		if(index==21)return ROTATION_21;
		if(index==22)return ROTATION_22;
		if(index==23)return ROTATION_23;
		return null;
	}
}