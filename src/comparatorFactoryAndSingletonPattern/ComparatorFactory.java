package comparatorFactoryAndSingletonPattern;

import java.util.Comparator;

import oodleOperate.MyFile;

public class ComparatorFactory {

	public Comparator<MyFile> getComparator(String sortType) {
		Comparator<MyFile> comparator = null;
		// 排序方式
		switch (sortType) {
		case "按文件名升序":
			comparator = FileNameComparatorSingleton.getSingleton("ESC");
			break;
		case "按文件名降序":
			comparator = FileNameComparatorSingleton.getSingleton("DESC");
			break;
		case "按文件大小升序":
			comparator = FileSizeComparatorSingleton.getSingleton("ESC");
			break;
		case "按文件大小降序":
			comparator = FileSizeComparatorSingleton.getSingleton("DESC");
			break;
		case "按修改时间升序":
			comparator = FileLastModifyTimeComparatorSingleton.getSingleton("ESC");
			break;
		case "按修改时间降序":
			comparator = FileLastModifyTimeComparatorSingleton.getSingleton("DESC");
			break;
		default:
			break;
		}
		return comparator;
	}
}
