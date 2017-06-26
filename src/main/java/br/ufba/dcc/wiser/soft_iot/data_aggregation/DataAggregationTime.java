package br.ufba.dcc.wiser.soft_iot.data_aggregation;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.ufba.dcc.wiser.soft_iot.entities.Device;
import br.ufba.dcc.wiser.soft_iot.entities.Sensor;
import br.ufba.dcc.wiser.soft_iot.entities.SensorData;
import br.ufba.dcc.wiser.soft_iot.local_storage.LocalDataController;
import br.ufba.dcc.wiser.soft_iot.mapping_devices.Controller;

public class DataAggregationTime {
	
	private String fusekiURI;
	private String baseURI;
	private String nameSpacePrefix;
	private HashMap<String, Date> lastDateSensor;
	private Controller fotDevices;
	private LocalDataController localDataController;
	private boolean debugModeValue;

	
	
}
