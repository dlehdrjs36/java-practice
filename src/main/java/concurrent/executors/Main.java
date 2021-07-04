package concurrent.executors;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    /***
     * Java Concurrent-2 (Executors)
     */
    private static void javaConcurrent2(){
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
        System.out.println("-------------------");
        ExecutorService executorService2 = Executors.newFixedThreadPool(2);//쓰레드를 두 개 가지는 Executor
        //작업 5개 수행
        executorService2.submit(getRunnable("Hello"));
        executorService2.submit(getRunnable("Dev"));
        executorService2.submit(getRunnable("History"));
        executorService2.submit(getRunnable("Java"));
        executorService2.submit(getRunnable("Thread"));
        executorService2.shutdown();
        System.out.println("-------------------");
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

    /***
     * Java Concurrent-3 (Callable, Future)
     */
    private static void javaConcurrent3() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Callable<String> hello = () -> {
            System.out.println("Callable hello Start!");
            Thread.sleep(2000L);
            return "Hello";
        };

        Callable<String> java = () -> {
            Thread.sleep(3000L);
            return "Java";
        };
        //현재 Executor 쓰레드가 2개이기 때문에 Blocking Queue에서 대기(hello가 완료되어야 작업 수행 가능)
        Callable<String> dev = () -> {
            Thread.sleep(1000L);
            return "Dev";
        };

        /*
            invokeAll() : Callable을 뭉쳐서 여러 작업을 동시에 수행할 수 있다.
            - 작업이 제일 오래걸리는 쓰레드가 완료되어야만 값을 가져올 수 있다.
         */
        try {
            List<Future<String>> futures = executorService.invokeAll(Arrays.asList(hello, java, dev));
            System.out.println("-------------------");
            for(Future<String> f : futures) {
                System.out.println(f.get());

            }
            System.out.println("-------------------");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        /*
            invokeAny() : Callable을 뭉쳐서 여러 작업을 동시에 수행할 수 있다.
            - 작업이 제일 빨리끝나는 쓰레드의 응답이 오면 값을 가져올 수 있다.
            - 블로킹 콜이다.
         */
        try {
            System.out.println("-------------------");
            String s = executorService.invokeAny(Arrays.asList(hello, java, dev));
            System.out.println(s);
            System.out.println("-------------------");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //Callable이 리턴하는 타입의 Future를 얻을 수 있다.
        Future<String> helloFuture = executorService.submit(hello);
        System.out.println("#######################");
        System.out.println(helloFuture.isDone()); //수행중인 작업이 끝났으면 true, 아니면 false
        System.out.println("Main Started!");

        /*
            현재 진행중인 작업을 interrupt 하면서 종료
        */
        //helloFuture.cancel(true);

        /*
            현재 진행중인 작업을 기다리고 종료(==gracefull)
            - 작업 완료를 기다린다고해도 cancel이 호출되면 Future에서 값을 꺼내는 것이 불가능하다.
               -> 이미 취소시킨 작업에서 꺼내려고하면 CancellationException 에러 발생
            - cancel을 하면 상태(isDone)는 무조건 true가 된다.
               -> 이 때의 true는 작업이 완료되었으니 값을 꺼낼 수 있다는 의미가 아니다.
               -> cancel 했기 때문에 종료된 것이다.
        */
        //helloFuture.cancel(false);

        try {
            /*
                get() 이전 까지는 코드는 계속 실행된다.
                get()을 만나면 결과 값을 가져올 때까지 대기(Blocking Call)
            */
            String s = helloFuture.get();//get()을 통해 Future의 값을 꺼낸다.
            System.out.println(helloFuture.isDone()); //작업이 끝났으면 true, 아니면 false
            System.out.println(s);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("Main End");
        executorService.shutdown();
    }

    public static void main(String[] args) {
        //Java Concurrent-2 (Executors)
        //javaConcurrent2();

        //Java Concurrent-3 (Callable, Future)
        javaConcurrent3();
    }
}
