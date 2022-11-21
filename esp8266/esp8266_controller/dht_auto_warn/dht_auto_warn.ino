#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <DHT.h>
#include <Adafruit_NeoPixel.h>
#include "DHT.h"

#define DHTTYPE DHT11
// ----------------------------- LED AUTO WARN WHEN HUMID AND TEMP CHANGE SUDDENLY---------------
const float TEMP_TO_WARN = 45;
const float HUMID_TO_WARN= 80;


const char* ssid = "";
const char* password ="";
Adafruit_NeoPixel ledstrip = Adafruit_NeoPixel(30, PIN, NEO_GRB + NEO_KHZ800);

WiFiClient client;
WiFiServer server(8191);

const int DHTPin = 5;
const int channelID = 1855735; //
const char* dhtServer = "api.thingspeak.com";
String writeAPIKey = "43ABY00L7T9D5X6R";
const int postingInterval = 5 * 1000; // post data every 5 seconds

bool state = false;
void setup() {
  Serial.begin(115200);
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  Serial.println("");
  
  while(WiFi.status() != WL_CONNECTED){
    delay(500);
    Serial.print(".");
  }
  server.begin();
  Serial.println("");
  Serial.print("Connected to: ")
  Serial.println(ssid)
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
    ledstrip.begin();
    state=false;
    dht.begin();
}

void ledWarnOn(){
  for(int i=0;i<30; ++i)
    ledstrip.setPixelColor(i, 255,0,0); 
    ledstrip.show();
}
void dht11Handle(){
  float temp = dht.readTemperature();
  float tempF = dht.readTemperature(true);
  float humi = dht.readHumidity();
  if (isnan(temp) || isnan(humi)) {
          Serial.println("Failed to read from DHT sensor!");
          return;
  }
  if(temp > TEMP_TO_WARN && humi > HUMID_TO_WARN){
    if(!state) {
      state=true;
    }
  }
  else state=false;
  if (client.connect(dhtServer, 80)) {
          // Construct API request body
          String body = "field1=" + String(temp, 1) + "&field2=" + String(tempF, 1) + "&field3=" + String(humi, 1);
          client.print("POST /update HTTP/1.1\n");
          client.print("Host: api.thingspeak.com\n");
          client.print("Connection: close\n");
          client.print("X-THINGSPEAKAPIKEY: " + writeAPIKey + "\n");
          client.print("Content-Type: application/x-www-form-urlencoded\n");
          client.print("Content-Length: ");
          client.print(body.length());
          client.print("\n\n");
          client.print(body);
          client.print("\n\n");
          Serial.printf("Nhiet do %s - Do am %s\r\n", String(temp, 1).c_str(), String(humi, 1).c_str());
  }
  
  client.stop();
}
void loop() {
  dht11Handle();
  if(state){
    ledWarnOn();
  }
  delay(500);
}
