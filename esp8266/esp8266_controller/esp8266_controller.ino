#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <WebSocketsServer.h>
#include <Hash.h>
#include <WiFiUdp.h>
#include <Adafruit_NeoPixel.h>
#include "DHT.h"



const int LED_ON = 1;
const int LED_OFF = 2;
const int LED_NTH = 3;
const int LED_WARN_ON = 6;
const int LED_WARN_OFF = 7;
const int LED_NTH_ONLY = 8;

const int TEMP_TO_WARN = 60;
const int HUMID_TO_WARN = 60;

int ledStripMode = LED_ON;

int standardBrightness = 60;
int groupOfLed = 5;

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
const int channelID = 1855735;  //
const char* serverThingspeak = "api.thingspeak.com";
String writeAPIKey = "43ABY00L7T9D5X6R";
const int postingInterval = 2 * 1000;  // post data every 2 seconds

unsigned long lastPostTime = 0;

DHT dht(DHTPin, DHTTYPE);
// ------------------------ END SETUP ------------------------------

// Wifi and socket settings
const char* ssid = "Dung Duong 2.4";
const char* password = "meomeogaugau";
unsigned int localPort = 7777;
char packetBuffer[BUFFER_LEN];
bool state = false;



Adafruit_NeoPixel ledstrip = Adafruit_NeoPixel(30, PIN, NEO_GRB + NEO_KHZ800);

WiFiUDP port;
WiFiServer server(8191);

// Network information
// IP must match the IP in config.py for sending UDP packet to NodeMCU ESP8266
//IPAddress ip(192, 168, 1, 98);  -------------------
// Set gateway to your router's gateway
//IPAddress gateway(192, 168, 1, 1);
//IPAddress subnet(255, 255, 255, 0);

void setup() {
  pinMode(3, FUNCTION_0);
  Serial.begin(230400);

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
  state = false;
  dht.begin();
  ledStripMode = LED_ON;
}

uint8_t N = 0;
#if PRINT_FPS
uint16_t fpsCounter = 0;
uint32_t secondTimer = 0;
#endif

void loop() {
  if (state == true) {
    if (ledStripMode == LED_ON) {
      Serial.println("Turn on Led");
      turnOnLed();
      state = false;
      delay(50);
    } else if (ledStripMode == LED_OFF) {
      Serial.println("Turn off led");
      turnOffLed();
      state = false;
      delay(50);
    } else if (ledStripMode == LED_WARN_ON) {
      state = false;
      Serial.println("Led warn");
      ledWarn();
      delay(50);
    } else if (ledStripMode == LED_WARN_OFF) {
      state = false;
      Serial.println("Led off");
      turnOffLed();
      delay(1);
    }
  }
  dht11Handle();
  getRequestString();
  delay(500);
}
void turnOnLed() {
  for (int i = 0; i < NUM_LEDS; ++i)
    ledstrip.setPixelColor(i, 255, 255, 255);
  ledstrip.setBrightness(standardBrightness);
  ledstrip.show();
}
void turnOffLed() {
  for (int i = 0; i < NUM_LEDS; ++i)
    ledstrip.setPixelColor(i, 0, 0, 0);
  ledstrip.show();
}
void turnNthLed(int n) {
  Serial.print("Turn on ");
  Serial.println(n);
  for (int i = 0; i < NUM_LEDS; ++i) {
    if (i == n) {
      ledstrip.setPixelColor(i, 255, 255, 0);
    }
  }
  ledstrip.setBrightness(standardBrightness);
  ledstrip.show();
}
void turnONNthLeds(int n) {
  Serial.print("Turn on ");
  Serial.println(n);
  for (int i = 0; i < NUM_LEDS; ++i) {
    if (i == n) {
      ledstrip.setPixelColor(i, 255, 255, 0);
    }
  }
  ledstrip.setBrightness(standardBrightness);
  ledstrip.show();
}

