# hibernate_day03

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

-----

###多对多

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