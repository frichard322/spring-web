package edu.bbte.idde.frim1910.backend.model;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public abstract class BaseEntity implements Serializable {
    protected UUID uuid;

    public BaseEntity() {
        this.uuid = UUID.randomUUID();
    }
}

