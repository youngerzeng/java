#Spring笔记

##Spring_day01

[TOC]

### Spring介绍

> Spring 负责管理项目中的所有对象
>
> AOP支持
>
> IOC(Inversion of Control)思想：控制反转思想，对象的创建权反转给Spring,需DI依赖注入
>
> Spring jdbc
>
> Junit 测试支持

### Spirng搭建

1. 导包

2. 创建对象  (例如User类对象)

3. 书写配置注册对象到容器

   > 位置随意(建议src下)
   >
   > 配置文件名随意（建议application.xml）

   ```xml
   <bean name="user" class="younger.User"></bean>
   ```

4. 代码测试

   ```java
   public void fun1(){
       ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext");
       User u = (User)ac.getBean("user");
       System.out.println(u);
   }
   ```

### Spring 配置详解

- Bean 元素

  ```xml
  <!-- 将User对象交给Spring容器管理 -->
  <!-- Bean元素：
  	class:对象完整类名
  	name：给对象起名，可重复可使用特殊字符
  	id：与name一样，不可重复不能使用特殊字符
  	scope: singleton(defalut):单例对象，只存在一个实例
  		   prototype:多例，每次创建都是新对象，整合struts时必须多例
  		   request：与request生命周期一致
    		   session:与session生命周期一致
  	生命周期属性：
  		  init-method:初始化方法，对象创建后立即调用
  		  destory-method:销毁方法，关闭就销毁
  -->
  <bean name="user" class="younger.User"
        init-method="init" destory-method="destory"></bean>
  ```

- Spring创建对象方式

  - **空参构造方法**(推荐)

    `<bean name="user" class="younger.User ></bean>"`

  - 静态工厂(了解)

  - 实例工厂（了解）

    ​

- spring 的分模块配置

  `<import resource="younger.applicationContext.xml />"`



### Spring属性注入

- set 方法注入

  ```xml
  <bean name="user" class="younger.User">
  	<!-- 值类型注入-->
      <property name="name" value="tom"></property>
      <property name="age" value="18"></property>
      <!--引用类型注入-->
      <property name="car" ref="car"></property>
  </bean>
  ```

- 构造函数注入

  ```xml
  <bean name="user" class="younger.User">
  	<constructor-arg name="name" index="0" type="java.lang.Integer" value="999"></constructor-arg>
      <constructor-arg name="car" ref="car" index="1"></constructor-arg>
  </bean>
  ```

- p名称注入(了解)

- spel注入(了解)

- 复杂类型注入 : 如果只注入一个值，直接使用value|ref即可

  - 数组 

    ```xml
    <property name="arr">
    	<array>
        	<value>tom</value>
            <value>jerry</value>
        </array>
    </property>
    ```

  - List

    ```xml
    <property name="arr">
    	<list>
        	<value>tom</value>
            <value>jerry</value>
            <ref bean="user3"/>
        </list>
    </property>		
    ```

  - Map

    ```xml
    <property name="map">
    	<map>
        	<entry key="url" value="jdbc:mysql:///crm"></entry>
            <entry key="user" value-ref="user3"></entry>
            <entry key-ref="user3" value-ref="user2"></entry>
        </map>
    </property>
    ```

  - Properties

    ```xml
    <property name="prop">
    	<props>
        	<prop key="driverClass">com.jdbc.mysql.Driver</prop>
            <prop key="userName">root</prop>
            <prop key="password">1234</prop>
        </props>
    </property>
    ```

    ​

## Spring_day02

### 使用注解配置Spring

1. 导包

2. 开启注解

   ```xml 
   <context:conponent-scan base-package="younger.bean"></context:conponent-scan>
   ```

3. 注解配置

   - 对象注册到容器：

     ```java
     @Component("user")
     	@Service("user")   //service
     	@Controller("user")		//web
     	@Respository("user")	//dao
     ```

   - 修改对象作用范围：`@Scope(scopeName="prototype")`

   - 值类型注入

     ```java
     //通过反射的Field赋值，破坏了封装性
     @Value("tom")
     private String name;
     //通过set方法赋值，推荐
     @Value("tom")
     public void setName(String name){
         this.name = name;
     }
     ```

   - 引用类型注入

     ```java
     @Autowired //自动装配
     private Car car;
     ```

     ```java
     @Autowired
     @Qualifier("car2")//有多个对象，此注解可以匹配指定对象
     private Car car;

     @Resource(name="car")//手动注入，指定注入哪个对象
     private Car car;
     ```

   - 初始化|销毁方法

     ```java
     @PostConstruct  //对象创建后调用。init-method
     public void init(){
         System.out.println("初始化方法");
     }
     @PreDestroy  //销毁前调用，destory-method
     public void destory(){
         System.out.println("销毁方法");
     }
     ```

     ​

