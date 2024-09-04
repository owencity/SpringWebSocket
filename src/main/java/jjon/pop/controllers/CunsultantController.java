package jjon.pop.controllers;

import jjon.pop.dtos.ChatroomDto;
import jjon.pop.dtos.MemberDto;
import jjon.pop.services.ConsultantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/consultants")
@RequiredArgsConstructor
@Controller
public class CunsultantController {

    private final ConsultantService consultantService;

    @ResponseBody
    @PostMapping
    public MemberDto saveMember(@RequestBody MemberDto memberDto) {
        return consultantService.saveMember(memberDto);
    }

    @GetMapping
    public String index() {
        return "consultants/index.html";
    }

    @ResponseBody
    @GetMapping("/chats")
    public Page<ChatroomDto> getChatroomPage( Pageable pageable) {
        return consultantService.getChatroomPage(pageable);
    }
}
// 일반 controller는 반환값을 데이터값이 아님 view name 으로 인식
// 단 ResponseBody를 붙이면 데이터값으로 반환 가능하다.
// pageable 쓰는데 왜 requestparam 쓰면안되는걸까?