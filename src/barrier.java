
public class barrier {

	Semaphore barrierSem;
	boolean isBarrierOn;
	int counter;
	public barrier () {
		barrierSem = new Semaphore(1);
		isBarrierOn = false;
		counter = 0;
	}
	
	public void sync() {
		
	}
	
	public void on() {
		isBarrierOn = true;
	}
	
	public void off() {
		isBarrierOn = false;
	}
	
}
