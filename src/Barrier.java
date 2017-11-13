
public class Barrier {
	boolean isBarrierOn;
	boolean shutdown;
	int barrierCounter;
	public Barrier () {
		isBarrierOn = false;
		barrierCounter = 0;
	}
	public synchronized void sync() throws InterruptedException {
		barrierCounter++;
		System.out.println(barrierCounter);
		if(barrierCounter == 9) {
			notifyAll();
			if(shutdown) {
				isBarrierOn = false;
			}
		} else {
			wait();
		}
		barrierCounter--;
	}
	
	
	
	public synchronized void on() {
		if(!isBarrierOn) {
			isBarrierOn = true;
		}
		shutdown = false;
	}
	
	public synchronized void off() {
		if(isBarrierOn) {
			isBarrierOn = false;
			notifyAll();
		}
	}
	public void shutdown() {
		if(!shutdown) {
			shutdown = true;
		}
	}
	
}
