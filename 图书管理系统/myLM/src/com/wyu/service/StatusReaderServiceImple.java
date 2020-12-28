package com.wyu.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wyu.mapper.StatusReaderMapper;
import com.wyu.pojo.StatusReader;
import com.wyu.pojo.StatusReaderExample;

@Service
public class StatusReaderServiceImple implements StatusReaderService {
	@Autowired
	private StatusReaderMapper mapper;// ����һ������
	
	@Override
	public List<StatusReader> findStatusReader() {
		// TODO Auto-generated method stub
		StatusReaderExample example = new StatusReaderExample();
		// List<Book> list = mapper.selectByExample(example);
		List<StatusReader> list = mapper.selectByExample(example);
		list.forEach(li -> System.out.println("ԭʼ����" + li));
		return list;
	}



}
