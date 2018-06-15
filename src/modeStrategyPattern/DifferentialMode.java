package modeStrategyPattern;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import oodleOperate.MyDir;
import oodleOperate.MyFile;
import readAndWriteProxyPattern.ReadandWriteProxy;
import templateMethodPattern.TraversalFile;

public class DifferentialMode implements Mode{

	private ConcurrentHashMap<String, List<MyFile>> NowMap = null;
	private ConcurrentHashMap<String, List<MyFile>> OldMap = null;
	private static HashSet<String> newFile = new HashSet<>();
	private static HashSet<String> deleteFile = new HashSet<>();
	private static HashSet<MyFile> FileNewInfo = new HashSet<>();
	private static HashSet<MyFile> FileOldInfo = new HashSet<>();
	
	//比较 显示差异
	//findDeleted寻找删除
	private void findDeleted(ConcurrentHashMap<String, List<MyFile>> present, ConcurrentHashMap<String, List<MyFile>> last) {
		List<MyFile> NowMapFile = null;
		List<MyFile> OldMapFile = null;
		for(String key:last.keySet()){
			OldMapFile = last.get(key);
			NowMapFile = present.get(key);
			if(NowMapFile == null){
				//Old里面找不到，说明是新建的目录
				deleteFile.add(key);
				for(MyFile md : OldMapFile){
					deleteFile.add(md.getPath());
				}
			}else{
				//对两个list遍历，找出新建的
				//重写了equals 只对名字进行判断
				for(MyFile mf : OldMapFile){
					if(!NowMapFile.contains(mf)){
						deleteFile.add(mf.getPath());
					}
				}
			}
		}
	}

	//findNew寻找新建，新的有，旧的没有
	private void findNew(ConcurrentHashMap<String, List<MyFile>> present, ConcurrentHashMap<String, List<MyFile>> last){
		List<MyFile> NowMapFile = null;
		List<MyFile> OldMapFile = null;
		for(String key:present.keySet()){
			NowMapFile = present.get(key);
			OldMapFile = last.get(key);
			if(OldMapFile == null){
				newFile.add(key);
				for(MyFile md : NowMapFile){
					newFile.add(md.getPath());
				}
				
			}else{
				//对两个list遍历，找出新建的
				//重写了equals 只对名字进行判断
				for(MyFile mf : NowMapFile){
					if(!OldMapFile.contains(mf)){
						newFile.add(mf.getPath());
					}
				}
			}
		}
	}
	
	//findChanged寻找修改
	private void findChanged(ConcurrentHashMap<String, List<MyFile>> present, ConcurrentHashMap<String, List<MyFile>> last) {
		List<MyFile> NowMapFile = null;
		List<MyFile> OldMapFile = null;
		for(String key:present.keySet()){
			NowMapFile = present.get(key);
			OldMapFile = last.get(key);
			if(OldMapFile != null){
				
				Map<String, MyFile> NowListToMap = new HashMap<>();
				Map<String, MyFile> OldListToMap = new HashMap<>();
				
				//list转map
				for(int i = 0; i < NowMapFile.size(); i++){
					MyFile f = NowMapFile.get(i);
					NowListToMap.put(f.getPath(), f);
				}		
				for(int i = 0; i < OldMapFile.size(); i++){
					MyFile f = OldMapFile.get(i);
					OldListToMap.put(f.getPath(), f);
				}
				for(String Nkey : NowListToMap.keySet()){
					MyFile timeNew = NowListToMap.get(Nkey);
					MyFile timeOld = OldListToMap.get(Nkey);
					if(timeOld != null && 
								timeNew.getLastModifyTime().compareTo(timeOld.getLastModifyTime()) != 0){
						FileNewInfo.add(timeNew);
						FileOldInfo.add(timeOld);
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String doMode(String path) {
		clearAll();
		TraversalFile TF = new TraversalFile();
		//递归遍历
		try {
			NowMap = (ConcurrentHashMap<String, List<MyFile>>) TF.getResult(path, new ConcurrentHashMap<String, List<MyFile>>());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//判断文件是否存在
		String newFilePath = ".\\SaveFileContext\\"+setFileName(path);
		File tempFile = new File(newFilePath);
		if(!tempFile.exists()){
			return "该文件目录暂无日志信息";
		}
		//从日志文件中读取
		ReadandWriteProxy RWP = new ReadandWriteProxy();
		OldMap = RWP.readObjFromFile(newFilePath);
		
		if(NowMap == null)
			return "当前目录不可读";
		
		//寻找新建的目录
		findNew(NowMap, OldMap);
		//寻找删除
		findDeleted(NowMap, OldMap);
		//寻找修改
		findChanged(NowMap, OldMap);
		
		return "差异模式成功";
	}
	
	@Override
	public String setFileName(String path) {
		String stmp = path.replaceAll("\\\\", "_");
		String sp = stmp.replaceAll(":", "")+ ".txt";
		return sp;
	}

	private void clearAll(){
		newFile.clear();
		deleteFile.clear();
		FileNewInfo.clear();
		FileOldInfo.clear();
	}
	
	public static HashSet<String> getNewFile() {
		return newFile;
	}

	public static HashSet<String> getDeleteFile() {
		return deleteFile;
	}

	public static HashSet<MyFile> getFileNewInfo() {
		return FileNewInfo;
	}

	public static HashSet<MyFile> getFileOldInfo() {
		return FileOldInfo;
	}
}
