package jpabook1.jpashop1.service;

import jpabook1.jpashop1.domain.Member;
import jpabook1.jpashop1.repository.OldMemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional  //롤백함
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired
    OldMemberRepository memberRepository;

    //회원가입
    @Test
    //@Rollback(false) //롤백안하고 커밋하기 때문에 sql문이 db에 나감
    public void 회원가입() throws Exception {
        Member member = new Member();
        member.setName("kim");

        Long savedId = memberService.join(member);

        assertEquals(member, memberRepository.findOne(savedId));
    }

    //중복회원 검증
    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        memberService.join(member1);
        /*
        try {
            memberService.join(member2);
        } catch (IllegalStateException e){
            return;
        }
        */
        memberService.join(member2);

        fail("예외가 발생해야 한다.");
    }
}