[![GitHub release (latest by date)](https://img.shields.io/github/v/release/multicus/steam-market-api)](https://github.com/multicus/steam-market-api/releases)

# Steam market API
This library provides simple functions to call endpoints on the Steam Community Market and returns them in a nice fashion.

This library currently supports these API calls:
| Steam API endpoint| Class | Response | Login required
|--|--|--|--|
| /market/priceoverview | PriceOverview | Price and volume of a single item on the market | No
| /market/search | MarketSearch | Sell- and buy prices, volume and item info of multiple items on the market| No
| /market/pricehistory | MarketHistory | All recorded price history including volume sold, price and date | Yes
| /inventory | Inventory | All items in the inventory of a user for a particular game | No
| /login | SteamLogin | Cookie containing sessionId for API calls that require user to be logged in (e.g. /market/pricehistory)

## Note on /login
This library currently does NOT support two-factor authentication with Steam Guard, so in order to use this API call you have to disable it on the Steam account.
