import sys
import socket
import time
import random
from utils.config import *
from view.gui import GUI
from controller.flight_controller import FlightController
from controller.request_controller import RequestController
from controller.response_controller import ResponseController
from model.request import Request

class UDP:
    def __init__(self):
        # self.client_host = CLIENT_HOST
        # self.client_port = CLIENT_PORT
        if len(sys.argv) == 3:
            self.server_host = sys.argv[1]
            self.server_port = int(sys.argv[2])
        else:
            print("Please enter the correct number of arguments for the server host and port.")
            sys.exit(1)

        self.timeout = TIMEOUT
        self.max_retries = MAX_RETRIES
        self.drop_rate = DROP_RATE

        self.run()
    
    def run(self):
        s = socket.socket(family=socket.AF_INET, type=socket.SOCK_DGRAM)
        s.settimeout(self.timeout)

        data = ""
        flight_controller = FlightController(GUI())
        request_controller = RequestController()
        response_controller = ResponseController()

        while data != "end":
            response_received = False
            request = flight_controller.display()
            marshalled_request = request_controller.prepare_request(request)
            for i in range(self.max_retries + 1):
                try:
                    if response_received:
                        break
                    
                    if random.random() <= self.drop_rate:
                        print("Request message dropped")
                        continue
                    s.sendto(marshalled_request, (self.server_host, self.server_port)) # send message
                    
                    #need to have another recvfrom so its able to listen for updates. 
                    data = s.recvfrom(UDP_BUF_SIZE) # receive response
                    response = response_controller.process_response(data)

                    #if type is subscription succes then I need to start a new thread that actively listens out for subscription 
                    #thread is sth like while(time.Now() < startTime + duration): s.recvfrom.....
                    #once loops ends, end the thread. 
                    response.print_response()
                    #depending on marshalled request,  
                    # if request type == 5, i will check the start time for this request 
                    res = response.get_response_body()
                    response_received = True
                    if res["requestType"] == "5":
                        start = time.time()
                        s.settimeout(int(res["duration"]))
                        while((start+ int(res["duration"])) >= time.time()):
                            data = s.recvfrom(UDP_BUF_SIZE) # receive response
                            response = response_controller.process_response(data)   
                            response.print_response() 
                        raise socket.timeout   
                           
                    s.settimeout(TIMEOUT) 

                    break
                except socket.timeout:
                    if request.get_request_type()==5:
                        request = Request(6, {'flight_id': int(res["flightId"]), 'duration': int(res["duration"])})
                        marshalled_request = request_controller.prepare_request(request)
                        s.sendto(marshalled_request, (self.server_host, self.server_port)) # send message
                        data = s.recvfrom(UDP_BUF_SIZE) # receive response
                        response = response_controller.process_response(data)   
                        response.print_response()
                    elif i != self.max_retries:
                        print("Request timed out. Retrying...")
                    else:
                        print("Request timed out.")

                    s.settimeout(TIMEOUT)
            if not response_received:
                print("Request failed. Please try again later.")

        s.close()  # close the connection