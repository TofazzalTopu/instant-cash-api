package com.info.api.service.impl.common;

import com.info.api.entity.MbkBrn;
import com.info.api.repository.MbkBrnRepository;
import com.info.api.service.common.MbkBrnService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MbkBrnServiceImpl implements MbkBrnService {


    private final MbkBrnRepository mbkBrnRepository;

    public MbkBrnServiceImpl(MbkBrnRepository mbkBrnRepository) {
        this.mbkBrnRepository = mbkBrnRepository;
    }

    @Override
    public List<MbkBrn> findAll() {
        return mbkBrnRepository.findAll();
    }




}
