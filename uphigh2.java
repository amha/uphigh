#include <Bridge.h>
#include <Temboo.h>
#include "TembooAccount.h" // Contains Temboo account information 

char deviceID[8] = "aaa";

// The number of times to trigger the action if the condition is met.
// We limit this so you won't use all of your Temboo calls while testing.
int maxCalls = 500;

// The number of times this Choreo has been run so far in this sketch.
int calls = 0;

void setup() {
  Serial.begin(9600);
  
  // For debugging, wait until the serial console is connected.
  delay(4000);
  // while(!Serial);
  Bridge.begin();

  // Initialize pins
  pinMode(A5, INPUT);
//  pinMode(13, OUTPUT);  // fred 
  //digitalWrite(2, LOW); // fred just commented out
  // Serial.println("force digital input to LOW.\n"); // fred just commented out
  Serial.println("Setup complete.\n");
} // setup

//void loop() {
//  int sensorValue = analogRead(A5);
//  Serial.println(sensorValue);
//  
//  if(sensorValue > 200) {
//   delay(5000); 
//  }
//}
void loop() {
  int sensorValue = analogRead(A5);
  
  Serial.println("Sensor: " + String(sensorValue));
  
  if(sensorValue > 200) {
    // reset to LOW
    digitalWrite(2, LOW);
    
    if (calls < maxCalls) {
      Serial.println("\nTriggered! Calling /Library/Google/Spreadsheets/AppendRow...");
      
      // - For Timestamp
      
      
      
      // For Times
      runAppendRow(deviceID);
      // runAppendRow(sensorValue);
      // digitalWrite(13, LOW);   // fred commented out 
      calls++;
      delay(5000);
    } else {  // calls loop
      Serial.println("\nTriggered! Skipping action to save Temboo calls during testing.");
      Serial.println("You can adjust or remove the calls/maxCalls if() statement to change this behavior.\n");
    }
  } // sensor value if
  //digitalWrite(13, LOW); fred commented out

}  // loop

// void runAppendRow(int sensorValue) {
void runAppendRow(String deviceID) { 
  TembooChoreo AppendRowChoreo;

  // Invoke the Temboo client
  AppendRowChoreo.begin();

  // Set Temboo account credentials
  AppendRowChoreo.setAccountName(TEMBOO_ACCOUNT);
  AppendRowChoreo.setAppKeyName(TEMBOO_APP_KEY_NAME);
  AppendRowChoreo.setAppKey(TEMBOO_APP_KEY);

  // Set profile to use for execution
  AppendRowChoreo.setProfile("yunGenericSpreadsheet");

  // Set Choreo inputs
  // String RowDataValue = String(sensorValue);
  String RowDataValue = String(deviceID);
  AppendRowChoreo.addInput("RowData", RowDataValue);

  // Identify the Choreo to run
  AppendRowChoreo.setChoreo("/Library/Google/Spreadsheets/AppendRow");

  // Run the Choreo
  unsigned int returnCode = AppendRowChoreo.run();

  // A return code of zero means everything worked
  if (returnCode == 0) {
    Serial.println("Done!\n");
  } else {
    // A non-zero return code means there was an error
    // Read and print the error message
    while (AppendRowChoreo.available()) {
      char c = AppendRowChoreo.read();
      Serial.print(c);
    }
    Serial.println();
  }
  
  AppendRowChoreo.close();
} // returnCode loop 