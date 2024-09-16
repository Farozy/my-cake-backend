package org.farozy.security.oauth2.user;

import java.util.Map;
import java.util.Optional;

public class FaceboookAuth2UserInfo extends OAuth2UserInfo{

    public FaceboookAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public void getId() {
        attributes.get("id");
    }

    @Override
    public String getName() { return (String) attributes.get("name"); }

    @Override
    public String getEmail() { return (String) attributes.get("email"); }

    @Override
    public String getImageUrl() {
        return getNestedAttribute(attributes, "picture", "data", "url");
    }

    public static String getNestedAttribute(Map<String, Object> map, String... keys) {
        Optional<Map<String, Object>> currentMap = Optional.of(map);

        for (int i = 0; i < keys.length - 1; i++) {
            String key = keys[i];
            currentMap = currentMap.flatMap(m -> castToMap(m.get(key)));
        }

        String finalKey = keys[keys.length - 1];
        return currentMap.map(m -> (String) m.get(finalKey)).orElse(null);
    }

    @SuppressWarnings("unchecked")
    private static Optional<Map<String, Object>> castToMap(Object obj) {
        if (obj instanceof Map) {
            return Optional.of((Map<String, Object>) obj);
        }
        return Optional.empty();
    }

}
