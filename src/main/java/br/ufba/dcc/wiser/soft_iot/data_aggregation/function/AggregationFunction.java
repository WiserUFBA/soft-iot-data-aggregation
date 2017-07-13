package br.ufba.dcc.wiser.soft_iot.data_aggregation.function;


import java.util.List;

import br.ufba.dcc.wiser.soft_iot.entities.SensorData;

public interface AggregationFunction {
	
	public List<SensorData> execute(List<SensorData> listSensorData);

}
