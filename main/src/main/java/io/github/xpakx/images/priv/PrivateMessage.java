package io.github.xpakx.images.priv;

import io.github.xpakx.images.account.User;
import io.github.xpakx.images.image.Image;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrivateMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    private String content;

    @Column(name = "is_read")
    private boolean read;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_Id", nullable = false)
    private User receiver;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }



    public boolean inConversation(String username) {
        if (sender.getUsername().equals(username)) {
            return true;
        }
        if (receiver.getUsername().equals(username)) {
            return true;
        }
        return false;
    }
}
