package cn.edu.pku.lcy.jsonformat.config;

import java.io.IOException;
import java.util.Properties;

import cn.edu.pku.lcy.jsonformat.util.IOUtil;

public class Config {
	
	public static final String PROJECT_PROPERTIES = "json-format.properties";
	
	public static boolean TEST_MODE = false;
	
	public static String ROOT_DIR = null;
	
	public static String TEST_FILE_PATH = null;
	
	public static String SEMANTIC_INTERFACE_FILE_PATH = null;
	
	public static String SEMANTIC_IMPL_FILE_PATH = null;
	
	/**
	 * 载入项目配置文件
	 */
	static {
		try {
			Properties prop = IOUtil.propertiesLoader(PROJECT_PROPERTIES);
			
			ROOT_DIR = prop.getProperty("rootDir");
			
			if ("true".equals(prop.getProperty("testMode"))) {
				TEST_MODE = true;
			} else {
				TEST_MODE = false;
			}
			TEST_FILE_PATH = ROOT_DIR + prop.getProperty("testFilePath");
			SEMANTIC_INTERFACE_FILE_PATH = ROOT_DIR + prop.getProperty("semanticInterfaceFileDir");
			SEMANTIC_IMPL_FILE_PATH = ROOT_DIR + prop.getProperty("semanticImplFileDir");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
