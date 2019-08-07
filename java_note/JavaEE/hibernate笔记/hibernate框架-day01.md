#hibernate框架

##hibernate_day01

----

[TOC]

### hibernate是什么

##### 框架概述：

- 框架是用来提高开发效率的
- 封装了一些我们需要使用的功能，调用即可
- 可以理解为半成品的项目

好处：操作数据库时可以面向对象的方式完成，不需要书写SQL语句

hibernate是一款 orm(object relational mapping对象关系映射)框架

##### hibernate框架的搭建：

1. 导包

2. 创建数据库

3. 创建实体类（类属性与数据库字段一致）

4. 书写对象与表的映射配置文件:实体类Customer所在包下创建 Customer.hbm.xml文件

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <!DOCTYPE hibernate-mapping PUBLIC 
       "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
       "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
       <hibernate-mapping>
       	<!-- 建立类和表的一个映射关系 -->
       	<!-- 
       		class 标签：用来建立类和表的映射
       			name 属性：类中的全路径
       			table 属性：表名（类名和表名一致可省略）
       			catelog 属性：数据库名称，可省略
       	 -->
       	<class name="younger.Customer" table="cst_customer">
       	<!-- 建立类中属性与表中的主键的映射 -->
       	<!-- 
       		id 标签：类属性与表主键字段对应
       			name 属性：类属性名
       			column 属性：表中字段（类属性和表字段名一致可省略）
       			length 属性：字段长度
   				not-null 属性：配置该属性不能为空，默认 false
       			type 属性：类型。 java数据类型，Hibernate数据类型（默认），SQL类型
       	 -->
       	 	<id name="cust_id" column="cust_id">
       		 	<!-- 主键生成策略 -->
       	 		<generator class="native"/>
       	 		
       	 </id>
       	 <!-- 建立类中普通属性与表中字段映射 -->
       	 <!-- 
       	 	property 标签:普通属性
       	 		name,column,length,type，not-null
       	  -->
       	  <property name="cust_name" column="cust_name"/>
       	  <property name="cust_source" column="cust_source"/>
       	  <property name="cust_industry" column="cust_industry"/>
       	  <property name="cust_level" column="cust_level"/>
       	  <property name="cust_linkman" column="cust_linkman"/>
       	  <property name="cust_phone" column="cust_phone"/>
       	  <property name="cust_mobile" column="cust_mobile"/>
       	</class>
       </hibernate-mapping>
   ```

   ​

5. 书写主配置文件:src下创建 hibernate.cfg.xml文件

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <!DOCTYPE hibernate-configuration PUBLIC
   	"-//Hibernate/Hibernate Configuration DTD 3.0//EN"
   	"http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
   	<hibernate-configuration>
   		<session-factory>
   			<!-- 必要的配置信息：连接数据库的基本参数(5个) -->
   			<property name="hibernate.connection.driver_class">com.mysql.jdbc.Driver</property>
   			<property name="hibernate.connection.url">jdbc:mysql:///customer</property>
   			<property name="hibernate.connection.username">root</property>
   			<property name="hibernate.connection.password">123456</property>
   			
   			<!-- hibernate的属性 -->
   			<!-- hibernate的方言：根据配置的方言生成相应的SQL语句 -->
   			<property name="hibernate.dialect">org.hibernate.dialect.MySQLDialect</property>
   			<!-- Hibernate显示SQL语句，打印到控制台 -->
   			<property name="hibernate.show_sql">true</property>
   			<!-- hibernate 格式化SQL语句 -->
   			<property name="hibernate.format_sql">true</property>
   			<!-- hibernate的hbm2dd1(数据定义语言：create drop alter....)属性 -->
   			<!-- 
   				hbm2ddl.hbm2ddl.auto的取值
   					none :不用hibernate自动生成表
   					create :每次都自动建表（开发中使用）
   					create-drop : 每次自动建表，执行程序结束后删除这个表（开发中使用）
   					update :自动建表，若已存在不再生成，可以更新表结构
   					validate :不自动建表，每次启动会校验数据库中表是否正确
   			 -->
   			 <property name="hibernate.hbm2ddl.auto">update</property>
   			 <!-- hibernate加载映射 -->
   			 <mapping resource="younger.Customer.hbm.xml"/>
   			
   		</session-factory>
   	</hibernate-configuration>
   ```

