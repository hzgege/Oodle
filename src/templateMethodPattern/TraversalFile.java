package templateMethodPattern;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import oodleOperate.FileConversion;
import oodleOperate.MyDir;
import oodleOperate.MyFile;

public class TraversalFile extends ABSTemplate{

	@Override
	public void cal(String path) {
		
		ConcurrentHashMap<String, List<MyFile>> m = new ConcurrentHashMap<>();
		File file = new File(path);
		List<MyFile> listFile = new ArrayList<>();
		File[] files = file.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isFile()) {
					MyFile MF = FileConversion.getMyFile(f);
					listFile.add(MF);
				} else {
					MyDir MD = FileConversion.getMyDir(f);
					listFile.add(MD);
					explore(f.getAbsolutePath());
				}
			}
		}
		m.put(path, listFile);
		try {
			fileQueue.put(m);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
		visitTime.decrementAndGet();
	}


	@SuppressWarnings("unchecked")
	@Override
	public void pollAndAdd() {
		try {
			final ConcurrentHashMap<String, List<MyFile>> m1 = (ConcurrentHashMap<String, List<MyFile>>) 
					fileQueue.poll(10, TimeUnit.SECONDS);
			((ConcurrentHashMap<String, List<MyFile>>) object).putAll(m1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}