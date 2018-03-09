package com.project.sns.addr.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.cxf.helpers.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.project.sns.addr.service.AddrService;
import com.project.sns.addr.vo.AddrVO;
import com.project.sns.board.vo.BoardVO;

import A.algorithm.AES;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Controller
public class AddrController {
	private final Logger logger = LoggerFactory.getLogger(AddrController.class);

	@Autowired
	private AddrService service;

	public static int[][] W;
	public static int[][] dp;
	public static int N;
	public static final int INF = 1000000000;
	private static Deque<Integer> route = new ArrayDeque<>();
	private static List<Integer> solution;
	private static List<Integer> path;
	static int re = INF;
	
	//DB 저장
	@RequestMapping("/inputAddr.do")
	public String inputAddr(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");

		String addr = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?ServiceKey=";
		String serviceKey = "429e9l%2BRPBvvMYSqI0TIu0JgvFl1vio2dcUfXj7d66%2F%2B2glco1EDs1HDHJBssw9U7HAt1A11Cy6N0Hbk2INDfQ%3D%3D";
		String parameter = "";
		// serviceKey = URLEncoder.encode(serviceKey,"utf-8");

		PrintWriter out = response.getWriter();
		// PrintWriter out = new PrintWriter(new OutputStream
		// Writer(response.getOutputStream(),"KSC5601"));
		// ServletOutputStream out = response.getOutputStream();p
		parameter = parameter + "&" + "areaCode=1";
		parameter = parameter + "&" + "numOfRows=4000";
		parameter = parameter + "&" + "MobileOS=ETC";
		parameter = parameter + "&" + "MobileApp=aa";
		parameter = parameter + "&" + "_type=json";

		addr = addr + serviceKey + parameter;
		URL url = new URL(addr);


		// BufferedReader in = new BufferedReader(new
		// InputStreamReader(url.openStream(), "UTF-8"));

		InputStream in = url.openStream();
		// CachedOutputStream bos = new CachedOutputStream();
		ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
		IOUtils.copy(in, bos1);
		in.close();
		bos1.close();

		String mbos = bos1.toString("UTF-8");

		byte[] b = mbos.getBytes("UTF-8");
		String s = new String(b, "UTF-8");
		out.println(s);

		JSONObject json = new JSONObject();
		json.put("data", s);
		// json.put("data", data);

		JSONObject jso = json.getJSONObject("data");
		JSONObject js = jso.getJSONObject("response");
		JSONObject jj = js.getJSONObject("body");
		JSONObject items = jj.getJSONObject("items");
		JSONArray jArray = items.getJSONArray("item");

		List<AddrVO> list = new ArrayList<AddrVO>();
		for (int i = 0; i < 3361; i++) {
			JSONObject a = jArray.getJSONObject(i);


			AddrVO vo = new AddrVO();

			// TourMap DB
			if (a.has("contenttypeid") && a.has("contentid") && a.has("title") && a.has("tel") && a.has("addr1")
					&& a.has("firstimage") && a.has("firstimage2") && a.has("cat2") && a.has("cat3") && a.has("mapx")
					&& a.has("mapy")) {
				vo.setContentTypeId(a.getString("contenttypeid"));
				vo.setContentId(a.getString("contentid"));
				vo.setTitle(a.getString("title"));
				vo.setTel(a.getString("tel"));
				vo.setAddr1(a.getString("addr1"));
				vo.setFirstimage(a.getString("firstimage"));
				vo.setFirstimage2(a.getString("firstimage2"));
				vo.setCat2(a.getString("cat2"));
				vo.setCat3(a.getString("cat3"));
				vo.setMapx(a.getString("mapx"));
				vo.setMapy(a.getString("mapy"));
				list.add(vo);
			}
		}

		int i = service.inputAddr(list);
		return "test";
	}

	//DB 저장(축제 update)
	@RequestMapping("/updateFest.do")
	public String updateFest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");

		String addr = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?ServiceKey=";
		String serviceKey = "429e9l%2BRPBvvMYSqI0TIu0JgvFl1vio2dcUfXj7d66%2F%2B2glco1EDs1HDHJBssw9U7HAt1A11Cy6N0Hbk2INDfQ%3D%3D";
		String parameter = "";
		// serviceKey = URLEncoder.encode(serviceKey,"utf-8");

