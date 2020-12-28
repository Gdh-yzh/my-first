package com.wyu.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyu.pojo.ReaderAll;
import com.wyu.pojo.ReaderQuery;
import com.wyu.pojo.Reader;
import com.wyu.service.ReaderService;

@Controller
public class ReaderController {
	
	@Autowired
	private ReaderService service;
	
	//1.���߹���
		@RequestMapping(value="/findReaders",produces="appliction/json;charset=utf8")
		@ResponseBody
		public String findReaders(int  pageNumber,int pageSize) {
			System.out.println("page"+pageNumber+";rows"+pageSize);
			//��1��5����ѯǰ���������ݣ���10��5���ӵ�10����ʼ��ѯ5������
			PageHelper.startPage(pageNumber,pageSize);
			List<ReaderAll> list = service.findReaders();
			list.forEach(li->System.out.println(li));
			//PageInfo info = new PageInfo<>();
			PageInfo<ReaderAll> info = new PageInfo<>(list);
			//�õ��ܼ�¼��
			long total = info.getTotal();
			//��list���Ϸ�װ��json����
			String jsonlist = JSON.toJSONString(list);
			//������ɵ�json����
			String json = "{\"total\" : "+total+",\"rows\" : "+jsonlist+"}";
			System.out.println("json="+json);
			return json;
			
		}
		// 2.����������ѯ����
		@RequestMapping(value = "/findReadersByText", produces = "appliction/json;charset=utf8")
		@ResponseBody
		public String findReadersByText(int pageNumber, int pageSize, String text) {
			System.out.println("pageNumber=" + pageNumber + ",pageSize=" + pageSize + ",text=" + text);
			System.out.println("text" + text);
			// (2,10)2Ϊҳ�룬10λÿҳ������
			PageHelper.startPage(pageNumber, pageSize);
			List<ReaderAll> list = service.findReadersByText(text);
			// list.forEach(li -> System.out.println(li));
			PageInfo<ReaderAll> info = new PageInfo<>(list);
			// �õ��ܼ�¼��
			long total = info.getTotal();
			// ��list���Ϸ�װ��json����
			String jsonlist = JSON.toJSONString(list);
			// ������ɵ�json����
			String json = "{\"total\" : " + total + ",\"rows\" : " + jsonlist + "}";
			System.out.println("json=" + json);
			System.out.println("���ݱ����ѯͼ��");
			System.out.println("info" + info);
			return json;

		}
		// 3.����
		@RequestMapping(value = "/findReadersBySort", produces = "appliction/json;charset=utf8")
		@ResponseBody
		public String findReadersBySort(int pageNumber, int pageSize, String text, String sortOrder, String sortName,
				String title) {
			System.out.println("pageNumber=" + pageNumber + ",pageSize=" + pageSize + ",text=" + text + ",title=" + title);
			System.out.println("text=" + text + ",sortName=" + sortName + ",sortOrder=" + sortOrder);
			// (2,10)2Ϊҳ�룬10λÿҳ������
			PageHelper.startPage(pageNumber, pageSize);
			List<ReaderAll> list = service.findReadersBySort(text, sortName, sortOrder);
			// list.forEach(li -> System.out.println(li));
			PageInfo<ReaderAll> info = new PageInfo<>(list);
			// �õ��ܼ�¼��
			long total = info.getTotal();
			// ��list���Ϸ�װ��json����
			String jsonlist = JSON.toJSONString(list);
			// ������ɵ�json����
			String json = "{\"total\" : " + total + ",\"rows\" : " + jsonlist + "}";
			System.out.println("json=" + json);
			System.out.println("info" + info);
			return json;

		}
		// 4.���¶�����Ϣ
		@RequestMapping(value = "/updateReader", produces = "appliction/text;charset=utf-8")
		@ResponseBody()
		public String updateReader(Reader read) throws ParseException {
			System.out.println("�޸ĵ��û���Ϣ��" + read);
			int expirationdays = read.getExpirationdays();
			String registrationdate = read.getRegistrationdate();
			System.out.println("ԭ����ע��������"+registrationdate);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date registrationdate1 = simpleDateFormat.parse(registrationdate);
			System.out.println("ע��������"+registrationdate1);
			read.setEnddate(addDate(registrationdate1,expirationdays));
			int in = service.updateReaderByNumber(read);
			String updateInfo = "0";
			if (in > 0) {
				System.out.println("�޸ĳɹ�");
				updateInfo = "1";
			}
			return updateInfo;
		}
	
		//5.
		@RequestMapping(value = "/findreaderByNumber", produces = "appliction/text;charset=utf-8")
		@ResponseBody()
		public List<ReaderAll> findreaderByNumber(String readernumber) {
			// TODO Auto-generated method stub
			List<ReaderAll> list = service.findreaderByNumber(readernumber);
			return list;
		}
		
