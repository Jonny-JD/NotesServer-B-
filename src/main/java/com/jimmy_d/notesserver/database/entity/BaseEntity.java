package com.jimmy_d.notesserver.database.entity;

import java.io.Serializable;

public interface BaseEntity <T extends Serializable>{
    T getId();
}
