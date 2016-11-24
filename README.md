# Fire

 为Android打造的数据库。代码精简小巧，性能卓越，去掉很多没有用的数据库操作，例如外联，内连等，只保留移动端会用到的数据库操作。
 代码小巧，简单易懂。

# 特点

 1.一行代码实现增删改查
 2.代码少，简单易懂

 ［device-2016-11-24-150815.png］

# Usage

 在Application当中
 ```java
    @Override
        public void onCreate() {

            super.onCreate();

            DBBase.Config config = DBBase.builcConfig()
                    .setName("dbName.db")
                    .setVersion(1)
                    .setCallback(new DBCallback() {
                        @Override
                        public void dbCreate() {

                        }

                        @Override
                        public void dbUpdate(SQLiteDatabase db, int oldVersion, int newVersion) {

                        }
                    });

            DBBase.getBase().init(this,config).creatTables(new Class[]{Person.class});
    }
 ```


 ```java
     DBBase.newDBHelper().save(person); //插入一条数据
     DBBase.newDBHelper().save(persons); //插入多条数据

     List<Person> result = DBBase.newDBHelper().find(Person.class); ／／查找所有
     DBBase.newDBHelper().from(Person.class).where("name","=","jack").and("age",">","12").find(); //条件查找

     DBBase.newDBHelper().update(person); //修改 注意id不能null
     DBBase.newDBHelper().update(persons);//修改多条

     ///delete
     DBBase.newDBHelper().deleteById(Person.class , "1"); ///根据ID删除
     DBBase.newDBHelper().deleteById(objects); ///根据ID删除一组
     DBBase.newDBHelper().delete(Person.class, WhereInfo.Builder.buildWhere("name", "=", "jack").and("age", ">", "12").build());
 ```
