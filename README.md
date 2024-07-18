### Deployment

1) Generate backend .jar files using the command `build_jar_files.bat`;
2) Ensure that you have a docker environment running properly and execute the command `clean_and_run.bat`. It will clear the docker resources, generate the docker images and run the command `docker compose up`; 
3) The web app will be available at http://localhost:3001/ when it is running locally or you can access the live version at http://35.173.139.184:3001/. 
4) Initial users: user=user_1/password=user_1 and user=user_2/password=user_1.