package selectCriteriaDecoratorPattern;

import java.io.File;

import oodleOperate.MyFile;

public class CriteriaBySize extends Decorator{
	
	public CriteriaBySize(SelectCriteria selectCriteria, String[] arr) {
		super(selectCriteria, arr);
	}

	@Override
	public boolean selectCriteria(File f){
		super.selectCriteria(f);
		if(!selectCriteria.selectCriteria(f)){
			return false;
		}
		
		if(f.isDirectory())
			return true;
		long a = 0, b = 0;
		String temp = MyFile.sizeConvertToString(f.length());
		temp = temp.substring(0, temp.indexOf('K'));
		long fsize = Long.parseLong(temp);
		if(Arr[0] != null)
		{
			a = Long.parseLong(Arr[0]);
		}else{
			a = -1;
		}
		if(Arr[1] != null)
		{
			b = Long.parseLong(Arr[1]);
		}else {
			b = fsize + 1;
		}
		
		if(fsize >= a && fsize <= b)
			return true;
		return false;
	}
	
}
