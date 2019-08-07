

hibeernate_day02

[TOC]

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
> >assigned: 自然主键生成策略，hibernate 不会管理主键值，由开发人员自己录入

> 代理主键：表的业务列没有某业务列符合，必须有并且不重复的特征时，创建一个没有业务意义的列作为主键
>
> > identity：主键自增，由数据库来维护主键值，录入时不需要指定主键
> >
> >  	1. sequence: Oracle中的主键生成策略
> > 		2. increment(了解): 主键自增，由hibernate来维护，每次插入前会先查询表中 id 最大值，+1 作为新主键
> > 		3. hilo(了解): 高低位算法，主键自增，由 hibernate 来维护，开发时不适用
> > 		4. native: hilo+sequence+identity 自动三选一策略
> > 		5. uuid: 产生随机字符串作为主键，主键类型必须为String 类型

----

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

---

### hibernate 进阶-一级缓存

缓存：提高效率，提高操作数据库的效率

- 提高查询效率
- 减少不必要的修改语句发送

----

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

#####事务的隔离级别：

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

---

### hibernate 中的批量查询

---

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

---

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



---

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