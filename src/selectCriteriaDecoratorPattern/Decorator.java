package selectCriteriaDecoratorPattern;

import java.io.File;

public abstract class Decorator implements SelectCriteria{

	@Override
	public abstract boolean selectCriteria(File f);

}
