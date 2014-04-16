import com.gmail.woodyc40.commons.providers.UnsafeProvider;
import com.gmail.woodyc40.commons.reflection.ReflectionTool;

public class UnsafeProviderTest {
    public static void main(String[] args) {
        System.out.println(UnsafeProvider.acquireField(ReflectionTool.forField("PROVIDER", UnsafeProvider.class), null)
                .getClass().getName());
    }
}
