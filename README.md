# 1. EmberLib

## 1.1. Usage

## 1.2. Models

### 1.2.1. Cache

在一些数据存取频繁的场景，缓存相对于数据库来说，是更好的选择。

本框架的缓存是在 `HashMap` 上实现的，在其基础上，本模块为存储的值添加的生命周期，并称其为 `Cell`。该模块的运作的思想来源于[Redis](https://redis.io/)，
不同之处主要在于本模块的数据是直接存储在内存之中。

本插件提供了公用缓存服务，但是多插件操作并没有权限判断，因此存储于此的数据是不安全的。即便如此，您仍然可以在此进行一些插件间的数据共享。

``` java
// todo 获取公用缓存服务

// 存数据的键避免与其他插件冲突
organism.set("myplugin.example.test", "value");
```

如果您想将自己插件的缓存保存到磁盘，并在下次服务器启动时加载，本模块提供了缓存的持久化操作，
其中过期的数据不会被存储，限期的数据期限不会因为持久化而改变。

``` java
// 加载数据到缓存
organism.load(this);

// 将缓存保存到插件目录下
organism.save(this);
```

**注意**：持久化可用于您自己创建的缓存上，切勿在公用缓存上，否则容易引起数据混乱

### 1.2.2. Database

数据库是持久化最常用的方式之一，但数据库的一些操作在 Java 的实践中较为繁琐，
过多的重复代码大大减少了开发的速度。

此模块借鉴了 [MyBatis](https://github.com/mybatis/mybatis-3/tree/master/src/main/java/org/apache/ibatis/annotations)。意在使用代码生成来简化 `jdbc` 的使用。

通过分层设计，我们可以将不同职责的代码分离到不同的层次中，提高了代码的可维护性和可扩展性。同时，各层次之间的依赖关系也使得代码更加清晰和易于理解。

ENTITY层

``` java
// todo
```

DAO层

``` java
// todo
```

SERVICE层

``` java
// todo
```

### 1.2.3. Config

### 1.2.4. Commands

简化指令管理，指令参数提示

### 1.2.5. Item

### 1.2.6. Text

### 1.2.7. Guide

``` txt
guide
  ├── view
  │     ├── page ⇌ page ⇌ page
  │     └── page
  └── view
        └── page ⇌ page
```
