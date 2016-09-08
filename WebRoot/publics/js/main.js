jQuery(document).ready(function() {
    var map = new AMap.Map('container');

    // 地图加载初始化设置
    map.setZoom(11);
    map.setCenter([114.002756, 22.641421]);
    map.setIsHotspot = true;
    map.keyboardEnable = true;

    // point对象，应该能够表示出车的位置及状态
    /*
     * point = {
     *   lnglats: []
     * }
     */ 
    var points = [];
  
    if(sanDianTu!=null){
    	var SDlength = sanDianTu.length;
    	
    	for(var i=0; i<SDlength; i++) {
			points.push({
         		'lnglat': sanDianTu[i]
        	});        
    	}
    }
    /*
     * 标记点使用的图标
     * redIcon: 重车
     * whiteIcon: 空车
     */
    var blueIcon = new AMap.Icon({
        image: 'http://webapi.amap.com/theme/v1.3/markers/n/mark_b.png',
        anchor: new AMap.Pixel(0, 0),
        size: new AMap.Size(5, 10),
    });
    var whiteIcon = new AMap.Icon({
        image: 'http://webapi.amap.com/theme/v1.3/markers/n/mark_r.png',
        anchor: new AMap.Pixel(0, 0),
        size: new AMap.Size(5, 10)
    });
    /*
     * - AMap.Marker类,用于标记兴趣点
     * - 设计标记点，传入一个对象参数
     * - position：经纬度位置
     * - map：标记点所在的Map对象（即在哪个容器内展现）
     */

    /*
     * 加载海量点
     * new AMap.MassMark(pointsArray, {param})
     */
    var mass = new AMap.MassMarks(points, {
        url: './publics/images/mark_b.png',
        anchor: new AMap.Pixel(3, 7),
        size: new AMap.Size(5, 7),
        opacity: 0.7,
        cursor: 'pointer',
        zIndex: 999
    });
    mass.setMap(map);

    /*
     * 热点坐标数据
     * 机场坐标：113.82122, 22.626752
     * 深圳北站：114.035529,22.615107
     */
    var hotPointsInfo = [
                      {
                        lnglat: [113.82122, 22.626752],
                        name:   '机场'
                      },
                      {
                        lnglat: [114.035529,22.615107],
                        name:   '深圳北站'
                      },
                      {
                        lnglat: [113.907886,22.527173],
                        name:   '深圳西站'
                      },
                      {
                        lnglat: [114.120691,22.601403],
                        name:   '深圳东站'
                      },
                      {
                        lnglat: [114.117249,22.531815],
                        name:   '深圳站'
                      },
                      {
                        lnglat: [114.069911,22.515577],
                        name:   '福田口岸'
                      },
                      {
                        lnglat: [113.912348,22.476597],
                        name:   '蛇口港'
                      },
                      {
                        lnglat: [114.075009,22.520228],
                        name:   '皇岗口岸'
                      }
                    ],
        // 热点动画实体        
        hotPointMarkers = [];
      
    /*
     * heatmap 热力图
     */
    var heatmapData = [],
        heatmapLength = heatMap.length,
        max = 0,
        heatmap;
    /*
     * 生成heatmap point 数据
     */
     
    for(var i=0; i < heatmapLength; i++) {
        max = Math.max(max, heatMap[i][2]);
        
        heatmapData.push({
          'lng': heatMap[i][0],
          'lat': heatMap[i][1],
          'count': heatMap[i][2]
        });
    }

    /*
     * 创建heatmap实例
     */
    map.plugin(["AMap.Heatmap"], function() {
        //初始化heatmap对象
        heatmap = new AMap.Heatmap(map, {
            radius: 25,
            opacity: [0, 0.75],
            gradient:{
                '0.25': 'blue',
                '0.35': 'rgba(117,211,248, .6)',
                '0.45': 'rgba(0, 255, 0, .7)',
                '0.85': '#ffea00',
                '1.0':  'red'
            }
        });
        // 设置热度图数据
        heatmap.setDataSet({
            data: heatmapData,
            min: 1,
            max: max*5
        });
        heatmap.hide();
    });
    
    /*
     * 区域聚焦函数，点击区域按钮，地图中心切换到该区
     * focusDistrict(id, x, y)
     * id：按钮元素id
     * x, y: 点的经纬度坐标
     */
    /*function focusDistrict(id, x, y, zoomNumber) {
        var zoom = zoomNumber || 12;
        AMap.event.addDomListener(document.getElementById(id), 'click', function() {
            map.setZoomAndCenter(zoom, [x, y]);
            //new AMap.Marker({
            //    map: map,
            //    position: [x, y]
            //});
        });
    }
    focusDistrict('sz', 114.082678, 22.542459, 13);
    focusDistrict('luohu', 114.136657, 22.546173);
    focusDistrict('futian', 114.056319, 22.520486);
    focusDistrict('nanshan', 113.930663, 22.533172);
    focusDistrict('baoan', 113.887576, 22.55402);
    focusDistrict('yantian', 114.23622, 22.55513);
    focusDistrict('longG', 114.247006, 22.72073);
    focusDistrict('longH', 114.038804, 22.674003);
    focusDistrict('guangM', 113.944587, 22.760555);
    focusDistrict('pingS', 114.350834, 22.687593);
    focusDistrict('dapeng', 114.480904, 22.588997);
    */
    /*
     * **************************
     * 激活当前按钮，改变背景色
     * **************************
     */
    // 区域选择按钮
    $('.zone-groups .item').click(function() {
        $('.zone-groups .item').removeClass().addClass('item');
        $(this).addClass('btn-actived');
    });
    
    // 功能按钮：空重车、热点、走势
    $('.btn-groups .echart-btn').click(function() {
        var len = hotPointsInfo.length,
        index = $(this).index(),
            i = 0;
        $('.btn-groups .echart-btn').removeClass().addClass('echart-btn');
        $(this).addClass('btn-actived');
        
        // 饼图和柱状图切换       
        $('.echart-box > div').css('display', 'none');
        $($('.echart-box > div')[index]).css('display', 'block');
        
        // 点击上下客比按钮
        if(index == 0) {
            for(i = 0; i < len; i++) {
                hotPointMarkers[i].hide();
            }

            heatmap.hide();
            mass.show();
        } 
        // 点击热度图按钮
        // show heatmap when clikc up-and-down hotpoint btn
        if(index == 1) {
            mass.hide();
            heatmap.show();
            // create and show hotpoint marker
            if(hotPointMarkers.length == 0) {
              for(i = 0; i < len; i++) {
                  hotPointMarkers.push(new AMap.Marker({
                    content: "<div class='pointAnimate'><span class='s-cir'></span><span class='b-cir'></span>" + 
                             "<span class='addrName'>" + hotPointsInfo[i].name + "</span>" + "</div>",
                    position: hotPointsInfo[i].lnglat,
                    map: map
                  }));
              }
            }
            else 
            {
                for(i = 0; i < len; i++) {
                    hotPointMarkers[i].show();
                }
            }
        }
        // 点击走势图按钮        
        if(index == 2) {
            $('#mask-layer').css({'opacity': '1','z-index': '1'});
        }
    });
    
    /*
     * 走势中遮罩层处理
     */ 
    $('#mask-layer').bind('click', function(e) {
        var e = e || window.event; 
        var elem = e.target || e.srcElement;
        var blankTaxiBtn = $('.btn-groups .echart-btn')[0];

        // 点击遮罩层，则隐藏走势图
        if (elem.id && elem.id == 'mask-layer') {
          $('#mask-layer').css({'opacity': '0','z-index': '-999'});
          // 空重车按钮转为选中状态
          $('.btn-groups .echart-btn').removeClass().addClass('echart-btn');
          $(blankTaxiBtn).addClass('btn-actived');

          // 切换到空重车比例视图
          $('.echart-box > div').css('display', 'none');
          $('#blank_taxi').css({'display': 'block'});

          heatmap.hide();
          mass.show();
        }
    });

    /*
     * 走势图 按天计算(#ByDay), 按小时计算(#ByTime)
     */
    $('#select-groups > span').click(function(e) {
        var target = $(this);
        $('#select-groups > span').removeClass();
        $(target).addClass('actived');
    });
    $('#select-groups > span').click(function() {
        $('#trend_chart_box > div').css('display', 'none');
        $($('#trend_chart_box > div')[$(this).index()]).css('display', 'block');
    });

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
        max:    laydate.now(),
        choose: function(datas) {
            console.log(typeof(datas));
            endDate.min = datas;
            endDate.start = datas;
        }
    }
    var endDate = {
        elem:   '#endDate',
        format: 'YYYY-MM-DD',
        max:    laydate.now()
    }

    laydate(startDate);
    laydate(endDate);
    
    /*
     * 初始化日期显示框
     * 默认显示的是今天到前一个月的今天
     */
    
    /*
     * 导航栏右侧按钮
     */
    function rightBarAnimate(ele) {
    	var flag = 1;
        $(ele).click(function() {
          if((flag=!flag))
            $('.echarts').animate({right: '10px'});
          else
            $('.echarts').animate({right: '-380px'});  
        });
    }
    rightBarAnimate('.right-menu');
    
    /*************************************
    *
    * echart init option
    * 
    ************************************/
  	var blankTaxiChart = echarts.init(document.getElementById('blank_taxi')),
  	    hotPointChart  = echarts.init(document.getElementById('hotPoints_taxi')),
  	    trendTime = echarts.init(document.getElementById('trend_time')),
  	    trendDay = echarts.init(document.getElementById('trend_date'));
  	
  	var pieOption = {
  	    title : {
  	        text: '出租车上下客比例',
  	        subtext: '数据来源深圳市GPS监管平台',
  	        x:'center',
  	        textStyle: {
  	            color: '#333'
  	        }
  	    },
  	    tooltip : {
  	        trigger: 'item',
  	        formatter: "{a} <br/>{b} : {c} ({d}%)"
  	    },
  	    legend: {
  	        orient : 'vertical',
  	        x : 'left',
  	        textStyle: {
  	            color: '#333'
  	        },
  	        data: ['空车', '重车']
  	    },
  	    toolbox: {
  	        show : true,
  	        orient: 'vertical',
  	        feature : {
  	            mark : {show: false},
  	            magicType : {
  	                show: true, 
  	                type: ['pie', 'funnel'],
  	                option: {
  	                    funnel: {
  	                        x: '25%',
  	                        width: '50%',
  	                        funnelAlign: 'left',
  	                        max: onOff[1] + onOff[0]
  	                    }
  	                }
  	            },
  	            restore : {show: true},
  	            saveAsImage : {show: true}
  	        }
  	    },
  	    calculable : true,
  	    series : [
  	        {
  	            name:'访问来源',
  	            type:'pie',
  	            radius : '50%',
  	            center: ['50%', '50%'],
  	            data:[
  	                {value: onOff[1], name: '空车'},
  	                {value: onOff[0], name: '重车'}
  	            ]
  	        }
  	    ]
  	};
  	var heatMapOption = {
  	    title: {
  	           text: '深圳市出租车热点区域',
  	           subtext: '测试数据',
  	           x: 'center',
  	           textStyle: {
  	              color: '#333'
  	           }
  	       },
  	   toolbox: {
  	      show: true,
  	      orient: 'vertical',
  	      feature : {
  	          mark : {show: false},
  	          magicType : {
  	              show: true, 
  	              type: ['bar', 'line']
  	          },
  	          restore : {show: true},
  	          saveAsImage : {show: true}
  	      }
  	   },
  	   tooltip: {
  	       trigger: 'axis',
  	       axisPointer: {
  	           type: 'shadow'
  	       }
  	   },
  	   legend: {
  	       data: ['2015年'],
  	       orient: 'vertical',
  	       x: 'left',
  	       textStyle: {
  	         color: '#333'
  	       }
  	   },
  	   grid: {
  	       left: '0%',
  	       right: '1%',
  	       bottom: '3%',
  	       containLabel: true
  	   },
  	   xAxis: {
  	       type: 'value',
  	       position: 'bottom',
  	       boundaryGap: [0, 0.01], 
  	       axisLabel: {
  	          textStyle: {
  	            color: '#333'
  	          }
  	       }
  	   },
  	   yAxis: {
  	       type: 'category',
  	       data: [
  	                {
  	                  value: '深圳站',
  	                  textStyle: {
  	                     color: '#333'
  	                  }
  	                },
  	                {
  	                  value: '深圳西站',
  	                  textStyle: {
  	                     color: '#333'
  	                  }
  	                },
  	                {
  	                  value: '福田口岸',
  	                  textStyle: {
  	                     color: '#333'
  	                  }
  	                },
  	                {
  	                  value: '蛇口港',
  	                  textStyle: {
  	                     color: '#333'
  	                  }
  	                },
  	                {
  	                  value: '皇岗口岸',
  	                  textStyle: {
  	                     color: '#333'
  	                  }
  	                },
  	                {
  	                  value: '深圳东站',
  	                  textStyle: {
  	                     color: '#333'
  	                  }
  	                },
  	                {
  	                  value: '深圳北站',
  	                  textStyle: {
  	                     color: '#333'
  	                  }
  	                },
  	                {
  	                  value: '机场东',
  	                  textStyle: {
  	                     color: '#333'
  	                  }
  	                }
  	       ],
  	   },
  	   series: [
  	       {
  	           name: '2015年',
  	           type: 'bar',
  	           data: [ 5000, 10000, 15000, 18000, 20000, 20000, 25000, 30000, 35000 ]
  	       },
  	   ]
  	};
  	var lineOptionByHours = {
  	    title: {
  	           text: '出租车重车走势',
  	           subtext: '测试数据',
  	           x: 'center',
  	           textStyle: {
  	               color: '#333'
  	           }
  	       },
  	   tooltip: {
  	       trigger: 'axis'
  	   },
  	   toolbox: {
  	       show: true,
  	       orient: 'vertical',
  	       feature : {
  	           mark : {show: false},
  	           dataView : {show: true, readOnly: false},
  	           magicType : {show: true, type: ['line', 'bar']},
  	           restore : {show: true},
  	           saveAsImage : {show: true}
  	       }
  	   },
  	   xAxis:  {
  	       type: 'category',
  	       boundaryGap: false,
  	       data: trendchart_time_xAxi,
  	       axisLabel: {
  	           textStyle: {
  	               color: '#333'
  	           }
  	       }
  	   },
  	   yAxis: {
  	       type: 'value',
  	       axisLabel: {
  	           textStyle: {
  	              color: '#333'
  	           }
  	       }
  	   },
  	   series: [
  	       {
  	           name:'重车',
  	           type:'line',
  	           data: trendchart_time_Data,
  	           itemStyle: {normal: {areaStyle: {type: 'default'}}}
  	       }
  	 ]
  	};
  	 var lineOptionByDay = {
  	     title: {
  	            text: '出租车重车走势',
  	            subtext: '测试数据',
  	            x: 'center',
  	            textStyle: {
  	                color: '#333'
  	            }
  	        },
  	    tooltip: {
  	        trigger: 'axis'
  	    },
  	    toolbox: {
  	        show: true,
  	        orient: 'vertical',
  	        feature : {
  	            mark : {show: false},
  	            dataView : {show: true, readOnly: false},
  	            magicType : {show: true, type: ['line', 'bar']},
  	            restore : {show: true},
  	            saveAsImage : {show: true}
  	        }
  	    },
  	    xAxis:  {
  	        type: 'category',
  	        boundaryGap: false,
  	        data: trendchart_day_xAxi,
  	        axisLabel: {
  	            textStyle: {
  	                color: '#333'
  	            }
  	        }
  	    },
  	    yAxis: {
  	        type: 'value',
  	        axisLabel: {
  	            textStyle: {
  	               color: '#333'
  	            }
  	        }
  	    },
  	    series: [
  	        {
  	            name:'重车',
  	            type:'line',
  	            data: trendchart_day_Data,
  	            itemStyle: {normal: {areaStyle: {type: 'default'}}}
  	        }
  	  ]
  	};
	blankTaxiChart.setOption(pieOption);  
	hotPointChart.setOption(heatMapOption);
	trendTime.setOption(lineOptionByHours);
	trendDay.setOption(lineOptionByDay);
})