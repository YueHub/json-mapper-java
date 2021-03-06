# JsonFormat
Json数据格式映射
### 调用说明
测试模式－将json-format.properties文件中的testMode设置为true

	JSONFormat.jsonObjectFormat(null);	// 解析一条json对象

	JSONFormat.jsonArrayFormat(null);	// 解析json数组

非测试模式-将json-format.properties文件中的testMode设置为false

	设需要处理的原始数据为JSONObject

	添加semType标识

	jsonObject.put("semtype", "IqiyiVideo");
	
	JSONFormat.jsonObjectFormat(json字符串);	// 解析一条json对象
	
	设需要处理的原始数据为JSONArray
	
	JSONObject jsonObject = new JSONObject();
	
	jsonObject.put("semtype", "IqiyiVideo");
	
	jsonArray.add(jsonObject);
	
	JSONFormat.jsonArrayFormat(json字符串);	// 解析json数组

### 原理

在移动搜索引擎项目中,不同爬虫爬取下来的数据格式千差万别,其中相同语义的字段在不同APP中名称不同,如视频标题在xxx1应用中可能取名为title,在xxx2应用中取名为name.

当需要对外提供视频类的数据时,需要对这些字段进行映射处理,将它们统一为同一名称如videoName.

此项目通过配置文件实现语义映射.

