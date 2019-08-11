package life.hk1221.community.service;


import life.hk1221.community.model.Comment;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    public void insert(Comment comment){
        if(comment.getParentId() == null || comment.getParentId() == 0){
            return;
        }
    }
}
