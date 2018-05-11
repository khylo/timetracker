package com.khylo.timetracker;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class StaticDataFactory  implements ApplicationContextAware  {
	ApplicationContext applicationContext;
	private boolean called = false;
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	public void createAll() {
		if(!called)
			createAll(false);
	}
	public void createAll(boolean force) {
		called=true;
    	Map<String, StaticData> customBeans =  applicationContext.getBeansOfType(StaticData.class);
    	for(Map.Entry<String, StaticData> entry :customBeans.entrySet()){
    		StaticData bean = entry.getValue();
            bean.createData();
    	}
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		this.applicationContext = arg0;
		
	}

}
