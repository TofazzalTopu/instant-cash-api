package com.info.api.service.impl.common;

import com.info.api.entity.Branch;
import com.info.api.repository.BranchRepository;
import com.info.api.service.common.BranchService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;

    public BranchServiceImpl(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }


    @Override
    public List<Branch> findAll() {
        return branchRepository.findAll();
    }

    @Override
    public List<Integer> findAllBranchRoutingNumbers() {
        return findAll().stream().map(Branch::getRoutingNumber).collect(Collectors.toList());
    }
}
