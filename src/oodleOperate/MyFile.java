package oodleOperate;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyFile implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * fileName 文件名
     * fileSize 文件大小
     * fileChangeTime 文件修改时间
     * filePath 文件路径
     */
    private String Name;
    private String Size;
    private String LastModifyTime;
    private String Path;
    private long sizeLong;
    public MyFile(){

    }

    /**
     * 构造函数
     * @param fileName
     * @param fileSize
     * @param fileLastModifyTime
     * @param filePath
     */
    public MyFile(String Path, String Name, long Size, long LastModifyTime){
    	this.Path = Path;
        this.Name = Name;
        this.sizeLong = Size;
        this.Size = sizeConvertToString(Size);
        this.LastModifyTime = timeConvertToString(LastModifyTime);
    }
    
    /**
     * 将long类型的time转为标准的时间格式
     * @param time 时间long
     * @return 时间的标准格式
     */
    public static String timeConvertToString(long time){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cal.setTimeInMillis(time);
        return simpleDateFormat.format(cal.getTime());
    }
    
    public static String sizeConvertToString(long size) {
    	if((size % (long)1024) == 0){
    		return String.valueOf(size / (long) 1024)+"KB";
    	}
    	return String.valueOf(size / (long) 1024 + 1)+"KB";
	}
    
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return " 名称：" + this.Name
    			+ " 修改日期：" + this.LastModifyTime
    			+ " 大小：" + this.Size + " 字节   路径："+ this.Path;
    }
    
    public String getName() {
        return Name;
    }

    public String getSize() {
        return Size;
    }

    public String getLastModifyTime() {
        return LastModifyTime;
    }

    public String getPath() {
        return Path;
    }
    
    public long getSizeLong() {
		return sizeLong;
	}
    
  //重写用于判断是否包含此文件夹，因此就只对名字进行比较
    @Override
    public boolean equals(Object object){
    	if(this == object)
            return true;
        if(object == null)
            return false;
        if(this.getClass() != object.getClass())
            return false;
        final MyFile f = (MyFile) object;
        if(this.getName().compareTo(f.getName()) != 0)
            return false;
        return true;
    }
}