		PrintWriter out = response.getWriter();
		// PrintWriter out = new PrintWriter(new OutputStream
		// Writer(response.getOutputStream(),"KSC5601"));
		// ServletOutputStream out = response.getOutputStream();
		parameter = parameter + "&" + "areaCode=1";
		parameter = parameter + "&" + "contentTypeId=15";
		parameter = parameter + "&" + "numOfRows=4000";
		parameter = parameter + "&" + "MobileOS=ETC";
		parameter = parameter + "&" + "MobileApp=aa";
		parameter = parameter + "&" + "_type=json";

		addr = addr + serviceKey + parameter;
		URL url = new URL(addr);


		// BufferedReader in = new BufferedReader(new
		// InputStreamReader(url.openStream(), "UTF-8"));

		InputStream in = url.openStream();
		// CachedOutputStream bos = new CachedOutputStream();
		ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
		IOUtils.copy(in, bos1);
		in.close();
		bos1.close();

		String mbos = bos1.toString("UTF-8");

		byte[] b = mbos.getBytes("UTF-8");
		String s = new String(b, "UTF-8");
		out.println(s);

		JSONObject json = new JSONObject();
		json.put("data", s);
		// json.put("data", data);

		JSONObject jso = json.getJSONObject("data");
		JSONObject js = jso.getJSONObject("response");
		JSONObject jj = js.getJSONObject("body");
		JSONObject items = jj.getJSONObject("items");
		JSONArray jArray = items.getJSONArray("item");

		List<AddrVO> list = new ArrayList<AddrVO>();
		for (int i = 0; i < 377; i++) {
			JSONObject a = jArray.getJSONObject(i);

			AddrVO vo = new AddrVO();

			// TourMap DB
			if (a.has("contenttypeid") && a.has("contentid") && a.has("title") && a.has("tel") && a.has("addr1")
					&& a.has("firstimage") && a.has("firstimage2") && a.has("cat2") && a.has("cat3") && a.has("mapx")
					&& a.has("mapy")) {
				vo.setContentTypeId(a.getString("contenttypeid"));
				vo.setContentId(a.getString("contentid"));
				vo.setTitle(a.getString("title"));
				vo.setTel(a.getString("tel"));
				vo.setAddr1(a.getString("addr1"));
				vo.setFirstimage(a.getString("firstimage"));
				vo.setFirstimage2(a.getString("firstimage2"));
				vo.setCat2(a.getString("cat2"));
				vo.setCat3(a.getString("cat3"));
				vo.setMapx(a.getString("mapx"));
				vo.setMapy(a.getString("mapy"));
				list.add(vo);
			}
		}

