package ext2;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Ext2GroupDesc {

	public static final int BLOCK_BITMAP_OFF = 0;
	public static final int INODE_BITMAP_OFF = 4;
	public static final int INODE_TABLE_OFF = 8;
	public static final int FREE_BLOCKS_COUNT_OFF = 12;
	public static final int FREE_INODES_COUNT_OFF = 14;
	public static final int USED_DIRS_OFF = 16;
	public static final int PAD_OFF = 18;
	public static final int RESERVED_OFF = 20;
	public static final int GROUP_DESC_SIZE = 32;
	
	public long bg_block_bitmap;
	public long bg_inode_bitmap;
	public long bg_inode_table;
	public int bg_free_blocks_count;
	public int bg_free_inodes_count;
	public int bg_used_dirs_count;
	public int bg_pad;
	public byte[] bg_reserved = new byte[3*4];
	
	public byte[] block_bitmap;
	public byte[] inode_bitmap;
	
	
	public void loadBitmap(RandomAccessFile f,Ext2SuperBlock sb) throws IOException {
		block_bitmap = new byte[sb.getBlockSize()];
		inode_bitmap = new byte[sb.getBlockSize()];
		Ext2Tools.readByte(f, bg_block_bitmap*sb.getBlockSize(), block_bitmap);
		Ext2Tools.readByte(f, bg_inode_bitmap*sb.getBlockSize(), inode_bitmap);
	}
	public Ext2Inode readInode(int ino,Ext2SuperBlock sb,RandomAccessFile f) throws IOException{
		int off = (int)(bg_inode_table*sb.getBlockSize() + ino*sb.getInodeSize());
		return Ext2Inode.readInode(f, off);
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Group Desc Info : \n");
		sb.append("Block bitemap : "+bg_block_bitmap+"\n");
		sb.append("Inode bitemap : "+bg_inode_bitmap+"\n");
		sb.append("Inode table : "+bg_inode_table+"\n");
		sb.append("Free blocks count : "+bg_free_blocks_count+"\n");
		sb.append("Free inodes count : "+bg_free_inodes_count+"\n");
		sb.append("Used dirs count : "+bg_used_dirs_count+"\n");
		sb.append("Pad : "+bg_pad+"\n");
		return sb.toString();
	}
	
	public static Ext2GroupDesc readGroupDesc(RandomAccessFile f,int off) throws IOException {
		Ext2GroupDesc gd = new Ext2GroupDesc();
		gd.bg_block_bitmap = Ext2Tools.readUnsignedInt(f, off+BLOCK_BITMAP_OFF);
		gd.bg_inode_bitmap = Ext2Tools.readUnsignedInt(f, off+INODE_BITMAP_OFF);
		gd.bg_inode_table = Ext2Tools.readUnsignedInt(f, off+INODE_TABLE_OFF);
		gd.bg_free_blocks_count = Ext2Tools.readUnsignedShort(f, off+FREE_BLOCKS_COUNT_OFF);
		gd.bg_free_inodes_count = Ext2Tools.readUnsignedShort(f, off+FREE_INODES_COUNT_OFF);
		gd.bg_used_dirs_count = Ext2Tools.readUnsignedShort(f, off+USED_DIRS_OFF);
		gd.bg_pad = Ext2Tools.readUnsignedShort(f, off+PAD_OFF);
		Ext2Tools.readByte(f, off+RESERVED_OFF, gd.bg_reserved);
		return gd;
	}
}
