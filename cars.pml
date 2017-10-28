/* DTU Course 02158 Concurrent Programming
 *   Mandatory Assignment
 *   Cars.pml
 *     Simple PROMELA model 
	Instead of direction, we changed it to being top and bottom 
	entrance location, which matched our java code better
 */

pid top1, top2, bot1, bot2;
byte TOP = 1;
byte BOT = 0;
byte semTop = 1;
byte semBot = 1;
byte semCounter = 1;

int inAlleyTop = 0;
int inAlleyBot = 0;

init {
	atomic {
		top1 = run Car(TOP);
		bot1 = run Car(BOT);
		top2 = run Car(TOP);
		bot2 = run Car(BOT);

	}  
}

inline P(semaphore) {
	atomic{
		if 
		:: (semaphore > 0) -> semaphore--;
		:: (semaphore <= 0) -> break;
		fi;
	}
}
inline V(semaphore) {
	atomic{semaphore++}
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
			:: (inAlleyTop <= 0) ->
				P(semBot);
				inAlleyTop++;
			fi;
			V(semCounter);
			V(semTop);
		:: (loc == BOT) ->
			P(semBot);
			P(semCounter);
			if
			:: (inAlleyBot > 0) ->
				inAlleyBot++;
			:: (inAlleyBot <= 0) ->
				P(semTop);
				inAlleyBot++;
			fi;
			V(semCounter);
			V(semBot);
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
				V(semBot);
				inAlleyTop--;
			fi;
		:: (loc == BOT) ->
			if
			:: (inAlleyBot > 1) ->
				inAlleyBot--;
			:: (inAlleyBot <= 1) ->
				V(semTop);
				inAlleyBot--;
			fi;
		fi;
		V(semCounter);
	od;
}