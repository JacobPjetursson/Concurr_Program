
public class Alley {
	// Define alley tiles in array
	

	Semaphore alleySemTop;
	Semaphore alleySemBot;
	Semaphore countMutex;
	int countUp;
	int countDown;

	public Alley() {
		alleySemTop = new Semaphore(1);
		alleySemBot = new Semaphore(1);
		countMutex = new Semaphore(1);
		countUp = 0;
		countDown = 0;
		
	}

	public void enter(int no) throws InterruptedException {		
		// For car no 1,2,3,4. We set the bottom semaphore, so that cars entering at bottom can't enter	
		if(no/5 == 0) {
			alleySemTop.P();
			countMutex.P();
			if(countUp > 0) {
				countUp++;
				System.out.println(countUp + "/" + countDown);
			} else {
				alleySemBot.P();
				countUp++;
				System.out.println(countUp + "/" + countDown);
			}
			countMutex.V();
			alleySemTop.V();
		// Same as above, but for car 5,6,7,8
		} else {
			alleySemBot.P();
			countMutex.P();
			if(countDown>0) {
				countDown++;
				System.out.println(countUp + "/" + countDown);
			} else {
				alleySemTop.P();
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
			countUp--;
			if(countUp == 0) {
				alleySemBot.V();
				System.out.println(countUp + "/" + countDown);
			}
		} else {
			countDown--;
			if(countDown == 0) {
				alleySemTop.V();
				System.out.println(countUp + "/" + countDown);
			}
			

		}
		countMutex.V();
	}
}