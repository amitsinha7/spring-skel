package com.cepheid.cloud.skel.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cepheid.cloud.skel.model.Description;
import com.cepheid.cloud.skel.repository.DescriptionRepository;
import com.cepheid.cloud.skel.service.DescriptionService;

@Service
public class DescriptionServiceImpl implements DescriptionService {

	@Autowired
	private DescriptionRepository descriptionmRepository;

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public Description createDescription(Description description) {
		return descriptionmRepository.save(description);

	}
}
