package templateMethodPattern;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;


public abstract class AbsTemplate {

	protected ExecutorService service;
	final protected BlockingQueue<Object> fileQueue = new ArrayBlockingQueue<Object>(500);
	final protected AtomicLong visitTime = new AtomicLong();
	protected Object object;

	protected void explore(String path) {
		visitTime.incrementAndGet();
		service.execute(new Runnable() {
			public void run() {
				cal(path);
			}
		});
	}

	public Object getResult(String p, Object o) throws InterruptedException {

		service = Executors.newFixedThreadPool(100);
		try {
			explore(p);
			setObject(o);
			while (visitTime.get() > 0 || fileQueue.size() > 0) {
				// 从BlockingQueue取出队首的对象,
				// 如果在指定时间内,一旦有数据可取,则立即返回队列中的数据
				// 否则直到时间超时还没有数据可取,返回失败。
				pollAndAdd();
			}
			return getObject();
		} finally {
			service.shutdown();
		}
	}

	protected Object getObject() {
		return object;
	}

	protected void setObject(Object object) {
		this.object = object;
	}

	protected abstract void cal(String path);
	protected abstract void pollAndAdd();
}
