package com.zkc.dao;

import com.zkc.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by zkc on 17/7/18.
 */
@Mapper
public interface MessageDAO {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content, has_read, conversation_id, created_date ";
    String SELECT_FIELDS = "id," + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{fromId},#{toId},#{content},#{hasRead},#{conversationId},#{createdDate})"})
    int addMessage(Message message);

    @Select({"select ", SELECT_FIELDS," from ",TABLE_NAME,
            " where conversation_id=#{conversationId} order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    //select *, count(id) as id || from (select * from wenda.message order by created_date desc) tt group by conversation_id order by created_date desc limit 0, 2;
    //@Select({"select ", INSERT_FIELDS," , count(id) as id from( select * from ",TABLE_NAME,
    //        "where from_id=#{userId} or to_id=#{userId} order by created_date desc) tt group by conversation_id order by created_date desc limit #{offset}, #{limit}"})
    @Select({"select message.*,tt.cnt from message,( select conversation_id,count(id) as cnt from " +
            "message group by conversation_id) tt where created_date in (select max(created_date) from " +
            "message group by conversation_id) and message.conversation_id=tt.conversation_id order " +
            "by created_date desc"})
    List<Message> getConversationList(@Param("userId") int userId,
                                        @Param("offset") int offset,
                                       @Param("limit") int limit);

}
