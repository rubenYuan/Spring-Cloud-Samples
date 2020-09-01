#Spring Boot概述

一：引导应用程序上下文

    一个Spring Cloud应用程序通过创建一个“引导”上下文来进行操作，这个上下文是主应用程序的父上下文。开箱即用，负责从外部源加载配置属性，还解密本地外部配置文件中的属性。这两个上下文共享一个Environment，这是任何Spring应用程序的外部属性的来源。Bootstrap属性的优先级高，因此默认情况下不能被本地配置覆盖。
    引导上下文使用与主应用程序上下文不同的外部配置约定，因此使用bootstrap.yml application.yml（或.properties）代替引导和主上下文的外部配置。例：
    bootstrap.yml

    spring:
      application:
    name: foo
      cloud:
    config:
      uri: ${SPRING_CONFIG_URI:http://localhost:8888}
    如果您的应用程序需要服务器上的特定于应用程序的配置，那么设置spring.application.name（在bootstrap.yml或application.yml）中是个好主意。
    您可以通过设置spring.cloud.bootstrap.enabled=false（例如在系统属性中）来完全禁用引导过程。

二：改变引导位置Properties
    这些属性的行为类似于具有相同名称的spring.config.*变体，实际上它们用于通过在其Environment中设置这些属性来设置引导ApplicationContext。如果在正在构建的上下文中有活动的配置文件（来自spring.profiles.active或通过Environment API）），则该配置文件中的属性也将被加载，就像常规的Spring Boot应用程序。

三：覆盖远程Properties的值

    通过引导上下文添加到应用程序的属性源通常是“远程”（例如从配置服务器），并且默认情况下，不能在本地覆盖，除了在命令行上。如果要允许您的应用程序使用自己的系统属性或配置文件覆盖远程属性，则远程属性源必须通过设置spring.cloud.config.allowOverride=true（在本地设置本身不起作用）授予权限。一旦设置了该标志，就会有一些更精细的设置来控制远程属性与系统属性和应用程序本地配置的位置：spring.cloud.config.overrideNone=true覆盖任何本地属性源，spring.cloud.config.overrideSystemProperties=false如果只有系统属性和env var应该覆盖远程设置，而不是本地配置文件。


四：自定义引导属性源


    引导过程添加的外部配置的默认属性源是Config Server，但您可以通过将PropertySourceLocator类型的bean添加到引导上下文（通过spring.factories）添加其他源。您可以使用此方法从其他服务器或数据库中插入其他属性。

    作为一个例子，请考虑以下微不足道的自定义定位器：

    @Configuration
    public class CustomPropertySourceLocator implements PropertySourceLocator {

     @Override
     public PropertySource<?> locate(Environment environment) {
           return new MapPropertySource("customProperty",
                   Collections.<String, Object>singletonMap("property.from.sample.custom.source", "worked as intended"));
     }

    }

    传入的Environment是要创建的ApplicationContext的Environment，即为我们提供额外的属性来源的。它将已经具有正常的Spring Boot提供的资源来源，因此您可以使用它们来定位特定于此Environment的属性源（例如通过将其绑定在spring.application.name上，如在默认情况下所做的那样Config Server属性源定位器）。

五：环境变化

    应用程序将收听EnvironmentChangeEvent，并以几种标准方式进行更改（用户可以以常规方式添加ApplicationListeners附加ApplicationListeners）。当观察到EnvironmentChangeEvent时，它将有一个已更改的键值列表，应用程序将使用以下内容：

      重新绑定上下文中的任何@ConfigurationProperties bean

      为logging.level.*中的任何属性设置记录器级别

    请注意，配置客户端不会通过默认轮询查找Environment中的更改，通常我们不建议检测更改的方法（尽管可以使用@Scheduled注释进行设置）。如果您有一个扩展的客户端应用程序，那么最好将EnvironmentChangeEvent广播到所有实例，而不是让它们轮询更改（例如使用Spring Cloud总线）。

六：刷新范围

    当配置更改时，标有@RefreshScope的Spring @Bean将得到特殊处理。这解决了状态bean在初始化时只注入配置的问题。例如，如果通过Environment更改数据库URL时DataSource有开放连接，那么我们可能希望这些连接的持有人能够完成他们正在做的工作。然后下一次有人从游泳池借用一个连接，他得到一个新的URL。

    刷新范围bean是在使用时初始化的懒惰代理（即当调用一个方法时），并且作用域作为初始值的缓存。要强制bean重新初始化下一个方法调用，您只需要使其缓存条目无效。

    RefreshScope是上下文中的一个bean，它有一个公共方法refreshAll()来清除目标缓存中的范围内的所有bean。还有一个refresh(String)方法可以按名称刷新单个bean。此功能在/refresh端点（通过HTTP或JMX）中公开。

七：端点

    对于Spring Boot执行器应用程序，还有一些额外的管理端点：

    POST到/env以更新Environment并重新绑定@ConfigurationProperties和日志级别

    /refresh重新加载引导带上下文并刷新@RefreshScope bean

    /restart关闭ApplicationContext并重新启动（默认情况下禁用）

    /pause和/resume调用Lifecycle方法（stop()和start() ApplicationContext）


Spring Cloud

一：Spring Cloud-前言

1.微服务架构概念：
    
    简而言之，微服务架构就是将一个完整的应用从数据存储开始垂直拆分成多个不同的服务。
    每个服务都能独立部署、独立维护、独立扩展，服务与服务间通过诸如RESTful API的方式互相调用。
    即微服务是自治的服务单元。

2.Spring Boot回顾：
    
    Spring Boot让我们的Spring应用变的更轻量化。具有如下优势：
        1.为所有Spring开发者更快的入门
        2.开箱即用，提供各种默认配置来简化项目配置
        3.内嵌式容器简化Web项目
        4.没有冗余代码生成和XML配置的要求

3.微服务架构进化
    
    服务化的核心就是将传统的一站式应用根据业务拆分成一个一个的服务，而微服务在这个基础上要更彻底地去耦合，并且强调DevOps和快速演化。
    DevOps是英文Development和Operations的合体，他要求开发、测试、运维进行一体化的合作，进行更小、更频繁、更自动化的应用发布，以及围绕应用架构来构建基础设施的架构。

    3.1.服务化之Nginx
        nginx通过接受客户端Http请求，根据路径配置，转发，跳转相应的服务。
        缺点：
        1.Nginx配置中存在服务调用的逻辑
        2.服务消费者不知道，真正服务提供者的实例。
        3.服务提供者不易管理
    也正是以上的缺点，演变出Dubbo

    3.2.服务化之Dubbo
        Dubbo是阿里开源的一个SOA服务治理解决方案。服务消费者和提供者都可将服务信息注册到Register，形成了服务中心的组件。通过Monitor进行很好的服务管理，消费者可以进行负载均衡，服务降级等。
        缺点：
        1.致命的缺点-维护停止（阿里目前又着手维护）
        2.Dubbo严重依赖于第三方组件（Zookeeper／Redis）
        3.由于Dubbo的RPC调用，使得服务提供方与消费方有着代码层次的高强度耦合。


4.服务化之Spring Cloud
    
    SpringCloud提出是开发面向云端的Application，为微服务提供了全套的组件技术支撑。值得注意的是：Spring Cloud抛弃了Dubbo的RPC通信，采用了基于Http的Rest方式通信。


    4.1.Spring Cloud的大家庭组成

        服务治理：这是Spring Cloud的核心。目前Spring Cloud主要通过整合Netflix的相关产品来实现这方面的功能（Spring Cloud Netflix），包括用于服务注册和发现的Eureka，调用断路器Hystrix，调用端负载均衡Ribbon，Rest客户端Feign，智能服务路由Zuul，用于监控数据收集和展示的Spectator、Servo、Atlas，用于配置读取的Archaius和提供Controller层Reactive封装的RxJava。

        分布式链路监控：Spring Cloud Sleuth提供了全自动、可配置的数据埋点，以收集微服务调用链路上的性能数据，并发送给Zipkin进行存储、统计和展示。

        消息组件：Spring Cloud Stream对于分布式消息的各种需求进行了抽象，包括发布订阅、分组消费、消息分片等功能，实现了微服务之间的异步通信。Spring Cloud Stream也集成了第三方的RabbitMQ和Apache Kafka作为消息队列的实现。而Spring Cloud Bus基于Spring Cloud Stream，主要提供了服务间的事件通信（比如刷新配置）。

        配置中心：基于Spring Cloud Netflix和Spring Cloud Bus，Spring又提供了Spring Cloud Config，实现了配置集中管理、动态刷新的配置中心概念。配置通过Git或者简单文件来存储，支持加解密。

        安全控制：Spring Cloud Security基于OAUTH2这个开放网络的安全标准，提供了微服务环境下的单点登录、资源授权、令牌管理等功能。

        命令行工具：Spring Cloud Cli提供了以命令行和脚本的方式来管理微服务及Spring Cloud组件的方式。


二：Sprint Cloud 服务发现与注册--Eureka

    默认情况下，DiscoveryClient的实现将使用远程发现服务器自动注册本地Spring Boot服务器。可以通过在@EnableDiscoveryClient中设置autoRegister=false来禁用此功能。而，EurekaClient将实现通过Eureka注册的服务发现。
    ServiceRegistry

    Commons现在提供了一个ServiceRegistry接口，它提供了诸如register(Registration)和deregister(Registration)之类的方法，允许您提供定制的注册服务。Registration是一个标记界面。

    @Configuration
    @EnableDiscoveryClient(autoRegister=false)
    public class MyConfiguration {
    private ServiceRegistry registry;

    public MyConfiguration(ServiceRegistry registry) {
        this.registry = registry;
    }

    // called via some external process, such as an event or a custom actuator endpoint
    public void register() {
        Registration registration = constructRegistration();
        this.registry.register(registration);
    }
}



    每个ServiceRegistry实现都有自己的Registry实现。
    Eureka服务自动注册两种实现方案：@EurekaClient 无须在配置文件中配置服务注册中心
    @DiscoveryClint 须在配置文件中配置服务注册中心

    默认情况下，ServiceRegistry实现将自动注册正在运行的服务。要禁用该行为，有两种方法。
    您可以设置@EnableDiscoveryClient(autoRegister=false)永久禁用自动注册。
    您还可以设置spring.cloud.service-registry.auto-registration.enabled=false以通过配置禁用该行为。

    Spring RestTemplate作为负载平衡器客户端

    RestTemplate可以自动配置为使用功能区。要创建负载平衡RestTemplate创建RestTemplate @Bean并使用@LoadBalanced限定符。
    警告
	通过自动配置不再创建RestTemplate bean。它必须由单个应用程序创建。

    @Configuration
    public class MyConfiguration {

    @LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
    }

    public class MyClass {
    @Autowired
    private RestTemplate restTemplate;

    public String doOtherStuff() {
        String results = restTemplate.getForObject("http://stores/stores", String.class);
        return results;
    }
}

    URI需要使用虚拟主机名（即服务名称，而不是主机名）。Ribbon客户端用于创建完整的物理地址。
    多个RestTemplate对象

    如果你想要一个没有负载平衡的RestTemplate，创建一个RestTemplate bean并注入它。要创建@Bean时，使用@LoadBalanced限定符来访问负载平衡RestTemplate。
    重要
	请注意下面示例中的普通RestTemplate声明的@Primary注释，以消除不合格的@Autowired注入。

    @Configuration
    public class MyConfiguration {

    @LoadBalanced
    @Bean
    RestTemplate loadBalanced() {
        return new RestTemplate();
    }

    @Primary
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
    }

    public class MyClass {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    @LoadBalanced
    private RestTemplate loadBalanced;

    public String doOtherStuff() {
        return loadBalanced.getForObject("http://stores/stores", String.class);
    }

    public String doStuff() {
        return restTemplate.getForObject("http://example.com", String.class);
    }
    }

    如果您看到错误java.lang.IllegalArgumentException: Can not set org.springframework.web.client.RestTemplate field com.my.app.Foo.restTemplate to com.sun.proxy.$Proxy89，请尝试注入RestOperations或设置spring.aop.proxyTargetClass=true。

    忽略网络接口

    有时，忽略某些命名网络接口是有用的，因此可以将其从服务发现注册中排除（例如，在Docker容器中运行）。可以设置正则表达式的列表，这将导致所需的网络接口被忽略。以下配置将忽略“docker0”接口和以“veth”开头的所有接口。
    application.yml

    spring:
      cloud:
    inetutils:
      ignoredInterfaces:
        - docker0
        - veth.*




1.选择Eureka理由
    
    对于服务治理而言，核心部分就是服务发现与注册。一般常见的有Zookeeper，而Spring Cloud推荐使用的是Eureka。
    因为在分布式系统中，有一个CAP定理，即 Consistency（一致性）、 Availability（可用性）、Partition tolerance（分区容错性），三者不可得兼。Zookeeper是Hadoop的子项目，它的作用也多作为服务的发现与注册。
    Zookeeper在CAP定理中满足的CP，也就是一致性和分区容错性，但是它不能保证服务的可用性。比如，服务消费者通过注册列表获取数据时，倘若，Zookeeper正在选主导致服务不可用，亦或者大多数服务宕机。
    在一般分布式系统的数据存储场景，数据一致性应该是首先被保证的。然而在服务发现的场景中，服务消费者能够消费才是首先保证的。

2.Eureka组件
    
    Eureka由多个instance(服务实例)组成，这些服务实例可以分为两种：Eureka Server和Eureka Client。为了便于理解，我们将Eureka client再分为Service Provider和Service Consumer。
    Eureka Server：服务的注册中心，负责维护注册的服务列表。
    Service Provider：服务提供方，作为一个Eureka Client，向Eureka Server做服务注册、续约和下线等操作，注册的主要数据包括服务名、机器ip、端口号、域名等等。
    Service Consumer：服务消费方，作为一个Eureka Client，向Eureka Server获取Service Provider的注册信息，并通过远程调用与Service Provider进行通信。

    2.1.Eureka Server
        Eureka Server作为一个独立的部署单元，以REST API的形式为服务实例提供了注册、管理和查询等操作。同时，Eureka Server也为我们提供了可视化的监控页面，可以直观地看到各个Eureka Server当前的运行状态和所有已注册服务的情况。
        Eureka Server可以运行多个实例来构建集群，解决单点问题，与Zookeeper不同的是，Eureka的集群是peer to peer每个节点都是对等的，无主次之分，提高了服务的可用性。
        Eureka Server节点启动后，会首先尝试从邻近节点获取所有实例注册表信息，完成初始化。Eureka Server通过getEurekaServiceUrls()方法获取所有的节点，并且会通过心跳续约的方式定期更新。
        默认配置下，如果Eureka Server在一定时间内没有接收到某个服务实例的心跳，Eureka Server将会注销该实例（默认为90秒，通过eureka.instance.lease-expiration-duration-in-seconds配置）。
        当Eureka Server节点在短时间内丢失过多的心跳时（比如发生了网络分区故障），那么这个节点就会进入自我保护模式。

        自我保护模式：
        默认配置下，如果Eureka Server每分钟收到心跳续约的数量低于一个阈值（instance的数量*(60/每个instance的心跳间隔秒数)*自我保护系数），并且持续15分钟，就会触发自我保护。
        在自我保护模式中，Eureka Server会保护服务注册表中的信息，不再注销任何服务实例。当它收到的心跳数重新恢复到阈值以上时，该Eureka Server节点就会自动退出自我保护模式。
        它的设计哲学前面提到过，那就是宁可保留错误的服务注册信息，也不盲目注销任何可能健康的服务实例。该模式可以通过eureka.server.enable-self-preservation = false来禁用，同时eureka.instance.lease-renewal-interval-in-seconds可以用来更改心跳间隔，eureka.server.renewal-percent-threshold可以用来修改自我保护系数（默认0.85）。


    2.2.Eureka Client
        2.2.1 服务注册
                启动时，会调用服务注册方法，向Eureka Server注册自己的信息。Eureka Server会维护一个已注册服务的列表。当实例状态发生变化时（如自身检测认为Down的时候），也会向Eureka Server更新自己的服务状态，同时用replicateToPeers()向其它Eureka Server节点做状态同步。

        2.2.2 续约与剔除
                服务实例启动后，会周期性地向Eureka Server发送心跳以续约自己的信息，避免自己的注册信息被剔除。续约的方式与服务注册基本一致：首先更新自身状态，再同步到其它Peer。如果Eureka Server在一段时间内没有接收到某个微服务节点的心跳，Eureka Server将会注销该微服务节点（自我保护模式除外）。

        2.2.3 服务消费
                Service Consumer本质上也是一个Eureka Client。它启动后，会从Eureka Server上获取所有实例的注册信息，包括IP地址、端口等，并缓存到本地。这些信息默认每30秒更新一次。前文提到过，如果与Eureka Server通信中断，Service Consumer仍然可以通过本地缓存与Service Provider通信。


    2.3.Eureka有三处缓存和一处延迟造成
        2.3.1 Eureka Server对注册列表进行缓存，默认时间为30s。
        2.3.2 Eureka Client对获取到的注册信息进行缓存，默认时间为30s。
        2.3.3 Ribbon会从上面提到的Eureka Client获取服务列表，将负载均衡后的结果缓存30s。

java -jar -Dspring.profiles.active=peer3  eureka-server/target/eureka-server.jar


三：Spring Cloud 服务调用端负载均衡--Ribbon

    Ribbon是Netflix发布的开源项目，主要功能是为REST客户端实现负载均衡。常见的组件
    1.ServerList 负载均衡使用的服务器列表。这个列表会缓存在负载均衡器中，并定期更新。当Ribbon与Eureka结合使用时，ServerList的实现类就是DiscoveryEnabledNIWSServerList，它会保存Eureka Server中注册的服务实例表。
    2.ServerListFilter 服务器列表过滤器。这是一个接口，主要用于对Service Consumer获取到的服务器列表进行预过滤，过滤的结果也是ServerList。Ribbon提供了多种过滤器的实现
    3.IPing 探测服务实例是否存活的策略。
    4.IRule 负载均衡策略，其实现类表述的策略包括：轮询、随机、根据响应时间加权等。
    5.ILoadBalancer 负载均衡器。这也是一个接口，Ribbon为其提供了多个实现，比如ZoneAwareLoadBalancer。
    6.RestClient 服务调用器。顾名思义，这就是负载均衡后，Ribbon向Service Provider发起REST请求的工具。

四：Spring Cloud 服务调用端熔断--Hystrix
   
    “断路器”本身是一种开关装置，当某个服务单元发生故障之后，通过断路器的故障监控（类似熔断保险丝），向调用方返回一个符合预期的、可处理的备选响应（FallBack），而不是长时间的等待或者抛出调用方无法处理的异常，这样就保证了服务调用方的线程不会被长时间、不必要地占用，从而避免了故障在分布式系统中的蔓延，乃至雪崩。
    除了隔离依赖服务的调用以外，Hystrix还提供了准实时的调用监控（Hystrix Dashboard），Hystrix会持续地记录所有通过Hystrix发起的请求的执行信息，并以统计报表和图形的形式展示给用户，包括每秒执行多少请求多少成功，多少失败等。
    要运行Hystrix仪表板使用@EnableHystrixDashboard注释您的Spring Boot主类。然后访问/hystrix，并将仪表板指向Hystrix客户端应用程序中的单个实例/hystrix.stream端点。

五：Spring Cloud 服务调用端代码抽象与封装--Feign
   
    Feign是一个声明式的Web Service客户端，它的目的就是让Web Service调用更加简单。它整合了Ribbon和Hystrix，从而让我们不再需要显式地使用这两个组件。Feign还提供了HTTP请求的模板，通过编写简单的接口和插入注解，我们就可以定义好HTTP请求的参数、格式、地址等信息。接下来，Feign会完全代理HTTP的请求，我们只需要像调用方法一样调用它就可以完成服务请求。

六：Spring Cloud 链路追踪--Sleuth
   
    spring cloud sleuth可以结合zipkin，将信息发送到zipkin，利用zipkin的存储来存储信息，利用zipkin ui来展示数据。
    1.提供链路追踪。通过sleuth可以很清楚的看出一个请求都经过了哪些服务。可以很方便的理清服务间的调用关系。
    2.可视化错误。对于程序未捕捉的异常，可以在zipkin界面上看到。
    3.分析耗时。通过sleuth可以很方便的看出每个采样请求的耗时，分析出哪些服务调用比较耗时。当服务调用的耗时随着请求量的增大而增大时，也可以对服务的扩容提供一定的提醒作用。
    4.优化链路。对于频繁地调用一个服务，或者并行地调用等，可以针对业务做一些优化措施

   
    6.1 术语：

    span：最基本的工作单元。由spanId来标志。Span也可以带有其他数据，例如：描述，时间戳，键值对标签，起始Span的ID，以及处理ID（通常使用IP地址）等等。 Span有起始和结束，他们跟踪着时间信息。span应该都是成对出现的，所以一旦创建了一个span，那就必须在未来某个时间点结束它。起始的span通常被称为：root span。它的id通常也被作为一个跟踪记录的id。
    traceId：一个树结构的span集合。把相同traceId的span串起来。
    annotation：用于记录一个事件时间信息。
        cs：client send。客户端发送，一个span的开始
        cr：client receive。客户端接收。一个span的结束
        ss：server send。服务器发送
        sr：server receive。服务器接收，开始处理。
        sr-cs和cr-ss:表示网络传输时长
        ss-sr:表示服务端处理请求的时长
        cr-cs:表示请求的响应时长

    6.2 采样率
    如果服务的流量很大，全部采集对存储压力比较大。这个时候可以设置采样率，sleuth 可以通过设置 spring.sleuth.sampler.percentage=0.1。不配置的话，默认采样率是0.1。也可以通过实现bean的方式来设置采样为全部采样(AlwaysSampler)或者不采样(NeverSampler)


七：Spring Cloud 路由转发--Zuul
    
    微服务场景下，每一个微服务对外暴露了一组细粒度的服务。客户端的请求可能会涉及到一串的服务调用，如果将这些微服务都暴露给客户端，那么会增加客户端代码的复杂度。
    将细粒度的服务组合起来提供一个粗粒度的服务，所有请求都导入一个统一的入口，那么整个服务只需要暴露一个api，对外屏蔽了服务端的实现细节，也减少了客户端与服务器的网络调用次数。这就是api gateway。
    有了api gateway之后，一些与业务关系并不大的通用处理逻辑可以从api gateway中剥离出来，api gateway仅仅负责服务的编排与结果的组装。
    此功能对于用户界面对其所需的后端服务进行代理是有用的，避免了对所有后端独立管理CORS和验证问题的需求。
    要启用它，使用@EnableZuulProxy注释Spring Boot主类，并将本地调用转发到相应的服务。按照惯例，具有ID“用户”的服务将接收来自位于/users（具有前缀stripped）的代理的请求。
    代理使用Ribbon来定位一个通过发现转发的实例，并且所有请求都以 hystrix命令执行，所以故障将显示在Hystrix指标中，一旦电路打开，代理将不会尝试联系服务。

    要跳过自动添加的服务，请将zuul.ignored-services设置为服务标识模式列表。
    zuul:
      ignoredServices: '*'
      routes:
        users: /myusers/**


    映射：
    1.url映射
    2.serviceId映射

    zuul:
      routes:
        api-node1:
          path: /ribbon/** #指定路径
          serviceId: spring-cloud-ribbon-consumer #指定服务
        api-node2:
          path: /feign/**
          url: http://localhost:8766
        api-node3:
          path: /templates/**
          url: forward:/templates

    这些简单的URL路由不会被执行为HystrixCommand，也不能使用Ribbon对多个URL进行负载平衡。为此，请指定service-route并为serviceId配置Ribbon客户端
    zuul:
      routes:
        users:
          path: /myusers/**
          serviceId: users

    ribbon:
      eureka:
        enabled: false

    users:
      ribbon:
        listOfServers: example.com,google.com


        要为所有映射添加前缀，请将zuul.prefix设置为一个值，例如/api。默认情况下，请求被转发之前，代理前缀被删除（使用zuul.stripPrefix=false关闭此行为）

    Cookie与头信息
    默认情况下，Zuul在请求路由时，会过滤HTTP请求头信息中的一些敏感信息，默认的敏感头信息通过zuul.sensitiveHeaders定义，包括Cookie、Set-Cookie、Authorization。
    如果您要将Cookie或授权标头传递到后端，这是必要，sensitiveHeaders:

    如果你使用 @EnableZuulProxy , 你可以使用代理路径上传文件, 它能够一直正常工作只要小文件. 对于大文件有可选的路径"/zuul/*"绕过Spring DispatcherServlet (避免处理multipart). 比如对于 zuul.routes.customers=/customers/** , 你可以使用 "/zuul/customers/*" 去上传大文件. Servlet路径通过 zuul.servletPath 指定. 如果使用Ribbon负载均衡器的代理路由, 在 处理非常大的文件时, 仍然需要提高超时配置.


八：Spring Cloud 配置中心--config

    在Spring Cloud Config为分布式系统中的外部配置提供服务器和客户端支持。。
    服务器存储后端的默认实现使用git，因此它轻松支持标签版本的配置环境，以及可以访问用于管理内容的各种工具。很容易添加替代实现，并使用Spring配置将其插入。
    Spring Cloud Config就是云端存储配置信息的,它具有中心化,版本控制,支持动态更新,平台独立,语言独立等特性。其特点是：

    a.提供服务端和客户端支持(spring cloud config server和spring cloud config client)
    b.集中式管理分布式环境下的应用配置
    c.基于Spring环境，无缝与Spring应用集成
    d.可用于任何语言开发的程序
    e.默认实现基于git仓库，可以进行版本管理
    f.可替换自定义实现

    1.server端

    拉取配置时更新git仓库副本，保证是最新结果
    支持数据结构丰富，yml, json, properties 等
    配合 eureke 可实现服务发现，配合 cloud bus 可实现配置推送更新
    配置存储基于 git 仓库，可进行版本管理
    简单可靠，有丰富的配套方案


    Spring Cloud Config服务器从git存储库（必须提供）为远程客户端提供配置：

    spring:
      cloud:
        config:
          server:
            git:
              uri: https://github.com/spring-cloud-samples/config-repo


    2.client端
    指明使用ConfigServer上哪个配置文件即可



    HTTP服务具有以下格式的资源：

    /{application}/{profile}[/{label}]
    /{application}-{profile}.yml
    /{label}/{application}-{profile}.yml
    /{application}-{profile}.properties
    /{label}/{application}-{profile}.properties

    本地git仓库：


    以下是上面示例中创建git仓库的方法：

    $ cd $HOME
    $ mkdir config-repo
    $ cd config-repo
    $ git init .
    $ echo info.foo: bar > application.properties
    $ git add -A .
    $ git commit -m "Add application.properties"

    环境库

    您要在哪里存储配置服务器的配置数据？管理此行为的策略是EnvironmentRepository，服务于Environment对象。此Environment是Spring Environment（包括propertySources作为主要功能）的域的浅层副本。Environment资源由三个变量参数化：

        {application}映射到客户端的“spring.application.name”;

        {profile}映射到客户端上的“spring.profiles.active”（逗号分隔列表）; 和

        {label}这是一个服务器端功能，标记“版本”的配置文件集。

    存储库实现通常表现得像一个Spring Boot应用程序从“spring.config.name”等于{application}参数加载配置文件，“spring.profiles.active”等于{profiles}参数。配置文件的优先级规则也与常规启动应用程序相同：活动配置文件优先于默认配置，如果有多个配置文件，则最后一个获胜（例如向Map添加条目）。


    spring:
     cloud:
    config:
      server:
        git:
          uri: https://github.com/spring-cloud-samples/config-repo
          repos:
            simple: https://github.com/simple/config-repo
            special:
              pattern: special*/dev*,*special*/dev*
              uri: https://github.com/special/config-repo
            local:
              pattern: local*
              uri: file:/home/configsvc/config-repo

    如果{application}/{profile}不匹配任何模式，它将使用在“spring.cloud.config.server.git.uri”下定义的默认uri。在上面的例子中，对于“简单”存储库，模式是simple/*（即所有配置文件中只匹配一个名为“简单”的应用程序）。“本地”存储库与所有配置文件中以“local”开头的所有应用程序名称匹配（将/*后缀自动添加到任何没有配置文件匹配器的模式）。


    每个存储库还可以选择将配置文件存储在子目录中，搜索这些目录的模式可以指定为searchPaths。例如在顶层：

    spring:
     cloud:
    config:
      server:
        git:
          uri: https://github.com/spring-cloud-samples/config-repo
          searchPaths: foo,bar*




本项目介绍：
    
    eureka-server为整个spring cloud项目的服务中心，除了之后的sleuth与stream相关的项目没有服务化，其余的均服务化。

最强的 eureka-server

    一：与eureka-provider-node* 联合测试eureka的服务发现与注册

    二：eureka-server本项目的测试多配置自由切换以及对等的集群部署

    三：与ribbon-consumer以及eureka-provider-node* 联合测试服务的消费，客户端的负载均衡，服务提供者的集群等

    四：与feign-consumer以及eureka-provider-node* 联合测试服务的熔断及上述第三点

    五：与sleuth-*（除了stream）相关项目联合测试链路的追踪，其中实现为http的zipkin

    六：与zuul,ribbon,feign联合测试路由的转发

    七：与config-*相关测试配置中心读取配置文件，消息总线BUG，集群等


    独立出来的sleuth-stream-*项目的测试链路追踪日志的持久化操作，该项目中持久化介质mysql,使用时，请初始化数据库连接。

 八：Spring Boot Admin
    
    spring-boot-admin，简称SBA，是一个针对spring-boot的actuator接口进行UI美化封装的监控工具。他可以：
    在列表中浏览所有被监控spring-boot项目的基本信息，详细的Health信息、内存信息、JVM信息、垃圾回收信息、各种配置信息（比如数据源、缓存列表和命中率）等，
    还可以直接修改logger的level。


csdn:详细介绍
    
  http://blog.csdn.net/ruben95001/article/details/77192432


