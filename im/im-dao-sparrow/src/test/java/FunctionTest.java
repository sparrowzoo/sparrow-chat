import com.sparrowzoo.chat.dao.sparrow.dao.po.Session;

import java.util.ArrayList;
import java.util.function.Function;

public class FunctionTest {
    // 接收方法引用作为参数
    public static void processString(Function<Session, String> function, String input) {
        Session session = new Session();
        String result = function.apply(session);
        System.out.println(result);
    }

    public static void main(String[] args) {
        processString(Session::getSessionKey, "hello world");
        new ArrayList<Session>().stream().map(Session::getSessionKey).forEach(System.out::println);
    }
}
