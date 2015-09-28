package com.wraithavens.conquest.Launcher;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Comparator;

class ResolutionSize{
	private static int gcd(int a, int b){
		return b==0?a:ResolutionSize.gcd(b, a%b);
	}
	static ResolutionSize[] generateSizes(){
		ArrayList<ResolutionSize> sizes = new ArrayList();
		ResolutionSize nativeSize = new ResolutionSize(sizes.size());
		sizes.add(nativeSize);
		sizes.add(new ResolutionSize(800, 600, sizes.size()));
		sizes.add(new ResolutionSize(1024, 600, sizes.size()));
		sizes.add(new ResolutionSize(1024, 768, sizes.size()));
		sizes.add(new ResolutionSize(1152, 864, sizes.size()));
		sizes.add(new ResolutionSize(1280, 720, sizes.size()));
		sizes.add(new ResolutionSize(1280, 768, sizes.size()));
		sizes.add(new ResolutionSize(1280, 800, sizes.size()));
		sizes.add(new ResolutionSize(1280, 960, sizes.size()));
		sizes.add(new ResolutionSize(1280, 1024, sizes.size()));
		sizes.add(new ResolutionSize(1360, 768, sizes.size()));
		sizes.add(new ResolutionSize(1366, 768, sizes.size()));
		sizes.add(new ResolutionSize(1400, 1050, sizes.size()));
		sizes.add(new ResolutionSize(1440, 900, sizes.size()));
		sizes.add(new ResolutionSize(1600, 900, sizes.size()));
		sizes.add(new ResolutionSize(1600, 1200, sizes.size()));
		sizes.add(new ResolutionSize(1680, 1050, sizes.size()));
		sizes.add(new ResolutionSize(1920, 1080, sizes.size()));
		sizes.add(new ResolutionSize(1920, 1200, sizes.size()));
		sizes.add(new ResolutionSize(2048, 1152, sizes.size()));
		sizes.add(new ResolutionSize(2560, 1440, sizes.size()));
		sizes.add(new ResolutionSize(2560, 1600, sizes.size()));
		sizes.add(new ResolutionSize(768, 1024, sizes.size()));
		sizes.add(new ResolutionSize(1093, 614, sizes.size()));
		sizes.add(new ResolutionSize(1536, 864, sizes.size()));
		sizes.add(new ResolutionSize(640, 480, sizes.size()));
		sizes.add(new ResolutionSize(160, 120, sizes.size()));
		sizes.sort(new Comparator<ResolutionSize>(){
			public int compare(ResolutionSize b, ResolutionSize a){
				if(a.real)
					return 1;
				if(b.real)
					return -1;
				double dis1 = Math.abs(a.ratio-nativeSize.ratio);
				double dis2 = Math.abs(b.ratio-nativeSize.ratio);
				if(dis1==dis2)
					return a.pixels==b.pixels?0:a.pixels<b.pixels?1:-1;
				return dis1<dis2?1:-1;
			}
		});
		return sizes.toArray(new ResolutionSize[sizes.size()]);
	}
	final int width;
	final int height;
	private final boolean real;
	private final double ratio;
	private final long pixels;
	private final int ratioNum;
	private final int ratioDen;
	public final int id;
	private ResolutionSize(int id){
		this.id = id;
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		width = dimension.width;
		height = dimension.height;
		real = true;
		ratio = (double)width/height;
		pixels = width*height;
		int gcd = ResolutionSize.gcd(width, height);
		ratioNum = width/gcd;
		ratioDen = height/gcd;
	}
	private ResolutionSize(int width, int height, int id){
		this.id = id;
		this.width = width;
		this.height = height;
		real = false;
		ratio = (double)width/height;
		pixels = width*height;
		int gcd = ResolutionSize.gcd(width, height);
		ratioNum = width/gcd;
		ratioDen = height/gcd;
	}
	public boolean isNative(){
		return real;
	}
	@Override
	public String toString(){
		if(real)
			return "Native ("+width+" x "+height+"    "+ratioNum+":"+ratioDen+")";
		StringBuilder sb = new StringBuilder();
		sb.append(width);
		if(sb.length()==3)
			sb.insert(0, ' ');
		sb.append(" x ");
		sb.append(height);
		int numDigits = String.valueOf(ratioNum).length();
		while(sb.length()<23-numDigits)
			sb.append(' ');
		sb.append(ratioNum);
		sb.append(':');
		sb.append(ratioDen);
		return sb.toString();
	}
}
