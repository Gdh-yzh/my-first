package com.wyu.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wyu.mapper.UserMapper;
import com.wyu.pojo.User;
import com.wyu.pojo.UserExample;

@Service
public class UserServiceImple implements UserService {
	//��spring��������������ע��ķ�ʽ
	@Autowired
	private UserMapper mapper;//����һ������
	//��ѯ�û�
	@Override
	public User findByUserNumber(String number) {
		// TODO Auto-generated method stub
		//����dao��������ݲ�ѯ
		
		return mapper.selectByPrimaryKey(number);//�õ����
	}
	//����û�
	@Override
	public int insertUser(User user) {
		// TODO Auto-generated method stub
		return mapper.insert(user);
	}
	//��ѯ�û�
	@Override
	public List<User> findUSers() {
		// TODO Auto-generated method stub
		//example.clear();//springĬ�ϴ����Ķ��ǵ���ģʽ
		UserExample example = new UserExample();
		List<User> list = mapper.selectByExample(example);
		list.forEach(li->System.out.println("ԭʼ����"+li));
		return list;
	}
	//ɾ���û�
	@Override
	public int deleteUserByNumber(String number) {
		// TODO Auto-generated method stub
		
		return mapper.deleteByPrimaryKey(number);
	}
	//��������
	@Override
	public int updateUserByNumber(User user) {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKey(user);
	}
	//ģ����ѯ
	@Override
	public List<User> findLikeNumbers(String number, String username, String roleId) {
		// TODO Auto-generated method stub
		return null;
	}

}