### spring与junit整合测试

1. 导包

2. 配置注解

   ```java
   //帮我们创建容器
   @Runwith(SpringJunit4ClassRunner.class)
   //指定创建容器时使用哪个配置文件
   @ContextConfiguration("classpath:applicationContext.xml")
   public class Demo{
       //将user注入到u变量中
       @Resource(name="user")
       private User u;
   }
   ```

3. 测试

   ```java
   @Test
   public void fun1(){
       System.out.println(u);
   }
   ```

   ​

### spring 的 aop

- spring 实现 aop 的原理

  动态代理( 优先）：被代理对象必须实现接口，才能产生代理对象

  cglib代理：第三方代理技术，可以对任何类生成代理

- aop 名词学习

  > Joinpoint(连接点)：目标对象中，所有可以增强的方法
  >
  > Pointcut(切入点)：目标对象，已经增强的方法
  >
  > Advice(通知/增强)：增强的代码
  >
  > Target(目标对象)：被代理对象
  >
  > Weaving(织入)：将通知应用到切入点的过程
  >
  > Proxy(代理)：将通知织入到目标对象后，形成代理对象
  >
  > aspect(切面):切入点 + 通知



## Spring_day03

### spring 整合 JDBC

1. 导包

2. 准备数据库

3. 书写 dao

   ```java
   public void save(User u){
       String sql = "insert into t_user values(null,?)";
       super.getJdbcTemplate().update(sql,u.getName());
   }
   public int getTotalCount(){
       String sql = "select count(*) from t_user";
       Integer count = super.getJdbcTemplate().queryForObject(sql,Integer.class);
       return count;
   }
   ```

   ​

### spring 中 aop 事务

- 编码式

  ```xml
  1.将核心事务管理器配置到spring中
  <bean name="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
      <property name="dataSource" ref="dataSource"></property>
  </bean>
  2.配置TransactionTemplate模板
  <bean name="transactionTemplate" class="org.springframework.transaction.support.TransactionTemplate">
  	<property name="transactionManager" ref="transactionManager"></property>
  </bean>
  3.将事务模板注入 Service
  <bean name="accountService" class="younger.service.AccountServiceImpl">
  	<property name="aa" ref="accountDao"></property>
      <property name="tt" ref="transactionTemplate"></property>
  </bean>
  ```

  ```java
  4.在Service中调用模板
  public void transfer(final Integer from,final Integer to,final Double money){
  tt.execute(new TransactionCallBackWithoutResult(){
  protected void doInTransactionWithoutResult(TransactionStatus arg0){
  	//减钱
  	ad.decreaseMoney(from,money);
  	int i = 1/0;
  	//加钱
  	ad.increaseMoney(to,money);
  }
  })
  }
  ```

- xml配置 aop

  1. 导包
  2. 导入新的约束
  3. 配置通知

  ```xml
  <tx:advice id="txAdvice" transaction-manager="transactionManager">
  	<tx:attributes>
      	<!-- 以方法为单位，指定方法用什么事务属性
  			isolation:隔离级别
  			propagation:传播行为
  			read-only:是否只读
  		-->
     		<tx:method name="save" isolation="REPEATABLE_READ" propagation="REQUIRED" read-only="false" />
          <tx:method name="update" isolation="REPEATABLE_READ" propagation="REQUIRED" read-only="false" />
          <tx:method name="delete" isolation="REPEATABLE_READ" propagation="REQUIRED" read-only="false" />
          <tx:method name="remove" isolation="REPEATABLE_READ" propagation="REQUIRED" read-only="false" />
          <tx:method name="get" isolation="REPEATABLE_READ" propagation="REQUIRED" read-only="true" />
          <tx:method name="find" isolation="REPEATABLE_READ" propagation="REQUIRED" read-only="true" />
          <tx:method name="transfer" isolation="REPEATABLE_READ" propagation="REQUIRED" read-only="true" />
      </tx:attributes>	
  </tx:advice>
  ```

  4. 配置将通知织入目标

     ```xml
     //织入配置
     <aop:config >
     	//配置切点表达式
         <aop:pointcut expression="execution(* younger.service.*ServiceImpl.*(..))" id="txPc"/>
         <!-- 配置切面：通知+切点
     		advice-ref:通知名称
     		pointcut-ref:切点名称
     	-->
         <aop:advisor advice-ref="txAdvice" pointcut-ref="txPc"/>
     </aop:config>
     ```

     ​

## Spring_day04

### 单独配置Spring

- 创建配置文件，导入约束

  ```xml
  <beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  		xmlns="http://www.springframework.org/schema/beans" 
  		xmlns:context="http://www.springframework.org/schema/context"
  		xmlns:aop="http://www.springframework.org/schema/aop" 
  		xmlns:tx="http://www.springframework.org/schema/tx"
  		xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.2.xsd 
  							http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.2.xsd 
  							http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.2.xsd 
  							http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-4.2.xsd ">
  </beans>	
  ```

- 配置spring随项目启动

  ```xml
   <!-- 让spring随web启动而创建的监听器 -->
    <listener>
    	<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
    <!-- 配置spring配置文件位置参数 -->
    <context-param>
    	<param-name>contextConfigLocation</param-name>
    	<param-value>classpath:applicationContext.xml</param-value>
    </context-param>
  ```

  ​

### sturts与spring整合

- 配置常量

  ```xml
  <!-- #  struts.objectFactory = spring	将action的创建交给spring	
  			struts.objectFactory.spring.autoWire = name spring负责装配Action依赖属性
  			-->
  	<constant name="struts.objectFactory" value="spring"></constant>

  ```

- 整合方案2：spring负责创建action以及组装

  ```xml
  applicationContext.xml
  <!-- action -->
  <!-- 注意：Action一定是多例的 -->
  <bean name="userAction" class="younger.web.action.UserAction" scope="prototype">
  	<property name="userService" ref="userService"></property>
  </bean>
  ```

  ```xml
  struts.xml
  <!-- 整合方案2:class属性上填写spring中action对象的BeanName
  		 		完全由spring管理action生命周期,包括Action的创建
  		 		注意:需要手动组装依赖属性
  		  -->
  		<action name="UserAction_*" class="userAction" method="{1}" >
  			<result name="toHome" type="redirect" >/index.htm</result>
  			<result name="error" >/login.jsp</result>
  		</action>
  ```

  ​

### spring 整合 hibernate

> 将sessionFactory 对象交给 spring 容器管理

```xml
<!-- 将SessionFactory配置到spring容器中 -->
	<!-- 加载配置方案1:仍然使用外部的hibernate.cfg.xml配置信息 -->
	<!-- <bean name="sessionFactory" class="org.springframework.orm.hibernate5.LocalSessionFactoryBean" >
		<property name="configLocation" value="classpath:hibernate.cfg.xml" ></property>
	</bean> -->
	<!-- 加载配置方案2:在spring配置中放置hibernate配置信息 -->
	<bean name="sessionFactory" class="org.springframework.orm.hibernate5.LocalSessionFactoryBean" >
		<!-- 将连接池注入到sessionFactory, hibernate会通过连接池获得连接 -->
		<property name="dataSource" ref="dataSource" ></property>
		<!-- 配置hibernate基本信息 -->
		<property name="hibernateProperties">
			<props>
				<!--  必选配置 -->
				<prop key="hibernate.connection.driver_class" >com.mysql.jdbc.Driver</prop>
				<prop key="hibernate.connection.url" >jdbc:mysql:///crm_32</prop>
				<prop key="hibernate.connection.username" >root</prop>
				<prop key="hibernate.connection.password" >1234</prop> 
				<prop key="hibernate.dialect" >org.hibernate.dialect.MySQLDialect</prop>
				
				<!--  可选配置 -->
				<prop key="hibernate.show_sql" >true</prop>
				<prop key="hibernate.format_sql" >true</prop>
				<prop key="hibernate.hbm2ddl.auto" >update</prop>
			</props>
		</property>
		<!-- 引入orm元数据,指定orm元数据所在的包路径,spring会自动读取包中的所有配置 -->
		<property name="mappingDirectoryLocations" value="classpath:cn/itcast/domain" ></property>
	</bean>
```

### spring 整合 c3p0 连接池

- 配置db.properties

  ```properties
  jdbc.jdbcUrl=jdbc:mysql:///crm_32
  jdbc.driverClass=com.mysql.jdbc.Driver
  jdbc.user=root
  jdbc.password=1234
  ```

- 引入连接池到spring中

  ```xml
  <!-- 读取db.properties文件 -->
  	<context:property-placeholder location="classpath:db.properties" />
  	<!-- 配置c3p0连接池 -->
  	<bean name="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource" >
  		<property name="jdbcUrl" value="${jdbc.jdbcUrl}" ></property>
  		<property name="driverClass" value="${jdbc.driverClass}" ></property>
  		<property name="user" value="${jdbc.user}" ></property>
  		<property name="password" value="${jdbc.password}" ></property>
  	</bean>
  ```

- 将连接池注入给 SessionFactory

  ```xml
  <bean name="sessionFactory" class="org.springframework.orm.hibernate5.LocalSessionFactoryBean" >
  		<!-- 将连接池注入到sessionFactory, hibernate会通过连接池获得连接 -->
  		<property name="dataSource" ref="dataSource" ></property>
  ```

  ​

### spring 整合 hibernate环境操作数据库

> Dao类继承 HibernateDaoSupport

```java
//HibernateDaoSupport 为dao注入sessionFactory
public class UserDaoImpl extends HibernateDaoSupport implements UserDao {
	public User getByUserCode(final String usercode) {
		//HQL
		return getHibernateTemplate().execute(new HibernateCallback<User>() {		
			public User doInHibernate(Session session) throws HibernateException {
					String hql = "from User where user_code = ? ";
					Query query = session.createQuery(hql);
					query.setParameter(0, usercode);
					User user = (User) query.uniqueResult();
				return user;
			}
		});
		//Criteria
		/*DetachedCriteria dc = DetachedCriteria.forClass(User.class);
		
		dc.add(Restrictions.eq("user_code", usercode));
		
		List<User> list = (List<User>) getHibernateTemplate().findByCriteria(dc);
			
		if(list != null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}*/
	}
```

> spring 中配置 dao

```xml
<bean name="userDao" class="younger.dao.impl.UserDaoImpl">
<!-- 注入sessionFactory -->
    <property name="sessionFactory" ref="sessionFactory"></property>
</bean>
```

### spring 的 aop事务

- 准备工作

  ```xml
  <!-- 核心事务管理器 -->
  	<bean name="transactionManager" class="org.springframework.orm.hibernate5.HibernateTransactionManager" >
  		<property name="sessionFactory" ref="sessionFactory" ></property>
  	</bean>
  ```

- xml 配置 aop事务

  ```xml
  <!-- 配置通知 -->
  	 <tx:advice id="txAdvice" transaction-manager="transactionManager" >
  		<tx:attributes>
  			<tx:method name="save*" isolation="REPEATABLE_READ" propagation="REQUIRED" read-only="false" />
  			<tx:method name="persist*" isolation="REPEATABLE_READ" propagation="REQUIRED" read-only="false" />
  			<tx:method name="update*" isolation="REPEATABLE_READ" propagation="REQUIRED" read-only="false" />
  			<tx:method name="modify*" isolation="REPEATABLE_READ" propagation="REQUIRED" read-only="false" />
  			<tx:method name="delete*" isolation="REPEATABLE_READ" propagation="REQUIRED" read-only="false" />
  			<tx:method name="remove*" isolation="REPEATABLE_READ" propagation="REQUIRED" read-only="false" />
  			<tx:method name="get*" isolation="REPEATABLE_READ" propagation="REQUIRED" read-only="true" />
  			<tx:method name="find*" isolation="REPEATABLE_READ" propagation="REQUIRED" read-only="true" />
  		</tx:attributes>
  	</tx:advice> 
  	<!-- 配置将通知织入目标对象
  	配置切点
  	配置切面 -->
  	 <aop:config>
  		<aop:pointcut expression="execution(* cn.itcast.service.impl.*ServiceImpl.*(..))" id="txPc"/>
  		<aop:advisor advice-ref="txAdvice" pointcut-ref="txPc" />
  	</aop:config> 
  ```

- 注解配置 aop 事务

  ```xml
  <!-- 开启注解事务 -->
  	<tx:annotation-driven transaction-manager="transactionManager" />
  ```

  ```java
  @Transactional(isolation=Isolation.REPEATABLE_READ,propagation=Propagation.REQUIRED,readOnly=true)
  public class UserServiceImpl implements UserService{
      @Transactional(isolation=Isolation.REPEATABLE_READ,propagation=Propagation.REQUIRED,readOnly=false)
  	public void saveUser(User u) {
  		ud.save(u);
  	}
  }
  ```

### 扩大 session作用

> 为了避免使用懒加载时出现 no-session问题，需要扩大session的作用范围

配置 filter

```xml
<!-- 扩大session作用范围
  	注意: 任何filter一定要在struts的filter之前调用
   -->
   <filter>
  	<filter-name>openSessionInView</filter-name>
  	<filter-class>org.springframework.orm.hibernate5.support.OpenSessionInViewFilter</filter-class>
  </filter>


<filter-mapping>
  	<filter-name>openSessionInView</filter-name>
  	<url-pattern>/*</url-pattern>
  </filter-mapping>
```





