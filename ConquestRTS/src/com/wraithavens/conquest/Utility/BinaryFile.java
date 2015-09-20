package com.wraithavens.conquest.Utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class BinaryFile{
	private static byte[] read(File file){
		synchronized(HDD_LOCK){
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
	}
	private static void write(File file, byte[] binary){
		synchronized(HDD_LOCK){
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
	}
	private static final Object HDD_LOCK = 0;
	private final static byte[] CompressionBuffer = new byte[3*1024*1024]; // 3
	// Mb.
	private byte[] binary;
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
	public void addInt(int n){
		binary[pos] = (byte)(n&0xFF);
		binary[pos+1] = (byte)(n>>8&0xFF);
		binary[pos+2] = (byte)(n>>16&0xFF);
		binary[pos+3] = (byte)(n>>24&0xFF);
		pos += 4;
	}
	public void addShort(short n){
		binary[pos] = (byte)(n&0xFF);
		binary[pos+1] = (byte)(n>>8&0xFF);
		pos += 2;
	}
	public void allocateBytes(int bytes){
		binary = Arrays.copyOf(binary, binary.length+bytes);
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
	public void compress(boolean writeBufSize){
		synchronized(CompressionBuffer){
			compress(CompressionBuffer, writeBufSize);
		}
	}
	public void compress(byte[] buffer, boolean writeBufSize){
		Deflater deflater = new Deflater();
		deflater.setInput(binary);
		deflater.finish();
		int size = deflater.deflate(buffer);
		pos = 0;
		if(writeBufSize){
			int originalSize = binary.length;
			binary = Arrays.copyOf(binary, size+4);
			addInt(originalSize);
			addBytes(buffer, 0, size);
		}else{
			binary = Arrays.copyOf(binary, size);
			addBytes(buffer, 0, size);
		}
	}
	public void decompress(boolean readBuffSize){
		synchronized(CompressionBuffer){
			decompress(CompressionBuffer, readBuffSize);
		}
	}
	public void decompress(byte[] buffer, boolean readBuffSize){
		try{
			Inflater inflater = new Inflater();
			if(readBuffSize){
				inflater.setInput(binary, 4, binary.length-4);
				pos = 0;
				int size = getInt();
				buffer = new byte[size];
			}else
				inflater.setInput(binary, 0, binary.length);
			int size = inflater.inflate(buffer);
			inflater.end();
			binary = Arrays.copyOf(buffer, size);
			pos = 0;
		}catch(DataFormatException e){
			e.printStackTrace();
		}
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
	public int getInt(){
		int i = binary[pos]&0xFF|(binary[pos+1]&0xFF)<<8|(binary[pos+2]&0xFF)<<16|(binary[pos+3]&0xFF)<<24;
		pos += 4;
		return i;
	}
	public short getShort(){
		short i = (short)(binary[pos]&0xFF|(binary[pos+1]&0xFF)<<8);
		pos += 2;
		return i;
	}
	public int size(){
		return binary.length;
	}
	private void addBytes(byte[] bytes, int offset, int length){
		for(int i = offset; i<offset+length; i++)
			binary[pos+i-offset] = bytes[i];
		pos += length;
	}
}
