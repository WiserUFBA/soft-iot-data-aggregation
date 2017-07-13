package br.ufba.dcc.wiser.soft_iot.data_aggregation.function;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.ufba.dcc.wiser.soft_iot.entities.SensorData;

public class AverageAggregationFunction implements AggregationFunction{
	
	
	public List<SensorData> execute(List<SensorData> listSensorData){
		List<SensorData> resultList = new ArrayList<SensorData>();
		Date startDate =  listSensorData.get(0).getStartTime();
		Date endDate =  startDate;
		float average = 0;
		for(SensorData sensorData : listSensorData){
			endDate = sensorData.getEndTime();
			if(Utils.isInteger(sensorData.getValue())){
				average += Integer.parseInt(sensorData.getValue());
			}else if (Utils.isFloat(sensorData.getValue())){
				average += Float.parseFloat(sensorData.getValue());
			}
		}
		average = average / listSensorData.size();
		String value;
		if(Utils.isInteger(listSensorData.get(0).getValue())){
			value = String.valueOf(Math.round(average));
		}else{
			value = String.valueOf(average);
		}
		SensorData aggSensorData = new SensorData(listSensorData.get(0).getDevice(), listSensorData.get(0).getSensor(),
				value,startDate, endDate);
		resultList.add(aggSensorData);
		return resultList;
	}
	
	

}
