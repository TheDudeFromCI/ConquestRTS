package com.wraithavens.conquest.Utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

class BinaryFileUtil{
	static byte[] readFile(File file){
		BufferedInputStream in = null;
		try{
			in=new BufferedInputStream(new FileInputStream(file));
			byte[] d = new byte[in.available()];
			in.read(d);
			return d;
		}catch(Exception exception){ exception.printStackTrace(); }
		finally{
			if(in!=null){
				try{ in.close();
				}catch(Exception exception){ exception.printStackTrace(); }
			}
		}
		return null;
	}
	static void writeFile(File file, byte[]... data){
		if(data==null)return;
		BufferedOutputStream out = null;
		try{
			out=new BufferedOutputStream(new FileOutputStream(file));
			for(byte[] d : data)out.write(d);
		}catch(Exception exception){ exception.printStackTrace(); }
		finally{
			if(out!=null){
				try{ out.close();
				}catch(Exception exception){ exception.printStackTrace(); }
			}
		}
	}
	static float byteArrayToFloat(byte[] b){ return ByteBuffer.wrap(b).getFloat(); }
	static byte[] floatToByteArray(float f){ return ByteBuffer.allocate(4).putFloat(f).array(); }
	public static int byteArrayToInteger(byte[] b){ return b[3]&0xFF|(b[2]&0xFF)<<8|(b[1]&0xFF)<<16|(b[0]&0xFF)<<24; }

}