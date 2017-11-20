
public class Alley {
	int countUp;
	int countDown;

	public Alley() {
		countUp = 0;
		countDown = 0;
		
	}

	public synchronized void enter(int no, Semaphore removeCarSem) throws InterruptedException {
		if(no / 5 == 0) {
			while(countDown != 0) {
				wait();
			}
		} else {
			while(countUp != 0) {
				wait();
			}
		}
		removeCarSem.P();
		if(no / 5 == 0) {
			countUp++;
			
		} else {
			
			countDown++;
		}
	}

	public synchronized void leave(int no, Semaphore removeCarSem) throws InterruptedException {
		removeCarSem.P();
		if(no / 5 == 0) {
			countUp--;
		} else {
			countDown--;
		}
		notifyAll();	
	}
	public synchronized void removeCar(int no) {
		if(no / 5 == 0) {
			countUp--;
		} else {
			countDown--;
		}
		notifyAll();
	}
	
}