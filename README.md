# About Repository

자동 경로 추천 서비스가 있는 여행 SNS 입니다.
사용자는 시간, 거리, 망고플레이트 평점, 사용자 별점, 날씨를 고려한 코스를 자동으로 추천 받고, 이 경로를 저장해 나만의 스토리로 SNS에 글을 올릴 수 있습니다.<br/>
URL : http://ec2-13-124-243-205.ap-northeast-2.compute.amazonaws.com/GAZUA/newMap.do  (메인페이지가 아닌 담당한 파트의 페이지)

## GAZUA (Travel SNS)
- Term : 2017.12.04 ~ 2018.03.05
- Dev : JAVA, Javascript, Spring Framwork, Mybatis, JQuery, AJAX, Amazon RDS(MySQL), MySQL Workbench, AWS
- Contents(Only my part) 
  - 다익스트라 알고리즘을 활용한 자동 경로 추천 및 지도 구현 그리고 경로를 저장하는 부분을 담당 
  - 카카오 맵 API를 이용해 행정 구역을 나누어 지역 별로 경로가 추천되도록 구현
  - Dijkstra Algorithm을 사용해 경로 추천 (가중치 : 거리 / 망고플레이트 평점 / 사용자 별점 / 시간) 
     - 거리 : 출발지 반경 1km 이내는 0점, +1km 당 10점씩 부여 
     - 시간 : 11:30-13:30(점심시간), 17:30-19:30(저녁시간) 에는 음식점 추천 
     - 망고플레이트 평점 : 망고플레이트서 크롤링 해온 평점을 DB에 저장해 높은 순으로 추천
     - 사용자 별점 : SNS 사용자들이 부여한 별점
  - 관광지 데이터는 Tour API를 활용
  - 날씨 API를 활용해 비/눈이 올 경우 실내 장소맊 추천하도록 구현 
  - 출발 시간 입력 
     - 시간 입력 : ’12’ 넘어가면 다시 ’00’이 되도록 구현, 화살표 클릭 및 직접 입력 
     - 분 입력 : ’59’ 넘어가면 다시 ’00’이 되도록 구현, 화살표 클릭 및 직접 입력
     