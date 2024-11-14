package com.rakeshnoothi.chit_chat.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rakeshnoothi.chit_chat.dto.FriendRequestDTO;
import com.rakeshnoothi.chit_chat.entity.FriendRequest;
import com.rakeshnoothi.chit_chat.entity.User;

public interface FriendRequestRepo extends JpaRepository<FriendRequest, Long>{
	boolean existsBySenderAndReceiver(User sender, User receiver);
	
	@Query("SELECT new com.rakeshnoothi.chit_chat.dto.FriendRequestDTO(" +
		       "f.id, f.createdAt, f.status, " +
		       "new com.rakeshnoothi.chit_chat.dto.UserDTO(f.sender.id, f.sender.firstname, f.sender.lastname, f.sender.username, f.sender.email)) " +
		       "FROM FriendRequest f WHERE f.receiver.id = :receiverId")
	List<FriendRequestDTO> findFriendRequestByReceiverId(@Param("receiverId") Long receiverId);
	
	@Query("SELECT new com.rakeshnoothi.chit_chat.dto.FriendRequestDTO(" +
		       "f.id, f.createdAt, f.status, " +
		       "new com.rakeshnoothi.chit_chat.dto.UserDTO(f.receiver.id, f.receiver.firstname, f.receiver.lastname, f.receiver.username, f.receiver.email)) " +
		       "FROM FriendRequest f WHERE f.sender.id = :senderId")
	List<FriendRequestDTO> findFriendRequestBySenderId(@Param("senderId") Long senderId);
	
	
}
