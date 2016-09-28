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
<link rel="stylesheet" type="text/css"
	href="./publics/libs/bootstrap-3.3.5-dist/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="./publics/libs/laydate/skins/default/laydate.css">
<link rel="stylesheet" type="text/css" href="./publics/css/light.css">
<script type="text/javascript"
	src="./publics/libs/jquery/jquery-3.1.0.min.js"></script>
<script type="text/javascript"
	src="./publics/libs/bootstrap-3.3.5-dist/js/bootstrap.min.js"></script>
<script type="text/javascript" src="./publics/libs/laydate/laydate.js"></script>
<script src="http://echarts.baidu.com/build/dist/echarts-all.js"></script>
<script type="text/javascript"
	src="http://webapi.amap.com/maps?v=1.3&key=d06d162e3246f0f6ca7b9780a998d372"></script>
</head>
<body>
	<div class="container">
		<div class='map' id="container"></div>
		<div class="top-nav">
			<div class="col-sm-2 col-md-2">
				<div class="logo"></div>
				<!-- <span>深圳市出租车空重车分析</span> -->
			</div>
			<div class="col-sm-10 col-md-10 btn-groups">
				<form action='gpsServLet' method='post' class="time-btn-groups" id="form-time">	
					<span class="label-group"> 
						<label>查询区域:</label> 
						<select name='district' id='district'> 
							<option value=7 selected>全市</option>
							<option value=1>罗湖区</option>
							<option value=2>福田区</option>
							<option value=3>南山区</option>
							<option value=4>龙岗区</option>
							<option value=5>宝安区</option>
							<option value=6>盐田区</option>
