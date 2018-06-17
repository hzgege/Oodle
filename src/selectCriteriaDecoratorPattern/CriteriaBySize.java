package selectCriteriaDecoratorPattern;

import java.io.File;

import oodleOperate.MyFile;
import ui.Controller;

public class CriteriaBySize extends Decorator{

	private SelectCriteria selectCriteria;
	private String[] sizeArr;
	
	public CriteriaBySize(SelectCriteria selectCriteria, String[] arr) {
		this.selectCriteria = selectCriteria;
		this.sizeArr = arr;
	}

	@Override
	public boolean selectCriteria(File f){
		
		if(!selectCriteria.selectCriteria(f)){
			return false;
		}
		
		if(f.isDirectory())
			return true;
		long a = 0, b = 0;
		String temp = MyFile.sizeConvertToString(f.length());
		temp = temp.substring(0, temp.indexOf('K'));
		long fsize = Long.parseLong(temp);
		if(sizeArr[0] != null)
		{
			a = Long.parseLong(sizeArr[0]);
		}else{
			a = -1;
		}
		if(sizeArr[1] != null)
		{
			b = Long.parseLong(sizeArr[1]);
		}else {
			b = fsize + 1;
		}
		
		if(fsize >= a && fsize <= b)
			return true;
		return false;
	}
	
}
