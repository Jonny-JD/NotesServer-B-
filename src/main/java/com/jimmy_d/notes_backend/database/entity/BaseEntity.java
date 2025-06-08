package com.jimmy_d.notes_backend.database.entity;

import java.io.Serializable;

public interface BaseEntity <T extends Serializable>{
    T getId();
}
