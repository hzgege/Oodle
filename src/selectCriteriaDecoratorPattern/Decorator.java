package selectCriteriaDecoratorPattern;

import java.io.File;

public class Decorator implements SelectCriteria{

	protected SelectCriteria selectCriteria;
	protected String[] Arr;
	
	public Decorator(SelectCriteria selectCriteria, String[] arr) {
		this.selectCriteria = selectCriteria;
		this.Arr = arr;
	}
	
	@Override
	public boolean selectCriteria(File f) {
		return true;
	}

}
