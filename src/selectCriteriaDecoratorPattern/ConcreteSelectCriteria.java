package selectCriteriaDecoratorPattern;

import java.io.File;

public class ConcreteSelectCriteria implements SelectCriteria{
	
	@Override
	public boolean selectCriteria(File f) {
		return true;
	}
}
