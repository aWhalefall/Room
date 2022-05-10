1.room 数据库的初始化注意事项
2.数据库升级

手动升级

 需要实现升级路径复写相关函数

private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
     @Override
     public void migrate(@NonNull SupportSQLiteDatabase database) {
         database.execSQL("ALTER TABLE user ADD COLUMN userRoomId INTEGER");
         database.execSQL("CREATE TABLE IF NOT EXISTS classroom (`roomId` TEXT NOT NULL, `masterTeacher` TEXT NOT NULL, `roomGrade` INTEGER NOT NULL, PRIMARY KEY(`roomId`))");
     }
    };


 添加新表
 增加字段


 修改字段
 删除字段


> https://www.jianshu.com/p/feba577213b7

SQLite 仅仅支持 ALTER TABLE 语句的一部分功能，我们可以用 ALTER TABLE 语句来更改一个表的名字，也可向表中增加一个字段（列），但是我们不能删除一个已经存在的字段，或者更改一个已经存在的字段的名称、数据类型、限定符等等。

改变表名 - ALTER TABLE 旧表名 RENAME TO 新表名

增加一列 - ALTER TABLE 表名 ADD COLUMN 列名 数据类型



自动升级方式


1.@Database schema 中声明的实体，如添加新列或表，更新主键、外键或索引，或更改列的默认值，Room 会自动检测出这些变化，
不需要额外介入。




 


3.设置表间关系 一对多 

表现：查询返回非List，只查询第一个符合条件数据一对多，list 返回所有一对多。

比如班级和学生是一对多根据班级表【班级Id】和 学生表的 学生班级字段进行关联 [1,2,3,4,5] 五个班级

1.默认会查出来班级1对应的班级1学生
2.如果返回list则会分别返回5个班级内各个班级的学生

