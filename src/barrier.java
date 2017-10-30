
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
			int i = barrierCounter;
			while(i>0){
				barrierSem.V();
				i--;
			}
		}
		counterSem.V();
		System.out.println(barrierCounter);
		barrierSem.P();
		barrierCounter--;
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
