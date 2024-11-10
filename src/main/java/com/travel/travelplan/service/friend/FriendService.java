package com.travel.travelplan.service.friend;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.travelplan.components.SmtpComponent;
import com.travel.travelplan.dto.CustomUserDetails;
import com.travel.travelplan.entity.User;
import com.travel.travelplan.entity.friend.Friend;
import com.travel.travelplan.entity.friend.FriendRequest;
import com.travel.travelplan.events.friend.FriendShipAcceptEvent;
import com.travel.travelplan.events.friend.FriendShipRequestEvent;
import com.travel.travelplan.repository.UserRepository;
import com.travel.travelplan.repository.friend.FriendRepository;
import com.travel.travelplan.repository.friend.FriendRequestRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService{

    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;

    private final ApplicationEventPublisher eventPublisher; 
    private final SmtpComponent smtpComponent;

    // 친구 요청 프로세스
    @Transactional(rollbackFor = Exception.class)
    public void requestFriendshipProcess(HttpServletRequest req, Integer userFriendId) {
        // 대상 유저 조회
        User targetUser = userRepository.findById(userFriendId)
                .filter(User::getIsUsed)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        // 현재 유저 조회
        User currentUser = getCurrentUser()
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요합니다."));

        // 이미 친구인 경우
        if(friendRepository.existsByUserOwnAndUserFriend(currentUser, targetUser)) {
            throw new IllegalArgumentException("이미 친구인 상태입니다.");
        }

        // 이미 요청한 경우
        if(friendRequestRepository.existsByRequestUserAndTargetUserAndSentDateBefore(currentUser, targetUser, LocalDateTime.now().plusDays(14))) {
            throw new IllegalArgumentException("이미 친구 요청을 보낸 상태입니다.");
        }

        // 이미 요청 받은 경우
        if(friendRequestRepository.existsByRequestUserAndTargetUserAndSentDateBefore(targetUser, currentUser, LocalDateTime.now().plusDays(14))) {
            throw new IllegalArgumentException("이미 친구 요청을 받은 상태입니다.");
        }

        // 친구 요청
        String scheme = req.getScheme();
        String serverName = req.getServerName();
        int serverPort = req.getServerPort();
        String url;
        if ((scheme.equals("http") && serverPort == 80) || (scheme.equals("https") && serverPort == 443)) {
            url = String.format("%s://%s", scheme, serverName);
        } else {
            url = String.format("%s://%s:%d", scheme, serverName, serverPort);
        }

        FriendRequest friendRequest = FriendRequest.builder()
                .requestUser(currentUser)
                .targetUser(targetUser)
                .url(url)
                .build();
        friendRequestRepository.save(friendRequest);

        // 이벤트 발행
        FriendShipRequestEvent friendRequestEvent = new FriendShipRequestEvent(friendRequest);
        eventPublisher.publishEvent(friendRequestEvent);

    }

    // 친구 추가 요청 이벤트 처리 ( 메일 발송 )
    @Async
    @EventListener(classes = FriendShipRequestEvent.class)
    @Retryable(
        maxAttempts = 3, // 최대 재시도 횟수
        backoff = @Backoff(delay = 1000, multiplier = 2) // 재시도 간격 (밀리초)
    )
    public void onFriendRequest(FriendShipRequestEvent friendShipRequestEvent) {
        log.info("친구 추가 요청 이벤트 발생 : {}", friendShipRequestEvent);

        FriendRequest friendRequest = friendShipRequestEvent.getFriendRequest();
        User requestUser = friendRequest.getRequestUser();
        User targetUser = friendRequest.getTargetUser();

        String url = friendRequest.getUrl();

        // 이메일 전송
        String targetEmail = targetUser.getUsername();
        String subject = "친구 추가 요청";
        String text = String.format("<p>%s(%s)님으로부터 친구 추가 요청을 받았습니다. 수락하시겠습니까?</p><br><br><a href='%s/api/v1/friend/%d/accept-friend?token=%s'>수락하기</a><br><a href='%s/api/v1/friend/%d/reject-friend?token=%s'>거절하기</a>", requestUser.getNickName(), requestUser.getUsername(), url, friendRequest.getId(), friendRequest.getToken(), url, friendRequest.getId(), friendRequest.getToken());

        if(smtpComponent.mailSend(targetEmail, subject, text)) {
            log.info("이메일 전송 성공");
            friendRequest.send();
            friendRequestRepository.save(friendRequest);

        } else {
            log.error("이메일 전송 실패");
            throw new IllegalArgumentException("이메일 전송에 실패했습니다.");
        }

    }

    // 친구 요청 수락 프로세스
    @Transactional(rollbackFor = Exception.class)
    public void acceptFriendshipProcess(Integer friendMailId, String token) {
        FriendRequest friendRequest = friendRequestRepository.findById(friendMailId)
                .filter(f -> f.getToken().equals(token))
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 요청입니다."));

        if(friendRequest.getIsAccepted()) {
            throw new IllegalArgumentException("이미 수락된 요청입니다.");
        }

        User loginUser = getCurrentUser()
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요합니다."));

        if(!loginUser.getUsername().equals(friendRequest.getTargetUser().getUsername())) {
            throw new IllegalArgumentException("해당 요청에 대한 권한이 없습니다.");
        }

        friendRequest.accept();
        friendRequestRepository.save(friendRequest);

        User currentUser = friendRequest.getTargetUser();
        User targetUser = friendRequest.getRequestUser();

        Friend friend = Friend.builder()
                .userOwn(currentUser)
                .userFriend(targetUser)
                .friendNickName(targetUser.getNickName())
                .build();
        Friend friend2 = Friend.builder()
                .userOwn(targetUser)
                .userFriend(currentUser)
                .friendNickName(currentUser.getNickName())
                .build();

        friendRepository.saveAll(List.of(friend, friend2));

        FriendShipAcceptEvent event = new FriendShipAcceptEvent(friendRequest);
        eventPublisher.publishEvent(event); // 친구 추가 완료 이벤트 발생 (뭐 대략 상대한테 다시 뭐 보내던가 할 수 는 있음)
    }

    // 친구 요청 거절 프로세스
    @Transactional(rollbackFor = Exception.class)
    public void rejectFriendshipProcess(Integer friendMailId, String token) {
        FriendRequest friendRequest = friendRequestRepository.findById(friendMailId)
                .filter(f -> f.getToken().equals(token))
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 요청입니다."));

        if(friendRequest.getIsAccepted()) {
            throw new IllegalArgumentException("이미 수락된 요청입니다.");
        }

        User loginUser = getCurrentUser()
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요합니다."));

        if(!loginUser.equals(friendRequest.getTargetUser())) {
            throw new IllegalArgumentException("해당 요청에 대한 권한이 없습니다.");
        }

        friendRequest.reject();
        friendRequestRepository.save(friendRequest);

        FriendShipRequestEvent event = new FriendShipRequestEvent(friendRequest);
        eventPublisher.publishEvent(event);
    }

    // 친구 수정 하기
    public Boolean updateFriend(Integer userFriendId) {
        return null;
    }

    // 친구 삭제 하기
    public Boolean deleteFriend(Integer userFriendId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteFriend'");
    }

    // 유저 친구 목록 보기
    public List<User> findFriendListByUser(Integer userId, Pageable pageable, String search) {
        User user = userRepository.findById(userId)
                .filter(User::getIsUsed)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        List<User> userList = friendRepository.findFriendListByUser(user, pageable, search);
        return userList;
    }

    // 나의 친구 목록 보기
    public List<User> findFriendListByUser(Pageable pageable, String search) {
        User currentUser = getCurrentUser()
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요합니다."));

        return findFriendListByUser(currentUser.getId(), pageable, search);
    }

    // 친구 요청 목록 보기
    public List<FriendRequest> findFriendRequestListByUser(Pageable pageable, String search) {
        User currentUser = getCurrentUser()
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요합니다."));
        return friendRequestRepository.findFriendRequestListByUser(currentUser, pageable, search);
    }

    // 친구 요청 받은 목록 보기
    public List<FriendRequest> findFriendRequestReceivedListByUser(Pageable pageable, String search) {
        User currentUser = getCurrentUser()
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요합니다."));
        return friendRequestRepository.findFriendRequestReceivedListByUser(currentUser, pageable, search);
    }

    private Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return Optional.ofNullable(((CustomUserDetails) authentication.getPrincipal()).getUser());
        } else {
            return Optional.empty();
        }
    }

}
