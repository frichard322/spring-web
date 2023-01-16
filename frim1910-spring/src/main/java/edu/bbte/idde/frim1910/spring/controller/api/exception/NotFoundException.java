package edu.bbte.idde.frim1910.spring.controller.api.exception;

import edu.bbte.idde.frim1910.spring.model.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@RequiredArgsConstructor
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    private final Class<? extends BaseEntity> entityClass;
    private final UUID id;

    @Override
    public String getMessage() {
        return "Could not find entity of type " + entityClass.getSimpleName() + " with ID " + id + ".";
    }

}
