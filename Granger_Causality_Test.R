# Developed by Joseph Scavetta
# Retrieves data from a local MySQL database containing dates with their corresponding
# bitcoin price index (BPI) and tweets analyzed for textual sentiment. The data is filtered
# and merged, and is then statistically analyzed for time series Granger causality.

##### Install Packages #####

#install.packages("RMySQL")
#install.packages("forecast")
#install.packages("lmtest")


##### Required Packages #####

library(RMySQL)
library(forecast)
library(lmtest)
library(ggplot2)


##### Connect to the Database and Retrieve Data #####

mydb <- dbConnect(MySQL(), user='root', password='root', dbname='bitcoin_price_sentiment', host='localhost')

rs <- dbSendQuery(mydb, "SELECT polarity, polarity_confidence, date FROM sentiment")
sentiment.data <- fetch(rs, n=-1)

rs <- dbSendQuery(mydb, "SELECT price_index, date FROM bitcoin_price_index")
bpi.data <- fetch(rs, n=-1)


##### Clean Sentiment Data #####

# Remove low confidence tweets
sentiment.confident <- sentiment.data[which(sentiment.data$polarity_confidence > 0.50), c(1,3)]

# Remove neutral tweets
sentiment.confident <- sentiment.confident[which(sentiment.confident$polarity != "neutral"),]

# Get proportion of positive tweets to negative tweets for each day
sentiment.by.date <- tapply(sentiment.confident$polarity, sentiment.confident$date, function(x) {
  positive <- length(which(x == "positive"))
  negative <- length(which(x == "negative"))
  
  return (positive / (positive + negative))
})
sentiment.by.date <- as.data.frame(sentiment.by.date)
sentiment.by.date <- data.frame(positivity.prop = as.numeric(sentiment.by.date$sentiment.by.date), date = row.names(sentiment.by.date))


##### Merge Data #####

combined.data <- merge.data.frame(bpi.data, sentiment.by.date, by = "date")
combined.data$date <- as.Date(combined.data$date)


##### Perform Granger Test #####

# Price index granger cause positivity proportion?
grangertest(combined.data$positivity.prop ~ combined.data$price_index, order = 2)

# Positivity proportion granger cause price index?
grangertest(combined.data$price_index ~ combined.data$positivity.prop, order = 2)


##### Visualize Data #####

# Raw Data
par(mar=c(5,5,1,5))
plot(combined.data$date, combined.data$price_index, xaxt="n", type="l", lwd=2, col="darkred", xlab=NA, ylab="Bitcoin Price Index (USD)")
axis(1, combined.data$date, format(combined.data$date, "%b %d"))
par(new = TRUE)
plot(combined.data$positivity.prop, pch=18, axes=FALSE, xlab=NA, ylab=NA, cex=2, col="darkblue", ylim=c(0,1))
axis(side = 4)
mtext(side = 4, line = 3, 'Proportion of Positive Tweets')
legend("bottomright", legend=c("Bitcoin Price Index", "Positive Tweet Prop"), 
       lty=c(1,0), pch=c(NA, 18), col=c("darkred", "darkblue"), lwd = c(2, NA))


# ##### EXTRA: Transform Data to Differenced Stationary Time Series #####
# 
# differenced.positivity.prop <- diff(combined.data$positivity.prop)
# differenced.price.index <- diff(combined.data$price_index)
# 
# 
# ##### EXTRA: Perform Granger Test #####
# 
# # Price index granger cause positivity proportion?
# grangertest(differenced.positivity.prop ~ differenced.price.index, order = 1)
# 
# # Positivity proportion granger cause price index?
# grangertest(differenced.price.index ~ differenced.positivity.prop, order = 1)
# 
# 
# ##### EXTRA: Visualize Data #####
# 
# par(mar=c(5,5,1,5))
# plot(differenced.price.index, type="l", lwd=2, col="darkred", xlab=NA, ylab="Bitcoin Price Index (USD)")
# par(new = TRUE)
# plot(differenced.positivity.prop, pch=18, axes=FALSE, xlab=NA, ylab=NA, cex=2, col="darkblue")
# axis(side = 4)
# mtext(side = 4, line = 3, 'Proportion of Positive Tweets')
# legend("bottomright", legend=c("Bitcoin Price Index", "Positive Tweet Prop"), 
#        lty=c(1,0), pch=c(NA, 18), col=c("darkred", "darkblue"), lwd = c(2, NA))