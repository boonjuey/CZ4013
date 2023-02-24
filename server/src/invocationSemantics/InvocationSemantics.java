package invocationSemantics;

import java.net.DatagramPacket;

import controller.RequestController;
import controller.ResponseController;
import errors.DuplicateFlightIdException;
import errors.InsufficientSeatsException;
import errors.InvalidFlightIdException;
import errors.NoFlightFoundException;
import model.Request;

public abstract class InvocationSemantics {
    protected RequestController requestController;
    protected ResponseController responseController;

    public InvocationSemantics(RequestController requestController, ResponseController responseController) {
        this.requestController = requestController;
        this.responseController = responseController;
    }

    public Request getRequest(DatagramPacket packet) {
        return requestController.getRequest(packet);
    }

    public Object processRequest(Request request) throws DuplicateFlightIdException,
            InsufficientSeatsException, InvalidFlightIdException, NoFlightFoundException {
        return requestController.processRequest(request);
    }

    public DatagramPacket sendResponse(Request request, Object result, Exception exception) {
        return responseController.sendResponse(request, result, exception);
    }
}
