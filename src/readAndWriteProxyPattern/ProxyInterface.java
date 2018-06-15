package readAndWriteProxyPattern;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import oodleOperate.MyFile;

public interface ProxyInterface {

	public ConcurrentHashMap<String, List<MyFile>> readObjFromFile(String filePath);
	
	public void writeObjToFile(ConcurrentHashMap<String, List<MyFile>> map, String filePath);
}
