package org.opsli.common.thread;

import cn.hutool.core.thread.ThreadUtil;

public class Test {

	public static void main(String[] args) {
		AsyncProcessExecutor executor = new AsyncProcessExecutorByNormal();
		for (int i = 0; i < 10000; i++) {
			int finalI = i;
			executor.put(()->{
				ThreadUtil.sleep(1000);
				System.out.println(finalI);
			});
		}
		boolean execute = executor.execute();
		System.out.println(execute);
	}

}
