import com.sparrowzoo.chat.dao.sparrow.dao.po.Session;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.function.Function;

@FunctionalInterface
interface SerializableFunction<T, R> extends Function<T, R>, Serializable {}

public class FunctionTest {
    // 接收方法引用作为参数
    public static void processString(SerializableFunction<Session,?> function, String input) {
        System.out.println(getMethodName(function));
    }

    public static String getMethodName(Function<?, ?> function) {
        try {
            // 反射获取 writeReplace 方法
            Method method = function.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            // 调用该方法获取 SerializedLambda 对象
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(function);

            // 解析方法名
            String methodName = serializedLambda.getImplMethodName();
            System.out.println(serializedLambda.getImplClass());
            return methodName;
        } catch (Exception e) {
            throw new RuntimeException("无法解析方法名", e);
        }
    }

    public static void main(String[] args) {
        processString(Session::getSessionKey, "hello world");
    }
}
