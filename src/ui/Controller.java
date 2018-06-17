package ui;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import comparatorSingletonPattern.FileLastModifyTimeComparatorSingleton;
import comparatorSingletonPattern.FileNameComparatorSingleton;
import comparatorSingletonPattern.FileSizeComparatorSingleton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import modeStrategyPattern.DifferentialMode;
import modeStrategyPattern.LogMode;
import modeStrategyPattern.Mode;
import oodleOperate.DoScandir;
import oodleOperate.MyDir;
import oodleOperate.MyFile;
import selectCriteriaDecoratorPattern.ConcreteSelectCriteria;
import selectCriteriaDecoratorPattern.CriteriaByLastModifyTime;
import selectCriteriaDecoratorPattern.CriteriaByName;
import selectCriteriaDecoratorPattern.CriteriaBySize;
import selectCriteriaDecoratorPattern.SelectCriteria;
import templateMethodPattern.AbsTemplate;
import templateMethodPattern.GetTotalSize;

public class Controller implements Initializable {

	private ObservableList<MyFile> tablelist = FXCollections.observableArrayList();
	private AbsTemplate absTemplate = new GetTotalSize();
	//右键弹出 获取文件夹大小的按钮
	private ContextMenu cm = null;
	private MenuItem menuItem = null;
	private DoScandir ds = new DoScandir();
	private List<MyFile> resultList = new ArrayList<>();
	
	@FXML
	private Button myButton;
	@FXML
	private TextField currentDir, filenametext, kbfloor, kbceiling;
	@FXML
	private CheckBox checkbox;	
	@FXML
	private TableView<MyFile> tableview;
	@FXML
	private TableColumn<MyFile, String> filenamecolumn, filemodifytimecolumn, filesizecolumn;
	@FXML
	private DatePicker datebegin, dateend;
	@FXML
	private ComboBox<String> sortway;
	
	
	
