#include <WiFi.h>
const int trig = 27;
const int echo = 14;
unsigned long duration;
int distance;
int base;
const char* ssid = "JAYCE";
const char* password = "12345678";
const char* host = "192.168.1.241";
const int port = 50050;
int currstat;
int beforestat = 3;
//int prevstat = -100;
WiFiClient client;
void setup() {
  Serial.begin(9600);
  delay(1000);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Connecting to WiFi.........");
    }
    Serial.println("Connected");
    client.connect(host, port);
    base = 0;
  duration = 0;
  distance = 0;
  // put your setup code here, to run /'"""once:
  delay(1000);
  pinMode(trig,OUTPUT);
  pinMode(echo,INPUT);
  base = 0;
  duration = 0;
  distance = 0;
  // put your setup code here, to run /'"""once:
  delay(1000);
  pinMode(trig,OUTPUT);
  pinMode(echo,INPUT);
  digitalWrite(trig,0);
  delayMicroseconds(5);
  digitalWrite(trig,1);
  delayMicroseconds(10);
  digitalWrite(trig,0);
  duration = pulseIn(echo,HIGH);
  distance = int(duration/2/29.421);
  base = distance;
  Serial.println(base);
  delay(200);
}
  
  void loop(){
  duration = 0;
  distance = 0;
  pinMode(trig,OUTPUT);
  pinMode(echo,INPUT);
  digitalWrite(trig,0);
  delayMicroseconds(5);
  digitalWrite(trig,1);
  delayMicroseconds(10);
  digitalWrite(trig,0);
  duration = pulseIn(echo,HIGH);
  distance = int(duration/2/29.421);
  int diff = base - distance;
   if(diff >= 10){
    currstat = 2;
    beforestat = senddata(currstat,beforestat);
    Serial.println("Jumping!");
  }else if(10 > diff && diff > -10){
    currstat = 1;
    beforestat = senddata(currstat,beforestat);
    Serial.println("Standing");
  }else if(diff <= -10){
    currstat = 0;
    beforestat = senddata(currstat,beforestat);
    Serial.println("Sitting!");
  }else{
    currstat = -2;
    beforestat = senddata(currstat,beforestat);
    Serial.println("Error input");
  }
  delay(200);
}

int senddata(int current,int before){
  if(current != before){
    client.println(currstat);
    before = current;
  }
  return before;
}
