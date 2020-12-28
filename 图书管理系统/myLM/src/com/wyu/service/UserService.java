package com.wyu.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.wyu.pojo.User;

@Service
public interface UserService {
	User findByUserNumber(String number);
	//���
	int insertUser(User user);
	//��ѯ�����û�
	List<User> findUSers();
	int deleteUserByNumber(String number);
	int updateUserByNumber(User user);	
	//ģ����ѯ
	List<User> findLikeNumbers(String number,String username,String roleId);
}
