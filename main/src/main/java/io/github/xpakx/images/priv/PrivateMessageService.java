package io.github.xpakx.images.priv;

import io.github.xpakx.images.account.User;
import io.github.xpakx.images.account.UserRepository;
import io.github.xpakx.images.comment.error.CommentNotFoundException;
import io.github.xpakx.images.image.error.NotAnOwnerException;
import io.github.xpakx.images.image.error.UserNotFoundException;
import io.github.xpakx.images.priv.dto.MessageData;
import io.github.xpakx.images.priv.dto.MessageRequest;
import io.github.xpakx.images.priv.error.MessageNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivateMessageService {
    private final UserRepository userRepository;
    private final PrivateMessageRepository privateMessageRepository;

    public MessageData sendMessage(MessageRequest request, String username) {
        var sender = getUser(username);
        var receiver = getUser(request.recipient());
        var msg = new PrivateMessage();
        msg.setContent(request.content());
        msg.setReceiver(userRepository.getReferenceById(receiver.getId()));
        msg.setSender(userRepository.getReferenceById(sender.getId()));
        var result = privateMessageRepository.save(msg);
        return toDto(result, sender, receiver);
    }

    private Long getUserId(String username) {
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(UserNotFoundException::new);
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    public void deleteMessage(Long id, String username) {
        var message = privateMessageRepository
                .findById(id)
                .orElseThrow(() -> new MessageNotFoundException("No message with such id"));
        if(!message.inConversation(username)) {
            throw new NotAnOwnerException("Not in conversation");
        }
        privateMessageRepository.delete(message);
    }

    public Page<MessageData> getSentMessagePage(int page, String username) {
        var userId = getUserId(username);
        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
        return privateMessageRepository
                .findBySenderId(userId, pageable)
                .map(this::toDto);
    }

    public Page<MessageData> getMessagePage(int page, String username) {
        var userId = getUserId(username);
        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
        var result = privateMessageRepository
                .findByReceiverId(userId, pageable);
        markAsRead(result.getContent());
        return result.map(this::toDto);
    }

    public Page<MessageData> getUnreadMessagePage(int page, String username) {
        var userId = getUserId(username);
        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
        var result = privateMessageRepository
                .findByReceiverIdAndReadIsFalse(userId, pageable);
        markAsRead(result.getContent());
        return result.map(this::toDto);
    }

    private MessageData toDto(PrivateMessage message) {
        return new MessageData(
                message.getId(),
                message.getContent(),
                message.getSender().getUsername(),
                message.getSender().getAvatarUrl(),
                message.getReceiver().getUsername(),
                message.getReceiver().getAvatarUrl()
        );
    }

    private MessageData toDto(PrivateMessage message, User sender, User receiver) {
        return new MessageData(
                message.getId(),
                message.getContent(),
                sender.getUsername(),
                sender.getAvatarUrl(),
                receiver.getUsername(),
                receiver.getAvatarUrl()
        );
    }

    private void markAsRead(List<PrivateMessage> messages) {
        var toChange = messages.stream()
                .filter((msg) -> !msg.isRead())
                .toList();
        if (toChange.isEmpty()) {
            return;
        }
        toChange.forEach((msg) -> msg.setRead(true));
        privateMessageRepository.saveAll(toChange);
    }
}
