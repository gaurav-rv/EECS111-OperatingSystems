package nachos.threads;

import java.util.LinkedList;

import nachos.machine.*;

/**
 * An implementation of condition variables that disables interrupt()s for
 * synchronization.
 * 
 * <p>
 * You must implement this.
 * 
 * @see nachos.threads.Condition
 */
public class Condition2 {
	/**
	 * Allocate a new condition variable.
	 * 
	 * @param conditionLock the lock associated with this condition variable.
	 * The current thread must hold this lock whenever it uses <tt>sleep()</tt>,
	 * <tt>wake()</tt>, or <tt>wakeAll()</tt>.
	 */
	
	private LinkedList<KThread> waitQ;
	
	public Condition2(Lock conditionLock) {
		this.conditionLock = conditionLock;
		waitQ = new LinkedList<KThread>();
	}
	
	
	/**
	 * Atomically release the associated lock and go to sleep on this condition
	 * variable until another thread wakes it using <tt>wake()</tt>. The current
	 * thread must hold the associated lock. The thread will automatically
	 * reacquire the lock before <tt>sleep()</tt> returns.
	 */
	public void sleep() {
		Lib.assertTrue(conditionLock.isHeldByCurrentThread());
		
		Machine.interrupt().disable();
		conditionLock.release();
		KThread kthread = KThread.currentThread();
		waitQ.add(kthread);
		KThread.sleep();
		conditionLock.acquire();
		Machine.interrupt().enable();
		
	}

	/**
	 * Wake up at most one thread sleeping on this condition variable. The
	 * current thread must hold the associated lock.
	 */
	public void wake() {
		Lib.assertTrue(conditionLock.isHeldByCurrentThread());
		
		Machine.interrupt().disable(); 
		
		if (!waitQ.isEmpty()){
			((KThread) waitQ.removeFirst()).ready(); 
		}
		Machine.interrupt().enable(); 
		
	}

	/**
	 * Wake up all threads sleeping on this condition variable. The current
	 * thread must hold the associated lock.
	 */
	public void wakeAll() {
		Lib.assertTrue(conditionLock.isHeldByCurrentThread());
		
		while(!waitQ.isEmpty()){
			wake();
		}
	}

	private Lock conditionLock;
}
