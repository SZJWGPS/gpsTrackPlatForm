var heatmapData = [];

for(var i=0; i<1000; i++) {
	heatmapData.push({
		'lng': 113.81 + Math.random()*(0.4),
		'lat': 22.52 + Math.random()*(0.3),
		'count': 1 + Math.random(100)
	})
}