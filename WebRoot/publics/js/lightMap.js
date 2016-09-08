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
	            // dataView : {show: true, readOnly: false},
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