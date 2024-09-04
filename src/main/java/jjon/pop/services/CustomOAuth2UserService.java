package jjon.pop.services;

import jjon.pop.entities.Member;
import jjon.pop.enums.Gender;
import jjon.pop.repositories.MemberRepository;
import jjon.pop.vos.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService  extends DefaultOAuth2UserService {

    final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributeMap = oAuth2User.getAttribute("kakao_account");
        String email = (String) attributeMap.get("email");
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> registerMember(attributeMap));


        return new CustomOAuth2User(member, oAuth2User.getAttributes());
    }
    private Member registerMember (Map<String, Object> attributeMap) {
        Member member = Member.builder()
                .email((String) attributeMap.get("email"))
                .nickName((String) ((Map) attributeMap.get("profile")).get("nickname"))
                .name((String) attributeMap.get("name"))
                .phoneNumber((String) attributeMap.get("phone_number"))
                .gender(Gender.valueOf(((String) attributeMap.get("gender")).toUpperCase()))
                .birthday(getBirthday(attributeMap))
                .role("ROLE_USER")
                .build();

        return memberRepository.save(member);
    }

    private LocalDate getBirthday(Map<String, Object> attributeMap) {
        String birthYear = (String) attributeMap.get("birthyear");
        String birthDay = (String) attributeMap.get("birthday");

        return LocalDate.parse(birthYear + birthDay, DateTimeFormatter.BASIC_ISO_DATE);
    }
}