6. 编写测试

   ```java
   public class Test1 {
   	
   	@Test
   	public void fun1 (){
   		//加载配置文件，读取并解析hibernate.cfg.xml核心配置文件
   		Configuration conf = new Configuration().configure();
   		//创建session对象，类似Connection，读取解析 customer.hbm.xml映射文件
   		//并获得Configuration所有配置信息
           /*
           	sessionFactory:创建session 对象
                 - sessionFactory负责保存和使用所有配置信息，消耗内存资源非常大
                 - sessionFactory属于线程安全的对象设计
                结论：保证在web项目中，只创建一个sessionFactory
           */
   		SessionFactory sessionFactory = conf.buildSessionFactory();
           //打开一个新的session对象,hiberante操作数据库的核心对象
   		Session session = sessionFactory.openSession();
           //获得一个与线程绑定的session对象
           //sessionFactory.getCurrentSession();
   		//开启事务并获得操作事务的tx对象
   		Transaction tx = session.beginTransaction();
   		//执行操作
           //增
   		Customer c = new Customer();
   		c.setCust_name("baidu");
   		session.save(c);
           //查
           Customer customer = session.get(Customer.class,1l);
           //改
           Customer c1 = session.get(Customer.class,1l);//获得修改对象
           c.setCust_name("younger");
           session.update(c1);
           //删
           Customer c2 = session.get(Customer.class,1l);
           session.delete(c2);
   		//事务提交
   		tx.commit();
           //回滚事务
           //tx.rollback();
   		session.close();
   		sessionFactory.close();
   	}
   }
   ```

   ​

##hibeernate_day02

### hibernate 中的实体规则

##### 实体类创建的注意事项：

1. 持久化类提供***无参数构造***
2. 成员变量**私有**，提供有 get/set 方法访问，需提供属性
3. 持久化类中的属性，应尽量使用**包装类型**
4. 持久化类需要提供 ***oid***，与数据库中的主键列对应
5. 不要用 final 修饰 class: hibernate 使用 cglib 代理生成代理对象，代理对象是继承被代理对象，如果被final 修饰，将无法生成代理

##### 主键类型：

> 自然主键(少见)：表的业务列中有某业务列符合，必须有并且不重复的特征时，该列可以作为主键使用
>
> > assigned: 自然主键生成策略，hibernate 不会管理主键值，由开发人员自己录入

> 代理主键：表的业务列没有某业务列符合，必须有并且不重复的特征时，创建一个没有业务意义的列作为主键
>
> > identity：主键自增，由数据库来维护主键值，录入时不需要指定主键
> >
> > 1. sequence: Oracle中的主键生成策略
> >    1. increment(了解): 主键自增，由hibernate来维护，每次插入前会先查询表中 id 最大值，+1 作为新主键
> >       1. hilo(了解): 高低位算法，主键自增，由 hibernate 来维护，开发时不适用
> >       2. native: hilo+sequence+identity 自动三选一策略
> >       3. uuid: 产生随机字符串作为主键，主键类型必须为String 类型

------

### hibernate中的对象状态

##### 对象分为三种状态：

- 瞬时状态：没有 id，没有在 session缓存中

- 持久化状态：有 id，在 session缓存中

- 游离|托管状态：有 id，没有在 session缓存中

  ![三种状态转换图](C:\Users\Administrator\Desktop\JavaEE\hibernate笔记\img\01.png)

  ```java
  public void demo(){
      Session session = HibernateUtils.openSession();
      Transaction tx = session.beginTransaction();
      Customer customer = new Customer();//瞬时态对象，没有持久化标识oid，没有被 session管理
      customer.setCust_name("老王");
      Serializable id = session.save(customer);//持久化对象，有持久化标识oid，被session管理
      tx.commit();
      session.close();
      System.out.println(customer);//托管态对象，有持久化标识oid，没有被session管理
  }
  ```

  注：持久态对象有自动更新数据库的能力

------

### hibernate 进阶-一级缓存

缓存：提高效率，提高操作数据库的效率

- 提高查询效率
- 减少不必要的修改语句发送

------

### hibernate 中的事务

