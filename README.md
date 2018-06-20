
# 项目需求

## 1.1 基本功能
	用戶可在文件系統中任意查看不同目錄，某一目錄中的所有文件可以不同的排序依據排列，排序依據包括：文件名，文件大小，修改時間。

	用OO(Object Oriented)範式實現如下函數：

	int scandir( dirName, fileList, selectCriteria, compare )

	該函數首先讀取目錄 dirName 中的所有文件，但祗選取滿足謂詞 selectCriteria 的所有文件，并按照 compare 指定的方式進行排序後將所選取的文件存入列表fileList中。注意：存入 fileList 中的每個文件都是一個對象，包含此文件的種種信息（但不包括該文件的內容），而非僅僅是文件名而已。

## 1.2 扩展功能
	程序可進入兩種模式：日誌模式及差異模式，描述如下：

	日誌模式：在日誌模式中程序遞歸地遍歷某目錄樹，以部分永久存儲的方式記錄所遍歷的文件信息。

	差異模式：在差異模式中程序遞歸地遍歷某目錄樹，將之與之前在日誌模式中所記錄的該目錄樹的文件信息相比較，顯示二者所有的差異。在顯示差異時，若某目錄在日誌產生之後被刪除或被創建，則祗需顯示該目錄名，而不必顯示該目錄的內容。

	說明：本程序在Windows或Linux下實現均可。

## 1.3 实现要求
	使用學過的設計模式（若學有餘力，亦可應用課堂未講過的設計模式）改進你的設計

	1）畫出應用模式之後的設計類圖，并闡述每個類、每個方法的目的及作用，至少讓他人可以大致明白你的設計；

	每個所應用的模式都要在系統設計類圖中加以標註及文字說明，譬如哪個類是 Strategy 模式中的host，哪個類是algorithm等等；闡述你為何要使用該模式，好處何在, 可能帶來的壞處是什麼。
	因大多數高級語言已經內置文件系統的訪問功能，請不要使用高級類庫、框架完成課程項目，否則就喪失了大部分鍛煉價值。


