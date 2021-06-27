package optional;

import java.util.Optional;

public class OnlineClass {

    private Integer id;

    private String title;

    private boolean closed;

    public Progress progress;
    /*
        아래와 같이 인스턴스 필드 타입으로 Optional을 사용하는 것은 좋지 않다.
        아래와 같은 형태는 설계의 문제이다.
        상위 클래스, 하위 클래스로 쪼개거나 위임 방식을 사용하는 것이 좋다.
    */
//  public Optional<Progress> progress;

    public OnlineClass(Integer id, String title, boolean closed) {
        this.id = id;
        this.title = title;
        this.closed = closed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

//    public Progress getProgress() {
//        에러는 필요한 상황에서만 던져야 하고 로직에서 사용하는 것은 좋지 않다.
//        if (this.getProgress() == null) {
//            throw new IllegalStateException();
//        }
//        return progress;
//    }
    public Optional<Progress> getProgress() {
        return Optional.ofNullable(progress);
        /*
            아래와 같이 Optional을 리턴하는 메소드에서 null을 리턴하면 안된다.
            null을 리턴하면 Optional을 받는 변수에서 관련 메소드 호출 시, 에러가 발생한다.
        */
//      return null;
    }

    public void setProgress(Optional<Progress> progress) {
        /*
            Optional은 리턴 값으로만 사용하는 것이 권장된다.
            위와 같이 매개변수에 Optional을 사용한 경우, 위험하다.(NullPointerException)
            매개변수가 null로 전달된 경우, 오히려 매개변수에 Optional을 사용함으로서 처리가 더 복잡해질 수 있다.(추가적인 null 비교 조건 필요)
            비교 조건을 추가하지 않는다면, 메소드 호출 시, 매개변수가 null일 경우에도 progress가 null로 설정되고 결과적으로 에러가 발생한다.
        */
        if (progress != null) {
            progress.ifPresent((p) -> this.progress = p);
        }
    }
}
