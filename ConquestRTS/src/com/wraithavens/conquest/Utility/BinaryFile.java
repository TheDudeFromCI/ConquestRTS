package com.wraithavens.conquest.Utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class BinaryFile{
	private static byte[] read(File file){
		BufferedInputStream in = null;
		try{
			in = new BufferedInputStream(new FileInputStream(file));
			byte[] d = new byte[in.available()];
			in.read(d);
			return d;
		}catch(Exception exception){
			exception.printStackTrace();
		}finally{
			if(in!=null)
				try{
					in.close();
				}catch(Exception exception){
					exception.printStackTrace();
				}
		}
		return null;
	}
	private static void write(File file, byte[] binary){
		BufferedOutputStream out = null;
		try{
			out = new BufferedOutputStream(new FileOutputStream(file));
			out.write(binary);
		}catch(Exception exception){
			exception.printStackTrace();
		}finally{
			if(out!=null)
				try{
					out.close();
				}catch(Exception exception){
					exception.printStackTrace();
				}
		}
	}
	private final byte[] binary;
	private int pos;
	/**
	 * Reads all binary data from a file into a byte array.
	 */
	public BinaryFile(File file){
		binary = read(file);
	}
	/**
	 * Creates an empty byte array of the desired size.
	 */
	public BinaryFile(int space){
		binary = new byte[space];
	}
	public void addBoolean(boolean val){
		addByte((byte)(val?1:0));
	}
	public void addByte(byte n){
		binary[pos] = n;
		pos++;
	}
	public void addFloat(float n){
		addInt(Float.floatToIntBits(n));
	}
	public void compile(File file){
		if(!file.exists()){
			try{
				file.getParentFile().mkdirs();
				file.createNewFile();
			}catch(Exception exception){
				exception.printStackTrace();
				return;
			}
		}
		write(file, binary);
	}
	public byte[] getBinary(){
		return binary;
	}
	public boolean getBoolean(){
		return getByte()==1;
	}
	public byte getByte(){
		byte b = binary[pos];
		pos++;
		return b;
	}
	public float getFloat(){
		return Float.intBitsToFloat(getInt());
	}
	public int size(){
		return binary.length;
	}
	private void addInt(int n){
		binary[pos] = (byte)(n&0xFF);
		binary[pos+1] = (byte)(n>>8&0xFF);
		binary[pos+2] = (byte)(n>>16&0xFF);
		binary[pos+3] = (byte)(n>>24&0xFF);
		pos += 4;
	}
	private int getInt(){
		int i = binary[pos]&0xFF|(binary[pos+1]&0xFF)<<8|(binary[pos+2]&0xFF)<<16|(binary[pos+3]&0xFF)<<24;
		pos += 4;
		return i;
	}
}
