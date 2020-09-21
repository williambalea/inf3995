import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestRegressor


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
