package controller;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.HashMap;
import model.Subscriber;
import model.Subscription;
import errors.DuplicateSubscriberException;
import errors.NoSubscriptionFoundException;

public class CallbackController {

    //maintain a list of subscribers upon startup of server
    private ConcurrentHashMap<Integer, ArrayList<Subscriber>> subscribersByFlight;

    public CallbackController(){
        this.subscribersByFlight = new ConcurrentHashMap<Integer, ArrayList<Subscriber>>();
    }
    //For subscription 
    public Subscription addSubscriber(int flightId, Subscriber newSubscriber) throws DuplicateSubscriberException{
        //subscription already exists
        //if flight in hashmap
        
        if (subscribersByFlight.containsKey(flightId)){
            System.out.printf("Flight exists %d \n", flightId);
            for(Subscriber subscriber: subscribersByFlight.get(flightId)){
                if((subscriber.getAddress().getHostAddress()).matches(newSubscriber.getAddress().getHostAddress()) && 
                (subscriber.getPort())== newSubscriber.getPort()){
                    System.out.println(subscriber.getAddress());
                    throw new DuplicateSubscriberException();
                }
            }
            ArrayList<Subscriber> subscribers = (ArrayList<Subscriber>) subscribersByFlight.get(flightId);
            subscribers.add(newSubscriber);   
            subscribersByFlight.put(flightId, subscribers);
            System.out.printf("Subscription added %d \n", flightId);
        }
        else{
            
            System.out.printf("Flight %d  doesn't exists \n", flightId);
            System.out.println(newSubscriber);
            ArrayList<Subscriber> subscribers = new ArrayList<Subscriber>(); 
            subscribers.add(newSubscriber);
            subscribersByFlight.put(flightId, subscribers);
            System.out.printf("Subscription added %d \n", flightId);
        }
        return new Subscription(flightId, newSubscriber.getDuration());
    }

    public Integer removeSubscriber(int flightId, Subscriber subscriber) throws NoSubscriptionFoundException{
        System.out.printf("Remove subscriber %d \n", flightId);
        System.out.println(subscriber);
        if(!subscribersByFlight.containsKey(flightId)){
            throw new NoSubscriptionFoundException();
        }
        boolean found = false;
        Subscriber removedSubscriber = null;
        ArrayList<Subscriber> subscribers = (ArrayList<Subscriber>) subscribersByFlight.get(flightId);
        System.out.println(subscribers.size());
        for(int i=0; i < subscribers.size(); i++ ){
            System.out.printf("%d \n", subscribers.size());
            System.out.print(subscribers.get(i));
            if(((subscribers.get(i)).getAddress().getHostAddress()).matches(subscriber.getAddress().getHostAddress()) && 
            (subscribers.get(i).getPort()== subscriber.getPort())){
                found = true; 
                removedSubscriber = subscribers.get(i);
                subscribers.remove(subscribers.get(i));
                subscribersByFlight.put(flightId, subscribers);
                
            }
        }
        if(!found){
            System.out.println("not found");
            throw new NoSubscriptionFoundException();
        }
        System.out.println(removedSubscriber);
        return new Integer(flightId); 
    }
    // public void removeSubscriber(int flightId,  ) throws NoSubscriptionFoundException{
    //     System.out.printf("Remove subscriber \n", flightId);
    //     if(!subscribersByFlight.containsKey(flightId) || !subscribersByFlight.get(flightId).contains(removedSubscriber)){
    //         throw new NoSubscriptionFoundException();
    //     }
    //     ArrayList<Subscriber> subscribers = (ArrayList<Subscriber>) subscribersByFlight.get(flightId);
    //     subscribers.remove(removedSubscriber);
    //     subscribersByFlight.put(flightId, subscribers);
    //     System.out.println(subscribersByFlight);
    // }

    public ArrayList<Subscriber> getSubscribersByFlightId(int id){
        return subscribersByFlight.get(id);
    }

    public int getSubscriberSizeByFlightId(int id){
        if(subscribersByFlight.get(id)!= null){
            return subscribersByFlight.get(id).size();
        }
        else{
            return 0;
        }
        
    }

}
