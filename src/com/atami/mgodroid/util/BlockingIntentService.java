package com.atami.mgodroid.util;

import java.util.LinkedList;

import android.app.IntentService;
import android.content.Intent;

/**
 * An extension of the IntentService class that will block
 * an Intent from entering the queue if the same Intent is
 * already in the queue to be run by the Service. A duplicate 
 * Intent is defined by the filterEqual() method. 
 * Blocking, for the purpose of this class, means the duplicate 
 * Intent still resides in IntentService's message queue, 
 * but onHandleIntent will return quickly after identifying 
 * the Intent as duplicate.
 * 
 * Note: when using this class, make sure to override
 * onHandleBlockingIntent instead of onHandleIntent as you
 * would with IntentService.
 * @author Sean
 *
 */
public abstract class BlockingIntentService extends IntentService {
	
	//Intent Action specifying to do nothing when this
	//Action is encountered in onHandleIntent().
	protected static String NO_ACTION = "no_action";
	
	//FIFO queue that mimics what is contained in
	//IntentService's message queue
	private LinkedList<Intent> queue;
	
	public BlockingIntentService(String name) {
		super(name);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		queue = new LinkedList<Intent>();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		for(Intent i: queue){
			if(intent.filterEquals(i)){
				intent.setAction(NO_ACTION);
				break;
			}
		}
		queue.push(intent);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try{
			if(!intent.getAction().equals(NO_ACTION)){
				onHandleBlockingIntent(intent);
			}else{
				throw new DuplicateIntentException();
			}
		}catch(DuplicateIntentException e){
			
		}catch(Exception e){
			
		}finally{
			queue.pop();
		}
	}
	
	protected abstract void onHandleBlockingIntent(Intent intent);
	
	@SuppressWarnings("serial")
	public class DuplicateIntentException extends Exception{}
	
}
