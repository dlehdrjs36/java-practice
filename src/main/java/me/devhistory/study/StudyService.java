package me.devhistory.study;

import me.devhistory.domain.Member;
import me.devhistory.domain.Study;
import me.devhistory.member.MemberService;

import java.util.Optional;

public class StudyService {

    private final MemberService memberService;

    private final StudyRepository repository;

    public StudyService(MemberService memberService, StudyRepository repository) {
        assert memberService != null; //null이면 assert exception 발생
        assert repository != null; //null이면 assert exception 발생
        this.memberService = memberService;
        this.repository = repository;
    }

    //인터페이스 기반으로 코드 작성
    public Study createNewStudy(Long memberId, Study study){
        Optional<Member> member = memberService.findById(memberId);
        study.setOwnerId(member.orElseThrow(()-> new IllegalArgumentException("Member doesn't exist for id: '" + memberId + "'")).getId());
        return repository.save(study);
    }

}
