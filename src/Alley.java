
public class Alley {
	// Define alley tiles in array
	

	Semaphore alleySem;
	Semaphore topSem;
	Semaphore countMutex;
	int countUp;
	int countDown;

	public Alley() {
		alleySem = new Semaphore(1);
		topSem = new Semaphore(1);
		countMutex = new Semaphore(1);
		countUp = 0;
		countDown = 0;
		
	}
	public void enter(int no) throws InterruptedException {		
		if(no/5 == 0) {			//Choose between up-going and down-going cars
			topSem.P();
			countMutex.P();
			
			if(countUp > 0) {	//first car
				countUp++;
				countMutex.V();
				topSem.V();
			} else {			//any after first car
				countMutex.V();
				alleySem.P();
				topSem.V();
				
				countMutex.P();
				countUp++;
				countMutex.V();
			}
		} else {
			countMutex.P();
			if(countDown > 0) {	//first car
				countDown++;
				countMutex.V();
			} else {			//after first
				countMutex.V();
				alleySem.P();
				
				countMutex.P();
				countDown++;
				countMutex.V();
			}
		}		
	}

	public void leave(int no) throws InterruptedException {
		countMutex.P();
		if(no/5 == 0) {			//choose between directions
			if(countUp > 1) {
				countUp--;
				
			}
			else {
				alleySem.V();	//last car leaving
				countUp--;
				
			}
		} else {
			if(countDown > 1) {
				countDown--;
				
			}
			else {
				alleySem.V();	//last car leaving
				countDown--;
				
			}
		}
		countMutex.V();
	}
}