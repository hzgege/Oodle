package templateMethodPattern;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 分配线程计算每个目录下的文件大小，计算结束后将值插入阻塞队列 随后循环读取阻塞队列的所有值累加，直到子目录遍历完毕
 */
public class GetTotalSize extends AbsTemplate{

	@Override
	public void cal(String path) {
		long fileSize = 0;
		File file = new File(path);
		if (file.isFile())
			fileSize = file.length();
		else {
			final File[] children = file.listFiles();
			if (children != null)
				for (File child : children) {
					if (child.isFile())
						fileSize += child.length();
					else {
						explore(child.getPath());
					}
				}
		}
		try {
			// 把fileSize加到BlockingQueue里,如果BlockQueue没有空间被阻塞直到有空间被唤醒
			fileQueue.put(fileSize);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		visitTime.decrementAndGet();
	}

	@Override
	public void pollAndAdd() {
		try {
			final Long size = (Long) fileQueue.poll(10, TimeUnit.SECONDS);
			object = (long)object + size;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
