package com.wyu.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wyu.mapper.TypeReaderMapper;
import com.wyu.pojo.TypeReader;
import com.wyu.pojo.TypeReaderExample;

@Service
public class TypeReaderServiceImple implements TypeReaderService {
	@Autowired
	private TypeReaderMapper mapper;// ����һ������
	
	@Override
	public List<TypeReader> findTypeReader() {
		// TODO Auto-generated method stub
		TypeReaderExample example = new TypeReaderExample();
		// List<Book> list = mapper.selectByExample(example);
		List<TypeReader> list = mapper.selectByExample(example);
		list.forEach(li -> System.out.println("ԭʼ����" + li));
		return list;
	}

}
