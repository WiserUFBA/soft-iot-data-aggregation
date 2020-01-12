package br.ufba.dcc.wiser.soft_iot.data_aggregation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import br.ufba.dcc.wiser.soft_iot.data_aggregation.function.*;
import br.ufba.dcc.wiser.soft_iot.entities.Device;
import br.ufba.dcc.wiser.soft_iot.entities.Sensor;
import br.ufba.dcc.wiser.soft_iot.entities.SensorData;
import br.ufba.dcc.wiser.soft_iot.local_storage.LocalDataController;
import br.ufba.dcc.wiser.soft_iot.mapping_devices.Controller;

public class DataAggregationTime {
	

	private Controller fotDevices;
	private LocalDataController localDataController;
	private int aggregationTimeInterval;
	private String strFunctionBysensor;
	private HashMap<String, Date> lastDateSensor;
	private boolean debugModeValue;
	
	
	public void init(){
		this.lastDateSensor = new HashMap<String, Date>();
		JSONArray jsonArrayAggregationFunc = new JSONArray(this.strFunctionBysensor);
		for (int i = 0; i < jsonArrayAggregationFunc.length(); i++){
			JSONObject jsonFuncSensor = jsonArrayAggregationFunc.getJSONObject(i);
			Device device = fotDevices.getDeviceById(jsonFuncSensor.getString("device_id"));
			Sensor sensor = device.getSensorbySensorId(jsonFuncSensor.getString("sensor_id"));
			String function = jsonFuncSensor.getString("function");
			Date lastDate = localDataController.getLastDateOfAggregatedSensorData(device, sensor, function);
			if(lastDate == null){
				lastDate = new GregorianCalendar(1900, Calendar.JANUARY, 1).getTime();
				localDataController.createFirstLastSensorDataAggregated(device, sensor, lastDate, function);
			}
			this.lastDateSensor.put(device.getId() + "_" + sensor.getId() + "_"
					+ function, lastDate);
		}
	}

	
	public void aggregateData(){
		printlnDebug("Starting aggregation procedure...");
		JSONArray jsonArrayAggregationFunc = new JSONArray(this.strFunctionBysensor);
		for (int i = 0; i < jsonArrayAggregationFunc.length(); i++){
			JSONObject jsonFuncSensor = jsonArrayAggregationFunc.getJSONObject(i);
			Device device = fotDevices.getDeviceById(jsonFuncSensor.getString("device_id"));
			Sensor sensor = device.getSensorbySensorId(jsonFuncSensor.getString("sensor_id"));
			String function = jsonFuncSensor.getString("function");
			Date lastDate = this.lastDateSensor.get(device.getId() + "_" + sensor.getId() + "_"
					+ function);
			printlnDebug("<sensor: " + sensor.getId() + " device: " + device.getId() +
					" function: "+ function + " last_date: " + lastDate + ">");
			List<SensorData> sensorData = 
					localDataController.getSensorDataByAggregationStatusAndDate(device, sensor, 0, lastDate);
			if(!sensorData.isEmpty()){
				Date lastAggregatedDate = aggregationByFunction(sensorData, function);
				if (lastAggregatedDate != null){
					localDataController.updateLastSensorDataAggregated(device, sensor, lastAggregatedDate, function);
					this.lastDateSensor.put(device.getId() + "_" + sensor.getId() + "_"
							+ function, lastAggregatedDate);
				}
			}
		}
	}
	
	private Date aggregationByFunction(List<SensorData> listSensorData, String functionName){
		Date lastDate = null;
		for (int i=0; i < listSensorData.size(); i++){
			SensorData sensorData = listSensorData.get(i);
			List<SensorData> aggregationListSensorData = new ArrayList<SensorData>();
			aggregationListSensorData.add(sensorData);
			long initialInterval = sensorData.getStartTime().getTime();
			for(i=i+1; i < listSensorData.size(); i++){
				SensorData nextSensorData = listSensorData.get(i);
				if ((nextSensorData.getStartTime().getTime() - initialInterval) <=  (this.aggregationTimeInterval * 1000)){
					aggregationListSensorData.add(nextSensorData);
				}else{
					i--;
					try {
						Object aggregationFunction = Class.forName("br.ufba.dcc.wiser.soft_iot.data_aggregation.function." + functionName).newInstance();
						List<SensorData> resultList = callFunction(((AggregationFunction)aggregationFunction), aggregationListSensorData);
						if(!resultList.isEmpty()){
							localDataController.insertSensorDataAggregated(resultList,1, functionName);
							/*printlnDebug("<start_date: " + resultList.get(0).getStartTime() 
									+ " end_date: " + resultList.get(0).getEndTime()
									+" value: " + resultList.get(0).getValue()+ " function_name: " + functionName + ">");
									*/
							lastDate = resultList.get(0).getEndTime();
						}
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}							
					break;
				}
			}
		}
		return lastDate;
	}
	
	private String getFunctionNameBySensor(Sensor sensor, Device device){
		String funcName = null;
		JSONArray jsonArrayDevices = new JSONArray(this.strFunctionBysensor);
		for (int i = 0; i < jsonArrayDevices.length(); i++){
			JSONObject jsonFuncSensor = jsonArrayDevices.getJSONObject(i);
			if (jsonFuncSensor.getString("device_id").contentEquals(device.getId()) &&
					jsonFuncSensor.getString("sensor_id").contentEquals(sensor.getId())){
				funcName = jsonFuncSensor.getString("function");
			}
		}
		return funcName;
	}
	
	private List<SensorData> callFunction(AggregationFunction function, List<SensorData> listSensorData){
		return function.execute(listSensorData);
	}
	
	private void printlnDebug(String str){
		if (debugModeValue)
			System.out.println(str);
	}

	public void setStrFunctionBysensor(String strFunctionBysensor) {
		this.strFunctionBysensor = strFunctionBysensor;
	}

	public void setAggregationTimeInterval(int aggregationTimeInterval) {
		this.aggregationTimeInterval = aggregationTimeInterval;
	}

	public void setFotDevices(Controller fotDevices) {
		this.fotDevices = fotDevices;
	}


	public void setLocalDataController(LocalDataController localDataController) {
		this.localDataController = localDataController;
	}


	public void setDebugModeValue(boolean debugModeValue) {
		this.debugModeValue = debugModeValue;
	}
	
	
	
}