##### 事务特性：

- a   原子性
- b   一致性
- c   隔离性
- d   持久性

##### 事务并发问题：

1. 脏读
2. 不可重复读
3. 幻|虚读

##### 事务的隔离级别：

- 读未提交   123
- 读已提交   23
- 可重复读（mysql默认级别）   3
- 串行化   没有问题

##### hibernate 中指定数据库的隔离级别：

```xml
<!-- 指定hibernate操作数据库时的隔离级别
   # hibernate.connection.isolation 1|2|4|8
0001   1   读未提交
0010   2   读已提交
0100   3   可重复读
1000   4   串行化
-->
<property name="hibernate.connection.isolation">4</property>
```

#### 项目中如何管理事务

- 业务开始之前打开事务，业务执行之后提交事务，出现异常回滚事务

- dao 层操作数据库，service 层控制事务，确保 dao层和service层使用同一个session对象

- ```xml
  <!-- 指定session与当前线程绑定-->
  <property name="hibernate.current_session_context_class">thread</property>
  ```

- sf.getCurrentSession()方法获得与当前线程绑定的session对象

------

### hibernate 中的批量查询

------

#### HQL 查询-Hibernate Query Language(多表查询但不复杂时使用)

> hibernate独家查询语言，属于面向对象的查询语言

- 基本查询

  ```java
  //书写 HQL语句
  String hql = "from Customer";//查询所有Customer对象
  //根据HQL语句创建查询对象
  Query query = session.createQuery(hql);
  //获得查询结果
  List<Customer> list = query.list();//返回list结果
  //query.uniqueResult();//接收唯一查询结果
  ```

- 条件查询

```java
//?号占位符
//书写HQL语句
String hql = "from Customer where cust_id=?";
//创建查询对象
Query query = session.createQuery(hql);
//设置参数
//query.setLong(0,1l);
query.setParameter(0,1l);
//获得查询结果
Customer c = (Customer)query.uniqueResult();
```

```java
//命名占位符
String hql = "from Customer where cust_id = :cust_id";
Query query = session.createQuery(hql);
query.setParameter("cust_id",1l);
Customer c = (Customer)query.uniqueResult();
```

- 分页查询

  ```java
  //书写HQL语句
  String hql = "from Customer";
  //创建查询对象
  Query query = session.createQuery(hql);
  //设置分页信息 limit ?,?
  query.setFirstResult(1);
  query.setMaxResults(1);
  //获得查询结果
  List<Customer> list = query.list();
  ```

------

#### Criteria查询(单表条件查询)

> hibernate 自创无语句面向对象查询

- 基本查询

  ```java
  //查询所有Customer对象
  Criteria criteria = session.createCriteria(Customer.class);
  List<Customer> list = criteria.list();
  ```

- 条件查询

  ```java
  //创建 criteria 查询对象
  Criteria criteria = session.createCriteria(Customer.class);
  //添加查询参数
  criteria.add(Restrictions.eq("cust_id",1l));
  //获得结果
  Customer c = (Customer)criteria.uniqueResult();
  ```

  ```java
  //条件查询
  //HQL语句中，不能出现任何数据库相关的信息
  //  >                 gt
  //  >=   			 ge
  //  <     		      lt
  //  <=   			 le
  //  ==                eq
  //  !=                ne
  //  in                in
  //  between  and      between
  //  like			 like
  //  is not null       isNotNull
  //  is null 		 isNull
  //  or				or
  //  and				and
  ```

- 分页查询

  ```java
  //创建 criteria查询对象
  Criteria criteria = session.createCriteria(Customer.class);
  //设置分页信息  limit ?,?
  criteria.setFirstResult(1);
  criteria.setMaxResult(2);
  //执行查询获得结果
  List<Customer> list = criteria.list();
  ```

- 设置查询总记录数

  ```java
  //创建 criteria查询对象
  Criteria criteria = session.createCriteria(Customer.class);
  //设置查询的聚合函数 => 总行数
  criteria.setProjection(Projections.rowCount());
  //执行查询
  Long count = (Lont) criteria.uniqueResult();
  ```

------

#### 原生 SQL 查询(复杂的业务查询)

