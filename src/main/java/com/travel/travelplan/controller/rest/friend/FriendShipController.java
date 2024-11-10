package com.travel.travelplan.controller.rest.friend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import com.travel.travelplan.service.friend.FriendService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/api/v1/friend")
@RequiredArgsConstructor
@RestControllerAdvice(basePackageClasses = FriendShipRestController.class)
public class FriendShipController {

    private final FriendService friendService;

    // 친구 요청 수락
    @GetMapping("/{id}/accept-friend") // 특수 케이스로 Get 만 가능하기 때문에 동사 사용
    public ModelAndView acceptRequest(ModelAndView mav, @PathVariable("id") Integer requestId, @RequestParam("token") String token) {
        mav.addObject("message", "요청을 수락하였습니다.");
        try {
            friendService.acceptFriendshipProcess(requestId, token);
        } catch (IllegalArgumentException e) {
            mav.addObject("message", e.getMessage());
        } catch (Exception e) {
            mav.addObject("message", "요청을 수락하는데 실패하였습니다. 잠시후 다시 시도해 주세요.");
        }
        mav.setViewName("friend/accept-reject");
        return mav;
    }

    // 친구 요청 거절
    @GetMapping("/{id}/reject-friend") // 특수 케이스로 Get 만 가능하기 때문에 동사 사용
    public ModelAndView rejectRequest(ModelAndView mav, @PathVariable("id") Integer requestId, @RequestParam("token") String token) {
        mav.addObject("message", "요청을 거절하였습니다.");
        try {
            friendService.rejectFriendshipProcess(requestId, token);
        } catch (IllegalArgumentException e) {
            mav.addObject("message", e.getMessage());
        } catch (Exception e) {
            mav.addObject("message", "요청을 거절하는데 실패하였습니다. 잠시후 다시 시도해 주세요.");
        }
        mav.setViewName("friend/accept-reject");
        return mav;
    }

}
