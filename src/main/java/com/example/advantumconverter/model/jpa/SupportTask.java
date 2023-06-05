package com.example.advantumconverter.model.jpa;

import com.example.advantumconverter.enums.SupportTaskState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
@Entity(name = "support_task")
public class SupportTask {

    @Id
    @Column(name = "support_task_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supportTaskId;

    @Column(name = "employee_chat_id")
    private Long employeeChatId;

    @Column(name = "message_id")
    private Integer messageId;
    @Column(name = "converter_name")
    private String converterName;
    @Column(name = "error_text")
    private String errorText;

    @Column(name = "result_text")
    private String resultText;
    @Column(name = "task_state")
    private SupportTaskState taskState;

    @Column(name = "support_chat_id")
    @Nullable
    private Long supportChatId;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "close_at")
    private Timestamp closeAt;

    @Column(name = "filePath")
    private String filePath;
}
