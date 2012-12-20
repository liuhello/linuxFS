package ext2;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Ext2Inode {
	
	public static final int S_IFMT = 00170000;
	public static final int S_IFSOCK = 0140000;
	public static final int S_IFLNK  = 0120000;
	public static final int S_IFREG = 0100000;
	public static final int S_IFBLK = 0060000;
	public static final int S_IFDIR = 0040000;
	public static final int S_IFCHR = 0020000;
	public static final int S_IFIFO = 0010000;
	public static final int S_ISUID = 0004000;
	public static final int S_ISGID = 0002000;
	public static final int S_ISVTX = 0001000;
	
	
	public static final int EXT2_ROOT_INO = 2;
	//public static final int EXT2_INODE_SIZE = 128*2;
	
	public static final int EXT3_N_BLOCKS = 15;
	
	public static final int MODE_OFF = 0;
	public static final int UID_OFF = 2;
	public static final int SIZE_OFF = 4;
	public static final int ACCESS_TIME_OFF = 8;
	public static final int CREATE_TIME_OFF = 12;
	public static final int MODIFY_TIME_OFF = 16;
	public static final int DELETION_TIME_OFF = 20;
	public static final int GID_OFF = 24;
	public static final int LINKS_COUNT_OFF = 26;
	public static final int BLOCKS_OFF = 28;
	public static final int FLAGS_OFF = 32;
	public static final int RESERVED1_OFF = 36;
	public static final int BLOCK_OFF = 40;
	public static final int GENERATION_OFF = 40+4*EXT3_N_BLOCKS;
	public static final int FILE_ACL_OFF = GENERATION_OFF+4;
	public static final int DIR_ACL_OFF = FILE_ACL_OFF+4;
	public static final int FADDR_OFF = DIR_ACL_OFF+4;
	public static final int FRAG_OFF = FADDR_OFF+4;
	public static final int FSIZE_OFF = FRAG_OFF+1;
	public static final int PAD1_OFF = FSIZE_OFF+1;
	public static final int UID_HIGH_OFF = PAD1_OFF+2;
	public static final int GID_HIGH_OFF = UID_HIGH_OFF+2;
	public static final int RESERVED_OFF = GID_HIGH_OFF+2;
	
	public int i_mode;
	public int i_uid;
	public long i_size;
	public long i_atime;
	public long i_ctime;
	public long i_mtime;
	public long i_dtime;
	public int i_gid;
	public int i_links_count;
	public long i_blocks;
	public long i_flags;
	public long l_i_reserved1;
	public long[] i_block = new long[EXT3_N_BLOCKS];
	public long i_generation;
	public long i_file_acl;
	public long i_dir_acl;
	public long i_faddr;
	public short l_i_frag;
	public short l_i_fsize;
	public int i_pad1;
	public int l_i_uid_high;
	public int l_i_gid_high;
	public long l_i_reserved2;
	
	
	public boolean isDir() {
		return (i_mode&S_IFMT) == S_IFDIR;
	}
	public boolean isRegularFile() {
		return (i_mode&S_IFMT) == S_IFREG;
	}
	
	public int getFileOff(int off,Ext2SuperBlock sb) {
		if(off > i_size) {
			return -1;
		}
		int block = off/sb.getBlockSize();
		int innerOff = off%sb.getBlockSize();
		if(block > 12) {
			return -1;
		}
		return (int)(i_block[block]*sb.getBlockSize()+innerOff);
	}
	
	public int getFileSize() {
		return (int)i_size;
	}
	
	public int getRemain(int off,Ext2SuperBlock sb) {
		return sb.getBlockSize() - off%sb.getBlockSize();
	}
	
	public static Ext2Inode readInode(RandomAccessFile f,int off) throws IOException {
		Ext2Inode inode = new Ext2Inode();
		inode.i_mode = Ext2Tools.readUnsignedShort(f, off+MODE_OFF);
		inode.i_uid = Ext2Tools.readUnsignedShort(f, off+UID_OFF);
		inode.i_size = Ext2Tools.readUnsignedInt(f, off+SIZE_OFF);
		inode.i_atime = Ext2Tools.readUnsignedInt(f, off+ACCESS_TIME_OFF);
		inode.i_ctime = Ext2Tools.readUnsignedInt(f, off+CREATE_TIME_OFF);
		inode.i_mtime = Ext2Tools.readUnsignedInt(f, off+MODIFY_TIME_OFF);
		inode.i_dtime = Ext2Tools.readUnsignedInt(f, off+DELETION_TIME_OFF);
		inode.i_gid = Ext2Tools.readUnsignedShort(f, off+GID_OFF);
		inode.i_links_count = Ext2Tools.readUnsignedShort(f, off+LINKS_COUNT_OFF);
		inode.i_blocks = Ext2Tools.readUnsignedInt(f, off+BLOCKS_OFF);
		inode.i_flags = Ext2Tools.readUnsignedInt(f, off+FLAGS_OFF);
		inode.l_i_reserved1 = Ext2Tools.readUnsignedInt(f, off+RESERVED1_OFF);
		for(int i = 0;i < EXT3_N_BLOCKS;i++) {
			inode.i_block[i] = Ext2Tools.readUnsignedInt(f, off+BLOCK_OFF+i*4);
		}
		inode.i_generation = Ext2Tools.readUnsignedInt(f, off+GENERATION_OFF);
		inode.i_file_acl = Ext2Tools.readUnsignedInt(f, off+FILE_ACL_OFF);
		inode.i_dir_acl = Ext2Tools.readUnsignedInt(f, off+DIR_ACL_OFF);
		inode.i_faddr = Ext2Tools.readUnsignedInt(f, off+FRAG_OFF);
		inode.l_i_frag = Ext2Tools.readUnsignedByte(f, off+FRAG_OFF);
		inode.l_i_fsize = Ext2Tools.readUnsignedByte(f, off+FSIZE_OFF);
		
		inode.i_pad1 = Ext2Tools.readUnsignedShort(f, off+PAD1_OFF);
		inode.l_i_uid_high = Ext2Tools.readUnsignedShort(f, off+UID_HIGH_OFF);
		inode.l_i_gid_high = Ext2Tools.readUnsignedShort(f, off+GID_HIGH_OFF);
		inode.l_i_reserved2 = Ext2Tools.readUnsignedInt(f, off+RESERVED_OFF);
		return inode;
	}
	
}