	//combobx的排序方式列表
	final static ObservableList<String> options =   
		    FXCollections.observableArrayList(  
		        "按文件名升序",  
		        "按文件名降序",  
		        "按文件大小升序",
		        "按文件大小降序",
		        "按修改时间升序",
		        "按修改时间降序"
		    ); 
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initTableView();
		initViewSizeMenu();
		initComboBox();
	}

	private void initComboBox(){
		sortway.getItems().addAll(options);
		sortway.setValue(options.get(0));
	}
	
	// 初始化tableview的右键弹出菜单
	private void initViewSizeMenu() {
		cm = new ContextMenu();
		menuItem = new MenuItem("View Size");
		cm.getItems().add(menuItem);
	}

	// 初始化tableView的列
	private void initTableView() {
		filenamecolumn.setCellValueFactory(new PropertyValueFactory<MyFile, String>("Name"));
		filemodifytimecolumn.setCellValueFactory(new PropertyValueFactory<MyFile, String>("LastModifyTime"));
		filesizecolumn.setCellValueFactory(new PropertyValueFactory<MyFile, String>("Size"));

		filenamecolumn.setSortable(false);
		filemodifytimecolumn.setSortable(false);
		filesizecolumn.setSortable(false);
		
		// tableview的监听事件
		tableview.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// tableview的双击事件，如果是文件夹就进入下一层
				if (event.getClickCount() == 2) {
					MyFile tempFile = tableview.getSelectionModel().getSelectedItem();
					// 双击的是文件夹，那么进入下一层
					if (tempFile != null && tempFile.getClass() == MyDir.class) {
						StringBuilder nowDir = new StringBuilder(currentDir.getText());
						if(nowDir.length() == 3)//防止有路径有\\的情况出现
							nowDir.append(tempFile.getName());
						else
							nowDir.append("\\" + tempFile.getName());
						String newDir = nowDir.toString();
						currentDir.setText(newDir);
						showAtTableView(newDir);
					}
				}
				// 右键 查看大小
				if (event.getButton() == MouseButton.SECONDARY) {
					MyFile tempFile = tableview.getSelectionModel().getSelectedItem();
					// 如果是文件夹，就有右键查看size的功能
					if (tempFile != null && tempFile.getClass() == MyDir.class) {
						cm.show(tableview, event.getScreenX(), event.getScreenY());
					}
					menuItem.setOnAction((ActionEvent e) -> {
						long tempFileSize = 0;
						String tempSize = "0 KB";
						try {
							tempFileSize = (long) absTemplate.getResult(tempFile.getPath(), (long)0);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						if (tempFileSize != 0) {
							String str = null;
							if ((tempFileSize % (long) 1024) == 0) {
								str = String.valueOf(tempFileSize / (long) 1024) + " KB";
							}
							str = String.valueOf(tempFileSize / (long) 1024 + 1) + " KB";
							StringBuffer sB = new StringBuffer(str);
							sB.append("(" + tempFileSize + "字节)");
							tempSize = sB.toString();
						}
						Alert sizeAlert = new Alert(Alert.AlertType.INFORMATION,
								"文件夹 " + tempFile.getName() + " 的大小为：" + tempSize);
						sizeAlert.showAndWait();
					});
				}
			}
		});
	}
	
	// 排序方式修改的监听
	public void cliclCombobox(){
		sortway.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				showAtTableView(currentDir.getText());
			}
		});
	}
	
	// 点击<按钮时调用,修改上方文本框路径,修改下方tableview
	public void clickBack(ActionEvent event) {
		String path = currentDir.getText();
		// 如果空
		if (path.length() == 0 || path.equals(System.getenv("COMPUTERNAME"))) {
			Alert error = new Alert(Alert.AlertType.ERROR, "错误：无法返回上一级文件夹 ");
			error.showAndWait();
		} else {
			// 不空 但是是最后一个
			if (path.lastIndexOf('\\') == path.indexOf('\\') && path.indexOf('\\') == path.length() - 1) {
				Alert error = new Alert(Alert.AlertType.ERROR, "错误：无法返回上一级文件夹 ");
				error.showAndWait();
			} else {
				// 去掉最后一个文件夹
				int index = path.lastIndexOf('\\');
				String strFatherDir = null;
				if (index == path.indexOf('\\'))
					strFatherDir = path.substring(0, index + 1);
				else
					strFatherDir = path.substring(0, index);
				currentDir.setText(strFatherDir);
				// 修改下方tableview
				showAtTableView(strFatherDir);
			}
		}
	}

	// 点击search按钮时调用,查找然后修改下方tableview
	public void clickSearch(ActionEvent event) {
		String path = currentDir.getText();
		if (path == null || path.equals(""))
			return;
		showAtTableView(path);
	}

	// 点击choose按钮时调用,选择目录并设置currentDir的text
	public void clickChoose(ActionEvent event) {
		// 目录选择器
		Stage fileChooseStage = null;
		DirectoryChooser folderChooser = new DirectoryChooser();
		File selectedFile = folderChooser.showDialog(fileChooseStage);
		folderChooser.setTitle("选择文件");
		// 若为空则返回，不为空获取路径，修改currentDir
		if (null == selectedFile)
			return;
		String path = selectedFile.getPath();
		currentDir.setText(path);
		showAtTableView(path);
	}

	// 点击日志模式按钮时调用
	public void clickLog(ActionEvent event) {
		Mode mode = new LogMode();
		String nowDir = currentDir.getText();
		File tempFile = new File(nowDir);
		if(!tempFile.exists()){
			Alert error = new Alert(AlertType.ERROR,"该文件/目录不存在");
			error.showAndWait();
			return ;
		}
		if(nowDir.equals("") || nowDir == null){
			Alert error = new Alert(Alert.AlertType.ERROR, " 您还未选择目录 ");
			error.setTitle("来自日志模式的错误");
			error.showAndWait();
			return;
		}
		//开启带返回值线程，对于C、D等大盘会有点慢
		Callable<String> callable = new Callable<String>() {
            public String call() throws Exception {
            	return mode.doMode(nowDir);
            }
        };
        FutureTask<String> future = new FutureTask<String>(callable);
        new Thread(future).start();
		Alert success;
		try {
			success = new Alert(Alert.AlertType.INFORMATION, future.get());
			success.showAndWait();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	// 点击差异模式按钮时调用
	public void clickDiff(ActionEvent event) {
		Mode mode = new DifferentialMode();
		String nowDir = currentDir.getText();
		File tempFile = new File(nowDir);
		if(!tempFile.exists()){
			Alert error = new Alert(AlertType.ERROR,"该文件/目录不存在");
			error.showAndWait();
			return ;
		}
		if(nowDir.equals("") || nowDir == null){
			Alert error = new Alert(Alert.AlertType.ERROR, " 您还未选择目录 ");
			error.setTitle("来自差异模式的错误");
			error.showAndWait();
			return;
		}
		//开启带返回值线程，对于C、D等大盘会有点慢
		Callable<String> callable = new Callable<String>() {
            public String call() throws Exception {
            	return mode.doMode(nowDir);
            }
        };
        FutureTask<String> future = new FutureTask<String>(callable);
        new Thread(future).start();
        Alert success;
		try {
			if(!future.get().equals("差异模式成功")){
				success = new Alert(Alert.AlertType.INFORMATION, future.get());
				success.showAndWait();
			}else{
				//成功的时候显示结果
				new ShowDiffUI().display(DifferentialMode.getNewFile(), DifferentialMode.getDeleteFile(),
						DifferentialMode.getFileNewInfo(), DifferentialMode.getFileOldInfo());
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	//获得文件名匹配值
	private String[] getFileTextName(){
		String[] nameArr = new String[1];
		if(filenametext.getText() != null && filenametext.getText().compareTo("") != 0){
			nameArr[0] =  filenametext.getText();
		}
		return nameArr;
	}
	
	//获得大小范围
	private String[] getSizeRange() {
		String[] strArr = new String[2];
		if(kbfloor.getText() != null && kbfloor.getText().compareTo("") != 0){
			strArr[0] = kbfloor.getText();
		}
		if(kbceiling.getText() != null && kbceiling.getText().compareTo("") != 0){
			strArr[1] = kbceiling.getText();
		}
		return strArr;
	}
	
	//获得时间范围
	private String[] getTimeRange() {
		String[] strArr = new String[2];
		if(datebegin.getValue() != null){
			strArr[0] = datebegin.getValue().toString();
		}
		if(dateend.getValue() != null){
			strArr[1] = dateend.getValue().toString();
		}
		return strArr;
	}
	
	// 将路径下的文件和文件夹显示与tableview
	private void showAtTableView(String path) {
		if (path == null || path.equals("") || path.equals(System.getenv("COMPUTERNAME")))
			return;
		tablelist.clear();
		resultList.clear();
		// 调用Scandir
		// 获取排序方式
		String sortWayString = sortway.getSelectionModel().getSelectedItem();
		Comparator<MyFile> comparator = null;
		getTimeRange();
		//排序方式
		switch (sortWayString) {
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
		//谓词设置
		String[] strName = getFileTextName();
		String[] timeArr = getTimeRange();
		String[] sizeArr = getSizeRange();
		
		SelectCriteria concreteSC = new ConcreteSelectCriteria();
		if(strName[0] != null)
			concreteSC = new CriteriaByName(concreteSC, strName);
		if(timeArr[0] != null || timeArr[1] != null)
			concreteSC = new CriteriaByLastModifyTime(concreteSC, timeArr);
		if(sizeArr[0] != null || sizeArr[1] != null)
			concreteSC = new CriteriaBySize(concreteSC, sizeArr);
		
		ds.scandir(path, resultList, concreteSC, comparator);
		// 将读到的Scandir显示到tableview
		for (MyFile l : resultList) {
			tablelist.add(l);
		}
		tableview.setItems(tablelist);
	}
}
