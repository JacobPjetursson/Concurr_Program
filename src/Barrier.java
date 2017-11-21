
public class Barrier {
	boolean isBarrierOn;
	boolean shutdown;
	boolean release1;
	boolean release2;
	int barrierCounter1;
	int barrierCounter2;
	int amountOfCars;
	public Barrier () {
		isBarrierOn = false;
		release1 = false;
		release2 = false;
		barrierCounter1 = 0;
		barrierCounter2 = 0;
		amountOfCars = 9;

	}
	//This function is an implementation of a two-phased barrier, but in a monitor setting.
	//Therefore almost have the exact same code twice, being the two phases.
	public synchronized void sync() throws InterruptedException {
		barrierCounter1++;		//Phase 1
		
		while(barrierCounter1 != 9 && !release1 && isBarrierOn) {
			wait();
		}
		// removeCarSem is here to manage proper removal of cars by enclosing the counters with the boolean set
		// in the CarControl class
		
		if(barrierCounter1 == 9) {
			release1 = true;
			notifyAll();
		} 
		barrierCounter1--;
		if(barrierCounter1 == 0) {
			release1 = false;
		}
		
		barrierCounter2++;		//Phase 2
		while(barrierCounter2 != 9 && !release2 && isBarrierOn) {
			wait();
		}
		
		if(barrierCounter2 == 9) {
			release2 = true;
			notifyAll();
			if(shutdown) {
				isBarrierOn = false;
			}
		} 
		barrierCounter2--;
		if(barrierCounter2 == 0) {
			release2 = false;
		}
		
	}
	
	public synchronized void on() {
		if(!isBarrierOn) {
			isBarrierOn = true;
			release1 = false;
			release2 = false;
		}
		shutdown = false;
	}
	
	public synchronized void off() {
		if(isBarrierOn) {
			isBarrierOn = false;
			release1 = true;
			release2 = true;
			notifyAll();
		}
	}
	public synchronized void shutdown() {
		if(!shutdown) {
			shutdown = true;
		}
	}
}