- 基本查询

  - 返回数组 List

    ```java
    //书写SQL语句
    String sql = "select * from cst_customer";
    //创建SQL查询对象
    SQLQuery query = session.createSQLQuery(sql);
    //获得查询结果
    List<Object[]> list = query.list();
    ```

  - 返回对象LIst

    ```java
    String sql = "select * from cst_customer";
    SQLQuery query = session.createSQLQuery(sql);
    //指定结果封装到哪个对象中
    query.addEntity(Customer.class);
    //获得结果
    List<Customer> list = query.list();
    ```

- 条件查询

  ```java
  String sql = "select * from cst_customer where cust_id = ?";
  //创建SQL查询对象
  SQLQuery query = session.createSQLQuery(sql);
  //设置参数
  query.setParameter(0,1l);
  //指定将结果集封装到哪个对象中
  query.addEntity(Customer.class);
  //调用查询结果
  List<Customer> list = query.list();		
  ```

- 分页查询

  ```java
  String sql = "select * from cst_customer limit ?,?";
  SQLQuery query = session.createSQLQuery(sql);

  query.setParameter(0,0);
  query.setParameter(1,1);
  query.addEntity(Customer.class);

  List<Customer> list = query.list();
  ```

  ​

##hibernate_day03

### 一对多|多对一

> 客户 （一）   --->      联系人   （多）

1. 创建表：联系人表中存在外键，指向客户

2. 创建实体

3. 创建映射

   ```xml
   <!--一对多-->
   <!--
   	name :集合属性名
   	column :外键列名
   	class :与我关联的对象完整类名
   -->
   <set name="linkMens">
   	<key column="lkm_cust_id"></key>
       <one-to-many class="linkMan" />
   </set>
   ```

   ```xml
   <!--多对一-->
   <!--
   	name：引用属性名
   	column:外键列名
   	class:与我关联的对象完整类名
   -->
   <many-to-one name="customer" column="lkm_cust_id" class="Customer"></many-to-one>
   ```

4. 操作

   ```java
   Customer  c = new Customer();
   c.setCust_name("张三");

   linkMan lm1 = new LinkMan();
   lm1.setLkm_name("李四");

   LinkMan lm2 = new LinkMan();
   lm2.setLkm_name("王五");

   c.getLinkMens().add(lm1);
   c.getLinkMens().add(lm2);

   lm1.setCustomer(c);
   lm2.setCustomer(c);

   session.save(c);
   session.save(lm1);
   session.save(lm2);
   ```

5. 级联操作

   ```
   级联操作：简化操作，目的为了少些代码
   	cascade
   		save-update:级联保存更新
   		delete:级联删除
   		all:save-update + delete
   	结论：用save-update，不建议使用delete	
   ```

6. 关系维护

   ```
   inverse属性:配置关系是否维护  true不维护，false（默认）维护
   	性能优化，提高关系维护的性能
   一对多关系中：一的一方放弃，多的一方不能放弃
   ```

------

### 多对多

- 映射

  ```xml
  <!--多对多-->
  <!--
  	name:集合属性名
  	table:配置中间表名
  	key
  		column:外键，别人引用“我”的外键列名
  		class:我与哪个类是多对多关系
  		column:外键，我引用别人的外键列名
  -->
  <set name="roles" table="sys_user_role">
  	<key column="user_id"></key>
      <many-to-many class="Role" column="role_id"></many-to-many>
  </set>
  ```

- 级联属性

  ```
  cascade级联操作：简化代码书写，该属性使用不适用无所谓，建议只使用save-update,如果使用delete操作太过危险
  ```

  ​

##hibernate_day04

### 查询总结

- oid查询查询--get
- 对象属性导航查询
- HQL
- Criteria
- 原生SQL

------

### 查询--HQL语法

- 基础语法

  ```java
  String hql = "from younger.Customer";
  String hql2 = "from Customer";
  String hql3 = "from java.lanag.Object";
  Query query = session.createQuery(hql3);
  List list = query.list();
  ```

- 排序

  ```java
  String hql1 = "from Customer order by cust_id asc";
  String hql2 = "from Customer order by cust_id desc";
  Query query = session.createQuery(hql2);
  List list = query.list();
  ```

