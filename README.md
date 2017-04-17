# JsonFormat
Json数据格式映射

在移动搜索引擎项目中,不同爬虫爬取下来的数据格式千差万别,其中相同语义的字段在不同APP中名称不同,如视频标题在xxx1应用中可能取名为title,在xxx2应用中取名为name,
当需要对外提供视频类的数据时,需要对这些字段进行映射处理,将它们统一为同一名称如videoName.

此项目通过配置文件实现语义映射.

实现原理如下:
![image](https://github.com/YueHub/JsonFormat/blob/master/screenShots/原理1.png)

按数据的应用需求将爬虫数据分为几个类型,如视频类,其中爱奇艺,腾讯视频中的大部分数据(视频类APP中的部分数据可能属于其他类型)等并是视频类的实例.则设计两类语义文件
1. 视频类型接口定义
2. 爱奇艺定义

例子如下
视频类接口
VideoInterface.json
```
{
    "semInterface" : {
          "document" : {
               "impl" : {
                    "interface" : "root"
               },
               "props" : {
                    "id" : {
                         "isArr" : false,
                         "dataType" : "String",
                         "unique" : true,
                         "required" : true,
                         "desc" : "UUID"
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
                         "desc" : "视频描述"
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
            "impl" : {
               "interface" : "VideoInterface",
               "sameAs" : {
                    "writer" : "content.writer",
                    "tags" : "content.tag",
                    "name" : "content.name",
                    "description" : "content.description"
               },
               "required" : [
                    "writer",
                    "description",
                    "tags"
               ]
            },
            "props" : {
               "重要的额外属性1" : {
               		"path" : "content.重要的额外属性1",
                    "isArr" : false,
                    "dataType" : "String",
                    "unique" : false,
                    "required" : true,
                    "desc" : "重要的额外属性1"
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
测试文件
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
