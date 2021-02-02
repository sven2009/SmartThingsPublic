/**
 *  MiFlora
 *
 *  Copyright 2021 Sven Heuer
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 */
import groovy.json.JsonSlurper
import java.text.DateFormat

metadata {
	definition (name: "MiFlora", namespace: "legendgarden64195", author: "Sven Heuer", cstHandler: true) {
		capability "Battery"
		capability "Illuminance Measurement"
		capability "Signal Strength"
		capability "Temperature Measurement"
        capability "legendgarden64195.moistureMeasurement"
	}


	simulator {
		// TODO: define status and reply messages here
	}

	tiles {
		// TODO: define your main and details tiles here
	}
    preferences {
		input name: "temperatureType", title:"Temperature" , type: "enum", required: true, options: ["C", "F"], defaultValue: "C"
        input name: "illuminanceType", title:"Illuminance" , type: "enum", required: true, options: ["lux", "µmol/(s·m²)"], defaultValue: "lux"
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
	// TODO: handle 'battery' attribute
	// TODO: handle 'illuminance' attribute
	// TODO: handle 'lqi' attribute
	// TODO: handle 'rssi' attribute
	// TODO: handle 'temperature' attribute

}
def setInfo(String app_url, String id) {
	state.app_url = app_url
    state.id = id
}

def setLanguage(language){}
def setExternalAddress(address){}

def setStatus(params){
	log.debug "${params.key} : ${params.data}"
    
    def data = new JsonSlurper().parseText(params.data)
    
    if(data.temperature != null){
        sendEvent(name:"temperature", value: makeTemperature(data.temperature), unit:temperatureType == null ? "C" : temperatureType)
    }
    if(data.moisture != null){
        sendEvent(name:"moisture", value: data.moisture, unit:"%")
    }
    if(data.lux != null){
        sendEvent(name:"illuminance", value: makeIlluminance(data.lux), unit:illuminanceType == null ? "lux" : illuminanceType)
    }
    if(data.fertility != null){
        sendEvent(name:"fertility", value: data.fertility, unit:"μS/cm")
    }
}

def makeTemperature(temperature){
	if(temperatureType == "F"){
    	return ((temperature * 9 / 5) + 32)
    }else{
    	return temperature
    }
}

def makeIlluminance(illuminance){
	if(temperatureType == "lux"){
    	return (illuminance)
    }else{
    	return (illuminance * 18 / 1000)
    }
}
def updated() {}
