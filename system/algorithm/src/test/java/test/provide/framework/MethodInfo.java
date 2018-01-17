package test.provide.framework;

public class MethodInfo {

    private final String name;
    private final Class<?>[] paramType;

    public MethodInfo(String name, Class<?>[] paramType) {
        this.name = name;
        this.paramType = paramType;
    }

    public String getName() {
        return name;
    }

    public Class<?>[] getParameterTypes() {
        return paramType;
    }
}