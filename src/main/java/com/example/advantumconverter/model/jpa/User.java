package com.example.advantumconverter.model.jpa;

import com.example.advantumconverter.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Objects;

import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;


@Getter
@Setter
@ToString
@Entity(name = "user_tbl")
public class User {

    @Id
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_role")
    private UserRole userRole;

    @Column(name = "user_role_text")
    private String userRoletext;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "companyId")
    private Company company;

    @Override
    public String toString() {
        return "User{" +
                "chatId=" + chatId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", userRole=" + userRole.getTitle() +
                ", registeredAt=" + registeredAt +
                ", company=" + company +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getChatId().equals(user.getChatId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChatId());
    }

    public String getNameOrFirst() {
        return userName != null && !userName.equals(EMPTY) ? userName : firstName;
    }
}
