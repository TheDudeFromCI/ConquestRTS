package wraithaven.conquest.client.GameWorld.LoopControls;

import java.nio.FloatBuffer;

public class Vector3f extends Vector{
	public static Vector3f add(Vector3f left, Vector3f right, Vector3f dest){
		if(dest==null) return new Vector3f(left.x+right.x, left.y+right.y, left.z+right.z);
		dest.set(left.x+right.x, left.y+right.y, left.z+right.z);
		return dest;
	}
	public static float angle(Vector3f a, Vector3f b){
		float dls = dot(a, b)/(a.length()*b.length());
		if(dls<-1f) dls = -1f;
		else if(dls>1.0f) dls = 1.0f;
		return (float)Math.acos(dls);
	}
	public static Vector3f cross(Vector3f left, Vector3f right, Vector3f dest){
		if(dest==null) dest = new Vector3f();
		dest.set(left.y*right.z-left.z*right.y, right.x*left.z-right.z*left.x, left.x*right.y-left.y*right.x);
		return dest;
	}
	public static float dot(Vector3f left, Vector3f right){
		return left.x*right.x+left.y*right.y+left.z*right.z;
	}
	public static Vector3f sub(Vector3f left, Vector3f right, Vector3f dest){
		if(dest==null) return new Vector3f(left.x-right.x, left.y-right.y, left.z-right.z);
		dest.set(left.x-right.x, left.y-right.y, left.z-right.z);
		return dest;
	}
	public float x,
			y,
			z;
	public Vector3f(){
		super();
	}
	public Vector3f(float x, float y, float z){
		set(x, y, z);
	}
	@Override public boolean equals(Object obj){
		if(this==obj) return true;
		if(obj==null) return false;
		if(getClass()!=obj.getClass()) return false;
		Vector3f other = (Vector3f)obj;
		if(x==other.x&&y==other.y&&z==other.z) return true;
		return false;
	}
	public final float getX(){
		return x;
	}
	public final float getY(){
		return y;
	}
	public float getZ(){
		return z;
	}
	@Override public float lengthSquared(){
		return x*x+y*y+z*z;
	}
	@Override public Vector load(FloatBuffer buf){
		x = buf.get();
		y = buf.get();
		z = buf.get();
		return this;
	}
	@Override public Vector negate(){
		x = -x;
		y = -y;
		z = -z;
		return this;
	}
	public Vector3f negate(Vector3f dest){
		if(dest==null) dest = new Vector3f();
		dest.x = -x;
		dest.y = -y;
		dest.z = -z;
		return dest;
	}
	public Vector3f normalise(Vector3f dest){
		float l = length();
		if(dest==null) dest = new Vector3f(x/l, y/l, z/l);
		else dest.set(x/l, y/l, z/l);
		return dest;
	}
	@Override public Vector scale(float scale){
		x *= scale;
		y *= scale;
		z *= scale;
		return this;
	}
	public void set(float x, float y){
		this.x = x;
		this.y = y;
	}
	public void set(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public final void setX(float x){
		this.x = x;
	}
	public final void setY(float y){
		this.y = y;
	}
	public void setZ(float z){
		this.z = z;
	}
	@Override public Vector store(FloatBuffer buf){
		buf.put(x);
		buf.put(y);
		buf.put(z);
		return this;
	}
	@Override public String toString(){
		StringBuilder sb = new StringBuilder(64);
		sb.append("Vector3f[");
		sb.append(x);
		sb.append(", ");
		sb.append(y);
		sb.append(", ");
		sb.append(z);
		sb.append(']');
		return sb.toString();
	}
	public Vector3f translate(float x, float y, float z){
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}
}