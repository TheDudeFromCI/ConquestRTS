package com.wraithavens.conquest.Utility;

public class ByteFormatter{
	private final byte[] bytes;
	private int pos;
	public ByteFormatter(byte[] bytes){
		this.bytes = bytes;
	}
	public void addBoolean(boolean b){
		bytes[pos] = (byte)(b?1:0);
		pos++;
	}
	public void addFloat(float f){
		int i = Float.floatToIntBits(f);
		bytes[pos] = (byte)(i&0xFF);
		bytes[pos+1] = (byte)(i>>8&0xFF);
		bytes[pos+2] = (byte)(i>>16&0xFF);
		bytes[pos+3] = (byte)(i>>24&0xFF);
		pos += 4;
	}
	public void addInt(int i){
		bytes[pos] = (byte)(i&0xFF);
		bytes[pos+1] = (byte)(i>>8&0xFF);
		bytes[pos+2] = (byte)(i>>16&0xFF);
		bytes[pos+3] = (byte)(i>>24&0xFF);
		pos += 4;
	}
	public void addShort(short s){
		bytes[pos] = (byte)(s&0xFF);
		bytes[pos+1] = (byte)(s>>8&0xFF);
		pos += 2;
	}
	public boolean getBoolean(){
		return getByte()==1;
	}
	public byte getByte(){
		byte b = bytes[pos];
		pos++;
		return b;
	}
	public float getFloat(){
		return Float.intBitsToFloat(getInt());
	}
	public int getInt(){
		int i = bytes[pos]&0xFF|(bytes[pos+1]&0xFF)<<8|(bytes[pos+2]&0xFF)<<16|(bytes[pos+3]&0xFF)<<24;
		pos += 4;
		return i;
	}
	public short getShort(){
		short i = (short)(bytes[pos]&0xFF|(bytes[pos+1]&0xFF)<<8);
		pos += 2;
		return i;
	}
}
