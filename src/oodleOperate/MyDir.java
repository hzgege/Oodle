package oodleOperate;

import java.util.List;

public class MyDir extends MyFile{


	private static final long serialVersionUID = 1L;
	private String Path;
	private String Name;
	private String Size;
	private String LastModifyTime;
	
	public MyDir() {
		// TODO Auto-generated constructor stub
	}
	
	public MyDir(String Path, String Name, long LastModifyTime){
		this.Name = Name;
		this.Path = Path;
		this.Size = "";
		this.LastModifyTime = timeConvertToString(LastModifyTime);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
    	return " 名称：" + this.Name + " "
				+ " 修改日期：" + this.LastModifyTime+"   路径："+ this.Path;
	}

	public String getName() {
		return Name;
	}

	public String getPath() {
		return Path;
	}

	public String getSize() {
		return Size;
	}

	public String getLastModifyTime() {
		return LastModifyTime;
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
