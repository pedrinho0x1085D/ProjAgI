


var estadoSensores = {};
var mapaLeituras = {};
var gauges = {};
var estaOn = false;
var gogogo = 0;
var atual = "";
var jaComecou = 0;
var contaWarnings = 0;
var lat;
var lng;

String.prototype.capitalize = function() {
    return this.charAt(0).toUpperCase() + this.slice(1);
}



function converteMapa(nomeSensor){
	var array = [];
	if(mapaLeituras[nomeSensor] != null && mapaLeituras[nomeSensor].length > 0){
		var tamanho = mapaLeituras[nomeSensor].length;
		
		for(var i = tamanho-1; i>=0;i--){
			var datinha = new Date(mapaLeituras[nomeSensor][i].horaLeitura.year, mapaLeituras[nomeSensor][i].horaLeitura.monthValue, mapaLeituras[nomeSensor][i].horaLeitura.dayOfMonth, mapaLeituras[nomeSensor][i].horaLeitura.hour, mapaLeituras[nomeSensor][i].horaLeitura.minute, mapaLeituras[nomeSensor][i].horaLeitura.second);
			
			var celsius = Math.floor((mapaLeituras[nomeSensor][i].tFarenheit - 32) * 5/9);
			array.push([datinha.getTime(), celsius]);
		}
	}
	return array;
}

function getLastRead(nome){
	var x = new Date(mapaLeituras[nome][0].horaLeitura.year, mapaLeituras[nome][0].horaLeitura.monthValue, mapaLeituras[nome][0].horaLeitura.dayOfMonth, mapaLeituras[nome][0].horaLeitura.hour, mapaLeituras[nome][0].horaLeitura.minute, mapaLeituras[nome][0].horaLeitura.second);
	var y = Math.floor((mapaLeituras[nome][0].tFarenheit - 32) * 5/9);
	
	
	return [x,y];
}

