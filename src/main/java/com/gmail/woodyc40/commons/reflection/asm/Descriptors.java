package com.gmail.woodyc40.commons.reflection.asm;

/**
 * Utility <code>class</code> to assist with building descriptors for methods and fields
 * 
 * @author AgentTroll
 * @version 1.0
 */ 
public class Descriptors {
    private Descriptors() {}
    
    /**
     * Builds a default descriptor appending a <code>class</code> type to the 
     * {@link java.lang.StringBuilder}
     * 
     * @param desc the descriptor builder to append the type to
     * @param type the <code>class</code> to append to the builder
     */ 
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

    /**
     * Creates a descriptor for a method
     * 
     * @param returnType the <code>return class</code> the method will exit with
     * @param paramTypes the parameters of the method
     * @return the method descriptor for the <code>return</code> and params
     */ 
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

    /**
     * Shortcut method to build a field descriptor, creates a StringBuilder and returns the String
     * representation
     * 
     * @param type the type the field represents
     * @return the descriptor for the declared type
     */ 
    public static String descField(Class<?> type) {
        StringBuilder builder = new StringBuilder();
        toDesc(builder, type);
        return builder.toString();
    }
}
