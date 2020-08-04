package com.sixj.util;

import com.sixj.annotation.NeedEncrypt;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * @author sixiaojie
 * @date 2020-08-04-16:24
 */
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
