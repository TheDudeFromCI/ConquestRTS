package wraithaven.conquest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class MediaSendPacket implements Packet{
	private String data;
	private String fileName;
	private String code;
	public void decode(String s){
		code=s;
		for(int i = 0; i<s.length(); i++){
			if(s.charAt(i)=='¤'){
				fileName=s.substring(0, i);
				data=s.substring(i+1);
				return;
			}
		}
	}
	public void encode(File file){
		fileName=file.getName();
		@SuppressWarnings("resource")Scanner in = null;
		try{
			in=new Scanner(file);
			in.useDelimiter("\\A");
			data=in.next();
		}catch(Exception exception){ exception.printStackTrace(); }
		finally{
			try{ if(in!=null)in.close();
			}catch(Exception exception){ exception.printStackTrace(); }
		}
		code=fileName+"¤"+data;
	}
	public void createFile(File parentFile){
		try{
			if(!parentFile.exists())parentFile.mkdirs();
			File file = new File(parentFile, fileName);
			file.createNewFile();
			@SuppressWarnings("resource")BufferedWriter out = null;
			try{
				out=new BufferedWriter(new FileWriter(file));
				out.write(data);
			}catch(Exception exception){ exception.printStackTrace(); }
			finally{
				try{ if(out!=null)out.close();
				}catch(Exception exception){ exception.printStackTrace(); }
			}
		}catch(Exception exception){ exception.printStackTrace(); }
	}
	public String compress(){ return code; }
	public PacketType getPacketType(){ return PacketType.mediaSend; }
}