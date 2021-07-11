package concurrent.executors;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

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
            현재 진행중인 작업을 기다리고 종료(==graceful)
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

    /***
     * Java Concurrent-4 (CompletableFuture)
     */
    private static void javaConcurrent4(){
        /*
            기존 Future의 문제점
            - Future에서 get()하기 전까지 Future의 결과 값을 이용한 작업을 할 수 없다.
         */
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        Callable<String> hello = () -> {
            Thread.sleep(1000L);
            return "Hello";
        };

        Future<String> future = executorService.submit(hello);
        try {
            future.get();
            //Future의 값을 이용한 로직은 future.get() 이후에 등장하게 된다.
            executorService.shutdown();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("#######################");

        /*
            CompletableFuture
            - 외부에서 명시적으로 Complet 시킬 수 있다.
         */
        CompletableFuture<String> future2 = new CompletableFuture<>();
        future2.complete("Dev History"); //Future의 기본값 설정과 동시에 작업 완료 처리가 된다.
        System.out.println(future2.isDone()); //상태 true 출력
        try {
            System.out.println(future2.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("#######################");

        //위와 결과 동일
        CompletableFuture<String> future3 = CompletableFuture.completedFuture("Dev History");
        System.out.println(future3.isDone()); //상태 true 출력
        try {
            System.out.println(future3.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("#######################");

        /*
            비동기로 작업 실행하기
            - runAsync() : 리턴 값이 없는 경우
            - supplyAsync() : 리턴 값이 있는 경우
         */
        CompletableFuture<Void> future4 = CompletableFuture.runAsync(() -> System.out.println("Hello " + Thread.currentThread().getName()));
        try {
            System.out.println(future4.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("#######################");

        CompletableFuture<String> future5 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        });
        try {
            System.out.println(future5.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("#######################");

        /*
            CompletableFuture는 Future와 달리 콜백 주는 것이 가능하다.
            그리고 get() 이전에 처리 로직을 작성하는 것이 가능해졌다.
            - thenApply(Function) : 리턴 값을 받아서 다른 값으로 바꾸는 콜백
            - thenAccept(Consumer) : 리턴 값으로 또 다른 작업을 처리하는 콜백(리턴없이)
            - thenRun(Runnable) : 리턴 값을 다른 쓰레드의 작업에서 사용하는 콜백
         */
        //받은 결과 값을 UpperCase로 변경.
        CompletableFuture<String> future6 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        }).thenApply((s) -> {
            System.out.println(Thread.currentThread().getName());
            return s.toUpperCase();
        });
        try {
            System.out.println(future6.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("#######################");

        //받은 결과 값을 UpperCase로 변경.
        CompletableFuture<Void> future7 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        }).thenAccept((s) -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println(s.toLowerCase());
        });
        try {
            future7.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("#######################");

        //리턴 값 받지않고 다른 작업 처리
        CompletableFuture<Void> future8 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        }).thenRun(() -> System.out.println(Thread.currentThread().getName()));
        try {
            future8.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("#######################");

        /*
            CompletableFuture Executor(쓰레드풀) 변경 실행에 따른 사용 쓰레드 변경 확인
         */
        ExecutorService executorService2 = Executors.newFixedThreadPool(4);
        CompletableFuture<Void> future9 = CompletableFuture.supplyAsync(() -> {//쓰레드풀 변경 실행. 사용 쓰레드 : pool-thread-1
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        }, executorService2).thenRun(() -> {//사용 쓰레드 : pool-thread-1 또는 main (thenRun()으로 전달한 콜백은 앞선 콜백을 실행한 쓰레드(pool-thread-1)나 그 쓰레드를 파생시킨 부모(main)에서 실행한다)
            System.out.println(Thread.currentThread().getName());
        }).thenRunAsync(() -> { //쓰레드풀 변경 실행. 사용 쓰레드 : pool-thread-2
            System.out.println(Thread.currentThread().getName());
        }, executorService2).thenRun(() -> { //사용 쓰레드 : pool-thread-2 또는 main (thenRun()으로 전달한 콜백은 앞선 콜백을 실행한 쓰레드(pool-thread-2)나 그 쓰레드를 파생시킨 부모(main)에서 실행한다)
            System.out.println(Thread.currentThread().getName());
        }).thenRunAsync(() -> { //쓰레드풀 변경 실행. 사용 쓰레드 : pool-thread-3
            System.out.println(Thread.currentThread().getName());
        }, executorService2).thenRunAsync(() -> { //쓰레드풀 변경 실행. 사용 쓰레드 : pool-thread-4
            System.out.println(Thread.currentThread().getName());
        }, executorService2);
        try {
            future9.get();
            executorService2.shutdown();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("#######################");

        /*
            비동기 작업 조합하기
            - thenCompose() : 두 작업이 서로 이어서 실행하도록 조합(CompletableFuture 두 개를 연결하여 처리한 하나의 CompletableFuture가 나온다.)
            - thenCombine() : 두 작업을 독립적으로 실행하고 둘 다 종료 했을 때 콜백 실행
            - allOf() : 여러 작업을 모두 실행하고 모든 작업 결과에 콜백 실행
            - anyOf() : 여러 작업 중에 가장 빨리 끝난 하나의 결과에 콜백 실행
         */
        //서로 의존관계 : thenCompose()
        CompletableFuture<String> dev = CompletableFuture.supplyAsync(() -> {
            System.out.println("Dev " + Thread.currentThread().getName());
            return "Dev";
        });
        try {
            CompletableFuture<String> future10 = dev.thenCompose(Main::getWorld);
            System.out.println(future10.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("#######################");

        //서로 의존관계 X : thenCombine()
        CompletableFuture<String> apple = CompletableFuture.supplyAsync(() -> {
            System.out.println("Apple " + Thread.currentThread().getName());
            return "Apple";
        });

        CompletableFuture<String> history = CompletableFuture.supplyAsync(() -> {
            System.out.println("History " + Thread.currentThread().getName());
            return "History";
        });
        //둘 다 모두 작업완료 했을때, 실행할 콜백 작성
        CompletableFuture<String> future11 = apple.thenCombine(history, (a, h) -> {
            return a + " " + h;
        });
        try {
            System.out.println(future11.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("#######################");

        /*
            2개 이상일 때, 여러 서브 태스크들을 합쳐서 처리하는 방법
            - allOf에 넘긴 모든 태스크들이 다 끝났을 때 모든 작업 결과에 콜백을 실행한다.
            - 문제는 여러 태스크들의 결과가 동일한 타입임을 보장할 수 없기 때문에 처리가 어렵다.
         */
        //join와 get은 똑같은데 예외처리방식에서 다르다.
        //get은 checked Exceptions, join은 unchecked Exceptions 발생
        //결과 값을 콜렉션으로 처리하기. 아래와 같이 처리하면 블로킹이 발생하지 않는다.
        List<CompletableFuture<String>> futures = Arrays.asList(apple, history);
        CompletableFuture[] futuresArray = futures.toArray(new CompletableFuture[futures.size()]);

        CompletableFuture<List<String>> results = CompletableFuture.allOf(futuresArray)
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));

        try {
            results.get().forEach(System.out::println);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("#######################");

        /*
            anyOf에 넘긴 태스크들 중 아무거나 빨리 끝나는 작업 하나의 결과에 콜백 실행
         */
        CompletableFuture<Void> results2 = CompletableFuture.anyOf(apple, history).thenAccept(System.out::println);
        try {
            results2.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("#######################");

        /*
            에러 발생 시, 콜백 실행
            - exceptionally(Function) : 에러가 발생했을 경우 콜백 실행
            - handle(BiFunction) : 정상적으로 종료되었을 경우, 에러가 발생했을 경우 모두 사용 가능
               -> 첫 번째 파라미터는 정상적으로 종료되었을 경우의 값, 두 번째 파라미터는 에러가 발생한 경우의 값
         */
        boolean throwError = true;
        CompletableFuture<String> banana = CompletableFuture.supplyAsync(() -> {
            if (throwError) {
                throw new IllegalArgumentException();
            }
            System.out.println("Banana " + Thread.currentThread().getName());
            return "Banana";
        }).exceptionally(ex -> {
            System.out.println(ex);
            return "Error!";
        });
        try {
            System.out.println(banana.get()); //에러 발생시 Error! 출력
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("#######################");

        CompletableFuture<String> grape = CompletableFuture.supplyAsync(() -> {
            if (throwError) {
                throw new IllegalArgumentException();
            }
            System.out.println("Grape " + Thread.currentThread().getName());
            return "Grape";
        }).handle((result, ex) -> {
            if(ex != null){
                System.out.println(ex);
                return "ERROR!";
            }
            return result;
        });
        try {
            System.out.println(grape.get()); //에러 발생시 Error! 출력
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static CompletableFuture<String> getWorld(String message) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("World " + Thread.currentThread().getName());
            return message + " World";
        });
    }

    public static void main(String[] args) {
        //Java Concurrent-2 (Executors)
        //javaConcurrent2();

        //Java Concurrent-3 (Callable, Future)
        //javaConcurrent3();

        //Java Concurrent-4 (CompletableFuture)
        javaConcurrent4();
    }
}
