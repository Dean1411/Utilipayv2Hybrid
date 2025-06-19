package UtilipayV2Hybrid.utilities;

import java.io.File;

public class FileUtils {
	
	public static String getFilePath(String fileName) {
		
		File file = new File("src/test/resources/" + fileName);
		
		if(file.exists()) {
			
			return file.getAbsolutePath();
		}else {
			
			throw new RuntimeException("File not found: " + file.getAbsolutePath());
		}
	}

}
