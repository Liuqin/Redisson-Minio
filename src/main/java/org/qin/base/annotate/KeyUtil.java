package org.qin.base.annotate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author LiuQin
 */


@Slf4j
@Component
public class KeyUtil {
    public String generate(Method method, String[] excludeKeys, Object... args) {
        StringBuilder sb = new StringBuilder(method.toString());
        for (Object arg : args) {
            log.info("arg:" + arg);
            if (arg instanceof Map<?, ?>) {
                List<Map.Entry<?, ?>> list = new ArrayList<>(((Map<?, ?>) arg).entrySet());
                if (excludeKeys != null && excludeKeys.length > 0) {
                    for (String ekey : excludeKeys) {
                        list = list.stream().filter(e -> !ekey.equals(e.getKey())).collect(Collectors.toList());
                    }
                }
                Collections.sort(list, (o1, o2) -> String.valueOf(o1.getKey()).compareTo(String.valueOf(o2.getKey())));
                sb.append(list.toString());
            } else {
                sb.append(castString(arg));
            }
        }
        log.info("幂等方法和参数:" + sb.toString());
        return md5(sb.toString());
    }

    @SneakyThrows
    public String castString(Object object) {
        if (object == null) {
            return "null";
        }
        if (object instanceof Number) {
            return object.toString();
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * @return
     * @descripttion 字符串加密Md5
     * @parms
     * @author liuqin
     * @date 2020/6/18
     */
    public String md5(String str) {
        StringBuilder buf = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] b = md.digest();
            int i;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append(0);
                }
                buf.append(Integer.toHexString(i));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }


}
