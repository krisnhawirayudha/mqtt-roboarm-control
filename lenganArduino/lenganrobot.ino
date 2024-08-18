#include <WiFi.h>
#include <WiFiClientSecure.h>
#include <PubSubClient.h>
#include <Wire.h>
#include <Adafruit_PWMServoDriver.h>
#include <EEPROM.h>
#include <time.h>

Adafruit_PWMServoDriver pwm = Adafruit_PWMServoDriver();

int servo1 = 0;
int servo2 = 0;
int servo3 = 0;
int servo4 = 0;

#define SERVOMIN 150
#define SERVOMAX 600
#define SERVO_MIN_ANGLE 0
#define SERVO_MAX_ANGLE 180

const char* ssid = "RESTRICTED AREA";
const char* password = "punyajerodelod1";
const char* mqtt_server = "b154cc6667e74aafa30a15667698f80e.s1.eu.hivemq.cloud";

WiFiClientSecure espClient;
PubSubClient client(espClient);

bool isScenarioRunning = false;
int scenarioIndex = 0;

static const char *root_ca PROGMEM = R"EOF(
-----BEGIN CERTIFICATE-----
MIIFazCCA1OgAwIBAgIRAIIQz7DSQONZRGPgu2OCiwAwDQYJKoZIhvcNAQELBQAw
TzELMAkGA1UEBhMCVVMxKTAnBgNVBAoTIEludGVybmV0IFNlY3VyaXR5IFJlc2Vh
cmNoIEdyb3VwMRUwEwYDVQQDEwxJU1JHIFJvb3QgWDEwHhcNMTUwNjA0MTEwNDM4
WhcNMzUwNjA0MTEwNDM4WjBPMQswCQYDVQQGEwJVUzEpMCcGA1UEChMgSW50ZXJu
ZXQgU2VjdXJpdHkgUmVzZWFyY2ggR3JvdXAxFTATBgNVBAMTDElTUkcgUm9vdCBY
MTCCAiIwDQYJKoZIhvcNAQEBBQADggIPADCCAgoCggIBAK3oJHP0FDfzm54rVygc
h77ct984kIxuPOZXoHj3dcKi/vVqbvYATyjb3miGbESTtrFj/RQSa78f0uoxmyF+
0TM8ukj13Xnfs7j/EvEhmkvBioZxaUpmZmyPfjxwv60pIgbz5MDmgK7iS4+3mX6U
A5/TR5d8mUgjU+g4rk8Kb4Mu0UlXjIB0ttov0DiNewNwIRt18jA8+o+u3dpjq+sW
T8KOEUt+zwvo/7V3LvSye0rgTBIlDHCNAymg4VMk7BPZ7hm/ELNKjD+Jo2FR3qyH
B5T0Y3HsLuJvW5iB4YlcNHlsdu87kGJ55tukmi8mxdAQ4Q7e2RCOFvu396j3x+UC
B5iPNgiV5+I3lg02dZ77DnKxHZu8A/lJBdiB3QW0KtZB6awBdpUKD9jf1b0SHzUv
KBds0pjBqAlkd25HN7rOrFleaJ1/ctaJxQZBKT5ZPt0m9STJEadao0xAH0ahmbWn
OlFuhjuefXKnEgV4We0+UXgVCwOPjdAvBbI+e0ocS3MFEvzG6uBQE3xDk3SzynTn
jh8BCNAw1FtxNrQHusEwMFxIt4I7mKZ9YIqioymCzLq9gwQbooMDQaHWBfEbwrbw
qHyGO0aoSCqI3Haadr8faqU9GY/rOPNk3sgrDQoo//fb4hVC1CLQJ13hef4Y53CI
rU7m2Ys6xt0nUW7/vGT1M0NPAgMBAAGjQjBAMA4GA1UdDwEB/wQEAwIBBjAPBgNV
HRMBAf8EBTADAQH/MB0GA1UdDgQWBBR5tFnme7bl5AFzgAiIyBpY9umbbjANBgkq
hkiG9w0BAQsFAAOCAgEAVR9YqbyyqFDQDLHYGmkgJykIrGF1XIpu+ILlaS/V9lZL
ubhzEFnTIZd+50xx+7LSYK05qAvqFyFWhfFQDlnrzuBZ6brJFe+GnY+EgPbk6ZGQ
3BebYhtF8GaV0nxvwuo77x/Py9auJ/GpsMiu/X1+mvoiBOv/2X/qkSsisRcOj/KK
NFtY2PwByVS5uCbMiogziUwthDyC3+6WVwW6LLv3xLfHTjuCvjHIInNzktHCgKQ5
ORAzI4JMPJ+GslWYHb4phowim57iaztXOoJwTdwJx4nLCgdNbOhdjsnvzqvHu7Ur
TkXWStAmzOVyyghqpZXjFaH3pO3JLF+l+/+sKAIuvtd7u+Nxe5AW0wdeRlN8NwdC
jNPElpzVmbUq4JUagEiuTDkHzsxHpFKVK7q4+63SM1N95R1NbdWhscdCb+ZAJzVc
oyi3B43njTOQ5yOf+1CceWxG1bQVs5ZufpsMljq4Ui0/1lvh+wjChP4kqKOJ2qxq
4RgqsahDYVvTH9w7jXbyLeiNdd8XM2w9U/t7y0Ff/9yi0GE44Za4rF2LN9d11TPA
mRGunUHBcnWEvgJBQl9nJEiU0Zsnvgc/ubhPgXRR4Xq37Z0j4r7g1SgEEzwxA57d
emyPxgcYxn/eR44/KJ4EBs+lVDR3veyJm+kXQ99b21/+jh5Xos1AnX5iItreGCc=
-----END CERTIFICATE-----
)EOF";

