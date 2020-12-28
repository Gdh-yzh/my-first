package com.wyu.mapper;

import com.wyu.pojo.Book;
import com.wyu.pojo.BookAll;
import com.wyu.pojo.BookExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BookMapper {
    int countByExample(BookExample example);

    int deleteByExample(BookExample example);

    int deleteByPrimaryKey(String booknumber);

    int insert(Book record);

    int insertSelective(Book record);

    List<Book> selectByExample(BookExample example);

  //����ѯ
    List<BookAll> selectBybookExample(BookExample example);
      
    Book selectByPrimaryKey(String booknumber);
    

    int updateByExampleSelective(@Param("record") Book record, @Param("example") BookExample example);

    int updateByExample(@Param("record") Book record, @Param("example") BookExample example);

    int updateByPrimaryKeySelective(Book record);

    int updateByPrimaryKey(Book record);
    int insertMany(List<Book> list);
}