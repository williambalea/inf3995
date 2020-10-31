import requests
from click._compat import raw_input
from dotenv import load_dotenv

API_KEY = "AIzaSyAuBN13-xxTgsWTV6SUdJbaJ61iWW4Tnoc"

URL = "https://maps.googleapis.com/maps/api/staticmap?"
LONGITUDE = "-73.57264"
LATITUDE ="45.52689"
center = "{},{}".format(LATITUDE, LONGITUDE)
zoom = 18

r =requests.get(URL + 'center='+center+'&format=png'+ '&markers=color:blue%7Clabel:S%7C11211'+'&zoom='+str(zoom)+'&size=1024x768&key='+API_KEY)

print(URL + 'center='+center+'&format=png'+ '&markers=color:blue%7Clabel:S%7C11211'+'&zoom='+str(zoom)+'&size=1024x768&key='+API_KEY)