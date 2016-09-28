$(document).ready(function() {
	var map = new AMap.Map('container');
	var keyword_btn = $('#key-word');
	// 地图加载初始化设置
	map.setZoom(11);
	map.setCenter([114.002756, 22.641421]);
	map.setIsHotspot = true;
	map.keyboardEnable = true;


	//为地图注册click事件获取鼠标点击出的经纬度坐标
  var clickEventListener = map.on('click', function(e) {
      document.getElementById("lnglat").value = e.lnglat.getLng() + ',' + e.lnglat.getLat()
  });
  

  // AMap plugin
  AMap.plugin('AMap.Autocomplete',function(){//回调函数
      //实例化Autocomplete
      var autoOptions = {
          city: "shen'zh", //城市，默认全国
          input:"key-word"//使用联想输入的input的id
      };
      autocomplete= new AMap.Autocomplete(autoOptions);
      //TODO: 使用autocomplete对象调用相关功能
  });



  AMap.event.addListener(autocomplete, "select", select);//注册监听，当选中某条记录时会触发
  /*
   * define function 
   */
  function select(e) {
      if (e.poi && e.poi.location) {
          map.setZoom(15);
          map.setCenter(e.poi.location);
      }
  }
});
