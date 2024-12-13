package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import dto.Message_1;

@Mapper
public interface MessageDao {

	@Insert("""
			INSERT INTO messages
				(senderId, receiverId, content, timestamp)
				VALUES (#{senderId}, #{receiverId}, #{content}, NOW())
			""")
	void sendMessage(int senderId, int receiverId, String content);

	@Select("""
            SELECT m.*, sender.name AS senderName, receiver.name AS receiverName
	            FROM messages m
		            JOIN member sender 
		            ON m.senderId = sender.id
		            JOIN member receiver 
		            ON m.receiverId = receiver.id
		            WHERE m.receiverId = #{memberId}
            """)
	List<Message_1> getReceivedMessages(int memberId);

	@Select("""
			    SELECT m.*, sender.name AS senderName, receiver.name AS receiverName
	            FROM messages m
		            JOIN member sender 
		            ON m.senderId = sender.id
		            JOIN member receiver 
		            ON m.receiverId = receiver.id
		            WHERE m.senderId = #{memberId}
			""")
	List<Message_1> getSentMessages(int memberId);

	@Select("""
			SELECT m.*, sender.name AS senderName, receiver.name AS receiverName
	            FROM messages m
			          JOIN member sender 
			          ON m.senderId = sender.id
			          JOIN member receiver 
			          ON m.receiverId = receiver.id
			          WHERE m.id = #{id}
			""")
	Message_1 getMessageById(int id);
}
