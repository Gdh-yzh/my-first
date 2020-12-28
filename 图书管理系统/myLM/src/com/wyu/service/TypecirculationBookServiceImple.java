package com.wyu.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wyu.mapper.TypecirculationBookMapper;
import com.wyu.pojo.TypecirculationBook;
import com.wyu.pojo.TypecirculationBookExample;

@Service
public class TypecirculationBookServiceImple implements TypecirculationBookService {
	@Autowired
	private TypecirculationBookMapper mapper;// ����һ������

	@Override
	public List<TypecirculationBook> findCirculationBook() {
		TypecirculationBookExample example = new TypecirculationBookExample();
		// List<Book> list = mapper.selectByExample(example);
		List<TypecirculationBook> list = mapper.selectByExample(example);
		list.forEach(li -> System.out.println("ԭʼ����" + li));
		return list;
	}

}
