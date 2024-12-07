package com.rakeshnoothi.chit_chat.repo;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rakeshnoothi.chit_chat.entity.Channel;

public interface ChannelRepo extends JpaRepository<Channel, Long>{
	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
		       "FROM User u JOIN u.channels c " +
		       "WHERE u.id = :userId AND c.id = :channelId")
	boolean existsByChannelIdAndUserId(@Param("channelId") Long channelId, @Param("userId") Long userId);
	
	@Query("SELECT u.username FROM User u JOIN u.channels c WHERE c.id = :channelId")
    Set<String> findUsernamesByChannelId(@Param("channelId") Long channelId);

}
