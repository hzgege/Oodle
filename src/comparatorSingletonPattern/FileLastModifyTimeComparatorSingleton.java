package comparatorSingletonPattern;

import java.util.Comparator;

import oodleOperate.MyDir;
import oodleOperate.MyFile;

public class FileLastModifyTimeComparatorSingleton implements Comparator<MyFile> {

	private static boolean flag = true;
	private static FileLastModifyTimeComparatorSingleton singleton = new FileLastModifyTimeComparatorSingleton(); 
	
	private FileLastModifyTimeComparatorSingleton() {
		
	}
	
	public static FileLastModifyTimeComparatorSingleton getSingleton(String sortway){
		if(sortway == "DESC")
		{
			flag = false;
		}else{
			flag = true;
		}
		return singleton;
	}
	
    //"文件修改时间比较"
    @Override
    public int compare(MyFile o1, MyFile o2){
    	Class c1 = o1.getClass();
    	Class c2 = o2.getClass();
    	if(c1 == MyFile.class){
    		if(c2 == MyFile.class){
    			if(o1.getLastModifyTime().compareTo(o2.getLastModifyTime()) < 0)
    				return flag?-1:1;
    	        else if(o1.getLastModifyTime().compareTo(o2.getLastModifyTime()) > 0)
    	        	return flag?1:-1;
    	        else
    	            return 0;
    		}else
    			return flag?1:-1;
    	}else{
    		if(c2 == MyDir.class){
    			if(o1.getLastModifyTime().compareTo(o2.getLastModifyTime()) < 0)
    				return flag?-1:1;
    	        else if(o1.getLastModifyTime().compareTo(o2.getLastModifyTime()) > 0)
    	        	return flag?1:-1;
    	        else
    	            return 0;
    		}else
    			return flag?-1:1;
    	}
        
    }


}