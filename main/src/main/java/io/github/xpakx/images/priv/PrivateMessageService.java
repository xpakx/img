package io.github.xpakx.images.priv;

import io.github.xpakx.images.account.User;
import io.github.xpakx.images.account.UserRepository;
import io.github.xpakx.images.comment.error.CommentNotFoundException;
import io.github.xpakx.images.image.error.NotAnOwnerException;
import io.github.xpakx.images.image.error.UserNotFoundException;
import io.github.xpakx.images.priv.dto.MessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrivateMessageService {
    private final UserRepository userRepository;
    private final PrivateMessageRepository privateMessageRepository;

    public PrivateMessage sendMessage(MessageRequest request, String username) {
        var senderId = getUserId(username);
        var receiverId = getUserId(request.recipient());
        var msg = new PrivateMessage();
        msg.setContent(request.content());
        msg.setReceiver(userRepository.getReferenceById(receiverId));
        msg.setSender(userRepository.getReferenceById(senderId));
        return privateMessageRepository.save(msg);
    }

    private Long getUserId(String username) {
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(UserNotFoundException::new);
    }

    public void deleteMessage(Long id, String username) {
        var message = privateMessageRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("No message with such id"));
        if(!message.inConversation(username)) {
            throw new NotAnOwnerException("Not in conversation");
        }
        privateMessageRepository.delete(message);
    }

    public Page<PrivateMessage> getMessagePage(int page, String username) {
        var userId = getUserId(username);
        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
        return privateMessageRepository
                .findByReceiverId(userId, pageable);
    }

    public Page<PrivateMessage> getSentMessagePage(int page, String username) {
        var userId = getUserId(username);
        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
        return privateMessageRepository
                .findBySenderId(userId, pageable);
    }

    public Page<PrivateMessage> getUnreadMessagePage(int page, String username) {
        var userId = getUserId(username);
        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
        return privateMessageRepository
                .findByReceiverIdAndReadIsFalse(userId, pageable);
    }
}
