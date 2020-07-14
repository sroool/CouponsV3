package com.example.CouponsV3.jobs;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.CouponsV3.web.Session;

@Service
public class CustomerLastActionJob implements Runnable {
	
	@Autowired
	private Map<String, Session> sessions;
	private boolean quit;
	
	@Override
	public void run() {
		while(!quit) {
			
			Map<String, Session> copySession = new HashMap<String, Session>(sessions);
			for(Map.Entry<String, Session> entry : copySession.entrySet()) {
				Session session = entry.getValue();
				long now = System.currentTimeMillis();
				long noActionTimeLimit = 1000 * 60 * 30;
				if(now - session.getLastActionTime() >= noActionTimeLimit ) {
					System.out.println(entry.getKey() + " passed the time limit, deleting");
					sessions.remove(entry.getKey());
				}
			}
			try {
				Thread.sleep(1000 * 60 );
			} catch (InterruptedException e) {}
		}
	}
	public void stop() {
		quit = true;
	}

}
