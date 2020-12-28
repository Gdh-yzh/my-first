package com.wyu.controller;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.wyu.pojo.User;
import com.wyu.service.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService service;
	//1����¼����
	@RequestMapping("/login")
	public String login(String number,String password,Model model,HttpSession session) {
		System.out.println("number==" + number + ",password==" + password);
		User user = service.findByUserNumber(number);
		System.out.println("��ѯ���������ǣ�"+ user);
		String loginInfo = "û������û�";
		if(user != null) {
			if(user.getUserstatusnumber() == 2)
			{
				loginInfo = "���û��ѱ�����";
			}
			else {
				if(password.equals(user.getPassword())) {
					System.out.println("��¼�ɹ�");
					session.setAttribute("user", user);
					return "lms_bs";
				}else {
					loginInfo = "�������";
					System.out.println("�������");
				}
			}
		}
		model.addAttribute("loginfo",loginInfo);
		return "login_user";
	}		
	/*
	//2.ע�Ṧ��
	@RequestMapping("/regUser")
	@ResponseBody
	public String regUser(User user) {
		System.out.println("�õ���"+user);
		user.Registrationdate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		//����service�����ӹ���
		int in = service.insertUser(user);
		if(in>0) {
			System.out.println("��ӳɹ�");
		}else {
			in=0;
		}
		//System.out.println("in"+in);
		return in+"";
		
	}
	
		//3.�û�����
		@RequestMapping(value="/findUsers",produces="appliction/json;charset=utf8")
		@ResponseBody
		public String findUsers(int  page,int rows) {
			System.out.println("page"+page+";rows"+rows);
			//��1��5����ѯǰ���������ݣ���10��5���ӵ�10����ʼ��ѯ5������
			PageHelper.startPage(page,rows);
			List<User> list = service.findUSers();
			list.forEach(li->System.out.println(li));
			//PageInfo info = new PageInfo<>();
			PageInfo<User> info = new PageInfo<>(list);
			//�õ��ܼ�¼��
			long total = info.getTotal();
			//��list���Ϸ�װ��json����
			String jsonlist = JSON.toJSONString(list);
			//������ɵ�json����
			String json = "{\"total\" : "+total+",\"rows\" : "+jsonlist+"}";
			System.out.println("json="+json);
			return json;
			
		}


		//4.ɾ���û�
		@RequestMapping(value="/deleteUser",produces="appliction/text;charset=utf-8")
		@ResponseBody()
		public String deleteUser(String number) {
			System.out.println("Ҫɾ����number��"+number);
			//����service���ɾ��
			int in = service.deleteUserByNumber(number);
			//����һ�������������ɾ�����
			String deleteRes = "ɾ��ʧ��";
			if(in > 0) {
				System.out.println("ɾ���ɹ�");
				deleteRes = "ɾ���ɹ�";
			}
			return deleteRes;
		}
		
		//5.�����û���Ϣ
		@RequestMapping(value="/updateUser",produces="appliction/text;charset=utf-8")
		@ResponseBody()
		public String updateUser(User user) {
			System.out.println("�޸ĵ��û���Ϣ��"+user);
			int in = service.updateUserByNumber(user);
			String updateInfo = "0";
			if(in > 0) {
				System.out.println("�޸ĳɹ�");
				updateInfo = "1";
			}
			return updateInfo;
		}
		
		//6.ģ����ѯ
		@RequestMapping(value="/findLikeNumbers",produces="appliction/json;charset=utf8")
		@ResponseBody()
		public String findLikeNumbers(int  page,int rows,String number,String username,String roleId) {
			System.out.println("page"+page+";rows"+rows);
			//number = "%"+number+"%";
			//��1��5����ѯǰ���������ݣ���10��5���ӵ�10����ʼ��ѯ5������
			PageHelper.startPage(page,rows);
			List<User> list = service.findLikeNumbers(number,username,roleId);
			list.forEach(li->System.out.println(li));
			//PageInfo info = new PageInfo<>();
			PageInfo<User> info = new PageInfo<>(list);
			//�õ��ܼ�¼��
			long total = info.getTotal();
			//��list���Ϸ�װ��json����
			String jsonlist = JSON.toJSONString(list);
			//������ɵ�json����
			String json = "{\"total\" : "+total+",\"rows\" : "+jsonlist+"}";
			System.out.println("json="+json);
			return json;
			
		}
		
		//7.��֤
		@RequestMapping("/isExistNumber")
		@ResponseBody()
		public String isExistNumber(String number) {
			System.out.println("number==" + number );
			User user = service.findByNumber(number);
			System.out.println("user---->"+user);
			String loginfo="0";
			if(user !=null) {
				loginfo="1";
			}
			return loginfo;
		}*/				
}
