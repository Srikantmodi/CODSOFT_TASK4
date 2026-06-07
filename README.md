# Currency Converter

This is a simple Java Swing currency converter built with pure Java SE.

## What it does

- Fetches live exchange rates from `https://api.exchangerate-api.com/v4/latest/`
- Converts between common currencies like USD, EUR, INR, GBP, JPY, CAD, and AUD
- Shows the result with a readable currency symbol

## Files

- `RateFetcher.java` - gets the exchange rate and calculates the converted amount
- `ConverterUI.java` - builds the Swing window and handles user input

## How to run

Open a terminal in this folder and run:

```bash
javac RateFetcher.java ConverterUI.java
java ConverterUI
```

## Notes

- No third-party libraries are used
- The app uses simple string parsing instead of JSON libraries
- If the internet connection is unavailable, the app shows a friendly failure message instead of crashing