实现原理如下:
![image](https://github.com/YueHub/JsonFormat/blob/master/pics/Image.png)

按数据的应用需求将爬虫数据分为几个类型,如视频类,其中爱奇艺,腾讯视频中的大部分数据(视频类APP中的部分数据可能属于其他类型)等并是视频类的实例.则设计两类语义文件
1. 视频类型接口定义
2. 爱奇艺定义

例子如下
假设有以下实例数据：
```
[
	{
	    "package_name": "com.qiyi.video",
	    "activity_name": "org.iqiyi.video.activity.PlayerActivity",
	    "web_url": "http://www.iqiyi.com/v_19rr9ulqu0.html",
	    "web_url_md5": "43FEDEA0FC5A9C3BB446D31C2A7941A8",
	    "title": "天气直播间 ：日照偏少河津大棚黄瓜生长缓慢",
	    "content": {
		"writer": "天气直播间",
		"tag": "社会 国语 国内",
		"name": "天气直播间 ：日照偏少河津大棚黄瓜生长缓慢",
		"description": "关注气象、气候、环保等相关新闻资讯，设置交通天气、灾害天气解读、预警预报资讯发布等板块；在重大灾害性天气（台风、暴雨洪涝等）来临时，将打通时段进行全程直播报道，展现灾害现场、提出防御指南，配以专家分析展望未来天气走势，对灾害天气可能给各行各业带来的影响进行跟踪报道。",
		"重要的额外属性1" : "xxxxx",
		"test" : {
		    "testInner" : "hello,world"
		}
	    },
	    "type": "video",
	    "semtype" : "IqiyiVideo",
	    "params": {
		"id": "v_19rr9ulqu0"
	    },
	    "crawl_timestamp": "1484830716778",
	    "重要的额外属性2" : "yyyyy"
	},
	{
	    "package_name": "com.qiyi.video",
	    "activity_name": "org.iqiyi.video.activity.PlayerActivity",
	    "web_url": "http://www.iqiyi.com/v_19rr9ulqu0.html",
	    "web_url_md5": "43FEDEA0FC5A9C3BB446D31C2A7941A8",
	    "title": "天气直播间 ：日照偏少河津大棚黄瓜生长缓慢",
	    "content": {
		"writer": "天气直播间",
		"tag": "社会 国语 国内",
		"name": "天气直播间 ：日照偏少河津大棚黄瓜生长缓慢",
		"description": "关注气象、气候、环保等相关新闻资讯，设置交通天气、灾害天气解读、预警预报资讯发布等板块；在重大灾害性天气（台风、暴雨洪涝等）来临时，将打通时段进行全程直播报道，展现灾害现场、提出防御指南，配以专家分析展望未来天气走势，对灾害天气可能给各行各业带来的影响进行跟踪报道。",
		"重要的额外属性1" : "xxxxx",
		"test" : {
		    "testInner" : "hello,world"
		}
	    },
	    "type": "video",
	    "semtype" : "IqiyiVideo",
	    "params": {
		"id": "v_19rr9ulqu0"
	    },
	    "crawl_timestamp": "1484830716778",
	    "重要的额外属性2" : "yyyyy"
	}
]
```

视频类接口
VideoInterface.json
```
{
    "semInterface" : {
          "document" : {
               "impl" : {
                    "interface" : "root"		// 实现接口为root表示该配置为接口文件
               },
               "props" : {						// 属性 
                    "id" : {					// ID字段
                         "isArr" : false,		// 是否为数组(暂时无用)
                         "dataType" : "String", // 数据类型
                         "unique" : true,		// 是否唯一
                         "required" : true,		// 是否为必有字段 false表示实现该接口的实例不是必须要含有该字段, 可以在配置文件中通过required进行选择
                         "desc" : "UUID"		// 描述
                    },
                    "crawerTime" : {	
                         "isArr" : false,
                         "dataType" : "String",
                         "unique" : false,
                         "required" : true,
                         "desc" : "数据爬取时间戳"
                    },
                    "webUrl" : {
                         "isArr" : false,
                         "dataType" : "String",
                         "unique" : false,
                         "required" : true,
                         "desc" : "URL"
                    },
                    "title" : {
                         "isArr" : false,
                         "dataType" : "String",
                         "unique" : false,
                         "required" : true,
                         "desc" : "标题"
                    },
                    "params" : {
                         "isArr" : false,
                         "dataType" : "String",
                         "unique" : false,
                         "required" : true,
                         "desc" : "params"
                    },
                    "name" : {
                         "isArr" : false,
                         "dataType" : "String",
                         "unique" : false,
                         "required" : true,
                         "desc" : "视频名称"
                    },
                    "writer" : {
                         "isArr" : false,
                         "dataType" : "String",
                         "unique" : false,
                         "required" : false,
                         "desc" : "视频作者"
                    },
                    "description" : {
                         "isArr" : false,
                         "dataType" : "String",
                         "unique" : false,
                         "required" : false,
                         "desc" : "视频描述"
                    },
                    "comments" : {
                         "isArr" : true,
                         "dataType" : "String",
                         "unique" : false,
                         "required" : false,
                         "desc" : "视频评论"
                    },
                    "tags" : {
                         "isArr" : true,
                         "dataType" : "String",
                         "unique" : false,
                         "required" : false,
                         "desc" : "视频标签"
                    }
               }
          }
     }
}
```
爱奇艺实例
IqiyiVideo.json
```
{
    "semImpl" : {
        "document" : {
            "impl" : {							// 实现的接口文件
               "interface" : "VideoInterface",	// 填写接口文件的名称
               "sameAs" : {						// 与接口中定义的字段语义相同,但是路径和名称不同时,进行映射配置
                    "writer" : "content.writer",// 实例数据中的content.writer对应了接口文件的writer字段
                    "tags" : "content.tag",		
                    "name" : "content.name",
                    "description" : "content.description"
               },
               "required" : [					// 针对接口文件中required为false的字段进行选择, 这里writer,description,tags将对外提供
                    "writer",
                    "description",
                    "tags"
               ]
            },
            "props" : {
               "重要的额外属性1" : {						// 除接口文件中定义的字段之外的自定义字段
               		"path" : "content.重要的额外属性1",	// 映射的路径
                    "isArr" : false,					// 是否为数组
                    "dataType" : "String",				// 数据类型
                    "unique" : false,					// 是否唯一
                    "required" : true,					// 是否对外提供该字段
                    "desc" : "重要的额外属性1"				// 描述
                },
               "重要的额外属性2" : {
               		"path" : "重要的额外属性2",
                    "isArr" : false,
                    "dataType" : "String",
                    "unique" : false,
                    "required" : true,
                    "desc" : "重要的额外属性2"
                },
                "testInner" : {
                    "path" : "content.test.testInner",
                    "isArr" : false,
                    "dataType" : "String",
                    "unique" : false,
                    "required" : true,
                    "desc" : "重要的额外属性2"
                }
			}
        }
    }
}
```

最终生成的数据格式如下：
```
[
  {
    "crawerTime": "crawerTime",
    "重要的额外属性1": "xxxxx",
    "testInner": "hello,world",
    "webUrl": "webUrl",
    "name": "天气直播间 ：日照偏少河津大棚黄瓜生长缓慢",
    "description": "关注气象、气候、环保等相关新闻资讯，设置交通天气、灾害天气解读、预警预报资讯发布等板块；在重大灾害性天气（台风、暴雨洪涝等）来临时，将打通时段进行全程直播报道，展现灾害现场、提出防御指南，配以专家分析展望未来天气走势，对灾害天气可能给各行各业带来的影响进行跟踪报道。",
    "id": "id",
    "writer": "天气直播间",
    "title": "title",
    "params": "params",
    "重要的额外属性2": "重要的额外属性2",
    "tags": "社会 国语 国内"
  },
  {
    "crawerTime": "crawerTime",
    "重要的额外属性1": "xxxxx",
    "testInner": "hello,world",
    "webUrl": "webUrl",
    "name": "天气直播间 ：日照偏少河津大棚黄瓜生长缓慢",
    "description": "关注气象、气候、环保等相关新闻资讯，设置交通天气、灾害天气解读、预警预报资讯发布等板块；在重大灾害性天气（台风、暴雨洪涝等）来临时，将打通时段进行全程直播报道，展现灾害现场、提出防御指南，配以专家分析展望未来天气走势，对灾害天气可能给各行各业带来的影响进行跟踪报道。",
    "id": "id",
    "writer": "天气直播间",
    "title": "title",
    "params": "params",
    "重要的额外属性2": "重要的额外属性2",
    "tags": "社会 国语 国内"
  }
]
```


