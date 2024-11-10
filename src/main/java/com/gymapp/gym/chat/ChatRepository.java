package com.gymapp.gym.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {

    List<Chat> findAllBySenderIdAndReceiverId(int senderId, int receiverId);
    List<Chat> findAllBySenderIdAndReceiverIdAndStatus(int senderId, int receiverId, String status);
    Page<Chat> findAllBySenderIdAndReceiverIdOrSenderIdAndReceiverId(Integer senderId, Integer receiverId, Integer receiverId2, Integer senderId2, Pageable pageable);
    Optional<List<Chat>> findAllByReceiverIdAndReceiverStatus(int receiverId, String status);


}
