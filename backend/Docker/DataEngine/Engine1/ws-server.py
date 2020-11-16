import asyncio
import websockets

async def response(websocket, path):
    print("Connected to engine1")
    await websocket.recv()

start_server = websockets.serve(response, 'localhost', 3000)
asyncio.get_event_loop().run_until_complete(start_server)
asyncio.get_event_loop().run_forever()