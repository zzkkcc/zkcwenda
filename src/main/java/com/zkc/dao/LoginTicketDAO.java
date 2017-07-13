package com.zkc.dao;

import com.zkc.model.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * Created by zkc on 17/7/12.
 */
@Mapper
public interface LoginTicketDAO {
    String TABLE_NAME = "login_ticket";
    String INSET_FIELDS = " user_id, expired, status, ticket ";
    String SELECT_FIELDS = " id, " + INSET_FIELDS;

    @Insert({"insert into " + TABLE_NAME, "(", INSET_FIELDS,
            ")values(#{userId}, #{expired}, #{status}, #{ticket})"})
    int addTicket(LoginTicket ticket);

    @Select({"select ", SELECT_FIELDS, "from", TABLE_NAME, "where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Update({"update ", TABLE_NAME, "set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("ticket") String ticket,
                      @Param("status") int status);
}
