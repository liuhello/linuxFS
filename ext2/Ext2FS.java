package ext2;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class Ext2FS {
	
	protected RandomAccessFile m_raf;
	protected String m_file;
	protected Ext2SuperBlock m_superBlock;
	protected Ext2GroupDesc[] m_groupdDescs;
	protected Map<Long, Ext2Inode> m_inodeCache = new HashMap<Long, Ext2Inode>();
	protected Ext2Inode m_rootInode;
	
	public Ext2FS(String file) {
		m_file = file;
	}
	
	public void load() throws IOException {
		m_raf = new RandomAccessFile(m_file, "r");
		m_superBlock = Ext2SuperBlock.readSuperBlock(m_raf);
		m_groupdDescs = new Ext2GroupDesc[m_superBlock.getBlockGroups()];
		int off = m_superBlock.getBlockSize();
		if(off == 1024) {
			off = 2048;
		}
		for(int i = 0;i < m_groupdDescs.length;i++) {
			m_groupdDescs[i] = Ext2GroupDesc.readGroupDesc(m_raf, off+i*Ext2GroupDesc.GROUP_DESC_SIZE);
			m_groupdDescs[i].loadBitmap(m_raf, m_superBlock);
		}
		m_rootInode = getInode(Ext2Inode.EXT2_ROOT_INO);
	}
	
	public Ext2Inode getInode(long ino) throws IOException {
		Ext2Inode inode = m_inodeCache.get(ino);
		if(inode != null) {
			return inode;
		}
		int group = m_superBlock.getGroup(ino);
		int innerIno = m_superBlock.getGroupInode(ino);
		if(group >= m_groupdDescs.length) {
			return null;
		}
		inode = m_groupdDescs[group].readInode(innerIno, m_superBlock, m_raf);
		if(inode != null) {
			m_inodeCache.put(ino,inode);
		}
		return inode;
	}
	
	public String getFileContent(String path) throws IOException {
		if(path.startsWith("/")) {
			path = path.substring(1);
		}
		String[] str = path.split("/");
		Ext2Inode tmp = m_rootInode;
		for(int i = 0;i < str.length;i++) {
			tmp = getInode(str, i, tmp);
			if(tmp == null) {
				break;
			}
		}
		if(tmp != null && tmp.isRegularFile()) {
			return new String(getInodeData(tmp));
		}
		throw new FileNotFoundException("file not find or is not a regular file!");
	}
	
	private byte[] getInodeData(Ext2Inode inode) throws IOException{
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		int len = 0;
		byte[] buffer = new byte[1024];
		while(len < inode.getFileSize()) {
			int off = inode.getFileOff(len, m_superBlock);
			int remain = inode.getRemain(len, m_superBlock);
			remain = Math.min(remain, inode.getFileSize() - len);
			remain = Math.min(remain,buffer.length);
			int n = Ext2Tools.readByte(m_raf, off, buffer, remain);
			bout.write(buffer, 0, n);
			len += n;
		}
		return bout.toByteArray();
	}
	
	private Ext2Inode getInode(String[] str,int i,Ext2Inode inode)  throws IOException{
		int len = 0;
		while(len < inode.i_size) {
			int off = inode.getFileOff(len, m_superBlock);
			Ext2DirEntry entry = Ext2DirEntry.readDirEntry(m_raf, off);
			len += entry.rec_len;
			if(!entry.isValidInode()) {
				continue;
			}
			Ext2Inode innerInode = getInode(entry.inode);
			String tmp = new String(entry.name);
			if(tmp.equals(str[i])) {
				return innerInode;
			}
		}
		return null;
	}
	
	public void listFile() throws IOException {
		if(m_rootInode.isDir()) {
			listFile(m_rootInode,"");
		}
	}
	
	private void listFile(Ext2Inode inode,String parent) throws IOException {
		if(!inode.isDir()) {
			return;
		}
		int len = 0;
		while(len < inode.i_size) {
			int off = inode.getFileOff(len, m_superBlock);
			Ext2DirEntry entry = Ext2DirEntry.readDirEntry(m_raf, off);
			len += entry.rec_len;
			if(!entry.isValidInode()) {
				continue;
			}
			Ext2Inode innerInode = getInode(entry.inode);
			String tmp = new String(entry.name);
			String file = parent +"/"+ tmp;
			if(!tmp.equals(".") && !tmp.equals("..")) {
				System.out.println(String.format("%s %dK %s", innerInode.isDir()?"D":"F",innerInode.i_size/1024,file));
				listFile(innerInode, file);
			}
		}
		
		
		
	}
	
	
}
