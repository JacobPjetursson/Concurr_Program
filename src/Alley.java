
public class Alley {
	// Define alley tiles in array
	

	Semaphore alleySemTop;
	Semaphore alleySemBot;
	int count;
	
	
	public Alley() {
		alleySemTop = new Semaphore(1);
		alleySemBot = new Semaphore(1);
		count = 0;
		
	}

	
	
	public void enter(int no) throws InterruptedException {
		
		if(no/5<1) {
			alleySemTop.P();
			if(count>0) {
				count++;
				alleySemTop.V();
			} else {
				alleySemBot.P();
				count++;
				alleySemTop.V();
			}
			
			
			
		} else {
			alleySemBot.P();
			if(count>0) {
				count++;
				alleySemBot.V();
			} else {
				alleySemTop.P();
				count++;
				alleySemBot.V();
			}
			
		}		
	}

	public void leave(int no) {
		
		if(count > 1) {
			count--;
		} else {
			if(no/5<1) {
				alleySemBot.V();
				count--;
			} else {
				alleySemTop.V();
				count--;
			}
			
			
		}
	}
	
	public void decrementAbs() {
		if(count > 0) count--; else count++;
	}
}
