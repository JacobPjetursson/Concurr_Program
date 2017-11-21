import java.util.Random;

//Prototype implementation of Car Test class
//Mandatory assignment
//Course 02158 Concurrent Programming, DTU, Fall 2017

//Hans Henrik Lovengreen     Oct 9, 2017

public class CarTest extends Thread {

    CarTestingI cars;
    int testno;

    public CarTest(CarTestingI ct, int no) {
        cars = ct;
        testno = no;
    }

    public void run() {
        try {
            switch (testno) { 
            case 0:
                // Demonstration of startAll/stopAll.
                // Should let the cars go one round (unless very fast)
                cars.startAll();
                sleep(3000);
                cars.stopAll();
                break;
            // Set a fast speed for all cars 
            case 1:
            	cars.println("Setting fast speed for all cars");
            	cars.startAll();
                for (int i = 1; i < 9; i++) {
                    cars.setSpeed(i, 1);
                }
                break;
            // Remove and restore random cars
            case 2:
            	cars.println("Removing and restoring cars at random");
            	cars.startAll();
            	for (int i = 1; i < 9; i++) {
            		cars.setSpeed(i, 1);
            	}
            	for (int j = 0; j < 100; j++) {
            		sleep(300);
            		int rand = 1 + new Random().nextInt(8);
            		cars.removeCar(rand);
            		
            		sleep(300);
            		
            		cars.restoreCar(rand);
            		cars.setSpeed(rand, 1);
            	}
            	break;
            case 19:
                // Demonstration of speed setting.
                // Change speed to double of default values
                cars.println("Doubling speeds");
                for (int i = 1; i < 9; i++) {
                    cars.setSpeed(i,50);
                };
                break;

            default:
                cars.println("Test " + testno + " not available");
            }

            cars.println("Test ended");

        } catch (Exception e) {
            System.err.println("Exception in test: "+e);
        }
    }

}



