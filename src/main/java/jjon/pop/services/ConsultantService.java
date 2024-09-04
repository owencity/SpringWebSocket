package jjon.pop.services;

import jjon.pop.dtos.ChatroomDto;
import jjon.pop.dtos.MemberDto;
import jjon.pop.entities.Chatroom;
import jjon.pop.entities.Member;
import jjon.pop.enums.Role;
import jjon.pop.repositories.ChatroomRepository;
import jjon.pop.repositories.MemberRepository;
import jjon.pop.vos.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConsultantService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final ChatroomRepository chatroomRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByName(username).get();
   /*
         member는 Member 클래스의 객체 , 실제 데이터를 가지고 있는 인스턴스
         Optional -> Java8 이후 도입, Member 객체가 있을수도 없을수도 있는 상황을을 나타내는 컨테이너
         Optional<Member> optionalMember는 Optional<Member>타입 , Optional<Member>는 Member 객체를 직접
         포함하는것이 아니라 이를 감싸고 있는 Wrapper 객체 , 또한 Optional 클래스 내부적으로 get 메서드에 관한내용이
         정의되어있어 get 사용이 value 값을 반환 또는 null을 반환하여 에러를 던진다
    */
        if(Role.fromCode(member.getRole()) != Role.CONSULTANT) {
            throw new AccessDeniedException("상담사가 아닙니다.");
        }
        // Role -> enum 타입 따로 static 메서드생성하여 반환

        return new CustomUserDetails(member, null);
        // CustomUSerDetails에 저장하나 단, 간단하게 확인만하기위해 속성값을 전달하지않고 null 로 전달
    }

    public MemberDto saveMember(MemberDto memberDto) {
        Member member = MemberDto.to(memberDto);
        member.updatePassword(memberDto.password(), memberDto.confirmedPassword(), passwordEncoder);
        // memberdto 에 있는 dto를 member에 저장
        // updastepassword 에 dto 패스워드, confirmed패스워드 , bcrypt로 저장
        member = memberRepository.save(member);

        return MemberDto.from(member);
        // -> retrun 타입 MemberDto 타입아니라서 에러 (Member -> MemberDto로 교체)
        // member from 으로 dto 리턴
    }

        // 엔티티가 프레젠테이션 레이어까지 가는것은 옳지않다, 컨트롤러레이어에서 엔티티를 반환하기보다 엔티티를 DTO로반환해서 반환
    public Page<ChatroomDto> getChatroomPage(Pageable pageable) {
        Page<Chatroom> chatroomPage = chatroomRepository.findAll(pageable);
        // 간단한 페이징 처리위해 page 도입 , pageable을 찾아서 chatroompage에 저장
        // 여기서 pageable은 뭘갖고오는걸까?
        return chatroomPage.map(ChatroomDto::from);
        // map에 dto from 저장해서 리턴..?
        // map( :: ) 이 코드 의미는 ?
    }
}
