package wraithaven.conquest.client.GameWorld.TileSystem;

public class BilinearInterpolation{
	public static double cosineInterpolation(double p1, double p2, double p3, double p4, double x, double y){
		return cosineInterpolation(cosineInterpolation(p1, p2, x), cosineInterpolation(p3, p4, x), y);
	}
	public static double cosineInterpolation(double a, double b, double frac){
		frac = (1-Math.cos(frac*Math.PI))/2;
		return (a*(1-frac)+b*frac);
	}
	public static double linearInterpolation(double p1, double p2, double p3, double p4, double x, double y){
		return linearInterpolation(linearInterpolation(p1, p2, x), linearInterpolation(p3, p4, x), y);
	}
	public static double linearInterpolation(double a, double b, double frac){
		return (1-frac)*a+frac*b;
	}
}