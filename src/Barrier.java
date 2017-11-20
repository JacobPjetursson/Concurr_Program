
public class Barrier {

	Semaphore syncSem;
	Semaphore counterSem;
	Semaphore barrierSem;
	boolean isBarrierOn;
	int barrierCounter;
	int prevCounter;
	public Barrier () {
		/* We use 3 semaphores in total, 1 for managing critical sections around the barrier counter (counterSem),
		 * one for forbidding cars from entering the barrier before all cars have left it (gateSem),
		 * and one for stopping the cars at the barrier (barrierSem)
		 */
		syncSem = new Semaphore(0);
		counterSem = new Semaphore(1);
		barrierSem = new Semaphore(9);
		isBarrierOn = false;
		barrierCounter = 0;
	}
	public void sync() throws InterruptedException {
		barrierSem.P();
		counterSem.P();
		barrierCounter++;
		if(barrierCounter == 9) {
			syncSem.V();
		}
		counterSem.V();
		syncSem.P();
		
		counterSem.P();
		barrierCounter--;
		
		syncSem.V();
		// All cars are allowed to enter the barrier again
		if(barrierCounter == 0) {
			syncSem.P();
			for(int i = 0; i<9;i++) barrierSem.V();
		}
		counterSem.V();
	}
	
	
	
	public void on() {
		if(!isBarrierOn) {
			isBarrierOn = true;
			syncSem = new Semaphore(0);
		}
	}
	
	public void off() {
		if(isBarrierOn) {
			int i = barrierCounter;
			while(i>0){
				syncSem.V();
				i--;
			}	
		}
		isBarrierOn = false;
	}
	
}
