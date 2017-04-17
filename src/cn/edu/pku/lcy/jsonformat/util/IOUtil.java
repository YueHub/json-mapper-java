package cn.edu.pku.lcy.jsonformat.util;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.log4j.Logger;


public class IOUtil {
	
	/**
	 * 日志组件
	 */
    public static Logger logger = Logger.getLogger(IOUtil.class);
	
	/**
	 * 读取资源文件 返回Properties对象
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
    public static Properties propertiesLoader(String fileName) throws IOException {
        // 文件在class的根路径  
        InputStream is = IOUtil.class.getClassLoader().getResourceAsStream(fileName);  

        BufferedReader br = new BufferedReader(new InputStreamReader(is));  
        Properties props = new Properties();
        
        props.load(br);  
        
        return props;
    }
	
    /**
     * 读取资源文件 返回文件文本内容
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String readResourceFile(String fileName) throws IOException {
        InputStream fis = IOUtil.class.getClassLoader().getResourceAsStream(fileName);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        StringBuffer sb = new StringBuffer();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }
    
    /**
     * 读取文本内容
     * @param path
     * @return
     */
    public static String readFile(String path) {
        String content = "";
        String line = null;
        try {
            BufferedReader bw = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            while ((line = bw.readLine()) != null) {
            	content += line;
            }
            bw.close();
        }
        catch (Exception e) {
            logger.warn("加载" + path + "失败，" + e);
        }
        return content;
    }
}
