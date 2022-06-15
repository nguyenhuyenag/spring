package com.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.Ipsum;
import com.repository.LoremIpsumRepository;
import com.service.DataService;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import com.util.RandomUtils;

@Service
public class DataServiceImpl implements DataService {

	private static final Logger LOG = LoggerFactory.getLogger(DataServiceImpl.class);

	@Autowired
	private LoremIpsumRepository repository;

	@Override
	public void init() {
		Lorem lorem = LoremIpsum.getInstance();
		for (int i = 0; i < 50; i++) {
			Ipsum entity = new Ipsum();
			entity.setCode(RandomUtils.initCode());
			entity.setContent(lorem.getParagraphs(50, 200));
			if (repository.save(entity) != null) {
				LOG.info("Save {} to lorem_ipsum", entity.getCode());
			}
		}
	}

//	@Autowired
//	private HistoryRepository historyRepository;

//	@Override
//	public void reset() {
//		LOG.info("Reset database ...");
//		// historyRepository.deleteAll();
//		repository.resetTinhTrangGui(); // set = 0
//	}
//
//	@Override
//	public void onSuccess(HoaDon hoadon) {
//		String mtp = hoadon.getMatdiep();
//		repository.updateTinhTrangGui(hoadon.getGuid());
//		if (!historyRepository.existsByMaThongDiep(mtp)) {
//			historyRepository.save(new History(mtp));
//		} else {
//			LOG.info("Duplicate");
//			historyRepository.save(new History(mtp, "Duplicate"));
//			System.exit(0);
//		}
//	}

//	@Override
//	public List<HoaDon> findAllWithLimit(int limit) {
//		return repository.findAllWithLimit(limit);
//	}

//	@Override
//	public void onSuccess(com.model.Lorem hoadon) {
//		
//	}

}
