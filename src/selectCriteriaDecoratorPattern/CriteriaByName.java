package selectCriteriaDecoratorPattern;

import java.io.File;

import application.MyController;

public class CriteriaByName extends Decorator{

	private SelectCriteria selectCriteria;
	private String[] strName;
	
	public CriteriaByName(SelectCriteria selectCriteria, String[] arr) {
		this.selectCriteria = selectCriteria;
		this.strName = arr;
	}

	@Override
	public boolean selectCriteria(File f){
		
		if(!selectCriteria.selectCriteria(f)){
			return false;
		}
		
		//相关业务
		if(f.getName().indexOf(strName[0]) == -1){
			return false;
		}
		return true;
	}
	

}
