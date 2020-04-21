/***************************************************************************************
* NodeMCU ESP8266 board program done on Arduino IDE to send/receive commands via Firebase
* Circuit: Red, green and blue LEDs connected to pins 3, 5 and 7.
* *
***************************************************************************************/

#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>

#define FIREBASE_HOST "your_firebase_project.firebaseio.com"                //Your Firebase project name address
#define FIREBASE_AUTH "xMOIloBC5b7ucMRpovkrTgA9qKnmKMckzj3hePOy" //Your Firebase authentication key

#define WIFI_SSID "your_ssid"                            //Your wifi name 
#define WIFI_PASSWORD "your_passwrod"            //Your wifi password

String fireStatus = "";
const int pin_red = D7;
const int pin_blue = D5;
const int pin_green = D3;
bool watchdog = false;

void setup() {
  
  Serial.begin(9600);
  delay(1000);

  pinMode(pin_red, OUTPUT);
  pinMode(pin_blue, OUTPUT);
  pinMode(pin_green, OUTPUT);
                    
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);                     //Try to connect with wifi
  Serial.print("Connecting to ");
  Serial.print(WIFI_SSID);
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("Connected to ");
  Serial.println(WIFI_SSID);
  Serial.print("IP Address is : ");
  Serial.println(WiFi.localIP());                                       //Print local IP address

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);         //Connect to firebase
}

void loop() {

  firebaseToOutput("/leds/red_led", pin_red);
  firebaseToOutput("/leds/blue_led", pin_blue);
  firebaseToOutput("/leds/green_led", pin_green); 
  delay(100);
}

String firebaseToOutput(String firebaseNode, int output_pin)
{
   fireStatus = Firebase.getString(firebaseNode);              //Get LED state from Firebase
   if (fireStatus == "ON") {
      Serial.print("Turn on LED pin ");
      Serial.println(output_pin);                                     
      digitalWrite(output_pin, HIGH);                                //Turn ON LED
   } 
   else if (fireStatus == "OFF") {
      Serial.print("Turn off LED pin ");
      Serial.println(output_pin);
      digitalWrite(output_pin, LOW);                              //Turn OFF LED
   }
   else if (Firebase.failed()) {
      Serial.print("Firebase error: ");
      Serial.println(Firebase.error());
   } 
   else {
      Serial.print("Error accessing Firebase!(");
      Serial.print(fireStatus);
      Serial.println ("). Check your node path.");
   } 
}
