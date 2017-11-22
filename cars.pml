/* DTU Course 02158 Concurrent Programming
 *   Mandatory Assignment
 *   Cars.pml
 *     Simple PROMELA model 
	Instead of direction, we changed it to being top and bottom 
	entrance location, which matched our java code better
 */

pid top1, top2, top3, top4, bot1, bot2, bot3, bot4;
byte TOP = 1;
byte BOT = 0;

byte semTop = 1;
byte semAlley = 1;
byte semCounter = 1;

int inAlleyTop = 0;
int inAlleyBot = 0;

init {
	atomic {
		top1 = run Car(TOP);
		top2 = run Car(TOP);
		bot1 = run Car(BOT);
		bot2 = run Car(BOT);
	}  
}

inline P(semaphore) {
	atomic{semaphore > 0 -> semaphore--;}
}
inline V(semaphore) {
	atomic{semaphore++;}
}

active proctype Car (byte loc) 
{
	do
	::	/* First statement is a dummy to allow a label at start */
		skip; 

entry:	
		if
		:: (loc == TOP) ->
			P(semTop);
			P(semCounter);
			if
			:: (inAlleyTop > 0) ->
				inAlleyTop++;
				V(semCounter);
				V(semTop);
			:: (inAlleyTop <= 0) ->
				V(semCounter);
				P(semAlley);
				V(semTop);
				
				P(semCounter);
				inAlleyTop++;
				V(semCounter);
			fi;

		:: (loc == BOT) ->
			P(semCounter);
			if
			:: (inAlleyBot > 0) ->
				inAlleyBot++;
				V(semCounter);
			:: (inAlleyBot <= 0) ->
				V(semCounter);
				P(semAlley);

				P(semCounter);
				inAlleyBot++;
				V(semCounter);
			fi;
		fi;

crit:	
		assert(!(inAlleyBot > 0 && inAlleyTop > 0));
exit: 
		P(semCounter);
		if
		:: (loc == TOP) ->
			if
			:: (inAlleyTop > 1) ->
				inAlleyTop--;
			:: (inAlleyTop <= 1) ->
				V(semAlley);
				inAlleyTop--;
			fi
		:: (loc == BOT) ->
			if
			:: (inAlleyBot > 1) ->
				inAlleyBot--;
			:: (inAlleyBot <= 1) ->
				V(semAlley);
				inAlleyBot--;
			fi
			
		fi;
		V(semCounter);
	od;
}

//ltl fair1 { [] ( (Car[top1]@entry) -> <>  (Car[top1]@crit) ) } 
//ltl fair2 { [] ( (Car[top2]@entry) -> <>  (Car[top2]@crit) ) }
//ltl fair3 { [] ( (Car[bot1]@entry) -> <>  (Car[bot1]@crit) ) }
//ltl fair4 { [] ( (Car[bot2]@entry) -> <>  (Car[bot2]@crit) ) }