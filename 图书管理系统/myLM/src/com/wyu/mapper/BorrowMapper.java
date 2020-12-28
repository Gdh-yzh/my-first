package com.wyu.mapper;

import com.wyu.pojo.Borrow;
import com.wyu.pojo.BorrowAll;
import com.wyu.pojo.BorrowExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BorrowMapper {
	int countByExample(BorrowExample example);

	int deleteByExample(BorrowExample example);

	int deleteByPrimaryKey(String borrownumber);

	int insert(Borrow record);

	int insertSelective(Borrow record);

	List<Borrow> selectByExample(BorrowExample example);

	// ���ϲ�ѯ
	List<BorrowAll> selectByExampleBorrowAll(BorrowExample example);

//	 ͳ�������ı��� 
	BorrowAll countMaxBookBorrow();

	// ͳ�ƽ��ĵĶ�������

	BorrowAll countReaderAmountBorrow();

	Borrow selectByPrimaryKey(String borrownumber);

	int updateByExampleSelective(@Param("record") Borrow record, @Param("example") BorrowExample example);

	int updateByExample(@Param("record") Borrow record, @Param("example") BorrowExample example);

	int updateByPrimaryKeySelective(Borrow record);

	int updateByPrimaryKey(Borrow record);
}