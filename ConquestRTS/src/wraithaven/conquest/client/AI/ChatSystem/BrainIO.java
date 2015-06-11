package wraithaven.conquest.client.AI.ChatSystem;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class BrainIO{
	public static InfoBit createInfoBitFromFile(File file){
		InfoBit info = new InfoBit();
		byte[] bytes = readFile(file);
		int index = 0;
		while(index<bytes.length){
			Tag tag = new Tag();
			int nameLength = bytes[index]+128;
			index++;
			tag.importance = bytes[index];
			index++;
			tag.title = new String(subByteArray(bytes, index, nameLength));
			index += nameLength;
			info.tags.add(tag);
		}
		return info;
	}
	public static void save(InfoBit info, File file){
		ArrayList<byte[]> bytes = new ArrayList();
		byte[] name;
		for(Tag tag : info.tags){
			name = tag.title.getBytes();
			bytes.add(new byte[]{(byte)(name.length-128), tag.importance});
			bytes.add(name);
		}
		writeFile(file, bytes.toArray(new byte[bytes.size()][]));
	}
	private static byte[] readFile(File file){
		BufferedInputStream in = null;
		try{
			in=new BufferedInputStream(new FileInputStream(file));
			byte[] d = new byte[in.available()];
			in.read(d);
			return d;
		}catch(Exception exception){
			exception.printStackTrace();
		}finally{
			if(in!=null){
				try{ in.close();
				}catch(Exception exception){
					exception.printStackTrace();
				}
			}
		}
		return null;
	}
	private static byte[] subByteArray(byte[] bytes, int start, int length){
		byte[] b = new byte[length];
		for(int i = 0; i<length; i++)
			b[i] = bytes[start+i];
		return b;
	}
	private static void writeFile(File file, byte[]... data){
		if(data==null)return;
		BufferedOutputStream out = null;
		try{
			out=new BufferedOutputStream(new FileOutputStream(file));
			for(byte[] d : data)out.write(d);
		}catch(Exception exception){
			exception.printStackTrace();
		}finally{
			if(out!=null){
				try{ out.close();
				}catch(Exception exception){
					exception.printStackTrace();
				}
			}
		}
	}
}