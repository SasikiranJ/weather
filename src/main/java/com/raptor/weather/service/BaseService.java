package com.raptor.weather.service;

import com.raptor.weather.context.ProcessingContext;

abstract class BaseService {

	protected BaseService nextService;

	public void setNextHandler(BaseService nextHandler) {
		this.nextService = nextHandler;
	}

	public abstract void process(ProcessingContext context);
}

