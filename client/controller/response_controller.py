from utils.Marshaller import unmarshal
from model.response import Response

class ResponseController:
    def process_response(self, data: bytes) -> Response:
        unmarshalled_response = unmarshal(data[0])
        return Response(unmarshalled_response)