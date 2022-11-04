// #include <Arduino.h>
// #include <ESP8266WiFi.h>
// #include <WebSocketsServer.h>
// #include <Hash.h>
// #include <WiFiUdp.h>
// #include <Adafruit_NeoPixel.h>
// #include "DHT.h"


// const int LED_ON=1;
// const int LED_OFF=2;
// const int LED_NTH = 3;
// const int MUSIC_MODE_ON=4;
// const int MUSIC_MODE_OFF=5;
// const int LED_WARN_ON=6;
// const int LED_WARN_OFF=7;

// int ledStripMode = LED_ON;

// // ------------- SETUP for WS2812-----------------------
// // DEFINE PIN = GPI3 (USE RX)
// #define PIN 3
// // Set to the number of LEDs in your LED strip
// #define NUM_LEDS 30
// // Maximum number of packets to hold in the buffer. Don't change this.
// #define BUFFER_LEN 1024
// // Toggles FPS output (1 = print FPS over serial, 0 = disable output)
// #define PRINT_FPS 0

// // ------------- SETUP for DHT11-----------------------
// #define DHTTYPE DHT11

// WiFiClient client;
// const int DHTPin = 5;
// const int channelID = 1855735; //
// const char* dhtServer = "api.thingspeak.com";
// String writeAPIKey = "43ABY00L7T9D5X6R";
// const int postingInterval = 2 * 1000; // post data every 2 seconds

// // DHT dht(DHTPin, DHTTYPE);
// // ------------------------ END SETUP ------------------------------

// // Wifi and socket settings
// const char* ssid     = "Dung Duong";
// const char* password = "meomeogaugau";
// unsigned int localPort = 7777;
// char packetBuffer[BUFFER_LEN];
// bool state=false;



// Adafruit_NeoPixel ledstrip = Adafruit_NeoPixel(30, PIN, NEO_GRB + NEO_KHZ800);

// WiFiUDP port;
// WiFiServer server(8191);

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
//      server.begin();
//     Serial.println("");
//     Serial.print("Connected to ");
//     Serial.println(ssid);
//     Serial.print("IP address: ");
//     Serial.println(WiFi.localIP());
//     port.begin(localPort);
//     // ledstrip.init(NUM_LEDS);
//     ledstrip.begin();
//     state=false;
//     // dht.begin();
//     ledStripMode = LED_ON;
// }

// uint8_t N = 0;
// #if PRINT_FPS 
//     uint16_t fpsCounter = 0;
//     uint32_t secondTimer = 0;
// #endif

// void loop() {
//   if(state == true){
//     // dht11Handle();
//       if(ledStripMode == LED_ON ) {
//         Serial.println("Turn on Led");
//         turnOnLed();
//         state = false;
//         delay(50);
//       }else if(ledStripMode == LED_OFF){
//         Serial.println("Turn off led");
//         turnOffLed();
//         state = false;
//         delay(50);
//       }
//       else if(ledStripMode == MUSIC_MODE_ON) {
//         state = false;
//         Serial.println("Led witg music");
//         ws2812WithMusic();
//         delay(50);
//       }else if(ledStripMode == MUSIC_MODE_OFF) {
//         state = false;
//         Serial.println("Turn on Led");
//         turnOnLed();
//         delay(50);
//       }else if(ledStripMode== LED_WARN_ON){
//         state = false;
//         Serial.println("Led warn");
//         ledWarn();
//         delay(50);
//       } else if(ledStripMode == LED_WARN_OFF){
//         state = false;
//         Serial.println("Led off");
//         turnOffLed();
//         delay(1);
//       }
//   }
  
//   getRequestString(); 
//   delay(1);
// }
// void turnOnLed() {
//     for(int i=0;i<NUM_LEDS; ++i)
//       ledstrip.setPixelColor(i, 255,255,255);
//     ledstrip.show();
// }
// void turnOffLed() {
//     for(int i=0;i<NUM_LEDS; ++i)
//       ledstrip.setPixelColor(i, 0,0,0);
//     ledstrip.show();
// }
// void turnNthLed(int n){
//   ledstrip.setPixelColor(n,255,255,0);  
// }
// void ledWarn(){
//   for(int i=0;i<30; ++i)
//     ledstrip.setPixelColor(i, 255,0,0);
//   ledstrip.show();
//   delay(1000);
// }
// void ws2812WithMusic() {
//   // Read data over socket
//     int packetSize = port.parsePacket();
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



// // ----------------------------------- GET REQUEST STRING FOR MODE USING-------------------------------

// void getRequestString(){  
//   WiFiClient client = server.available();
//   if (!client){
//     return;
//   }
    
//   Serial.println("Waiting for new client");
//   while(!client.available())
//   {
//     delay(1);
//   }
//   String request = client.readStringUntil('\r');
//   Serial.println(request);
//   client.flush();
//     delay(1);
//   if(request.indexOf("/LED=ON") != -1){
//     Serial.println("Received");
//     ledStripMode= LED_ON;
//     state = true;
//   }
//   else if(request.indexOf("/LED=OFF") != -1){
//     Serial.println("Received");
//     ledStripMode= LED_ON;
//     state = true;
//   }
//   else if(request.indexOf("/LED=NTH") != -1){
//     Serial.println("Received");
//     ledStripMode= LED_NTH;
//     state = true;
//   }
//   else if(request.indexOf("/MUSIC=ON") != -1){
//     Serial.println("Received");
//     ledStripMode= MUSIC_MODE_ON;
//     state = true;
//   }
//   else if(request.indexOf("/MUSIC=OFF") != -1){
//     Serial.println("Received");
//     ledStripMode= MUSIC_MODE_OFF;
//     state = true;
//   }
//   else if(request.indexOf("/LED_WARN_ON")!= -1){
//     Serial.println("Received");
//     ledStripMode= LED_WARN_ON;
//     state = true;
//   }
//   else if(request.indexOf("/LED_WARN_OFF") != -1){
//     Serial.println("Received");
//     ledStripMode= LED_WARN_OFF;
//     state = true;
//   }
  
//   Serial.println("Client disonnected");
  
// }

#include <ESP8266WiFi.h>
#include <WebSocketsServer.h>
#include <WiFiUdp.h>
#include <Adafruit_NeoPixel.h>


// ------------- SETUP for WS2812-----------------------
// DEFINE PIN = GPI3 (USE RX)
#define PIN 3
// Set to the number of LEDs in your LED strip
#define NUM_LEDS 30
// Maximum number of packets to hold in the buffer. Don't change this.
#define BUFFER_LEN 1024
// Toggles FPS output (1 = print FPS over serial, 0 = disable output)
#define PRINT_FPS 0


// Wifi and socket settings
const char* ssid     = "Dung Duong";
const char* password = "meomeogaugau";
unsigned int localPort = 7777;
char packetBuffer[BUFFER_LEN];


Adafruit_NeoPixel ledstrip = Adafruit_NeoPixel(30, PIN, NEO_GRB + NEO_KHZ800);

WiFiUDP port;

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

    Serial.println("");
    Serial.print("Connected to ");
    Serial.println(ssid);
    Serial.print("IP address: ");
    Serial.println(WiFi.localIP());
    port.begin(localPort);
    // ledstrip.init(NUM_LEDS);
    ledstrip.begin();

}

uint8_t N = 0;
#if PRINT_FPS 
    uint16_t fpsCounter = 0;
    uint32_t secondTimer = 0;
#endif

void loop() {
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




