package practicum.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import practicum.comment.dto.CommentDto;
import practicum.comment.dto.NewCommentDto;
import practicum.comment.model.Comment;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "event", ignore = true)
    Comment newCommentDtoToComment(NewCommentDto newCommentDto);

    CommentDto commentToCommentDto(Comment comment);

    List<CommentDto> listCommentToListCommentDto(List<Comment> comment);
}
