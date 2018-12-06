package com.marcin.hashtagmessengerclient.cache;
/**************************************************************
 * Simple representation of user for caching purposes
 * only stores id and first name
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
public class CachedUser {
    private Long id;
    private String firstName;

    public CachedUser(Long id, String firstName) {
        this.id = id;
        this.firstName = firstName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
