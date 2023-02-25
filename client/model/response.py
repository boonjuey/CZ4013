class Response:
    def __init__(self, response_body: map) -> None:
        self.response_body = response_body

    def get_response_body(self) -> map:
        return self.response_body
    
    def set_response_body(self, response_body: map) -> None:
        self.response_body = response_body

    def print_response(self) -> None:
        print('\nResponse from server')
        for k, v in self.response_body.items():
            print(f'{k}: {v}')