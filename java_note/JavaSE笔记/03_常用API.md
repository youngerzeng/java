# 常用API

[TOC]

## Object

### 重写equals

- Object类中的equals方法:比较的是对象地址，方法内使用"=="

重写equals方法：根据对象中的属性值进行比较

###重写toString 

Object类中的toString :

- f返回的是内存地址（对象的类型 + @ + 内存地址值）

  `getClass().getName() + "@" + Integer.toHexString(hashCode())`

重写toStirng,返回类中所有成员变量的值

##String

####String 类的使用

* Java 程序中的所有字符串字面值（如 "abc" ）都作为此类的实例实现。
* 字符串是常量,在创建之后不能更改
* 一旦这个字符串确定了，那么就会在内存区域中就生成了这个字符串。字符串本身不能改变，但str变量中记录的地址值是可以改变的。


* 源码分析,String类底层采用的是字符数组:
    private final char value[]
    - private外部无法获取value数组,就无法改变数组中元素的值
    - final修饰说明value是常量,一旦创建初始化,就不能被改变


* String s3 = "abc"：在内存中只有一个对象。这个对象在字符串常量池中
* String s4 = new String("abc");
    - 在内存中有两个对象。一个new的对象在堆中
    - 一个字符串本身对象，在字符串常量池中

####String类的构造方法

* public String():空构造
* public String(byte[] bytes):把字节数组转成字符串
	 public String(byte[] bytes,int index,int length):把字节数组的一部分转成字符串		
* public String(String original):把字符串常量值转成字符串


* public String(char[] value):把字符数组转成字符串
* public String(char[] value,int index,int count):把字符数组的一部分转成字符串

####String类的成员方法

* int length(): 返回字符串的长度

* String substring(int beginIndex,int endIndex): 获取字符串的一部分

* String substring(int beginIndex): 获取字符串的一部分

* boolean startsWith(String prefix): 判断字符串是不是另一个字符串的前缀,开头

* boolean endsWith(String prefix): 判断字符串是不是另一个字符串的后缀,结尾

* boolean contains (String s): 判断一个字符串中,是否包含另一个字符串

* int indexOf(char ch):  查找在字符串中第一次出现ch的索引,不存在返回-1

* byte[] getBytes(): 将字符串转成字节数组

* char[] toCharArray(): 将字符串转成字符数组

* boolean equals(Object obj): 判断字符串中的字符是否完全相同
   boolean equalsIgnoreCase(String s): 判断字符串中的字符是否相同,忽略大小写	

* char charAt(int index):返回指定索引处值

   ​


###StringBuffer

* 线程安全的可变字符序列 
* 底层采用字符数组实现,初始容量为16

成员方法：

* StringBuffer append(), 将任意类型的数据,添加缓冲区
* delete(int start,int end): 删除缓冲区中字符
* insert(int index, 任意类型): 将任意类型数据,插入到缓冲区的指定索引上
* replace(int start,int end, String str): 将指定的索引范围内的所有字符,替换成新的字符串
* reverse(): 将缓冲区中的字符反转
* String toString(): 继承Object,重写toString()
* char charAt (int index):返回指定索引处的char值
* int indexOf(String str)
* String substring(int start):截取，返回新的String

### StringBuilder

String,StringBuffer,StringBuilder区别

* String：不可变的字符序列


* StringBuffer：可变  ,是线程安全的,效率低
* StringBuilder：可变  ,是线程不安全的,效率高

## Date

毫秒值：

* java.util.Date
* 时间原点; 公元1970年1月1日,午夜0:00:00 英国格林威治  毫秒值就是0
* System.currentTimeMillis() 返回值long类型参数，获取当前日期的毫秒值

方法：

* public Date()
* public Date(long times)


* public long getTime()	：将当前的日期对象，转为对应的毫秒值
* public void setTime(long times)：根据给定的毫秒值，生成对应的日期对象

### SimpleDateFormat

完成日期和文本之间的转换,将日期格式化

```java
DateFormat df= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//对日期进行格式化
Date date = new Date(1607616000000L);
String str_time = df.format(date);
System.out.println(str_time);//2020年12月11日
```

- String format(Date date):Date格式化为时间字符串
- Date parse (String source)：字符串生成日期

### Calendar

日历类(抽象类) ：getInstance()获取对象

* Date getTime() ：把日历对象,转成Date日期对象
* int get(日历字段) ：获取指定日历字段的值
* set(int field,int value)  设置指定的时间
* set(int year,int month,int day) 传递3个整数的年,月,日


* add(int field, int value) 进行整数的偏移
* int get(int field) 获取指定字段的值

## Integer

- static int parseInt(String s) 字符串转整数
- static String toString(int i)  整数转字符
- 十进制转成二进制String  toBinarString(int)
- 十进制转成八进制 String  toOctalString(int)
- 十进制转成十六进制 String toHexString(int)
- MAX_VALUE : 2^32-1
- MIN_VALUE :  - 2^31
- 装箱：Integer  Integer.valueOf(整数值)
- 拆箱：int  Integer对象.intValue()

## System

-  static long currentTimeMillis() : 获取系统当前毫秒值
-  static void exit(0)  : 退出虚拟机,所有程序全停止
- static void gc() :当没有更多引用指向该对象时,会自动调用垃圾回收机制回收堆中的对象, 同时调用回收对象所属类的finalize方法()
- static Properties getProperties()  : 获取当前操作系统的属性:例如操作系统名称
- arraycopy(Object src, int srcPos, Object dest, int destPos, int length)

## Math

- static double sqrt(double d)	返回参数的平方根
- static double pow(double a, double b)     a的b次方
- static double floor(double d)    返回小于或者等于参数d的最大整数
- static double ceil(double d)    返回大于或者等于参数d的最小整数
- static int abs(int i)    获取参数的绝对值
- static double round(doubl d)    获取参数的四舍五入,取整数
- static double random() 返回随机数 [0.0-1.0)

## Arrays

-  static String toString(数组)    将数组变成字符串
- static int binarySearch(数组, 被查找的元素)   不存在返回-1
- static void sort(数组)    升序排列
- static int[]  copyOf(int[] original,int newLength)  :复制数组
- static boolean equals(int[] a,int[] a2) :比较两个数组是否相等

## BigInteger

- BigInteger(string val) ：构造方法，可以传递任意长度的数字格式字符串
- b1+b2对象的和 ：b1.add(b2)
- b1-b2对象的差：b1.subtract(b2)
- b1*b2对象的乘积 ：b1.multiply(b2)
- b2/b1：b2.divide(b1)

##BigDecimal

超级大型的浮点数据,提供高精度的浮点运算

方法同上