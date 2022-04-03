
# Jackson
方法功能如下：

![1648979189785](.assets\Readme\1648979189785.png)

1
对象转jacksonObject
对象转jacksonArray

2
string转jacksonObject
string转jacksonArray

3
对象类型互转

4
对象转string
string转对象

5
对象转bytes

6
this转string
this转对象



# JacksonObject

![1648978943486](.assets\Readme\1648978943486.png)

**1.获取Jackson对象**
getJacksonObject(String key)
getJacksonArray(String key)

**2.获取原始对象**
getObject(String key)
getNode(String key)

**3.获取具体类型对象**
getObject(String key, Class<T> clazz)
getJavaObject(String key)
getObject(String key, TypeReference<T> typeReference)

**4.获取包装或原子类型**
Boolean
Bytes
Short
Integer
Long
Float
Double
BigDecimal
BigInteger
String
Date

**5.属性操作**
isEmpty()
size()
contains()
put()
remove()
map()
ifPresent()



# JacksonObject

![1648983679631](.assets\Readme\1648983679631.png)

**1.获取Jackson对象**
getJacksonObject(int index)
JacksonArray getJacksonArray(int index)

**2.获取具体类型对象**
getObject(int index, Class<T> clazz)
getObject(int index, TypeReference<T> typeReference)

**3.获取包装或原子类型**

**4.属性操作**