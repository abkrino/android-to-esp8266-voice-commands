# android-to-esp8266-voice-commands

Simple Android app to listen to voice commands on your device and send them to an ESP8266 board.

Commands are sent via Firebase cloud-hosted NoSQL realtime database, which then are read by ESP8266 through your local wifi connection.

Kaminski - fckaminski66@gmail.com


## Firebase setup:
- Create your own realtime database with Firebase. Seee the chapter "Making a Real-time Database" from the tutorial
    https://medium.com/coinmonks/arduino-to-android-real-time-communication-for-iot-with-firebase-60df579f962
- In your Realtime Database, create the child/values pairs accondingly to the Android program, or alternatively, select the "Import JSON" option and then import the  "leds-export.json" file.

## ESP8266 setup:
- Connect three LEDs to three output pins.
- Using Arduino IDE, open VoiceCommandsToArduino.ino project.
- Install 'ESP8266 WiFi' and 'Firebase ESP8266 Client' libraries to your Arduino IDE.
- Adjust the output pins numbers used, your local WiFi SSID/password and your Firebase settings. 
- Upload the project to your board.

## Android setup:
- Import Android app project in Android Studio.
- Select menu Tools > Firebase. At the right panel, select "Real-Time Database". Log on with your Google account and select:
	a) Connect your App to Firebase by selecting the realtme database you created and clicking "Connect to Firebase".
	b) Add the Realtime Database to yout app.
  If you need help here, refer to the tutorial https://medium.com/coinmonks/arduino-to-android-real-time-communication-for-iot-with-firebase-60df579f962..

- Install "Google Text-to-Speech" from Google Play and enable it at phone settings.
- Enable your phone "Developer options" and then enable "USB debugging" and "Install via USB".
- Connect your phone to the a workstation USB port and authorize file transfer.
- Check if Android Studio now list it your device in the upper menu. If so, build your application and run it.