<!-- 							<option value=0>市外</option> -->
							
						</select>	 
					</span>					
					<span class="label-group"> <label>开始日期:</label> <input
						type="text" id='startDate' class="laydate-icon" name="startDate"
						autoComplete='off'>
					</span> <span class="label-group"> <label>结束日期:</label> <input
						type="text" id='endDate' class="laydate-icon" name="endDate"
						autoComplete='off'>
					</span> <span class="label-group"> <label>查询时段:</label> <select
						name="startTime" id="startTime" size=1>
							<option value="00:00:00" selected>00点</option>
							<option value="01:00:00">01点</option>
							<option value="02:00:00">02点</option>
							<option value="03:00:00">03点</option>
							<option value="04:00:00">04点</option>
							<option value="05:00:00">05点</option>
							<option value="06:00:00">06点</option>
							<option value="07:00:00">07点</option>
							<option value="08:00:00">08点</option>
							<option value="09:00:00">09点</option>
							<option value="10:00:00">10点</option>
							<option value="11:00:00">11点</option>
							<option value="12:00:00">12点</option>
							<option value="13:00:00">13点</option>
							<option value="14:00:00">14点</option>
							<option value="15:00:00">15点</option>
							<option value="16:00:00">16点</option>
							<option value="17:00:00">17点</option>
							<option value="18:00:00">18点</option>
							<option value="19:00:00">19点</option>
							<option value="20:00:00">20点</option>
							<option value="21:00:00">21点</option>
							<option value="22:00:00">22点</option>
							<option value="23:00:00">23点</option>
					</select>
					<label>至</label> 
					<select name="endTime" id="endTime" size=1>
							<option value="00:59:59">00点</option>
							<option value="01:59:59">01点</option>
							<option value="02:59:59">02点</option>
							<option value="03:59:59">03点</option>
							<option value="04:59:59">04点</option>
							<option value="05:59:59">05点</option>
							<option value="06:59:59">06点</option>
							<option value="07:59:59">07点</option>
							<option value="08:59:59">08点</option>
							<option value="09:59:59">09点</option>
							<option value="10:59:59">10点</option>
							<option value="11:59:59">11点</option>
							<option value="12:59:59">12点</option>
							<option value="13:59:59">13点</option>
							<option value="14:59:59">14点</option>
							<option value="15:59:59">15点</option>
							<option value="16:59:59">16点</option>
							<option value="17:59:59">17点</option>
							<option value="18:59:59">18点</option>
							<option value="19:59:59">19点</option>
							<option value="20:59:59">20点</option>
							<option value="21:59:59">21点</option>
							<option value="22:59:59">22点</option>							
							<option value="23:59:59" selected>23点</option>
					</select>
					</span>
					<button type="submit" class='btn btn-sm btn-info search-btn'
						id='#searchBtn'>查询</button>
				</form>
				<div class="right-menu">
					<span class="first-line"></span> <span class="middle-line"></span>
					<span class="last-line"></span>
				</div>
			</div>
		</div>
		<!-- 空重车走势图 -->
		<div id='mask-layer'>
			<div class="inner-box">
				<div class="select-groups" id='select-groups'>
					<span id='ByHours' class='actived'>按时段</span>
					<span id='ByDay'>按天</span>
					<span id='ByWeek'>按周</span>
				</div>
				<div id="trend_chart_box">
					<div id="trend_time"></div>
					<div id="trend_date"></div>
					<div id="trend_week"></div>
				</div>
			</div>
		</div>
	</div>
	<div class="right-bar">
		<nav class="options">
			<li id="echart-opt" class="active-echart-opt">图<br>表<br>数<br>据</li>
			<li id="hotpoint-opt">自<br>定<br>义<br>重<br>点<br>区<br>域</li>
		</nav>
		<div class="echart-data">
			<div class="echarts">
				<ul class="btn-groups">
					<li class="echart-btn btn-actived">空重车占比</li>
					<li class="echart-btn">重点区域</li>
					<li class="echart-btn">上客走势</li>
				</ul>
				<div class="echart-box">
					<!-- 空重车饼图 -->
					<div id="blank_taxi"></div>
					<!-- 热点图 -->
					<div id="hotPoints_taxi"></div>
				</div>
			</div>
		</div>
		<div class="hotpoint">
			<form action="" method="" id="hotpoint-form">
				<input type="text" name="searchText" 
				id="keyvalue" placeholder="请输入要添加的区域名" maxlength='6'>
				<input type="text" readonly="true" name="longitude" placeholder="经度值" id='longitude'>
				<input type="text" readonly="true" name="latitude" placeholder="纬度值" id='latitude'>
				<button type="submit" id="primary-add-btn">添加</button>
			</form>
			<div class="hotpoint-list">
				<hr>
				<table class="table table-striped" id="tb-hotpoint-info">
					<thead>
						<tr>
							<td>名称</td>
							<td>经度</td>
							<td>纬度</td>
							<td>按钮</td>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<!-- js script -->
	<script src="./publics/js/main.js"></script>
	<script type="text/javascript">		
		/*
	     * 初始化日期显示框
	     * 默认显示的是昨天日期
	     */
	    var dateNow = new Date();
	    var start =  dateNow.getFullYear()-1 + '-' + (dateNow.getMonth()+1) + '-' + (dateNow.getDate()-1);
	    var end   =  dateNow.getFullYear()-1 + '-' + (dateNow.getMonth()+1) + '-' + (dateNow.getDate()-1);
	   	    
		var startDate = '<%=session.getAttribute("startDate")%>';
		var endDate = '<%=session.getAttribute("endDate")%>';
		var startTime = '<%=session.getAttribute("startTime")%>';
		var endTime = '<%=session.getAttribute("endTime")%>';
		var district = <%=session.getAttribute("district")%>;

		if (startDate.length > 8) {
			start = startDate;
		}
		if (endDate.length > 8) {
			end = endDate;
		}
		// console.log(start + '===' + end);

		$('#startDate').val(start);
		$('#endDate').val(end);

		if (startTime.length > 5) {
			$('#startTime').val(startTime);
		}
		if (endTime.length > 5) {
			$('#endTime').val(endTime);
		}
		if (district != null) {
			$('#district').val(district);
			initialDistrict = district;
		}
		
		// init chart data		
		var gpsJson =<%=session.getAttribute("gpsjson")%>;

		if (gpsJson == null || "null" == gpsJson) {
			window.location.href = "gpsServLet?startDate=" + $('#startDate').val() 
					+ "&endDate=" + $('#endDate').val()
					+ "&startTime=" + $('#startTime').val() 
					+ "&endTime=" + $('#endTime').val()
					+ "&district=" + $('#district').val();
		}
		if(district!=null){
			changeMapCenter(district);
		}
		
		//console.log(gpsJson);
	</script>
 	<script src="./publics/js/mychart.js"></script> 
</body>
</html>