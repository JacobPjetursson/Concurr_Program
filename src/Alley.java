
public class Alley {
	// Define alley tiles in array
	

	Semaphore alleySemTop;
	Semaphore alleySemBot;
	Semaphore countSem;
	int count;
	
	
	public Alley() {
		alleySemTop = new Semaphore(1);
		alleySemBot = new Semaphore(1);
		countSem = new Semaphore(1);
		count = 0;
		
	}

	
	
	public void enter(int no) throws InterruptedException {
		
		if(no/5<1) {
			alleySemTop.P();
			countSem.P();
			if(count>0) {
				
				count++;
				countSem.V();
				alleySemTop.V();
			} else {
				alleySemBot.P();
				
				count++;
				countSem.V();
				alleySemTop.V();
			}
		
			
			
		} else {
			alleySemBot.P();
			countSem.P();
			if(count>0) {
				
				count++;
				countSem.V();
				alleySemBot.V();
			} else {
				alleySemTop.P();
				
				count++;
				countSem.V();
				alleySemBot.V();
			}
			
		}		
	}

	public void leave(int no) throws InterruptedException {
		countSem.P();
		if(count > 1) {
			
			count--;
			countSem.V();
		} else {
			if(no/5<1) {
				alleySemBot.V();
				
				count--;
				countSem.V();
			} else {
				alleySemTop.V();
				
				count--;
				countSem.V();
			}
			
			
		}
	}
	
	
}
