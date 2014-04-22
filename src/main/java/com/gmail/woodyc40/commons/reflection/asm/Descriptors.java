package com.gmail.woodyc40.commons.reflection.asm;

public class Descriptors {
    private static void toDesc(StringBuilder desc, Class<?> type) {
        if (type.isArray()) { // arrays
            int len = 1 + type.getName().lastIndexOf("[");
            for (int i = 0; i <= len; i++)
                desc.append('[');
            try {
                toDesc(desc, type.getComponentType());
            } catch (Exception e) {
                desc.append('L');
                String name = type.getName();
                desc.append(name.substring(0, name.length() - 2).replace(".", "/"));
                desc.append(';');
            }
        } else if (type.isPrimitive()) { // primitives
            desc.append(Primitives.getDesc(type));
        } else { // classes
            desc.append('L');
            desc.append(type.getName().replace('.', '/'));
            desc.append(';');
        }
    }

    public static String descMethod(Class<?> returnType, Class[] paramTypes) {
        StringBuilder desc = new StringBuilder();
        desc.append('(');
        if (paramTypes != null)
            for (final Class paramType : paramTypes)
                toDesc(desc, paramType);

        desc.append(')');
        if (returnType != null)
            toDesc(desc, returnType);

        return desc.toString();
    }

    public static String descField(Class<?> type) {
        StringBuilder builder = new StringBuilder();
        toDesc(builder, type);
        return builder.toString();
    }
}
