package com.liu.pojo;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * servlet中url与处理器影射关系对象
 */
public class Handler {

    private Pattern pattern; //String中url是支持正则的

    private Object controller;

    private Method method;

    private Map<String, Integer> paramIndexMapping = new HashMap<String, Integer>(); //参数顺序，为了进行参数绑定，key是参数名，value代表第几个参数

    public Handler(Pattern pattern, Object controller, Method method) {
        this.pattern = pattern;
        this.controller = controller;
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Map<String, Integer> getParamIndexMapping() {
        return paramIndexMapping;
    }

    public void setParamIndexMapping(Map<String, Integer> paramIndexMapping) {
        this.paramIndexMapping = paramIndexMapping;
    }
}
