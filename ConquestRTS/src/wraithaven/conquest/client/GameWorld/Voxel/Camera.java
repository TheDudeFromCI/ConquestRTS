package wraithaven.conquest.client.GameWorld.Voxel;

import org.lwjgl.opengl.GL11;
import com.sun.javafx.geom.Vec3f;
import wraithaven.conquest.client.GameWorld.LoopControls.MatrixUtils;

public class Camera{
	public float cameraMoveSpeed = 1;
	private final Vec3f direction = new Vec3f();
	public final Frustum frustum = new Frustum();
	public float goalX,
			goalY,
			goalZ;
	private final Vec3f position = new Vec3f();
	public float x,
			y,
			z,
			rx,
			ry,
			rz,
			sx = 1,
			sy = 1,
			sz = 1;
	public Camera(){}
	public Camera(float fov, float aspect, float near, float far, boolean ortho){
		if(ortho) MatrixUtils.setupOrtho(fov, aspect, near, far);
		else MatrixUtils.setupPerspective(fov, aspect, near, far);
	}
	public Vec3f getDirection(){
		direction.x = (float)(Math.cos(Math.toRadians(ry-90))*Math.cos(Math.toRadians(-rx)));
		direction.y = (float)Math.sin(Math.toRadians(-rx));
		direction.z = (float)(Math.sin(Math.toRadians(ry-90))*Math.cos(Math.toRadians(-rx)));
		return direction;
	}
	public Vec3f getGoalPosition(){
		position.x = goalX;
		position.y = goalY;
		position.z = goalZ;
		return position;
	}
	public Vec3f getPosition(){
		position.x = x;
		position.y = y;
		position.z = z;
		return position;
	}
	private void translateInvertMatrix(){
		GL11.glRotatef(rx, 1, 0, 0);
		GL11.glRotatef(ry, 0, 1, 0);
		GL11.glRotatef(rz, 0, 0, 1);
		GL11.glTranslatef(-x, -y, -z);
		GL11.glScalef(sx, sy, sz);
	}
	public void update(double delta){
		x = (float)((x*(1f-delta*cameraMoveSpeed))+(goalX*delta*cameraMoveSpeed));
		y = (float)((y*(1f-delta*cameraMoveSpeed))+(goalY*delta*cameraMoveSpeed));
		z = (float)((z*(1f-delta*cameraMoveSpeed))+(goalZ*delta*cameraMoveSpeed));
		translateInvertMatrix();
		frustum.calculateFrustum();
	}
}