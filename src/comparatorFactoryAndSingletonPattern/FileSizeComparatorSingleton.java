package comparatorFactoryAndSingletonPattern;

import java.util.Comparator;

import oodleOperate.MyDir;
import oodleOperate.MyFile;

public class FileSizeComparatorSingleton implements Comparator<MyFile> {

	private boolean flag = true;
	private static FileSizeComparatorSingleton singleton = new FileSizeComparatorSingleton();
	
	private FileSizeComparatorSingleton() {
		
	}
	
	public static FileSizeComparatorSingleton getSingleton(String sortway){
		if(sortway == "DESC")
		{
			singleton.flag = false;
		}
		return singleton;
	}
	
    //"文件大小比较"
    @Override
    public int compare(MyFile o1, MyFile o2) {
    	Class c1 = o1.getClass();
    	Class c2 = o2.getClass();
    	if(c1 == MyFile.class){
    		if(c2 == MyFile.class){
    			if(o1.getSizeLong() < o2.getSizeLong())
    	            return flag?-1:1;
    	        else if(o1.getSizeLong() > o2.getSizeLong())
    	            return flag?1:-1;
    	        else
    	            return 0;
    		}else
    			return flag?1:-1;
    	}else{
    		if(c2 == MyDir.class){
    			return flag?o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase())
    					:o2.getName().toLowerCase().compareTo(o1.getName().toLowerCase());
    		}else
    			return flag?-1:1;
    	}
    }
}