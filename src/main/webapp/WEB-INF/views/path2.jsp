<%@page import="com.project.sns.addr.vo.AddrVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
<meta name="viewport"
   content="width=device-width, initial-scale=1, maximum-scale=1">
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=1993b1e3b0175008e57aef80bfdd05b0"></script>
<link rel="stylesheet"
   href="resources/facebook/assets/css/bootstrap2.css">
<link rel="stylesheet"
   href="resources/facebook/assets/css/facebook2.css">
<link rel="stylesheet" href="resources/css/map.css" type="text/css"/>

<!-- <script type="text/javascript" src="resources/js/map.js"></script> -->
<script type="text/javascript" src="resources/facebook/assets/js/jquery.js"></script>
<script type="text/javascript" src="resources/facebook/assets/js/bootstrap.js"></script>
<script type="text/javascript" src="resources/js/map.js"></script>
<script type="text/javascript" src="resources/js/startTime.js"></script>


<style>
 #sidemenu {
	background-image: url("resources/image/pathbackground.jpg");
	}
 #searchMap{
   background-image: url('resources/image/search_icon.jpg');
   }
   
  .scope{
	float:left;
	margin: 10px;
	top: 0;
	}
	
   .review{
	margin: 10px;
	}
</style>

<title>TourSNS</title>

</head>
<body>   

<div class="wrapper">
      <div class="box">
         <div class="row row-offcanvas row-offcanvas-left">
               <!--
                  col-sm-12, col-xs-12 둘 다 같은 width 설정 class.
                  col-xs-12는 disable된 상태
                  
                  맨 뒤의 숫자를 변경하면(col-sm-10) 회색 화면의 가로가 줄어들어 
                  백그라운드의 회색 화면이 나타남.                  
                -->
            <div id="main" class="column col-sm-12 col-xs-12">
               
            <%@ include file="include/topbar.jsp"%>
   <div id="setDiv">
      <div id="mask"></div>
      <div id="window"></div>
      <div id="mapBox">
         <div id="searchBox" style="z-index: 2;">
             <div id="searchTextBox"style="z-index: 3;">
         <input type="text" id="searchMap" onkeydown="enterkey();"/> <img src="resources/image/search_btn.jpg" id = "searchMapBtn" type="button" value="검색" onclick="searchMap();"/>
      		</div>
        </div>
         <div id="weatherBox" style="z-index:2;">
			  <img id="sun" src ="resources/image/sun.png">     
           	  <img id="rain" src ="resources/image/rain.png">
      	</div>
         <div id="map" style="z-index: 1;" ></div> 

      </div>
      
      <!-- 오른쪽 사이드 -->  
      <div id="sidemenu">  
         <button class="sideBtn" id="getpath" onclick="recommend()"><img src="resources/image/path.png"></button>
         <button class="sideBtn" id="newpath" onclick="newpath()"><img src="resources/image/pathreset.png"></button>
         <button class="sideBtn" id="savepath" onclick="savepath()"><img src="resources/image/pathsave.png"></button>
      <div></div>
	 <!-- 출발 시간 -->
          <div style = "padding-top : 15px; padding-left : 25px;">
          	<span style = "color : white; font-size : 17px; float : left; ">출발 시간 </span>
        	
 			<input type="text" id="startTime" onclick="setTime()" readonly></input>

			<div id="timeOption" style = "float : left; margin: 10px 0;">
				<input type='text' id='hour' value="00" onchange="hourSetting(this.value)"></input>
					<button class="timeBtn" onclick="upHour()"><img src= "resources/image/timeup.png"></button>
					<button class="timeBtn" onclick="downHour()"><img src= "resources/image/timedown.jpg"></button>
					<span style = "color : white; font-size : 17px;">:</span>
				 <input type='text' id='minute' value="00" onchange="minuteSetting(this.value)"></input>
					<button class="timeBtn" onclick="upMinute()"><img src= "resources/image/timeup.png"></button>
					<button class="timeBtn" onclick="downMinute()"><img src= "resources/image/timedown.jpg"></button>
				 <select id ="ampm">
					 <option value='AM'>AM</option>
					 <option value='PM'>PM</option>
				 </select>
			</div>
         </div> 
		<h4 id="pathlist"></h4>		
      <div id="path">
      			</div>     
   			   </div>      
   			 </div>
            </div>         
         </div>
      </div>
   </div>
<script>

$('#timeOption').hide();
$('#sun').hide();
$('#rain').hide();

var mapContainer = document.getElementById('map'), // 지도를 표시할 div  
    mapOption = { 
        center: new daum.maps.LatLng(37.566826, 126.9786567), // 지도의 중심좌표
        level: 8 // 지도의 확대 레벨
    };

var map = new daum.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
customOverlay = new daum.maps.CustomOverlay({});
var weather = <%=(int) request.getAttribute("weather")%>;
if(weather==1){
	$('#sun').show();
} else {
	$('#rain').show();	
}

// 마커를 표시할 위치와 title 객체 배열입니다 
var positions =  [
<%
   List<AddrVO> list = (List<AddrVO>) request.getAttribute("list");
   for (int i = 0; i < list.size(); i++) {
%>
{title : "<%=list.get(i).getTitle()%>",
 contenttypeid : "<%=list.get(i).getContentTypeId()%>",
 contentid : "<%=list.get(i).getContentId()%>",
 latlng : new daum.maps.LatLng(<%=list.get(i).getMapy()%>,<%=list.get(i).getMapx()%>),
 mapx : "<%=list.get(i).getMapx()%>",
 mapy : "<%=list.get(i).getMapy()%>"				
}

<%
   if (list.size() - 1 > i) {
         out.append(",");
      }
   }
%>
];

//경로저장
function savepath(){
   
   var id = "<%=request.getAttribute("id")%>";      

   if(id == "null"){
      alert("회원만 이용 가능합니다.");
	  location.href = "login.do";
   } else {      
      var storyName = prompt('새로운 스토리명을 입력해주세요.');
      if(storyName!=null && storyName!=''){
         $.ajax({                           //스토리 저장
            url : "inputStory.do",
            data : {"id":id, "story_title":storyName},
            success : function(){
               for(var i=0; i<arr.length; i++){
                  $.post("insertPath.do", {"contentId" : arr[i].contentId}, function(){            //경로 저장
                  })            
               }
               alert("스토리가 저장 되었습니다.");
            },
            error : function(){
               alert("코스를 먼저 찾아주세요.");
            }
         })
      }	
   } 
}

</script>  

</body>
</html>