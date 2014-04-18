import com.gmail.woodyc40.commons.providers.UnsafeProvider;
import com.gmail.woodyc40.commons.reflection.ReflectionTool;

public class UnsafeProviderTest {
    public static void main(String[] args) throws NoSuchFieldException {
        // Reflection - Prints
        // sun.misc.Unsafe
        System.out.println(UnsafeProvider.acquireField(ReflectionTool.forField("PROVIDER", UnsafeProvider.class), null)
                .getClass().getName());

        // Unsafe cast - Prints
        // 2337
        // 8451275
        // java.lang.Object@4741d6
        // 4669910
        String string = "Hi";
        Object dummy = new Object();

        System.out.println(string.hashCode());
        System.out.println(dummy.hashCode());

        Object ret = UnsafeProvider.castSuper("Hi", dummy);
        System.out.println(ret);
        System.out.println(ret.hashCode());
    }
}
