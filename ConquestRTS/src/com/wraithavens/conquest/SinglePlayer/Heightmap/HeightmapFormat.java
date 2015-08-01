package com.wraithavens.conquest.SinglePlayer.Heightmap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

// ---
// TODO Make this file format much, much, much smaller. It's way to freaking
// large. It takes way too much precious time to save and load.
// ---
public class HeightmapFormat{
	private static int byteArrayToInteger(byte[] b){
		return b[3]&0xFF|(b[2]&0xFF)<<8|(b[1]&0xFF)<<16|(b[0]&0xFF)<<24;
	}
	private static void integerToByteArray(int a, byte[] buf){
		buf[0] = (byte)(a>>24&0xFF);
		buf[1] = (byte)(a>>16&0xFF);
		buf[2] = (byte)(a>>8&0xFF);
		buf[3] = (byte)(a&0xFF);
	}
	private boolean reading;
	private boolean writing;
	private final File file;
	private BufferedInputStream in;
	private BufferedOutputStream out;
	private final byte[] temp = new byte[4];
	public HeightmapFormat(File file){
		this.file = file;
		if(!file.exists()){
			file.getParentFile().mkdirs();
			try{
				file.createNewFile();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	public void beginReading(){
		if(writing)
			throw new IllegalStateException("Still writing file!");
		if(reading)
			throw new IllegalStateException("Already reading file!");
		reading = true;
		try{
			in = new BufferedInputStream(new FileInputStream(file));
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}
	public void beginWriting(){
		if(writing)
			throw new IllegalStateException("Already writing file!");
		if(reading)
			throw new IllegalStateException("Still reading file!");
		writing = true;
		try{
			out = new BufferedOutputStream(new FileOutputStream(file));
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}
	public boolean isReading(){
		return reading;
	}
	public boolean isWriting(){
		return writing;
	}
	/**
	 * @param out
	 *            - A RGBA array for the next color in the heightmap.
	 */
	public void read(float[] out){
		if(!reading)
			throw new IllegalStateException("Not reading file!");
		out[0] = readFloat();
		out[1] = readFloat();
		out[2] = readFloat();
		out[3] = readFloat();
	}
	public void stopReading(){
		if(!reading)
			throw new IllegalStateException("Not reading file!");
		reading = false;
		try{
			in.close();
			in = null;
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}
	public void stopWriting(){
		if(!writing)
			throw new IllegalStateException("Not writing file!");
		writing = false;
		try{
			out.close();
			out = null;
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}
	/**
	 * @param in
	 *            - A RGBA array for the color of the next pixel in the
	 *            heightmap.
	 */
	public void write(float[] in){
		writeFloat(in[0]);
		writeFloat(in[1]);
		writeFloat(in[2]);
		writeFloat(in[3]);
	}
	public void writeFloat(float f){
		try{
			integerToByteArray(Float.floatToIntBits(f), temp);
			out.write(temp);
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}
	private float readFloat(){
		try{
			in.read(temp);
			return Float.intBitsToFloat(byteArrayToInteger(temp));
		}catch(IOException e){
			e.printStackTrace();
		}
		return 0;
	}
}
