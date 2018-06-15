package readAndWriteProxyPattern;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import oodleOperate.MyFile;

public class WriteToFile extends AbsReadandWrite{

	public void writeObjToFile(ConcurrentHashMap<String, List<MyFile>> map, String filePath) {
		
		try {
			// 写对象流
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath));
			oos.writeObject(map);
			oos.flush();
			oos.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
}
