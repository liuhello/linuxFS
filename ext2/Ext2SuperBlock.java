package ext2;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Ext2SuperBlock {
	public long m_inode_count;
	public long m_block_count;
	public long m_r_block_count;
	public long m_free_block_count;
	public long m_free_inode_count;
	public long m_first_data_block;
	public long m_log_block_size;
	public int m_log_frag_size;
	public long m_blocks_per_group;
	public long m_frags_per_group;
	public long m_inodes_per_group;
	public long m_mtime;
	public long m_wtime;
	public int m_mnt_count;
	public short m_max_mnt_count;
	public int m_magic;
	public int m_state;
	public int m_errors;
	public int m_minor_rev_level;
	public long m_lastcheck;
	public long m_checkinterval;
	public long m_creator_os;
	public long m_rev_level;
	public int m_def_resuid;
	public int m_def_resgid;
	public long m_first_ino;
	public int m_inode_size;
	public int m_block_group_nr;
	public long m_feature_compat;
	public long m_feature_incompat;
	public long m_feature_ro_compat;
	public byte[] m_uuid = new byte[16];
	public byte[] m_volume_name = new byte[16];
	public byte[] m_last_mounted = new byte[64];
	public long m_algorithm_usage_bitmap;
	public short m_prealloc_blocks;
	public short m_prealloc_dir_blocks;
	public int m_padding1;
	public byte[] m_journal_uuid = new byte[16];
	public long m_journal_inum;
	public long m_journal_dev;
	public long m_last_orphan;
	public byte[] m_reserved = new byte[197*4];
	
	
	public int getBlockSize() {
		return 1<<(10+m_log_block_size);
	}
	public int getBlockGroups() {
		return (int)((m_block_count - m_first_data_block -1)/m_blocks_per_group + 1);
	}
	public int getGroup(long ino) {
		return (int)((ino-1)/m_inodes_per_group);
	}
	public int getGroupInode(long ino) {
		return (int)((ino-1)%m_inodes_per_group);
	}
	public int getDescPerBlocks() {
		return getBlockSize()/Ext2GroupDesc.GROUP_DESC_SIZE;
	}
	public int getInodeSize() {
		return m_inode_size;
	}
	
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer sb = new StringBuffer();
		sb.append("Ext2 Super block Info :\n");
		sb.append("Inode Count : "+m_inode_count+"\n");
		sb.append("Block Count : "+m_block_count+"\n");
		sb.append("Remain Block Count : "+m_r_block_count+"\n");
		sb.append("Free Block Count : "+m_free_block_count+"\n");
		sb.append("Free Inode Count : "+m_free_inode_count+"\n");
		sb.append("First Data Block : "+m_first_data_block+"\n");
		sb.append("Log Block Size : "+m_log_block_size+"\n");
		sb.append("Block Size : "+getBlockSize()+"\n");
		sb.append("Log Frag Size : "+m_log_frag_size+"\n");
		sb.append("Blocks Per Group : "+m_blocks_per_group+"\n");
		sb.append("Frags Per Group : "+m_frags_per_group+"\n");
		sb.append("Inodes Per Group : "+m_inodes_per_group+"\n");
		sb.append("Mount Time : "+sdf.format(new Date(m_mtime*1000))+"\n");
		sb.append("Write Time : "+sdf.format(new Date(m_wtime*1000))+"\n");
		sb.append("Mount Count : "+m_mnt_count+"\n");
		sb.append("Max Mount Count : "+m_max_mnt_count+"\n");
		sb.append(String.format("Magic : %x\n", m_magic));
		sb.append("State : "+m_state+"\n");
		sb.append("Errors : "+m_errors+"\n");
		sb.append("Minor Revision Level : "+m_minor_rev_level+"\n");
		sb.append("Last Check Time : "+sdf.format(new Date(m_lastcheck*1000))+"\n");
		sb.append("Check Interval : "+m_checkinterval+"\n");
		sb.append("Creator OS : "+m_creator_os+"\n");
		sb.append("Revision Level : "+m_rev_level+"\n");
		sb.append("Default uid for reserved blocks : "+m_def_resuid+"\n");
		sb.append("Default gid for reserved blocks : "+m_def_resgid+"\n");
		sb.append("First non-reserved inode : "+m_first_ino+"\n");
		sb.append("Size of inode structure : "+m_inode_size+"\n");
		sb.append("Block group of this superblock : "+m_block_group_nr+"\n");
		sb.append("Compatible feature set : "+m_feature_compat+"\n");
		sb.append("Incompatible feature set : "+m_feature_incompat+"\n");
		sb.append("Readonly-compatible feature set : "+m_feature_ro_compat+"\n");
		
		sb.append("Volume name : "+new String(m_volume_name)+"\n");
		sb.append("Directory where last mounted : "+new String(m_last_mounted)+"\n");
		sb.append("Algorithm usage bitmap : "+m_algorithm_usage_bitmap+"\n");
		sb.append("Prealloc blocks : "+m_prealloc_blocks+"\n");
		sb.append("Prealloc dir blocks : "+m_prealloc_dir_blocks+"\n");
		sb.append("Padding 1 : "+m_padding1+"\n");
		sb.append("日志文件的 inode号数 : "+m_journal_inum+"\n");
		sb.append("日志文件的设备号 : "+m_journal_dev+"\n");
		sb.append("Start of list of inodes to delete : "+m_last_orphan+"\n");
		sb.append("Block groups : "+getBlockGroups()+"\n");
		sb.append("Desc per block : "+getDescPerBlocks()+"\n");
		return sb.toString();
	}
	
	
	public static Ext2SuperBlock readSuperBlock(RandomAccessFile f) throws IOException {
		Ext2SuperBlock sb = new Ext2SuperBlock();
		sb.m_inode_count = Ext2Tools.readInodeCount(f);
		sb.m_block_count = Ext2Tools.readBlockCount(f);
		sb.m_r_block_count = Ext2Tools.readRemainBlockCount(f);
		sb.m_free_block_count = Ext2Tools.readFreeBlockCount(f);
		sb.m_free_inode_count = Ext2Tools.readUnsignedInt(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.FREE_INODE_COUNT_OFF);
		sb.m_first_data_block = Ext2Tools.readUnsignedInt(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.FIRST_DATA_BLOCK_OFF);
		sb.m_log_block_size = Ext2Tools.readUnsignedInt(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.LOG_BLOCK_SZIE_OFF);
		sb.m_log_frag_size = Ext2Tools.readInt(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.LOG_FRAG_SIZE_OFF);
		sb.m_blocks_per_group = Ext2Tools.readUnsignedInt(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.BLOCK_PER_GROUP_OFF);
		sb.m_frags_per_group = Ext2Tools.readUnsignedInt(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.FRAGS_PER_GROUP_OFF);
		sb.m_inodes_per_group = Ext2Tools.readUnsignedInt(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.INODES_PER_GROUP_OFF);
		sb.m_mtime = Ext2Tools.readUnsignedInt(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.MOUNT_TIME_OFF);
		sb.m_wtime = Ext2Tools.readUnsignedInt(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.WRITE_TIME_OFF);
		sb.m_mnt_count = Ext2Tools.readUnsignedShort(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.MOUNT_COUNT_OFF);
		sb.m_max_mnt_count = Ext2Tools.readShort(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.MAX_MOUNT_COUNT_OFF);
		sb.m_magic = Ext2Tools.readUnsignedShort(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.MAGIC_OFF);
		sb.m_state = Ext2Tools.readUnsignedShort(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.STATE_OFF);
		sb.m_errors = Ext2Tools.readUnsignedShort(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.ERRORS_OFF);
		sb.m_minor_rev_level = Ext2Tools.readUnsignedShort(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.MIRROR_REV_LEVEL_OFF);
		sb.m_lastcheck = Ext2Tools.readUnsignedInt(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.LAST_CHECK_OFF);
		sb.m_checkinterval = Ext2Tools.readUnsignedInt(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.CHECK_INTERVAL_OFF);
		sb.m_creator_os = Ext2Tools.readUnsignedInt(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.CREATOR_OS_OFF);
		sb.m_rev_level = Ext2Tools.readUnsignedInt(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.REV_LEVEL_OFF);
		sb.m_def_resuid = Ext2Tools.readUnsignedShort(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.DEF_RES_UID_OFF);
		sb.m_def_resgid = Ext2Tools.readUnsignedShort(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.DEF_RES_GID_OFF);
		sb.m_first_ino = Ext2Tools.readUnsignedInt(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.FIRST_INO_OFF);
		sb.m_inode_size = Ext2Tools.readUnsignedShort(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.IONDE_SIZE_OFF);
		sb.m_block_group_nr = Ext2Tools.readUnsignedShort(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.BLOCK_GROUP_NR_OFF);
		sb.m_feature_compat = Ext2Tools.readUnsignedInt(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.FEATURE_COMPAT_OFF);
		sb.m_feature_incompat = Ext2Tools.readUnsignedInt(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.FEATURE_INCOMPAT_OFF);
		sb.m_feature_ro_compat = Ext2Tools.readUnsignedInt(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.FEATURE_RO_COMPAT_OFF);
		Ext2Tools.readByte(f,Ext2Tools.BLOCK_SIZE+Ext2Tools.UUID_OFF,sb.m_uuid);
		Ext2Tools.readByte(f,Ext2Tools.BLOCK_SIZE+Ext2Tools.VOLUM_NAME_OFF,sb.m_volume_name);
		Ext2Tools.readByte(f,Ext2Tools.BLOCK_SIZE+Ext2Tools.LAST_MOUNTED_OFF,sb.m_last_mounted);
		sb.m_algorithm_usage_bitmap = Ext2Tools.readUnsignedInt(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.ALGORITHM_USAGE_BITMAP_OFF);
		sb.m_prealloc_blocks = Ext2Tools.readUnsignedByte(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.PREALLOC_BLOCKS_OFF);
		sb.m_prealloc_dir_blocks = Ext2Tools.readUnsignedByte(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.PREALLOC_DIR_BLOCKS_OFF);
		sb.m_padding1 = Ext2Tools.readUnsignedShort(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.PADDING_1_OFF);
		Ext2Tools.readByte(f,Ext2Tools.BLOCK_SIZE+Ext2Tools.JOURNAL_UUID_OFF,sb.m_journal_uuid);
		sb.m_journal_inum = Ext2Tools.readUnsignedInt(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.JOURNAL_NUM_OFF);
		sb.m_journal_dev = Ext2Tools.readUnsignedInt(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.JOURNAL_DEV_OFF);
		sb.m_last_orphan = Ext2Tools.readUnsignedInt(f, Ext2Tools.BLOCK_SIZE+Ext2Tools.LAST_ORPHAN_OFF);
		Ext2Tools.readByte(f,Ext2Tools.BLOCK_SIZE+Ext2Tools.RESERVED_OFF,sb.m_reserved);
		return sb;
	}
	
}
