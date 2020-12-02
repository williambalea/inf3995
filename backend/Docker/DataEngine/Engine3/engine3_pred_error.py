import json
import pandas as pd
import matplotlib.pyplot as plt2
import numpy as np
import base64
import pickle
from pathlib import Path


class Engine3_Pred_Error:

	ERROR_JSON_PATH = './tempFiles/error_data_and_graph.json'
	ERROR_GRAPH_PATH = './tempFiles/errorGraph2.png'
	GRAPH_LABEL_ROTATION = '45'
	GRAPH_COLOR = '#D52B1E'
	GRAPH_LABEL_STEP = 500
	
	def get_error_json(self, pred_df):
		#load error json from file or generate it if desn't exist
		myFile = Path(self.ERROR_JSON_PATH)
		if myFile.is_file():
			print('loading error json...')
			json_error = pickle.load(open(myFile, 'rb'))
		else:
			json_error = self.generate_error_json(pred_df)
			pickle.dump(json_error, open(myFile, 'wb'))

		print('getting error json DONE')
		return json_error

	def generate_error_json(self, pred_df):
		print('generating error json...')

		#Prediction accuracy per hour for each station
		predError = pred_df['predictions'].values - pred_df['test_labels'].values
		# Calculate mean absolute percentage error (MAPE)
		mape = 100 * (abs(predError) / pred_df['test_labels'].values)
		# Calculate and display accuracy
		predAccuracy = round(100 - np.mean(mape), 2)
		print('Accuracy per hour for each station:', predAccuracy, '%.')
		
		pred_df = pred_df.groupby(['month', 'day', 'hour']).agg({'predictions': 'sum', 'test_labels': 'sum'})

		#Prediction accuracy per hour (sum of all stations)
		predErrorPerHour = pred_df['predictions'].values - pred_df['test_labels'].values
		#Calculate mean absolute percentage error (MAPE)
		mape = 100 * (abs(predErrorPerHour) / pred_df['test_labels'].values)
		#Calculate and display accuracy
		predAccuracyHour = round(100 - np.mean(mape), 2)
		print('Accuracy hour hour (sum of all stations):', predAccuracyHour, '%.')
		
		
		xAxisDate = []
		for i in range(len(pred_df.index.values)):
			xAxisDate.append( str(pred_df.index.values[i][0])
							+ "-" 
							+ str(pred_df.index.values[i][1]) 
							+ ' ' 
							+ str(pred_df.index.values[i][2])
							+ 'h')
				
		xAxis = pd.Series(xAxisDate)

		graphErrorFile = Path(self.ERROR_GRAPH_PATH)
		if graphErrorFile.is_file():
			pass
		else:
			barWidth = 0.25
			plt2.clf()
			plt2.plot(xAxis, predErrorPerHour,'.',  color=self.GRAPH_COLOR,  label='Prediction predErrorPerHour')
			# Add xticks on the middle of the group bars
			('adding xticks')
			plt2.xticks(xAxis[::self.GRAPH_LABEL_STEP], rotation=self.GRAPH_LABEL_ROTATION)
			plt2.xlabel('Per Dates')
			plt2.ylabel('Prediction predErrorPerHour')
			plt2.title('Prediction predErrorPerHour per Date in 2017')
			plt2.tight_layout()
			plt2.legend()
			plt2.savefig(self.ERROR_GRAPH_PATH)
			#TODO
			plt2.show()
			print('generating error graph DONE', flush=True)

		return self.datatoJSON_error(plt2, xAxis, predErrorPerHour, predAccuracyHour)


	def datatoJSON_error(self, graph, x, y, precision):
		graphString = self.toBase64_error()

		myJson =  '{  "data":{ "precision":[], "time":[], "predErrorPerHour":[] }, "graph":[] }'

		jsonInString = json.loads(myJson)
		jsonInString["data"]["precision"] = precision
		jsonInString["data"]["time"] = x[0:len(x)].tolist()
		jsonInString["data"]["predErrorPerHour"] = y.tolist()
		jsonInString["graph"] = graphString

		return json.dumps(jsonInString)

	def toBase64_error(self):
		with open(self.ERROR_GRAPH_PATH, "rb") as imageFile:
			strg = base64.b64encode(imageFile.read()).decode('utf-8')
			while strg[-1] == '=':
				strg = strg[:-1]
		return strg