package myblog.demo.service;

import myblog.demo.po.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> listCommentByBlogId(Long id);

    Comment saveComment(Comment comment);
}
