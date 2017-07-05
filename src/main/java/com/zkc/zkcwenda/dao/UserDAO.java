package com.zkc.zkcwenda.dao;

import com.zkc.zkcwenda.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * Created by zkc on 17/6/28.
 */
@Repository
@Mapper
public interface UserDAO {
    String TABLE_NAME = " user ";
    String INSERT_FILEDS = " name, password, salt, headUrl ";
    String SELECT_FIELDS = "id," + INSERT_FILEDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FILEDS,
            ") values (#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);

}
