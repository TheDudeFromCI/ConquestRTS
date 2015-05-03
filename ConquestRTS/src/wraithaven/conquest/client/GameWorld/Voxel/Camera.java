package wraithaven.conquest.client.GameWorld.Voxel;

import org.lwjgl.opengl.GL11;
import com.sun.javafx.geom.Vec3f;

public class Camera{
	public float x, y, z, rx, ry, rz, sx=1, sy=1, sz=1;
	public float goalX, goalY, goalZ, goalRX, goalRY, goalRZ, goalSX=1, goalSY=1, goalSZ=1;
	public float cameraRotationSpeed=1, cameraMoveSpeed=1, cameraScaleSpeed=1;
	public final Frustum frustum = new Frustum();
	private final Vec3f direction = new Vec3f();
	private final Vec3f position = new Vec3f();
	public Camera(float fov, float aspect, float near, float far, boolean ortho){
		if(ortho)MatrixUtils.setupOrtho(fov, aspect, near, far);
		else MatrixUtils.setupPerspective(fov, aspect, near, far);
	}
	public void update(double delta){
		x=(float)((x*(1f-delta*cameraMoveSpeed))+(goalX*delta*cameraMoveSpeed));
		y=(float)((y*(1f-delta*cameraMoveSpeed))+(goalY*delta*cameraMoveSpeed));
		z=(float)((z*(1f-delta*cameraMoveSpeed))+(goalZ*delta*cameraMoveSpeed));
		rx=(float)((((((goalRX-rx)%360)+540)%360)-180)*delta*cameraRotationSpeed)+rx;
		ry=(float)((((((goalRY-ry)%360)+540)%360)-180)*delta*cameraRotationSpeed)+ry;
		rz=(float)((((((goalRZ-rz)%360)+540)%360)-180)*delta*cameraRotationSpeed)+rz;
		sx=(float)((sx*(1f-delta*cameraScaleSpeed))+(goalSX*delta*cameraScaleSpeed));
		sy=(float)((sy*(1f-delta*cameraScaleSpeed))+(goalSY*delta*cameraScaleSpeed));
		sz=(float)((sz*(1f-delta*cameraScaleSpeed))+(goalSZ*delta*cameraScaleSpeed));
		translateInvertMatrix();
		frustum.calculateFrustum();
	}
	private void translateInvertMatrix(){
		GL11.glRotatef(rx, 1, 0, 0);
		GL11.glRotatef(ry, 0, 1, 0);
		GL11.glRotatef(rz, 0, 0, 1);
		GL11.glTranslatef(-x, -y, -z);
		GL11.glScalef(sx, sy, sz);
	}
	public Vec3f getDirection(){
		direction.x=(float)(Math.cos(Math.toRadians(ry-90))*Math.cos(Math.toRadians(-rx)));
		direction.y=(float)Math.sin(Math.toRadians(-rx));
		direction.z=(float)(Math.sin(Math.toRadians(ry-90))*Math.cos(Math.toRadians(-rx)));
		return direction;
	}
	public Vec3f getPosition(){
		position.x=x;
		position.y=y;
		position.z=z;
		return position;
	}
	public Vec3f getGoalDirection(){
		direction.x=(float)(Math.cos(Math.toRadians(goalRY-90))*Math.cos(Math.toRadians(-goalRX)));
		direction.y=(float)Math.sin(Math.toRadians(-goalRX));
		direction.z=(float)(Math.sin(Math.toRadians(goalRY-90))*Math.cos(Math.toRadians(-goalRX)));
		return direction;
	}
	public Vec3f getGoalPosition(){
		position.x=goalX;
		position.y=goalY;
		position.z=goalZ;
		return position;
	}
}