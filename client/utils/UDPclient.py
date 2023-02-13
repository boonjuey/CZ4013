import socket
from utils.config import *

class UDP:
    def __init__(self):
        self.client_host = CLIENT_HOST
        self.client_port = CLIENT_PORT

        self.server_host = SERVER_HOST
        self.server_port = SERVER_PORT

        self.run()
    
    def run(self):
        s = socket.socket(family=socket.AF_INET, type=socket.SOCK_DGRAM)

        message = "This is a sample message being sent from the Python client"
        data = ""
        while data != "end":
            s.sendto(str.encode(message), (self.server_host, self.server_port))  # send message
            data = s.recvfrom(UDP_BUF_SIZE) # receive response

            print('Received from server: ', data[0].decode())  # show in terminal

            #s.close()  # close the connection
            break # By right shouldnt break, but it will keep sending the same message, so need change the logic later