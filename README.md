2020.9.17
---------------
该项目已距离现在过了7个月！可能有人会问当初为什么会有这个项目，在疫情初期，信息都掌握在国家和大媒体手上，开发者没有即时的API和SERVICE可以使用，于是我启动了这项任务。一个疫情广播，刷新网页数据到可调用的接口。
这个项目很小，但我今天突然发现我的代码被带到北极了！(Arctic Vault Programme)，不过这点贡献简直不得一提，感谢从2020年初就奋斗在疫情一线的工作者和开发者，致敬。
此项目至此结束。

This project has been for 7 months!People may asks why I created this project. At first of the COVID-19,all informations are published by goverments or medias,there are no efficient ways to get data by API or SERVICE.So I started on this work.This is a broadcast,publishing the datas from media to API.
This is a small project,But I just found that this project was send to (Arctic Vault Programme).This is a luxury for such small works,Thank to all the workers or coders facing the virus from 2020.Salute.
This programme is now closed.

----------------




获取四项疫情数据至本地文件，
Get Diagnose,write to Json

运行环境:JRE7+
Environment:JRE7+

HOW THIS WORKS?

<del>爬虫,数据来源:丁香园</del>
(已在新版中被废除
<del>SELENIUM</del>from https://ncov.dxy.cn/ncovh5/view/pneumonia



HTTP requestLocation(HTTP请求地址Location)
API:


[GLOBAL](GET)(全球)
http://api.tianapi.com/txapi/ncovabroad/index
param:?key=f1b1ff0ecc9eb27d2a17bff48f32a168

[REPORTS](GET)(新闻)
http://api.tianapi.com/txapi/ncov/index
param:?key=f1b1ff0ecc9eb27d2a17bff48f32a168

[CHINA](GET)(境内)
http://api.tianapi.com/txapi/ncovcity/index
param:?key=f1b1ff0ecc9eb27d2a17bff48f32a168
