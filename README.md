# StarScanner

- 本项目主要实现了apk反编译、展示反编译源代码、登录鉴权的功能

## 1. apk反编译

### 1.1 基于命令行执行

#### 必备武器

-  Runtime可以执行cmd命令

  ```java
  String cmd = "cmd.exe /c start " + file.getAbsolutePath();
  Process exec = Runtime.getRuntime().exec(cmd);
  ```

- JDT AST相关依赖（不太确定是不是这2个）

  ```xml
  <dependency>
  <groupId>org.eclipse.jdt</groupId>
  <artifactId>org.eclipse.jdt.core</artifactId>
  <version>3.7.1</version>
  </dependency>
  <dependency>
  <groupId>org.eclipse</groupId>
  <artifactId>org.eclipse.osgi</artifactId>
  <version>3.8.0.v20120529-1548</version>
  </dependency>
  ```

#### Q&A

- Q：如何读取java文件获取源代码？
  - A: 使用JDT AST

### 1.2 基于jadx api

#### 必备武器

- 参考jadx的文档，需要引入下面三个关键的依赖才可以执行jadx的相关函数

  ```xml
  <dependency>
  	<groupId>io.github.skylot</groupId>
  	<artifactId>jadx-core</artifactId>
  	<version>1.3.2</version>
  </dependency>
  <dependency>
  	<groupId>io.github.skylot</groupId>
  	<artifactId>jadx-plugins-api</artifactId>
  	<version>1.3.2</version>
  </dependency>
  <dependency>
  	<groupId>io.github.skylot</groupId>
  	<artifactId>jadx-dex-input</artifactId>
  	<version>1.3.2</version>
  </dependency>
  ```

- ByteArrayOutputStream

  ```java
  ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(cls.getCode().getBytes().length);
  byteArrayOutputStream.write(cls.getCode().getBytes(), 0, cls.getCode().getBytes().length);
  ```

#### Q&A

- Q：jadx只能显示整个类的代码，不能显示某一个函数的代码
  - A：获取当前函数的起始行和下一个函数的起始行，取中间的代码
- Q：用file读和写文件，容易有漏洞
  - A: 用ByteArrayOutputStream直接操作字符串

#### 具体代码

### 1.3 代码高亮

#### 必备武器

- Prism highlighter依赖

  ```xml
  <dependency>
      <groupId>com.github.appreciated</groupId>
      <artifactId>prism-element</artifactId>
      <version>2.0.0.beta2</version>
  </dependency>
  ```

- 用法

  ```java
   PrismHighlighter javaCode = null;
  String content = "";
  javaCode = new PrismHighlighter(content, Language.java);
  ```

## 2.源代码展示

### 2.1 创建实体

- 三注解：

  @Entity
  @Table
  @Data

- 模板：

  ```java
  public class SourceCode {
      @Id
      @GeneratedValue
      private Long id;
      /*...*/
  }
  ```

- 级联类型设置：（这个具体是为啥不太清楚）

  ```java
  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
  @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
  ```

### 2.2 创建Repository

- 利用Jpa创建

  ```java
  public interface SourceCodeDetailsRepository extends JpaRepository<SourceCodeDetails, Long> {
   ;
  }
  ```

### 2.3 创建Service

- 建立service实现获取源代码的功能，在vaddin前端的构造器中传入service。

## 3. 登录鉴权

- 添加Spring Security依赖

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
  </dependency>
  ```

### 必备武器

- 获取当前登录的用户

  ```java
  SecurityContext context = SecurityContextHolder.getContext();
  Object principal = context.getAuthentication().getPrincipal();
  if (principal instanceof UserDetails) {
      return (UserDetails) context.getAuthentication().getPrincipal();
  }
  ```

### 3.1 登录

#### 创建实体

- 创建user：implements UserDetails。

  ```java
  @OneToOne Role
  ```

- 创建Role：记录权限名称和具体的权限。如name :管理员 code:admin

  ```java
  @ManyToMany Menu
  ```

- 创建Menu:记录具体的url信息

#### 实现UserDetailsService

- 重写loadUserByUsername方法，返回User

#### MyAuthenticationProvider

- 实现AuthenticationProvider

- 验证用户输入的密码是否正确

- 需要在SecurityConfig中配置

  ```java
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.authenticationProvider(myAuthenticationProvider);
  }
  ```

####  建立view(vaadin)

- 创建LoginForm
- 创建Sign-up view

### 3.2 鉴权

#### SecurityService

功能：

- 获取登录的用户
- 创建logout()方法
- 建立水平权限的实现方法

#### 垂直权限

##### MenuFilterInvocationSecurityMetadataSource

- 实现FilterInvocationSecurityMetadataSource
- 通过object获取当前用户访问的url
- 查找数据库中满足要求的url，返回这些url所具有的role集合
- 如果找不到，返回的就是Role_login

##### MenuAccessDecisionManager

- 实现AccessDecisionManager
- 获取上一步返回的权限集合，判断用户的role是否属于该集合
- 如果用户有权限，return; 否则抛出异常

#### 水平权限

限制用户可以访问的url

- 首先在配置类或启动类中开启注解

  ```java
  @EnableGlobalMethodSecurity(prePostEnabled = true)
  ```

- [vaadin] 在加载页面前，用@PreAuthorize注解限制

  ```java
  @PreAuthorize("@securityService.checkScanJob(#aLong)")
  @Override
      public void setParameter(BeforeEvent beforeEvent, Long aLong) {
          current = this.service.getByJobId(aLong);
          this.id = aLong;
          refreshUI();
      }
  ```

  - @securityService表示获取securityService这个bean，并调用它的checkScanJob的方法
  - "#aLong"表示获取方法中的参数along

#### [vaadin]Security的其他类

##### ConfigureUIServiceInitListener

- 配置重定向监听器，未登录前，所有页面都重定向到LoginView

##### CustomRequestCache

- 缓存，(不理解，需要学习)

##### SecurityUtils

#### SecurityConfig

- 配置hasRole

  ```java
  http.authorizeRequests()
          .antMatchers("/sign-up, /login").permitAll()
          .antMatchers("/user").hasRole("admin")
          .antMatchers("/admin").hasRole("superadmin")
          .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
          .anyRequest().authenticated();
  ```

- 配置403页面(目前没成功)

  ```java
  http.exceptionHandling().accessDeniedPage("/403");
  ```

- 配置使用垂直权限的2个过滤器

  ```java
  http.csrf().disable()
          .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
              @Override
              public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                  object.setSecurityMetadataSource(menuFilterInvocationSecurityMetadataSource); //动态获取url权限配置
                  object.setAccessDecisionManager(menuAccessDecisionManager); //权限判断
                  return object;
              }
          })；
  ```
