package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.Chat;

@Mapper
public interface ChatDao {

//	@Select("""
//			SELECT * FROM chat
//			WHERE
//			    (senderId = #{id} AND receiverId = #{receiverId})
//			    OR (senderId = #{receiverId} AND receiverId = #{id})
//				ORDER BY `timestamp` ASC;
//			""")
//	List<Chat> getChat(int id, int receiverId);

	@Select("""
			SELECT
			    c.*,
			    sm.name AS senderName,
			    rm.name AS receiverName
			FROM chat AS c
				INNER JOIN member AS sm 
					ON c.senderId = sm.id
				INNER JOIN member AS rm 
					ON c.receiverId = rm.id
			WHERE
			    (c.senderId = #{id} AND c.receiverId = #{receiverId})
			    OR (c.senderId = #{receiverId} AND c.receiverId = #{id})
			ORDER BY c.`timestamp` ASC;
			""")
	List<Chat> getChat(int id, int receiverId);

	@Insert("""
			    INSERT INTO chat (senderId, receiverId, content, `timestamp`, isRead)
			    VALUES (#{senderId}, #{receiverId}, #{content}, NOW(), 1)
			""")
	void saveMessage(@Param("senderId") int senderId, @Param("receiverId") int receiverId,
			@Param("content") String content);
	
	@Update("""
		    UPDATE chat 
		    SET isRead = 0 
		    WHERE id = #{chatId}
		""")
		void read(@Param("chatId") int chatId);
	
}