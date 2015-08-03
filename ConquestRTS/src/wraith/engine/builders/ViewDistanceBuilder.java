package wraith.engine.builders;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import com.wraithavens.conquest.SinglePlayer.Blocks.ViewDistances;
import com.wraithavens.conquest.Utility.BinaryFile;

public class ViewDistanceBuilder{
	public static void main(String[] args){
		int x, y, z;
		ArrayList<double[]> cells = new ArrayList();
		String folder = System.getProperty("user.dir")+File.separatorChar+"Data"+File.separatorChar+"ChunkLoad";
		for(ViewDistances view : ViewDistances.values()){
			System.out.println("Processing view distance: "+view.value);
			System.out.println("  Loading cells.");
			cells.clear();
			for(x = -view.value; x<view.value; x++)
				for(y = -view.value; y<view.value; y++)
					for(z = -view.value; z<view.value; z++)
						cells.add(new double[]{
							x, y, z, getDistance(x, y, z)
						});
			System.out.println("  Sorting cells.");
			cells.sort(new Comparator<double[]>(){
				public int compare(double[] a, double[] b){
					return a[3]==b[3]?0:a[3]>b[3]?1:-1;
				}
			});
			System.out.println("  Saving cells.");
			BinaryFile bin = new BinaryFile(cells.size()*3);
			for(double[] c : cells){
				bin.addByte((byte)c[0]);
				bin.addByte((byte)c[1]);
				bin.addByte((byte)c[2]);
			}
			bin.compile(new File(folder, view.value+".dat"));
		}
		System.out.println("Finished.");
	}
	private static double getDistance(int x, int y, int z){
		return x*x+y*y/2+z*z;
	}
}
