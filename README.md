# PTIT_IOT_VirtualAssitanceAndroid

## BTL IOT PTIT 2022: Java socket, Android, Speech Recoginition, Virtual Assistance, IOT

> Latest update on [develop branch](https://github.com/Gn0hp/PTIT_IOT_VirtualAssitanceAndroid/tree/develop).

## 1. Team members
  - [Nguyễn Trung Kiên](https://github.com/kiennt2781) B19DCCN346
  - [Phạm Việt Hoàng](https://github.com/pvhoang245) B19DCCN284
  - [Trần Khắc Phong](https://github.com/Gn0hp) B19DCCN502

##Requirements:
  - Device:
    + ESP8266
    + WS2812 multiple leds
    + DHT11 (optional)
  - Code:
    + Arduino IDE
    + library: 
      ++ WebSockets by Markus Sattler
      ++ Adafruit NeoPixel by Adafruit
      ++ ESP8266 and ESP32 OLED driver for SSD1306 displays by ThingPulse
      ++ WiFi101 by Adafruit
      ++ DHTSensor by Adafruit (optional)
      
     
## 2. Execute
  - Hardware: 
    - ws2812 - esp8266 plug: 
     - > 5v - vv
	   - > din - rx
	   - > gnd-gnd
  - Code:
    - Arduino:
    **NOTIFY: If your ledstrip has less than 60 leds then only need <=5v supply, else you will have to need an distinguish supply >5v** 
      - Modify NUM_LEDS with your own amount of led you have.
      - Config ssid - password with your Wifi
      - (Optional for DHT11) Config API thingspeak key, channel,... to your own data.
      - Upload code to esp8266, you will receive a ip address, copy it !
    - Python: 
    ** Suggest use anaconda virtual environment **
      - Install package:
        > pip install numpy scipy pyqtgraph pyaudio 
        and pyqt5(I don't remember exactly name xD. Program will report error then when your run so let install it later as soon as you know exactly name :D)
      - Config UUP_IP in config.py to the ip address you saved.
      - Config N_PIXELS in config.py to the amount of led you have on your ledstrip
      - (Optional): If you want to use gui then change USE_GUI to True and un-comment  ** app = QtGui.QApplication([]) ** in visualization.py file.
      - Run visualization.py file in terminal.
    *** Congratulation!! xD :V
     
