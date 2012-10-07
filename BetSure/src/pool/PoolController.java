package pool;

import java.util.ArrayList;

import message.Message;

import config.Configurable;

public class PoolController extends Configurable {

	protected Integer	DEFAULT_POOLSIZE 		= new Integer(10);
	protected String	DEFAULT_CONSUMERNAME 	= "pool.PoolConsumer";
	protected Boolean 	DEFAULT_AUTOSTART 		= new Boolean(false);
	protected Long		DEFAULT_DEATHWAIT		= new Long(750);
	protected Long		DEFAULT_SLEEPWAIT		= new Long(500);
	
	private ArrayList<PoolConsumer> consumers = new ArrayList<PoolConsumer>();
	@SuppressWarnings("unused")
	private Thread thread = null;
	private boolean continueRunning = false;
	
	public PoolController() {
		super();
	}
	
	public void initialise() {
		// generate the pool
		for (int i=0; i<this.getPoolSize(); i++) {
			PoolConsumer o = this.loadObject(this.getConsumerName());
			if (o instanceof PoolConsumer) this.consumers.add(o);
		}
		// start the thread
		if (this.getAutoStart()) this.startThread();
	}
	
	public void startThread() {
		this.continueRunning = true;
		this.thread = new Thread() {
			public void run() {
				runThread();
			};
		};
	}
	
	public void stopThread() {
		this.continueRunning = false;
		try {
			Thread.sleep(this.getDeathWait());
		} catch (InterruptedException e) {
			// ignore this
		}
		this.thread = null; // let garbage collection worry about it
	}
	
	private long getDeathWait() {
		Long retval = this.getLong("wait_death");
		if (retval == null) {
			retval = this.getDefaultDeathWait();
			this.setDeathWait(retval);
		}
		return retval.longValue();
	}
	
	private Long getDefaultDeathWait() {
		return this.DEFAULT_DEATHWAIT;
	}
	
	private void setDeathWait(Long v) {
		this.set("wait_death", v.toString());
	}
	
	private boolean getAutoStart() {
		Boolean b = this.getBoolean("auto_start");
		if (b == null) {
			b = this.getDefaultAutoStart();
			this.setAutoStart(b);
		} 
		return b.booleanValue();		
	}
	
	private Boolean getDefaultAutoStart() {
		return this.DEFAULT_AUTOSTART;
	}
	
	private void setAutoStart(Boolean value) {
		this.set("auto_start", value.toString());
	}
	
	private void runThread() {
		while (this.continueRunning) {
			this.tick();
			try {
				Thread.sleep(this.getSleepWait());
			} catch (InterruptedException e) {
				// ignore this error
			}
		}
	}
	
	private long getSleepWait() {
		Long retval = this.getLong("wait_sleep");
		if (retval == null) {
			retval = this.getDefaultSleepWait();
			this.setSleepWait(retval);
		}
		return retval;
	}
	
	private Long getDefaultSleepWait() {
		return this.DEFAULT_SLEEPWAIT;
	}
	
	private void setSleepWait(Long v) {
		this.set("wait_sleep", v.toString());
	}
	
	protected void tick() {
		// TODO: PoolController.tick() - bandwidth requirements
		for (PoolConsumer consumer : this.consumers) {
			consumer.tick();
		}
	}
	
	private PoolConsumer loadObject(String n) {
		PoolConsumer o = null;
		try {
			Class<?> c = Class.forName(n);
			o = (PoolConsumer) c.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// ignore the error
		} catch (ClassNotFoundException e1) {
			// ignore the error
		}
		return o;
		
	}
	
	public String getConsumerName() {
		String n = this.get("consumer_name");
		if (n == null) {
			n = this.getDefaultConsumerName();
			this.setConsumerName(n);
		}
		return n;
	}
	
	protected String getDefaultConsumerName() {
		return this.DEFAULT_CONSUMERNAME;
	}
	
	private void setConsumerName(String n) {
		this.set("consumer_name", n);
	}
	
	public int getPoolSize() {
		Integer i = this.getInteger("pool_size");
		if (i == null) {
			i = this.getDefaultPoolSize();
			this.setPoolSize(i);
		}
		return i.intValue();
	}
	
	protected Integer getDefaultPoolSize() {
		return this.DEFAULT_POOLSIZE;
	}
	
	protected void setPoolSize(Integer i) {
		this.set("pool_size", i.toString());
	}
	
	private PoolConsumer getLeastBusyConsumer() {
		int size = 0;
		int id = 0;
		for (int i=0; i<this.getPoolSize(); i++) {
			PoolConsumer consumer = this.consumers.get(i);
			if (consumer.getQueueSize() == 0) return consumer; // this consumer has nothing pending, so we'll use this
			if ((size == 0) || (size > consumer.getQueueSize())) { 
				size = consumer.getQueueSize(); 
				id = i;
			}
		}
		return this.consumers.get(id);
	}
	
	public void consume(Message msg) {
		this.getLeastBusyConsumer().consume(msg);		
	}
	
	public void terminate() {
		for (PoolConsumer consumer : this.consumers) {
			consumer.terminate();
		}
		super.terminate();
	}
}
