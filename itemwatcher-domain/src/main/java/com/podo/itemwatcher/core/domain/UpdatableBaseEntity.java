package com.podo.itemwatcher.core.domain;

import lombok.Getter;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class UpdatableBaseEntity extends BaseEntity {

    @LastModifiedDate
    private LocalDateTime updateAt;

    @LastModifiedBy
    private String updateBy;

}
