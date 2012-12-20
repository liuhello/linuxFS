package ext2;

public class Utils {

	public static short toUnsignedByte(byte[] buffer) {
		short result = 0;
		result = (short)(buffer[0]&0xff);
		return result;
	}
	
	public static int toUnsignedShort(byte[] buffer) {
		int result = 0;
		result += (int)((buffer[1]&0xff)<<8);
		result += (int)(buffer[0]&0xff);
		return result;
	}
	
	public static short toShort(byte[] buffer) {
		short result = 0;
		result += (short)((buffer[1]&0xff)<<8);
		result += (short)(buffer[0]&0xff);
		return result;
	}
	
	public static long toUnsignedInt(byte[] buffer) {
		long result = 0;
		result += (long)((buffer[3]&0xff)<<24);
		result += (long)((buffer[2]&0xff)<<16);
		result += (long)((buffer[1]&0xff)<<8);
		result += (long)(buffer[0]&0xff);
		return result;
	}
	
	public static int toInt(byte[] buffer) {
		int result = 0;
		result += (int)((buffer[3]&0xff)<<24);
		result += (int)((buffer[2]&0xff)<<16);
		result += (int)((buffer[1]&0xff)<<8);
		result += (int)(buffer[0]&0xff);
		return result;
	}
	
}
