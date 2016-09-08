 /*************************************
     *
     * echart.js
     * 
     ************************************/
    var blankTaxiChart = echarts.init(document.getElementById('blank_taxi')),
        hotPointChart  = echarts.init(document.getElementById('hotPoints_taxi')),
        trendTaxiChart = echarts.init(document.getElementById('trend_taxi'));
    var pieOption = {
        title : {
            text: '出租车空车比例',
            subtext: '数据来源深圳市GPS监管平台',
            x:'center',
            textStyle: {
                color: '#fff'
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
                color: '#fff'
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
                            max: 1548
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
                center: ['50%', '60%'],
                data:[
                    {value: 450, name: '空车'},
                    {value: 1500, name: '重车'}
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
                  color: '#fff'
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
             color: '#fff'
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
                color: '#fff'
              }
           }
       },
       yAxis: {
           type: 'category',
           data: [
                    {
                      value: '深圳北站',
                      textStyle: {
                         color: '#fff'
                      }
                    },
                    {
                      value: '机场东',
                      textStyle: {
                         color: '#fff'
                      }
                    },
                    {
                      value: '福田站',
                      textStyle: {
                         color: '#fff'
                      }
                    },
                    {
                      value: '深圳东站',
                      textStyle: {
                         color: '#fff'
                      }
                    },
                    {
                      value: '深圳站',
                      textStyle: {
                         color: '#fff'
                      }
                    },
                    {
                      value: '深圳西站',
                      textStyle: {
                         color: '#fff'
                      }
                    }    
           ],
       },
       series: [
           {
               name: '2015年',
               type: 'bar',
               data: [ 5000, 10000, 15000, 20000, 25000, 30000, 35000 ]
           },
       ]
    };
    var lineOption = {
        title: {
               text: '出租车一周空重走势',
               subtext: '测试数据',
               x: 'center',
               textStyle: {
                   color: '#fff'
               }
           },
       tooltip: {
           trigger: 'axis'
       },
       legend: {
           orient: 'vertical',
           x: 'left',
           data:['重车','空车'],
           textStyle: {
              color: '#fff'
           }
       },
       toolbox: {
           show: true,
           orient: 'vertical',
           feature : {
               mark : {show: false},
               dataView : {show: true, readOnly: false},
               magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
               restore : {show: true},
               saveAsImage : {show: true}
           }
       },
       xAxis:  {
           type: 'category',
           boundaryGap: false,
           data: ['周一','周二','周三','周四','周五','周六','周日'],
           axisLabel: {
               textStyle: {
                   color: '#fff'
               }
           }
       },
       yAxis: {
           type: 'value',
           axisLabel: {
               textStyle: {
                  color: '#fff'
               }
           }
       },
       series: [
           {
               name:'重车',
               type:'line',
               data:[100, 1200, 200, 1540, 260, 830, 710],
               itemStyle: {normal: {areaStyle: {type: 'default'}}}
               /*markPoint: {
                   data: [
                       {type: 'max', name: '最大值'},
                       {type: 'min', name: '最小值'}
                   ]
               },
               markLine: {
                   data: [
                       {type: 'average', name: '平均值'}
                   ]
               }*/
           },
           {
               name:'空车',
               type:'line',
               data:[30, 182, 434, 791, 390, 30, 10],
               itemStyle: {normal: {areaStyle: {type: 'default'}}}
           }
     ]
   };

    blankTaxiChart.setOption(pieOption);  
    hotPointChart.setOption(heatMapOption);
    trendTaxiChart.setOption(lineOption);