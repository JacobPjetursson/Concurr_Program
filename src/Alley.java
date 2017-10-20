
public class Alley {
	// Define alley tiles in array
	

	Semaphore alleySem;
	int count;
	
	
	public Alley() {
		alleySem = new Semaphore(1);
		count = 0;
		
	}

	
	
	public void enter(int no) throws InterruptedException {
		
		if(no/5<1 && count>0) {
			count++;
			return;
		} else if(no/5==1 && count<0) {
			count--;
			return;
		}
		alleySem.P();
		
		if(no/5<1) count++; else count--;
		
		
	}

	public void leave(int no) {
		
		if(Math.abs(count) != 1) {
			if(count > 0) count--; else count++;
			
		} else {
			alleySem.V();
			if(count > 0) count--; else count++;
		}
	}
	
	public void decrementAbs() {
		if(count > 0) count--; else count++;
	}
}
