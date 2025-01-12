package com.info.api.service.common;

import com.info.api.entity.Branch;

import java.util.List;

public interface BranchService {

    List<Branch> findAll();
    List<Integer> findAllBranchRoutingNumbers();
}