// Fungsi CRC16
uint16_t crc16(const uint8_t* data, uint16_t length) {
  uint16_t crc = 0xFFFF;

  for (uint16_t i = 0; i < length; i++) {
    crc ^= data[i];

    for (uint8_t j = 0; j < 8; j++) {
      if (crc & 0x0001) {
        crc >>= 1;
        crc ^= 0xA001;
      } else {
        crc >>= 1;
      }
    }
  }

  return crc;
}

void sendDataWithCRC(const String& data) {
  uint16_t crcValue = crc16((const uint8_t*)data.c_str(), data.length());
  String dataWithCRC = data + "," + String(crcValue, HEX);

  // Mengirim data ke topik MQTT
  client.publish("sliderbase", dataWithCRC.c_str());
}

void setup_wifi() {
  delay(10);
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  randomSeed(micros());

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

void reconnect() {
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    String clientId = "ESP32Client - MyClient";

    if (client.connect(clientId.c_str(), "lengankrisnha", "Riders12")) {
      Serial.println("connected");

      client.subscribe("sliderbase");
      client.subscribe("sliderbasetest");
      client.subscribe("slidershoulder");
      client.subscribe("sliderelbow");
      client.subscribe("slidergrip");
      client.subscribe("controlScenario");
      client.subscribe("multiServoControl");  // Tambahkan topik baru untuk multi-servo control
      
    } else {
      Serial.print("failed, rc = ");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      delay(5000);
    }
  }
}

void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  String incommingMessage = "";

  for (int i = 0; i < length; i++) {
    incommingMessage += (char)payload[i];
  }

  int commaIndex = incommingMessage.lastIndexOf(',');
  if (commaIndex != -1) {
    String data = incommingMessage.substring(0, commaIndex);
    String crcReceivedStr = incommingMessage.substring(commaIndex + 1);
    uint16_t crcReceived = (uint16_t)strtol(crcReceivedStr.c_str(), NULL, 16);
    uint16_t crcCalculated = crc16((const uint8_t*)data.c_str(), data.length());

    if (crcReceived == crcCalculated) {
      Serial.println("CRC valid. Data diterima: " + data);
      // Lanjutkan dengan data
    } else {
      Serial.println("CRC tidak valid. Data mungkin korup.");
    }
  }
}

