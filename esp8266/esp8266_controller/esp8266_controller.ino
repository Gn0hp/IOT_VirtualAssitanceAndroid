#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <WebSocketsServer.h>
#include <Hash.h>
#include <WiFiUdp.h>
#include <Adafruit_NeoPixel.h>
#include "DHT.h"


const int LED_ON=1;
const int LED_OFF=2;
const int MUSIC_MODE_ON=3;
const int MUSIC_MODE_OFF=4;
const int LED_WARN_ON=5;
const int LED_WARN_OFF=6;

int ledStripMode = LED_ON;

// ------------- SETUP for WS2812-----------------------
// DEFINE PIN = GPI3 (USE RX)
#define PIN 3
// Set to the number of LEDs in your LED strip
#define NUM_LEDS 30
// Maximum number of packets to hold in the buffer. Don't change this.
#define BUFFER_LEN 1024
// Toggles FPS output (1 = print FPS over serial, 0 = disable output)
#define PRINT_FPS 0

// ------------- SETUP for DHT11-----------------------
#define DHTTYPE DHT11

WiFiClient client;
const int DHTPin = 5;
const int channelID = 1855735; //
const char* dhtServer = "api.thingspeak.com";
String writeAPIKey = "43ABY00L7T9D5X6R";
const int postingInterval = 2 * 1000; // post data every 2 seconds

DHT dht(DHTPin, DHTTYPE);
// ------------------------ END SETUP ------------------------------

// Wifi and socket settings
const char* ssid     = "";
const char* password = "";
unsigned int localPort = 7777;
char packetBuffer[BUFFER_LEN];



Adafruit_NeoPixel ledstrip = Adafruit_NeoPixel(30, PIN, NEO_GRB + NEO_KHZ800);

WiFiUDP port;
WiFiServer server(80);

// Network information
// IP must match the IP in config.py
//IPAddress ip(192, 168, 1, 98);
// Set gateway to your router's gateway
//IPAddress gateway(192, 168, 1, 1);
//IPAddress subnet(255, 255, 255, 0);

void setup() {
    pinMode(3, FUNCTION_0); 
    Serial.begin(115200);
    //WiFi.config(ip, gateway, subnet);
    WiFi.mode(WIFI_STA);
    WiFi.begin(ssid, password);
    Serial.println("");
    // Connect to wifi and print the IP address over serial
    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.print(".");
    }
     server.begin();
    Serial.println("");
    Serial.print("Connected to ");
    Serial.println(ssid);
    Serial.print("IP address: ");
    Serial.println(WiFi.localIP());
    port.begin(localPort);
    // ledstrip.init(NUM_LEDS);
    ledstrip.begin();
    dht.begin();
    ledStripMode = LED_ON;
}

uint8_t N = 0;
#if PRINT_FPS 
    uint16_t fpsCounter = 0;
    uint32_t secondTimer = 0;
#endif

void loop() {
  dht11Handle();
    if(ledStripMode == LED_ON ) {
      turnOnLed();
    }else if(ledStripMode == MUSIC_MODE_ON) {
      ws2812WithMusic();
    } else if(ledStripMode== LED_WARN_ON){
      ledWarn();
    }else if(ledStripMode == LED_OFF){
      for(int i=0;i<NUM_LEDS;++i){
        ledstrip.setPixelColor(i,0,0,0);
      }
      ledstrip.show();
    }
  getRequestString(); 
}
void turnOnLed() {
    for(int i=0;i<30; ++i)
      ledstrip.setPixelColor(i, 255,255,255);
    ledstrip.show();
}
void ledWarn(){
  for(int i=0;i<30; ++i)
    ledstrip.setPixelColor(i, 210,43,43);
  ledstrip.show();
  delay(1000);
}
void ws2812WithMusic() {
  // Read data over socket
    int packetSize = port.parsePacket();
    // If packets have been received, interpret the command
    if (packetSize) {
        int len = port.read(packetBuffer, BUFFER_LEN);
        for(int i = 0; i < len; i+=4) {
            packetBuffer[len] = 0;
            N = packetBuffer[i];
            ledstrip.setPixelColor(N, (uint8_t)packetBuffer[i+1],(uint8_t)packetBuffer[i+2],(uint8_t)packetBuffer[i+3]);
        } 
        ledstrip.show();
        #if PRINT_FPS
            fpsCounter++;
        #endif
    }
    #if PRINT_FPS
        if (millis() - secondTimer >= 1000U) {
            secondTimer = millis();
            Serial.printf("FPS: %d\n", fpsCounter);
            fpsCounter = 0;
        }   
    #endif  
}

void dht11Handle() {
    float temp = dht.readTemperature();
    float tempF = dht.readTemperature(true);
    float humi = dht.readHumidity();
    if(WiFi.status() == WL_CONNECTED){
      if (isnan(temp) || isnan(humi)) {
              Serial.println("Failed to read from DHT sensor!");
              return;
      }
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
    client.stop();}
}

// ----------------------------------- GET REQUEST STRING FOR MODE USING-------------------------------
// 1 - turn on led
// 2- led with music
// 3 - led with fire warning
void getRequestString(){  
  WiFiClient client = server.available();
  if (!client){
    return;
  }
    
  Serial.println("Waiting for new client");
  while(!client.available())
  {
    delay(1);
  }
  String request = client.readStringUntil('\r');
  Serial.println(request);
  client.flush();
    delay(1);
  if(request.indexOf("/LED=ON") != -1){
    ledStripMode= LED_ON;
  }else if(request.indexOf("/LED=OFF")!= -1){
    ledStripMode=LED_OFF;
  }
  else if(request.indexOf("/MUSIC=ON") != -1){
    ledStripMode= MUSIC_MODE_ON;
  }else if(request.indexOf("/LED=WARN")!= -1){
    ledStripMode= LED_WARN_ON;
  }  
  Serial.println("Client disonnected");
  Serial.println("");
}

