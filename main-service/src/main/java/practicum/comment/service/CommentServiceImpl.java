package practicum.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practicum.comment.dto.CommentStatusUpdateRequest;
import practicum.comment.dto.NewCommentDto;
import practicum.comment.mapper.CommentMapper;
import practicum.comment.model.Comment;
import practicum.comment.model.CommentStatus;
import practicum.comment.repository.CommentRepository;
import practicum.comment.repository.CommentSpecRepository;
import practicum.event.model.Event;
import practicum.event.model.enums.EventState;
import practicum.event.repository.EventRepository;
import practicum.exception.IncorrectRequestException;
import practicum.exception.ObjectNotFoundException;
import practicum.exception.RequestConflictException;
import practicum.user.model.User;
import practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specification.where;
import static practicum.comment.repository.CommentSpecRepository.*;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final CommentSpecRepository commentSpecRepository;
    private final CommentMapper commentMapper;

    @Override
    public Comment addUserComment(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User commenter = userRepository.findById(userId).orElseThrow(() -> {
            throw new ObjectNotFoundException("User with id = " + userId + " was not found.");
        });

        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new ObjectNotFoundException("Event with id = " + eventId + " doesn't exist.");
        });

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new RequestConflictException("Users are not allowed to comment on unpublished events.");
        }

        Comment comment = commentMapper.newCommentDtoToComment(newCommentDto);
        comment.setUser(commenter);
        comment.setEvent(event);
        comment.setCreatedOn(LocalDateTime.now());
        comment.setStatus(CommentStatus.PENDING);

        return commentRepository.save(comment);
    }

    @Override
    public Comment updateUserComment(Long userId, Long eventId, Long commentId, NewCommentDto newCommentDto) {
        Comment comment = commentRepository.findByIdAndUserIdAndEventId(commentId, userId, eventId).orElseThrow(() -> {
            throw new ObjectNotFoundException("Comment with id = " + commentId + " by user id = " + userId +
                    " for event id = " + eventId + " doesn't exist.");
        });

        if (comment.getStatus().equals(CommentStatus.PENDING)) {
            throw new RequestConflictException("Users are not allowed to update comments, which are pending moderation.");
        }

        comment.setText(newCommentDto.getText());
        comment.setCreatedOn(LocalDateTime.now());
        comment.setStatus(CommentStatus.PENDING);

        return commentRepository.save(comment);
    }

    @Override
    public Comment getUserEventComment(Long userId, Long eventId, Long commentId) {
        Comment comment = commentRepository.findByIdAndUserIdAndEventId(commentId, userId, eventId).orElseThrow(() -> {
            throw new ObjectNotFoundException("Comment with id = " + commentId + " by user id = " + userId +
                    " for event id = " + eventId + " doesn't exist.");
        });

        if (comment.getStatus().equals(CommentStatus.PENDING)) {
            throw new RequestConflictException("Users are not allowed to review comments, which are pending moderation.");
        }

        return comment;
    }

    @Override
    public List<Comment> getAllUserComments(Long userId) {
        List<Comment> comments = commentRepository.findAllByUserIdAndStatus(userId, CommentStatus.PUBLISHED);

        if (!comments.isEmpty()) {
            return comments;
        }

        return new ArrayList<>();
    }

    @Override
    public List<Comment> getAllUserEventComments(Long userId, Long eventId) {
        List<Comment> comments = commentRepository.findAllByUserIdAndEventIdAndStatus(userId, eventId, CommentStatus.PUBLISHED);

        if (!comments.isEmpty()) {
            return comments;
        }

        return new ArrayList<>();
    }

    @Override
    @Transactional
    public void deleteUserComment(Long userId, Long eventId, Long commentId) {
        if (commentRepository.existsByIdAndUserIdAndEventIdAndStatus(commentId, userId, eventId, CommentStatus.PUBLISHED)) {
            commentRepository.deleteByIdAndUserIdAndEventIdAndStatus(commentId, userId, eventId, CommentStatus.PUBLISHED);
        } else {
            throw new ObjectNotFoundException("Comment with id = " + commentId + " by user id = " + userId +
                    " for event id = " + eventId + " is pending moderation or doesn't exist.");
        }
    }

    @Override
    public List<Comment> getAdminComments(String text, List<Long> users, List<CommentStatus> statuses, List<Long> events,
                                          LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                          Integer from, Integer size) {

        if ((rangeStart != null && rangeEnd != null) && (rangeStart.isAfter(rangeEnd) || rangeStart.isEqual(rangeEnd))) {
            throw new IncorrectRequestException("Start time must not after or equal to end time.");
        }

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").ascending());

        Page<Comment> commentsPage = commentSpecRepository.findAll(where(hasText(text))
                .and(hasUsers(users))
                .and(hasStatuses(statuses))
                        .and(hasEvents(events))
                        .and(hasRangeStart(rangeStart))
                        .and(hasRangeEnd(rangeEnd)), pageable);

        return commentsPage.stream()
                .collect(Collectors.toList());
    }

    @Override
    public List<Comment> moderateAdminComments(CommentStatusUpdateRequest updateRequest) {
        List<Comment> comments = commentRepository.findAllByIdInAndStatus(updateRequest.getCommentIds(), CommentStatus.PENDING);

        if (comments.size() != updateRequest.getCommentIds().size()) {
            throw new ObjectNotFoundException("Incorrect comment id(s) in the request body.");
        }

        switch (updateRequest.getStatus()) {
            case PUBLISHED:
                comments.forEach(comment -> comment.setStatus(CommentStatus.PUBLISHED));
                comments = commentRepository.saveAll(comments);
                return comments;
            case DELETED:
                comments.forEach(comment -> commentRepository.deleteAllByIdIn(updateRequest.getCommentIds()));
                return new ArrayList<>();
            default:
                throw new IncorrectRequestException("Incorrect admin moderate request with status 'Pending'.");
        }
    }
}