import com.gmail.woodyc40.commons.reflection.asm.ClassBytecode;

import java.lang.reflect.Method;

public class ClassByteCodeTest {
    public static void main(String[] args) throws Throwable {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        ClassBytecode bytecode = new ClassBytecode("test.Test", java.lang.Object.class, new Class[] { },
                false);
        Method method = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);

        method.setAccessible(true);

        byte[] bytes = bytecode.writeClassCode();
        bytecode.getConstantPool().debug();
        Class<?> clazz = (Class<?>) method.invoke(loader, bytes, 0, bytes.length);

        loader.loadClass(clazz.getName());
    }
}
