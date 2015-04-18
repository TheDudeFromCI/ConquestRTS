package wraithaven.conquest.client;

public class MapHeightScaler{
	private final float scale;
	private final float shift;
	public MapHeightScaler(float level){
		if(level==1){
			scale=1;
			shift=0;
		}else if(level>1){
			shift=level-1;
			scale=1-shift;
		}else{
			shift=0;
			scale=level;
		}
	}
	public float scale(float noise){ return noise*scale+shift; }
}