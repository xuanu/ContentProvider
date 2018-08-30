# ContentProvider
contentprovider

起因：内置软件经常有一些数据库的东西不想用户删除应用就跟着删除，所以想用一个别的软件来存放数据。

使用方法：
URI: "content://" + getPackageName() + ".cn.zeffect.db.provider?table=test"
table是一定需要的，我根据这个来做操作的。
1. 查询不存在字段时，不报错。
2. 删除不存在字段时，不报错。
3. 插入不存在字段时，默认添加。（影响效率）
4. 更新不存在字段时，默认添加。（影响效率）