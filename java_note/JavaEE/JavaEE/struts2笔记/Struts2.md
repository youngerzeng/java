# Struts2_day01

------

[TOC]

### 1. struts2的了解

------

1.1 *struts*的优势：

- 自动封装参数


- 参数校验
- 结果的处理(转发|重定向)
- 国际化
- 显示等待页面
- 表单的防止重复提交	

### 2.struts2框架初步搭建

---

###### 1. 导包

###### 2. 书写Action类

```java
public class HelloAction{
	
	public String hello(){
		System.out.println("hello world");
		return "success";
	}
}
```

###### 3. 书写src/struts.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE struts PUBLIC
	"-//Apache Software Foundation//DTD Struts Configuration 2.3//EN"
	"http://struts.apache.org/dtds/struts-2.3.dtd">
<struts>
	<constant name="struts.i18n.encoding" value="UTF-8"></constant>
	
	<package name="hello" namespace="/hello" extends="struts-default">
		<action name="HelloAction_*" class="younger.HelloAction" method="{1}">
			<result name="success">/index.jsp</result>
	</action>		
	</package>
	
</struts>
```

###### 4. 将struts2核心过滤器配置到web.xml

```xml
<filter>
  	<filter-name>struts2</filter-name>
  	<filter-class>org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter</filter-class>
  </filter>
  <filter-mapping>
  	<filter-name>struts2</filter-name>
  	<url-pattern>/*</url-pattern>
  </filter-mapping>
```

### 3. struts2 访问流程 &struts2架构

---

![struts2访问流程 &struts2架构](F:\JavaEE\06-struts2 (第39-42天)\struts2-day01\struts2-day01\struts2.bmp)

​

​

​

​

​

​

​

​



### 4.配置详解

----

####struts.xml配置	

```xml
<!-- package:将Action配置封装，就是可以在Package中配置很多action。
	name属性：必选。给包起个名字，起到标识作用，随便起，不能和其他包名重复；
	namepsace属性：给 action 的访问路径命名空间
 	extends属性：继承一个指定包 (struts-dafult)
	abstract 属性：包是否为抽象，标识性属性，标识该包不能独立运行，专门被继承(true,false)
-->
<package name="hello" namespace="/hello" extends="struts-dafult"></package>
```

``` xml
<!-- action元素：配置action类
	name属性：必填，Action访问资源名，默认执行execute
	class属性：action的完整类名
	method属性：指定调用Action中的哪个方法来处理请求
-->
<action name="HelloAction" class="younger.Hello.HelloAction" method="hello"></action>
```

```xml
<!--result元素：结果配置
	name属性：标识结果处理的名称，与action方法返回值对应
	type属性：指定调用哪个result类来处理结果，默认使用转发
	标签体：填写页面的相对路径
-->
<result name="success" type="dispatcher" >/hello.jsp</result>
```

```xml
<!--引入其他struts配置文件-->
<include file="younger/struts.xml"></include>
```

#### struts修改常量配置

struts **配置顺序**:  ***struts.xml***   ,   struts.properties  ,   web.xml

struts 默认**常量配置位置**：struts2-core-2.3.24.jar/org.apache.struts2/defautlt..properties

修改struts 常量配置 :

	>**方式1：src/struts.xml **  (推荐)

```xml
<struts><constant name="struts.il8n.encoding" value="UTF-8"></constant></struts>
```

> 方式2： 在src下创建sturts.properties

```xml
struts.il8n.encoding=UTF-8
```

> 方式3： 在项目的web.xml中

```xml
<!--配置常量-->
<context-param>
	<param-name>struts.i18n.encoding</param-name>
    <param-value>UTF-8</param-value>
</context-param>
```

####struts常量配置

```xml
<!-- i18n:国际化，解决post提交乱码-->
<constant name="struts.i18n.encoding" value="UTF-8"></constant>
<!--指定反问action时的后缀名-->
<constant name="struts.action.extension" value="action"></constant>
<!--指定struts2 是否以开发模式运行
		1.热加载主配置（不需要重启即可生效）
		2.提供更多错误信息输出，方便开发时的调试
-->
<constant name="struts.devMode" value="true"></constant>
```

**分模块开发的配置**：导入其他的标准struts.xml配置文件

```xml
<struts> <incluede file="struts-shop.xml"></incluede> </struts>
```

**动态方法调用:**

> 1.开启动态方法常量：
>
> <constant name="struts.enable.DynamicMethodInvocation" value="true"></constant>
>
> 访问： http://localhost:8080/struts/HelloAction!find.action

> 2. 通配符方式调用：**(推荐)**
>
>    使用{1} 取出第一个星号通配内容
>
>    <action name="DemoAction_*" class="younger.DemoAction" method="{1}"></action>

#### Action的书写方式####

1. 创建POJO类，不继承任何类
2. 实现 Action 接口
3. 继承 ***ActionSupoort*** 类


### 5.结果跳转方式

---

- 转发

  ```xml
  <action name="Demo1Action" class="younger.Demo1Action" method="execute">
  	<result name="success" type="dispatcher">/hello.jsp</result>
  </action>
  ```

- 重定向:  **type="redirect"**

- 转发到Action

  ```xml
  <action name="Demo2Action" class="younger.Demo2Action" method="execute">
  	<result name="success" type="chain">
          <!-- action的名字 -->
      	<param name="actionName">Demo1Action</param>
          <!-- action	所在的命名空间 -->
          <param name="namespace">/</param>
      </result>
  </action> 
  ```

- 重定向到Action:  **type="redirectAction"**

### 6.访问servletAPI方式

---

- 原理

  **ActionContext**

  |        Map         |      数据中心       |
  | :----------------: | :-----------------: |
  |    原生resquest    | HttpServletResquest |
  |    原生response    | HttpServletResponse |
  | 原生ServletContext |   ServletContext    |
  |     request域      |         Map         |
  |     Session域      |         Map         |
  |   application域    |         Map         |
  |     param参数      |         Map         |
  |      aattr域       |   Map  3个域合一    |
  |     ValueStack     |        值栈         |

- **通过ActionContext**(推荐)

```java
//request域 --> map(struts不推荐使用原生request)
//不推荐
Map<String,Object> requestScope = (Map<String,Object>)ActionContext.get("request");
//推荐
ActionContext.getContext().put("name","requestTom");
ActionContext.getContext().getSession().put("name","sessionTom");
ActionContext.getContext().getApplication().put("name","application");
```

- 通过ServletActionContext(耦合度高，不推荐)

  ```java
  HttpServletRequest request = ServletActionContext.getRequest();
  HttpSession session = request.getSession();
  HttpoServletResponse response =ServletActionContext.getResponse();
  ServletContext servletContext = ServletActionContext.getServletContext();
  ```

- 通过实现接口方式(ServletRequestAware)

### 如何获得参数

---

- Action 生命周期：每次请求创建一个新的Action实例。线程安全，可以使用成员变量接收参数

- 属性驱动

  ```jsp
  <form action="${pageContext.request.contextPath}/DemoAction">
      用户名：<input type="text" name="name"/><br>
      年龄：<input type="text" name="age"/><br>
      生日：<input type="text" name="birthday"/><br>
      <input type="submit" value="提交"/>
  </form>
  ```

  ```java
  //准备与参数名相同的属性
  private String name;
  //自动类型转换 ，只能转换8大基本数据类型以及对应包装类
  private Integer age;
  //支持特定类型字符串转换为 Date，例如 yyyy-MM-dd
  private Date birthday;
  ...各属性的get/set方法...
  ```

- 对象驱动

```java
public class DemoAction extends ActionSupport{
    //准备user对象
    private User user;
    .....User的get/set方法....    
}
```

- 模型驱动

  ```java
  public class DemoAction extends ActionSupport implements ModelDriven<User>{
      private User user = new User();
      public User getModel(){
          return user;
      }
  }
  ```

### 7.集合类型参数封装

---

- list

  ```jsp
  <form action="${pageContext.request.contextPath}/strutsDemo.action"
        method="post">
      名称：<input type="text" name="list[0].name"><br/>
      年龄：<input type="text" name="list[0].age"><br/>
      名称：<input type="text" name="list[1].name"><br/>
      年龄：<input type="text" name="list[1].age"><br/>
      <input type="submit" value="提交">
  </form>
  ```

  ```java
  public class StrutsDemo extends ActionSupport{
      private List<User> list;
      public List<User> getList(){
          return list;
      }
      public void setList(List<User> list){
          this.list = list;
      }
  }
  ```

- Map

  ```jsp
  <form action="${pageContext.request.contextPath}/strutsDemo.action"
        method="post">
      名称：<input type="text" name="map['one'].name"><br/>
      年龄：<input type="text" name="map['one'].age"><br/>
      名称：<input type="text" name="map['two'].name"><br/>
      年龄：<input type="text" name="map['two'].age"><br/>
      <input type="submit" value="提交">
  </form>
  ```

  ```java
  public class StrutsDemo extends ActionSupport{
      private Map<User> map;
      public Map<User> getMap(){
          return map;
      }
      public void seMap(Map<User> map){
          this.map = map;
      }
  }
  ```

  ​

### 8.OGNL表达式

---

> ognl:对象视图导航语言（例如${user.addr.name}）。

- 导包(struts包已包含)

- 代码准备

  ```java
  //准备OGNLContext
  //Root
  User rootUser = new User("tom",18);
  //Context
  Map<String,User> context = new HashMap<String,User>();
  context.put("user1",new User("jack",18));
  context.put("user2",new User("rose",22));
  OgnlContext oc = new OgnlContext();
  oc.setRoot(rootUser);
  oc.setValues(context);
  ```

- 语法

  - 基本取值

    ```java
    //取出root中user对象的name属性
    String name = (String) Ognl.getValue("name",oc,oc.getRoot());
    //取出context中user1对象的name属性
    String name = (String) Ognl.getValue("#)
    ```

  - 赋值

    ```java
    Ognl.getValue("name='jerry'",oc,oc.getRoot());
    Ognl.getValue("#user1.name='younger'",oc,oc.getRoot());
    ```

  - 调用方法

    ```java
    Ognl.getValue("#user1.setName('lilei')",oc,oc.getRoot());
    ```

  - 调用静态方法

    ```java
    //Double pi = (Double) Ognl.getValue("@java.lang.Math@PI",oc,oc.getRoot());
    Double pi = (Double) Ognl.getValue("@@PI",oc,oc.getRoot());
    ```

  - 创建对象（List，Map）

    ```java
    //创建list对象
    Integer size = (Integer) Ognl.getValue("{'tom','jerry','jack','rose'}.size()",oc,oc.getRoot());
    String name = (String) Ognl.getValue("{'tom','jerry','jack'}[0]",oc,oc.getRoot());
    ```

    ​

### OGNL与Struts结合

---

> 结合原理：OGNLContetxt ---> ValueStack 值栈
>
> ValueStack:  Root  : 当前访问的Action对象
>
> ​		      Context： ActionContext数据中心
>
> ```java
> //ValueStack中的两部分
> CompoundRoot root;
> transient Map<String,Object> context;
> ```
>
> ```java
> //获得值栈
> ValueStack vs = ActionContext.getContext().getValueStack();
> vs.push(u);
> ```
>
> 