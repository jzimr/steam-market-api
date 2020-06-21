# Steam market API
This library provides simple functions to call endpoints on the Steam Community Market and returns them in a nice fashion.

This library currently supports these API calls:
| Steam API endpoint| Class | Response |
|--|--|--|
| /market/priceoverview | PriceOverview | Price and volume of a single item on the market |
| /market/search | MarketSearch | Sell- and buy prices, volume and item info of multiple items on the market
| /market/pricehistory | MarketHistory | All recorded price history including volume sold, price and date.
| /inventory | Inventory | All items in the inventory of a user for a particular game.
| /login | SteamLogin | Cookie containing sessionId for API calls that require user to be logged in (e.g. /market/pricehistory)
