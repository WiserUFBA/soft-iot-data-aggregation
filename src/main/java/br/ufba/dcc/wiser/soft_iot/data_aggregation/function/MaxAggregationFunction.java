package br.ufba.dcc.wiser.soft_iot.data_aggregation.function;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.ufba.dcc.wiser.soft_iot.entities.SensorData;

public class MaxAggregationFunction implements AggregationFunction {

	@Override
	public List<SensorData> execute(List<SensorData> listSensorData) {
		List<SensorData> resultList = new ArrayList<SensorData>();
		Date startDate = listSensorData.get(0).getStartTime();
		Date endDate = startDate;
		float max = Float.parseFloat(listSensorData.get(0).getValue());
		for (SensorData sensorData : listSensorData) {
			endDate = sensorData.getEndTime();
			if (max <= Float.parseFloat(sensorData.getValue())) {
				if (Utils.isInteger(sensorData.getValue())) {
					max = Integer.parseInt(sensorData.getValue());
				} else if (Utils.isFloat(sensorData.getValue())) {
					max = Float.parseFloat(sensorData.getValue());
				}
			}

		}
		String value;
		if (Utils.isInteger(listSensorData.get(0).getValue())) {
			value = String.valueOf(Math.round(max));
		} else {
			value = String.valueOf(max);
		}
		SensorData aggSensorData = new SensorData(listSensorData.get(0).getDevice(), listSensorData.get(0).getSensor(),
				value, startDate, endDate);
		resultList.add(aggSensorData);
		return resultList;

	}

}
