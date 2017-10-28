
public class Alley {
	// Define alley tiles in array
	

	Semaphore alleySemTop;
	Semaphore alleySemBot;
	Semaphore countSem;
	int countUp;
	int countDown;

	public Alley() {
		alleySemTop = new Semaphore(1);
		alleySemBot = new Semaphore(1);
		countSem = new Semaphore(1);
		countUp = 0;
		countDown = 0;
		
	}

	public void enter(int no) throws InterruptedException {		
		if(no/5<1) {
			alleySemTop.P();
			countSem.P();
			if(countUp>0) {
				countUp++;
			} else {
				alleySemBot.P();
				countUp++;
			}
			countSem.V();
			alleySemTop.V();
		} else {
			alleySemBot.P();
			countSem.P();
			if(countDown>0) {
				countDown++;
			} else {
				alleySemTop.P();
				countDown++;
			}
			countSem.V();
			alleySemBot.V();
		}		
	}

	public void leave(int no) throws InterruptedException {
		countSem.P();
		if(no/5<1) {
			if(countUp > 1) {
				countUp--;
			}
			else {
				alleySemBot.V();
				countUp--;
			}
		} else {
			if(countDown > 1) {
				countDown--;
			}
			else {
				alleySemTop.V();
				countDown--;
			}
		}
		countSem.V();
	}
}