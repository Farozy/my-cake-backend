package org.farozy.helper;


import lombok.RequiredArgsConstructor;
import org.farozy.utility.FileUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FillMapHelper {

    public static Map<String, Object> fillMapData(Object obj, String srcPath) throws
            IOException, IllegalAccessException {
        Map<String, Object> map = new LinkedHashMap<>();

        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            Object value = field.get(obj);

            if ("image".equals(field.getName()) || "photo".equals(field.getName()) || "picture".equals(field.getName())) {
                map.put("image", value != null ? FileUtils.getImageDetails(srcPath, (String) value) : null);
            } else {
                if (!"password".equals(field.getName())) {
                    map.put(field.getName(), value);
                }
            }
        }

        return map;
    }
}
