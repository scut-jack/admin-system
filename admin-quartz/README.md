# admin-quartz模块逻辑

## 定时任务平台的业务逻辑
1. 进入定时任务管理页面的第一步是查询已有的定时任务以及还可以看任务的详细信息（是否在运行和运行日志等），还支持任务数据的Excel导出
2. 第二步新增定时任务，注意新增任务的方法需要在代码里面提前定义【后续通过反射调用该方法执行】，否则无法通过校验无法创建任务
（这里的问题在于每次新增任务都需要重启服务，这样会影响原有任务的执行，
需优化，例如服务解析外部传进来jar包的方式生成任务；
或者单独作为一个微服务通过注册中心向其他服务拉取特定注解标明的任务）
3. 第三步修改定时任务的一些参数修改任务的状态，修改任务状态后会判断任务是否需要重新调度
4. 第四步任务的立即执行，任务执行后可以查看任务的调度日志
5. 第五步可以对已有任务进行删除

## quart框架的使用
要先了解quart框架的配置：包括Scheduler调度器的名称配置，集群配置，ThreadPool线程池配置以及JDBC-JobStoreCMT任务数据持久化的配置。
官网链接：http://www.quartz-scheduler.org/documentation/quartz-2.3.0/configuration/
1. 业务逻辑主要是定义一个任务，任务数据的CRUD，以及定义任务日志，任务日志数据的CRUD；要注意的是前端业务定义的任务并发quart框架的调度器进行调度的任务（Job），
   需要在业务代码中通过业务数据（业务任务）去创建quart框架需要的任务（Job）而后框架代码去执行任务（Job）
2. Quartz框架的核心类有以下三部分：
    a. 任务 Job ： 需要实现的任务类，实现 execute() 方法，执行后完成任务。
    b. 触发器 Trigger ： 包括 SimpleTrigger 和 CronTrigger。
    c. 调度器 Scheduler ： 任务调度器，负责基于 Trigger触发器，来执行 Job任务。
    d. 【JobDetail & Job 方式，Sheduler每次执行，都会根据JobDetail创建一个新的Job实例，可以规避并发访问问题。】
    e. 【当 Scheduler 调用一个 job，就会将 JobExecutionContext 传递给 Job 的 execute() 方法; 
        Job 能通过 JobExecutionContext 对象访问到 Quartz 运行时候的环境以及 Job 本身的明细数据。】
    f. 【有状态的 job 可以理解为多次 job调用期间可以持有一些状态信息，这些状态信息存储在 JobDataMap 中。 
        而默认的无状态 job，每次调用时都会创建一个新的 JobDataMap。】
3. 任务数据的保存可以存在内存中，问题是应用重启后任务数据丢失； 
   也可以保存在关系型数据库中，quart框架已经帮我们写好关系型数据库表结构了，
   只需要在数据库中执行quart jar包中提供的示例就可以持久化保存任务【quartz-2.3.2.jar!/org/quartz/impl/jdbcjobstore/tables_mysql_innodb.sql】
4. 项目中使用quart的核心方法为**ScheduleUtils.createScheduleJob**，
   该方法作用是根据前段页面创建的任务数据（业务数据）调用quart框架的API创建Scheduler，Trigger和Job
   为了记录任务执行的时间和日志等信息项目中任务都继承抽象类**AbstractQuartzJob**，而AbstractQuartzJob继承自quart的Job接口作为任务
5. quart中任务（Job）执行方式有两种：允许并发执行和不允许并发执行，
   允许并发执行的意思是假设任务重复调度时间是30s但是一个任务执行的时间是34s，
   那么它还没执行结束时Scheduler会创建该任务（Job）的新的JobDetail对象按时执行
6. 前端页面定义好任务后（业务数据），该项目通过反射去拉取页面上定义的数据（是否允许并发指标数据）创建并发任务（Job）和非并发任务（Job），
   同时这两种任务对象（Job）执行时会通过反射拉取前端页面定义的（业务数据）类.方法去执行【具体并发非并发的执行交给quart框架解决，它自己会用线程池来做】
7. quart框架定义的任务和触发器相关数据管理工作可以用spring框架提供的**org.springframework.scheduling.quartz.LocalDataSourceJobStore**类管理，
   这里还包括任务数据的事务管理等，详情参考quart官网