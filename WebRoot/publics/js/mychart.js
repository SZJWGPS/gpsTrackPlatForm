jQuery(document).ready(function() {
    var points = gpsJson.sandiantu;
    var getlnglatevent;
 
    /*
     * 标记点使用的图标
     * redIcon: 重车
     * whiteIcon: 空车
     */
    
    var blueIcon = new AMap.Icon({
        image: 'http://webapi.amap.com/theme/v1.3/markers/n/mark_b.png',
        anchor: new AMap.Pixel(0, 0),
        size: new AMap.Size(5, 5),
    });
    var whiteIcon = new AMap.Icon({
        image: 'http://webapi.amap.com/theme/v1.3/markers/n/mark_r.png',
        anchor: new AMap.Pixel(0, 0),
        size: new AMap.Size(5, 5)
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
        anchor: new AMap.Pixel(0, 0),
        size: new AMap.Size(5, 7),
        opacity: 0.9,
        cursor: 'pointer',
        zIndex: 999
    });
    mass.setMap(map);

    /*
     * 热点坐标数据
     * 机场坐标：113.82122, 22.626752
     * 深圳北站：114.035529,22.615107
     */
    var hotPointsInfo = gpsJson.point.normal.spot,
        // 热点动画实体        
        hotPointMarkers = [];
      
    /*
     * heatmap 热力图
     */
    var heatmapData = gpsJson.heatmap,
        heatmapLength = gpsJson.heatmap.length,
        max = gpsJson.heatmapmax,
        heatmap;
    /*
     * 生成heatmap point 数据
     */
     
    /*for(var i=0; i < heatmapLength; i++) {
        max = Math.max(max, heatMap[i][2]);
        
        heatmapData.push({
          'lng': heatMap[i][0],
          'lat': heatMap[i][1],
          'count': heatMap[i][2]
        });
    }
     */
    
    /*
     * 创建heatmap实例
     */
    map.plugin(["AMap.Heatmap"], function() {
        //初始化heatmap对象
        heatmap = new AMap.Heatmap(map, {
            radius: 10,
            opacity: [0, 0.5],            
            gradient:{
                '0.25': 'rgba(0,0,255, 0.35)',
                '0.35': 'rgba(117,211,248, 0.5)',
                '0.45': 'rgba(0, 255, 0, 0.6)',
                '0.85': 'rgba(255, 234, 0, 0.7)',
                '1.0':  'rgba(255, 0, 0, 9.0)'
            }
        });
        // 设置热度图数据
        heatmap.setDataSet({
            data: heatmapData,
            min: 1,
            max: max*3
        });
        heatmap.hide();
    });
    
    // 功能按钮：空重车、热点、走势
    $('.btn-groups .echart-btn').click(function() {
        var len = hotPointsInfo.length,
        index = $(this).index(),
            i = 0;
        //移除地图的click事件的监听
        // map.on('click', getLnglat);
        
        
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
    // 图表数据、自定义热点按钮切换
    $('.options > li').click(function() {
   		var index = $(this).index();

   		$('.options > li').removeClass();
        $(this).addClass('active-echart-opt');

   		if (index == 0) {
   			$('.hotpoint').css('display', 'none');
   			$('.echart-data').css('display', 'block');
   		}
   		else
   		{
   			$('.echart-data').css('display', 'none');
   			$('.hotpoint').css('display', 'block');
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

    
    /*************************************
    *
    * echart init option
    * 
    ************************************/
  	var blankTaxiChart = echarts.init(document.getElementById('blank_taxi')),
  	    hotPointChart  = echarts.init(document.getElementById('hotPoints_taxi')),
  	    trendTime = echarts.init(document.getElementById('trend_time')),
  	    trendDay = echarts.init(document.getElementById('trend_date')),
  	    trendWeek = echarts.init(document.getElementById('trend_week'));
  	
  	var pieOption = {
  	    title : {
  	        text: '空重车比例',
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
  	        data: ['重车', '空车']
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
  	                        max: gpsJson.onoffValue[0] + gpsJson.onoffValue[1]
  	                       // max: 1500
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
//      	            name:'访问来源',
  	            type:'pie',
  	            radius : '50%',
  	            center: ['50%', '50%'],
	  	          itemStyle : {
	                  normal : {
	                      label : {
	                          position : 'inner',
	                          formatter : function (params) {                         
	                            return (params.percent - 0).toFixed(0) + '%'
	                          }
	                      },
	                      labelLine : {
	                          show : false
	                      }
	                  },
	                
	                  emphasis : {
	                      label : {
	                          show : true,
	                          formatter : "{b}\n{d}%"
	                      }
	                  } 
	            },
  	            data:[
  	                {value: gpsJson.onoffValue[1] , name: '空车'},
  	                {value: gpsJson.onoffValue[0], name: '重车'}
  	            ]
  	        }
  	    ]
  	};
  
  	var heatMapOption = {
  	    title: {
  	           text: '重点区域',
//      	           subtext: '数据来源深圳市GPS监管平台',
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
  	       data: gpsJson.point.normal.name
  	   },
  	   series: [
  	       {
  	           name: '2015年',
  	           type: 'bar',
  	           // point.normal.name
  	           // point.normal.value  	           
  	           data: gpsJson.point.normal.value
  	       },
  	   ]
  	};
  	var lineOptionByHours = {
  	    title: {
  	           text: '上客走势',
  	           subtext: '数据来源深圳市GPS监管平台',
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
  	       data: gpsJson.trendchart.byhourframe.xAxi,
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
  	           data: gpsJson.trendchart.byhourframe.data,
  	           itemStyle: {normal: {areaStyle: {type: 'default'}}}
  	       }
  	 ]
  	};
  	 var lineOptionByDay = {
  	     title: {
  	            text: '上客走势',
  	            subtext: '数据来源深圳市GPS监管平台',
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
  	        data: gpsJson.trendchart.byday.xAxi,
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
  	            data: gpsJson.trendchart.byday.data,
  	            itemStyle: {normal: {areaStyle: {type: 'default'}}}
  	        }
  	  ]
  	};
  	var lineOptionByWeek = {
  	  	     title: {
  	  	            text: '上客走势',
  	  	            subtext: '数据来源深圳市GPS监管平台',
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
  	  	        data: gpsJson.trendchart.byweek.xAxi,
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
  	  	            data: gpsJson.trendchart.byweek.data,
  	  	            itemStyle: {normal: {areaStyle: {type: 'default'}}}
  	  	        }
  	  	  ]
  	  	};
	blankTaxiChart.setOption(pieOption);  
	hotPointChart.setOption(heatMapOption);
	trendTime.setOption(lineOptionByHours);
	trendDay.setOption(lineOptionByDay);
	trendWeek.setOption(lineOptionByWeek);
});