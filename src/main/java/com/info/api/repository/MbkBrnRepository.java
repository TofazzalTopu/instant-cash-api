package com.info.api.repository;

import com.info.api.entity.MbkBrn;
import com.info.api.entity.MbkBrnKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MbkBrnRepository extends JpaRepository<MbkBrn, MbkBrnKey> {
}
