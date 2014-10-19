package com.ipe.common.web.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 *  No serializer found for class org.hibernate.proxy.pojo.javassist.JavassistL
 *  详情：
 *  No serializer found for class org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer
 *  and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) )
 *  (through reference chain: com.ipe.module.core.web.util.BodyWrapper["rows"]-&gt;java.util.UnmodifiableRandomAccessList[0]-&gt;
 *  com.ipe.module.bpm.entity.TaskProxy["userForm"]-&gt;com.ipe.module.core.entity.User_$$_javassist_1["handler"])
 *
 * 解决办法1：
 * ObjectMapper mapper = new ObjectMapper();
 * mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 * 解决办法2：
 * @JsonIgnoreProperties( value={"hibernateLazyInitializer","handler"})
 *
 * Created by tangdu on 14-2-16.
 */
public class HibernateObjectMapper extends ObjectMapper {
    public HibernateObjectMapper() {
        disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }
}
