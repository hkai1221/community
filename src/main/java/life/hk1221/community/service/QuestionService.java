package life.hk1221.community.service;

import life.hk1221.community.dto.PaginationDTO;
import life.hk1221.community.dto.QuestionDTO;
import life.hk1221.community.mapper.QuestionMapper;
import life.hk1221.community.mapper.UserMapper;
import life.hk1221.community.model.Question;
import life.hk1221.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;


    public PaginationDTO list(Integer page, Integer size) {


        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalPage;
        Integer totalCount = questionMapper.count();

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        paginationDTO.setPagination(totalPage,page);

        if (page < 1)
            page = 1;
        if (page > totalPage)
            page = totalPage;
        Integer offset=size*(page-1);

        List<Question> questions=questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOList=new ArrayList<>();



        for(Question question:questions){
           User user= userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);



        return paginationDTO;
    }

    public PaginationDTO listByUserId(Integer userid,Integer page, Integer size){

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.countByUserId(userid);
        Integer totalPage;
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }


        if (page < 1)
            page = 1;
        if (page > totalPage)
            page = totalPage;

        paginationDTO.setPagination(totalPage,page);


        Integer offset=size*(page-1);
        List<Question> questions=questionMapper.listByUserId(userid,offset,size);
        List<QuestionDTO> questionDTOList=new ArrayList<>();


        for(Question question:questions){
            User user= userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }

    public QuestionDTO getById(Integer id) {
       Question question = questionMapper.getById(id);
       QuestionDTO questionDTO =new QuestionDTO();
       BeanUtils.copyProperties(question,questionDTO);
        return null;
    }
}