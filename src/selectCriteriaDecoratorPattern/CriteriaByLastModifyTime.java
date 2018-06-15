package selectCriteriaDecoratorPattern;

import java.io.File;

import oodleOperate.MyFile;

public class CriteriaByLastModifyTime extends Decorator{

	private SelectCriteria selectCriteria;
	private String[] timeArr;
	
	public CriteriaByLastModifyTime(SelectCriteria selectCriteria, String[] arr) {
		this.selectCriteria = selectCriteria;
		this.timeArr = arr;
	}

	@Override
	public boolean selectCriteria(File f){
		
		if(!selectCriteria.selectCriteria(f)){
			return false;
		}
		
		//同前，两个时间怎么获取，注意可以为空
		String timeb = null, timee = null;
		String fTime = MyFile.timeConvertToString(f.lastModified()).substring(0, 10);
		if(timeArr[0] != null)
		{
			timeb = timeArr[0];
		}else{
			timeb = "0000-00-00";
		}
		if(timeArr[1] != null){
			timee = timeArr[1];
		}else{
			timee = fTime;
		}
		if(fTime.compareTo(timeb) >= 0 && fTime.compareTo(timee) <= 0)
			return true;
		return false;
	}
}
