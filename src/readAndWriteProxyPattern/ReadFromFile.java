package readAndWriteProxyPattern;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import oodleOperate.MyFile;

public class ReadFromFile extends AbsReadandWrite{

	@SuppressWarnings("unchecked")
	public ConcurrentHashMap<String, List<MyFile>> readObjFromFile(String filePath) {
		 
        ObjectInputStream ois = null;  
        ConcurrentHashMap<String, List<MyFile>>  fileAll = null;  
		try {
			// 读并赋值给fileAll
			ois = new ObjectInputStream(new FileInputStream(filePath));
			fileAll= (ConcurrentHashMap<String, List<MyFile>>) ois.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			try{
				ois.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}

		return fileAll;
	}

}
