package invocationSemantics;

import java.util.Map;

import controller.RequestController;
import controller.ResponseController;

import java.util.HashMap;

import errors.DuplicateFlightIdException;
import errors.InsufficientSeatsException;
import errors.InvalidFlightIdException;
import errors.NoFlightFoundException;
import model.Request;

public class AtMostOnce extends InvocationSemantics {
    private Map<Request, Object> history = new HashMap<>();

    public AtMostOnce(RequestController requestController, ResponseController responseController) {
        super(requestController, responseController);
    }

    @Override
    public Object processRequest(Request request) throws DuplicateFlightIdException,
            InsufficientSeatsException, InvalidFlightIdException, NoFlightFoundException {
        for (Request r : history.keySet()) {
            if (r.getRequestId() == request.getRequestId()
                    && r.getClientAddress().equals(request.getClientAddress())
                    && r.getClientPort() == request.getClientPort()) {
                return history.get(r);
            }
        }
        Object result = super.processRequest(request);
        history.put(request, result);
        return result;
    }
}
