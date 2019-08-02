package life.hk1221.community.service;

import life.hk1221.community.advice.CustomizeErrorCode;
import life.hk1221.community.dto.PaginationDTO;
import life.hk1221.community.dto.QuestionDTO;
import life.hk1221.community.exception.CustomizeException;
import life.hk1221.community.mapper.QuestionMapper;
import life.hk1221.community.mapper.UserMapper;
import life.hk1221.community.model.Question;
import life.hk1221.community.model.QuestionExample;
import life.hk1221.community.model.User;
import org.apache.ibatis.session.RowBounds;
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
        Integer totalCount =(int)questionMapper.countByExample(new QuestionExample());

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
        List<Question> questions=questionMapper.selectByExampleWithRowbounds(new QuestionExample(),new RowBounds(offset,size));

        List<QuestionDTO> questionDTOList = new ArrayList<>();



        for(Question question:questions){
           User user= userMapper.selectByPrimaryKey(question.getCreator());
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
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userid);
        Integer totalCount =(int)questionMapper.countByExample(questionExample);
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

        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userid);
        List<Question> questions=questionMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));

        List<QuestionDTO> questionDTOList=new ArrayList<>();


        for(Question question:questions){
            User user= userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }

    public QuestionDTO getById(Integer id) {
       Question question = questionMapper.selectByPrimaryKey(id);
       if (question == null){
           throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
       }

       QuestionDTO questionDTO =new QuestionDTO();
       BeanUtils.copyProperties(question,questionDTO);
       User user= userMapper.selectByPrimaryKey(question.getCreator());
       questionDTO.setUser(user);

        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId() == null){
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        }else {
            //更新
            question.setGmtModified(System.currentTimeMillis());

            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(updateQuestion, example);
            if(updated !=1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }
}
