package ext2;

import java.io.IOException;

public class TestExt2 {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Ext2FS fs = new Ext2FS("D:/test1.img");
		fs.load();
		fs.listFile();
		System.out.println(fs.getFileContent("/log/select_server.log.2012-12-10"));
	}

}
