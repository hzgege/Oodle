package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import comparatorFactoryPattern.FileNameComparator;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import oodleOperate.MyFile;

public class ShowDiff {

	public void display(HashSet<String> newFile,HashSet<String> deleteFile, 
			HashSet<MyFile> FileNewInfo, HashSet<MyFile> FileOldInfo){
		
		List<MyFile> newInfoList = new ArrayList<>(FileNewInfo);
		List<MyFile> oldInfoList = new ArrayList<>(FileOldInfo);
		//随便按一种方式排序，确保遍历时一一对应
		Comparator<MyFile> comparator = new FileNameComparator("ESC");
		Collections.sort(newInfoList,comparator);
		Collections.sort(oldInfoList,comparator);
		
		Stage window = new Stage();
		window.setTitle("差异模式结果");
		window.initModality(Modality.APPLICATION_MODAL);
	    window.setWidth(500);
	    window.setHeight(600);
	    
	    Label labelNew = new Label("新增的文件或目录：");
	    TextArea textAreaNew = new TextArea();
	    
	    Label labelDelete = new Label("删除的文件或目录：");
	    TextArea textAreaDelete = new TextArea();
	    
	    Label labelChange = new Label("修改的文件或目录：");
	    TextArea textAreaChange = new TextArea();
	    
	    StringBuffer sbNew = new StringBuffer();
	    StringBuffer sbDelete = new StringBuffer();
	    StringBuffer sbChange = new StringBuffer();
	    
	    //设置文本域内容
	    for(String s : newFile){
	    	sbNew.append(s+"\n");
	    }
	    textAreaNew.setText(sbNew.toString());
	    
	    for(String s : deleteFile){
	    	sbDelete.append(s+"\n");
	    }
	    textAreaDelete.setText(sbDelete.toString());
	    
	    MyFile tempNew = null;
	    MyFile tempOld = null;
	    for(int i = 0; i < newInfoList.size(); i++){
	    	tempNew = newInfoList.get(i);
	    	tempOld = oldInfoList.get(i);
	    	sbChange.append("当前信息："+tempNew+"\n");
	    	sbChange.append("日志信息："+tempOld+"\n");
	    	sbChange.append("\n");
	    }
	    textAreaChange.setText(sbChange.toString());
	    
	    textAreaNew.setEditable(false);
	    textAreaDelete.setEditable(false);
	    textAreaChange.setEditable(false);
	    
	    VBox vBox = new VBox();
	    vBox.setSpacing(10);
	    vBox.getChildren().add(labelNew);
	    vBox.getChildren().add(textAreaNew);
	    vBox.getChildren().add(labelDelete);
	    vBox.getChildren().add(textAreaDelete);
	    vBox.getChildren().add(labelChange);
	    vBox.getChildren().add(textAreaChange);
	    
	    Scene scene = new Scene(vBox);
	    window.setScene(scene);
	    window.showAndWait();
	}
}
