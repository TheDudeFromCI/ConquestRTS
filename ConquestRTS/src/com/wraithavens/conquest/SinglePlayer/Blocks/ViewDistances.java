package com.wraithavens.conquest.SinglePlayer.Blocks;

public enum ViewDistances{
	View_5(5),
	View_8(8),
	View_10(10),
	View_12(12),
	View_15(15),
	View_18(18),
	View_20(20),
	View_25(25),
	View_30(30),
	View_40(40),
	View_50(50);
	public final int value;
	private ViewDistances(int value){
		this.value = value;
	}
}
