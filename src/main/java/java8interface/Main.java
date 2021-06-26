package java8interface;

import java.util.*;

public class Main {

    /**
     * 맵 key name의 값을 대문자로 변환한다.
     * @param map
     * @return
     */
    public static String getMapNameToUpperCase(Map<String, Object>  map){
        return map.get("name").toString().toUpperCase();
    }

    /**
     * 맵 생성 시간을 비교한다. 큰 값이 우선 출력
     * @param map1
     * @param map2
     * @return
     */
    public static int mapCreateTimeCompare(Map<String, Object> map1, Map<String, Object> map2) {
        return Integer.parseInt(map2.get("createTime").toString())-Integer.parseInt(map1.get("createTime").toString());
    }

    public static void main(String[] args){
        Goo goo = new DefaultGoo("Goo");

        /*
            default 키워드 메소드 & 추상 메소드 : 인스턴스 사용
        */
        goo.printName(); //인터페이스의 추상 메소드 구현
        goo.printNameUpperCase(); //기본 메소드의 충돌이 발생하여 인터페이스의 기본 메소드 재정의
        goo.printNameLowerCase(); //인터페이스의 기본 메소드
        System.out.println("###############");
        /*
            static 키워드 메소드 : 해당 인터페이스 타입에서 사용
        */
        Goo.printAnything(); //인터페이스의 헬퍼, 유틸리티 메소드
        System.out.println("###############");
        /*
            자바8에서 추가한 기본 메소드로 인한 API 변화
            Iterable 인터페이스
             - forEach()
             - spliterator()

            Collection 인터페이스
             - stream() / parallelStream()
             - removeIf(Predicate)
             - spliterator()

            Comparator 인터페이스
             - reversed()
             - thenComparing()
             - static reverseOrder() / naturalOrder()
             - static nullsFirst() / nullsLast()
             - static comparing()
        */

        /* forEach(), spliterator() */
        List<Map<String, Object>> name = new ArrayList<>();
        name.add(Map.of("name", "history", "createTime", (int)System.currentTimeMillis()));
        name.add(Map.of("name", "devdev", "createTime", (int)System.currentTimeMillis()+1));
        name.add(Map.of("name", "appleapple", "createTime", (int)System.currentTimeMillis()+2));
        name.add(Map.of("name", "apple", "createTime", (int)System.currentTimeMillis()+3));
        name.add(Map.of("name", "HooHoo", "createTime", (int)System.currentTimeMillis()+4));
        name.add(Map.of("name", "HooHoo", "createTime", (int)System.currentTimeMillis()+5));
        name.add(Map.of("name", "HooHoo", "createTime", (int)System.currentTimeMillis()+6));
        name.add(Map.of("name", "barbar", "createTime", (int)System.currentTimeMillis()+7));
        name.forEach(System.out::println);
        System.out.println("###############");
        //쪼갤 수 있는 기능을 가진 Iterator. 기본적으로는 Iterator와 같다
        Spliterator<Map<String, Object>> originSpliterator = name.spliterator();
        Spliterator<Map<String, Object>> spliterator = name.spliterator();
        //쪼개는 기능. 원래 spliterator에서 가지고 있던 엘리먼트를 반으로 쪼개서 기존 spliterator에 넣어주고 나머지 반의 엘리먼트는 spliterator1에 넣어준다. 엘리먼트의 순서가 중요하지 않을 때 사용(병렬 처리).
        Spliterator<Map<String, Object>> spliterator1 = spliterator.trySplit();
        //hasNext() 대신 tryAdvance 사용. 다음 것이 있으면 true, 없으면 false 반환
        System.out.println("===originSpliterator(spliterator 반으로 나누기 전의 원본)===");
        while (originSpliterator.tryAdvance(System.out::println));
        System.out.println("###############");

        System.out.println("===spliterator===");
        while (spliterator.tryAdvance(System.out::println));
        System.out.println("===spliterator1===");
        while (spliterator1.tryAdvance(System.out::println));
        System.out.println("###############");
        /* //forEach(), spliterator() */

        /* stream() */
        long count = name.stream().map(Main::getMapNameToUpperCase)
                                .filter(s -> s.startsWith("H"))
                                .count();
        System.out.println(count);
        System.out.println("###############");
        /* //stream() */
        /* removeIf() */
        //조건을 만족하는 엘리먼트 삭제처리
        name.removeIf((s) -> s.get("name").toString().startsWith("d"));
        name.forEach(System.out::println);
        System.out.println("###############");
        /* //removeIf() */

        /* reversed(), thenComparing() */
        //문자순으로 정렬
        Comparator<Map<String, Object>> compareToIgnoreCase = (map1, map2) -> map1.get("name").toString().compareToIgnoreCase(map2.get("name").toString());
        name.sort(compareToIgnoreCase);
        System.out.println("===compareToIgnoreCase===");
        name.forEach(System.out::println);

        //문자역순으로 정렬
        name.sort(compareToIgnoreCase.reversed());
        System.out.println("===compareToIgnoreCase.reversed()===");
        name.forEach(System.out::println);

        //문자역순으로 정렬 후 추가적인 비교가 더 필요한 경우에 사용. 정렬 결과 중 같은 값에 대해서 정렬 수행
        //문자역순으로 정렬 후, 같은 값이 있다면 나중에 생성된 것이 우선적으로 표시되도록 정렬
        name.sort(compareToIgnoreCase.reversed().thenComparing(Main::mapCreateTimeCompare));
        System.out.println("===compareToIgnoreCase.reversed().thenComparingInt(Main::mapCreateTimeCompare)===");
        name.forEach(System.out::println);
        /* //reversed(), thenComparing() */
    }
}