function getEstadoSensores(){
    var url = "http://localhost:9090/estadoSensores";

	
	
    $.get('executaCenas.php', {'link': url}, function(res){
		if(res){
			estaOn = true;
			var json = JSON.parse(res);
        
			$.each(json, function(key, value) {
				var new_key = key.replace(" ","_");
				
				estadoSensores[new_key] = value;
			});
		}
		else {
			estaOn = false;
			$.each(estadoSensores, function(key, value) {
				if(estadoSensores[key] != null){
					estadoSensores[key] = "desconhecido";
				}
			});
		}
        
    });
}
function atualizaGUI(){
	var quadro = $("#quadro-sensores");
	var menu = $("ul.lista_sensores");
	
	contaWarnings = 0;
	$('#warnings').html("");
	
	$.each(estadoSensores, function(key, value) {
		var new_key = key.replace("_"," ");
		$('#'+key+'_gauge').find('.gauge-estado span').text(value);
		//"value": Math.floor((mapaLeituras[key][0].tFarenheit -32) * 5/9)
		
		var coiz;
		
		if(quadro.find('.quadro_'+key+'').length === 0){
			quadro.append('<div class="sensor-info quadro_'+key+'"><div class="gauges" id="'+key+'_gauge"></div></div>');
			gauges[key] = $('#'+key+'_gauge');
		}
		
		else {
			if($('#'+key+'_gauge').find('canvas').length === 0) {
				if(mapaLeituras[key] != null && mapaLeituras[key].length > 0) {
					var celsius = Math.floor((mapaLeituras[key][0].tFarenheit -32) * 5/9);
					
					var fill = "";
					
					var minutos = ""+(mapaLeituras[key][0].horaLeitura.minute);
					if(minutos.length < 2)
						minutos = "0"+minutos;
					
					var segundos = ""+mapaLeituras[key][0].horaLeitura.second;
					if(segundos.length<2)
						segundos = "0"+segundos;
					
					var ultima = mapaLeituras[key][0].horaLeitura.dayOfMonth +" do "+ mapaLeituras[key][0].horaLeitura.monthValue+" de "+mapaLeituras[key][0].horaLeitura.year+", "+mapaLeituras[key][0].horaLeitura.hour+":"+minutos+":"+segundos;
					
					var sum = 0;
					for(var i=0; i<mapaLeituras[key].length;i++){
						sum += (mapaLeituras[key][i].tFarenheit -32) * 5/9;
					}
					var media = Math.floor(sum/mapaLeituras[key].length);
					
					if(celsius >= 5)
						fill = "#E74C3C";
					if(celsius >= 10)
						fill = "#F1C40F";
					if(celsius >= 19)
						fill = "yellowgreen";
					if(celsius >= 23)
						fill = "#F1C40F"
					if(celsius >= 26)
						fill = "#E74C3C";
					
					$('#'+key+'_gauge').append("<span class='gauge-title'>"+new_key+"</span>");
					
					coiz = $("#"+key+"_gauge").tempGauge({
						borderColor: "#dedede",
						borderWidth: 6,
						defaultTemp: celsius,
						fillColor: fill,
						labelSize: 12,
						maxTemp: 40,
						minTemp: -10,
						width:100,
						labelColor:"black",
						showLabel:false
					});
					$('#'+key+'_gauge').append(coiz);
					
					
					if(estadoSensores[key] != null)
						$('#'+key+'_gauge').append("<div class='gauge-info'><div class='gauge-atual'>Temp. Atual: <span>"+celsius+" ºC</span></div><div class='gauge-media'>Temp. Média: <span>"+media+" ºC</span></div><div class='gauge-estado'>Estado do sensor: <span>"+value+"</span></div><div class='gauge-ultima'>Última leitura: <span>"+ultima+"</span></div>");
					else
						$('#'+key+'_gauge').append("<div class='gauge-info'><div class='gauge-atual'>Temp. Atual: <span>"+celsius+" ºC</span></div><div class='gauge-media'>Temp. Média: <span>"+media+" ºC</span></div><div class='gauge-estado'>Estado do sensor: <span>desconhecido</span></div><div class='gauge-ultima'>Última leitura: <span>"+ultima+"</span></div>");
				}
				else {
					
					
					
					$('#'+key+'_gauge').append("<span class='gauge-title'>"+new_key+"</span>");
					
					coiz = $("#"+key+"_gauge").tempGauge({
						borderColor: "#dedede",
						borderWidth: 6,
						defaultTemp: 0,
						fillColor: "transparent",
						labelSize: 12,
						maxTemp: 40,
						minTemp: -10,
						width:100,
						labelColor:"black",
						showLabel:false
					});
					$('#'+key+'_gauge').append(coiz);
					
					
					if(estadoSensores[key] != null)
						$('#'+key+'_gauge').append("<div class='gauge-info'><div class='gauge-atual'>Temp. Atual: <span>desconhecida</span></div><div class='gauge-media'>Temp. Média: <span>desconhecida</span></div><div class='gauge-estado'>Estado do sensor: <span>"+value+"</span></div><div class='gauge-ultima'>Última leitura: <span>desconhecida</span></div>");
					else
						$('#'+key+'_gauge').append("<div class='gauge-info'><div class='gauge-atual'>Temp. Atual: <span>desconhecida</span></div><div class='gauge-media'>Temp. Média: <span>desconhecida</span></div><div class='gauge-estado'>Estado do sensor: <span>desconhecido</span></div><div class='gauge-ultima'>Última leitura: <span>desconhecida</span></div>");
					
				}
			}
			else {
				if(mapaLeituras[key] != null && mapaLeituras[key].length > 0) {
					var celsius = Math.floor((mapaLeituras[key][0].tFarenheit -32) * 5/9);
						
					var fill = "";
					
					var minutos = ""+(mapaLeituras[key][0].horaLeitura.minute);
					if(minutos.length < 2)
						minutos = "0"+minutos;
					
					var segundos = ""+mapaLeituras[key][0].horaLeitura.second;
					if(segundos.length<2)
						segundos = "0"+segundos;
					
					var ultima = mapaLeituras[key][0].horaLeitura.dayOfMonth +" do "+ mapaLeituras[key][0].horaLeitura.monthValue+" de "+mapaLeituras[key][0].horaLeitura.year+", "+mapaLeituras[key][0].horaLeitura.hour+":"+minutos+":"+segundos;
					
					var sum = 0;
					for(var i=0; i<mapaLeituras[key].length;i++){
						sum += (mapaLeituras[key][i].tFarenheit -32) * 5/9;
					}
					var media = Math.floor(sum/mapaLeituras[key].length);
					
					if(celsius > media + 9 || celsius < media - 5)
						contaWarnings++;
					
					if(celsius > media+9)
						$('#warnings').append("<li><span class='hot'>HOT</span><span class=\"text\">Atenção o sensor \""+key+"\" detectou uma temperatura muito elevada!</span></li>");
					else if(celsius < media-5)
						$('#warnings').append("<li><span class='cold'>COLD</span><span class=\"text\">Atenção o sensor \""+key+"\" está a uma temperatura muito baixa para o normal!</span></li>");
					
					
					if(celsius >= 5)
						fill = "#E74C3C";
					if(celsius >= 10)
						fill = "#F1C40F";
					if(celsius >= 19)
						fill = "yellowgreen";
					if(celsius >= 23)
						fill = "#F1C40F"
					if(celsius >= 26)
						fill = "#E74C3C";
				
				
					coiz = $("#"+key+"_gauge").tempGauge({
						borderColor: "#dedede",
						borderWidth: 6,
						defaultTemp: celsius,
						fillColor: fill,
						labelSize: 12,
						maxTemp: 40,
						minTemp: -10,
						width:100,
						labelColor:"black",
						showLabel:false
					});
					$('#'+key+'_gauge').find('canvas').replaceWith(coiz);
					$('#'+key+'_gauge').find('.gauge-atual span').text(celsius+" ºC");
					$('#'+key+'_gauge').find('.gauge-media span').text(media+" ºC");
					$('#'+key+'_gauge').find('.gauge-ultima span').text(ultima);
					$('#'+key+'_gauge').find('.gauge-estado span').text(value);
					
					
				}
			}
		}
		if(menu.find("li#ss_"+key+"").length === 0){
			//menu.append("<li id='ss_"+key+"' class='sensor'><img src='/img/"+value+".png'/><span class='status'>"+new_key+"</span></li>");
			menu.append("<li id='ss_"+key+"' class='sensor'><div class='bolinha'><div class='"+value+"'></div></div><span class='status'>"+new_key+"</span></li>");
			atualizaGUI();
		}
		else{
			menu.find("li#ss_"+key+"").find('.bolinha').find('div').removeClass().addClass(value);
		}
		
		
		

	});
	
	
	if(!$('#individual_page').hasClass('closed')){
		var datinha = new Date(mapaLeituras[atual][0].horaLeitura.year, mapaLeituras[atual][0].horaLeitura.monthValue-1, mapaLeituras[atual][0].horaLeitura.dayOfMonth, mapaLeituras[atual][0].horaLeitura.hour, mapaLeituras[atual][0].horaLeitura.minute, mapaLeituras[atual][0].horaLeitura.second);
					
		var celsius = Math.floor((mapaLeituras[atual][0].tFarenheit - 32) * 5/9);
		$('.spb_chart').highcharts().series[0].addPoint([datinha.getTime(), celsius]);
		
		if($('div.status-ball div').attr('class') !== estadoSensores[atual]) {
			$('div.status-ball div').removeClass().addClass(estadoSensores[atual]);
		
			if(estadoSensores[atual] === "error")
				$('div.titulo span.tit-status').text("(erro de leitura)");
			else if(estadoSensores[atual] === "timedout")
				$('div.titulo span.tit-status').text("(sensor não respondeu)");
			else
				$('div.titulo span.tit-status').text("("+estadoSensores[atual]+")");
		}
	}
	
	/*
	if(Object.keys(estadoSensores).length > 9){
		
		$('body').css('height', ""+parseInt(100 + 15*(Object.keys(estadoSensores).length / 9)) + "%");
	}
	*/
	
}

