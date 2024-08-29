package io.github.xpakx.images.priv;

import io.github.xpakx.images.account.User;
import io.github.xpakx.images.account.UserRepository;
import io.github.xpakx.images.image.error.UserNotFoundException;
import io.github.xpakx.images.priv.dto.MessageRequest;
import lombok.RequiredArgsConstructor;
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
}
