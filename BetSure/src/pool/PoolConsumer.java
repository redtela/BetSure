package pool;

import java.util.Vector;

import message.Message;
import config.Configurable;

public class PoolConsumer extends Configurable {

	private Vector<Message> queue = new Vector<Message>();
	
	public PoolConsumer() {
		super();
	}
	
	public int getQueueSize() {
		return this.queue.size();
	}
	
	private synchronized Vector<Message> getQueue() {
		return this.queue;
	}
	
	public void consume(Message msg) {
		this.getQueue().add(msg);
	}
	
	public void tick() {
		// TODO: PoolConsumer.tick() - bandwidth management
	}

}