		int i = service.inputAddr(list);
		return "test";
	}

	//자세한 정보 표시
	@RequestMapping("/callDetail.do")
	public void callDetail(HttpServletRequest request, HttpServletResponse response, @RequestParam String contentId,
			@RequestParam String contentTypeId) throws Exception {
		logger.info("callDetail");
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");

		String addr = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailIntro?ServiceKey=";
		String serviceKey = "429e9l%2BRPBvvMYSqI0TIu0JgvFl1vio2dcUfXj7d66%2F%2B2glco1EDs1HDHJBssw9U7HAt1A11Cy6N0Hbk2INDfQ%3D%3D";
		String parameter = "";

		PrintWriter out = response.getWriter();

		parameter = parameter + "&" + "contentId=" + contentId;
		parameter = parameter + "&" + "contentTypeId=" + contentTypeId;
		parameter = parameter + "&" + "MobileOS=ETC";
		parameter = parameter + "&" + "MobileApp=aa";
		parameter = parameter + "&" + "_type=json";

		addr = addr + serviceKey + parameter;
		URL url = new URL(addr);

		InputStream in = url.openStream();

		ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
		IOUtils.copy(in, bos1);
		in.close();
		bos1.close();

		String mbos = bos1.toString("UTF-8");

		byte[] b = mbos.getBytes("UTF-8");
		String s = new String(b, "UTF-8");
		out.println(s);

		JSONObject json = new JSONObject();
		json.put("data", s);
		
	}
	
	@ResponseBody
	@RequestMapping("/callInfo")
	public AddrVO callInfo(@RequestParam String contentId) throws Exception{
		AddrVO vo = service.callInfo(contentId);
		double scope = 0;
		double star = service.getStarAvg(contentId);
		if(vo.getScope()!=null) {
			scope = Double.parseDouble(vo.getScope());
			if(scope != 0) {
				if(service.getStarAvg(contentId)>0) {
					if(star!=0) {
						scope = (scope + star)/2;
					}
				}
			}
		}
		String scopeStr = Double.toString(scope);
		String starStr = Double.toString(star);
		
		if(vo.getContentTypeId().equals("39")) {
			AddrVO vo2 = service.callReview(contentId);
			if(vo2!=null) {
					vo.setLink1(vo2.getLink1());
					vo.setLink2(vo2.getLink2());
					vo.setLink3(vo2.getLink3());
					vo.setImage1(vo2.getImage1());
					vo.setImage2(vo2.getImage2());
					vo.setImage3(vo2.getImage3());
					vo.setScope(scopeStr);
		} else {
			vo.setScope(scopeStr);
			vo2 = null;
		}
		} else {
			vo.setStar(starStr);
		}
		return vo;
	}


	  @RequestMapping("/insertPath.do")
	    public void insertPath(HttpSession session, BoardVO vo) throws Exception {

	    	String id = (String) session.getAttribute("id");
	    	id = AES.setDecrypting(id);
	    	
		int storySeq = service.getStoryseq(id);
		
	    vo.setWriter(id);
	    	vo.setStory_seq(storySeq);
	    	service.insertPath(vo);
	    
	    }
	  
		 @RequestMapping("/newMap")
		 public String path2(HttpServletRequest request, HttpSession session)throws Exception{
		    List<AddrVO> list = service.getAddress();
		    request.setAttribute("list", list);   
		    
		    if(session.getAttribute("id") != null) {				//로그인이 되어있을 때만 pathCount를(경로 묶음) 불러온다.
		    	
		    	String id = (String) session.getAttribute("id");
		    	id = AES.setDecrypting(id);

		    request.setAttribute("id", id);
		    }
		    
		    
			//날씨 API
			   String[] pty = new String[3];
		       String[] wfKor = new String[3];
		       String[] hour1 = new String[3];
		        int weather =0 ;
		        try {
		              DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();				//문서 읽기 위한 factory
		              DocumentBuilder builder = factory.newDocumentBuilder();								//빌더 생성
		            
		              Document xmlDoc = null;
		              String url = "http://www.weather.go.kr/wid/queryDFSRSS.jsp?zone=1159068000";
		              xmlDoc = builder.parse(url);															//생성된 빌더를 통해 XML문서를 Document 객체로 파싱해서 가져온다	            
		              Element root = xmlDoc.getDocumentElement();
		              // System.out.println(root.getTagName());
		               
		              for (int i = 0; i < pty.length; i++) {
		               Node xml = root.getElementsByTagName("pubDate").item(0);     
		               Node xmlNode1 = root.getElementsByTagName("data").item(i);
		               
		               Node xmlNode21 = ((Element) xmlNode1).getElementsByTagName(
		                 "pty").item(0);
		               Node xmlNode22 = ((Element) xmlNode1).getElementsByTagName(
		                 "wfKor").item(0);
		               Node xmlNode23 = ((Element) xmlNode1).getElementsByTagName(
		                 "hour").item(0);
		                
		               pty[i] = xmlNode21.getTextContent();
		               wfKor[i] = xmlNode22.getTextContent();
		               hour1[i] = "기준시각 : " + xmlNode23.getTextContent() + "시";
		               
		               System.out.println("기준 시간"+xml.getTextContent());
		               System.out.println("기온 : 0 => 맑음  1,2,3 => 눈/비           현상태 =>"+pty[i] + "    날씨: " + wfKor[i] + "    시간: " +hour1[i]);
		               
		               if(pty[i].equals("0"))
		                  weather = 1;
		               else 
		                  weather = 2;
		              }	            
		             } catch (Exception e) {

		             }	        
		        
		      request.setAttribute("weather", weather);
		    return "path2";
		 }
	
	//Dijkstra - 코스추천 길찾기
	@RequestMapping("/getPath.do")
	public @ResponseBody Map<String, Object> getPath(@RequestParam String sigungucode, @RequestParam String startTime, String weather) throws Exception {
		
		
	       List<AddrVO> list = null;
	       
	       if(weather.equals("1"))
	       {	
	    	   list = service.getAddrWithCode(sigungucode);
	       }
	       else 
	       {
	    	   list = service.getAddrByWeather(sigungucode, "1");	//날씨가 안 좋을 때에는 실내 데이터만 가져온다.
	       }

		List<BoardVO> listHeart = service.getHeart();
		List<BoardVO> listStar = service.getStarAvgList();
	       
		double distanceMeter = 0;
		//그래프 저장용 맵
        //HashMap<출발지, HashMap<도착지, 거리>>
        HashMap<String, HashMap<String, ArrayList>> distanceMap = 
                new HashMap<String, HashMap<String, ArrayList>>();
        
        //도착지, 거리 저장용 임시 맵
        //tempMap을 만든후 이를 다시 distanceMap에 put
        HashMap<String, ArrayList> tempMap = new HashMap<String, ArrayList>();
       
		// 시간 구하기용
		Calendar cal = Calendar.getInstance();
		// 출력 형태를 지정
		SimpleDateFormat dataFormat = new SimpleDateFormat("YYYYMMddHHmm");							//나중에 시간되면 축제 데이터 받을 때 사용하도록 년월일까지 표시
		String curDate = dataFormat.format(cal.getTime());
		
		int time = 0;
		String sTime = startTime.replace(":", "");
		String ampm = sTime.substring(4);
		if(sTime.substring(0,4).equals("0000")) {
			time = Integer.parseInt(curDate.substring(8));
		} else if(ampm.equals("PM")){
			time = Integer.parseInt(sTime.substring(0,4)) + 1200;				//PM일 경우 12시간을 더해서 오후 표시
		}
		
		if(ampm.equals("PM")) {
			time = Integer.parseInt(sTime.substring(0,4)) + 1200;
		}

		if(time >= 2400) {
			time = time - 1200;					//pm12:00의 경우 24를 넘어가서 점심시간 계산을 못해서 24:00를 넘어가면 -12:00를 해줍니다
		}
     		
		for(int i=0; i<list.size(); i++) {
			tempMap = new HashMap<>();
			for(int j=0; j<list.size(); j++) {
				distanceMeter = distance(Double.parseDouble(list.get(i).getMapy()), Double.parseDouble(list.get(i).getMapx()), Double.parseDouble(list.get(j).getMapy()), Double.parseDouble(list.get(j).getMapx()), "meter");	
//				System.out.println(list.get(i).getTitle() + " --> " + distanceMeter + " --> " + list.get(j).getTitle() );	
				ArrayList<Object> mapList = new ArrayList<>();
				mapList.add(distanceMeter);									//거리
				mapList.add(list.get(j).getScope());						//네이버 음식점 평점
				mapList.add(list.get(j).getContentId());					
				mapList.add(list.get(j).getContentTypeId());
				for(int l=0; l<listStar.size(); l++) {
					if(listStar.get(l) !=null && list.get(j).getContentId().equals(listStar.get(l).getContentId())) {
						mapList.add(listStar.get(l).getStar());
						System.out.println(listStar.get(l).getStar());
					}
				}				
				for(int k=0; k<listHeart.size(); k++) {
					if(listHeart.get(k) !=null && list.get(j).getContentId().equals(listHeart.get(k).getContentId())) {
						mapList.add(listHeart.get(k).getHeart());			//게시글 좋아요
				}
				}
							
					
				tempMap.put(list.get(j).getContentId(), mapList);			
			}	
			distanceMap.put(list.get(i).getContentId(), tempMap);
		}
		
		Collections.shuffle(list);			//무작위 출발점 추려내기 위해서 list 한번 섞음		
		
		if(1130 <= time && time <= 1330 || 1730 <= time && time <= 1930) {			//점심, 저녁시간일 경우 음식점으로
			while(!list.get(0).getContentTypeId().equals("39")) {					
				Collections.shuffle(list);
			}
		} else { 
			while(list.get(0).getContentTypeId().equals("39")) {					//그 외 시간은 다른 장소로 출발지 지정
				Collections.shuffle(list);
			}
		};
		

		String start = list.get(0).getContentId();
		
		ArrayList destinationArry = new ArrayList<>();			//목적지 리스트 중 랜덤으로 하나의 목적지를 뽑아내기 위한 배열
		
		int lastTime = 0;			//경로 중 가장 마지막 시간
		Result result = dijkstra(distanceMap, start, time);    	//dijkstra(거리 맵, 출발지, 콘텐츠타입ID, 현재날짜시간)
		
		for(String key : result.preNode.keySet()) {	
			if(!result.preNode.get(key).isEmpty()) {							//출발지는 preNode 배열이 없으므로 result.preNode.get(key).get(1)에서 IndexOutBoundsException 에러남.  그래서 사용				
				if((int) result.preNode.get(key).get(1) > lastTime) { 			//result.preNode.get(key).get(1) = HHmm 형태의 시간이 들어있음
					lastTime = (int) result.preNode.get(key).get(1);	
				}
				
				int resultTime = (Integer) result.preNode.get(key).get(1);
				if(1130 <= lastTime && lastTime <= 1330 || 1730 <= lastTime && lastTime <= 1930) {       		//목적지 밥시간엔 음식점, 밥시간 외에는 관광지로 세팅
												
					if(result.preNode.get(key).get(2).equals("39") && resultTime == lastTime) {					
							destinationArry.add(key);						
					}
				} else {
					if(!result.preNode.get(key).get(2).equals("39") && resultTime == lastTime) {
							destinationArry.add(key);						
					}					
				}
			}
		};
			
	
		Collections.shuffle(destinationArry);						
		String destination = destinationArry.get(0).toString();
               
        ArrayList<String> path = new ArrayList<>();			//경로 담아두는 LIST
        ArrayList<String> pathTime = new ArrayList<>();		//경로 이동에 따른 시간 LIST
        String curNode = destination; 	//현재노드는 destination   
        
        path.add(destination);							//도착 노드
        	pathTime.add(String.valueOf(lastTime));			//도착 시간
        while(!result.preNode.get(curNode).isEmpty()){					
           curNode = (String) result.preNode.get(curNode).get(0);				//경로들을 json 데이터에 담는다.
           path.add(curNode);
           if(result.preNode.get(curNode).size() != 0) {
        	   		String curTime = String.valueOf(result.preNode.get(curNode).get(1));				//경로의 시간 데이터를 json 데이터에 담는다.
        	   		pathTime.add(curTime);
           } else {        	 
        	   		pathTime.add(String.valueOf(time));												//출발 시간
           }          
        }																
               
        Map<String, Object> jsonData = new HashMap<String, Object>();
        jsonData.put("path",path);
        jsonData.put("time", pathTime);
        return jsonData;
	}

	   // TSP
    @ResponseBody
    @RequestMapping(value = "/getpath", method= {RequestMethod.POST})
    public List<AddrVO> path(HttpServletRequest req, @RequestBody List<AddrVO> paramData) throws Exception{


          for(int i = 0; i < paramData.size(); i++) {


          }

             int[][] W = getP.getW(paramData);
             
          for(int i = 0; i<W.length; i++) {
             for(int j = 0; j <W[0].length; j++) {
             }
          }               
          getP.getShortestPath(1, 1, W);
          
           int delayTime = 2000;
           long saveTime = System.currentTimeMillis();
           long currTime = 0;
           while(currTime - saveTime < delayTime) {
              currTime = System.currentTimeMillis();
           }System.out.println("시간2초 경과");
           
          List<Integer> path = getP.getPath();

          Collections.reverse(path);
          
          int longest = 0;
          int star = 0;         
          for(int i = 0; i < path.size(); i++) {
             if(i == path.size()-1) {
                if(W[path.get(i)][path.get(0)] > longest) {
                   longest = W[path.get(i)][path.get(0)];
                   star = path.get(0);
                }
             }else if(W[path.get(i)][path.get(i+1)] > longest) {
                longest = W[path.get(i)][path.get(i+1)];
                star = path.get(i+1);
             }
          }
          
          for(int i = 0; i<W.length; i++) {
             for(int j = 0; j <W[0].length; j++) {
                System.out.print(W[i][j] + " ");
             }
          }
          
          
          int index = path.indexOf(star);
          List<Integer> result = new ArrayList<>();
          for(int i = index; i < path.size(); i++) {
             result.add(path.get(i));
          }
          for(int i = 0; i < index; i++) {
             result.add(path.get(i));
          }
          

          
          for(int i : result) {
          }
          
          List<AddrVO> re = new ArrayList<>();
          for(int i : result) {
             re.add(paramData.get(i-1));
          }

          
          return re;
    }  
		
	//거리 포인트 가중치 (Dijkstra)
	private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {

		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;

		if (unit == "kilometer") {
			dist = dist * 1.609344;
		} else if (unit == "meter") {
			dist = dist * 1609.344;
		}
		
		int distPoint = (int) Math.floor(dist/1000) * 10;
		//Meter로 계산할 때 1000으로 나눠서 소수점 이하 버림
		// * 10은 가중치 값 계산할 때 범위 적용가능하게 범위 늘리기용
		
		return (distPoint);
	}

	// This function converts decimal degrees to radians
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	// This function converts radians to decimal degrees
	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
	
	
	//다익스트라 알고리즘
	    final static double INFINITY = Double.MAX_VALUE;		
    
	    //dijkstra return 오브젝트
	    private static class Result{
	        //노드 까지 최단 거리
	        HashMap<String, Double> shortestPath = new HashMap<>();
	        //자기 이전의 노드 -> 루트 추적용
	        HashMap<String, ArrayList> preNode = new HashMap<>(); 
	    }
	    
	    //input: Map<출발지, Map<도착지, 거리>>, 최초 출발지, 시간, 날씨
	    //output: Result object
	    //do: dijkstra 알고리즘을 이용하여 출발지부터 각 노드까지 최단 거리, 루트 계산
	    private static Result dijkstra(HashMap<String, HashMap<String, ArrayList>> graph, String start, Integer time){
	    	HashMap<String, Double> shortestPath= new HashMap<>();
	        HashMap<String, ArrayList> preNode = new HashMap<>();			//<시간, 이전노드이름>
	        HashMap<String, Integer> timeMap = new HashMap<>();

	        shortestPath.put(start,0.0);
	        preNode.put(start, new ArrayList());
	        timeMap.put(start, time);
	        
	        //그래프의 각 노드를 저장할 집합
	        HashSet<String> Q = new HashSet<>();
	        
	        for(String key: graph.keySet()){       //출발->도착까지 모든 노드 Q에 저장
	            Q.add(key);
	            if(!key.equals(start)){			   //출발지가 아니면 경로, 이전 노드 초기화	            	
	            	shortestPath.put(key, INFINITY);	            	
	                preNode.put(key, new ArrayList());
	                timeMap.put(key, 0);
	            }
	        }
	        
	        while(!Q.isEmpty()){			        //Q가 빌때 까지 반복
	            //현재 Q안에서 최소 distance인 node 찾은 후 꺼내기
	            String minNode = "";
	            String minTypeId = "";
	            double minNodeDistance = INFINITY;
	            int curTime = 0;
	            
	            for(String node: Q){

	            		if(shortestPath.get(node) < minNodeDistance){	//최소거리 map에서 node까지의 거리가 가장 낮은걸 minNode, minNodeDistance에 업데이트
	            			minNode = node;								
	            			minNodeDistance = shortestPath.get(node);
	            			curTime = timeMap.get(node);
	            		}	           		
	            }
	            Q.remove(minNode);	// 노드간 거리들 중 가장 낮은 거리는 Q에서 제외시킴
	            
	            //거리 최소 node의 이웃 노드까지 거리 Map 읽어 오기
	            //최소 node 까지 거리 + 이웃 노드까지 거리 < 현재 이웃 노드의 최소 거리 이면 shortestPath, preNode 갱신      
	            
	            HashMap<String, ArrayList> minNodeMap = graph.get(minNode);		//가장 적은 거리를 통해 이동했으니깐 거기서 출발(minNode=출발)	
	            
	            for(String key: minNodeMap.keySet()) {	
	           
	            //minNode.get(key).get(0) : 거리
	            //minNode.get(key).get(1) : 네이버 평점
	            //minNode.get(key).get(2) : 콘텐츠ID
	            //minNode.get(key).get(3) : 콘텐츠 타입ID (카테고리)
	            //minNode.get(key).get(4) : 사용자가 준 별점
	            //minNode.get(key).get(5) : 좋아요...임시
	            	
	            	String sco = minNodeMap.get(key).get(1).toString().replaceAll("\n", "");
	            	Double scop = Double.parseDouble(sco);
	            	int scope = (int)Math.round(scop);
		            String contentId = minNodeMap.get(key).get(2).toString();
		            String contentTypeId = minNodeMap.get(key).get(3).toString();
		            int nextTime = curTime + 200;				//한 장소에 2시간 씩 머문다고 가정하여 알고리즘을 돌 때마다 2시간씩 더함
		            int like = 0;
		            int star = 0;
		            if(minNodeMap.get(key).size()==5) {
		            	star = (int)Math.round(Double.parseDouble(minNodeMap.get(key).get(4).toString()));
		            }
		            if(minNodeMap.get(key).size()==6) {
		            	like = (int) minNodeMap.get(key).get(5);
		            }
		            
	            		double distance = minNodeDistance + Double.parseDouble(minNodeMap.get(key).get(0).toString());		//처음 = 0 + 거리, 첫바퀴는 거리세팅      		//이동한 거리 + 출발지에서 다음 노드까지 거리

	            		distance = distance + 10;          			    	            		

	            			if(1130<=nextTime && nextTime<=1330 || 1730<=nextTime && nextTime<=1930) {
	            				if(contentTypeId.equals("39")) {					//밥 시간에 음식점 코드면 평점에 따라 가중치 차등 부여

	            					switch (scope){
	            					case 1: distance = distance - 1; break;
	            					case 2: distance = distance - 3; break;
	            					case 3: distance = distance - 5; break;
	            					case 4: distance = distance - 8; break;
	            					case 5: distance = distance - 10; break;
	            					default : distance = distance - 1; break;
					                }
			                	} else {
			                		distance = distance + 5000;						//음식점이 아닌 곳은 가중치 더해서 못가도록
			                	} 		                	
			                } else {
			                	if(contentTypeId.equals("39"))		            //밥 시간이 아닐 때 음식점들에 대해서 가중치 부여
			                		distance = distance + 10000;
			                }		                				                				                

	            			
	            			switch (star){
	        					case 1: distance = distance - 1; break;
	        					case 2: distance = distance - 3; break;
	        					case 3: distance = distance - 5; break;
	        					case 4: distance = distance - 8; break;
	        					case 5: distance = distance - 10; break;
	        					default : distance = distance - 1; break;
				                }
	            			

/*	            			if(like <= 50) {
	            				distance = distance - 1;
	            			} else {
	            				distance = distance - 10;
	            			}*/
		                
		                if(distance < shortestPath.get(key)){						//key까지 최소거리보다 distance가 적으면 그거로 바꿈		                	
		                	ArrayList<Object> nodeMap = new ArrayList<>();
		                	shortestPath.put(key, distance);						//minNode(출발) -> key(도착) 거리 update		                    
		                	nodeMap.add(minNode);
		                	nodeMap.add(nextTime);
		                	nodeMap.add(contentTypeId);
		                	
		                    timeMap.put(key, nextTime);
		                    preNode.put(key, nodeMap);
		                }
	            	}            
	            }
	        	        
	        Result result = new Result();
	        result.shortestPath.putAll(shortestPath);
	        result.preNode.putAll(preNode);
	        return result;
	    }
	    

}