## 1.4运行需求
	1.4.1. 需安装JavaFx(https://www.yiibai.com/javafx/install-efxclipse-into-eclipse.html#article-start)
	1.4.2. jdk 1.8 以上

# 类图
![Image text](https://raw.githubusercontent.com/hzgege/Oodle/master/READMEIMG/%E7%B1%BB%E5%9B%BE.png)

# 类图描述

### 1.	MyFile类 
	i.	实现Serializable序列化接口，用于序列化存取日志信息
	ii.	重写equals方法和toString方法
	iii.	sizeConvertToString方法是将long类型的文件size转为带KB的size字符串
	iv.	timeConvertToString方法是将long类型的文件修改时间转为固定格式yyyy-MM-dd HH:mm:ss 的字符串

### 2.	MyDir类
	i.	继承MyFile类，也可进行序列化存储
	ii.	重写equals方法和toString方法

### 3.	FileConvert类
	i.	两个类方法getMyFile和getMyDir，分别将传入File类型对象的转换为自定义的MyFile和MyDir类型对象

### 4.	DoScandir类
	i.	需求中第一部分的要求，根据传入的路径，获取该路径下的所有文件和文件夹，将符合所设定的谓词的文件或文件夹 通过FileConvert类转换为自定义的类型对象，存入List中，最后根据设定的Compare方式对List进行排序。

### 5.	FileLastModifyTimeComparatorSingleton类
	i.	修改时间排序方式单例
	ii.	内部有一个boolean类型的变量，用于判断升序还是降序（升序时文件夹在前，文件在后，降序相反）
	iii.	getSingleton返回该单例

### 6.	FileSizeComparatorSingleton类
	i.	文件大小排序方式单例（ii、iii和6中的相同）

### 7.	FileNameComparatorSingleton类
	i.	文件名排序方式单例（ii、iii和6中的相同）

### 8.	SelectCriteria接口
	i.	谓词接口
	ii.	接口方法selectCriteria(File f)判断传入的File对象是否满足，返回类型为boolean

### 9.	ConcreteCriteria类
	i.	具体谓词类，负责接受谓词
	ii.	selectCriteria(File f) 默认返回true

### 10.Decorator类
	i.	装饰角色，持有一个SelectCriteria对象
	ii.	selectCriteria(File f) 默认返回true

### 11.CriteriaBySize类
	i.	文件大小装饰角色
	ii.	selectCriteria(File f) 中判断File对象的大小是否符合设定的范围（文件夹默认返回false）

### 12.CriteriaByName类
	i.	文件名装饰角色
	ii.	selectCriteria(File f) 中判断File对象的名字是否含有设定的字符串

### 13.CriteriaByLastModifyTime类
	i.	文件修改时间装饰角色
	ii.	selectCriteria(File f) 中判断File对象的最后修改时间是否符合设定的时间范围

### 14.AbsTemplate抽象类
	i.	模板方法抽象类，使用线程以及阻塞队列，对文件树进行递归遍历，获取所需信息（文件夹大小、 文件夹下所有文件和子文件夹的所有文件信息）
	ii.	内部有一个BlockingQueue<Object> fileQueue，将线程获取到的信息存入该阻塞队列中，定时从队列中拉取信息进行增加计算操作（最终结果用于返回）。
	iii.	ExecutorService service 线程接口用于打开线程
	iv.	定义两个抽象操作 cal(String path) 和 pollAndAdd() 根据需要的不同的信息，有不同的计算方法和从阻塞队列中拉取信息并增加计算的方法。
	v.	定义模板方法，开启线程explore(String path)、返回结果
	getResult(String p, Object o)、Object的getter和setter方法（所需信息的类型）

### 15.GetTotalSize类
	i.	继承ABSTemplate抽象类，根据size所需类型为long，实现long类型的cal(String path) 和 pollAndAdd()

### 16.TraversalFile类
	i.	继承ABSTemplate抽象类，根据所需类型为ConcurrentHashMap<String, List<MyFile>>，实现该类型的cal(String path) 和 pollAndAdd()

### 17.Mode接口
	i.	策略模式接口

### 18.DifferentialMode类
	i.	内部有newFile、deleteFile、FileNewInfo、FileOldInfo四个HashSet，分别存放差异模式的结果（新建的、被删除的、修改了的（新的信息、旧的信息））
	ii.	findNew、findDeleted、findChanged三个方法分别用于doMode时寻找新增的文件、被删除的文件、修改的文件，并将结果存入对应的HashSet中。
	iii.	doMode(String path) 对路径进行差异模式操作，使用模板方法中的递归遍历方法，然后读取已序列化存储该路径的内容，再进行比较寻找差异。
	iv.	clearAll() 方法将四个HashSet清空。

### 19.LogMode类
	i.	doMode(String path) 对路径进行日志模式操作，使用模板方法中的递归遍历方法，并将结果进行序列化存储
	ii.	setFileName(String path) 设置序列化存储的文件名

### 20.ProxyInterface抽象代理接口
	i.	序列化写入文件代理方法writeObjToFile
	ii.	序列化读取方法readObjFromFile

### 21.AbsReadAndWrite抽象类
	i.	目标对象抽象类，实现ProxyInterface接口，并对两个方法提供空的实现，使ReadFromFile类只需实现所需的readObjFromFile方法，而不需实现writeObjToFile。WriteToFile同理

### 22.ReadFromFile类
	i.	目标对象，继承AbsReadAndWrite，实现具体的readObjFromFile方法

### 23.WriteToFile类
	i.	目标对象，继承AbsReadAndWrite，实现具体的writeObjToFile方法

### 24.ReadandWriteProxy类
	i.	代理对象，内部拥有ReadFromFile和WriteToFile对象
	ii.	DirExists方法，判断所进行读取的文件夹是否存在，不存在则创建
	iii.	在进行读取之前进行ii中的操作。

### 25.Main类
	i.	程序入口

### 26.Controller类
	i.	控制器，界面的点击响应，文件信息的显示

### 27.ShowDiffUI类
	i.	用于显示差异模式的结果



# 使用到的设计模式

### 1.	单例模式
	i.	涉及的类图
![Image text](https://raw.githubusercontent.com/hzgege/Oodle/master/READMEIMG/%E5%8D%95%E4%BE%8B%E6%A8%A1%E5%BC%8F.png)
	ii.	为什么使用
	a)	因为此处是要返回一个排序比较器，无需每次更换排序方式就新建一个对象，浪费空间，所以使用单例模式，提高效率，保证对应的排序方式只有一个实例。
	b)	此处只有三个对象，使用空间换时间的方法，无需每次访问都判断对象是否存在，而在类初始化时就生成对象。
	iii.	设计原则
	DIP，排序方式和功能调用都依赖于抽象接口。
	iv.	坏处
	职责过重，在一定程度上违反了SRP

### 2.	装饰器模式
	i.	涉及的类图
![Image text](https://raw.githubusercontent.com/hzgege/Oodle/master/READMEIMG/%E8%A3%85%E9%A5%B0%E5%99%A8%E6%A8%A1%E5%BC%8F.png)
	ii.	为什么使用
	a)	设置谓词，可以有文件名是否匹配字符串、文件大小是否在设定范围内、文件修改时间是否在设定范围内。通过装饰器模式，可以动态为谓词对象添加不同的功能。
	b)	通过使用0~3种不同的装饰类，可以创造出不同组合，灵活性更高。
	c)	谓词类与装饰类可以独立变化，可以根据需要继续添加。
	iii.	设计原则
	OCP
	iv.	坏处
	a)	会产生很多小的对象，增加复杂性
	b)	虽然比继承更灵活，但同时排错也会更困难

