package com.example.advantumconverter.aspect;

import com.example.advantumconverter.api.bot.DistributionService;
import com.example.advantumconverter.exception.DatabaseException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Log4j2
@AllArgsConstructor
public class ExceptionAspect {

    private final DistributionService distributionService;

    @AfterThrowing(
            pointcut = "execution(* com.example.advantumconverter..*(..))",
            throwing = "ex"
    )
    public void handleSpecificException(DatabaseException ex) {
        log.error(ex);
        distributionService.sendTgMessageToAdmin("DatabaseException: " + ex.getMessage());
    }
}
