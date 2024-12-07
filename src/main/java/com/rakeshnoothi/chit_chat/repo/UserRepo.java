package com.rakeshnoothi.chit_chat.repo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rakeshnoothi.chit_chat.dto.ChannelDTO;
import com.rakeshnoothi.chit_chat.dto.FriendDTO;
import com.rakeshnoothi.chit_chat.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);
	
	@Query("SELECT new com.rakeshnoothi.chit_chat.dto.FriendDTO(f.id, f.firstname, f.lastname, f.username) " +
	           "FROM User u JOIN u.friends f WHERE u.id = :userId")
	Set<FriendDTO> findFriendsByUserId(@Param("userId") Long userId);
	
	
	@Query("SELECT CASE WHEN COUNT(uf) > 0 THEN true ELSE false END " +
		       "FROM User u JOIN u.friends uf " +
		       "WHERE u.id = :userId AND uf.id = :friendId")
	boolean existsFriendship(@Param("userId") Long userId, @Param("friendId") Long friendId);
	
    @Query("SELECT new com.rakeshnoothi.chit_chat.dto.ChannelDTO(c.id, c.name, c.createdAt, c.totalMembers) " + 
    "FROM User u JOIN u.channels c WHERE u.id = :userId")
    List<ChannelDTO> findChannelsByUserId(@Param("userId") Long userId);
	
}
