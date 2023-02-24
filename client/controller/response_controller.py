from utils.Marshaller import unmarshal

class ResponseController:
    def process_response(self, data: bytes) -> str:
        unmarshalled_response = unmarshal(data[0])
        if unmarshalled_response.get('error'):
            return f'> Error from server: {unmarshalled_response["error"]}'
        elif unmarshalled_response.get('result'):
            return f'> Response from server: {unmarshalled_response["result"]}'
        else:
            return f'> Response from server: {self.format_response(unmarshalled_response)}'
    
    def format_response(self, response: map) -> str:
        str_response = '\n'
        for k, v in response.items():
            str_response += f'{k}: {v} \n'
        return str_response