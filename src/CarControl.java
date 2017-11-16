//Prototype implementation of Car Control
//Mandatory assignment
//Course 02158 Concurrent Programming, DTU, Fall 2017

//Hans Henrik Lovengreen     Oct 9, 2017


import java.awt.Color;

class Gate {
    Semaphore g = new Semaphore(0);
    Semaphore e = new Semaphore(1);
    boolean isopen = false;

    public void pass() throws InterruptedException {
        //CarControl.activeCarCounter--;
        //System.out.println("carCounter BEFORE: " + CarControl.activeCarCounter);
    	g.P(); 
    	//CarControl.activeCarCounter++;
    	//System.out.println("carCounter AFTER: " + CarControl.activeCarCounter);
        g.V();
        
    }

    public void open() {
        try { e.P(); } catch (InterruptedException e) {}
        if (!isopen) { g.V();  isopen = true; }
        e.V();
        
        
    }

    public void close() {
        try { e.P(); } catch (InterruptedException e) {}
        if (isopen) { 
            try { g.P(); } catch (InterruptedException e) {}
            isopen = false;
        }
        e.V();
    }

}

class Car extends Thread {
	
    int basespeed = 1;             // Rather: degree of slowness
    int variation =  50;             // Percentage of base speed

    CarDisplayI cd;                  // GUI part

    int no;                          // Car number
    Pos startpos;                    // Startpositon (provided by GUI)
    Pos barpos;                      // Barrierpositon (provided by GUI)
    Color col;                       // Car  color
    Gate mygate;                     // Gate at startposition


    int speed;                       // Current car speed
    Pos curpos;                      // Current position 
    Pos newpos;                      // New position to go to

    public Car(int no, CarDisplayI cd, Gate g) {
    	
        this.no = no;
        this.cd = cd;
        mygate = g;
        startpos = cd.getStartPos(no);
        barpos = cd.getBarrierPos(no);  // For later use

        col = chooseColor();

        // do not change the special settings for car no. 0
        if (no==0) {
            basespeed = 0;  
            variation = 0; 
            setPriority(Thread.MAX_PRIORITY); 
        }
    }

    public synchronized void setSpeed(int speed) { 
        if (no != 0 && speed >= 0) {
            basespeed = speed;
        }
        else
            cd.println("Illegal speed settings");
    }

    public synchronized void setVariation(int var) { 
        if (no != 0 && 0 <= var && var <= 100) {
            variation = var;
        }
        else
            cd.println("Illegal variation settings");
    }

    synchronized int chooseSpeed() { 
        double factor = (1.0D+(Math.random()-0.5D)*2*variation/100);
        return (int) Math.round(factor*basespeed);
    }

    private int speed() {
        // Slow down if requested
        final int slowfactor = 3;  
        return speed * (cd.isSlow(curpos)? slowfactor : 1);
    }

    Color chooseColor() { 
        return Color.blue; // You can get any color, as longs as it's blue 
    }

    Pos nextPos(Pos pos) {
        // Get my track from display
        return cd.nextPos(no,pos);
    }

    boolean atGate(Pos pos) {
        return pos.equals(startpos);
    }

   public void run() {
        try {

            speed = chooseSpeed();
            curpos = startpos;
            cd.mark(curpos,col,no);

            while (true) { 
                sleep(speed());
  
                if (atGate(curpos)) { 
                    mygate.pass(); 
                    speed = chooseSpeed();
                }
                newpos = nextPos(curpos);
                
                //if(newpos.col == 0 && // call newpos er start af alley ) {
                //	alley.enter();
                //}
                // Få en bil til at køre igennem alley
                //  Move to new position
                
                if(		(newpos.row==1 && newpos.col==2)&&no/5<1 || 
                		(newpos.row==2 && newpos.col==0)&&(curpos.row==2&&curpos.col==1) ||
                		(newpos.row==9 && newpos.col==0)&&no/5==1
                		)
                	CarControl.alley.enter(no);
                
                if(		(curpos.row == 6 && no/5 <1) && CarControl.barrier.isBarrierOn && newpos.col != 0 && curpos.col != 2 ||
                		(curpos.row == 5 && no/5 ==1) && CarControl.barrier.isBarrierOn && newpos.col != 0 && curpos.col != 2
                		) CarControl.barrier.sync();
                
                CarControl.sems[newpos.row][newpos.col].P();
                cd.clear(curpos);
                cd.mark(curpos,newpos,col,no);
                sleep(speed());
                cd.clear(curpos,newpos);
                cd.mark(newpos,col,no);
                
                // Own code here
                Pos tempPos = curpos;
                curpos = newpos;
                CarControl.sems[tempPos.row][tempPos.col].V();
                if(		(tempPos.row==9 && tempPos.col==0)&&no/5<1||
                		(tempPos.row==1&&tempPos.col==2)&&no/5==1) 
                	CarControl.alley.leave(no);
            }

        } catch (Exception e) {
            cd.println("Exception in Car no. " + no);
            System.err.println("Exception in Car no. " + no + ":" + e);
            e.printStackTrace();
        }
    }
   
   

}

public class CarControl implements CarControlI{

    CarDisplayI cd;           // Reference to GUI
    Car[]  cars;               // Cars
    Gate[] gate;              // Gates
    static Semaphore[][] sems;
    static Alley alley; 
    static Barrier barrier;
    //static int activeCarCounter;

    public CarControl(CarDisplayI cd) {
        this.cd = cd;
        cars  = new  Car[9];
        gate = new Gate[9];
        sems = new Semaphore[11][12];
        alley = new Alley();
        barrier = new Barrier();
        //activeCarCounter = 9;
        
        for (int no = 0; no < 9; no++) {
            gate[no] = new Gate();
            cars[no] = new Car(no,cd,gate[no]);
            cars[no].start();
        } 
        for(int i = 0; i < sems.length; i++) {
        	for(int j = 0; j < sems[i].length; j++) {
        		sems[i][j] = new Semaphore(1);
        	}
        }
        
        
    }

   public void startCar(int no) {
        gate[no].open();
    }

    public void stopCar(int no) {
        gate[no].close();
    }

    public void barrierOn() { 
    	barrier.on();
        //cd.println("Barrier On not implemented in this version");
    }

    public void barrierOff() { 
        barrier.off();
    	//cd.println("Barrier Off not implemented in this version");
    }

    public void barrierShutDown() { 
        cd.println("Barrier shut down not implemented in this version");
        // This sleep is for illustrating how blocking affects the GUI
        // Remove when shutdown is implemented.
        try { Thread.sleep(3000); } catch (InterruptedException e) { }
        // Recommendation: 
        //   If not implemented call barrier.off() instead to make graphics consistent
    }

    public void setLimit(int k) { 
        cd.println("Setting of bridge limit not implemented in this version");
    }

    public void removeCar(int no) { 
        cd.println("Remove Car not implemented in this version");
    }

    public void restoreCar(int no) { 
        cd.println("Restore Car not implemented in this version");
    }

    /* Speed settings for testing purposes */

    public void setSpeed(int no, int speed) { 
        cars[no].setSpeed(speed);
    }

    public void setVariation(int no, int var) { 
        cars[no].setVariation(var);
    }

}






