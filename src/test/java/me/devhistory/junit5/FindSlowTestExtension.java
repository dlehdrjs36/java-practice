package me.devhistory.junit5;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;

public class FindSlowTestExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    //private static final long THRESHOLD = 1000L; //1초이상 걸리면 느린테스트로 기준 설정

    private long THRESHOLD; //1초이상 걸리면 느린테스트로 기준 설정

    public FindSlowTestExtension(long THRESHOLD) {
        this.THRESHOLD = THRESHOLD;
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        ExtensionContext.Store store = getStore(context);
        store.put("START_TIME", System.currentTimeMillis());
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        //기존에 SlowTest 애노테이션 없는 경우에만 메시지 출력
        Method requiredTestMethod = context.getRequiredTestMethod();
        SlowTest annotation = requiredTestMethod.getAnnotation(SlowTest.class);

        String testMethodName = context.getRequiredTestMethod().getName();
        ExtensionContext.Store store = getStore(context);
        long start_time = store.remove("START_TIME", long.class);//START_TIME의 값을 꺼내고 지운다.
        long duration = System.currentTimeMillis() - start_time;
        if (duration > THRESHOLD && annotation == null){
            System.out.printf("Please consider mark method [%s] with @SlowTest.\n", testMethodName);
        }
    }

    private ExtensionContext.Store getStore(ExtensionContext context) {
        String testClassName = context.getRequiredTestClass().getName();
        String testMethodName = context.getRequiredTestMethod().getName();
        //Store : 값을 넣고 빼는 용도
        //테스트클래스 이름 + 매서드 이름을 조합하여 네임 스페이스를 만든다. 테스트 클래스 이름, 메서드 이름 정보는 context를 통해서 얻어올 수 있다.
        ExtensionContext.Store store = context.getStore(ExtensionContext.Namespace.create(testClassName, testMethodName));
        return store;
    }
}
