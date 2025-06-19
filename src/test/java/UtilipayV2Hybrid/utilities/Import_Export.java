package UtilipayV2Hybrid.utilities;

import java.io.File;

public class Import_Export {
	
	public static String getFilePath(String fileName) {
		
		File file = new File("src/test/resources/ImportFiles/" + fileName);
		
		if(file.exists()) {
			
			return file.getAbsolutePath();
		}else {
			
			throw new RuntimeException("File not found: " + file.getAbsolutePath());
		}
	}

}
