//시간 설정
function setTime(){
	$('#timeOption').toggle();
}

//0~12의 숫자 입력(시간)
function hourSetting(value){
	
	if(value>=12){					//12시 이상 입력 하면 12로 설정
		$('#hour').val(12);
	} else if(value>0 && value<12){
		if(value<10){				//두자리 이하면 앞에 '0'설정
			value = "0" + value;
			$('#hour').val(value);
		}
	} else {
		$('#hour').val("00");		//문자 or 0이하의 값은 모두 00으로 설정
	};
	changeTime();

}
//0~59의 숫자 입력(분)
function minuteSetting(value){
	if(value>=59){					//59분 이상 입력 하면 59로 설정
		$('#minute').val(59);
	} else if(value>0 && value<59){
		if(value<10){				//두자리 이하면 앞에 '0'설정
			value = "0" + value;
			$('#minute').val(value);
		}
	} else {
		$('#minute').val("00");		//문자 or 0이하의 값은 모두 00으로 설정
	};
	changeTime();
}

//+1시간
function upHour(){
	var hour = 0;
	if(typeof $('#hour').val()=="string"){			//처음 넘어오는 value는 string이라서 number로 바꿔준다.
		hour = Number($('#hour').val());
	}
	hour = hour + 1;
	if(hour<10){									//한자리 숫자는 앞에 '0'추가해서 두자리 맞춰주기 위함
		hour = "0" + hour;
		$('#hour').val(hour);
		changeTime();
	} else if(hour>12){								//12시가 넘어가면 다시 00으로 맞춰준다.
		hour = "00";
		$('#hour').val(hour);
		changeTime();
	}
	$('#hour').val(hour);
	changeTime();
	
}
//-1시간
function downHour(){
	var hour = 0;
	if(typeof $('#hour').val()=="string"){			//처음 넘어오는 value는 string이라서 number로 바꿔준다.
		hour = Number($('#hour').val());
	}
	hour = hour - 1;
	if(hour<0){										//0보다 밑이면 다시 12시로 설정
		hour = 12;
		$('#hour').val(hour);
		changeTime();
	} else {								
		if(hour<10){									//한자리 숫자는 앞에 '0'추가해서 두자리 맞춰주기 위함
			hour = "0" + hour;
			$('#hour').val(hour);
			changeTime();
		}
	}
	$('#hour').val(hour);	
	changeTime();
}
//+10분
function upMinute(){
	var minute = 0;
	if(typeof $('#minute').val()=="string"){			//처음 넘어오는 value는 string이라서 number로 바꿔준다.
		minute = Number($('#minute').val());
	}
	minute = minute + 10;
	if(minute<10){									//한자리 숫자는 앞에 '0'추가해서 두자리 맞춰주기 위함
		minute = "0" + minute;
		$('#minute').val(minute);	
		changeTime();
	} else if(minute>50){								//12시가 넘어가면 다시 00으로 맞춰준다.
		minute = "00";
		$('#minute').val(minute);
		changeTime();
	}
	$('#minute').val(minute);	
	changeTime();
}
//-10분
function downMinute(){
	var minute = 0;
	if(typeof $('#minute').val()=="string"){			//처음 넘어오는 value는 string이라서 number로 바꿔준다.
		minute = Number($('#minute').val());
	}
	minute = minute - 10;
	if(minute<0){										//0보다 밑이면 다시 12시로 설정
		minute = 50;
		$('#minute').val(minute);
		changeTime();
	} else {								
		if(minute<10){									//한자리 숫자는 앞에 '0'추가해서 두자리 맞춰주기 위함
			minute = "0" + minute;
			$('#minute').val(minute);	
			changeTime();
		}
	}
	$('#minute').val(minute);
	changeTime();
	
}



//변경된 출발 시간 text value로 저장
function changeTime(){
	var hour = $('#hour').val();
	var min = $('#minute').val();
	var ampm = $('#ampm').val();

	$('#startTime').val(hour + ":" + min + ampm);
}

//00:00 보여주기 용도 
$(function time(){
	var hour = $('#hour').val();
	var min = $('#minute').val();
	var ampm = $('#ampm').val();
	$('#startTime').val(hour + ":" + min + ampm);
})