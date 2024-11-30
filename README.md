<h1> Spring Boot based Weather Details Service </h1>


<h2>API Details: </h2>

```
/weather-by-ip?ip=123.123.123.123
```

Input: IP Address
Output: 
```json
{
"ip": "123.123.123.123",
"location": {
"city": "Pune",
"country": "India"
},
"weather": {
"temperature": 30.5,
"humidity": 60,
"description": "clear sky"
}
}
```
