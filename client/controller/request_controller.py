import model.request as Request
from utils.Marshaller import marshal

class RequestController:
    def __init__(self):
        self.request_id = 0

    def get_request_id(self) -> int:
        request_id = self.request_id
        self.request_id += 1
        return request_id

    def set_request_id(self, request_id: int) -> None:
        self.request_id = request_id

    def prepare_request(self, request: Request) -> bytearray:
        request.set_request_id(self.get_request_id())
        return marshal(self.request_obj_to_map(request))

    def request_obj_to_map(self, request: Request) -> map:
        request_map = {
            "request_id": request.get_request_id(),
            "request_type": request.get_request_type(),
        }
        for key, value in request.get_request_body().items():
            request_map[key] = value
        return request_map