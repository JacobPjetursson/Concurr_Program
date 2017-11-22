
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
		if(no/5 == 0) {
			topSem.P();
			countMutex.P();
			
			if(countUp > 0) {
				countUp++;
				countMutex.V();
				topSem.V();
			} else {
				countMutex.V();
				alleySem.P();
				topSem.V();
				
				countMutex.P();
				countUp++;
				countMutex.V();
			}
		} else {
			countMutex.P();
			if(countDown > 0) {
				countDown++;
				countMutex.V();
			} else {
				countMutex.V();
				alleySem.P();
				
				countMutex.P();
				countDown++;
				countMutex.V();
			}
		}		
	}

	public void leave(int no) throws InterruptedException {
		// Here we count down the cars leaving the alley and release the relevant semaphores if the counters reach 0.
		countMutex.P();
		if(no/5 == 0) {
			if(countUp > 1) {
				countUp--;
				
			}
			else {
				alleySem.V();
				countUp--;
				
			}
		} else {
			if(countDown > 1) {
				countDown--;
				
			}
			else {
				alleySem.V();
				countDown--;
				
			}
		}
		countMutex.V();
	}
/*
	public void enter(int no) throws InterruptedException {		
		// For car no 1,2,3,4. We set the bottom semaphore, so that cars entering at bottom can't enter	
		if(no/5 == 0) {
			alleySemTop.P();
			countMutex.P();
			if(countUp == 0 && countDown == 0) {
				alleySemBot.P();
				countUp++;
				System.out.println(countUp + "/" + countDown);
			} else {
				countUp++;
				System.out.println(countUp + "/" + countDown);
			}
			countMutex.V();
			alleySemTop.V();
		// Same as above, but for car 5,6,7,8
		} else {
			alleySemBot.P();
			countMutex.P();
			if(countDown == 0 && countUp == 0) {
				alleySemTop.P();
				countDown++;
				System.out.println(countUp + "/" + countDown);
			} else {
				countDown++;
				System.out.println(countUp + "/" + countDown);
			}
			countMutex.V();
			alleySemBot.V();
		}		
	}

	public void leave(int no) throws InterruptedException {
		// Here we count down the cars leaving the alley and release the relevant semaphores if the counters reach 0.
		countMutex.P();
		if(no/5 == 0) {
			if(countUp == 1) {
				alleySemBot.V();
				countUp--;
				System.out.println(countUp + "/" + countDown);
			}
			else {
				countUp--;
				System.out.println(countUp + "/" + countDown);
			}
		} else {
			if(countDown == 1) {
				alleySemTop.V();
				countDown--;
				System.out.println(countUp + "/" + countDown);
			}
			else {
				countDown--;
				System.out.println(countUp + "/" + countDown);
			}
		}
		countMutex.V();
	}
	*/
}