//TSP에서 사용하는 클래스
class getP{
	   public static int[][] W;
	   public static int[][] dp;
	   public static int N;
	   public static final int INF = 1000000000;
	   private static Deque<Integer> route = new ArrayDeque<>();
	   private static List<Integer> solution;
	   private static List<Integer> path;
	   static int re = INF;
	   

	   
	    public static int[][] getW(List<AddrVO> paramData){
	      double distanceMeter = 0;
	       int[][] W = null;
	      N = paramData.size();
	       W = new int[N + 1][N + 1];
	      dp = new int[N + 1][1 << N];
	      re = INF;
	      
	      if(route!=null) {
	      route.clear();
	      }

	      
	      // 2차원 배열의 모든 원소를 -1로
	      for (int i = 1; i <= N; i++) {
	         Arrays.fill(dp[i], -1);
	      }
	        
	        for(int i = 0; i < N; i++) {
	            for(int j = 0; j < N; j++) {
	               if(i==j)continue;
	               
	               //추가ㅑ!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!음식점 사이에 5km 추가.
	               if(paramData.get(i).getContentTypeId().equals("39") && paramData.get(j).getContentTypeId().equals("39")) {
	                  distanceMeter = distance2(Double.parseDouble(paramData.get(i).getMapy()), Double.parseDouble(paramData.get(i).getMapx()), Double.parseDouble(paramData.get(j).getMapy()), Double.parseDouble(paramData.get(j).getMapx()), "meter");
	                  distanceMeter += 5000;
	               }else {
	               distanceMeter = distance2(Double.parseDouble(paramData.get(i).getMapy()), Double.parseDouble(paramData.get(i).getMapx()), Double.parseDouble(paramData.get(j).getMapy()), Double.parseDouble(paramData.get(j).getMapx()), "meter");   
	               }
	               W[i+1][j+1] = (int) Math.floor(distanceMeter);                       
	            }   
	         }
	       return W;
	    }   
	   
