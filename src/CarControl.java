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
    	g.P(); 
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
        	this.speed = speed;
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
    boolean atAlleyEntrance() {
    	return (curpos.row==1 && curpos.col==3 && no/5 == 0) || 
        	   (curpos.row==2 && curpos.col==1 && no/5 == 0) ||
        	   (curpos.row==10 && curpos.col==0 && no/5 == 1);
    }
    boolean atAlleyExit() {
    	return (curpos.row==9 && curpos.col==1 && no/5 == 0) ||
        (curpos.row==0 && curpos.col==2 && no/5==1); 
    }
    boolean atBarrierEntrance() {
    	return (curpos.row == barpos.row && curpos.col == barpos.col &&
        		CarControl.barrier.isBarrierOn);
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
                // Own code here
                if(atAlleyEntrance()) {
                	CarControl.alley.enter(no);
                }
                if(atAlleyExit()) {
                	CarControl.alley.leave(no);
                }
                
                if(atBarrierEntrance()) {
                	CarControl.barrier.sync();
                }
                // Before doing the movement below, we set the semaphore corresponding to the new position of the car
                CarControl.sems[newpos.row][newpos.col].P();
                cd.clear(curpos);
                cd.mark(curpos,newpos,col,no);
                sleep(speed());
                cd.clear(curpos,newpos);
                cd.mark(newpos,col,no);
                CarControl.sems[curpos.row][curpos.col].V();
                
                curpos = newpos;
            }

        } catch (Exception e) {
        	/*
            cd.println("Exception in Car no. " + no);
            System.err.println("Exception in Car no. " + no + ":" + e);
            e.printStackTrace();
            */
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


    public CarControl(CarDisplayI cd) {
        this.cd = cd;
        cars  = new  Car[9];
        gate = new Gate[9];
        sems = new Semaphore[11][12];
        alley = new Alley();
        barrier = new Barrier();

        
        
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
    }

    public void barrierOff() { 
        barrier.off();
    }

    public void barrierShutDown() { 
       cd.println("Shutdown not implemented in this version");
    }

    public void setLimit(int k) { 
        cd.println("Setting of bridge limit not implemented in this version");
    }

    public void removeCar(int no) { 
    	cd.println("Remove car not implemented in this version");
    }

    public void restoreCar(int no) { 
    	cd.println("Restore car not implemented in this version");
    }

    /* Speed settings for testing purposes */

    public void setSpeed(int no, int speed) { 
        cars[no].setSpeed(speed);        
    }

    public void setVariation(int no, int var) { 
        cars[no].setVariation(var);
    }

}