function getMapaLeituras(){
    var url = "http://localhost:9090/mapaLeituras";

    $.get('executaCenas.php', {'link': url}, function(res){
		if(res){
			estaOn = true;
			var json = JSON.parse(res);
        
			$.each(json, function(key, value) {
				var new_key = key.replace(" ","_");
				mapaLeituras[new_key] = value;
			});
		}
        else {
			estaOn = false;
		}

    });
}
function getMapaAtual(nomeSensor) {
	var arrayzinho = [];
	if(mapaLeituras[nomeSensor] != null) {
		var tamanho = mapaLeituras[nomeSensor].length;

		for(var i = tamanho-1; i>=0;i--){
			var datinha = new Date(mapaLeituras[nomeSensor][i].horaLeitura.year, mapaLeituras[nomeSensor][i].horaLeitura.monthValue-1, mapaLeituras[nomeSensor][i].horaLeitura.dayOfMonth, mapaLeituras[nomeSensor][i].horaLeitura.hour, mapaLeituras[nomeSensor][i].horaLeitura.minute, mapaLeituras[nomeSensor][i].horaLeitura.second);
			
			var celsius = Math.floor((mapaLeituras[nomeSensor][i].tFarenheit - 32) * 5/9);
			arrayzinho.push({x:datinha.getTime(), y:celsius});
		}
	}
	
	return arrayzinho;
}

