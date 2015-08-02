package com.wraithavens.conquest.Utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

/**
 * A class designed to offer several basic functions that are almost required to
 * binary file read and writing.
 *
 * @author TheDudeFromCI
 */
public class BinaryFileUtil{
	/**
	 * Reads a file, and returns all bytes from that file.
	 *
	 * @param file
	 *            - The file to read.
	 * @return A byte array containing every byte from the file.
	 */
	public static byte[] readFile(File file){
		BufferedInputStream in = null;
		try{
			in = new BufferedInputStream(new FileInputStream(file));
			byte[] d = new byte[in.available()];
			in.read(d);
			return d;
		}catch(Exception exception){
			exception.printStackTrace();
		}finally{
			if(in!=null){
				try{
					in.close();
				}catch(Exception exception){
					exception.printStackTrace();
				}
			}
		}
		return null;
	}
	/**
	 * Converts a byte array to a float.
	 *
	 * @param b
	 *            - The byte array. Must be a length of at least 4.
	 * @return The float.
	 */
	static float byteArrayToFloat(byte[] b){
		return ByteBuffer.wrap(b).getFloat();
	}
	/**
	 * Converts a float to a byte array with a length of 4.
	 *
	 * @param f
	 *            - The float.
	 * @return The byte array.
	 */
	static byte[] floatToByteArray(float f){
		return ByteBuffer.allocate(4).putFloat(f).array();
	}
	/**
	 * Takes a byte array, or list of byte arrays, and writes them into a file.
	 * This method replaces any current data, with the new data provided by the
	 * byte array. Using a list of byte arrays will combine the bytes arrays
	 * into a single byte array as it writes them.
	 *
	 * @param file
	 *            - The file to write in.
	 * @param data
	 *            - The byte array, or list of byte arrays to write.
	 */
	static void writeFile(File file, byte[]... data){
		if(data==null)
			return;
		BufferedOutputStream out = null;
		try{
			out = new BufferedOutputStream(new FileOutputStream(file));
			for(byte[] d : data)
				out.write(d);
		}catch(Exception exception){
			exception.printStackTrace();
		}finally{
			if(out!=null){
				try{
					out.close();
				}catch(Exception exception){
					exception.printStackTrace();
				}
			}
		}
	}
}
