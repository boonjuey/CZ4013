import socket
from utils.config import *
from view.gui import GUI
from controller.flight_controller import FlightController
from controller.request_controller import RequestController
from controller.response_controller import ResponseController
class UDP:
    def __init__(self):
        self.client_host = CLIENT_HOST
        self.client_port = CLIENT_PORT

        self.server_host = SERVER_HOST
        self.server_port = SERVER_PORT

        self.timeout = TIMEOUT
        self.max_retries = MAX_RETRIES

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
            
            for _ in range(self.max_retries + 1):
                try:
                    if response_received:
                        break
                    
                    s.sendto(marshalled_request, (self.server_host, self.server_port)) # send message
                    
                    data = s.recvfrom(UDP_BUF_SIZE) # receive response
                    response = response_controller.process_response(data)
                    response.print_response()
                    response_received = True
                    break
                except socket.timeout:
                    print("Request timed out. Retrying...")
            if not response_received:
                print("Request failed. Please try again later.")

        s.close()  # close the connection