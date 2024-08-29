package io.github.xpakx.images.priv;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long> {
    Page<PrivateMessage> findByReceiverId(Long receiverId, Pageable pageable);
    Page<PrivateMessage> findBySenderId(Long senderId, Pageable pageable);

    Page<PrivateMessage> findByReceiverIdAndReadIsFalse(Long userId, Pageable pageable);
}