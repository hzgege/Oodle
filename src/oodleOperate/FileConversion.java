package oodleOperate;

import java.io.File;
import java.io.FileNotFoundException;

public class FileConversion {

	public static MyFile getMyFile(File file){

		String path = file.getPath();
		String name = file.getName();
		long size = file.length();
		long time = file.lastModified();
		MyFile myFile = new MyFile(path, name, size, time);
		return myFile;
	}

	public static MyDir getMyDir(File file) {
		String path = file.getPath();
		String name = file.getName();
		long time = file.lastModified();
		MyDir myDir = new MyDir(path, name, time);
		return myDir;
	}
}
