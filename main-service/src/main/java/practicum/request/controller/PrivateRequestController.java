package practicum.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import practicum.request.dto.ParticipationRequestDto;
import practicum.request.mapper.RequestMapper;
import practicum.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestController {

    private final RequestService requestService;
    private final RequestMapper requestMapper;

    @GetMapping("/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable Long userId) {
        log.info("Calling GET: /users/{userId}/requests with 'userId': {}", userId);
        return requestMapper.listRequestToListRequestDto(requestService.getUserRequests(userId));
    }

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addUserRequest(@PathVariable Long userId,
                                                  @RequestParam(name = "eventId") Long eventId) {
        log.info("Calling POST: /users/{userId}/requests with 'userId': {}, 'eventId': {}", userId, eventId);
        return requestMapper.requestToRequestDto(requestService.addParticipationRequest(userId, eventId));
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto updateUserRequest(@PathVariable Long userId,
                                                     @PathVariable Long requestId) {
        log.info("Calling PATCH: /users/{userId}/requests/{requestId}/cancel with 'userId': {}, 'requestId': {}", userId, requestId);
        return requestMapper.requestToRequestDto(requestService.cancelParticipationRequest(userId, requestId));
    }
}