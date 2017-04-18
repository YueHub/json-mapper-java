package cn.edu.pku.lcy.jsonformat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import cn.edu.pku.lcy.jsonformat.config.Config;
import cn.edu.pku.lcy.jsonformat.consts.ProjectConst;
import cn.edu.pku.lcy.jsonformat.util.IOUtil;


public class JSONFormat {
	
    public static void main(String args[]) throws Exception {
    	if (Config.TEST_MODE == true) { // 如果开启测试模式
    		String testJson = IOUtil.readFile(Config.TEST_FILE_PATH);
    		
    		// String result = JSONFormat.jsonObjectFormat(testJson);
    		String resultArray = JSONFormat.jsonArrayFormat(testJson);
    		System.out.println("resultArray" + resultArray);
//    		System.out.println("result:" + result);
//    		JSONFormat.logger.info("result:" + result);
    	}
    }
    
    /**
     * 映射单个JSON对象
     * @param jsonStr
     * @return
     * @throws Exception 
     */
    public static String jsonObjectFormat(String jsonStr) throws Exception {
    	
    	if (Config.TEST_MODE == true && jsonStr == null) {	// 如果开启了测试模式
    		jsonStr = IOUtil.readFile(Config.TEST_FILE_PATH);
    	}
    	if (Config.TEST_MODE == false && jsonStr == null) {	// 如果为开启测试模式,调用方式错误
    		throw new Exception("配置文件测试模式未开启,无法调用jsonObjectFormat(null)");
    	}
    	
    	JSONObject jsonObject = JSON.parseObject(jsonStr);
    	if (jsonObject.get("semtype") != null) {
    		String semType = jsonObject.get("semtype").toString();
            String semImplFileName = semType + ProjectConst.CONFIG_FILE_POSTFIX;
            // 语义实例文件
            String semImplContent = IOUtil.readFile(Config.SEMANTIC_IMPL_FILE_PATH + semImplFileName);
            JSONObject semImplObject = JSON.parseObject(semImplContent).getJSONObject("semImpl");   // 语义实现文件JSON对象
            JSONObject documentObject = semImplObject.getJSONObject("document");
            JSONObject implObject = documentObject.getJSONObject("impl");	// 实现接口
            JSONObject propsObject = documentObject.getJSONObject("props");	// 自定义属性
            LinkedHashMap<String, String> propsJsonMap = JSON.parseObject(propsObject.toString(), new TypeReference<LinkedHashMap<String, String>>() {});
            
            String interfaceStr = implObject.get("interface").toString();
            JSONObject sameAsObject = implObject.getJSONObject("sameAs");
            LinkedHashMap<String, String> sameAsJsonMap = JSON.parseObject(sameAsObject.toString(), new TypeReference<LinkedHashMap<String, String>>() {});
            JSONArray requiredObject = implObject.getJSONArray("required");
            
            List<String> requiredFields = new ArrayList<String>();
            List<String> apiFields = new ArrayList<String>();
            for (Object requiredField : requiredObject) {
                requiredFields.add(requiredField.toString());
            }
            
            String semInterfaceFileName = interfaceStr + ProjectConst.CONFIG_FILE_POSTFIX;
            String semInterfaceContent = IOUtil.readFile(Config.SEMANTIC_INTERFACE_FILE_PATH + semInterfaceFileName);
            JSONObject semInterfaceObject = JSON.parseObject(semInterfaceContent).getJSONObject("semInterface");	// 语义接口文件
            
            JSONObject idocumentObject = semInterfaceObject.getJSONObject("document");
            JSONObject ipropsObject = idocumentObject.getJSONObject("props");
            LinkedHashMap<String, String> jsonMap = JSON.parseObject(ipropsObject.toString(), new TypeReference<LinkedHashMap<String, String>>() {});
            // 最终对外生成的接口数据
            for (Entry<String, String> props : jsonMap.entrySet()) {
                String key = props.getKey();
                JSONObject metaPropObject = JSON.parseObject(props.getValue());
                if ("true".equals(metaPropObject.get("required").toString())) {
                    apiFields.add(key);
                }
            }
            apiFields.addAll(requiredFields);
            
            Map<String, String> fieldAndPath = new LinkedHashMap<String, String>();
            for (String apiField : apiFields) {
                fieldAndPath.put(apiField, apiField);
            }
            for (Entry<String, String> sameAs : sameAsJsonMap.entrySet()) {
                fieldAndPath.put(sameAs.getKey(), sameAs.getValue());
            }
            for (Entry<String, String> prop : propsJsonMap.entrySet()) {
                String path = JSON.parseObject(prop.getValue()).get("path").toString();
                fieldAndPath.put(prop.getKey(), path);
            }
            
            JSONObject resultJsonObject = new JSONObject();	// 最终返回的结果
            // 根据路径进行映射
            for (Entry<String, String> fieldPath : fieldAndPath.entrySet()) {
                String[] paths = fieldPath.getValue().split("\\.");
                String value = null;
                JSONObject tempJSONObject = null;
                if (paths.length == 1) {
                    value = paths[0];
                } else if (paths.length > 1) {
                    tempJSONObject = jsonObject.getJSONObject(paths[0]);
                    for (int i = 1; i < paths.length - 1; i++) {
                        tempJSONObject = tempJSONObject.getJSONObject(paths[i]);
                    }
                    value = tempJSONObject.get(paths[paths.length-1]).toString();
                }
                
                if (paths.length > 1) {
                }
                resultJsonObject.put(fieldPath.getKey(), value);
            }
            return resultJsonObject.toJSONString();
    	} else {
    		throw new Exception("semtype未定义");
    	}
    	
    }
    
    /**
     * 映射JSON对象数组
     * @param jsonStr
     * @return
     */
    public static String jsonArrayFormat(String jsonStr) throws Exception {
    	
    	if (Config.TEST_MODE == true && jsonStr == null) {	// 如果开启了测试模式
    		jsonStr = IOUtil.readFile(Config.TEST_FILE_PATH);
    	}
    	if (Config.TEST_MODE == false && jsonStr == null) {	// 如果为开启测试模式,调用方式错误
    		throw new Exception("配置文件测试模式未开启,无法调用jsonObjectFormat(null)");
    	}
    	
    	JSONArray jsonArray = null;
    	try {
    		jsonArray = JSON.parseArray(jsonStr);
    	} catch (Exception e) {
    		e.printStackTrace();
    		throw new Exception("json数组格式错误");
    	}
    	JSONArray resultJsonArray = new JSONArray();
    	if (jsonArray != null) {
    		for (Object object : jsonArray) {
    			JSONObject jsonObject = (JSONObject) JSON.toJSON(object);
    			String resultJsonObject = JSONFormat.jsonObjectFormat(jsonObject.toJSONString());
    			JSONObject newJsonObject = JSON.parseObject(resultJsonObject);
    			resultJsonArray.add(newJsonObject);
    		}
    	}
    	return resultJsonArray.toJSONString();
    }

}

