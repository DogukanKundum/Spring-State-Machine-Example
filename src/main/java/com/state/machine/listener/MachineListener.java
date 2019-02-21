package com.state.machine.listener;

import com.state.machine.review.Events;
import com.state.machine.review.States;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

/**
 * Created by Dogukan Kundum on 21 Feb 2019.
 */
@Slf4j
public class MachineListener implements StateMachineListener<States, Events> {

    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void stateChanged(State<States, Events> from, State<States, Events> to) {
        LOGGER.info("State changed from {} to {}", getStateInfo(from), getStateInfo(to));
    }

    @Override
    public void stateEntered(State<States, Events> state) {
        LOGGER.info("Entered state {}", getStateInfo(state));
    }

    @Override
    public void stateExited(State<States, Events> state) {
        LOGGER.info("Exited state {}", getStateInfo(state));
    }

    @Override
    public void eventNotAccepted(Message<Events> message) {
        LOGGER.error("Event not accepted: {}", message.getPayload());
    }

    @Override
    public void transition(Transition<States, Events> transition) {

    }

    @Override
    public void transitionStarted(Transition<States, Events> transition) {

    }

    @Override
    public void transitionEnded(Transition<States, Events> transition) {

    }

    @Override
    public void stateMachineStarted(StateMachine<States, Events> stateMachine) {
        LOGGER.info("Machine started: {}", stateMachine);
    }

    @Override
    public void stateMachineStopped(StateMachine<States, Events> stateMachine) {
        LOGGER.info("Machine stopped: {}", stateMachine);
    }

    @Override
    public void stateMachineError(StateMachine<States, Events> stateMachine, Exception e) {
        LOGGER.error("Machine error: {}", stateMachine);
    }

    @Override
    public void extendedStateChanged(Object key, Object value) {
        LOGGER.info("Extended state changed: [{}: {}]", key, value);
    }

    @Override
    public void stateContext(StateContext<States, Events> stateContext) {
        LOGGER.info("stateContext: [{}: {}]", stateContext.getStateMachine().getId(), stateContext.getStateMachine().getState());
    }

    public static String getStateInfo(State<States, Events> state) {
        return state != null ? state.getId().name() : "EMPTY STATE";
    }

    public static String getTransitionInfo(Transition<States, Events> t) {
        return String.format("[%s: %s]",
                             t.getSource() != null ? t.getSource().getId() : "EMPTY",
                             t.getTarget() != null ? t.getTarget().getId() : "EMPTY"
        );
    }

}