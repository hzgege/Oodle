package oodleOperate;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import application.MyController;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import selectCriteriaDecoratorPattern.Decorator;
import selectCriteriaDecoratorPattern.SelectCriteria;

public class DoScandir {

    /**
     * @param dirName    目录
     * @param fileList   保存的List
     * @param selectCriteria 谓词
     * @param compare 自定义排序
     * @return
     */
	public int scandir(String dirName, List<MyFile> fileList, SelectCriteria SC, Comparator<MyFile> compare) {

		fileList.clear();
        if(dirName == null)
            return -1;
        File tempFile = new File(dirName);
		if(!tempFile.exists()){
			Alert error = new Alert(AlertType.ERROR,"该文件/目录不存在");
			error.showAndWait();
			return -1;
		}
        File file = new File(dirName);
        File[] files = file.listFiles();  //获取所有的文件或文件夹
        
        //没有文件或文件夹时
        if(files == null)
        	return 0;

        //遍历
        for(File f : files){
            //符合谓词
            if(SC.selectCriteria(f)){
                    if(!f.isDirectory()){
                    	MyFile myFile = FileConversion.getMyFile(f);
                    	fileList.add(myFile);
                    }
                    else//f.isDirectory 目录
                    {
                    	MyDir myDir = FileConversion.getMyDir(f);
                    	fileList.add(myDir);
                    }
            }
        }
        if(fileList != null){
        	//按compare排序
            Collections.sort(fileList,compare);
        }
        return 0;
    }
}
