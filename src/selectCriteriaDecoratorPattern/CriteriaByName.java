package selectCriteriaDecoratorPattern;

import java.io.File;

public class CriteriaByName extends Decorator{

	public CriteriaByName(SelectCriteria selectCriteria, String[] arr) {
		super(selectCriteria, arr);
	}

	@Override
	public boolean selectCriteria(File f){
		super.selectCriteria(f);
		if(!selectCriteria.selectCriteria(f)){
			return false;
		}
		
		//相关业务
		if(f.getName().indexOf(Arr[0]) == -1){
			return false;
		}
		return true;
	}
	

}
