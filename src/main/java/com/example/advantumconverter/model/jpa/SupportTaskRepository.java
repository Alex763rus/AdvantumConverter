package com.example.advantumconverter.model.jpa;

import com.example.advantumconverter.enums.SupportTaskState;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SupportTaskRepository extends CrudRepository<SupportTask, Long> {

    public List<SupportTask> findByTaskState(SupportTaskState supportTaskState);

    public List<SupportTask> findBySupportChatIdAndTaskState(Long chatId, SupportTaskState supportTaskState);
}