	   public static int getShortestPath(int current, int visited, int[][] W) {
	      

	      
	      // 모든 정점을 다 들른 경우
	      if (visited == (1 << N) - 1) {   
	         route.push(1);
	         //1까지 경로 리턴
	         return W[current][1];
	      }

	      int ret = INF;
	      
	      // 집합에서 다음에 올 원소를 고르자!
	      for (int i = 1; i <= N; i++) {
	         int next = i;
	         
	         // 이미 들렀던 곳일때 pass
	         if ((visited & (1 << (next - 1))) != 0) { 
	            continue;
	         }
	         
	         // 0은 경로가 없으므로 pass
	         if(W[current][next] == 0)
	            continue;
	         
	         route.push(i);

	         int temp = W[current][next] + getShortestPath(next, visited + (1 << (next - 1)), W);

	         if(route.size()==N) {
	            solution = new ArrayList<>(route);
	            route.pop();
	         }
	         if(route.size()==2) {
	            if(re > temp + W[i][1]) {
	               path = new ArrayList<>(solution);
	               re = temp;
	            }
	         }
	         route.pop();
	         ret = Math.min(ret, temp);   
	      }   
	      return dp[current][visited] = ret;
	   }
	   
	   
	   public static List<Integer> getPath() {
	      return path;
	   }
	   
	   //좌표로 거리 계산(TSP)
	   private static double distance2(double lat1, double lon1, double lat2, double lon2, String unit) {

	      double theta = lon1 - lon2;
	      double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
	            + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

	      dist = Math.acos(dist);
	      dist = rad2deg(dist);
	      dist = dist * 60 * 1.1515;

	      if (unit == "kilometer") {
	         dist = dist * 1.609344;
	      } else if (unit == "meter") {
	         dist = dist * 1609.344;
	      }
	      
	      return dist;
	   }
	   
	   // This function converts decimal degrees to radians
	   private static double deg2rad(double deg) {
	      return (deg * Math.PI / 180.0);
	   }

	   // This function converts radians to decimal degrees
	   private static double rad2deg(double rad) {
	      return (rad * 180 / Math.PI);
	   }

	   
	}
