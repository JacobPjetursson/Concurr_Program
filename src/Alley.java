
public class Alley {
	// Define alley tiles in array
	Semaphore alleySemTop;
	Semaphore alleySemBot;
	int countUp;
	int countDown;

	public Alley() {
		countUp = 0;
		countDown = 0;
		
	}

	public synchronized void enter(int no) throws InterruptedException {
		if(no/5<1) {
			while(countDown != 0) {
				System.out.println("wut");
				wait();
			}
		} else {
			while(countUp != 0) {
				System.out.println("wut");
				wait();
			}
		}
		
		if(no/5<1) {
			countUp++;
			
		} else {
			countDown++;
		}
	}

	public synchronized void leave(int no) throws InterruptedException {
		if(no/5 < 1) {
			countUp--;
		} else {
			countDown--;
		}
		if(countUp == 0 && countDown == 0) {
			notifyAll();
		}
		
	}
}