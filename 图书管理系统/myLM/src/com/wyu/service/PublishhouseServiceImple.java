package com.wyu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wyu.mapper.PublishhouseMapper;
import com.wyu.pojo.Publishhouse;
import com.wyu.pojo.PublishhouseExample;

@Service
public class PublishhouseServiceImple implements PublishhouseService {
	@Autowired
	private PublishhouseMapper mapper;// ����һ������

	@Override
	public List<Publishhouse> findCirculationBook() {
		PublishhouseExample example = new PublishhouseExample();
		// List<Book> list = mapper.selectByExample(example);
		List<Publishhouse> list = mapper.selectByExample(example);
		list.forEach(li -> System.out.println("ԭʼ����" + li));
		return list;
	}

}
