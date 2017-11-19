
public class Alley {
	int countUp;
	int countDown;
	Semaphore removeCarSem;

	public Alley() {
		countUp = 0;
		countDown = 0;
		removeCarSem = new Semaphore(1);
		
	}

	public synchronized void enter(int no) throws InterruptedException {
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

	public synchronized void leave(int no) throws InterruptedException {
		removeCarSem.P();
		if(no / 5 == 0) {
			countUp--;
		} else {
			countDown--;
		}
		removeCarSem.V();
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