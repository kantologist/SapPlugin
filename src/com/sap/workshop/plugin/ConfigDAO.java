package com.sap.workshop.plugin;

public interface ConfigDAO {
	int getProperty(String key, int defaultValue);
}