- 条件

  ```java
  String hql1 = "from Customer where cust_id = ?";
  String hql2 = "from Customer where cust_id= :id";
  Query query = session.createQuery(hql2);
  //query.setParameter(0,2l);
  query.setParameter("id",2l);
  List list = query.list();
  ```

  ​

- 分页

  ```java
  String hql1 = "from Customer";
  Query query = session.createQuery(hql1);
  //limit ?,? , (当前页数-1)*每页条数
  query.setFirstResult(2);
  query.setMaxResults(2);
  List list = query.list();
  ```

  ​

- 聚合

  ```java
  //count(*),sum(cust_id),avg(cust_id),max(cust_id),min(cust_id)
  String hql = "select count(*) from Customer";
  Query query = session.createQuery(hql);
  Number number = (Number) query.uniqueResult();
  ```

- 投影

  ```java
  String hql1 = "select cust_name,cust_id from Customer";
  String hql2 = "select new Customer(cust_id,cust_name) from Customer";
  Query query = session.createQuery(hql2);
  List list = query.list();
  ```

- 多表查询

  - SQL

    ```
    内连接
      隐式内连接：select * from A,B where b.aid=a.id
      显式内连接：select * from A inner join B on b.aid=a.id
    外连接
      左外：select * from A left [outer] join B on b.aid=a.id
      右外：select * from A right [outer] join B on b.aid=a.id
    ```

  - HQL

    - 内连接

      ```java
      String hql = "from Customer c inner join c.linkMens";
      Query query = session.createQuery(hql);
      List<Customer> list = query.list();
      ```

    - 左外连接

      ```java
      String hql = "from Customer c left join c.linkMens";
      ```

    - 右外连接

      ```java
      String hql = "from Customer c right join c.linkMens";
      ```

------

### 查询--Criteria语法

- 基本语法

  ```java
  Criteria c = session.createCriteria(Customer.class);
  List<Customer> list = c.list();
  ```

- 条件语法

  ```java
  Criteria c = session.createCriteria(Customer.class);
  //c.add(Restrictions.idEq(2l));
  c.add(Restrictions.eq("cust_id",2l));
  List<Customer> list = c.list();
  ```

- 分页

  ```java
  Criteria c = session.createCriteria(Customer.class);
  c.setFirstResult(0);
  c.setMaxResults(2);
  List<Custmer> list = c.list();
  ```

- 排序

  ```java
  Criteria c = session.createCriteria(Customer.class);
  c.addOrder(Order.asc("cust_id"));
  //c.addOrder(Order.desc("cust_id"));
  List<Customer> list = c.list();
  ```

- 统计

  ```java
  Criteria c = session.createCriteria(Customer.class);
  c.setProjection(Projections.rowCount());
  List list = c.list();
  ```

- 离线查询

  ```java
  public void fun1(){
      //service/web层
      DetachedCriteria dc=DetachedCriteria.forClass(Customer.class);
      dc.add(Restrictions.idEq(6l));//拼装条件(全部与普通Criteria一致)
      //------------------------------------------------------------
      Criteria c = dc.getExecutableCriteria(session);
      List list = c.list();
      System.out.println(list);
      //------------------------------------------------------------
      tc.commit();
      session.close();
  }
  ```

------

### 查询优化

- 类级别查询：

  - get方法

  - load方法：应用类级别的加载策略

    `<class name="Customer table="cst_customer" lazy="false">`

- 关联级别查询

  - 集合策略

    ```
    lazy 属性：决定是否延迟加载
    	true(default):延迟加载，懒加载
    	false:立即加载
    	extra：及其懒惰
    fetch属性：决定加载策略，使用什么类型的sql 加载集合数据
    	select(default):单表查询加载
    	join：多表查询加载集合
    	subselect：子查询加载集合
    ```

  - 关联属性策略

    ```
    fetch 决定加载的sql语句
    	select：使用单表查询
    	join：多表查询
    lazy  决定加载实际
    	false：立即加载
    	proxy：由customer类级别加载策略决定
    ```

    > 结论：为了提高效率，fetch选择select,lazy选择true，全部使用默认值

- 批量抓取

  ```
  batch-size:抓取集合的数量为3
  	抓取客户的集合时，一次抓取几个客户的联系人集合
  ```

  ​