void turnOffNthLed(int n) {
  Serial.print("Turn on ");
  Serial.println(n);
  for (int i = 0; i < NUM_LEDS; ++i) {
    if (i == n) {
      ledstrip.setPixelColor(i, 0, 0, 0);
      break;
    }
  }
  ledstrip.show();
}
void ledWarn() {
  for (int i = 0; i < 30; ++i)
    ledstrip.setPixelColor(i, 255, 0, 0);
  ledstrip.setBrightness(standardBrightness);
  ledstrip.show();
  delay(1000);
}


void changeBrightness(int n) {
  ledstrip.setBrightness(standardBrightness + n);
  ledstrip.show();
}
void turnOnWithSpecifyColor(int r, int g, int b) {
  for (int i = 0; i < 30; ++i)
    ledstrip.setPixelColor(i, r, g, b);
  ledstrip.setBrightness(standardBrightness);
  ledstrip.show();
}
void turnOnSpecifyLedWithSpecifyColor(int n, int r, int g, int b) {
  ledstrip.setPixelColor(n, r, g, b);
  ledstrip.setBrightness(standardBrightness);
  ledstrip.show();
}



void dht11Handle() {
  unsigned long currentTime = millis();
  Serial.println("Reading dht11 ...");
  float temp = dht.readTemperature();
  float tempF = dht.readTemperature(true);
  float humi = dht.readHumidity();
  Serial.print("Reading dht11 ...: temp: ");
  Serial.print(temp);
  Serial.print("  humid: ");
  Serial.println(humi);
  if (isnan(temp) || isnan(humi)) {
    Serial.println("Failed to read from DHT sensor!");
    return;
  }
  if (temp > TEMP_TO_WARN && humi > HUMID_TO_WARN) {
    //do something?
    ledWarn();
  }
  if (currentTime - lastPostTime >= (postingInterval) && client.connect(serverThingspeak, 80)) {
    Serial.println("dht11 Posting ...");
    lastPostTime = currentTime;
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

// ----------------------------------- GET REQUEST STRING FOR MODE USING-------------------------------

void getRequestString() {
  WiFiClient client = server.available();
  if (!client) {
    return;
  }

  Serial.println("Waiting for new client");
  while (!client.available()) {
    delay(1);
  }
  String request = client.readStringUntil('\r');
  Serial.println(request);
  client.flush();
  delay(1);
  if (request.indexOf("/LED=ON") != -1) {
    Serial.println("Received");
    ledStripMode = LED_ON;
    state = true;
  } else if (request.indexOf("/LED=OFF") != -1) {
    Serial.println("Received");
    ledStripMode = LED_OFF;
    state = true;
  } else if (request.indexOf("/LED_WARN_ON") != -1) {
    Serial.println("Received");
    ledStripMode = LED_WARN_ON;
    state = true;
  } else if (request.indexOf("/LED_WARN_OFF") != -1) {
    Serial.println("Received");
    ledStripMode = LED_WARN_OFF;
    state = true;
  } else if (request.indexOf("/BRIGHTNESS")  != -1) {
    if (request.indexOf("INCREASE")  != -1) {
      changeBrightness(50);
    } else if (request.indexOf("DECREASE")  != -1) {
      changeBrightness(-50);
    }
    state = false;
  } else if (request.indexOf("/CHANGE_COLOR")  != -1) {
    int indexR = request.indexOf("?r=") + 3;
    Serial.print(indexR);

    int indexG = request.indexOf("&g=") + 3;
    Serial.print(indexG);

    int indexB = request.indexOf("&b=") + 3;

    Serial.print(indexB);
    int r = request.substring(indexR, indexG - 3).toInt();
    int g = request.substring(indexG, indexB - 3).toInt();
    int b = request.substring(indexB).toInt();
    Serial.print(r);
    Serial.print(g);
    Serial.print(b);
    turnOnWithSpecifyColor(r, g, b);
  } else if (request.indexOf("/LED_GROUP")  != -1) {
    Serial.println("go here !");
    int index = request.indexOf("/LED_GROUP/") + 11;
    Serial.print("index group is: ");
    Serial.println(index);
    int ledGroup = request.substring(index).toInt() - 1;
    int tmp = groupOfLed * ledGroup;
    if (request.indexOf("ON")  != -1) {
      for (int i = tmp; i <= tmp + 5; ++i) {
        turnONNthLeds(i);
      }
    } else if (request.indexOf("OFF")  != -1) {
      for (int i = tmp; i <= tmp + 5; ++i) {
        turnOffNthLed(i);
      }
    }

    state = false;
  } else if (request.indexOf("/LED_NTH") != -1) {
    String nth;
    int index = request.indexOf("/LED_NTH") + 9;
    for (int i = index; i < request.length(); ++i) {
      if (request[i] == ' ') {
        break;
      }
      nth += request[i];
    }
    state = false;
     if (request.indexOf("OFF") != -1)
      turnOffNthLed(nth.toInt());
       if (request.indexOf("ON") != -1)
      turnONNthLeds(nth.toInt());
  }

  Serial.println("Client disonnected");
}


// #include <Arduino.h>
// #include <ESP8266WiFi.h>
// #include <WebSocketsServer.h>
// #include <Hash.h>
// #include <WiFiUdp.h>
// #include <Adafruit_NeoPixel.h>

// // Led control only music

// // ------------- SETUP for WS2812-----------------------
// // DEFINE PIN = GPI3 (USE RX)
// #define PIN 3
// // Set to the number of LEDs in your LED strip
// #define NUM_LEDS 30
// // Maximum number of packets to hold in the buffer. Don't change this.
// #define BUFFER_LEN 1024
// // Toggles FPS output (1 = print FPS over serial, 0 = disable output)
// #define PRINT_FPS 0


// // Wifi and socket settings
// const char* ssid     = "Dung Duong 2.4";
// const char* password = "meomeogaugau";
// unsigned int localPort = 7777;
// char packetBuffer[BUFFER_LEN];


// Adafruit_NeoPixel ledstrip = Adafruit_NeoPixel(30, PIN, NEO_GRB + NEO_KHZ800);

// WiFiUDP port;

// // Network information
// // IP must match the IP in config.py
// //IPAddress ip(192, 168, 1, 98);
// // Set gateway to your router's gateway
// //IPAddress gateway(192, 168, 1, 1);
// //IPAddress subnet(255, 255, 255, 0);

// void setup() {
//     pinMode(3, FUNCTION_0);
//     Serial.begin(115200);
//     //WiFi.config(ip, gateway, subnet);
//     WiFi.mode(WIFI_STA);
//     WiFi.begin(ssid, password);
//     Serial.println("");
//     // Connect to wifi and print the IP address over serial
//     while (WiFi.status() != WL_CONNECTED) {
//         delay(500);
//         Serial.print(".");
//     }

//     Serial.println("");
//     Serial.print("Connected to ");
//     Serial.println(ssid);
//     Serial.print("IP address: ");
//     Serial.println(WiFi.localIP());
//     port.begin(localPort);
//     // ledstrip.init(NUM_LEDS);
//     ledstrip.begin();
//     for(int i=0;i<NUM_LEDS; ++i){
//       ledstrip.setPixelColor(i,255,0,0);
//     }
//     ledstrip.show();

// }

// uint8_t N = 0;
// #if PRINT_FPS
//     uint16_t fpsCounter = 0;
//     uint32_t secondTimer = 0;
// #endif

// void loop() {
//         int packetSize = port.parsePacket();
//     // If packets have been received, interpret the command
//     if (packetSize) {
//         int len = port.read(packetBuffer, BUFFER_LEN);
//         for(int i = 0; i < len; i+=4) {
//             packetBuffer[len] = 0;
//             N = packetBuffer[i];
//             ledstrip.setPixelColor(N, (uint8_t)packetBuffer[i+1],(uint8_t)packetBuffer[i+2],(uint8_t)packetBuffer[i+3]);
//         }
//         ledstrip.show();
//         #if PRINT_FPS
//             fpsCounter++;
//         #endif
//     }
//     #if PRINT_FPS
//         if (millis() - secondTimer >= 1000U) {
//             secondTimer = millis();
//             Serial.printf("FPS: %d\n", fpsCounter);
//             fpsCounter = 0;
//         }
//     #endif
// }
