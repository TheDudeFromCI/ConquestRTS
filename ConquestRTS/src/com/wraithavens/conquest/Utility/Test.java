package com.wraithavens.conquest.Utility;

public class Test{
	public static void main(String[] args){
		final float secondsPassed = 10;
		final float percentDone = 0.5f;
		float timeLeft = 1/percentDone*secondsPassed-secondsPassed;
		System.out.println(timeLeft);
	}
}