function getLeituraMaisRecente(nomeSensor) {
	
	var arrayzinho = [];
	if(mapaLeituras[nomeSensor] != null && mapaLeituras[nomeSensor].length > 0) {
		
		var datinha = new Date(mapaLeituras[nomeSensor][0].horaLeitura.year, mapaLeituras[nomeSensor][0].horaLeitura.monthValue, mapaLeituras[nomeSensor][0].horaLeitura.dayOfMonth, mapaLeituras[nomeSensor][0].horaLeitura.hour, mapaLeituras[nomeSensor][0].horaLeitura.minute, mapaLeituras[nomeSensor][0].horaLeitura.second);
								
		var celsius = Math.floor((mapaLeituras[nomeSensor][0].tFarenheit - 32) * 5/9);
		data = [datinha.getTime(), celsius];
	}
	return data;
	
}

function poeOnline(string){
    var url = "http://localhost:9090/botaOnline?nomes="+string;

	
    $.get('mudaEstado.php', {'link': url}, function(res){
		if(res){
			estaOn = true;
			getEstadoSensores();
			getMapaLeituras();
			atualizaGUI();
		}
		else{
			estaOn = false;
		}
    });
}

function poeOffline(string){
    var url = "http://localhost:9090/botaOffline?nomes="+string;

    
    $.get('mudaEstado.php', {'link': url}, function(res){
		if(res){
			estaOn = true;
			getEstadoSensores();
			getMapaLeituras();
			atualizaGUI();
		}
		else{
			estaOn = false;
		}
    });
}

function novoSensor(url){
	$.get('mudaEstado.php', {'link': url}, function(res){
		if(res){
			var json = JSON.parse(res);
			if(json.status === "ok"){
				alert("Sensor adicionado com sucesso.");
				setTimeout(function(){
					getEstadoSensores();
					getMapaLeituras();
					atualizaGUI();
				},1000);
			}
			else if(json.status === "fail"){
				alert("Já existe sensor com esse nome.");
			}
			
		}
		else{
			estaOn = false;
		}
    });
}



function buildChart(){
	$('.spb_chart').highcharts({
		
		chart: {
			type: 'area',
			animation: Highcharts.svg, // don't animate in old IE
			marginRight: 5,
			zoomType: 'x',
			reflow: true,
			
			events: {
				load: function () {
					
					var series = this.series[0];
					
				}
			}
		},
		title: {
			text: 'Leituras do sensor: <b>'+atual.capitalize()+'</b>'
		},
		xAxis: {
			type: 'datetime',
			tickPixelInterval: 120
		},
		yAxis: {
			title: {
				text: 'Temperatura (ºC)'
			},
			plotLines: [{
				value: 0,
				width: 1,
				color: '#808080'
			}]
		},
		tooltip: {
			formatter: function () {
				return '<b>' + this.series.name + '</b><br/>' +
					Highcharts.dateFormat('%d-%m-%Y, %H:%M:%S', this.x) + '<br/>' +
					'<b>'+Highcharts.numberFormat(this.y, 2)+'</b>';
			}
		},
		legend: {
			enabled: false
		},
		exporting: {
			enabled: false
		},
		plotOptions: {
			area: {
				
				fillOpacity: 0.4,
				marker: {
					radius: 2
				},
				lineWidth: 0.5,
				states: {
					hover: {
						lineWidth: 1
					}
				},
				threshold: null
				
			}
		},
		series: [{
			name: 'Leitura',
			data: getMapaAtual(atual),
			color: '#1ABC9C',
			turboThreshold: 0
		}]
	});
}
	