		//6.ͳ�ƶ�������
		@RequestMapping(value = "/readerAmounts", produces = "appliction/text;charset=utf-8")
		@ResponseBody()
		public String readerAmounts() {
			// TODO Auto-generated method stub
			int in = service.readerAmounts();
			return "{\"amounts\" : " + in+"}";
		}
		
		//7.����id��ѯ������Ϣ
		@RequestMapping(value = "/findReaderByID", produces = "appliction/json;charset=utf8")
		@ResponseBody
		public String findReaderByID(String readernumber) {
			System.out.println("readernumber=" + readernumber);
			ReaderAll list = service.findReadersById(readernumber);
			// ��list���Ϸ�װ��json����
			String jsonlist = JSON.toJSONString(list);
			System.out.println(jsonlist);
			// ������ɵ�json����
			return jsonlist;
		}

		public int findReaderExist(String readernumber) {
			// TODO Auto-generated method stub
			int in = service.findReaderExist(readernumber);
			return in;
		}
		/*public int updateReaderBySelect(Reader reader) {
			int in =service.updateReaderBySelect(reader);
			return in;
		}*/
		//2.��Ӷ���
		@RequestMapping(value = "/addReader", produces = "appliction/json;charset=utf8")
		@ResponseBody
		public String addReader(ReaderAll reader) {
			int expirationdays = reader.getExpirationdays();
			Date registrationdate = new Date();
			reader.setEnddate(addDate(registrationdate,expirationdays));
			reader.setReaderstatusnumber(1);
			// ��ǰʱ��
			reader.setRegistrationdate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			int in = service.insertReader(reader);
			String Json="";
			if (in > 0) {
				List<ReaderAll> readerall = service.findReaderByNumber(reader.getReadernumber());
				Json = JSON.toJSONString(readerall);
				System.out.println("Json:" + Json);
				return Json;
			} 
			return Json;
		}
		//���ڼ�������
		public static Date addDate(Date date,long day) {
			 long time = date.getTime(); // �õ�ָ�����ڵĺ�����
			 day = day*24*60*60*1000; // Ҫ���ϵ�����ת���ɺ�����
			 time+=day; // ��ӵõ��µĺ�����
			 return new Date(time); // ��������ת��������
			}
		
		// 4.ɾ������
		@RequestMapping(value = "/deleteReader", produces = "appliction/text;charset=utf-8")
		@ResponseBody()
		public String deleteReader(String number) {
			System.out.println("Ҫɾ����number:" + number);
			// ����service���ɾ��
			// int in = service.deleteBookByNumber("123");
			int in = service.deleteReaderByNumber(number);
			// ����һ�������������ɾ�����
			// String deleteRes = "ɾ��ʧ��";
			if (in > 0) {
				System.out.println("ɾ���ɹ�");
				// deleteRes = in;
			}
			System.out.println(in);
			return in + "";
		}

		// 5.ɾ��һЩ����
		@RequestMapping(value = "/deleteReaders", produces = "appliction/json;charset=utf8")
		@ResponseBody()
		public String deleteReaders(@RequestBody String jsonNumbers) {
			System.out.println("deleteReadersҪɾ����numbers:" + jsonNumbers);
			// ��json���ַ���ת��Ϊlist
			List<String> numbers = (List<String>) JSONArray.parseArray(jsonNumbers, String.class);

			// ����service���ɾ��
			int in = service.deleteReaderByNumbers(numbers);
			// int in = 0;
			// ����һ�������������ɾ�����
			// String deleteRes = "ɾ��ʧ��";
			if (in > 0) {
				System.out.println("ɾ���ɹ�");
				// deleteRes = in;
			}
			System.out.println(in);
			return in + "";
		}
		
