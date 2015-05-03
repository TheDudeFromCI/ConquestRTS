package wraithaven.conquest.client.GameWorld;

import com.sun.javafx.geom.Vec3f;

public class Sphere{
	public float x, y, z, r;
	public Vec3f getCenter(){
		Vec3f v = new Vec3f();
		v.x=x;
		v.y=y;
		v.z=z;
		return v;
	}
}