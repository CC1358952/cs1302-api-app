# Deadline

## Part 1.1: App Description

This app allows a user to input a current NHL player's name and it returns
some basic info about the player and weather metrics for the city where they
were born in. The first API is an NHL player/statistic API that returns (in my
case) the name, team, position, and city of birth. The second is a weather API
takes in the city of birth term and returns the temperature, wind speed,
precipitation, humidity, a short description, and the code for an image which I
saved as a local resource to speed up the loading process.

The app displays the current weather and player information, both of which cannot be
accessed by utilizing one API by itself.
### GitHub Repo Link:
```
https://github.com/CC1358952/cs1302-api-app/tree/main
```

## Part 1.2: APIs

### API 1

```
https://search.d3.nhle.com/api/v1/search/player?culture=en-us&limit=200&q=mcdavid
```

This API returns a JSON response containing information about the player desired.
I chose to utilize the name, teamAbbrev, active, birthCity, and birthCountry variables.

> culture: the language outputs should be in.

> limit: needed to create response, 200 is arbitrary.

> q: the player name to search.

### API 2

```
https://api.weatherapi.com/v1/current.json?key=04ef81b3a35e42ccb9e144659240205&q=Richmond+Hill
```

This API returns a JSON response containing information about the location and current weather
of the city desired. I created three additional subclasses called Location, Condition, and CurrentWeather
to get the information I wanted. I chose to utilize the name, and country from the Location class,
the temp_f, mind_mph, precip_in, and humidity from the CurrentWeather class, and the text and icon
url from the Condition class.
> key: the API key necessary to run.

> q: city name to search.

## Part 2: New

In past projects, we did a lot of work on handling exceptions, however, in this project, I feel I gained a
better grasp on predicting errors from the user and functions and then catching them in the appropriate
locations. That is a very valuable lesson to learn so I'm glad I learned it in this project.

I also feel I got a lot of practice of how to effectively break up long chunks of code during this project.
My start and init methods were very long, too long to pass checkStyle so I created orgainized helper methods
to make the actual JavaFX methods shorter and easier to understand.

On a less important note, I learned a lot of NHL players with odd names and odd cities of birth in order
to comprehensively check the app for errors. That is just added to the archives of nearly useless information
stored in my head, right next to standard-issue millitary watch specifications and advanced baseball analytics.

## Part 3: Retrospect

If I were to redo this project, I think I would add a feature or two to be more creative. I didn't feel that
any feature I would add would be the best and most efficient by the deadline so I held off this time. I think
I would have also played around with UI design more to make the app look more appealing and/or exciting. It took
me a while to find two APIs that would work well enough together so if I were to start over I would have a plan
and therefore more time to make the concept the best it can be.

I'm still proud of what I have created. If you showed me this project at the beginning of the semester and said
that I built this a few months later, I would not have believed you. This project did a good job of showcasing and
implementing most of the new things I learned this semester.