
public class Barrier {

	Semaphore barrierSem;
	Semaphore counterSem;
	Semaphore gateSem;
	boolean isBarrierOn;
	int barrierCounter;
	int prevCounter;
	public Barrier () {
		/* We use 3 semaphores in total, 1 for managing critical sections around the barrier counter (counterSem),
		 * one for forbidding cars from entering the barrier before all cars have left it (gateSem),
		 * and one for stopping the cars at the barrier (barrierSem)
		 */
		barrierSem = new Semaphore(0);
		counterSem = new Semaphore(1);
		gateSem = new Semaphore(9);
		isBarrierOn = false;
		barrierCounter = 0;
	}
	public void sync() throws InterruptedException {
		gateSem.P();
		counterSem.P();
		barrierCounter++;
		if(barrierCounter == 9) {
			barrierSem.V();
		}
		counterSem.V();
		barrierSem.P();
		
		counterSem.P();
		barrierCounter--;
		
		barrierSem.V();
		// All cars are allowed to enter the barrier again
		if(barrierCounter == 0) {
			barrierSem.P();
			for(int i = 0; i<9;i++) gateSem.V();
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
			int i = barrierCounter;
			while(i>0){
				barrierSem.V();
				i--;
			}	
		}
		isBarrierOn = false;
	}
	
}
