package com.wyu.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wyu.mapper.BorrowMapper;
import com.wyu.pojo.Borrow;
import com.wyu.pojo.BorrowAll;
import com.wyu.pojo.BorrowExample;
import com.wyu.pojo.BorrowExample.Criteria;

@Service
public class BorrowServiceImple implements BorrowService {
	@Autowired
	private BorrowMapper mapper;// ����һ������
	
	
//1.��ѯ���߽��ı���δ�黹	
@Override
public int countByReaderNumber(String readernumber) {
	// TODO Auto-generated method stub
	BorrowExample example = new BorrowExample();
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	Date dateTime;
	try {
		dateTime = simpleDateFormat.parse("0003-00-01");
		//���߱�ź�endDateΪ��
		example.createCriteria().andReadernumberEqualTo(readernumber).andEnddateLessThan(dateTime);
		System.out.println("����ִ������");
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//���߱�ź͹黹����>����
	//example.createCriteria().andReadernumberEqualTo(readernumber).andReturndateGreaterThan(new Date());
	
	int in = mapper.countByExample(example);
	return in;
}
//2.��ѯ���ߵ�ͼ���Ƿ�����
@Override
public int countOverdueByReaderNumber(String readernumber) {
	// TODO Auto-generated method stub
	BorrowExample example = new BorrowExample();
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	Date dateTime;
	try {
		dateTime = simpleDateFormat.parse("0003-00-01");
		//���߱�ź�endDateΪ��
		example.createCriteria().andReadernumberEqualTo(readernumber).andEnddateLessThan(dateTime).andReturndateLessThan(new Date());
		System.out.println("����ִ���Ƿ�����");
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	int in = mapper.countByExample(example);
	return in;
}
//3.��ӽ�����Ϣ
@Override
public int addBorrowByNumbers(Borrow borrow) {
	int in = mapper.insert(borrow);
	
	// TODO Auto-generated method stub
	return in;
}
//4.ͼ����ĵĴ���
@Override
public int borrowCount(String booknumber) {
	BorrowExample example = new BorrowExample();
	Criteria criteria = example.createCriteria();
	criteria.andBooknumberEqualTo(booknumber);
	int in = mapper.countByExample(example);
	// TODO Auto-generated method stub
	return in;
}
//5.��ѯδ�黹��ͼ��-����ͼ����
@Override
public List<Borrow> findBorrowBybookNumberNoReturn(String booknumber) {
	// TODO Auto-generated method stub
	BorrowExample example = new BorrowExample();
	Criteria criteria = example.createCriteria();
	//ͼ��
	criteria.andBooknumberEqualTo(booknumber);
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	Date dateTime;
	try {
		System.out.println("�黹ʱ��С��0003-00-01");
		dateTime = simpleDateFormat.parse("0003-00-01");
		//���߱�ź�endDateΪ��
		
		criteria.andEnddateLessThan(dateTime);
		System.out.println("�黹ʱ��try-catch");
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	List<Borrow> borrow = mapper.selectByExample(example);
	
	
	return borrow;
}

//5.��ѯδ�黹��ͼ��-���ݶ��߱��
@Override
public List<Borrow> findBorrowByreaderNumberNoReturn(String readernumber) {
	// TODO Auto-generated method stub
	BorrowExample example = new BorrowExample();
	Criteria criteria = example.createCriteria();
	//ͼ��
	criteria.andReadernumberEqualTo(readernumber);
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	Date dateTime;
	try {
		System.out.println("�黹ʱ��С��0003-00-01");
		dateTime = simpleDateFormat.parse("0003-00-01");
		//���߱�ź�endDateΪ��
		
		criteria.andEnddateLessThan(dateTime);
		System.out.println("�黹ʱ��try-catch");
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	List<Borrow> borrow = mapper.selectByExample(example);
	
	
	return borrow;
}

//6.���ݽ��ı�Ų�ѯͼ��
@Override
public Borrow findBorrowByNumber(String borrownumber) {
	Borrow borrow = mapper.selectByPrimaryKey(borrownumber);
	// TODO Auto-generated method stub
	return borrow;
}
@Override
public int upDateBorrow(Borrow borrow) {
	
	int in = mapper.updateByPrimaryKey(borrow);
	// TODO Auto-generated method stub
	return in;
}
//7.����ͼ��ͨ��select Column
@Override
public int upDateBorrowBySelect(Borrow borrow) {
	BorrowExample example = new BorrowExample();
	Criteria criteria = example.createCriteria();
	if(borrow.getBorrownumber()!=null) {
		criteria.andBorrownumberEqualTo(borrow.getBorrownumber());
		borrow.setBorrownumber(null);
	}else {
		System.out.println("updateBook()��borrownumberΪ��");
	}
	int in = mapper.updateByExampleSelective(borrow, example);
	return in;
}

//8.��ѯ����
@Override
public int deleteBorrowByNumbers(List<String> numbers) {
	BorrowExample example = new BorrowExample();
	Criteria criteria = example.createCriteria();
	criteria.andBorrownumberIn(numbers);
	// TODO Auto-generated method stub
	int i = mapper.deleteByExample(example);
	return i;
}

@Override
public List<BorrowAll> findBorrowsByText(String text) {

	BorrowExample example = new BorrowExample();
	String likeText = "%" + text + "%";
	Criteria criteria = example.createCriteria();
	System.out.println(text + ":text");

	criteria.andBooknumberLike(likeText);
	example.or().andBorrownumberLike(likeText);
	example.or().andReadernumberLike(likeText);
	// example.or(criteria);

	List<BorrowAll> borrow = mapper.selectByExampleBorrowAll(example);
	// List<Book> book = mapper.selectBybookExample(example);
	// TODO Auto-generated method stub
	return borrow;
}

@Override
public List<BorrowAll> findBorrowsBySort(String text, String sortName, String sortOrder) {
	// TODO Auto-generated method stub
	BorrowExample example = new BorrowExample();
	String likeText = "%" + text + "%";
	Criteria criteria = example.createCriteria();
	criteria.andBooknumberLike(likeText);
	example.or().andBorrownumberLike(likeText);
	example.or().andReadernumberLike(likeText);

	//criteria.andReadernameLike(likeText);
	//example.or().andReadernumberLike(likeText);
	if(sortName!=null&&sortOrder!=null&&!sortName.equals("")&&!sortOrder.equals("")) {
		example.setOrderByClause(sortName + " " + sortOrder);
	}

	List<BorrowAll> list = mapper.selectByExampleBorrowAll(example);

	list.forEach(li -> System.out.println("ԭʼ����" + li));
	return list;
}		
		
//ͼ���������:	17��
@Override
public int borrowAmounts() {
	// TODO Auto-generated method stub
	BorrowExample example = new BorrowExample();
	int in = mapper.countByExample(example);
	return in;
}
//ͼ��δ�黹����:
@Override
public int noReturnAmounts() {
	// TODO Auto-generated method stub
	BorrowExample example = new BorrowExample();
	Criteria criteria = example.createCriteria();
	//ͼ��
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	Date dateTime;
	try {
		System.out.println("�黹ʱ��С��0003-00-01");
		dateTime = simpleDateFormat.parse("0003-00-01");
		//���߱�ź�endDateΪ��
		criteria.andEnddateLessThan(dateTime);
		System.out.println("�黹ʱ��try-catch");
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	int in = mapper.countByExample(example);
	return in;
}
//����ͼ����߽��Ĵ���:	
@Override
public int topBorrowAmounts() {
	// TODO Auto-generated method stub
	
	BorrowAll ins = mapper.countMaxBookBorrow();
	//int in = Integer.parseInt(ins);
	System.out.println("11ins"+ins);
	int in = ins.getMaxBorrow().getMaxBook();
	return in;
}
//��������:
@Override
public int readerBorrowAmounts() {
	// TODO Auto-generated method stub
	BorrowAll ins = mapper.countReaderAmountBorrow();
	System.out.println("22ins"+ins);
	int in = ins.getMaxBorrow().getReaderAmount();
	//int in = Integer.parseInt(ins);
	return in;
}
@Override
public int deleteBorrowByNumber(String number) {
	// TODO Auto-generated method stub
		int in = mapper.deleteByPrimaryKey(number);
		return in;
	}
	
	
}
