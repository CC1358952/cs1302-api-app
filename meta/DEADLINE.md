# Deadline

Modify this file to satisfy a submission requirement related to the project
deadline. Please keep this file organized using Markdown. If you click on
this file in your GitHub repository website, then you will see that the
Markdown is transformed into nice-looking HTML.

## Part 1.1: App Description

This app allows a user to input a current NHL player's name and it returns
some basic info about the player and weather metrics for the city where they
were born in. The first API is an NHL player/statistic API that returns (in my
case) the name, team, position, and city of birth. The second is a weather API
takes in the city of birth term and returns the temperature, wind speed,
precipitation, humidity, a short description, and the code for an image which I
saved as a local resource to speed up the loading process.

### GitHub Repo Link:
```
https://github.com/CC1358952/cs1302-api-app/tree/main
```

## Part 1.2: APIs

> For each RESTful JSON API that your app uses (at least two are required),
> include an example URL for a typical request made by your app. If you
> need to include additional notes (e.g., regarding API keys or rate
> limits), then you can do that below the URL/URI. Placeholders for this
> information are provided below. If your app uses more than two RESTful
> JSON APIs, then include them with similar formatting.

### API 1

```
https://search.d3.nhle.com/api/v1/search/player?culture=en-us&limit=200&q=mcdavid
```

> culture: the language outputs should be in.
> limit: needed to create response, 200 is arbitrary.
> q: the player name to search.

### API 2

```
https://api.weatherapi.com/v1/current.json?key=04ef81b3a35e42ccb9e144659240205&q=Richmond+Hill
```

> key: the API key necessary to run.
> q: city name to search.

## Part 2: New

> What is something new and/or exciting that you learned from working
> on this project?

TODO WRITE / REPLACE

## Part 3: Retrospect

> If you could start the project over from scratch, what do
> you think might do differently and why?

TODO WRITE / REPLACE
