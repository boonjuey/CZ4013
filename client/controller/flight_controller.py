from model.request import Request

class FlightController:
    def __init__(self, view):
        self.view = view

    def display(self) -> Request:
        request = None
        while True:
            self.view.display_menu()

            choice = 0
            while choice not in range(1, 8):
                try:
                    choice = int(input('Enter your choice: '))
                    if choice not in range(1, 8):
                        raise ValueError
                except ValueError:
                    print('Invalid choice. Please try again.')

            if choice == 1:
                request = self.view.display_choice_1()
            elif choice == 2:
                request = self.view.display_choice_2()
            elif choice == 3:
                request = self.view.display_choice_3()
            elif choice == 4:
                request = self.view.display_choice_4()
            elif choice == 5:
                request = self.view.display_choice_5()            
            elif choice == 6:
                request = self.view.display_choice_6()
            elif choice == 7:
                self.view.display_choice_7()
            break
        return request
        