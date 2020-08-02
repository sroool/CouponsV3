package com.example.CouponsV3.jobs;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.CouponsV3.web.Session;

@Service
public class CustomerLastActionJob implements Runnable {
	
	@Autowired
	private Map<String, Session> sessions;
	private boolean quit; // a switch used to kill the last action job
	private final long NO_ACTION_TIME_LIMIT = 1000 * 60 * 30; // a constant dictating how long can a user be inactive
	
	@Override
	public void run() {
		while(!quit) {
			
			Map<String, Session> copySession = new HashMap<String, Session>(sessions); // creates a copy of the sessions map to avoid 
																					   // concurrency issues
			for(Map.Entry<String, Session> entry : copySession.entrySet()) {           // loop over the entries
				Session session = entry.getValue();
				long now = System.currentTimeMillis();
				if(now - session.getLastActionTime() >= NO_ACTION_TIME_LIMIT ) {           // check if a session is past the noActionTimeLimit
					sessions.remove(entry.getKey()); 									// remove if so
				}
			}
			try {//sleep 
				Thread.sleep(1000 * 60 );
			} catch (InterruptedException e) {}
		}
	}
	public void stop() {
		quit = true;
	}

}