function showLocation(position) {
	lat = position.coords.latitude;
	lng = position.coords.longitude;
	
	
	var url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lng+"&sensor=true";
	console.log(url);
	
	
	//APIKEY -> 6817e4de649f6efdf676509076321d45
	var time = (new Date).toJSON();
	time = time.substring(0,time.length-5);

	
	var url_dois = "https://api.forecast.io/forecast/6817e4de649f6efdf676509076321d45/"+lat+","+lng+","+time+"?units=ca&exclude=hourly,alerts,flags&lang=pt";
	

	
	$.get('locationThings.php', {'link': url}, function(res){
		if(res){
			var jsonRes = JSON.parse(res);
			var dados = jsonRes['results'][2].address_components;
			
			var localidade = "";
			var cidade = "";
			var distrito = "";
			var pais = "";
			
			for(var i=0; i<dados.length;i++){
				if(dados[i].types[0] === "administrative_area_level_3")
					localidade = dados[i].long_name;
				else if(dados[i].types[0] === "administrative_area_level_2")
					cidade = dados[i].long_name;
				else if(dados[i].types[0] === "administrative_area_level_1")
					distrito = dados[i].long_name;
				else if(dados[i].types[0] === "country")
					pais = dados[i].long_name;
			}
			if(localidade === "" || cidade === "")
				dados = jsonRes['results'][3].address_components;
			
			for(var i=0; i<dados.length;i++){
				if(dados[i].types[0] === "administrative_area_level_3")
					localidade = dados[i].long_name;
				else if(dados[i].types[0] === "administrative_area_level_2")
					cidade = dados[i].long_name;
				else if(dados[i].types[0] === "administrative_area_level_1")
					distrito = dados[i].long_name;
				else if(dados[i].types[0] === "country")
					pais = dados[i].long_name;
			}
			
			$('#current-weather .weather-text span.location-atual').html(localidade+", "+cidade+"<br>"+distrito+", "+pais);
			
			$.post('weatherThings.php', {'link': url_dois}, function(result) {
				if(result){
					var temp = JSON.parse(result);
					var tempMinHoje = Math.floor(temp.daily.data[0].temperatureMin)+" ºC";
					var tempMaxHoje = Math.floor(temp.daily.data[0].temperatureMax)+" ºC";
					var probPrecipHoje = Math.floor(temp.daily.data[0].precipProbability * 100) + "%";
					var sumario = temp.currently.summary;
					var icon = temp.currently.icon;
					var tempNow = Math.floor(temp.currently.temperature) + " ºC";
					var humidade = Math.floor(temp.currently.humidity * 100) + "%";
					var probPrecipitacao = Math.floor(temp.currently.precipProbability * 100) + "%";
					var intensidadePrecipitacao = Math.floor(temp.currently.precipIntensity * 100) + "%";
					var windSpeed = temp.currently.windSpeed+ " km/h";
					var coberturaAtual = Math.floor(temp.currently.cloudCover * 100) + "%";
					var sumarioPrevisao = temp.daily.data[0].summary;
					
					//icons: clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night
					var source = "/img/weather-icons/";
					if(icon === "clear-day")
						source += "icon-sunny.png";
					else if(icon === "clear-night")
						source += "icon-moon.png";
					else if(icon === "rain")
						source += "icon-raining.png";
					else if(icon === "snow")
						source += "icon-snowing.png";
					else if(icon === "sleet")
						source += "icon-sleet.png";
					else if(icon === "wind")
						source += "icon-windy.png";
					else if(icon === "fog")
						source += "icon-mist.png";
					else if(icon === "cloudy")
						source += "icon-cloudy.png";
					else if(icon === "partly-cloudy-day")
						source += "icon-cloudysunny.png";
					else if(icon === "partly-cloudy-night")
						source += "icon-cloudymoon.png";
					
				
					$('#current-weather .icon img').attr('src', source);
					$('#current-weather div.weather-text span.temperatura-atual').text(tempNow);
					
					var tempoAtual = $('#weather-pop-up #tempoAtual');
					var previsoesHoje = $('#weather-pop-up #previsoesHoje');
					
					tempoAtual.find('.tempAtual').html('<b>Temperatura atual</b> '+tempNow);
					tempoAtual.find('.sumario').html('<b>Descrição</b> '+sumario);
					tempoAtual.find('.probPrecip').html('<b>Probabilidade de Precipitação</b> '+probPrecipitacao);
					tempoAtual.find('.intensPrecip').html('<b>Intensidade Precipitação</b> '+ intensidadePrecipitacao);
					tempoAtual.find('.windSpeed').html('<b>Velocidade do Vento</b> '+windSpeed);
					tempoAtual.find('.humidade').html('<b>Humidade</b> '+humidade);
					tempoAtual.find('.coberturaNuvens').html('<b>Cobertura de Nuvens</b> '+coberturaAtual);
					
					previsoesHoje.find('.previsaoHoje').html('<b>Previsão</b> '+sumarioPrevisao);
					previsoesHoje.find('.tempMinHoje').html('<b>Temperatura Mínima</b> '+tempMinHoje);
					previsoesHoje.find('.tempMaxHoje').html('<b>Temperatura Máxima</b> '+tempMaxHoje);
					previsoesHoje.find('.probPrecipHoje').html('<b>Probabilidade de Precipitação</b> '+probPrecipHoje);
				}
			});
			
		}
	});
	
 }

