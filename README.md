Trading Journal Android App
Overview

The Trading Journal Android App is a personal trading tracker developed as part of the RMA course project. It allows users to track cryptocurrency and stock trades, monitor live prices, and calculate profit and loss (PnL) in real-time. The app integrates with external market APIs for price updates and provides a user-friendly interface for managing trades and viewing historical data.

Features

Add and manage trades: Record trades with details such as symbol, direction (BUY/SELL), size, entry price, and exit price.
Live prices: Fetch real-time market prices from external APIs.
Profit & Loss calculation: Automatically calculates PnL based on trade direction and market price changes.
Trade history: View all past trades in a structured list.
News feed: Displays relevant trading news for selected assets.
Data storage: Uses Room database for offline persistence of trades.
MVVM architecture: Ensures separation of concerns and maintainable code.
Data binding and LiveData: For reactive UI updates.

Technologies Used

Android Studio with Java
MVVM Architecture
Room Database for local data storage
Retrofit2 for API communication
LiveData and ViewModel for reactive UI
Data Binding for UI management
Gradle for build automation

Project Structure

app/src/main/java/com/example/projekat/ui/activities/ – Contains app activities.
app/src/main/java/com/example/projekat/ui/fragments/ – Contains UI fragments for trades and news.
app/src/main/java/com/example/projekat/ui/viewmodel/ – ViewModels managing data and logic.
app/src/main/java/com/example/projekat/data/ – Repository classes handling API calls and data sources.
app/src/main/java/com/example/projekat/db/ – Room database entities and DAO.

License

This project is for educational purposes as part of the RMA course project.
