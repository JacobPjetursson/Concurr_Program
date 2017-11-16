
public class Alley {
	int countUp;
	int countDown;

	public Alley() {
		countUp = 0;
		countDown = 0;
		
	}

	public synchronized void enter(int no) throws InterruptedException {
		if(no/5<1) {
			while(countDown != 0) {
				wait();
			}
		} else {
			while(countUp != 0) {
				wait();
			}
		}
		
		if(no/5 == 0) {
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
	public synchronized void wakeupThreads() {
		notifyAll();
	}
	public synchronized void removeCar(int no) {
		if(no / 5 == 0) {
			countUp--;
		} else {
			countDown--;
		}
	}
	
}