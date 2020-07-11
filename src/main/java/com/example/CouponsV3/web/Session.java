package com.example.CouponsV3.web;

import com.example.CouponsV3.facades.ClientFacade;

public class Session {
	private ClientFacade clientFacade;
	private long lastActionTime;
	/**
	 * @param clientFacade
	 * @param lastActionTime
	 */
	public Session(ClientFacade clientFacade, long lastActionTime) {
		super();
		this.clientFacade = clientFacade;
		this.lastActionTime = lastActionTime;
	}
	/**
	 * @return the clientFacade
	 */
	public ClientFacade getClientFacade() {
		return clientFacade;
	}
	/**
	 * @param clientFacade the clientFacade to set
	 */
	public void setClientFacade(ClientFacade clientFacade) {
		this.clientFacade = clientFacade;
	}
	/**
	 * @return the lastActionTime
	 */
	public long getLastActionTime() {
		return lastActionTime;
	}
	/**
	 * @param lastActionTime the lastActionTime to set
	 */
	public void setLastActionTime(long lastActionTime) {
		this.lastActionTime = lastActionTime;
	}
	
}
