package jpabook1.jpashop1.service;

import jpabook1.jpashop1.domain.Member;
import jpabook1.jpashop1.repository.MemberRepository;
import jpabook1.jpashop1.repository.OldMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    //private final OldMemberRepository memberRepository;
    private final MemberRepository memberRepository;

    /*
    //@Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    */

    //회원가입
    @Transactional(readOnly = false)
    public Long join(Member member) {
        validateDuplicateMember(member); //중복 회원검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원조회
    @Transactional(readOnly = true)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId).get();
    }

    //회원 수정(api)
    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findById(id).get();
        member.setName(name);
    }

}
