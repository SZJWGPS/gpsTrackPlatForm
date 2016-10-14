	var map = new AMap.Map('container');	
	var initialDistrict =7 ; //
	var districtChanged = false;
	
	// 地图加载初始化设置
	map.setZoom(11);
	map.setCenter([114.17281,22.659404]);
	map.setIsHotspot = true;
	map.keyboardEnable = true;  
    
    //为地图注册click事件获取鼠标点击出的经纬度坐标
    var getlnglatevent = map.on('click', function(e){
        document.getElementById('longitude').value = e.lnglat.getLng();
        document.getElementById('latitude').value = e.lnglat.getLat(); 
    });
	/*
	 * 导航栏右侧按钮
	*/
	function rightBarAnimate(ele) {
		var flag = 1;
	    $(ele).click(function() {
	      if((flag=!flag))
	        $('.right-bar').animate({right: '0px'});
	      else
	        $('.right-bar').animate({right: '-380px'});  
	    });
	}
	rightBarAnimate('.right-menu');

	/************************************
	*
	* 引用laydate 控件
	***********************************/
	   /*
	* 参数设置：
	* elem:   显示日期的元素id
	* format：日期格式，有：YYYY-MM-DD hh:mm:ss, YYYY-MM-DD
	* 
	*/
    var startDate = {
       elem:   '#startDate',
	   format: 'YYYY-MM-DD',
	   min: '2015-01-01',
       max:    laydate.now(),
       choose: function(datas) {
           endDate.min = datas;
           endDate.start = datas;
       }
       
   };
    var endDate = {
       elem:   '#endDate',
	   format: 'YYYY-MM-DD',
	   min: '2015-01-01',
       max: laydate.now()
	};
    
   laydate(startDate);
   laydate(endDate);
   /*
	 * 计算指定日期n天后的日期
	 */
	function adddays(starDate, interval) {  
	    var now = new Date(starDate);  
	    var newdate = new Date();  
	    var sumtimes = now.getTime()+(interval*24*60*60*1000);  
	    newdate.setTime(sumtimes);  
	    
	    var endDate = "'" + newdate.getFullYear() + "-" + (newdate.getMonth()+1) + "-" + newdate.getDate() + "'";
	    return {
	    	sumtimes: sumtimes,
	    	endDate: endDate
	   };
	}
  /*
   *  表单验证函数
   */
   function checkDate() {
   	//  验证日期的正则表达式
		var reg = /((^((1[8-9]\d{2})|([2-9]\d{3}))(-)(10|12|0?[13578])(-)(3[01]|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))(-)(11|0?[469])(-)(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))(-)(0?2)(-)(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)(-)(0?2)(-)(29)$)|(^([3579][26]00)(-)(0?2)(-)(29)$)|(^([1][89][0][48])(-)(0?2)(-)(29)$)|(^([2-9][0-9][0][48])(-)(0?2)(-)(29)$)|(^([1][89][2468][048])(-)(0?2)(-)(29)$)|(^([2-9][0-9][2468][048])(-)(0?2)(-)(29)$)|(^([1][89][13579][26])(-)(0?2)(-)(29)$)|(^([2-9][0-9][13579][26])(-)(0?2)(-)(29)$))/;
		var startDate = document.getElementById('startDate'),
		      endDate  = document.getElementById('endDate'),
		      result = $(startDate).val().match(regs) && $(endDate).val().match(regs) ;
		
		if($(startDate).val() == null || $(endDate).val() == null)
		{
			alert('fail');
			return false;
		}
		
		if(result)
		{
			altert('输入格式不正确，请按yyyy-MM-dd 的格式输入！');
		   		return false;
		}
		else
	   	{
	   		return true;
	   	}
   }
   
   // 查询表单验证    
   $('#form-time').submit(function () {
	   var startDate = $('#startDate').val(),
			endDate  = $('#endDate').val(),
			startTime = $('#startTime').val(),
			endTime = $('#endTime').val();

	   if(startDate > endDate)
		{
			alert('开始日期不能晚于结束日期，请重新选择或输入！');
			return false;
		}
	   if(startDate.length == 0 || endDate.length == 0)
		{
			alert('请输入正确的日期格式，如：2016-01-01');
	   		return false;
	   	}
	   	if(startTime>endTime){
	   		alert('开始时间不能晚于结束时间，请重新选择！');
			return false;
	   	}	   	
	   	return true;
   }); 
   // 添加热点表单验证
   $('#primary-add-btn').submit(function() {
	   alter(111);
	   
	   if($('#keyvalue').val() == null || $('#longitude').va() == null || $('#latitude').val()== null ) {
		   	return false;
	   }
	   
	   return true;
   });
   /*
    * 区域select change event
    * 
    */  
   function changeMapCenter(zoneValue) {
	   //如果所选区域跟查询区域不同，则创建新地图
	   if(zoneValue != initialDistrict){
		  // 重新创建地图
		   map = new AMap.Map('container');				
		   map.setIsHotspot = true;
		   map.keyboardEnable = true;
		   
		   districtChanged = true; //标识区域改变过
		//如果所选区域跟查询区域不同，并且区域被改变过，则重新加载查询的信息
	   }else if(districtChanged){
		   districtChanged = false;
		   location.reload();
	   }
 
	   var districtName = '深圳市';
	   var districtListIndex = 0;
	   // 罗湖
	   if( zoneValue == 1)
	   {
		   map.setZoom(13);
		   map.setCenter([114.162465,22.577489]);
		   districtName = '罗湖区';
	   }
	   else if( zoneValue == 2 )
	   {
		   map.setZoom(13);
		   map.setCenter([114.073621,22.546442]);
		   districtName = '福田区';
	   }
	   else if(zoneValue == 3)
	   {
		   map.setZoom(12);
		   map.setCenter([113.979421,22.553533]);
		   districtName = '南山区';
		   districtListIndex = 1;
	   }
	   else if(zoneValue == 4)
	   {
		   map.setZoom(11);
		   map.setCenter([114.349996,22.657376]);
		   districtName = '龙岗区';
	   }
	   else if(zoneValue == 5)
	   {
		   map.setZoom(11);
		   map.setCenter([113.990836,22.717279]);
		   districtName = '宝安区';
	   }
	   else if(zoneValue == 6)
	   {
		   map.setZoom(13);
		   map.setCenter([114.288154,22.598628]);
		   districtName = '盐田区';
	   }
	   else if(zoneValue == 7)
	   {
		   map.setZoom(11);		  
		   map.setCenter([114.17281,22.659404]);
		   districtName = '深圳市';
	   } 
	  // addBorder(districtName,districtListIndex);
   }
   
   function addBorder(districtName, districtListIndex) {	   
        //加载云图层插件
        AMap.service('AMap.DistrictSearch', function() {
            var opts = {
                subdistrict: 0,   //返回下一级行政区
                extensions: 'all',  //返回行政区边界坐标组等具体信息
                level: 'district'  //查询行政级别为 区
            };

            //实例化DistrictSearch
            var district = new AMap.DistrictSearch(opts);
            //district.setLevel('district');
            //行政区查询
            district.search(districtName, function(status, result) {
                var bounds = result.districtList[districtListIndex].boundaries;
                var polygons = [];
                if (bounds) {
                    for (var i = 0, l = bounds.length; i < l; i++) {
                        //生成行政区划polygon
                        var polygon = new AMap.Polygon({
                            map: map,
                            strokeWeight: 3,
                            path: bounds[i],
                            fillOpacity: 0,
                            fillColor: 'rgb(0,100,200)',
                            strokeColor: 'rgb(0,100,200)'
                        });
                        polygons.push(polygon);
                    }
                    //map.setFitView();//地图自适应
                }
            });
        });
    }
   $('#district').change(function() {
	   var zoneValue= $(this).children('option:selected').val();
	   changeMapCenter(zoneValue);
	   
   });
   /*
    * 自定义重点区域功能
    */
   // 自定义热点表单
   function deleteSpot(lng, lat, currentRow) {
       var url = "http://localhost:8080/gpsTrackPlatForm/hotspotManageServlet?" + 
                 "spotName=&longitude=" + lng + "&latitude=" +
                 lat + "&excuteCode=delete";

       $.get(url, function (data, status) {
           console.log(data.valueOf());
           if(data.dao_code == 3) {
               currentRow.remove(); 
               alert(data.msg);
           }
       });
   }

   function addSpot(name, lng, lat, tr) {
       var url = "http://localhost:8080/gpsTrackPlatForm/hotspotManageServlet?" + 
                 "spotName=" + name + "&longitude=" + lng + "&latitude=" +
                 lat + "&excuteCode=add";
       $.get(url, function (data, status) {
           
           console.log(data.valueOf());
           // alert(data.table_row);
           if(data.dao_code == 1)
           {
               $('#tb-hotpoint-info tbody').append(tr);
               alert(data.msg); 
               return 1; 
           }
           
           if(data.dao_code == 0)
           {
               alert(data.msg);
               return 1;
           }
           if (data.dao_code == 6) {
                alert(data.msg);
               return 1;
           }
       });
   }
   $('#primary-add-btn').click(function(e) {

	     e.preventDefault();
	     var hotpointName = $.trim($('#keyvalue').val()),
	         longitude = $('#longitude').val(),
	         latitude = $('#latitude').val();

	     if (hotpointName.length == 0 || longitude.length == 0 || latitude.length == 0) {
	       alert('请输入自定义热点信息!');
	       return false;
	     }
	     else
	     {
	       var tr = "<tr><td>" + hotpointName + "</td><td>" + longitude + "</td><td>" + 
	                latitude + "</td><td>" + "<button type='button' class='btn btn-mini btn-danger'>删除</button></td></tr>";

	       addSpot(hotpointName, longitude, latitude, tr);
	     }
	     return false;
   });
   // delete row
   $('table').on('click', 'button', function(evetn) {
       var lng = $(this).parent().prev().prev().text(),
           lat = $(this).parent().prev().text(),
           currentRow = $(this).parent().parent();
       if(event.returnValue = confirm("删除是不可恢复的，你确认要删除吗？"))
       {
           deleteSpot(lng, lat, currentRow);
       }
   });

  $('#hotpoint-opt').on('click', function() {
	  if((!$('#tb-hotpoint-info tbody tr').length))
	  {
		  $.get('http://localhost:8080/gpsTrackPlatForm/hotspotManageServlet?spotName=&longitude=&latitude=&excuteCode=', function(data, status) {
	
		        if(data.length != 0) {
		          $('#tb-hotpoint-info tbody tr').remove();        
		        }
		        $('#tb-hotpoint-info tbody').append(data.table_row);
		  });  
	  }
  });  
