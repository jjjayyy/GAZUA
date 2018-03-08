<%@page import="com.project.sns.addr.vo.AddrVO"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>


<title>Insert title here</title>
    <style>
        #setDiv {
            padding-top: 100px;
            text-align: center;
        }
        #mask {
            position:absolute;
            left:0;
            top:0;
            z-index:9999;
            background-color:#000;
            display:none;
        }
        #window {
            display: none;
            background-color: #ffffff;
            height: 200px;
            z-index:99999;
            width:600px;
            height:250px;
            border:1px;
            solid #ccc;
            position:fixed;
            
        }
    </style>   
</head>
<body>
<div id="setDiv">

    <div id="mask"></div>
    <div id="window">

    
    
    
    </div>
    <div id="map" style="width:100%;height:850px;"></div>
</div>


<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=1993b1e3b0175008e57aef80bfdd05b0"></script>
<script>


var mapContainer = document.getElementById('map'), // ������ ǥ���� div  
    mapOption = { 
        center: new daum.maps.LatLng(37.566826, 126.9786567), // ������ �߽���ǥ
        level: 3 // ������ Ȯ�� ����
    };

var map = new daum.maps.Map(mapContainer, mapOption); // ������ �����մϴ�

// ��Ŀ�� ǥ���� ��ġ�� title ��ü �迭�Դϴ� 
var positions =  [
   <%
      List<AddrVO> list = (List<AddrVO>) request.getAttribute("list");
      for (int i = 0; i < list.size(); i++) {
   %>
   {title : "<%=list.get(i).getTitle()%>",
    contenttypeid : "<%=list.get(i).getContentTypeId()%>",
    contentid : "<%=list.get(i).getContentId()%>",
    latlng : new daum.maps.LatLng(<%=list.get(i).getMapy()%>,<%=list.get(i).getMapx()%>)
   }

   <%
      if (list.size() - 1 > i) {
            out.append(",");
         }
      }
   %>
   ];

   
   // ��Ŀ �̹����� �̹��� �ּ��Դϴ�
   var imageSrc = "http://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png"; 
       
   for (var i = 0; i < positions.length; i ++) {
       

       addMarker(positions[i].latlng, positions[i].title, positions[i].contentid, positions[i].contenttypeid);    
         
       
       //function(positions[i].contentId); �ؼ� calldetail�� ���� �Ѱ��ְ� ajax�� �޾ƿͼ� ����غ���
   }

    // �� ���� ����ũ�� Ŭ���ÿ��� ��� �����ϵ��� ó���մϴ�.
    $('#mask').click(function () {
        $(this).hide();
        $('#window').hide();
    });   
    
 
function callDetail(contentid,contenttypeid){
      
      
   
/*     method = method || "post";
   
     var form = document.createElement("form");

     form.setAttribute("method", method);

     form.setAttribute("action", url);

         var hiddenField = document.createElement("input");

         hiddenField.setAttribute("type", "hidden");

         hiddenField.setAttribute("name", "contentId");

         hiddenField.setAttribute("value", contentid);

         form.appendChild(hiddenField);
         
         var hiddenField2 = document.createElement("input");

         hiddenField2.setAttribute("type", "hidden");

         hiddenField2.setAttribute("name", "contentTypeId");

         hiddenField2.setAttribute("value", contenttypeid);

         form.appendChild(hiddenField2);

     

   form.submit(); */
   
   
   location.href ="callDetail.do?contentId="+contentid+"&contentTypeId="+contenttypeid;
   
}   
    
    
function addMarker(position, title, contentid, contenttypeid) {   

    // ��Ŀ �̹����� �̹��� ũ�� �Դϴ�
    var imageSize = new daum.maps.Size(24, 35); 
    
    // ��Ŀ �̹����� �����մϴ�    
    var markerImage = new daum.maps.MarkerImage(imageSrc, imageSize); 
    
    // ��Ŀ�� �����մϴ�
    var marker = new daum.maps.Marker({
        map: map, // ��Ŀ�� ǥ���� ����
        position: position, // ��Ŀ�� ǥ���� ��ġ
        title : title, // ��Ŀ�� Ÿ��Ʋ, ��Ŀ�� ���콺�� �ø��� Ÿ��Ʋ�� ǥ�õ˴ϴ�
        image : markerImage // ��Ŀ �̹��� 
    });
   
   
    
   daum.maps.event.addListener(marker, 'click', function() {
      callDetail(contentid, contenttypeid);
      
      wrapWindowByMask();
      
      $.ajax({        
            url: 'callDetail.do',
            type: 'get',
            dataType: 'json',
            success: function(data){
               console.log(data);
//                 console.log(data.response.body.items.item);
                var myItem = data.response.body.items.item;
                 
                for(var i=0; myItem.length; i++){
                    var output = '';
                    console.log(myItem.length);

                    output += '<h4>'+myItem[i].treatmenu+'</h4>';
                    output += '<h4>'+myItem[i].opentimefood+'</h4>';
                    output += '<h4>'+myItem[i].reservationfood+'</h4>'; 
                    document.getElementById("window").innerHTML += output;
                }
            },
          error: function(XMLHttpRequest, textStatus, errorThrown) { 
              alert("Status: " + textStatus); alert("Error: " + errorThrown); 
          } 
   });
   });

   
    
   }
  

function wrapWindowByMask(){ //ȭ���� ���̿� �ʺ� ���Ѵ�.
    
   var maskHeight = $(document).height();
   var maskWidth = $(window).width(); //����ũ�� ���̿� �ʺ� ȭ�� ������ ����� ��ü ȭ���� ä���.
   
   $('#mask').css({'width':maskWidth,'height':maskHeight});   //����ũ�� ���� ó�� 
   $('#mask').fadeTo("slow",0.8);

   var left = ( $(window).scrollLeft() + ( $(window).width() - $('#window').width()) / 2 );
    var top = ( $(window).scrollTop() + ( $(window).height() - $('#window').height()) / 2 );
    
    $('#window').css({'left':left,'top':top, 'position':'absolute'});
    
    $('#window').show();
 
   

}
   
<%-- var linePath =  [
   <%
      List<AddrVO> list2 = (List<AddrVO>) request.getAttribute("list");
      for (int i = 0; i < list.size(); i++) {
   %>
    new daum.maps.LatLng(<%=list2.get(i).getMapY()%>,<%=list2.get(i).getMapX()%>)
   

   <%
      if (list2.size() - 1 > i) {
            out.append(",");
         }
      }
   %>
   ];
 var polyline = new daum.maps.Polyline({
    path: linePath, // ���� �����ϴ� ��ǥ�迭 �Դϴ�
    strokeWeight: 5, // ���� �β� �Դϴ�
    strokeColor: '#FFAE00', // ���� �����Դϴ�
    strokeOpacity: 0.7, // ���� ������ �Դϴ� 1���� 0 ������ ���̸� 0�� �������� �����մϴ�
    strokeStyle: 'solid' // ���� ��Ÿ���Դϴ�
});

// ������ ���� ǥ���մϴ� 
polyline.setMap(map);    --%>


   
</script>





</body>
</html>