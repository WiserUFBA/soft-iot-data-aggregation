# FoT-Gateway-Data-Aggregation

## Introduction

Module of SOFT-IoT plataform to aggregate data in FoT-Gateway. It collects non-aggregated data from internal database, applies functions of aggregation and stores resulted data in internal database.

## Installation

This module depends of modules [fot-gateway-mapping-devices](https://github.com/WiserUFBA/fot-gateway-mapping-devices) and [fot-gateway-local-storage](https://github.com/WiserUFBA/fot-gateway-local-storage). They need to be installed and started before FoT-Gateway-Data-Aggregation.

To install this bundle using our custom maven support execute the following commands in Karaf Shell:

```sh
config:edit org.ops4j.pax.url.mvn 
config:property-append org.ops4j.pax.url.mvn.repositories ", https://github.com/WiserUFBA/wiser-mvn-repo/raw/master/releases@id=wiser"
config:update
mvn:br.ufba.dcc.wiser.soft_iot/fot-gateway-mapping-devices/1.0.0
mvn:br.ufba.dcc.wiser.soft_iot/fot-gateway-local-storage/1.0.0
bundle:install -s mvn:br.ufba.dcc.wiser.soft_iot/fot-gateway-data-aggregation/1.0.0
```
FoT-Gateway-Data-Aggregation has a configuration file (*br.ufba.dcc.wiser.soft_iot.data_aggregation.cfg*), where is possible set information about the time fo execute aggregation procedure and configure the aggregation function for each sensor.

Finally, for correct execution of module you need copy the file:
```
fot-gateway-data-aggregation/src/main/resources/br.ufba.dcc.wiser.soft_iot.data_aggregation.cfg
```
to:
```
<servicemix_directory>/etc
```


## Deploy to Maven Repo

To deploy this repo into our custom maven repo, change pom according to the new version and after that execute the following command. Please ensure that both wiser-mvn-repo and this repo are on the same folder.

```sh
mvn -DaltDeploymentRepository=release-repo::default::file:../wiser-mvn-repo/releases/ deploy
```



## Support and development

<p align="center">
	Developed by Leandro Andrade at </br>
  <img src="https://wiki.dcc.ufba.br/pub/SmartUFBA/ProjectLogo/wiserufbalogo.jpg"/>
</p>
