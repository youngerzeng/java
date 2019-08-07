# hibernate_day04

### 查询总结

- oid查询查询--get
- 对象属性导航查询
- HQL
- Criteria
- 原生SQL

---

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

---

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

---

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