package org.mrying.chat.settings;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 邓和颖
 * @since 2023/10/16 15:58
 */

@Component
@ConfigurationProperties(prefix = "api.chatgpt")
public class ChatgptConfiguration {

    private String host;

    private String path;

    private String method;

    private String gptKey;

    private String model;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getGptKey() {
        return gptKey;
    }

    public void setGptKey(String gptKey) {
        this.gptKey = gptKey;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
