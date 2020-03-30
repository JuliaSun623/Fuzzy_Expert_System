package com.fuzzy;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

/**
 * Servlet implementation class Evaluate
 */
@WebServlet("/HouseList")
public class HouseList extends HttpServlet {
	
	private static String readFileContents(File fileEntry) throws IOException{
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileEntry.getAbsolutePath()), "utf-8"));
		StringBuilder sb = new StringBuilder();
		String line = in.readLine();
		while(line != null) {
			sb.append(line);
			sb.append(System.lineSeparator());
			line = in.readLine();
		}
		return sb.toString();
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	

    public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException { 
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		List<Map> list =new ArrayList<Map>();
		//System.out.println(budget);
		List<Map> house_list =new ArrayList<Map>();
		File rootFolder = new File("F:������/�˹�����/ר��ϵͳPJ/fuzzy_expert/result");
		File [] files = rootFolder.listFiles();
		for(int i=0; i<100;i++) {
			int tag = i*200;
			if(files[tag].isFile()) {
				HouseData house = JSON.parseObject(readFileContents(files[tag]),HouseData.class);
				
				String status;
				if(house.room_status==2)
					status = "Refined Decoration";
				else if(house.room_status==1)
					status = "Simple Decoration";
				else
					status = "Rough house";
				String[] areas;
				areas = files[i].getName().split("_");
				
				Map map = new HashMap();
				map.put("name", house.name);
				map.put("location", house.location);
				map.put("room_area", house.room_area);
				map.put("layout", house.room_type);
				map.put("price", house.room_price);
				map.put("area", areas[0]);	//�ṩ���ļ����ֺ�������
				map.put("year", house.room_time);
				map.put("status", status);
				house_list.add(map);
			}
		}
		list = house_list;
		int page=0;
        //�õ��������ĵ�ǰҳ
        String str_page= request.getParameter("page");
        Paging paging=new Paging();
        paging.setList(list); //�����ݿ�õ����ݴ����list����
        paging.setCount(); //��������
        paging.setPagesize(20); //һ��ҳ������ݶ�����
        paging.setPagenumber(); //�ܵ�ҳ����
        paging.setEndpage(); //���һҳ
        paging.setIndexpage(1); //��һҳ
        if (str_page!=null) {
            //��ҳת�������ж����С
            int pag=Integer.parseInt(str_page);
            //�������㣬����������pagֵ������ǰҳpage
            if (pag>=0) {
                page=pag;
                //���С�����ֵʱ�򣬽��䴫������ֵ��1�ڸ�ֵ����ǰҳ������һֱ�����һҳ
                if (pag>(paging.getPagenumber()-1)) {
                    page=pag-1;
                }
            }
        }
        paging.setPage(page);
        List<Map> list_page =new ArrayList<Map>();
        //����ǰҳ��ֵ�����µ�list_page�����У�list������ȫ�������ۺϣ���i�������еļ������ݸ�list_page
        for (int i = page*20; i <(page+1)*20-1&&i<99; i++) {
            list_page.add(list.get(i));
        }
//		List<Map> list =new ArrayList<Map>();
//		list = house_list.subList(page.start, page.start+page.count);
		
		request.setAttribute("house", list_page);
		request.setAttribute("paging", paging);
		request.getRequestDispatcher("/Houselist.jsp").forward(request, response);
		
	}

}