function errorHandler(err) {
	if(err.code == 1)
	   alert("Error: Access is denied!");
	else if( err.code == 2)
	   alert("Error: Position is unavailable!");
}

function getLocation(){
	if(navigator.geolocation){
	   var options = {timeout:27000};
	   navigator.geolocation.getCurrentPosition(showLocation, errorHandler, options);
	}
	else
	   alert("Sorry, browser does not support geolocation!");
}


$(document).ready(function(){
    
	getLocation();
	//http://maps.googleapis.com/maps/api/geocode/json?latlng=38.711346999999996,-9.1317797&sensor=true
	
	
	getEstadoSensores();
	getMapaLeituras();
	atualizaGUI();
	setTimeout(function(){
		getEstadoSensores();
		getMapaLeituras();
		atualizaGUI();
	},1000);
	setTimeout(function(){
		getEstadoSensores();
		getMapaLeituras();
		atualizaGUI();
		if(contaWarnings > 0) {
			$('#warning-button  img').addClass("warnings").attr('src','/img/warningz.png');
		}
		else {
			$('#warning-button  img').removeClass("warnings").attr('src','/img/no-warning.png');
		}
	},1000);
	setTimeout(function(){
		getEstadoSensores();
		getMapaLeituras();
		atualizaGUI();
		if(contaWarnings > 0) {
			$('#warning-button  img').addClass("warnings").attr('src','/img/warningz.png');
		}
		else {
			$('#warning-button  img').removeClass("warnings").attr('src','/img/no-warning.png');
		}
	},1000);
	
    
    setInterval(function(){
		console.log("atualizar..");
		getEstadoSensores();
		getMapaLeituras();
		atualizaGUI();
		
		
		if(contaWarnings > 0) {
			$('#warning-button  img').addClass("warnings").attr('src','/img/warningz.png');
		}
		else {
			$('#warning-button  img').removeClass("warnings").attr('src','/img/no-warning.png');
		}
	},11000);
	
	$('button#acrescentar-sensor').click(function(){
		var input = $('input#novo_sensor');
		if(input.val().length > 1){
			var valor = input.val();
			var nome = valor.replace(" ","_");
			var url = "http://localhost:9090/poeAgente?nomeSensor="+nome;
			novoSensor(url);
		}
	});
	
	$("div#new_sensor button").click(function(){
		
		if($("div.add_sensor").hasClass('open')) {
			$(this).removeClass('open');
			$("div.add_sensor").removeClass('open');
			$(this).parent().removeClass('open');
		}
		else {
			$(this).addClass('open');
			$("div.add_sensor").addClass('open');
			$(this).parent().addClass('open');
		}
	});
	
	$("#setinha").click(function(){
		if($(this).text() === ">>")
			$(this).text("<<");
		else
			$(this).text(">>");
		$("#esquerda, #direita, #setinha").toggleClass('closed');
	});
	
	
    
	var cenas = $('li.sensor.selected').length;
	if(cenas === 0){
		$('#btn_ligar').prop('disabled',true);
		$('#btn_desligar').prop('disabled',true);
	}
	else {
		$('#btn_ligar').prop('disabled',false);
		$('#btn_desligar').prop('disabled',false);
	}

	$('ul.lista_sensores').on("click", "li.sensor", function(){
		if($(this).hasClass('selected'))
			$(this).removeClass('selected');
		else
			$(this).addClass('selected');
		
		var tamanho = $('li.sensor.selected').length;
		if(tamanho === 0){
			$('#btn_ligar').prop('disabled',true);
			$('#btn_desligar').prop('disabled',true);
		}
		else {
			$('#btn_ligar').prop('disabled',false);
			$('#btn_desligar').prop('disabled',false);
		}
	});
	
	

	$('#btn_ligar').click(function(){
		var nomes = "";

		var tamanho = $('li.sensor.selected').length;
		

		if(tamanho > 0) {
			var i = 0;
			$('li.sensor.selected').each(function(){
				var nome = $(this).find('span').text().replace(" ","_");
				if(i === tamanho - 1)
					nomes += nome;
				else
					nomes += nome+",";
				estadoSensores[nome] = "online";
				i++;
			});
			$('li.sensor.selected').each(function(){
				$(this).removeClass('selected');
			});
		}

		poeOnline(nomes);

	});

	$('#btn_desligar').click(function(){
		var nomes = "";

		var tamanho = $('li.sensor.selected').length;

		if(tamanho > 0) {
			var i = 0;
			$('li.sensor.selected').each(function(){
				var nome = $(this).find('span').text().replace(" ","_");
				if(i === tamanho - 1)
					nomes += nome;
				else
					nomes += nome+",";
				estadoSensores[nome] = "offline";
				i++;
			});
			$('li.sensor.selected').each(function(){
				$(this).removeClass('selected');
			});
		}

		poeOffline(nomes);
	});
	

	
	$('#go_back').click(function(){
		gogogo = 0;
		$('.spb_chart').highcharts().series[0].setData([]);
		$('.spb_chart').highcharts().redraw();
		$(this).removeClass('show')
		$('#individual_page').addClass('closed');
		$("#esquerda, #direita").removeClass('hidden');
		atual = "";
		$('#setinha').fadeIn("1500");
		$('#warning-button').removeClass('closed');
	});
	
	$('#warning-button').click(function(){
		if($(this).hasClass('pressed'))
			$(this).removeClass('pressed');
		else 
			$(this).addClass('pressed');
		
		if($('#warnings').hasClass('open'))
			$('#warnings, #triangulozinho').removeClass('open');
		else
			$('#warnings, #triangulozinho').addClass('open');
	});
	
	$(document).on('click', '.gauges', function(){
		gogogo = 1;
		$('#warnings').removeClass('open');
		$('#current-weather .icon').removeClass('open');
		$('#weather-pop-up').removeClass('open');
		
		
		$('#warning-button').addClass('closed').removeClass('pressed');
		$('#setinha').fadeOut("1500");
		$('#individual_page').removeClass('closed');
		$("#esquerda, #direita").addClass('hidden');
		$('#go_back').addClass('show');
		
		var nome = $(this).find('.gauge-title').text();
		$('.ip_header .titulo span.tittit').text(nome);
		atual = nome;
		
		
		
		
		if(jaComecou === 0) {
			buildChart();
			jaComecou = 1;
		}
		else {
			//if($('.spb_chart').highcharts().series !== undefined) {
			
			
			$('.spb_chart').highcharts().series[0].setData(getMapaAtual(atual));
			$('.spb_chart').highcharts().setTitle({ text: 'Leituras do sensor <b>'+atual.capitalize()+'</b>'});
			//}
		}
		
	});
	
	$('#current-weather .icon').click(function(){
		if($('#weather-pop-up').hasClass('open')) {
			$('#weather-pop-up').removeClass('open');
			$(this).removeClass('open');
		}
		else {
			$('#weather-pop-up').addClass('open');
			$(this).addClass('open');
		}
	});
	
});



$(document).ajaxComplete(function(){
	
	
});