		// 8.��������ѯ����
		@RequestMapping(value = "/queryReaders", produces = "appliction/json;charset=utf8")
		@ResponseBody
		public String queryReaders(ReaderQuery reader) {
			System.out.println(reader.toString());
			System.out.println(reader);
			// (2,10)2Ϊҳ�룬10λÿҳ������
			int pageNumber = reader.getPageNumber();
			int pageSize = reader.getPageSize();
			com.github.pagehelper.Page<Object> page = PageHelper.startPage(pageNumber, pageSize);
			List<ReaderAll> list;
			String json = "";
			System.out.println(pageNumber + "," + pageSize);
			try {
				list = service.queryReaders(reader);
				list.forEach(li -> System.out.println(li));
				PageInfo<ReaderAll> info = new PageInfo<>(list);
				// �õ��ܼ�¼��
				long total = info.getTotal();
				// ��list���Ϸ�װ��json����
				String jsonlist = JSON.toJSONString(list);
				json = "{\"total\" : " + total + ",\"rows\" : " + jsonlist + "}";
				System.out.println("������������԰�,total" + total);
				System.out.println("������������԰�,info" + info);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("page:" + page);
			// ������ɵ�json����
			System.out.println("��������ѯ");
			System.out.println("json=" + json);
			return json;
		}

		@RequestMapping(value = "/findReadersQuery", produces = "appliction/json;charset=utf8")
		@ResponseBody
		public String findReadersQuery(int pageNumber, int pageSize, String readerString) {
			System.out.println("�ַ����õ�������"+readerString+"ҳ��"+pageNumber+"��С"+pageSize);
			try {
				readerString = URLDecoder.decode(readerString, "utf-8");
			} catch (UnsupportedEncodingException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			HashMap<String, String> readerMap = form2Map(readerString);
			ReaderQuery reader = JSON.parseObject(JSON.toJSONString(readerMap), ReaderQuery.class);
			Reader readers = JSON.parseObject(JSON.toJSONString(readerMap), Reader.class);
			System.out.println(reader);
			System.out.println(readers);
			// System.out.println("pageNumber=" + pageNumber + ",pageSize=" + pageSize +
			// ",text=" + text + ",title=" + title);
			// (2,10)2Ϊҳ�룬10λÿҳ������
			PageHelper.startPage(pageNumber, pageSize);
			List<ReaderAll> list;
			String json = "";
			try {
				list = service.queryReaders(reader);
				list.forEach(li -> System.out.println(li));
				PageInfo<ReaderAll> info = new PageInfo<>(list);
				// �õ��ܼ�¼��
				long total = info.getTotal();
				// ��list���Ϸ�װ��json����
				String jsonlist = JSON.toJSONString(list);
				json = "{\"total\" : " + total + ",\"rows\" : " + jsonlist + "}";
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// ������ɵ�json����
			return json;
		}

		// 9.����
		@RequestMapping(value = "/findReadersQueryBySort", produces = "appliction/json;charset=utf8")
		@ResponseBody
		public String findReadersQueryBySort(int pageNumber, int pageSize, String readerString, String sortOrder,
				String sortName) {
			try {
				readerString = URLDecoder.decode(readerString, "utf-8");
			} catch (UnsupportedEncodingException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			HashMap<String, String> readerMap = form2Map(readerString);
			ReaderQuery reader = JSON.parseObject(JSON.toJSONString(readerMap), ReaderQuery.class);
			Reader readers = JSON.parseObject(JSON.toJSONString(readerMap), Reader.class);
			System.out.println(reader);
			System.out.println(readers);
			// System.out.println("pageNumber=" + pageNumber + ",pageSize=" + pageSize +
			// ",text=" + text + ",title=" + title);
			// (2,10)2Ϊҳ�룬10λÿҳ������
			PageHelper.startPage(pageNumber, pageSize);
			List<ReaderAll> list;
			String json = "";
			try {
				list = service.queryReadersBySort(reader, sortOrder, sortName);
				list.forEach(li -> System.out.println(li));
				PageInfo<ReaderAll> info = new PageInfo<>(list);
				// �õ��ܼ�¼��
				long total = info.getTotal();
				// ��list���Ϸ�װ��json����
				String jsonlist = JSON.toJSONString(list);
				json = "{\"total\" : " + total + ",\"rows\" : " + jsonlist + "}";
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// ������ɵ�json����
			return json;
		}

		// ��jsonת��Ϊmap
		public static HashMap<String, String> form2Map(String orderinfo) {
			System.out.println("orderinfo:" + orderinfo);

			System.out.println(orderinfo);
			String listinfo[];
			HashMap<String, String> map = new HashMap<String, String>();
			listinfo = orderinfo.split("&");
			for (String s : listinfo) {
				String list[] = s.split("=");
				System.out.println("list[0]:" + list);
				if (list.length > 1) {

					map.put(list[0], list[1]);
					System.out.println("list[0], list[1]=" + list[0] + ":" + list[1]);

				} else if (list.length == 1) {
					map.put(list[0], "");
				}
			}
			return map;
		}
		
		//���ߵ�¼
		@RequestMapping("/loginReader")
		public String login(String number,String password,Model model,HttpSession session) {
			System.out.println("number==" + number + ",password==" + password);
			Reader reader = service.findByReaderNumber(number);
			System.out.println("��ѯ���������ǣ�"+ reader);
			String loginInfo = "û������û�";
			if(reader != null) {
				if(reader.getReaderstatusnumber() == 2)
				{
					loginInfo = "���û��ѱ�����";
				}
				else {
					if(password.equals(reader.getPassword())) {
						System.out.println("��¼�ɹ�");
						session.setAttribute("reader", reader);
						return "lms_bs_reader";
					}else {
						loginInfo = "�������";
						System.out.println("�������");
					}
				}
			}
			model.addAttribute("loginfo",loginInfo);
			return "login_reader";
		}		
}


