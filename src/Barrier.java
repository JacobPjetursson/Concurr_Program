
public class Barrier {

	Semaphore barrierSem;
	Semaphore counterSem;
	boolean isBarrierOn;
	int barrierCounter;
	public Barrier () {
		barrierSem = new Semaphore(0);
		counterSem = new Semaphore(1);
		isBarrierOn = false;
		barrierCounter = 0;
	}
	public void sync() throws InterruptedException {
		counterSem.P();
		barrierCounter++;
		if(barrierCounter == 9) {
			barrierSem.V();
		}
		System.out.println(barrierCounter);
		counterSem.V();
		barrierSem.P();
		
		counterSem.P();
		barrierCounter--;
		
		barrierSem.V();
		if(barrierCounter == 0) {
			barrierSem.P();
		}
		counterSem.V();
	}
	
	
	
	public void on() {
		if(!isBarrierOn) {
			isBarrierOn = true;
			barrierSem = new Semaphore(0);
		}
	}
	
	public void off() {
		if(isBarrierOn) {
			isBarrierOn = false;
			int i = barrierCounter;
			while(i>0){
				barrierSem.V();
				i--;
			}	
		}
	}
	
}
