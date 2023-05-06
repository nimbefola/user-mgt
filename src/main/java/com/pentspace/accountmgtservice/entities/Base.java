package com.pentspace.accountmgtservice.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@EntityListeners(BaseEntityListener.class)
public abstract class Base implements Serializable {

    private static final long serialVersionUID = 1L;
    @Version
    private Long version;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", insertable = true, updatable = true, unique = true, nullable = false)
    protected String id;

    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date created;
    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date updated;

    public String getId() {
        return id;
    }

    //public void setId(String id) {
//        this.id = id;
//    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Long getVersion() {
        return version;
    }

//    public void setVersion(Long version) {
//        this.version = version;
//    }
}
