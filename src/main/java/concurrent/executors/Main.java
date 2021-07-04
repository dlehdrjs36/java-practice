package concurrent.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args){
        /*
            Executor 주의할 점
            - 작업을 수행 후, 다음 작업이 들어올 때 까지 계속 대기하기 때문에 프로세스가 종료되지 않는다. 그렇기 때문에 명시적으로 shutdown 해주어야 한다.
        */

        //static 팩토리 메소드 사용
        ExecutorService executorService = Executors.newSingleThreadExecutor(); //쓰레드를 하나만 쓰는 executor
        //쓰레드 작업 수행
        executorService.submit(() -> {
            System.out.println("#######################");
            System.out.println("Hello " + Thread.currentThread().getName());
            System.out.println("#######################");
        });
        //프로세스 종료
        executorService.shutdown(); //gracefull shutdown : 현재 진행중인 작업은 끝까지 마치고 끝내는 것을 의미
//      executorService.shutdownNow(); // 현재 진행중인 작업을 끝까지 마치지 않고 끝내는 것을 의미

        //쓰레드를 두 개 가지는 Executor
        ExecutorService executorService2 = Executors.newFixedThreadPool(2);
        //작업 5개 수행
        executorService2.submit(getRunnable("Hello"));
        executorService2.submit(getRunnable("Dev"));
        executorService2.submit(getRunnable("History"));
        executorService2.submit(getRunnable("Java"));
        executorService2.submit(getRunnable("Thread"));
        executorService2.shutdown();

        //주기적으로 어떤 작업을 실행하고 싶을 때 사용할 수 있는 Executor
        ScheduledExecutorService executorService3 = Executors.newSingleThreadScheduledExecutor();
        //3초 후에 작업 수행
        executorService3.schedule(getRunnable("Hello1"), 3, TimeUnit.SECONDS);
        //1초 대기 후 2초마다 작업 수행, interrupt 발생되면 종료됨
        executorService3.scheduleWithFixedDelay(getRunnable("Hello2"), 1, 2, TimeUnit.SECONDS);
//      executorService3.shutdown(); //interrupt 발생으로 인하여 주석 처리

    }

    private static Runnable getRunnable(String message) {
        return () -> System.out.println(message + Thread.currentThread().getName());
    }

}
