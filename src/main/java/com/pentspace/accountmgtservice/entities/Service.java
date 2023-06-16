package com.pentspace.accountmgtservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    public class Service {
        @Id
//        @GeneratedValue(strategy = GenerationType.SEQUENCE)
//        @Column(name = "id", nullable = false)
        private String id;

        @Column(name = "created")
        @Temporal(TemporalType.TIMESTAMP)
        protected Date created;

        @Column(name = "updated")
        @Temporal(TemporalType.TIMESTAMP)
        protected Date updated;

        private String title;
        private String description;
        private String serviceImageUrl;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;

        @JsonIgnore
        @ManyToMany(mappedBy = "services")
        private Set<Account> accounts;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Set<Account> getAccounts() {
            return accounts;
        }

        public void setAccounts(Set<Account> accounts) {
            this.accounts = accounts;
        }

//        public Long getId() {
//            return id;
//        }
//
//        public void setId(Long id) {
//            this.id = id;
//        }


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getServiceImageUrl() {
            return serviceImageUrl;
        }

        public void setServiceImageUrl(String serviceImageUrl) {
            this.serviceImageUrl = serviceImageUrl;
        }


    }