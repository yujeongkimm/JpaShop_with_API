package jpabook1.jpashop1.api;

import jpabook1.jpashop1.domain.Member;
import jpabook1.jpashop1.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 등록
     */
    @PostMapping("api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
       Member member = new Member();
       member.setName(request.getName());
       Long id = memberService.join(member);

       return new CreateMemberResponse(id);
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id){
            this.id = id;
        }
    }

    /**
     * 수정
     */
    @PostMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2 (@PathVariable("id") Long id,
                                                @RequestBody UpdateMemberRequest request) {
        memberService.update(id, request.getName());
        Member find = memberService.findOne(id);
        return new UpdateMemberResponse(find.getId(), find.getName());
    }

    //DTO (받는 값)
    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    //리턴값 (json으로)
    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    /**
     * 조회
     */
    @GetMapping("/api/v2/members")
    public Result membersV2() {
        List<Member> list = memberService.findMembers();
        //엔티티 -> DTO로 변환!
        List<MemberDto> collect = list.stream().map(m -> new MemberDto(m.getName(), m.getId()))
                .collect(Collectors.toList());

        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
        private Long id;
    }

    //리턴값
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }
}
