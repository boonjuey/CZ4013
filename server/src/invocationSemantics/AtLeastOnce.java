package invocationSemantics;

import controller.RequestController;
import controller.ResponseController;

public class AtLeastOnce extends InvocationSemantics {
    public AtLeastOnce(RequestController requestController, ResponseController responseController) {
        super(requestController, responseController);
    }
}
