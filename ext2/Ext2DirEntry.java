package ext2;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Ext2DirEntry {
	
	public static final int EXT3_NAME_LEN = 255;
	public static final int INODE_OFF = 0;
	public static final int REC_LEN_OFF = INODE_OFF+4;
	public static final int NAME_LEN_OFF = REC_LEN_OFF+2;
	public static final int FILE_TYPE_OFF = NAME_LEN_OFF+1;
	public static final int NAME_OFF = FILE_TYPE_OFF+1;
	
	public long inode;
	public int rec_len;
	public short name_len;
	public short file_type;
	public byte[] name;
	
	public boolean isValidInode() {
		return inode >= Ext2Inode.EXT2_ROOT_INO;
	}
	
	public static Ext2DirEntry readDirEntry(RandomAccessFile f,int off) throws IOException {
		Ext2DirEntry entry = new Ext2DirEntry();
		entry.inode = Ext2Tools.readUnsignedInt(f, off+INODE_OFF);
		entry.rec_len = Ext2Tools.readUnsignedShort(f, off+REC_LEN_OFF);
		entry.name_len = Ext2Tools.readUnsignedByte(f, off+NAME_LEN_OFF);
		entry.name = new byte[entry.name_len];
		Ext2Tools.readByte(f, off+NAME_OFF, entry.name);
		return entry;
	}
}