void moveServoSmoothly(int servoIndex, int startPosition, int targetPosition) {
  int stepSize = 1;
  int direction = (targetPosition > startPosition) ? 1 : -1;

  for (int i = startPosition; (direction > 0) ? (i <= targetPosition) : (i >= targetPosition); i += direction * stepSize) {
    pwm.setPWM(servoIndex, 0, map(i, SERVO_MIN_ANGLE, SERVO_MAX_ANGLE, SERVOMIN, SERVOMAX));
    delay(10);
  }
}

int scenarioSteps[][2] = {
  {0, 30}, {3, 90}, {2, 110}, {1, 60}, {3, 180}, {1, 0}, {2, 20},
  {0, 150}, {2, 110}, {1, 60}, {3, 90}, {1, 10}, {2, 20}, {0, 90}, {3, 180}
};

void runScenario() {
  if (isScenarioRunning && scenarioIndex < (sizeof(scenarioSteps) / sizeof(scenarioSteps[0]))) {
    int servoIndex = scenarioSteps[scenarioIndex][0];
    int targetPosition = scenarioSteps[scenarioIndex][1];
    
    if (servoIndex == 0) {
      moveServoSmoothly(servoIndex, servo1, targetPosition);
      servo1 = targetPosition;
    } else if (servoIndex == 1) {
      moveServoSmoothly(servoIndex, servo2, targetPosition);
      servo2 = targetPosition;
    } else if (servoIndex == 2) {
      moveServoSmoothly(servoIndex, servo3, targetPosition);
      servo3 = targetPosition;
    } else if (servoIndex == 3) {
      moveServoSmoothly(servoIndex, servo4, targetPosition);
      servo4 = targetPosition;
    }

    scenarioIndex++;
    if (scenarioIndex == (sizeof(scenarioSteps) / sizeof(scenarioSteps[0]))) {
      isScenarioRunning = false;
      scenarioIndex = 0;
      Serial.println("Scenario completed.");
    }
  }
}

void setup() {
  EEPROM.begin(512);
  delay(500);
  Serial.begin(115200);

  setup_wifi();

  espClient.setCACert(root_ca);
  client.setServer(mqtt_server, 8883);
  client.setCallback(callback);
  
  Wire.begin(4, 5);
  pwm.begin();
  pwm.setPWMFreq(60);
  Serial.println("ROBOT ARM CONTROLLER");

  servo1 = EEPROM.read(0);
  servo2 = EEPROM.read(1);
  servo3 = EEPROM.read(2);
  servo4 = EEPROM.read(3);

  pwm.setPWM(0, 0, map(servo1, SERVO_MIN_ANGLE, SERVO_MAX_ANGLE, SERVOMIN, SERVOMAX));
  pwm.setPWM(1, 0, map(servo2, SERVO_MIN_ANGLE, SERVO_MAX_ANGLE, SERVOMIN, SERVOMAX));
  pwm.setPWM(2, 0, map(servo3, SERVO_MIN_ANGLE, SERVO_MAX_ANGLE, SERVOMIN, SERVOMAX));
  pwm.setPWM(3, 0, map(servo4, SERVO_MIN_ANGLE, SERVO_MAX_ANGLE, SERVOMIN, SERVOMAX));

  // Set time
  configTime(0, 0, "pool.ntp.org");
}

void loop() {
  if (!client.connected()) {
    reconnect();
  }

  if (client.connected()) {
    client.loop();
  } else {
    Serial.println("Koneksi MQTT terputus, mencoba kembali...");
    delay(5000);
  }
  
  runScenario();
}