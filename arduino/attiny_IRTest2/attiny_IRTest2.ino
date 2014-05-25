#include <SoftwareSerial.h>
#include <MeetAndroid.h>


int LEDpin = 4;
int Threshold = 200;
const byte IRPIN = 1;
float p[3];
int i = 0;
int Temp = 200;

SoftwareSerial  mySerial(0,1); // 設定對應bt的 rx 與 tx 接腳
MeetAndroid meetAndroid(mySerial);

//MeetAndroid meetAndroid(0, 1, 9600);

void setup()  
{
  // use the baud rate your bluetooth module is configured to 
  // not all baud rates are working well, i.e. ATMEGA168 works best with 57600
  mySerial.begin(9600);
  meetAndroid.registerFunction(ArdSetting,'S');
  meetAndroid.registerFunction(StopCount, 'o');
  meetAndroid.registerFunction(ArdCheck,'c');
}

void loop()
{
  meetAndroid.receive(); // you need to keep this in your loop() to receive events
  int dist = Count_distnce();
  if(compare_filter(dist, Threshold) && (dist > 20))
     meetAndroid.send(dist);   
  else  
     meetAndroid.send(0);   
}

void ArdSetting(byte flag, byte numOfValues)
{
   int ACK = 0;
   meetAndroid.send(ACK);
   Threshold = meetAndroid.getInt();  
}

void StopCount(byte flag, byte numOfValues)
{
   int received = meetAndroid.getInt();  
}


void ArdCheck(byte flag, byte numOfValues)
{
   int ACK = 0;
   meetAndroid.send(ACK);
}


int Count_distnce()
{
  float volts = analogRead(IRPIN)*0.0048828125;   // value from sensor * (5/1024) - if running 3.3.volts then change 5 to 3.3
  int distance = (int)(65*pow(volts, -1.10));          // worked out from graph 65 = theretical distance / (1/Volts)S - luckylarry.co.
  return distance;
}  

int average_filter(int average)
{
  p[i] = average;
  i++;
  if(i >= 3)
    i = 0;
  
  average = (p[0] + p[1] + p[2] )/3;
  
  return (int)average;
}

boolean compare_filter(int arg, int thd)
{
 boolean Flag = false;
 if (Temp < Threshold && arg <Threshold && arg > 20)
   Flag = true;
 else
   Flag = false;
   
 Temp = arg;
 
 return Flag;
}
