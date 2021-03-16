package com.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entity.Vocabulary;
import com.repository.VocabRepository;
import com.service.VocabService;

@Service
public class VocabServiceImpl implements VocabService {

	@Autowired
	private VocabRepository repository;

	private final int N = 4;

	private Set<String> ignoreWords = new HashSet<>();

	/**
	 * Random ngẫu nhiên trong List: [min, max + 1]
	 */
	private Vocabulary randomFromList(List<Vocabulary> list) {
		int index = ThreadLocalRandom.current().nextInt(0, (list.size() - 1) + 1);
		return list.get(index);
	}

	/**
	 * Kiểm tra không trùng với từ phía trước và tăng biến count
	 */
	private Vocabulary handle(Vocabulary vocab, String flag) {
		String word = vocab.getWord();
		if (!ignoreWords.contains(word)) {	// check exits
			ignoreWords.add(word);
			if (StringUtils.isNotEmpty(vocab.getPronounce())) {
				if ("1".equals(flag)) {
					increaseCountById(vocab); // increate count
				}
				// string fist uppercase
				vocab.setTranslate(StringUtils.capitalize(vocab.getTranslate()));
				return vocab;
			}
		}
		return null;
	}

	@Override
	public Vocabulary getRandomVocab(String flag) {
		while (true) {
			Vocabulary vocab = repository.getRandomWord();
			if (vocab != null) {
				vocab = handle(vocab, flag);
				if (vocab != null) {
					return vocab;
				}
			}
		}
	}

	@Override
	public Vocabulary getRandomVocab2(String flag) {
		while (true) {
			Vocabulary vocab = repository.getRandomWord();
			List<Vocabulary> list = repository.getListVocabLimitByCount(vocab.getCount(), N);
			list.add(vocab);
			vocab = randomFromList(list);
			vocab = handle(vocab, flag);
			if (vocab != null) {
				return vocab;
			}
		}
	}

	@Transactional
	@Override
	public void increaseCountById(Vocabulary vocab) {
		vocab.setCount(vocab.getCount() + 1);
		repository.save(vocab);
	}

}
