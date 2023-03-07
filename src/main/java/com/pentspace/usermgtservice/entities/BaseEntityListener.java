package com.pentspace.usermgtservice.entities;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

public class BaseEntityListener {
    @PrePersist
    public void prePersistEntity(Base entity){
        if(entity.getCreated() == null){
            entity.setCreated(new Date());
        }
        preUpdateEntity(entity);
    }


    @PreUpdate
    public void preUpdateEntity(Base entity){
        entity.setUpdated(new Date());
    }
}
