package wraithaven.conquest.client.GameWorld.LoopControls;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import com.sun.javafx.geom.Vec3f;

public class MatrixUtils{
	private static final float[] FORWARD = new float[3];
	private static final float[] IDENTITY_MATRIX = {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1};
	private static final FloatBuffer matrix = BufferUtils.createFloatBuffer(16);
	private static final float[] SIDE = new float[3];
	private static final float[] UP = new float[3];
	public static void cross(float[] v1, float[] v2, float[] result){
		result[0] = v1[1]*v2[2]-v1[2]*v2[1];
		result[1] = v1[2]*v2[0]-v1[0]*v2[2];
		result[2] = v1[0]*v2[1]-v1[1]*v2[0];
	}
	public static void lookAt(float eyex, float eyey, float eyez, float centerx, float centery, float centerz, float upx, float upy, float upz){
		float[] forward = MatrixUtils.FORWARD;
		float[] side = MatrixUtils.SIDE;
		float[] up = MatrixUtils.UP;
		forward[0] = centerx-eyex;
		forward[1] = centery-eyey;
		forward[2] = centerz-eyez;
		up[0] = upx;
		up[1] = upy;
		up[2] = upz;
		MatrixUtils.normalize(forward);
		MatrixUtils.cross(forward, up, side);
		MatrixUtils.normalize(side);
		MatrixUtils.cross(side, forward, up);
		MatrixUtils.makeIdentity(MatrixUtils.matrix);
		MatrixUtils.matrix.put(0, side[0]);
		MatrixUtils.matrix.put(4, side[1]);
		MatrixUtils.matrix.put(8, side[2]);
		MatrixUtils.matrix.put(1, up[0]);
		MatrixUtils.matrix.put(5, up[1]);
		MatrixUtils.matrix.put(9, up[2]);
		MatrixUtils.matrix.put(2, -forward[0]);
		MatrixUtils.matrix.put(6, -forward[1]);
		MatrixUtils.matrix.put(10, -forward[2]);
		GL11.glMultMatrix(MatrixUtils.matrix);
		GL11.glTranslatef(-eyex, -eyey, -eyez);
	}
	private static void makeIdentity(FloatBuffer m){
		int oldPos = m.position();
		m.put(MatrixUtils.IDENTITY_MATRIX);
		m.position(oldPos);
	}
	public static void matrixLookAt(Vec3f eye, Vec3f center, Vec3f up, float[] result){
		Vec3f f = new Vec3f(center);
		f.sub(eye);
		f.normalize();
		Vec3f u = new Vec3f(up);
		u.normalize();
		Vec3f s = new Vec3f();
		s.cross(f, u);
		s.normalize();
		u.cross(s, f);
		for(int i = 0; i<16; i++)
			result[i] = 1;
		result[0] = s.x;
		result[4] = s.y;
		result[8] = s.z;
		result[1] = u.x;
		result[5] = u.y;
		result[9] = u.z;
		result[2] = -f.x;
		result[6] = -f.y;
		result[10] = -f.z;
		result[3] = -eye.x;
		result[7] = -eye.y;
		result[11] = -eye.z;
	}
	public static float[] normalize(float[] v){
		float r;
		r = (float)Math.sqrt(v[0]*v[0]+v[1]*v[1]+v[2]*v[2]);
		if(r==0) return v;
		r = 1/r;
		v[0] *= r;
		v[1] *= r;
		v[2] *= r;
		return v;
	}
	public static void setupImageOrtho(float scaleX, float scaleY, float near, float far){
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, scaleX, 0, scaleY, near, far);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	public static void setupOrtho(float scaleX, float scaleY, float near, float far){
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(scaleX/-2, scaleX/2, scaleY/-2, scaleY/2, near, far);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	public static void setupPerspective(float fov, float aspect, float near, float far){
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		float sine, cotangent, deltaZ;
		float radians = fov/2*(float)Math.PI/180;
		deltaZ = far-near;
		sine = (float)Math.sin(radians);
		if(deltaZ==0||sine==0||aspect==0) return;
		cotangent = (float)Math.cos(radians)/sine;
		MatrixUtils.makeIdentity(MatrixUtils.matrix);
		MatrixUtils.matrix.put(0, cotangent/aspect);
		MatrixUtils.matrix.put(5, cotangent);
		MatrixUtils.matrix.put(10, -(far+near)/deltaZ);
		MatrixUtils.matrix.put(11, -1);
		MatrixUtils.matrix.put(14, -2*near*far/deltaZ);
		MatrixUtils.matrix.put(15, 0);
		GL11.glMultMatrix(MatrixUtils.matrix);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	public static void takeScreenShot(File file, int displayWidth, int displayHeight){
		try{
			file.getParentFile().mkdirs();
			file.createNewFile();
			GL11.glReadBuffer(GL11.GL_FRONT);
			ByteBuffer buffer = BufferUtils.createByteBuffer(displayWidth*displayHeight*4);
			GL11.glReadPixels(0, 0, displayWidth, displayHeight, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
			BufferedImage image = new BufferedImage(displayWidth, displayHeight, BufferedImage.TYPE_INT_RGB);
			int x, y, i, r, g, b;
			for(x = 0; x<displayWidth; x++){
				for(y = 0; y<displayHeight; y++){
					i = (x+(displayWidth*y))*4;
					r = buffer.get(i)&0xFF;
					g = buffer.get(i+1)&0xFF;
					b = buffer.get(i+2)&0xFF;
					image.setRGB(x, displayHeight-(y+1), (0xFF<<24)|(r<<16)|(g<<8)|b);
				}
			}
			ImageIO.write(image, "PNG", file);
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}
}