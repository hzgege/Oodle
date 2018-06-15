package comparatorFactoryPattern;

import java.util.Comparator;

import oodleOperate.MyDir;
import oodleOperate.MyFile;

public class FileNameComparator implements Comparator<MyFile>{
	
	private boolean flag = true;
	public FileNameComparator(String upOrDown) {
		// TODO Auto-generated constructor stub
		if(upOrDown == "DESC")
		{
			flag = false;
		}
	}
	
	//"文件名比较"
    @Override
    public int compare(MyFile o1, MyFile o2) {
    	Class c1 = o1.getClass();
    	Class c2 = o2.getClass();
    	if(c1 == MyFile.class){
    		if(c2 == MyFile.class){
    			return flag?o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase())
    					:o2.getName().toLowerCase().compareTo(o1.getName().toLowerCase());
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
