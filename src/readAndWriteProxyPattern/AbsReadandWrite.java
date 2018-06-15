package readAndWriteProxyPattern;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import oodleOperate.MyFile;

public abstract class AbsReadandWrite implements ProxyInterface{

	@Override
	public ConcurrentHashMap<String, List<MyFile>> readObjFromFile(String filePath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeObjToFile(ConcurrentHashMap<String, List<MyFile>> map, String filePath) {
		// TODO Auto-generated method stub
		
	}

}