### 3.	模板方法模式
	i.	涉及的类图
![Image text](https://raw.githubusercontent.com/hzgege/Oodle/master/READMEIMG/%E6%A8%A1%E6%9D%BF%E6%96%B9%E6%B3%95%E6%A8%A1%E5%BC%8F.png)
	ii.	为什么使用
	a)	这里是递归遍历文件树的时候使用的，因为两个类的遍历方法相同，区别在于所需要获取的对象是不同的，于是将其封装为一个模板方法，由子类实现具体细节，实现代码复用。
	b)	对扩展方便，符合OCP
	iii.	设计原则
	OCP
	iv.	坏处
	对于不同的实现需要新建一个子类来实现，如果数量多的话，系统更复杂庞大


### 4.	策略模式
	i.	涉及的类图
![Image text](https://raw.githubusercontent.com/hzgege/Oodle/master/READMEIMG/%E7%AD%96%E7%95%A5%E6%A8%A1%E5%BC%8F.png)
	ii.	为什么使用
	对于需求中的日志模式和差异模式，两者不相互依赖，提供对两个算法的管理。符合OCP原则，增加策略时无需修改。
	iii.	设计原则
	OCP
	iv.	坏处
	a)	调用者需知道所有的策略算法，了解他们的区别，并决定使用哪一个策略。

### 5.	代理模式
	i.	涉及的类图
![Image text](https://raw.githubusercontent.com/hzgege/Oodle/master/READMEIMG/%E4%BB%A3%E7%90%86%E6%A8%A1%E5%BC%8F.png)
	ii.	为什么使用
	a)	在进行序列化存储时，需检查路径是否存在，为了不违反存储类的单一职责，使用代理模式可以在进行存储操作的前后，加上一些必须的操作
	b)	一定程度上在调用者和目标对象之间起到中介作用，起到保护目标对象的作用
	iii.	设计原则
	SRP
	iv.	坏处
	增加代理对象会导致系统复杂度增加

# 程序运行截圖

## 主界面
![Image text](https://raw.githubusercontent.com/hzgege/Oodle/master/READMEIMG/%E4%B8%BB%E7%95%8C%E9%9D%A2.png)

## 排序
![Image text](https://raw.githubusercontent.com/hzgege/Oodle/master/READMEIMG/%E6%8E%92%E5%BA%8F.png)

## 右键目录查看大小，双击进入目录，返回按钮返回
![Image text](https://raw.githubusercontent.com/hzgege/Oodle/master/READMEIMG/%E8%BF%94%E5%9B%9E.png)

## 谓词设置(输入文件名、文件大小范围、修改时间范围，ClearAll可以全清空)
![Image text](https://raw.githubusercontent.com/hzgege/Oodle/master/READMEIMG/%E8%B0%93%E8%AF%8D.png)

## 日志模式与差异模式
![Image text](https://raw.githubusercontent.com/hzgege/Oodle/master/READMEIMG/%E6%97%A5%E5%BF%97%E6%A8%A1%E5%BC%8F.png)
![Image text](https://raw.githubusercontent.com/hzgege/Oodle/master/READMEIMG/%E5%B7%AE%E5%BC%82%E6%A8%A1%E5%BC%8F.png)