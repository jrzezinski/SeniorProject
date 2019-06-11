 // Initializing Firebase SDK 
 
 FirebaseOptions options = new FirebaseOptions.Builder()
    .setCredentials(GoogleCredentials.getApplicationDefault())
    .setDatabaseUrl("https://seniorproject-master.firebaseio.com/")
    .build();

FirebaseApp.initializeApp(options); 
