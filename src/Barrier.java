
public class Barrier {

	Semaphore barrierSem;
	Semaphore counterSem;
	Semaphore gateSem;
	boolean isBarrierOn;
	int barrierCounter;
	int prevCounter;
	public Barrier () {
		barrierSem = new Semaphore(0);
		counterSem = new Semaphore(1);
		gateSem = new Semaphore(9);
		isBarrierOn = false;
		barrierCounter = 0;
		prevCounter = 0;
	}
	public void sync() throws InterruptedException {
		gateSem.P();
		counterSem.P();
		barrierCounter++;
		if(barrierCounter == 9) {
			barrierSem.V();
		}
		
		System.out.println(barrierCounter);
		if(barrierCounter == 1 && prevCounter != 9) {
			System.out.println("ERRORS");
		} else if(barrierCounter != 1 && barrierCounter != prevCounter +1) {
			System.out.println("ERRORS");
		}
		prevCounter = barrierCounter;
		counterSem.V();
		barrierSem.P();
		
		counterSem.P();
		barrierCounter--;
		
		barrierSem.V();
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
