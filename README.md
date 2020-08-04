# annotation-jasypt

#### 简介

写一个简单的切面，在查询员工信息时，使用切面给员工登录密码加密

引入aop需要的依赖：

```xml
<!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-aop -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-aop</artifactId>
  <version>2.1.15.RELEASE</version>
</dependency>
```

定义两个注解，一个用在需要加密的字段上，另一个加在需要使用切面的方法上：

```java
/**
 * 需要加密的字段
 * @author sixiaojie
 * @date 2020-08-04-15:52
 */
@Target(ElementType.FIELD)// 应用范围--字段
@Retention(RetentionPolicy.RUNTIME)// 注解的生命周期
public @interface NeedEncrypt {

    /**
     * 加密用到的密钥
     * @return
     */
    String secretKey();
}
```



```java
/**
 * 使用加密的方法
 * @author sixiaojie
 * @date 2020-08-04-16:00
 */
@Target(ElementType.METHOD)// 作用范围--方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableEncrypt {// 对被这个注解修饰的方法切面
}
```

employee实体类，在password字段上加NeedEncrypt注解，并设置一个密钥：

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private String id;
    private String name;

    @NeedEncrypt(secretKey = "bd154!*74e-9fba0")
    private String password;

}
```

service方法上加EnableEncrypt注解：

```java
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Override
    @EnableEncrypt
    public List<Employee> list(){
        List<Employee> employees = initEmpList();
        return employees;
    }

    private List<Employee> initEmpList(){
        ArrayList<Employee> empList = new ArrayList<>();
        empList.add(new Employee("1","曹操","111111"));
        empList.add(new Employee("2","孙权","222222"));
        empList.add(new Employee("3","刘备","333333"));
        empList.add(new Employee("4","关羽","444444"));
        empList.add(new Employee("5","张飞","555555"));
        return empList;
    }
}
```

写一个切面

```java
@Component
@Aspect
public class SetEncryptAspect {

    @Autowired
    private BeanUtil beanUtil;

    @Around("@annotation(com.sixj.annotation.EnableEncrypt)")
    public Object doSetEncrypt(ProceedingJoinPoint point) throws Throwable{
        // 前置增强

        // 执行被切面的方法，获取结果集
        Object proceed = point.proceed();

        // 后置增强 --对密码进行加密
        try {
            beanUtil.setValueForCollection((Collection) proceed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 将被设置好之后的值返回
        return proceed;
    }
}
```

关键代码封装了一个BeanUtil

```java
@Component
public class BeanUtil{

    public void setValueForCollection(Collection collection) throws Exception{
        // 集合中所包含对象的类型
        Class<?> clazz = collection.iterator().next().getClass();
        Field[] fields = clazz.getDeclaredFields();
        // 反射
        for (Field needField : fields) {
            NeedEncrypt needEncrypt = needField.getAnnotation(NeedEncrypt.class);
            if(needEncrypt == null){
                continue;
            }
            needField.setAccessible(true);
            // 密钥
            String secretKey = needEncrypt.secretKey();
            for (Object obj : collection) {
                //[{id=1, name=曹操, password=111111},...]
                Object targetValue = needField.get(obj);
                if(targetValue == null){
                    continue;
                }
                String encryptPwd = JasyptUtil.encryptPwd(secretKey, targetValue.toString());
                needField.set(obj,encryptPwd);
            }
        }
    }
}

```

Jasypt加密的依赖和Util

```xml
<dependency>
    <groupId>com.github.ulisesbocchio</groupId>
    <artifactId>jasypt-spring-boot-starter</artifactId>
    <version>1.14</version>
</dependency>
```



```java
public class JasyptUtil {
    /**
     * Jasypt生成加密结果
     *
     * @param password 配置文件中设定的加密密码 jasypt.encryptor.password
     * @param value    待加密值
     * @return
     */
    public static String encryptPwd(String password, String value) {
        PooledPBEStringEncryptor encryptOr = new PooledPBEStringEncryptor();
        encryptOr.setConfig(cryptOr(password));
        String result = encryptOr.encrypt(value);
        return result;
    }


    /**
     * 解密
     *
     * @param password 配置文件中设定的加密密码 jasypt.encryptor.password
     * @param value    待解密密文
     * @return
     */
    public static String decyptPwd(String password, String value) {
        PooledPBEStringEncryptor encryptOr = new PooledPBEStringEncryptor();
        encryptOr.setConfig(cryptOr(password));
        String result = encryptOr.decrypt(value);
        return result;
    }


    public static SimpleStringPBEConfig cryptOr(String password) {
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(password);
        config.setAlgorithm(StandardPBEByteEncryptor.DEFAULT_ALGORITHM);
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        return config;
    }
}
```

