class Request:
    def __init__(self, request_type: int, request_body: dict):
        self.request_type = request_type
        self.request_body = request_body

    def get_request_id(self) -> int:
        return self.request_id

    def set_request_id(self, request_id: int) -> None:
        self.request_id = request_id

    def get_request_type(self) -> int:
        return self.request_type

    def set_request_type(self, request_type: int) -> None:
        self.request_type = request_type

    def get_request_body(self) -> dict:
        return self.request_body

    def set_request_body(self, request_body: dict) -> None:
        self.request_body = request_body
    