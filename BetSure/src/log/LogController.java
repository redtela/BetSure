package log;

import pool.PoolController;

public class LogController extends PoolController {
	
	public LogController() {
		super();
		System.out.println("Pool Size is " + this.getPoolSize());
	}
	
	@Override
	protected Integer getDefaultPoolSize() {
		return new Integer(2);
	}
	
	@Override
	protected String getDefaultConsumerName() {
		return "log.LogConsumer";
	}
}
