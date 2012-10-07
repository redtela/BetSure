package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Configurable {
	
	private String name = getClass().getName();
	private String path = "config/";
	
	private Properties properties = null;
	
	public Configurable() {
		super();
		loadProperties();
	}
	
	private String getPath() {
		return this.path + this.name + ".properties";
	}
	
	private void loadProperties() {
		String fname = this.getPath();
		this.properties = new Properties();
		File f = new File(fname);
		if (f.exists()) {
			try {
				FileInputStream fis = new FileInputStream(f);
				this.properties.load(fis);
				fis.close();
			} catch (NullPointerException npe) {
				// probably because the file doesn't exist
			} catch (IOException e) {
				// ignore the IOException
			}
		}
	}
	
	protected String get(String key) {
		return this.properties.getProperty(key);
	}
	
	protected void set(String key, String value) {
		this.properties.put(key, value);
	}
	
	protected Long getLong(String key) {
		Long retval = null;
		try {
			retval = new Long(this.get(key));
		} catch (NumberFormatException e) {
			// ignore this
		}
		return retval;
	}
	
	protected Integer getInteger(String key) {
		Integer retval = null;
		try {
			retval = new Integer(this.get(key));
		} catch (NumberFormatException e) {
			// ignore this
		}
		return retval;
	}
	
	protected Boolean getBoolean(String key) {
		return new Boolean(this.get(key));
	}

	public void terminate() {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(this.getPath(), false);
			this.properties.store(fos, null);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			// ignore the error
			e.printStackTrace();
		}
		
	}
}
