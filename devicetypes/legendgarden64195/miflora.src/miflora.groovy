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
import groovy.transform.Field
import java.text.DateFormat

metadata {
	definition (name: "MiFlora", namespace: "legendgarden64195", author: "Sven Heuer", cstHandler: true) {
        capability "Temperature Measurement"
        capability "legendgarden64195.moistureMeasurement"
        capability "Illuminance Measurement"
        capability "streamorange58819.fertility"
	}

	simulator {
	}
    
    preferences {
	}

    tiles{
        valueTile("illuminance", "device.illuminance", decoration: "flat", width: 2, height: 1) {
            state "default", label:'${currentValue}'
        }
        valueTile("fertility", "device.fertility", decoration: "flat", width: 2, height: 1) {
            state "default", label:'${currentValue}'
        }
        valueTile("moisture", "device.moisture", decoration: "flat", width: 2, height: 1) {
            state "default", label:'${currentValue}'
        }
        valueTile("temperature", "device.temperature", decoration: "flat", width: 2, height: 1) {
            state "default", label:'${currentValue}'
        }
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
}

def setStatus(params){
	log.debug "${params.key} : ${params.data}"
    def data = new JsonSlurper().parseText(params.data)
    if(data.temperature != null){
        sendEvent(name:"temperature", value: data.temperature)
    }
    if(data.moisture != null){
        sendEvent(name:"moisture", value: data.moisture)
    }
    if(data.lux != null){
        sendEvent(name:"illuminance", value: data.lux)
    }
    if(data.fertility != null){
        sendEvent(name:"fertility", value: data.fertility)
    }
    updateLastTime()
}

def updated() {
    setLanguage(settings.selectedLang)
}

def updateLastTime(){
	def now = new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone)
    sendEvent(name: "lastCheckin", value: now)
}
