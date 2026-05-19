package com.jimmy_d.notes_backend.exceptions.rest;

public enum Types{
    EMAIL("email"),
    ID("id"),
    USERNAME("username"),
    TAG("tag");


    private final String value;

    Types(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }


}
