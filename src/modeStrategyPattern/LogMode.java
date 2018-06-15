package modeStrategyPattern;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import oodleOperate.MyFile;
import readAndWriteProxyPattern.ReadandWriteProxy;
import templateMethodPattern.TraversalFile;

public class LogMode implements Mode{

	@SuppressWarnings("unchecked")
	@Override
	public String doMode(String path) {
		
		ConcurrentHashMap<String, List<MyFile>> LogMap = null;
		TraversalFile TF = new TraversalFile();
		//递归遍历
		try {
			LogMap = (ConcurrentHashMap<String, List<MyFile>>) TF.getResult(path, new ConcurrentHashMap<String, List<MyFile>>());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//存储
		ReadandWriteProxy RSH = new ReadandWriteProxy();
		String newFilePath = ".\\SaveFileContext\\"+setFileName(path);
		RSH.writeObjToFile(LogMap, newFilePath);
		return "日志模式存储成功";
	}
	
	@Override
	public String setFileName(String path) {
		String stmp = path.replaceAll("\\\\", "_");
		String sp = stmp.replaceAll(":", "")+ ".txt";
		return sp;
	}
}
