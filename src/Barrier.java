
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
	public synchronized void sync(Semaphore removeCarSem) throws InterruptedException {
		barrierCounter1++;
		
		removeCarSem.V();
		while(barrierCounter1 != amountOfCars && !release1) {
			wait();
		}
		removeCarSem.P();
		if(barrierCounter1 == amountOfCars) {
			release1 = true;
			notifyAll();
			if(shutdown) {
				isBarrierOn = false;
			}
		} 
		barrierCounter1--;
		if(barrierCounter1 == 0) {
			release1 = false;
		}
		
		barrierCounter2++;
		removeCarSem.V();
		while(barrierCounter2 != amountOfCars && !release2) {
			wait();
		}
		removeCarSem.P();
		if(barrierCounter2 == amountOfCars) {
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
	public synchronized void removeCar(boolean atBarrier) {
		if(atBarrier && isBarrierOn) {
			barrierCounter1--;
			barrierCounter2--;
		}
		amountOfCars--;
		notifyAll();
	}
	public synchronized void addCar() {
		amountOfCars++;
		notifyAll();
	}
}
