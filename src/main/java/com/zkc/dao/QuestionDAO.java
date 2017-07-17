package com.zkc.dao;

import com.zkc.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * Created by zkc on 17/7/5.
 */
@Mapper
public interface QuestionDAO {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count ";
    String SELECT_FIELDS = "id," + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    int addQuestion(Question question);

    @Select({"select ", SELECT_FIELDS," from ",TABLE_NAME," where id=#{id}"})
    Question selectById(int id);
    List<Question> selectLatestQuestions(@Param("userId") int userId,
                                            @Param("offset") int offset,
                                            @Param("limit") int limit);
    //@Select({"select", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    //Question getById(int id);
}
