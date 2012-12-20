package ext2;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Ext2Tools {

	public static final int BLOCK_SIZE = 1024;
	public static final int INODE_COUNT_OFF = 0;
	public static final int BLOCK_COUNT_OFF = 4;
	public static final int R_BLOCK_COUNT_OFF = 8;
	public static final int FREE_BLOCK_COUNT_OFF = 12;
	public static final int FREE_INODE_COUNT_OFF = 16;
	public static final int FIRST_DATA_BLOCK_OFF = 20;
	public static final int LOG_BLOCK_SZIE_OFF = 24;
	public static final int LOG_FRAG_SIZE_OFF = 28;
	public static final int BLOCK_PER_GROUP_OFF = 32;
	public static final int FRAGS_PER_GROUP_OFF = 36;
	public static final int INODES_PER_GROUP_OFF = 40;
	public static final int MOUNT_TIME_OFF = 44;
	public static final int WRITE_TIME_OFF = 48;
	public static final int MOUNT_COUNT_OFF = 52;
	public static final int MAX_MOUNT_COUNT_OFF = 54;
	public static final int MAGIC_OFF = 56;
	public static final int STATE_OFF = 58;
	public static final int ERRORS_OFF = 60;
	public static final int MIRROR_REV_LEVEL_OFF = 62;
	public static final int LAST_CHECK_OFF = 64;
	public static final int CHECK_INTERVAL_OFF = 68;
	public static final int CREATOR_OS_OFF = 72;
	public static final int REV_LEVEL_OFF = 76;
	public static final int DEF_RES_UID_OFF = 80;
	public static final int DEF_RES_GID_OFF = 82;
	public static final int FIRST_INO_OFF = 84;
	public static final int IONDE_SIZE_OFF = 88;
	public static final int BLOCK_GROUP_NR_OFF = 90;
	public static final int FEATURE_COMPAT_OFF = 92;
	public static final int FEATURE_INCOMPAT_OFF = 96;
	public static final int FEATURE_RO_COMPAT_OFF = 100;
	public static final int UUID_OFF = 104;
	public static final int VOLUM_NAME_OFF = 120;
	public static final int LAST_MOUNTED_OFF = 136;
	public static final int ALGORITHM_USAGE_BITMAP_OFF = 200;
	public static final int PREALLOC_BLOCKS_OFF = 204;
	public static final int PREALLOC_DIR_BLOCKS_OFF = 205;
	public static final int PADDING_1_OFF = 206;
	public static final int JOURNAL_UUID_OFF = 208;
	public static final int JOURNAL_NUM_OFF = 224;
	public static final int JOURNAL_DEV_OFF = 228;
	public static final int LAST_ORPHAN_OFF = 232;
	public static final int RESERVED_OFF = 236;
	public static final int VALID_EXT2_MAGIC = 0xef53;
	
	
	public static int readMagic(RandomAccessFile f) throws IOException {
		byte[] buffer = new byte[2];
		f.seek(BLOCK_SIZE+MAGIC_OFF);
		f.read(buffer);
		return Utils.toUnsignedShort(buffer);
	}
	
	public static boolean isValidExt2(RandomAccessFile f) throws IOException {
		int magic = readMagic(f);
		return magic == VALID_EXT2_MAGIC;
	}
	public static long readInodeCount(RandomAccessFile f) throws IOException {
		byte[] buffer = new byte[4];
		f.seek(BLOCK_SIZE + INODE_COUNT_OFF);
		f.read(buffer);
		return Utils.toUnsignedInt(buffer);
	}
	public static long readBlockCount(RandomAccessFile f) throws IOException {
		byte[] buffer = new byte[4];
		f.seek(BLOCK_SIZE + BLOCK_COUNT_OFF);
		f.read(buffer);
		return Utils.toUnsignedInt(buffer);
	}
	
	public static long readRemainBlockCount(RandomAccessFile f) throws IOException {
		byte[] buffer = new byte[4];
		f.seek(BLOCK_SIZE + R_BLOCK_COUNT_OFF);
		f.read(buffer);
		return Utils.toUnsignedInt(buffer);
	}
	public static long readFreeBlockCount(RandomAccessFile f) throws IOException {
		byte[] buffer = new byte[4];
		f.seek(BLOCK_SIZE + FREE_BLOCK_COUNT_OFF);
		f.read(buffer);
		return Utils.toUnsignedInt(buffer);
	}
	public static long readUnsignedInt(RandomAccessFile f,int off) throws IOException {
		byte[] buffer = new byte[4];
		f.seek(off);
		f.read(buffer);
		return Utils.toUnsignedInt(buffer);
	}
	public static void readByte(RandomAccessFile f,long off,byte[] buffer) throws IOException {
		f.seek(off);
		f.read(buffer);
	}
	
	public static int readByte(RandomAccessFile f,long off,byte[] buffer,int len) throws IOException {
		f.seek(off);
		return f.read(buffer, 0, len);
	}
	
	public static int readInt(RandomAccessFile f,int off) throws IOException {
		byte[] buffer = new byte[4];
		f.seek(off);
		f.read(buffer);
		return Utils.toInt(buffer);
	}
	public static int readUnsignedShort(RandomAccessFile f,int off) throws IOException {
		byte[] buffer = new byte[2];
		f.seek(off);
		f.read(buffer);
		return Utils.toUnsignedShort(buffer);
	}
	public static short readShort(RandomAccessFile f,int off) throws IOException {
		byte[] buffer = new byte[2];
		f.seek(off);
		f.read(buffer);
		return Utils.toShort(buffer);
	}
	public static short readUnsignedByte(RandomAccessFile f,int off) throws IOException {
		byte[] buffer = new byte[1];
		f.seek(off);
		f.read(buffer);
		return Utils.toUnsignedByte(buffer);
	}
}
