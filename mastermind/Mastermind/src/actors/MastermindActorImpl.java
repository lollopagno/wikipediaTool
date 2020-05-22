package actors;

import akka.actor.AbstractActor;

public abstract class MastermindActorImpl extends AbstractActor implements MastermindActor {
    @Override
    public abstract Receive createReceive();

    protected void log(String message){
        synchronized (System.out){
            System.out.println(message);
        }
    }
}
