面试官请看这里！！！
下面做逐一介绍，并且标识出关键代码所处位置



springcloud-addcoupon7501（添加优惠券服务）

1.7501是设置的端口号(以下服务都是这样)
2.\springcloud-addcoupon7501\src\main\java\com\demo\consumer\AddCouponCounsumer.java  
文件有该服务的关键代码，配有部分注释



springcloud-emailsender7401（邮箱发送服务）

1.\springcloud-emailsender7401\src\main\java\com\demo\consumer\EmailSenderConsumer.java 
文件有该服务的关键代码，



springcloud-gateway6002-jwt（jwt权限认证服务）

1.\springcloud-gateway6002-jwt\src\main\java\com\demo\jwt  
目录下有支持jwt的内容

2.\springcloud-gateway6002-jwt\src\main\java\com\demo\aop   
jwt的认证是使用的aop的方式  在方法上添加注解即可验证jwt的有效性



springcloud-seckill7301 （秒杀接口）

1.\springcloud-seckill7301\src\main\java\com\demo\controller\SecKillController.java  
该controller下是具体的秒杀逻辑，代码分析过程都写在注释里了



springcloud-snowflake11001（基于雪花算法的唯一ID生成服务）

1. \src\main\java\com\demo\snowflake  该文件下是雪花算法的主要逻辑，代码相较于网络上的方案稍作更改



