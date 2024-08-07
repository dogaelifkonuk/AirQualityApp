# AirQualityApp

This project is a RESTful API to fetch and display air quality data from the OpenWeather API, integrating it with both backend and frontend components.
Backend and frontend files are sperated into two folders named 'backend' and 'frontend' respectively. Both of these folders are present in the main branch.

In order to run the app, you need to pull these folders to seperate IDEs and run them simultaneously. For easing this process, I opened 2 branches named 'backend' and 'frontend'. For example, for pulling the backend folder, you can checkout to backend branch and you can also do the same for the frontend part.

# Backend
OpenWeather API data is fetched using an API key and this key in unique to every user. Before running the project, you need to insert your API key inside the project.

Simply go to 'backend/src/main/resources/application.properties' and write your api key on the following line:
api-key={your-api-key}

After inserting the API keyi you can run the project. 
Backend part of the project is run by clicking on 'Run AirPollutionApplication' inside 'backend/src/main/java/com/example/air_pollution_app
/AirPollutionApplication.java'.

# Frontend
Frontend part of the project is created using yarn. You need to create node_modules using the command 'yarn' or 'yarn install' on your local machine. 
After node_modules are created, you can run the project with the command 'yarn dev'. 
By navigating to the specified port in your browser, you can see the user interface on web.

# Using the app
You can access CO, O3, and, SO2 levels in any city for a specified range of dates. The air quality data will be displayed in a table format, with categories depending on the amount of aforementioned chemical components. These categories are Good, Satisfactory, Moderate, Poor, Severe and Hazardous. 

You can also delete Air Quality Data, by clicking on the trash icons next to each record.

Important note: The system is designed to make OpenWeather API calls when a requested record is not stored in the database. So, if the page is refreshed after deleting a record, the record will be fetched again from OpenWeather API and displayed as before and saved to database.
