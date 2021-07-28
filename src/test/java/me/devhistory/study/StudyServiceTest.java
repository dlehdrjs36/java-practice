package me.devhistory.study;

import me.devhistory.domain.Member;
import me.devhistory.domain.Study;
import me.devhistory.member.MemberService;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {
    /**
     * @Mock 애노테이션을 이용한 Mock 객체 생성
     * 여러 테스트에서 사용할 때 전역적으로 생성하는 것은 유용하다.
     * @Mock 애노테이션을 처리할 확장팩이 필요하다.
     * - @ExtendWith(MockitoExtension.class)
     */
    @Mock
    MemberService memberServiceMock;

    @Mock
    StudyRepository studyRepositoryMock;

    @Test
    void createStudyServiceMock(){
        StudyService studyService = new StudyService(memberServiceMock, studyRepositoryMock);
        System.out.println("@Mock 애노테이션으로 전역으로 생성한 Mock 객체 테스트");
        assertNotNull(studyService);
    }

    @Test
    void createStudyServiceMockParameter(@Mock MemberService memberServiceMock2,
                                         @Mock StudyRepository studyRepositoryMock2){
        StudyService studyService = new StudyService(memberServiceMock2, studyRepositoryMock2);
        System.out.println("@Mock 애노테이션으로 파라미터 부분에 생성한 Mock 객체 테스트");
        assertNotNull(studyService);
    }

    @Test
    void createStudyService() {
        /**
         * 구현체가 없어서 인라인으로 구현체를 생성하여 할당
         */
        MemberService memberService = new MemberService() {
            @Override
            public Optional<Member> findById(Long memberId) {
                return Optional.empty();
            }
        };

        StudyRepository studyRepository = new StudyRepository() {
            @Override
            public List<Study> findAll() {
                return null;
            }

            @Override
            public List<Study> findAll(Sort sort) {
                return null;
            }

            @Override
            public List<Study> findAllById(Iterable<Long> iterable) {
                return null;
            }

            @Override
            public <S extends Study> List<S> saveAll(Iterable<S> iterable) {
                return null;
            }

            @Override
            public void flush() {

            }

            @Override
            public <S extends Study> S saveAndFlush(S s) {
                return null;
            }

            @Override
            public void deleteInBatch(Iterable<Study> iterable) {

            }

            @Override
            public void deleteAllInBatch() {

            }

            @Override
            public Study getOne(Long aLong) {
                return null;
            }

            @Override
            public <S extends Study> List<S> findAll(Example<S> example) {
                return null;
            }

            @Override
            public <S extends Study> List<S> findAll(Example<S> example, Sort sort) {
                return null;
            }

            @Override
            public Page<Study> findAll(Pageable pageable) {
                return null;
            }

            @Override
            public <S extends Study> S save(S s) {
                return null;
            }

            @Override
            public Optional<Study> findById(Long aLong) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(Long aLong) {
                return false;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(Long aLong) {

            }

            @Override
            public void delete(Study study) {

            }

            @Override
            public void deleteAll(Iterable<? extends Study> iterable) {

            }

            @Override
            public void deleteAll() {

            }

            @Override
            public <S extends Study> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public <S extends Study> Page<S> findAll(Example<S> example, Pageable pageable) {
                return null;
            }

            @Override
            public <S extends Study> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends Study> boolean exists(Example<S> example) {
                return false;
            }
        };

        StudyService studyService = new StudyService(memberService, studyRepository);
        System.out.println("인라인 구현체 생성 테스트");
        assertNotNull(studyService); //인스턴스 정상 생성여부 확인
        System.out.println("---------------");
        /**
         * 인터페이스의 구현체는 없으나 내가 사용하고 있는 코드에서 해당 인터페이스를 의존성으로 갖고 있고
         * 이 인터페이스를 기반으로 동작하는 코드를 작성하는 경우 Mock을 사용하기 좋다.(Mocking)
         * 즉, 구현체가 아직 준비가 안되있다거나 구현체가 이미 있더라도 특정 코드만 테스트하고 싶을 경우 Mock을 만들어서 테스트할 수 있다.
         *
         * Mokito를 사용하면 위에서 구현체를 인라인으로 생성하여 할당하는 작업을 대신해준다.
         */
        MemberService memberServiceMock = mock(MemberService.class); //Mock 객체 생성
        StudyRepository studyRepositoryMock = mock(StudyRepository.class); //Mock 객체 생성

        StudyService studyService1 = new StudyService(memberServiceMock, studyRepositoryMock);
        System.out.println("mock 메소드로 생성한 Mock 객체 테스트");
        assertNotNull(studyService1);
        System.out.println("---------------");
    }

}