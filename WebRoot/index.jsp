<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8" import="java.util.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>深圳市出租车轨迹分析</title>
	<link rel="stylesheet" type="text/css" href="./publics/libs/bootstrap-3.3.5-dist/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="./publics/libs/laydate/skins/default/laydate.css">
	<link rel="stylesheet" type="text/css" href="./publics/css/light.css">
	<script type="text/javascript" src="./publics/libs/jquery/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="./publics/libs/bootstrap-3.3.5-dist/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="./publics/libs/laydate/laydate.js"></script>
	<script src="http://echarts.baidu.com/build/dist/echarts-all.js"></script>
	<script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=d06d162e3246f0f6ca7b9780a998d372"></script>
</head>
<body>
	<%	
		String gpsJsonStr = "";	
		Object gpsJson = session.getAttribute("gpsjson");
		if (gpsJsonStr != null) {
			gpsJsonStr = (String)gpsJson;
		}
		//System.out.print(gpsJsonStr+"====");
	%>
	
	<div class="container">
		<div class='map' id="container"></div>
		<div class="top-nav">
			<div class="col-sm-2 col-md-2">
				<div class="logo"></div>
				<!-- <span>深圳市出租车空重车分析</span> -->
			</div>
			<div class="col-sm-10 col-md-10 btn-groups">
				<form action='gpsServLet' method='post' class="time-btn-groups">
					<span class="label-group"> 
						<label>开始日期:</label>
						<input type="text" id='startDate' class="laydate-icon" name="startDate">
					</span> 
					<span class="label-group">
						<label>结束日期:</label> 
						<input type="text" id='endDate' class="laydate-icon" name="endDate">
					</span> 
					<span class="label-group"> 
						<label>查询时段:</label> 
						<select name="startTime" id="startTime" size=1>
							<option value="00:00:00" selected>00:00</option>
							<option value="01:00:00">01:00</option>
							<option value="02:00:00">02:00</option>
							<option value="03:00:00">03:00</option>
							<option value="04:00:00">04:00</option>
							<option value="05:00:00">05:00</option>
							<option value="06:00:00">06:00</option>
							<option value="07:00:00">07:00</option>
							<option value="08:00:00">08:00</option>
							<option value="09:00:00">09:00</option>
							<option value="10:00:00">10:00</option>
							<option value="11:00:00">11:00</option>
							<option value="12:00:00">12:00</option>
							<option value="13:00:00">13:00</option>
							<option value="14:00:00">14:00</option>
							<option value="15:00:00">15:00</option>
							<option value="16:00:00">16:00</option>
							<option value="17:00:00">17:00</option>
							<option value="18:00:00">18:00</option>
							<option value="19:00:00">19:00</option>
							<option value="20:00:00">20:00</option>
							<option value="21:00:00">21:00</option>
							<option value="22:00:00">22:00</option>
							<option value="23:00:00">23:00</option>
						</select> 
						<label>至</label> 
						<select name="endTime" id="endTime" size=1>
							<option value="01:00:00">01:00</option>
							<option value="02:00:00">02:00</option>
							<option value="03:00:00">03:00</option>
							<option value="04:00:00">04:00</option>
							<option value="05:00:00">05:00</option>
							<option value="06:00:00">06:00</option>
							<option value="07:00:00">07:00</option>
							<option value="08:00:00">08:00</option>
							<option value="09:00:00">09:00</option>
							<option value="10:00:00">10:00</option>
							<option value="11:00:00">11:00</option>
							<option value="12:00:00">12:00</option>
							<option value="13:00:00">13:00</option>
							<option value="14:00:00">14:00</option>
							<option value="15:00:00">15:00</option>
							<option value="16:00:00">16:00</option>
							<option value="17:00:00">17:00</option>
							<option value="18:00:00">18:00</option>
							<option value="19:00:00">19:00</option>
							<option value="20:00:00">20:00</option>
							<option value="21:00:00">21:00</option>
							<option value="22:00:00">22:00</option>
							<option value="23:00:00">23:00</option>
							<option value="23:59:59" selected>24:00</option>
						</select>
					</span>
					<button type="submit" class='btn btn-sm btn-info search-btn'>查询</button>
				</form>			
				<div class="right-menu">
					<span class="first-line"></span>
					<span class="middle-line"></span>
					<span class="last-line"></span>
				</div>
			</div>
		</div>
		<div class="echarts">
			<ul class="btn-groups">
				<li class="echart-btn btn-actived">上下客占比</li>
				<li class="echart-btn">上下客热点</li>
				<li class="echart-btn">重车走势</li>
			</ul>
			<div class="echart-box">
				<!-- 空重车饼图 -->
				<div id="blank_taxi"></div>
				<!-- 热点图 -->
				<div id="hotPoints_taxi"></div>
			</div>
			<!-- <div class="city-zone">
				<ul class="zone-groups">
					<li id='sz' class="item btn-actived">
						<span>全市</span>
						<i></i>
					</li>
					<li id='luohu' class="item">
						<span>罗湖区</span>
						<i></i>
					</li>
					<li id='futian' class="item">
						<span>福田区</span>
						<i></i>
					</li>
					<li id='nanshan' class="item">
						<span>南山区</span>
						<i></i>
					</li>
					<li id='yantian' class="item">
						<span>盐田区</span>
						<i></i>
					</li>
					<li id='baoan' class="item">
						<span>宝安区</span>
						<i></i>
					</li>
					<li id='longG' class="item">
						<span>龙岗区</span>
						<i></i>
					</li>
					<li id='guangM' class="item">
						<span>光明新区</span>
						<i></i>
					</li>
					<li id='longH' class="item">
						<span>龙华新区</span>
						<i></i>
					</li>
					<li id='pingS' class="item">
						<span>坪山新区</span>
						<i></i>
					</li>
					<li id='dapeng' class="item">
						<span>大鹏新区</span>
						<i></i>
					</li>
				</ul>
			</div> -->
		</div>
		<!-- 空重车走势图 -->
		<div id='mask-layer'>
			<div class="inner-box">
				<div class="select-groups" id='select-groups'>
					<span id='ByHours' class='actived'>按小时</span>
					<span id='ByDay'>按天</span>
				</div>
				<div id="trend_chart_box">
					<div id="trend_time" ></div>
					<div id="trend_date"></div>
				</div>
			</div>
		</div>
	</div>
	<!-- js script -->
	<script type="text/javascript">	
				
		 /*
	     * 初始化日期显示框
	     * 默认显示的是今天到前一个月的今天
	     */
	    var dateNow = new Date();
	    var start = '' + dateNow.getFullYear() + '-' + (dateNow.getMonth()+1) + '-' + dateNow.getDate();
	    var end   = '' + dateNow.getFullYear() + '-' + (dateNow.getMonth()+1) + '-' + dateNow.getDate();
	   
	
		var startDate = '<%=session.getAttribute("startDate")%>';
		var endtDate = '<%=session.getAttribute("endtDate")%>';
		var startTime = '<%=session.getAttribute("startTime")%>';
		var endTime = '<%=session.getAttribute("endTime")%>';
		if (startDate.length > 5){
			start = startDate;
		}
		if (endtDate.length > 5){
			end = endtDate;
		}
		$('#startDate').val(start);
	    $('#endDate').val(end);
	    
		
		if (startTime.length > 5){
			$('#startTime').val(startTime);
		}
		if (endTime.length > 5){
			$('#endTime').val(endTime);						
		}
		// init chart data		
		var gpsJson = <%=gpsJsonStr%>;	
		console.log(gpsJson);
	</script>	
	<!--  <script src="./publics/js/lightMap.js"></script> -->
	<script src="./publics/js/main.js"></script>
</body>
</html>