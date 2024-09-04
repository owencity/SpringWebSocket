package jjon.pop.dtos;

import jjon.pop.entities.Member;
import jjon.pop.enums.Gender;

import java.time.LocalDate;

public record MemberDto(
        Long id,
        String email,
        String nickName,
        String name,
        String password,
        String confirmedPassword,
        Gender gender,
        String phoneNumber,
        LocalDate birthday,
        String role)
// record -> 자바 16부터 도입된 새로운 종류 클래스 , 자동으로 생성자,equals, hashcode, toString() 메서드를 생성
// 불변 객체를 쉽게 만들기 위함

{
    public static MemberDto from(Member member) {
        return new MemberDto(
                member.getId(),
                member.getEmail(),
                member.getNickName(),
                member.getName(),
                null,
                null,
                member.getGender(),
                member.getPhoneNumber(),
                member.getBirthday(),
                member.getRole()
                // record는 자바의 일반 클래스와 유사하게 동작하므로 new 키워드를 사용하여 객체를 생성할 수 있다.
                // 또한, record는 모든 필드를 포함하는 생성자를 자동으로 제공하므로, new 키워드를 통해
                // 엔티티의 값을 전달받아 객체를 생성할 수 있다.
        );
    }
    // from 메서드는 Member엔티티 객체를 받아 DTO로 변환 , 데이터베이스에서 가져온 member 객체를 DTO로 변환하여
    // 클라이언트에 전달하거나 , 서비스 계층에서 사용 , 메서드명 직관적이지않아 fromEntity 가 적절할듯
        public static Member to(MemberDto memberDto) {
            return Member.builder()
                    .id(memberDto.id())
                    .email(memberDto.email())
                    .nickName(memberDto.nickName())
                    .name(memberDto.name())
                    .gender(memberDto.gender())
                    .phoneNumber(memberDto.phoneNumber())
                    .birthday(memberDto.birthday())
                    .role(memberDto.role())
                    .build();
            // record 는 아래 생성자를 자동으로 생성해주나 build 패턴으로 생성자보다 더유연하고 가독성좋게 MeberDto에 담을수있다
//public MemberDto(Long id, String email, String nickName, String name, String password, String confirmedPassword, Gender gender, String phoneNumber, LocalDate birthday, String role) {
//                this.id = id;
//                this.email = email;
//                this.nickName = nickName;
//                this.name = name;
//                this.password = password;
//                this.confirmedPassword = confirmedPassword;
//                this.gender = gender;
//                this.phoneNumber = phoneNumber;
//                this.birthday = birthday;
//                this.role = role;
//            }

        }
        // to 메서드는 DTO 객체를 받아 엔티티 객체로 변환 , 이메서드는 클라이언트로부터 받은 데이터를 데이터베이스에 저장
        // 할때 사용 메서드명 직관적이지 않아 toEntity 가 좋을듯

        /*
         static Dto 방식은 record 와 잘 어울린다 객체를 선언하지않고 static으로 DTO로 만들어 데이터를 보내거나
         받을수있다
         record 의 모토는 Data carrier or Immutable data class로 요약된다
         단순하게 데이터를 담고 운반하는 역활을 간결하게하고 기본적으로 불변성을 제공 생성 시 설정된 필드는 변경할수 없다.
         스레드 안전성 보장 , 함수형 프로그래밍 패턴에서 활용하기에 적합
         보일러플레이트 코드를 줄이기위함, 보일러플레이트를 완전히 제거하기 위한게아니라 특정한용도(불변데이터 운반)를 더
         쉽게 처리 하기 위해 사용 또한 자바 클래스의 대체제가 아니며 여전히 복잡한 로직, 상태변화, 상속계층 구조 등이
         필요한 경우 클래스 사용이 적합
         */
    }


