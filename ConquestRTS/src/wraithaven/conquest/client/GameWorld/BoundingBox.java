package wraithaven.conquest.client.GameWorld;

import com.sun.javafx.geom.Vec3f;

public class BoundingBox{
	public float x1,
			y1,
			z1;
	public float x2,
			y2,
			z2;
	public Vec3f getMax(){
		Vec3f v = new Vec3f();
		v.x = Math.max(x1, x2);
		v.y = Math.max(y1, y2);
		v.z = Math.max(z1, z2);
		return v;
	}
	public Vec3f getMin(){
		Vec3f v = new Vec3f();
		v.x = Math.min(x1, x2);
		v.y = Math.min(y1, y2);
		v.z = Math.min(z1, z2);
		return v;
	}
}