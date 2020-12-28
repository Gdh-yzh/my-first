package com.wyu.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyu.pojo.Book;
import com.wyu.pojo.BookAll;
import com.wyu.pojo.Borrow;
import com.wyu.pojo.BorrowAll;
import com.wyu.pojo.Reader;
import com.wyu.pojo.ReaderAll;
import com.wyu.service.BorrowService;

@Controller
public class BorrowController {
	@Autowired
	private BorrowService service;
	@Autowired
	private BookController bookController;
	@Autowired
	private ReaderController readerController;
	
	//ͼ�����
	@RequestMapping(value = "/borrowQuery", produces = "appliction/json;charset=utf8")
	@ResponseBody
	@Transactional
	public String borrowQuery(String booknumber, String readernumber) {
		String tag = "";
		BookAll bookall;
		String json = "{\"tag\" : "+tag+"}";
		List<BookAll> bookList = bookController.findBookByNumber(booknumber);
		List<ReaderAll> readerList = readerController.findreaderByNumber(readernumber);
		if (bookList.isEmpty()) {
			tag="ͼ�����������ͼ�鲻����";
			json = "{\"tag\" : \""+tag+"\"}";
			return json;
		}
		if (readerList.isEmpty()) {
			tag="���߱��������߲�����";
			json = "{\"tag\" : \""+tag+"\"}";
			return json;
		}

		bookall = bookList.get(0);
		String statusBook = bookall.getStatusBook().getBookstatus();
		String typecirculationBook = bookall.getTypecirculationBook().getTypecirculationname();
		if (!typecirculationBook.equals("���")) {
			tag="ͼ�鲻�����";
			json = "{\"tag\" : \""+tag+"\"}";
			return json;
		} 
		if (!statusBook.equals("�ڼ���")) {
			tag="ͼ�鲻�ڼ��ϣ��޷����";
			json = "{\"tag\" : \""+tag+"\"}";
			return json;
		}
		ReaderAll readerall;
		readerall = readerList.get(0);
		String statusReader = readerall.getStatusReader().getStatusname();
		int borrowamount = readerall.getTypeReader().getBorrowamount();
		
		
		//�ж�ͼ���Ƿ�����
		int borrowedamount = service.countByReaderNumber(readernumber);
		if(statusReader.equals("����")) {
			tag="���߱�����,�޷���ȡͼ��";
			json = "{\"tag\" : \""+tag+"\"}";
			return json;
		}
		
		//��ѯ���߽��ı���δ�黹
		int overdue = service.countOverdueByReaderNumber(readernumber);
		if(overdue>0) {
			tag="���ߴ���ͼ������,����ȥ�黹ͼ��";
			json = "{\"tag\" : \""+tag+"\"}";
			return json;
		}
		if(borrowamount<=borrowedamount) {
			tag="���߽���ͼ���Ѵﵽ"+borrowamount+"��,�޷���ȡͼ��";
			json = "{\"tag\" : \""+tag+"\"}";
			return json;
		}
		
		Borrow borrow = new Borrow();
		Long timeStamp = System.currentTimeMillis();
		borrow.setBorrownumber(timeStamp.toString());
		borrow.setBooknumber(booknumber);
		borrow.setReadernumber(readernumber);
		
		//���Ĵ���
		int borrowCount = service.borrowCount(booknumber);
		borrow.setBorrowingstimes(borrowCount+1);
		Date borrowdate = new Date();
		borrow.setBorrowdate(borrowdate);
		//��ȡ���ߵ����͵�����
		int days = readerall.getTypeReader().getBorrowdays();
		Date returnDate = addDate(borrowdate, days);
		borrow.setReturndate(returnDate);
		//�ѽ��
		borrow.setBorrowstatusnumber(1);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date enddate;
		try {
			enddate = simpleDateFormat.parse("0000-00-00");
			borrow.setEnddate(enddate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//��ӽ�����Ϣ
		int in = service.addBorrowByNumbers(borrow);
		if(in>0) {
			//����ͼ�����Ϣ
			int bookin=bookController.updateBookStatus(booknumber,3);
			if(bookin>0) {
				System.out.println("ͼ��״̬�ı�ɹ�");
			}else {
				System.out.println("�����Ա�ֶ��Ķ�ͼ��״̬Ϊ�ѽ��,���Ϊ3");
			}
			
			List<BookAll> newbookList = bookController.findBookByNumber(booknumber);
			String bookLists = JSON.toJSONString(newbookList);
			String readerLists = JSON.toJSONString(readerList);
			tag="���ĳɹ�";
			json = "{\"tag\" : \"" +tag+ "\",\"book\" : " + bookLists + ",\"reader\" : " + readerLists +"}";
			return json;
			
		}
		tag="����ʧ��";
		json = "{\"tag\" : \""+tag+"\"}";
		return json;
	}
	
	

	
	//���ڼ�������
	public static Date addDate(Date date,long day) {
		 long time = date.getTime(); // �õ�ָ�����ڵĺ�����
		 day = day*24*60*60*1000; // Ҫ���ϵ�����ת���ɺ�����
		 time+=day; // ��ӵõ��µĺ�����
		 return new Date(time); // ��������ת��������
		}
	
	
	//2.��ѯͼ���н��ļ�¼
	@RequestMapping(value = "/returnBorrowBybooknumber", produces = "appliction/json;charset=utf8")
	@ResponseBody
	public String returnBorrowBybooknumber(String booknumber) {
		//��ѯ���ļ�¼���Ƿ����ͼ��
		//String json = "{\"tag\" : "+tag+"}";
		int inbook = bookController.findBookExist(booknumber);
		String tag="";
		String json = "{\"tag\" : \""+tag+"\"}";
		if(inbook==0) {
			tag="ͼ�鲻����";
			json = "{\"tag\" : \""+tag+"\"}";
			return json;
		}
		List<Borrow> borrowList = service.findBorrowBybookNumberNoReturn(booknumber);
		
		if(borrowList.isEmpty()) {
			tag="�޽��ļ�¼";
			System.out.println("borrowListΪ��,�޽��ļ�¼");
			System.out.println("borrowList:"+borrowList.toString());
			json = "{\"tag\" : \""+tag+"\"}";
			return json;
		}
		List<BookAll> bookAll;
		List<ReaderAll> readerAll;
		bookAll = bookController.findBookByNumber(booknumber);
		readerAll = readerController.findreaderByNumber(borrowList.get(0).getReadernumber());
		String book = JSON.toJSONString(bookAll);
		String reader = JSON.toJSONString(readerAll);
		String borrow = JSON.toJSONString(borrowList);
		tag="�н��ļ�¼";
		json = "{\"tag\" : \"" +tag+ "\",\"book\" : " + book + ",\"reader\" : " + reader +",\"borrow\" : " + borrow +"}";
		return json;
	}
	
		@RequestMapping(value = "/returnBorrowByreadernumber", produces = "appliction/json;charset=utf8")
		@ResponseBody
		public String returnBorrowByreadernumber(String readernumber) {
			//��ѯ���ļ�¼���Ƿ����ͼ��
			//String json = "{\"tag\" : "+tag+"}";
			int inreader = readerController.findReaderExist(readernumber);
			String tag="";
			String json = "{\"tag\" : \""+tag+"\"}";
			if(inreader==0) {
				tag="ͼ�鲻����";
				json = "{\"tag\" : \""+tag+"\"}";
				return json;
			}
			List<Borrow> borrowList = service.findBorrowByreaderNumberNoReturn(readernumber);
			
			if(borrowList.isEmpty()) {
				tag="�޽��ļ�¼";
				System.out.println("borrowListΪ��,�޽��ļ�¼");
				System.out.println("borrowList:"+borrowList.toString());
				json = "{\"tag\" : \""+tag+"\"}";
				return json;
			}
			List<BookAll> bookAll;
			List<ReaderAll> readerAll;
			bookAll = bookController.findBookByNumber(borrowList.get(0).getBooknumber());
			readerAll = readerController.findreaderByNumber(readernumber);
			String book = JSON.toJSONString(bookAll);
			String reader = JSON.toJSONString(readerAll);
			String borrow = JSON.toJSONString(borrowList);
			tag="�н��ļ�¼";
			json = "{\"tag\" : \"" +tag+ "\",\"book\" : " + book + ",\"reader\" : " + reader +",\"borrow\" : " + borrow +"}";
			return json;
		}
	
	//3.�黹ͼ��
	
	@RequestMapping(value = "/returnBook", produces = "appliction/json;charset=utf8")
	@ResponseBody
	@Transactional
	public String returnBook(String borrownumber) {
		Borrow borrow = service.findBorrowByNumber(borrownumber);
		Borrow borrow2 = new Borrow();
		Date date = new Date();
		borrow2.setEnddate(date);
		borrow2.setBorrowstatusnumber(3);
		borrow2.setBorrownumber(borrownumber);
		Book book = new Book();
		book.setBooknumber(borrow.getBooknumber());
		book.setBookstatusnumber(2);
		Reader reader = new Reader();
		reader.setReadernumber(borrow.getReadernumber());
		int inbook = bookController.updateBookBySelect(book);
		//int inreader = readerController.updateReaderBySelect(reader);
		int inborrow = service.upDateBorrowBySelect(borrow2);
		
		return inbook+inborrow+"";
	}
	
	// 5.ɾ��һЩ������Ϣ
			@RequestMapping(value = "/deleteBorrows", produces = "appliction/json;charset=utf8")
			@ResponseBody()
			public String deleteBorrows(@RequestBody String jsonNumbers) {
				System.out.println("deleteBorrowsҪɾ����numbers:" + jsonNumbers);
				// ��json���ַ���ת��Ϊlist
				List<String> numbers = (List<String>) JSONArray.parseArray(jsonNumbers, String.class);

				// ����service���ɾ��
				int in = service.deleteBorrowByNumbers(numbers);
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

			// 6.����������ѯ������Ϣ
			@RequestMapping(value = "/findBorrowsByText", produces = "appliction/json;charset=utf8")
			@ResponseBody
			public String findBorrowsByText(int pageNumber, int pageSize, String text) {
				System.out.println("pageNumber=" + pageNumber + ",pageSize=" + pageSize + ",text=" + text);
				System.out.println("text" + text);
				// (2,10)2Ϊҳ�룬10λÿҳ������
				PageHelper.startPage(pageNumber, pageSize);
				List<BorrowAll> list = service.findBorrowsByText(text);
				// list.forEach(li -> System.out.println(li));
				PageInfo<BorrowAll> info = new PageInfo<>(list);
				// �õ��ܼ�¼��
				long total = info.getTotal();
				// ��list���Ϸ�װ��json����
				String jsonlist = JSON.toJSONString(list);
				// ������ɵ�json����
				String json = "{\"total\" : " + total + ",\"rows\" : " + jsonlist + "}";
				System.out.println("json=" + json);
				System.out.println("���ݱ����ѯ������Ϣ");
				System.out.println("info" + info);
				return json;

			}
		
	
	//4.��ѯ������Ϣ findBorrows
	
	
	@RequestMapping(value="/findBorrowsBySort",produces="appliction/json;charset=utf8")
	@ResponseBody
	public String findBorrowsBySort(int pageNumber, int pageSize, String text, String sortOrder, String sortName,
			String title) {
		System.out.println("pageNumber=" + pageNumber + ",pageSize=" + pageSize + ",text=" + text + ",title=" + title);
		System.out.println("text=" + text + ",sortName=" + sortName + ",sortOrder=" + sortOrder);
		// (2,10)2Ϊҳ�룬10λÿҳ������
		PageHelper.startPage(pageNumber, pageSize);
		List<BorrowAll> list = service.findBorrowsBySort(text, sortName, sortOrder);
		// list.forEach(li -> System.out.println(li));
		PageInfo<BorrowAll> info = new PageInfo<>(list);
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
	
	//5.ͳ�ƽ�������
	
	
			@RequestMapping(value = "/borrowAmounts", produces = "appliction/text;charset=utf-8")
			@ResponseBody()
			public String borrowAmounts() {
				// TODO Auto-generated method stub
				
			//	ͼ���������:	17��
			//	ͼ��δ�黹����:	
			//	����ͼ����߽��Ĵ���:	
			//	���߽��ĸ���:
				int inBorrow = service.borrowAmounts();
				int inNoReturn = service.noReturnAmounts();
				int inTopBorrow = service.topBorrowAmounts();
				int inReaderBorrow = service.readerBorrowAmounts();
				
				return "{\"borrow\" : " + inBorrow+",\"noReturn\" : " + inNoReturn + ",\"topBorrow\" : " + inTopBorrow + ",\"readerBorrow\" : " + inReaderBorrow + "}";
			}
			// 6.ɾ��������Ϣ
			@RequestMapping(value = "/deleteBorrow", produces = "appliction/text;charset=utf-8")
			@ResponseBody()
			public String deleteBoorrow(String number) {
				System.out.println("Ҫɾ����number:" + number);
				// ����service���ɾ��
				// int in = service.deleteBookByNumber("123");
				int in = service.deleteBorrowByNumber(number);
				// ����һ�������������ɾ�����
				// String deleteRes = "ɾ��ʧ��";
				if (in > 0) {
					System.out.println("ɾ���ɹ�");
					// deleteRes = in;
				}
				System.out.println(in);
				return in + "";
			}

}
