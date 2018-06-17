package readAndWriteProxyPattern;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import oodleOperate.MyFile;

public class ReadandWriteProxy implements ProxyInterface{
	
	private String path;
	private AbsReadandWrite writeToFile = new WriteToFile();
	private AbsReadandWrite readFromFile = new ReadFromFile();
	
	public ReadandWriteProxy() {

	}

	private void DirExists() {
		path = ".\\SaveFileContext";
		File Folder = new File(path);
		if(!Folder.exists()){
			Folder.mkdirs();
		}
	}

	@Override
	public ConcurrentHashMap<String, List<MyFile>> readObjFromFile(String filePath) {
		DirExists();
		ConcurrentHashMap<String, List<MyFile>> temp = readFromFile.readObjFromFile(filePath);
		return temp;
	}

	@Override
	public void writeObjToFile(ConcurrentHashMap<String, List<MyFile>> map, String filePath) {
		DirExists();
		writeToFile.writeObjToFile(map, filePath);
	}

}
