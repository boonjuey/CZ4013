package invocationSemantics;

import java.util.List;

import controller.RequestController;
import controller.ResponseController;

import java.util.ArrayList;

import errors.DuplicateFlightIdException;
import errors.DuplicateRequestException;
import errors.InsufficientSeatsException;
import errors.InvalidFlightIdException;
import errors.NoFlightFoundException;
import model.Request;

public class AtMostOnce extends InvocationSemantics {
    private List<Request> history = new ArrayList<>();

    public AtMostOnce(RequestController requestController, ResponseController responseController) {
        super(requestController, responseController);
    }

    @Override
    public Object processRequest(Request request) throws DuplicateFlightIdException, DuplicateRequestException,
    InsufficientSeatsException, InvalidFlightIdException, NoFlightFoundException  {
        for (Request r : history) {
            if (r.getRequestId() == request.getRequestId()
                    && r.getClientAddress().equals(request.getClientAddress())
                    && r.getClientPort() == request.getClientPort()) {
                throw new DuplicateRequestException();
            }
        }
        history.add(request);
        return super.processRequest(request);
    }
}
