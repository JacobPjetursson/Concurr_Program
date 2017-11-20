
public class Barrier {
	boolean isBarrierOn;
	boolean shutdown;
	boolean release;
	int barrierCounter;
	int amountOfCars;
	public Barrier () {
		isBarrierOn = false;
		release = false;
		barrierCounter = 0;
		amountOfCars = 9;

	}
	public synchronized void sync(Semaphore removeCarSem) throws InterruptedException {
		barrierCounter++;
//		System.out.println(barrierCounter);
		removeCarSem.V();
		while(barrierCounter != amountOfCars && !release) {
			wait();
		}
		removeCarSem.P();
		if(barrierCounter == amountOfCars) {
			release = true;
			notifyAll();
			if(shutdown) {
				isBarrierOn = false;
			}
		} 
		
		barrierCounter--;
		if(barrierCounter == 0) {
			release = false;
		}
	}
	
	public synchronized void on() {
		if(!isBarrierOn) {
			isBarrierOn = true;
			release = false;
		}
		shutdown = false;
	}
	
	public synchronized void off() {
		if(isBarrierOn) {
			isBarrierOn = false;
			release = true;
			notifyAll();
		}
	}
	public synchronized void shutdown() {
		if(!shutdown) {
			shutdown = true;
		}
	}
	public synchronized void removeCar(boolean atBarrier) {
		if(atBarrier && isBarrierOn) {
			barrierCounter--;
		}
		amountOfCars--;
		notifyAll();
	}
	public synchronized void addCar() {
		amountOfCars++;
		notifyAll();
	}
}
