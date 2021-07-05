package concurrent;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class Main {
    /***
     * Java Concurrent-1 (Thread)
     */
    private static void javaConcurrent1(){
        /*
            과거의 쓰레드 사용 방법
            - 쓰레드가 늘어날 때마다 인터럽트 처리가 점점 늘어나고 복잡해진다.(문제)
               -> 수십, 수백개의 쓰레드를 코딩으로 직접 관리하는 것은 어렵다.
        */
        //1. Thread 인터페이스 구현
        Thread thread = new Thread(() -> {
            while(true){
                System.out.println("Thread1: " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000L); //쓰레드를 sleep 시키면 다른 쓰레드가 먼저 일을 처리할 수 있게 된다.
                } catch (InterruptedException e) { //sleep 동안에 누군가가 이 쓰레드를 깨우면 발생.
                    //쓰레드가 interrupt 되었을 경우 작업 처리.
                    //쓰레드를 종료 시키거나, 별도의 동작을 수행하도록 할 수도 있다.
                    System.out.println("exit!");
                    return; //종료
                }
            }
        });
        thread.start();

        //2. Thread 클래스 상속
        MyThread myThread = new MyThread();
        myThread.start();

        System.out.println("main Thread: " + Thread.currentThread().getName());
        try {
            myThread.join(); //메인 쓰레드는 thread2가 끝날 때까지 기다린다.
        } catch (InterruptedException e) {//메인쓰레드가 대기 중일 때 interrupt 되었을 경우 작업 처리.
            e.printStackTrace();
        }

        System.out.println(thread + " is finished");
        thread.interrupt(); //쓰레드를 깨우는 동작을 수행
    }
    private static class MyThread extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            System.out.println("Thread2: " + Thread.currentThread().getName());
        }
    }

    /*
        배열 병렬 정렬
         - Fork/Join 프레임워크를 사용해서 배열을 병렬로 정렬
     */
    private static void parallelSort() {
        int size = 1500;
        int[] numbers = new int[size];
        Random random = new Random();
        IntStream.range(0, size).forEach(i -> numbers[i] = random.nextInt());
        //일반 정렬 시간 측정(쓰레드 1개 사용)
        long start = System.nanoTime();
        Arrays.sort(numbers); // Dual-Pivot Quicksort 사용
        System.out.println("serial sorting took " + (System.nanoTime() - start));

        IntStream.range(0, size).forEach(i -> numbers[i] = random.nextInt());
        //병렬 정렬 시간 측정(쓰레드 여러 개 사용)
        start = System.nanoTime();
        Arrays.parallelSort(numbers); //배열을 쪼개면서 하위 배열 길이가 최소 단위에 도달하면 합치면서 정렬(Arrays.sort 사용)
        System.out.println("parallel sorting took " + (System.nanoTime() - start));
    }
    public static void main(String[] args) {
        //Java Concurrent-1 (Thread)
        //javaConcurrent1();

        //배열 병렬 정렬
        parallelSort();
    }


}
