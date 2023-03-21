import datetime
from model.request import Request


class GUI:
    def __init__(self):
        self.regex = datetime.datetime.strptime

    def display_menu(self) -> None:
        print('\n============================================')
        print('Welcome to the flight information system')
        print('1. Get flight identifier(s) based on source and destination')
        print('2. Get flight information based on flight identifier')
        print('3. Make reservation')
        print('4. Display all destinations')
        print('5. Add a new flight')
        print('6. Monitor seat availability for a flight')
        print('7. Exit')
        print('============================================\n')

    def display_choice_1(self) -> Request:
        source = input('Please enter the source of the flight: ')
        destination = input('Please enter the destination of the flight: ')
        print('Please wait while we retrieve the flight identifier(s) for you...')
        return Request(0, {'source': source, 'destination': destination})

    def display_choice_2(self) -> Request:
        while True:
            try:
                flight_id = int(
                    input('Please enter the flight identifier of the flight: '))
                break
            except ValueError:
                print('Invalid flight identifier. Please try again.')

        print('Please wait while we retrieve the flight information for you...')
        return Request(1, {'flight_id': flight_id})

    def display_choice_3(self) -> Request:
        while True:
            try:
                flight_id = int(
                    input('Please enter the flight identifier of the flight: '))
                break
            except ValueError:
                print('Invalid flight identifier. Please try again.')

        while True:
            try:
                seats_to_reserve = int(
                    input('Please enter the number of seats to reserve: '))
                break
            except ValueError:
                print('Invalid number of seats. Please try again.')

        print('Please wait while we reserve the seats for you...')
        return Request(2, {'flight_id': flight_id, 'seats_to_reserve': seats_to_reserve})

    def display_choice_4(self) -> Request:
        print('Please wait while we retrieve the list of destinations for you...')
        return Request(3, {})

    def display_choice_5(self) -> Request:
        while True:
            try:
                flight_id = int(
                    input('Please enter the flight identifier of the flight: '))
                break
            except ValueError:
                print('Invalid flight identifier. Please try again.')

        source = input('Please enter the source of the flight: ')
        destination = input('Please enter the destination of the flight: ')

        while True:
            try:
                departure_time = input(
                    'Please enter the departure time of the flight (DD/MM/YYYY HH:MM:SS): ')
                departure_time = int(self.regex(
                    departure_time, '%d/%m/%Y %H:%M:%S').timestamp())
                break
            except ValueError:
                print('Invalid departure time. Please try again.')
            except OSError:
                print('Invalid departure time. Please try again.')

        while True:
            try:
                airfare = float(
                    input('Please enter the airfare of the flight: '))
                break
            except ValueError:
                print('Invalid airfare. Please try again.')

        while True:
            try:
                available_seats = int(
                    input('Please enter the number of seats available on the flight: '))
                
                break
            except ValueError:
                print('Invalid number of seats. Please try again.')
        
        print('Please wait while we add the flight for you...')
        return Request(4, {'flight_id': flight_id, 'source': source, 'destination': destination, 'departure_time': departure_time, 'airfare': airfare, 'available_seats': available_seats})

    def display_choice_6(self) -> Request:
        while True:
            try:
                flight_id = int(
                    input('Please enter the flight identifier of the flight: '))
                duration = int(
                    input('Please enter the monitor interval for this flight in seconds: '))
                break
            except ValueError:
                print('Invalid flight identifier. Please try again.')
        print(f'Monitoring seat availability for flight {str(flight_id)} ...')
        return Request(5, {'flight_id': flight_id, 'duration': duration})

    def display_choice_7(self) -> None:
        print('Thanks for using the application!')
        exit()
