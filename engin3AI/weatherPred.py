import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestRegressor
from sklearn.tree import export_graphviz
import pydot
import matplotlib.pyplot as plt
import datetime


#ACQUIRE DATA
features = pd.read_csv("temps.csv")

##IDENTIFY ANOMALIES IN DATA
features.describe()

##DATA PREP
#one-hot encode data
features = pd.get_dummies(features)

#Features and Targets and Convert Data to arrays
labels = np.array(features['actual'])
features = features.drop('actual', axis = 1)
feature_list = list(features.columns)
features = np.array(features)

#Training and testing Sets
train_features, test_features, train_labels, test_labels = train_test_split(features, labels, test_size = 0.25, random_state =42)

##ESTABLISH BASELINE
baseline_preds = test_features[:, feature_list.index('average')]
baseline_errors = abs(baseline_preds - test_labels)
print('Average baseline error: ', round(np.mean(baseline_errors), 2))

##TRAIN MODEL
rf = RandomForestRegressor(n_estimators= 1000, random_state = 42)
rf.fit(train_features, train_labels)

##MAKE PREDICTIONS ON THE TESTSET
predictions = rf.predict(test_features)
errors = abs(predictions - test_labels)
print('Mean Absolute Error:', round(np.mean(errors), 2), 'degrees.')

##DETERMINE PERFORMANCE METRICS
mape = 100 * (errors / test_labels)
accuracy = 100 - np.mean(mape)
print('accuracy:', round(accuracy, 2), '%.')

##INTERPRET MODEL AND REPORT RESULTS 
#Visualizing single decision tree
tree = rf.estimators_[5]
export_graphviz(tree, out_file= 'tree.dot', feature_names = feature_list, rounded = True, precision = 1)
(graph, ) = pydot.graph_from_dot_file('tree.dot')
graph.write_png('tree.png')


#Visualizing single decision small tree
rf_small = RandomForestRegressor(n_estimators = 10, max_depth = 3)
rf_small.fit(train_features, train_labels)
tree_small = rf_small.estimators_[5]
export_graphviz(tree_small, out_file= 'small_tree.dot', feature_names = feature_list, rounded = True, precision = 1)
(graph, ) = pydot.graph_from_dot_file('small_tree.dot')
graph.write_png('small_tree.png')
#---

# Get numerical feature importances
importances = list(rf.feature_importances_)
# List of tuples with variable and importance
feature_importances = [(feature, round(importance, 2)) for feature, importance in zip(feature_list, importances)]
# Sort the feature importances by most important first
feature_importances = sorted(feature_importances, key = lambda x: x[1], reverse = True)
# Print out the feature and importances 
[print('Variable: {:20} Importance: {}'.format(*pair)) for pair in feature_importances]
#---

##Setting visualizing chart
#First chart
plt.style.use('fivethirtyeight')
x_values = list(range(len(importances)))
plt.bar(x_values, importances, orientation = 'vertical')
plt.xticks(x_values, feature_list, rotation='vectical')
plt.ylabel('Importance')
plt.xlabel('Varialbe')
plt.title('Variable Importances')

##Plot dataset
#Dates of training values
months = features[:, feature_list.index('month')]
days = features[:, feature_list.index('day')]
years = features[:, feature_list.index('year')]

#List and then convert to datetime object
dates = [str(int(year)) + '-' + str(int(month)) + '-' + str(int(day)) 
for year, month, day in zip(years, months, days) ] 
dates = [datetime.datetime.strptime(date, '%Y-%m-%d') for date in dates]

#Dataframe with true values and dates
true_data = pd.DataFrame(data = {'date': dates, 'actual': labels})

#Dates of predictions
months = test_features[:, feature_list.index('month')]
days = test_features[:, feature_list.index('day')]
years = test_features[:, feature_list.index('year')]

#Column of dates
test_dates = [str(int(year)) + '-' + str(int(month)) + '-' + str(int(day)) 
for year, month, day in zip(years, months, days) ] 

#Convert to datetime objects
test_dates = [datetime.datetime.strptime(date, '%Y-%m-%d') for date in test_dates]

#Dataframe with predictions and dates
predictions_data = pd.DataFrame(data = {'date': test_dates, 'prediction': predictions})

#Plot the predicted values
plt.plot(true_data['date'], true_data['actual'], 'b-', label = 'actual')
plt.plot(predictions_data['date'], predictions_data['prediction'], 'ro', label='prediction')
plt.xticks(rotation='60')
plt.legend()

#Graph labels
plt.xlabel('Date')
plt.ylabel('Maximum Temperature (F)')
plt.title('Actual and Predicted Values')
#---

## Second chart 
#Make the data accessible for plotting
true_data['temp_1'] = features[:, feature_list.index('temp_1')]
true_data['average'] = features[:, feature_list.index('average')]
true_data['friend'] = features[:, feature_list.index('friend')]

#Plot all the data as lines
plt.plot(true_data['date'], true_data['actual'], 'b-', label = 'actual', alpha = 1.0)
plt.plot(true_data['date'], true_data['temp_1'], 'y-', label = 'temp_1', alpha = 1.0)
plt.plot(true_data['date'], true_data['average'], 'k-', label = 'average', alpha = 0.8)
plt.plot(true_data['date'], true_data['friend'], 'r-', label = 'friend', alpha = 0.3)

#Formatting plot
plt.legend()
plt.xticks(rotation = '60')

#Labels and title
plt.xlabel('Date')
plt.ylabel('Maximum Temperature (F)')
plt.title('Actual Max Temps and Variables')