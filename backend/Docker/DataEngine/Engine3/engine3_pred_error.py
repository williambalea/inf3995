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
	
	
	def get_error_json(self, pred_df):
		#load dataframe from file or generate it if desn't exist
		print('1.getting error json...')
		my_file = Path(self.ERROR_JSON_PATH)
        
		if my_file.is_file():
			print('json error file exists')
			print('1.2.loading json error...')
			# load the model from disk
			json_error = pickle.load(open(my_file, 'rb'))
		else:
			print('json error file does not exist')
			json_error = self.generate_error_json(pred_df)
			print('1.3.saving model...')
			# save the model to disk
			pickle.dump(json_error, open(my_file, 'wb'))
			print('saving model DONE')


		print('getting error json DONE')
		return json_error

	def generate_error_json(self, pred_df):
		print('1.3.1.generation of error json...')

		print('Accuracy 1 TO THE HOUR AND STATION groupby in pred_error -------------------------------------------------------')
		errors_accuracy1 = pred_df['predictions'].values - pred_df['test_labels'].values
		# Calculate mean absolute percentage error (MAPE)
		mape = 100 * (abs(errors_accuracy1) / pred_df['test_labels'].values)# Calculate and display accuracy
		pred_accuracy1 = round(100 - np.mean(mape), 2)
		print('Accuracy:', pred_accuracy1, '%.')
		
		print('1.3.2.loading prediction...')
		print('loading prediction DONE')
		pred_df = pred_df.groupby(['month', 'day', 'hour']).agg({'predictions': 'sum', 'test_labels': 'sum'})

		print('Accuracy 2 TO THE DATE groupby in pred_error -------------------------------------------------------')
		errors_accuracy = pred_df['predictions'].values - pred_df['test_labels'].values
		# Calculate mean absolute percentage error (MAPE)
		mape = 100 * (abs(errors_accuracy) / pred_df['test_labels'].values)# Calculate and display accuracy
		pred_accuracy = round(100 - np.mean(mape), 2)
		print('Accuracy:', pred_accuracy, '%.')
		

		print('1.3.3.calculating prediction errors...')
		errors = pred_df['predictions'].values - pred_df['test_labels'].values

		# print('dataframe lenght: ')
		# print(len(temp_df['predictions']))

		print('1.3.4.getting xAxisDate...')
		xAxisDate = []
		for i in range(len(pred_df.index.values)):
			xAxisDate.append( str(pred_df.index.values[i][0])
							+ "-" 
							+ str(pred_df.index.values[i][1]) 
							+ ' ' 
							+ str(pred_df.index.values[i][2])
							+ 'h')
			# xAxis = pd.Series(xAxisDate)

		xAxis = pd.Series(xAxisDate)
		print('getting xAxisDate DONE')

		print('xAxis (date): ')

		print('Yaxis (errors): ')
		print('error length: ',len(errors))
		# print(errors)


		graph_error_file = Path(self.ERROR_GRAPH_PATH)
				
		if graph_error_file.is_file():
			print('graph error file already exists ')
		else:
			print('graph error file does not exist')
			print('1.3.5.generating error graph...')
			barWidth = 0.25
			plt2.clf()
			plt2.plot(xAxis, errors,'.',  color='#D52B1E',  label='Predictions')
			# Add xticks on the middle of the group bars
			('adding xticks')
			label_step = 500
			plt2.xticks(xAxis[::label_step], rotation='45')
			plt2.xlabel('Per Dates')
			plt2.ylabel('Predictions')
			plt2.title('Prediction Errors per Date in 2017')
			plt2.tight_layout()
			# plt.xaxis.set_minor_locator(MultipleLocator(5))
			# Create legend & Show graphic
			plt2.legend()
			plt2.savefig(self.ERROR_GRAPH_PATH)
			plt2.show()
			print('generating error graph DONE', flush=True)
			# plt.show()


		finalJson = self.datatoJSON_error(plt2, xAxis, errors, pred_accuracy)

		print('generation of error json DONE')
		return finalJson


	def datatoJSON_error(self, graph, x, y, precision):
		print('1.3.6.converting data and graph to JSON...', flush=True)
		graphString = self.toBase64_error()

		myJson =  '{  "data":{ "precision":[], "time":[], "errors":[] }, "graph":[] }'

		o = json.loads(myJson)
		o["data"]["precision"] = precision
		o["data"]["time"] = x[0:len(x)].tolist()
		o["data"]["errors"] = y.tolist()
		o["graph"] = graphString

		# print('Label used: ', flush=True)
		# print(y, flush=True)

		print('converting data and graph to JSON DONE', flush=True)
		return json.dumps(o)


	def toBase64_error(self):
		print('1.3.6.1 converting graph png to base64...', flush=True)
		with open(self.ERROR_GRAPH_PATH, "rb") as imageFile:
			strg = base64.b64encode(imageFile.read()).decode('utf-8')
			while strg[-1] == '=':
				strg = strg[:-1]           
			# print(strg)
		print('converting graph png to base64 DONE', flush=True)
		return strg